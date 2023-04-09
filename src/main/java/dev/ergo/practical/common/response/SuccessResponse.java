package dev.ergo.practical.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResponse<T> {
    private String message;
    private T data;
}
