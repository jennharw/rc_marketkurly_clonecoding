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
public class ReviewReq {
    private int reviewId;
    private int orderId;
    private int itemId;
    private String title;
    private String url;
    private String description;
}
