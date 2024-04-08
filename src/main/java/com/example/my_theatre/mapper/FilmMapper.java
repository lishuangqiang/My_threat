package com.example.my_theatre.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.my_theatre.entity.dto.FilmDto;
import com.example.my_theatre.entity.po.Admin;
import com.example.my_theatre.entity.po.Film;
import com.example.my_theatre.entity.vo.FilmVo;

import java.util.List;
import java.util.Map;

public interface FilmMapper extends BaseMapper<Film> {

    Boolean addfilm(String movieName, String leadingActor,String filePath, String movieType, String movieYear, String movieCountry, int movieTime,int movieTicket);

    void delFilm(String movieName);

    List<FilmVo> allFilm();

    List<FilmVo> listWithFlavor();

    List<FilmVo> selectFilmsByPage(Map<String, Integer> params);

    Boolean selectBymovieName(String movieName);
}
