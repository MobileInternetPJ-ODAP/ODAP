package com.example.odap.repository;

import com.example.odap.entity.VoiceData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoiceDataRepository extends JpaRepository<VoiceData, Long> {
    Page<VoiceData> findByDatasetId(Long datasetID, PageRequest pageRequest);
    VoiceData findByDatasetIdAndId(Long datasetId, Long id);
    int countByDatasetId(Long datasetID);
}
