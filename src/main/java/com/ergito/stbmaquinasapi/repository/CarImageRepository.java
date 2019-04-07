package com.ergito.stbmaquinasapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ergito.stbmaquinasapi.model.CarImage;

@Repository
public interface CarImageRepository extends JpaRepository<CarImage, String> {

}
