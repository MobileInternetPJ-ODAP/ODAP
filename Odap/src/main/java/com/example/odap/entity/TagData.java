package com.example.odap.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ImageData")
@Data
@Getter
@Setter
public class TagData {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "dataset_id")
    private String datasetId;
    @Column(name = "sample_id")
    private String sampleId;
    @Column(name = "begin_pos")
    private String beginPos;
    @Column(name = "end_pos")
    private String endPos;
    @Column(name = "tag")
    private String tag;
    @Column(name = "tagger_id")
    private Long taggerId;
    public TagData() {
        super();
    }
    public TagData(String datasetId, String sampleId, String beginPos, String endPos, String tag, Long taggerId) {
        super();
        this.datasetId = datasetId;
        this.sampleId = sampleId;
        this.beginPos = beginPos;
        this.endPos = endPos;
        this.tag = tag;
        this.taggerId = taggerId;
    }


}
