package com.application.sushi.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdatePassword {
    private String email;
    private int status;
}
