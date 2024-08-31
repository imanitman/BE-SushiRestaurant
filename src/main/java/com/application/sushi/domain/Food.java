package com.application.sushi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "foods")
public class Food {
    
    private String name;
    private String price;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private String image;
    private int quantity;
}
