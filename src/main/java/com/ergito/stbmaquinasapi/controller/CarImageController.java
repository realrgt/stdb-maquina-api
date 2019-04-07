package com.ergito.stbmaquinasapi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ergito.stbmaquinasapi.model.CarImage;
import com.ergito.stbmaquinasapi.payload.UploadFileResponse;
import com.ergito.stbmaquinasapi.service.CarImageService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping({"/carros"})
public class CarImageController {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(CarImageController.class);

    @Autowired
    private CarImageService service;

    @PostMapping
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        CarImage carImage = service.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
        		.path("/carros/")
                .path(carImage.getId())
                .toUriString();

        return new UploadFileResponse(carImage.getId() ,carImage.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }

//    @PostMapping("/mult")
//    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
//        return Arrays.asList(files)
//                .stream()
//                .map(file -> uploadFile(file))
//                .collect(Collectors.toList());
//    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        // Load file from database
        CarImage carImage = service.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(carImage.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + carImage.getFileName() + "\"")
                .body(new ByteArrayResource(carImage.getData()));
    }
	
	@GetMapping
	public List findAll() {
		return this.service.findAll();
	}
}
