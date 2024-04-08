package com.example.my_theatre.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.my_theatre.entity.po.User;
import com.example.my_theatre.entity.vo.ThreatVo;

import java.time.LocalDateTime;
import java.util.List;


public interface ThreatMapper extends BaseMapper<User> {


    List<ThreatVo> selectall();

    Boolean insertNewMovie(LocalDateTime startTime, LocalDateTime endTime, String movieName, int movieId, String moviePicture);

    boolean isRightTime(LocalDateTime startTime);

    Boolean deletemovieByid(int id);
}




