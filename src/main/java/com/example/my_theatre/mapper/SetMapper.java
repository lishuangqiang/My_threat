package com.example.my_theatre.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.my_theatre.entity.po.Set;
import com.example.my_theatre.entity.po.User;


public interface SetMapper extends BaseMapper<Set> {

    Boolean select(int xSet, int ySet);

    void insertSet(int xSet, int ySet, String movieName, int playmovieId);
}




