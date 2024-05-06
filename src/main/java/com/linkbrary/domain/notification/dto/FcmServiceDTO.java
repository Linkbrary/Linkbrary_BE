package com.linkbrary.domain.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FcmServiceDTO {
    private String username;
    private Long contentId;
    private NotificationType type;
    private String title;
    private String content;

    public static FcmServiceDTO of(String username, Long contentId, NotificationType type, String title, String content){
        FcmServiceDTO dto = new FcmServiceDTO();
        dto.username = username;
        dto.contentId = contentId;
        dto.type = type;
        dto.title = title;
        dto.content = content;
        return dto;
    }
}
