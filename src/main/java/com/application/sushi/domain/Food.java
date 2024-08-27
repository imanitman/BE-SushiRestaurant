package com.application.sushi.domain;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Food {
    private String name;
    private String price;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
}
