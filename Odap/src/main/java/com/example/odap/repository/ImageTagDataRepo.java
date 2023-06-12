package com.example.odap.repository;
import com.example.odap.entity.TagData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ImageTagDataRepo extends JpaRepository<TagData, Long>{
    List<TagData> findByDatasetIdAndSampleId(String datasetId, String sampleId);
    List<TagData> findByDatasetId(String datasetId);
}
