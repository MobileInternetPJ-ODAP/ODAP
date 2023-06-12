package com.example.odap.controller;

import com.example.odap.entity.*;
import com.example.odap.repository.DatasetRepository;
import com.example.odap.repository.PictureDataRepository;
import com.example.odap.repository.TextDataRepository;
import com.example.odap.repository.VoiceDataRepository;
import com.example.odap.request.DatasetAddRequest;
import com.example.odap.request.DatasetUpdateRequest;
import com.example.odap.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.odap.DTO.DatasetResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class DatasetController {

    @Autowired
    private DatasetRepository datasetRepository;

    @Autowired
    private PictureDataRepository pictureDataRepository;

    @Autowired
    private VoiceDataRepository voiceDataRepository;

    @Autowired
    private TextDataRepository textDataRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private String uploadDir;

    @CrossOrigin
    @GetMapping("/count_datasets")
    public ResponseEntity<Map<String, Object>> getDatasetCount() {
        long count = datasetRepository.count();

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("error_msg", "success");
        response.put("data", count);

        return ResponseEntity.ok(response);
    }


    @CrossOrigin
    @PostMapping("/dataset")
    public ResponseEntity<Map<String, Object>> createDataset(
            HttpServletRequest httpRequest,
            @RequestParam String desc,
            @RequestParam String sample_type,
            @RequestParam String tag_type,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (!(sample_type.equals("语音") || sample_type.equals("图片") || sample_type.equals("文本"))) {
            Map<String, Object> response = new HashMap<>();
            response.put("error_msg", "不支持的数据集类型！目前仅支持图片、语音、文本"); // 设置错误信息
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        //将文件存储在固定目录下，并将路径赋给dataset
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //生成一个唯一的文件名
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File serverFile = new File(dir.getAbsolutePath() + File.separator + fileName);

        //将文件存储在固定目录下
        try (OutputStream os = Files.newOutputStream(serverFile.toPath())) {
            os.write(file.getBytes());
        }

        //在数据集表中加入数据集的记录
        Dataset dataset = new Dataset();
        dataset.setDatasetName(file.getOriginalFilename());
        long id = userService.getCurrentUserId(httpRequest);
        dataset.setPublisherId(id);
        LocalDateTime pubTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String pubTimeStr = pubTime.format(formatter);
        dataset.setPubTime(pubTimeStr);
        dataset.setDescription(desc);

        double sizeInBytes = file.getSize();
        double sizeInMB = sizeInBytes / (1024 * 1024);
        //保留两位小数
        sizeInMB = (double) Math.round(sizeInMB * 100) / 100;

        dataset.setSampleSize(sizeInMB);
        dataset.setSampleType(sample_type);
        dataset.setTagType(tag_type);
        dataset.setFilePath(serverFile.getAbsolutePath());

        datasetRepository.save(dataset);
        DatasetResponse datasetResponse = new DatasetResponse(dataset);

        //对文本数据集进行解析和存储
        if(sample_type.equals("文本")){ //文本数据需要特别处理，语音数据和图片数据类似
            String filePath = serverFile.getAbsolutePath();
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                ObjectMapper mapper = new ObjectMapper();
                while ((line = reader.readLine()) != null) {
                    // 解析每一行的 JSON 数据
                    String jsonObject = mapper.readValue(line, Object.class).toString();
                    System.out.println(jsonObject);
                    TextData textData = new TextData(dataset.getId(), jsonObject);
                    textDataRepository.save(textData);
                }
            }
        }
        //对语音和图片数据集进行解压并存储
        else {
            // 开始解压文件，并将数据加入对应语音/图像数据表
            String unzipDirPath = dir.getAbsolutePath() + File.separator + "unzip" + File.separator + fileName;
            File unzipDir = new File(unzipDirPath);
            if (!unzipDir.exists()) {
                unzipDir.mkdirs();
            }

            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(serverFile))) {
                ZipEntry zipEntry = zis.getNextEntry();
                while (zipEntry != null) {
                    File newFile = newFile(unzipDir, zipEntry);
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                    if(sample_type.equals("图片")) {
                        PictureData pictureData = new PictureData(dataset.getId(), newFile.getName(), newFile.getAbsolutePath());
                        pictureDataRepository.save(pictureData);
                    }
                    else{ // 否则样本类型为语音
                        VoiceData voiceData = new VoiceData(dataset.getId(), newFile.getName(), newFile.getAbsolutePath());
                        voiceDataRepository.save(voiceData);
                    }
                    zipEntry = zis.getNextEntry();
                }
                zis.closeEntry();
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("error_msg", "success");
        response.put("data", datasetResponse);
        return ResponseEntity.ok(response);
    }

    @CrossOrigin
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

    @CrossOrigin
    @PutMapping("/dataset/{id}")
    public ResponseEntity<Map<String, Object>> updateDataset(@PathVariable("id") String id, @RequestParam String desc,
                                                             @RequestParam String sample_type, @RequestParam String tag_type,
                                                             @RequestParam("file") MultipartFile file) throws IOException {
        // 根据id查询数据库中的数据集
        Dataset dataset = datasetRepository.findById(Long.valueOf(id)).orElse(null);
        if (dataset == null) {
            return ResponseEntity.notFound().build();
        }

        // 删除原有文件
        File oldFile = new File(dataset.getFilePath());
        if (oldFile.exists()) {
            oldFile.delete();
        }

        // 保存新上传的文件到指定目录下

        // String uploadDir = "/Users/zhengyuanze/upload_data";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File serverFile = new File(dir.getAbsolutePath() + File.separator + fileName);

        try (OutputStream os = Files.newOutputStream(serverFile.toPath())) {
            os.write(file.getBytes());
        }

        // 更新数据集的属性
        dataset.setDescription(desc);
        dataset.setSampleType(sample_type);
        dataset.setTagType(tag_type);
        dataset.setFilePath(serverFile.getAbsolutePath());

        double sizeInBytes = file.getSize();
        double sizeInMB = sizeInBytes / (1024 * 1024);
        //保留两位小数
        sizeInMB = (double) Math.round(sizeInMB * 100) / 100;
        dataset.setSampleSize(sizeInMB);

        // 保存更新后的数据集到数据库
        Dataset updatedDataset = datasetRepository.save(dataset);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("error_msg", "success");
        response.put("data", new DatasetResponse(updatedDataset));

        return ResponseEntity.ok(response);
    }

    @CrossOrigin
    @GetMapping ("/del_dataset/{id}")
    public ResponseEntity<Map<String, Object>> deleteDataset(HttpServletRequest request, @PathVariable("id") String id) {
        // 根据id查询数据库中的数据集
        Dataset dataset = datasetRepository.findById(Long.valueOf(id)).orElse(null);
        if (dataset == null) {
            return ResponseEntity.notFound().build();
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if(!user.getUserName().equals("root")){ // 验证是否是管理员用户
            Map<String, Object> response = new HashMap<>();
            response.put("code", 405);
            response.put("error_msg", "Only root members can delete dataset!");
            return ResponseEntity.ok(response);
        }
        // 删除数据集
        assert dataset != null;
        File oldFile = new File(dataset.getFilePath());
        if (oldFile.exists()) {
            oldFile.delete();
        }
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
            return ResponseEntity.notFound().build();
        }

        // 构建响应数据
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("error_msg", "success");
        response.put("data", new DatasetResponse(dataset));

        return ResponseEntity.ok(response);
    }


    public File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}


