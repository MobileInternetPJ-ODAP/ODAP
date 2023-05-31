package com.example.odap.controller;

import com.example.odap.DTO.DatasetResponse;
import com.example.odap.entity.Dataset;
import com.example.odap.entity.PictureData;
import com.example.odap.repository.PictureDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class SampleController {

    @Autowired
    private PictureDataRepository pictureDataRepository;

    @GetMapping("/samples")
    public ResponseEntity<Map<String, Object>> getSamples(
            @RequestParam("page_num") int pageNum,
            @RequestParam("page_size") int pageSize,
            @RequestParam("dataset_id") Long datasetId
    ) {
        // 构建分页请求对象
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);

        // 执行分页查询
        Page<PictureData> pictureDataPage;

        pictureDataPage = pictureDataRepository.findByDatasetId(datasetId, pageRequest);


        // 构建响应数据
        List<PictureData> pictureDatas = pictureDataPage.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("error_msg", "success");
        response.put("data", pictureDatas);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/count_samples")
    public ResponseEntity<Map<String, Object>> countSamples(
            @RequestParam("dataset_id") Long datasetId
    ) {
        // 执行查询
        int count = pictureDataRepository.countByDatasetId(datasetId);

        // 构建响应数据
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("error_msg", "success");
        response.put("data", count);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/sample_data")
    public ResponseEntity<byte[]> getSampleData(
            @RequestParam("dataset_id") Long datasetId,
            @RequestParam("sample_id") Long id
    ) throws IOException {
        PictureData pictureData = pictureDataRepository.findByDatasetIdAndId(datasetId, id);
        if (pictureData != null) {
            Path path = Paths.get(pictureData.getFilePath());
            byte[] image = Files.readAllBytes(path);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
