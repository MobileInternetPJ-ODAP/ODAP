package com.example.odap.controller;

import com.example.odap.entity.Dataset;
import com.example.odap.repository.DatasetRepository;
import com.example.odap.request.DatasetAddRequest;
import com.example.odap.request.DatasetUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.odap.DTO.DatasetResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DatasetController {

    @Autowired
    private DatasetRepository datasetRepository;
    @PostMapping("/dataset")
    public ResponseEntity<Map<String, Object>> createDataset(@RequestBody DatasetAddRequest request) {
        // 暂时随便创建一个数据集对象
        Dataset dataset = new Dataset();
        dataset.setDatasetName("test");
        dataset.setPublisherId(1L);
        dataset.setPubTime("test");
        dataset.setDescription(request.getDesc());
        dataset.setSampleSize(1);
        dataset.setSampleType(request.getSample_type());
        dataset.setTagType(request.getTag_type());

        datasetRepository.save(dataset);

        DatasetResponse datasetResponse = new DatasetResponse(dataset);



        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("error_msg", "success");
        response.put("data", datasetResponse);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/datasets")
    public ResponseEntity<Map<String, Object>> getDatasets(
            @RequestParam("page_num") int pageNum,
            @RequestParam("page_size") int pageSize,
            @RequestParam(value = "publisher_id", required = false) Long publisherId
    ) {
        // 构建分页请求对象
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);

        // 执行分页查询
        Page<Dataset> datasetPage;
        if (publisherId != null) {
            datasetPage = datasetRepository.findByPublisherId(publisherId, pageRequest);
        } else {
            datasetPage = datasetRepository.findAll(pageRequest);
        }

        // 构建响应数据
        List<Dataset> datasets = datasetPage.getContent();
        // 将 Dataset 转换为 DatasetResponse 自定义返回体中展示的参数
        List<DatasetResponse> datasetResponses = new ArrayList<>();
        for (Dataset dataset : datasets) {
            datasetResponses.add(new DatasetResponse(dataset));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("error_msg", "success");
        response.put("data", datasetResponses);

        return ResponseEntity.ok(response);
    }


    @PutMapping("/dataset/{id}")
    public ResponseEntity<Map<String, Object>> updateDataset(@PathVariable("id") String id, @RequestBody DatasetUpdateRequest request) {
        // 根据id查询数据库中的数据集
        Dataset dataset = datasetRepository.findById(Long.valueOf(id)).orElse(null);
        if (dataset == null) {
            // 处理数据集不存在的情况
            // 返回适当的错误响应
        }

        // todo: 更新数据集的属性，新上传的文件的其他属性也要解析，这里先不管
        dataset.setDescription(request.getDesc());
        dataset.setSampleType(request.getSample_type());
        dataset.setTagType(request.getTag_type());

        // 保存更新后的数据集到数据库
        Dataset updatedDataset = datasetRepository.save(dataset);

        // 构建响应数据
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("error_msg", "success");
        response.put("data", new DatasetResponse(updatedDataset));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/dataset/{id}")
    public ResponseEntity<Map<String, Object>> deleteDataset(@PathVariable("id") String id) {
        // 根据id查询数据库中的数据集
        Dataset dataset = datasetRepository.findById(Long.valueOf(id)).orElse(null);
        if (dataset == null) {
            // 处理数据集不存在的情况
            // 返回适当的错误响应
        }

        // 删除数据集
        datasetRepository.delete(dataset);

        // 构建响应数据
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("error_msg", "success");
        response.put("data", null);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/dataset/{id}")
    public ResponseEntity<Map<String, Object>> getDataset(@PathVariable("id") String id) {
        // 根据id查询数据库中的数据集
        Dataset dataset = datasetRepository.findById(Long.valueOf(id)).orElse(null);
        if (dataset == null) {
            // 处理数据集不存在的情况
            // 返回适当的错误响应
        }

        // 构建响应数据
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("error_msg", "success");
        response.put("data", new DatasetResponse(dataset));

        return ResponseEntity.ok(response);
    }



}
