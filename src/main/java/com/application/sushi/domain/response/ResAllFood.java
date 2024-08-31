package com.application.sushi.domain.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

import com.application.sushi.domain.Food;

@Getter
@Setter
public class ResAllFood {
    private List<Food> allFoods;
    private int totalPage;
    private long totalElement;
    private int numberOfElement;
}
