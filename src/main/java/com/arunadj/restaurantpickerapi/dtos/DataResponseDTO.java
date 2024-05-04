package com.arunadj.restaurantpickerapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataResponseDTO<T> {
    private String status;
    private String message;

    private T data;
}
