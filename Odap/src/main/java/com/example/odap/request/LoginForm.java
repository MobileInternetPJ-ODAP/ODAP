package com.example.odap.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
public class LoginForm {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
