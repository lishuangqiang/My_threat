package com.example.my_theatre.controller.admin;


import com.example.my_theatre.common.BaseResponse;
import com.example.my_theatre.common.ResultUtils;
import com.example.my_theatre.context.BaseContext;
import com.example.my_theatre.entity.dto.FilmDto;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.entity.po.Film;
import com.example.my_theatre.entity.vo.FilmVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.service.impl.AdminFilmServiceImpl;
import com.example.my_theatre.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/film")
public class adminFilmController {
    @Autowired
    private AdminFilmServiceImpl adminFilmService;


    /**
     * 添加电影
     *
     * @param moviePicture
     * @param movieName
     * @param leadingActor
     * @param movieTime
     * @param movieCountry
     * @param movieType
     * @param movieYear
     * @return
     */
    @PostMapping("/addFilm")
    public BaseResponse<String> addFilm(MultipartFile moviePicture,
                                        String movieName, String leadingActor
            , int movieTime, String movieCountry, String movieType
            , String movieYear) {
        log.info("当前管理员" + BaseContext.getCurrentId() + "正在上传电影：" + movieName);
        FilmDto film = new FilmDto();
        film.setMovieName(movieName);
        film.setLeadingActor(leadingActor);
        film.setMovieTime(movieTime);
        film.setMovieCountry(movieCountry);
        film.setMovieType(movieType);
        film.setMovieYear(movieYear);


        try {
            adminFilmService.addfilm(moviePicture, film);

        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }
        return ResultUtils.success("保存成功");
    }

    /**
     * 删除电影
     *
     * @param movieName
     * @return
     */
    @PostMapping("/delFilm")
    public BaseResponse<String> delFilm(String movieName) {
        try {
            adminFilmService.delFilm(movieName);

        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }
        return ResultUtils.success("删除成功");
    }

    /**
     * 查询所有电影
     */
    @GetMapping("/allFilm")
    public BaseResponse<List<FilmVo>> allFilm() {
        List<FilmVo> films = adminFilmService.allFilm();
        return ResultUtils.success(films);
    }

    /**
     * 查询热门电影（基于Redis做缓存）
     * 票房前八
     */
    @GetMapping("/getHotFilms")
    public BaseResponse<List<FilmVo>> getHotFilms() {
        List<FilmVo> films = adminFilmService.getHotFilms();
        return ResultUtils.success(films);
    }


}

