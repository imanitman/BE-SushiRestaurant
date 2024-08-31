package com.application.sushi.domain.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqFoodDto {
    private String name;
    private String price;
    private String description;
    private MultipartFile file;
}
