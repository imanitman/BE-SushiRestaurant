package com.application.sushi.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.application.sushi.domain.Food;
import com.application.sushi.service.FoodService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
public class FoodController {
    private final FoodService foodService;
    private final String urlImage = "D://SideProject/shushi/image/food/";

    public FoodController(FoodService foodService){
        this.foodService = foodService;
    }
    @PostMapping("/chef/food")
    public ResponseEntity<String> createNewFood(@RequestParam ("name") String name, @RequestParam ("price") String price,@RequestParam("description") String description,
    @RequestParam ("image") MultipartFile image, @RequestParam("quantity") int quantity) throws IOException {
        Food newFood = new Food();
        if (name != null && price != null){
            newFood.setName(name);
            newFood.setPrice(price);
            newFood.setDescription(description);
            newFood.setQuantity(quantity);
            if (image != null && !image.isEmpty()){
                String fileName = image.getOriginalFilename();
                Path path = Paths.get(urlImage + fileName);
                Files.copy(image.getInputStream(), path);
                newFood.setImage(path.toString());
            }
            this.foodService.createNewFood(newFood);
            return ResponseEntity.ok().body("Save data successfully");
        }
        else{
            return ResponseEntity.ok().body("File not found");
        }
    }
    @GetMapping("/foods")
    public String getAllFood(@RequestParam String param) {
        return new String();
    }
}
