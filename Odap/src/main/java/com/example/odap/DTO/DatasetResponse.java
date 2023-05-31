package com.example.odap.DTO;
import com.example.odap.entity.Dataset;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DatasetResponse {
    private String _id;
    private String dataset_name;
    private String publisher_id;
    private String pub_time;
    private String desc;
    private String sample_type;
    private String tag_type;

    public DatasetResponse(Dataset dataset) {
        this._id = dataset.getId().toString();
        this.dataset_name = dataset.getDatasetName();
        this.publisher_id = dataset.getPublisherId().toString();
        this.pub_time = dataset.getPubTime();
        this.desc = dataset.getDescription();
        this.sample_type = dataset.getSampleType();
        this.tag_type = dataset.getTagType();
    }
}
