package com.example.my_theatre.service;

import com.example.my_theatre.entity.dto.FilmDto;
import com.example.my_theatre.entity.vo.FilmVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface AdminFilmService {
    void addfilm(MultipartFile file, FilmDto film);


    void delFilm(String movieName);

    List<FilmVo> allFilm();

    List<FilmVo> getHotFilms();

    List<FilmVo> allFilmByPage(int page, int size);

    void setFilmStatus(FilmDto filmDto);
}
