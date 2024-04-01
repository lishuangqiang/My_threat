package com.example.my_theatre.controller.admin;


import com.example.my_theatre.common.BaseResponse;
import com.example.my_theatre.common.ResultUtils;
import com.example.my_theatre.context.BaseContext;
import com.example.my_theatre.entity.dto.FilmDto;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.service.impl.AdminFilmServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/film")
public class adminFilmController {
    @Autowired
    private AdminFilmServiceImpl adminFilmService;
    @PostMapping("/addFilm")
    public BaseResponse<String> addFilm(@RequestBody FilmDto film)
    {
        log.info("当前管理员"+ BaseContext.getCurrentId() +"正在上传电影："+film.getMovieName());
        try {
            adminFilmService.addfilm(film);

        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }
        return ResultUtils.success("保存成功");
    }
}
