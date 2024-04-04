package com.example.my_theatre.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.my_theatre.entity.po.Admin;
import com.example.my_theatre.entity.po.Film;

import java.util.List;

public interface FilmMapper extends BaseMapper<Film> {

    Boolean addfilm(String movieName, String leadingActor,String filePath, String movieType, String movieYear, String movieCountry, int movieTime,int movieTicket);
}
