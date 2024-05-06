package com.linkbrary.domain.link.controller;
import com.linkbrary.common.response.ApiResponse;
import com.linkbrary.domain.link.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.linkbrary.common.util.CallExternalApi.callExternalGetApi;
import static com.linkbrary.common.util.CallExternalApi.sendNotification;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final LinkService linkService;

    @GetMapping("/test/external-api")
    public ResponseEntity<String> testExternalApi(@RequestParam String url, @RequestParam int mode) {
        try {
            String response = callExternalGetApi(url,mode);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "디렉토리 api test용 api")
    @GetMapping("/test/directory")
    public String getLink() {
        return linkService.getDirectory();
    }

    @Operation(summary = "벡터 서치 test용 api")
    @GetMapping("/test/vector-search")
    public ApiResponse getVectorSearch() {
        return ApiResponse.onSuccess(linkService.testVector());
    }

    @Operation(summary = "임베딩 추출")
    @GetMapping("/test/keyword-embedding")
    public ApiResponse getKeywordEmbedding(@RequestParam String url) {
        return ApiResponse.onSuccess(linkService.testEmbedding(url));
    }

    @Operation(summary = "푸시알람 테스트")
    @GetMapping("/test/push-notification")
    public ApiResponse testPushNotification(@RequestParam String url) {
        sendNotification("ExponentPushToken[h-7qDIFbPYHd9z1FLJaUy2]", "test용", "test용 바디", url);
        return ApiResponse.onSuccess("성공");
    }

}
