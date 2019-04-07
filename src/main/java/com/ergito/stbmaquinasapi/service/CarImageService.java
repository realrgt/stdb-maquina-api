package com.ergito.stbmaquinasapi.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import com.ergito.stbmaquinasapi.exception.FileStorageException;
import com.ergito.stbmaquinasapi.exception.MyFileNotFoundException;
import com.ergito.stbmaquinasapi.model.CarImage;
import com.ergito.stbmaquinasapi.repository.CarImageRepository;

@Service
public class CarImageService {

	@Autowired
	private CarImageRepository repository;

	public CarImage storeFile(MultipartFile file) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}

			CarImage carImage = new CarImage(fileName, file.getContentType(), file.getBytes());

			return repository.save(carImage);
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	public CarImage getFile(String fileId) {
		return repository.findById(fileId)
				.orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
	}
	
	public List findAll() {
		return this.repository.findAll();
	}

}
