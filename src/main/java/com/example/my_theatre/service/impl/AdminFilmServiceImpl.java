package com.example.my_theatre.service.impl;

import com.example.my_theatre.common.ResultUtils;
import com.example.my_theatre.entity.dto.FilmDto;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.mapper.FilmMapper;
import com.example.my_theatre.service.AdminFilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminFilmServiceImpl implements AdminFilmService {
    @Resource
    private FilmMapper filmMapper;


    @Override
    public void addfilm(FilmDto film) throws BusinessException {
        //获取前端所有字段。
        String filmName = film.getMovieName();
        String leadingActor =  film.getLeadingActor();
        String moviePicture =  film.getMoviePicture();
        String moiveType =  film.getMoiveType();
        String movieYear =  film.getMovieYear();
        String moiveCountry =  film.getMoiveCountry();
        int movieTime=  film.getMoiveTime();
        //写入数据库
        if(filmMapper.addfilm(filmName,leadingActor,moviePicture,
                moiveType,movieYear,moiveCountry, movieTime)==Boolean.FALSE)
        {
            throw  new BusinessException(ErrorCode.FILM_FAIL, "电影保存失败");
        }

    }
}
