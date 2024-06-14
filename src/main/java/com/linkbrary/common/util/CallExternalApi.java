package com.linkbrary.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkbrary.common.response.code.ErrorCode;
import com.linkbrary.common.response.exception.handler.UserLinkHandler;
import com.linkbrary.domain.link.dto.DirectoryInfo;
import com.linkbrary.domain.link.service.LinkService;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class CallExternalApi {
    private static final String EXTERNAL_API_URL = "http://3.37.89.171:8000";
    private static final Logger logger = LoggerFactory.getLogger(LinkService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String PUSH_NOTIFICATION_URL = "https://exp.host/--/api/v2/push/send?useFcmV1=true";
    public static String callExternalGetApi(String url, int mode) {
        switch (mode) {
            case 1:
                return handleGetRequest(url, "/crawl/");
            case 2:
                return handleGetRequest(url, "/linkbrary/");
            default:
                throw new IllegalArgumentException("Invalid mode: " + mode);
        }
    }

    private static String handleGetRequest(String url, String endpoint) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8);
            String requestUrl = EXTERNAL_API_URL + endpoint + encodedUrl;
            HttpGet request = new HttpGet(requestUrl);
            request.addHeader("Accept", "application/json");
            HttpClientResponseHandler<String> responseHandler = createResponseHandler();
            return httpClient.execute(request, responseHandler);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message != null && message.contains("Unexpected response status: 451")) {
                throw new UserLinkHandler(ErrorCode.LINK_NOT_ALLOWED);
            } else {
                throw new UserLinkHandler(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Retryable(value = ClientProtocolException.class, maxAttempts = 3 , backoff = @Backoff(delay = 1000))
    public static DirectoryInfo handleDirectoryRecommendationPostRequest(String content, String directory, String endpoint) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            content = escapeSpecialCharacters(content);
            directory = escapeSpecialCharacters(directory);
            System.out.println(content);
            String requestUrl = EXTERNAL_API_URL + endpoint;
            HttpPost postRequest = new HttpPost(requestUrl);
            postRequest.addHeader("Accept", "application/json");
            postRequest.addHeader("Content-Type", "application/json");
            // Create the JSON payload
            String jsonPayload = String.format("{\"directory\": \"%s\", \"content\": \"%s\"}", directory, content);
            // Set the payload
            StringEntity entity = new StringEntity(jsonPayload, StandardCharsets.UTF_8);
            postRequest.setEntity(entity);
            HttpClientResponseHandler<String> responseHandler = createResponseHandler();
            String jsonResponse = httpClient.execute(postRequest, responseHandler);
            return parseDirectoryInfo(jsonResponse);
        } catch (Exception e) {
            logger.error("HTTP POST request failed", e);
            throw new UserLinkHandler(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public static float[] handleEmbeddingPostRequest(String keyword) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String requestUrl = EXTERNAL_API_URL + "/embedding";
            HttpPost postRequest = new HttpPost(requestUrl);
            postRequest.addHeader("Accept", "application/json");
            postRequest.addHeader("Content-Type", "application/json");
            // Create the JSON payload
            String jsonPayload = String.format("{\"contents\": \"%s\"}", keyword);
            // Set the payload
            StringEntity entity = new StringEntity(jsonPayload, StandardCharsets.UTF_8);
            postRequest.setEntity(entity);
            HttpClientResponseHandler<String> responseHandler = createResponseHandler();
            String jsonResponse = httpClient.execute(postRequest, responseHandler);
            return parseEmbedding(jsonResponse, "embedded_contents");
        } catch (Exception e) {
            String message = e.getMessage();
            if (message != null && message.contains("Unexpected response status: 451")) {
                throw new UserLinkHandler(ErrorCode.LINK_NOT_ALLOWED);
            } else {
                throw new UserLinkHandler(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private static HttpClientResponseHandler<String> createResponseHandler() {
        return response -> {
            int status = response.getCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
    }

    private static DirectoryInfo parseDirectoryInfo(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            String directory = rootNode.path("directory").asText();
            if (directory.contains("디렉토리의 이름")){
                throw new RuntimeException();
            }
            String idStr = rootNode.path("id").asText();
            directory = directory.replaceAll("^<|>$", "");
            idStr = idStr.replaceAll("^<|>$", "");
            System.out.println(jsonResponse);
            Long id = "null".equals(idStr) ? null : Long.parseLong(idStr);
            // Return DirectoryInfo object
            return DirectoryInfo.builder().name(directory).id(id).build();
        } catch (Exception e) {
            logger.error("Failed to parse directory info", e);
            throw new UserLinkHandler(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public static float[] parseEmbedding(String jsonResponse, String key) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode embedNode = rootNode.get(key);
            float[] vectorEmbedding = new float[3072];
            if (embedNode != null && embedNode.isArray()) {
                for (int i = 0; i < embedNode.size(); i++) {
                    vectorEmbedding[i] = (float) embedNode.get(i).asDouble();
                }
            }
            return vectorEmbedding;
            // Return DirectoryInfo object
        } catch (Exception e) {
            logger.error("Failed to parse directory info", e);
            throw new UserLinkHandler(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public static String escapeSpecialCharacters(String input) {
        if (input == null) {
            return null;
        }
        StringBuilder escapedInput = new StringBuilder();
        for (char c : input.toCharArray()) {
            switch (c) {
                case '\n':
                    escapedInput.append("\\n");
                    break;
                case '\t':
                    escapedInput.append("\\t");
                    break;
                case '\r':
                    escapedInput.append("\\r");
                    break;
                case '\\':
                    escapedInput.append("\\\\");
                    break;
                case '\"':
                    escapedInput.append("\\\"");
                    break;
                case '\'':
                    escapedInput.append("\\'");
                    break;
                default:
                    escapedInput.append(c);
                    break;
            }
        }
        return escapedInput.toString();
    }

    public static void sendNotification(String token, String title, String body, String data) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost postRequest = new HttpPost(PUSH_NOTIFICATION_URL);
            postRequest.addHeader("Accept", "application/json");
            postRequest.addHeader("Content-Type", "application/json");
            //postRequest.addHeader("Authorization", "Bearer ya29.c.c0AY_VpZivWrjiQa1rOCDROhWlc0R_48UWAseC1XsZuHt1K-YdHJIZYeaGOw-QotAuxfPDP1xlmCljcnkyxUDla_2ye0_-84iHB7aiqz5bPv-bN5UiqhRieMuOPyTcrduhHCwNFts0x-8tz5cZp9sjiCz7AnH9TCTj4DqfF-GFy-2RTbrXOi6D9mOlvCvKSnxoFlgUXcL2ts4M6jyhLKNXSFF1SH9kMf6ZJwqccma5PD42CwURUZq2-1CpImW96fcgz67DU3DESNN5L5KrCuLFtVBKXHCqkX0wcTRfIa8U1RqfwaPPVZCPMDjboQjrF8ysE6WIuo4mvDTCv4_UItUC9rbxZGcchRA7wneyGKmTVsJnBhphXOvIn-szH385AlpqM9blmXn7bnMn59Ydji74dbxXBqx_r5XSpn1t4xc1yWUBQRpBe4Xhd9xZ4bQFdjxrwbwkwi57r6OutU6hJzbi69oYO7MZsv7SYMsz7OZS5I5nFmn2Q96u8MuU7kQ5vnoprtntpBbI0kjQR_gxaSdue0aMY-l82Xvb5BQ_4XczeSz6j_3BVygqfUswRkRl_-fktOWRtJY4WxoyrolnqZ0Fktq26WkZ2Ze5a59MkI6th5wwq9-xBBhvkBnZpSWcwdof0MsshpdVYUMVY8UucSI02WuFFUYjS-yFaUhs8_91gktWl-VlV2d_2vBj4341MW_mUI2jvO4ymF42IxVbgFzqR8zmcfO7az-BumQRiFvlBqV6dX6enesmjQZ6JhJ2fszBislfqbswtYumVFrYlcor9Jb_jgylaJB0Mvw8ZUi7QlvYby_r31sdJOWZXSclpQFk_bjjYJhRJSgbIFIfUy18Os75oxsrxn7YpyYFexvl6Uwbix-BgRU5blV7zhJXs1Rnt4sv909tVQ92_FW5ggJyf1y2l9dcnqfUpxnaQ4gyJIejaBmSkZjXhh9k64SvmnIyii2jf9Wq5tvtnRn2_gYlaUu5xrztbZn4wp1qXaiYwk72amMYhSm3fef");
            System.out.println(token);
            String jsonPayload = String.format(
                    "{\"to\":\"%s\",\"sound\":\"default\",\"title\":\"%s\",\"body\":\"%s\",\"data\":{\"directory\":\"%s\"}}",
                    token, title, body, data
            );
            StringEntity entity = new StringEntity(jsonPayload, StandardCharsets.UTF_8);
            postRequest.setEntity(entity);
            HttpClientResponseHandler<String> responseHandler = createResponseHandler();
            String responseBody = httpClient.execute(postRequest, responseHandler);
            System.out.println("Response: " + responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
