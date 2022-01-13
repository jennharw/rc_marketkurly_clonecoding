package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Text;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRes {
    private int reviewId;
    private int orderId;
    private String title;
    private int userId;
    private String url;

    private String description;
    private int itemId;
}
