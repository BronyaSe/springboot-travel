package com.bronya.travel.Entity.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RouteCommentPageDTO {
    private int id;
    private String content;
    private BigDecimal rating;
    private String username;
    private String avatar;
    private LocalDateTime createdAt;
}
