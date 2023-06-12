package com.example.odap.repository;

import com.example.odap.entity.Dataset;
import com.example.odap.entity.PictureData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureDataRepository extends JpaRepository<PictureData, Long> {
    Page<PictureData> findByDatasetId(Long datasetID, PageRequest pageRequest);
    PictureData findByDatasetIdAndId(Long datasetId, Long id);
    int countByDatasetId(Long datasetID);
}
