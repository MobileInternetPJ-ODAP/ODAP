package com.example.odap.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "TextData")
@Data
@Getter
@Setter
public class TextData {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "dataset_id")
    private Long datasetId;
    @Column(name = "content")
    private String content;

    public TextData(){
        super();
    }

    public TextData(Long datasetId, String content){
        super();
        this.datasetId = datasetId;
        this.content = content;
    }


}
