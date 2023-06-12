package com.example.odap.controller;
import com.example.odap.DTO.TagResponse;
import com.example.odap.entity.TagData;
import com.example.odap.repository.ImageTagDataRepo;
import com.example.odap.repository.VoiceDataRepository;
import com.example.odap.request.TagForm;
import com.example.odap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class TagController {

    @Autowired
    private ImageTagDataRepo imageTagDataRepo;
    @Autowired
    private VoiceDataRepository voiceDataRepository;
    @Autowired
    private UserService userService;
    @CrossOrigin
    @PostMapping("/tag")
    public ResponseEntity<Map<String, Object>> addTag(
            HttpServletRequest httpRequest,
            @RequestBody TagForm tagForm
            ) {
        try{
            long taggerId = userService.getCurrentUserId(httpRequest);
            System.out.println(taggerId);
            TagData tagData = new TagData(tagForm.getDataset_id(), tagForm.getSample_id(), tagForm.getBegin_pos(), tagForm.getEnd_pos(), tagForm.getTag(), taggerId);
            System.out.println(tagData);
            imageTagDataRepo.save(tagData);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("error_msg", "success");
//        response.put("data", datasetResponse);
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            Map<String, Object> response = new HashMap<>();
            response.put("code", 501);
            response.put("error_msg", e);
            return ResponseEntity.ok(response);
        }
    }
    @CrossOrigin
    @GetMapping("/tags")
    public ResponseEntity<Map<String, Object>> getTags(
            @RequestParam("dataset_id") String datasetId,
            @RequestParam("sample_id") String sampleId
    ) {
        List<TagData> imageDataList = imageTagDataRepo.findByDatasetIdAndSampleId(datasetId, sampleId);
        List<TagResponse> tagResponseList = new ArrayList<TagResponse>();
        for(TagData item: imageDataList){
            tagResponseList.add(new TagResponse(item));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("error_msg", "success");
        response.put("data", tagResponseList);

        return ResponseEntity.ok(response);
    }

    @CrossOrigin
    @GetMapping("/del_tag/{id}")
    public ResponseEntity<Map<String, Object>> delTag(
            @PathVariable("id") String tagId
    ) {
        System.out.println(1);
        TagData imageData = imageTagDataRepo.findById(Long.parseLong(tagId)).orElse(null);
        if(imageData == null){
            Map<String, Object> response = new HashMap<>();
            response.put("code", 404);
            response.put("error_msg", "tag not found!");
            return ResponseEntity.ok(response);
        }
        imageTagDataRepo.delete(imageData);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("error_msg", "success");

        return ResponseEntity.ok(response);
    }
}
