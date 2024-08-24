package com.application.sushi.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqSignupDto {
    private String username;
    private String userpassword;
    private String name;
}
