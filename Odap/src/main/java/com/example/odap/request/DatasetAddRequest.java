package com.example.odap.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DatasetAddRequest {
    private String desc;
    private String sample_type;
    private String tag_type;
}