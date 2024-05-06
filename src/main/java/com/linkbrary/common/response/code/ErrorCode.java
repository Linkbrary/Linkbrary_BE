package com.linkbrary.common.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseErrorCode {

    // 가장 일반적인 응답
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "COMMON405", "지원하지 않는 미디어 파일입니다."),
    IMAGE_IS_NULL(HttpStatus.BAD_REQUEST, "COMMON406", "이미지 파일은 null이면 안됩니다."),

    // 멤버 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    MEMBER_LOGIN_FAILURE(HttpStatus.BAD_REQUEST, "MEMBER4003", "아이디 혹은 비밀번호를 잘못 입력하였습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수 입니다."),
    MEMBER_SIGNUP_ERROR(HttpStatus.BAD_REQUEST, "SIGNUP4001", "회원가입 유효성 검사 실패"),
    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "SIGNUP4002", "이미 존재하는 이메일입니다."),
    //링크 관련 에러
    LINK_NOT_FOUND(HttpStatus.NOT_FOUND, "LINK4041", "디비에 저장된 링크가 존재하지 않습니다."),
    LINK_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "LINK4001", "해당 링크는 지원되지 않습니다."),
    LINK_ALREADY_EXIST(HttpStatus.BAD_REQUEST,"LINK4002","해당 링크는 이미 저장되어 있습니다."),

    //디렉토리 관련 에러
    DIRECTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "DIRECTORY4041", "해당 디렉토리가 존재하지 않습니다."),
    USER_REMINDER_SETTING_NOT_FOUND(HttpStatus.NOT_FOUND,"REMINDER4041", "유저의 리마인더 설정이 존재하지 않습니다."),

    //리마인더 관련 에러
    REMINDER_ALREADY_EXIST(HttpStatus.BAD_REQUEST,"REMINDER4001","이미 리마인더가 존재합니다.")
    ,
    REMINDER_NOT_FOUND(HttpStatus.NOT_FOUND,"REMINDER4041", "해당 리마인더가 존재하지 않습니다."),

    //API call 관련 에러
    API_GET_CALL_MODE_ERROR(HttpStatus.BAD_REQUEST, "API4001","해당 모드는 존재하지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
