package me.strong.hateoas.common.application.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by taesu on 2018-06-19.
 */
@Getter
@NoArgsConstructor
public class ApiResponse<T> {
    private T result;
    private String message;
    private ErrorCode errorCode;

    public ApiResponse(T result, String message) {
        this.result = result;
        this.message = message;
    }

    public ApiResponse(T result, String message, ErrorCode errorCode) {
        this.result = result;
        this.message = message;
        this.errorCode = errorCode;
    }

    public static <T> ApiResponse<T> fromSuccessResult(T result) {
        return ApiResponse.fromSuccessResult(result, "Success");
    }

    public static <T> ApiResponse<T> fromSuccessResult(T result, String message) {
        return new ApiResponse(result, message);
    }

    public static <T> ApiResponse<T> fromErrorResponse(String errorMessage) {
        return ApiResponse.fromErrorResponse(null, errorMessage);
    }

    public static <T> ApiResponse<T> fromErrorResponse(T result, String errorMessage) {
        return ApiResponse.fromErrorResponse(result, errorMessage, ErrorCode.UNKNOWN);
    }

    public static <T> ApiResponse<T> fromErrorResponse(T result, String errorMessage, ErrorCode errorCode) {
        return new ApiResponse(result, errorMessage, errorCode);
    }
}
