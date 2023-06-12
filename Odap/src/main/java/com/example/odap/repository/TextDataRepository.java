package com.example.odap.repository;

import com.example.odap.entity.TextData;
import com.example.odap.entity.VoiceData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextDataRepository extends JpaRepository<TextData, Long> {
    Page<TextData> findByDatasetId(Long datasetID, PageRequest pageRequest);
    TextData findByDatasetIdAndId(Long datasetId, Long id);
    int countByDatasetId(Long datasetID);
}
