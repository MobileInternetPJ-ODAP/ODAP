package com.example.odap.DTO;

import com.example.odap.entity.TagData;
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
    public TagResponse(TagData tagData) {
        this.tag_id = tagData.getId().toString();
        this.begin_pos = tagData.getBeginPos();
        this.end_pos = tagData.getEndPos();
        this.tag = tagData.getTag();
    }

}
