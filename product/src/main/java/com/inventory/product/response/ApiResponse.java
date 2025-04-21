package com.inventory.product.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {

    private String message;
    private int status;
    private LocalDateTime timestamp;
    private T data;

}
