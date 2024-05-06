package com.example.my_theatre.controller.common;

import com.example.my_theatre.entity.vo.FilmVo;
import com.example.my_theatre.service.impl.CommonServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/common/film")
public class commonController {
    @Resource
    private CommonServiceImpl commonService;

    /**
     * 查询所有上映电影
     * (首页展示，只展示八条)
     */
    @GetMapping ("/getlittleStartMovie")
    public List<FilmVo> getAllMovie(){

        return commonService.getlittleStartMovie();
    }

    /**
     * 查询待上映电影
     * (首页展示，只展示八条)
     */
    @GetMapping("getlittleWaitStartMovie")
    public List<FilmVo> getlittleWaitStartMovie(){
        return commonService.getlittleWaitStartMovie();
    }


    /**
     * 查询票房前八
     */
    @GetMapping("/getBoxRanking")
    public List<FilmVo> getBoxRanking(){
        return commonService.getBoxRanking();
    }


}
