package com.example.odap.pojo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
public class SignupForm {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
