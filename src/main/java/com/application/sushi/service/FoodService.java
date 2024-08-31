package com.application.sushi.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.application.sushi.domain.Food;
import com.application.sushi.domain.response.ResAllFood;
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
    public ResAllFood fetchAllFood(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Food> foodPage = this.foodRepository.findAll(pageable);
        List<Food> allFood = foodPage.getContent();
        ResAllFood resAllFood = new ResAllFood();
        resAllFood.setTotalPage(foodPage.getTotalPages());
        resAllFood.setTotalElement(foodPage.getTotalElements());
        resAllFood.setNumberOfElement(foodPage.getNumberOfElements());
        resAllFood.setAllFoods(allFood);
        return resAllFood;
    }
    public Food fetchFoodById(long id){
        return this.foodRepository.findById(id);
    }

    public void deleteFood(long id){
        this.foodRepository.deleteById(id);
    }
}