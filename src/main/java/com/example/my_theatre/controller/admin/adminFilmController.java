package com.example.my_theatre.controller.admin;


import com.example.my_theatre.common.BaseResponse;
import com.example.my_theatre.common.ResultUtils;
import com.example.my_theatre.context.BaseContext;
import com.example.my_theatre.entity.dto.FilmDto;

import com.example.my_theatre.entity.vo.FilmVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.service.impl.AdminFilmServiceImpl;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/film")
@Api(tags = "管理员管理电影相关接口")
public class adminFilmController {
    @Resource
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
                                        String movieName,
                                        String leadingActor,
                                         int movieTime,
                                        String movieCountry,
                                        String movieType,
                                        String movieYear,
                                        int movieStatus) {
        log.info("当前管理员" + BaseContext.getCurrentId() + "正在上传电影：" + movieName);
        //构造电影实体类
        FilmDto film = new FilmDto();
        film.setMovieName(movieName);
        film.setLeadingActor(leadingActor);
        film.setMovieTime(movieTime);
        film.setMovieCountry(movieCountry);
        film.setMovieType(movieType);
        film.setMovieYear(movieYear);
        film.setMovieStatus(movieStatus);
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
    public BaseResponse<String> delFilm(@RequestBody String movieName) {
        log.info("当前管理员正在删除电影 "+ movieName + "管理员id为：");
        try {
            adminFilmService.delFilm(movieName);

        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }
        return ResultUtils.success("删除成功");
    }

    /**
     * 查询所有电影（分页查询，减轻数据库压力）
     */
    @GetMapping("/allFilm")
    public BaseResponse<List<FilmVo>> allFilm(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("分页查询数据库所有电影.....");
        List<FilmVo> films = adminFilmService.allFilmByPage(page, size);
        return ResultUtils.success(films);
    }
    /**
     * 查询热门电影（基于Redis做缓存）
     * 票房前八
     */
    @GetMapping("/getHotFilms")
    public BaseResponse<List<FilmVo>> getHotFilms() {
        //todo（这里用到了Redis,因此要注意数据库和redis的更新行为）
        List<FilmVo> films = adminFilmService.getHotFilms();
        return ResultUtils.success(films);
    }

    /**
     * 设置电影状态
     */
    @PostMapping("/setFilmStatus")
    public BaseResponse<String> setFilmStatus(@RequestBody FilmDto filmDto) {
        log.info("当前管理员正在设置电影状态");
        try {
            adminFilmService.setFilmStatus(filmDto);
        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }
        return ResultUtils.success("设置成功");
    }

}

