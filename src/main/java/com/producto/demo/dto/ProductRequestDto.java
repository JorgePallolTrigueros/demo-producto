package com.producto.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @PositiveOrZero
    private double price;
    @NotEmpty
    private String category;
    @PositiveOrZero
    private BigDecimal quantity;
}
