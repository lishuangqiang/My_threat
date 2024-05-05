package com.example.my_theatre.service.impl;

import com.example.my_theatre.entity.vo.FilmVo;
import com.example.my_theatre.mapper.FilmMapper;
import com.example.my_theatre.service.commonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class CommonServiceImpl implements commonService {
    @Resource
    private FilmMapper filmMapper;
    /**
     * 查询所有上映电影
     *
     * @return
     */
    @Override
    public List<FilmVo> getlittleStartMovie() {
        return filmMapper.getlittleStartMovie();
    }

    /**
     * 查询所有待上映电影
     * @return
     */
    @Override
    public List<FilmVo> getlittleWaitStartMovie() {
        return filmMapper.getlittleWaitStartMovie();
    }

    @Override
    public List<FilmVo> getBoxRanking() {
        return filmMapper.getBoxRanking();
    }
}
