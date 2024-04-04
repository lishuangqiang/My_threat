package com.example.my_theatre.service.impl;

import com.example.my_theatre.common.ResultUtils;
import com.example.my_theatre.entity.dto.FilmDto;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.mapper.FilmMapper;
import com.example.my_theatre.service.AdminFilmService;
import com.example.my_theatre.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Service
public class AdminFilmServiceImpl implements AdminFilmService {
    @Resource
    private FilmMapper filmMapper;


    @Resource
    private AliOssUtil aliOssUtil;

    @Override
    public void addfilm(MultipartFile file,FilmDto film) throws BusinessException {
        //获取前端所有字段。
        String movieName = film.getMovieName();
        String leadingActor =  film.getLeadingActor();
        String movieType =  film.getMoiveType();
        String movieYear =  film.getMovieYear();
        String movieCountry =  film.getMoiveCountry();
        int movieTime=  film.getMoiveTime();
        //将图片存入阿里云
        String filePath = aliOssUtil.upload(file);
        //写入数据库
        if(filmMapper.addfilm(movieName,leadingActor,filePath,
                movieType,movieYear,movieCountry, movieTime,0)==Boolean.FALSE)
        {
            throw  new BusinessException(ErrorCode.FILM_FAIL, "电影保存失败");
        }

    }
}
