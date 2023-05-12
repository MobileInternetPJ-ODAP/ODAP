package com.example.odap.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//{
//    "desc": "string",
//    "sample_type": "string",
//    "tag_type": "string"
//}

@Data
@Getter
@Setter
public class DatasetUpdateRequest {
    private String desc;
    private String sample_type;
    private String tag_type;

}
