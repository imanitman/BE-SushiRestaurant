package com.application.sushi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.sushi.domain.Food;
import java.util.List;
public interface FoodRepository extends JpaRepository<Food, Long> {
    Food save (Food food);
    List<Food> findAll();
    Food findById(long id);
    void deleteById(long id);
}
