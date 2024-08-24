package com.application.sushi.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqLoginDto {
    private String username;
    private String password;
}
