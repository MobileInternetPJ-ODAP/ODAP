package com.example.odap.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Dataset")
@Data
@Getter
@Setter
public class Dataset {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "dataset_name")
    private String datasetName;

    @Column(name = "publisher_id")
    private Long publisherId;
    @Column(name = "pub_time")
    private String pubTime;
    @Column(name = "description")
    private String description;
    @Column(name = "sample_type")
    private String sampleType;
    @Column(name = "sample_size")
    private double sampleSize;
    @Column(name = "tag_type")
    private String tagType;

    @Column(name = "file_path")
    private String filePath;

    public Dataset() {
        super();
    }

    public Dataset(String name, Long publisherId, String pubTime, String description, String sampleType, double sampleSize, String tagType, String filePath){
        super();
        this.datasetName = name;
        this.publisherId = publisherId;
        this.pubTime = pubTime;
        this.description = description;
        this.sampleType = sampleType;
        this.sampleSize = sampleSize;
        this.tagType = tagType;
        this.filePath = filePath;
    }

}
