package com.example.odap.controller;

import com.example.odap.entity.*;
import com.example.odap.repository.PictureDataRepository;
import com.example.odap.repository.ImageTagDataRepo;
import com.example.odap.repository.TextDataRepository;
import com.example.odap.repository.VoiceDataRepository;
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
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class SampleController {

    @Autowired
    private PictureDataRepository pictureDataRepository;

    @Autowired
    private TextDataRepository textDataRepository;

    @Autowired
    private VoiceDataRepository voiceDataRepository;

    @Autowired
    private ImageTagDataRepo imageTagDataRepo;

    @GetMapping("/samples")
    public ResponseEntity<Map<String, Object>> getSamples(
            @RequestParam("page_num") int pageNum,
            @RequestParam("page_size") int pageSize,
            @RequestParam("dataset_id") Long datasetId,
            @RequestParam("sample_type") String sampleType
    ) {
        // 构建分页请求对象
        if(sampleType.equals("图片")){
            System.out.println(1);
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
        else if(sampleType.equals("语音")){
            System.out.println(2);
            PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
            Page<VoiceData> voiceDataPage;
            voiceDataPage = voiceDataRepository.findByDatasetId(datasetId, pageRequest);
            List<VoiceData> voiceDatas = voiceDataPage.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("error_msg", "success");
            response.put("data", voiceDatas);
            return ResponseEntity.ok(response);
        }
        else{ //文本
            System.out.println(3);
            PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
            Page<TextData> textDataPage;
            textDataPage = textDataRepository.findByDatasetId(datasetId, pageRequest);
            List<TextData> textDatas = textDataPage.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("error_msg", "success");
            response.put("data", textDatas);
            return ResponseEntity.ok(response);
        }
    }


    @GetMapping("count_tagged_samples")
    public ResponseEntity<Map<String, Object>> countTaggedSamples(
            @RequestParam("dataset_id") Long datasetId
    ) {
        List<TagData> all_response = imageTagDataRepo.findByDatasetId(datasetId.toString());
        int taggedCount = 0;
        HashSet<String> hashSet = new HashSet<>();
        for( TagData d : all_response){
            if(!hashSet.contains(d.getSampleId())){
                taggedCount += 1;
                hashSet.add(d.getSampleId());
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("error_msg", "success");
        response.put("data", taggedCount);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count_samples")
    public ResponseEntity<Map<String, Object>> countSamples(
            @RequestParam("dataset_id") Long datasetId,
            @RequestParam("sample_type") String sampleType
    ) {
        // 执行查询
        int count = 0;
        if(sampleType.equals("图片")){
            count = pictureDataRepository.countByDatasetId(datasetId);
        }
        else if(sampleType.equals("文本")){
            count = textDataRepository.countByDatasetId(datasetId);
        }
        else{
            count = voiceDataRepository.countByDatasetId(datasetId);
        }

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
            @RequestParam("sample_id") Long id,
            @RequestParam("sample_type") String sampleType
    ) throws IOException {
        if(sampleType.equals("文本")){
            TextData textData = textDataRepository.findByDatasetIdAndId(datasetId, id);
            System.out.println(textData);
            if (textData != null) {
                byte[] textBytes = textData.getContent().getBytes();
                return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(textBytes);
            }
            else {
                return ResponseEntity.notFound().build();
            }
        }
        else if(sampleType.equals("语音")){
            VoiceData voiceData = voiceDataRepository.findByDatasetIdAndId(datasetId, id);
            if (voiceData != null) {
                Path path = Paths.get(voiceData.getFilePath());
                byte[] voiceRecord = Files.readAllBytes(path);
                return ResponseEntity.ok().contentType(MediaType.parseMediaType("audio/mpeg")).body(voiceRecord);
            }
            else {
                return ResponseEntity.notFound().build();
            }
        }
        else {
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
}
