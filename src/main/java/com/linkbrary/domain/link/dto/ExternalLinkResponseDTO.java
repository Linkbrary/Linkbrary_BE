package com.linkbrary.domain.link.dto;

import lombok.Data;
import java.util.List;

@Data
public class ExternalLinkResponseDTO {
    private String title;
    private String url;
    private String summary;
    private String content;
    private String thumbnail;
    private float[] embed;
}
