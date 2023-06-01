package com.example.odap.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
public class TagForm {
    @NotBlank
    private String dataset_id;
    @NotBlank
    private String sample_id;
    @NotBlank
    private String begin_pos;
    @NotBlank
    private String end_pos;
    @NotBlank
    private String tag;
}
