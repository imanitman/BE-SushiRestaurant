package com.application.sushi.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.application.sushi.domain.Food;
import com.application.sushi.domain.response.ResAllFood;
import com.application.sushi.service.FoodService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




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
    public ResponseEntity<ResAllFood> getAllFood(@RequestParam ("page") int page, @RequestParam("size") int size) {
        ResAllFood resAllFood = this.foodService.fetchAllFood(page, size);
        return ResponseEntity.ok().body(resAllFood);
    }
    @PutMapping("chef/food/{id}")
    public ResponseEntity<Food> updateFood(@PathVariable ("id") long id,
        @RequestParam ("name") String name, @RequestParam ("description") String description,
        @RequestParam ("logo") MultipartFile logo, @RequestParam ("quantity") int quantity, @RequestParam("price") String price) throws IOException {
        Food currentFood = this.foodService.fetchFoodById(id);
        currentFood.setName(name);
        currentFood.setDescription(description);
        currentFood.setPrice(price);
        // xóa file ảnh cũ trong system
        String currentFilePath = currentFood.getImage();
        Path curentPath = Paths.get(currentFilePath);
        Files.deleteIfExists(curentPath);

        String fileName = logo.getOriginalFilename();
        Path path = Paths.get(urlImage + fileName);
        Files.copy(logo.getInputStream(), path);
        currentFood.setImage(path.toString());
        this.foodService.createNewFood(currentFood);
        return ResponseEntity.ok().body(currentFood);
    }
    @DeleteMapping("/chef/food/{id}")
    public ResponseEntity<Void> deleteFood (@PathVariable ("id") long id) throws IOException{
        Food currentFood = this.foodService.fetchFoodById(id);
        String pathFile = currentFood.getImage();
        Path path = Paths.get(pathFile);
        Files.deleteIfExists(path);
        this.deleteFood(id);
        return ResponseEntity.ok().body(null);
    }
}
