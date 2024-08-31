package com.application.sushi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.sushi.domain.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {
    Food save (Food food);
}
