package com.application.sushi.service;

import org.springframework.stereotype.Service;

import com.application.sushi.domain.Food;
import com.application.sushi.repository.FoodRepository;

@Service
public class FoodService {
    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository){
        this.foodRepository = foodRepository;
    }
    public Food createNewFood(Food food){
        return this.foodRepository.save(food);
    }
}
