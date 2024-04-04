package com.example.my_theatre.service;

import com.example.my_theatre.entity.dto.FilmDto;
import org.springframework.web.multipart.MultipartFile;

public interface AdminFilmService {
    void addfilm(MultipartFile file, FilmDto film);


}
