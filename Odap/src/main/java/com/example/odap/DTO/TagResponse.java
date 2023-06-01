package com.example.odap.DTO;

import com.example.odap.entity.ImageTagData;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TagResponse {
    private String tag_id;
    private String begin_pos;
    private String end_pos;
    private String tag;
    public TagResponse(ImageTagData imageTagData) {
        this.tag_id = imageTagData.getId().toString();
        this.begin_pos = imageTagData.getBeginPos();
        this.end_pos = imageTagData.getEndPos();
        this.tag = imageTagData.getTag();
    }

}
