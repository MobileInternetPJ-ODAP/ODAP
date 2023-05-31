package com.example.odap.repository;

import com.example.odap.entity.Dataset;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatasetRepository extends JpaRepository<Dataset, Long> {
    Dataset findByDatasetName(String datasetName);
    boolean existsByDatasetName(String datasetName);
    Page<Dataset> findByPublisherId(Long publisherId, PageRequest pageRequest);

}
