package com.example.odap.entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "PictureData")
@Data
@Getter
@Setter
public class PictureData {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "dataset_id")
    private Long datasetId;

    @Column(name = "name")
    private String name;

    @Column(name = "file_path")
    private String filePath;

    public PictureData() {
        super();
    }

    public PictureData(Long datasetId, String name, String filePath){
        super();
        this.datasetId = datasetId;
        this.name = name;
        this.filePath = filePath;
    }

}
