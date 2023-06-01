package com.example.odap.repository;
import com.example.odap.entity.ImageTagData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ImageTagDataRepo extends JpaRepository<ImageTagData, Long>{
    List<ImageTagData> findByDatasetIdAndSampleId(String datasetId, String sampleId);
}
