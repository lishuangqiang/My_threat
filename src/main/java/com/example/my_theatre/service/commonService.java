package com.example.my_theatre.service;

import com.example.my_theatre.entity.vo.FilmVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface commonService {
    List<FilmVo> getlittleStartMovie();

    List<FilmVo> getlittleWaitStartMovie();

    List<FilmVo> getBoxRanking();
}
