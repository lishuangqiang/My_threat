package com.example.my_theatre.service.impl;

import com.example.my_theatre.common.ResultUtils;
import com.example.my_theatre.context.BaseContext;
import com.example.my_theatre.entity.dto.FilmDto;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.entity.vo.FilmVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.mapper.FilmMapper;
import com.example.my_theatre.service.AdminFilmService;
import com.example.my_theatre.utils.AliOssUtil;
import com.example.my_theatre.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AdminFilmServiceImpl implements AdminFilmService {
    @Resource
    private FilmMapper filmMapper;
    @Resource
    private AliOssUtil aliOssUtil;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 添加电影
     * @param file
     * @param film
     * @throws BusinessException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addfilm(MultipartFile file,FilmDto film) throws BusinessException {
        log.info("当前管理员正在添加电影："+film.getMovieName()+"管理员id为："+ BaseContext.getCurrentId());
        //获取前端所有字段。
        String movieName = film.getMovieName();
        String leadingActor =  film.getLeadingActor();
        String movieType =  film.getMovieType();
        String movieYear =  film.getMovieYear();
        String movieCountry =  film.getMovieCountry();
        int movieStatus = film.getMovieStatus();
        int movieTime=  film.getMovieTime();

        //检查数据库中是否已经添加过相同的电影：
        if(filmMapper.selectBymovieName(movieName)==Boolean.TRUE)
        {
            throw new BusinessException(ErrorCode.FILM_FAIL,"禁止上传相同电影");
        }
        //将图片存入阿里云
        String filePath = aliOssUtil.upload(file);
        //写入数据库
        if(filmMapper.addfilm(movieName,leadingActor,filePath,
                movieType,movieYear,movieCountry, movieTime,0,movieStatus)==Boolean.FALSE)
        {
            throw  new BusinessException(ErrorCode.FILM_FAIL, "电影保存失败");
        }

    }
    /**
     * 删除电影
     * @param movieName
     */
    @Override
    public void delFilm(String movieName) {
        log.info("当前管理员正在删除电影："+ movieName+"管理员id为："+ BaseContext.getCurrentId());
        // 写入数据库
        filmMapper.delFilm(movieName);

    }

    /**
     * 查询所有电影
     * @return
     */
    @Override
    public List<FilmVo> allFilm() {
        return filmMapper.allFilm();
    }


    /**
     * 查询热门电影(票房前十)
     * @return
     */
    @Override
    public List<FilmVo> getHotFilms() {
        //先检查Redis中是否有数据
        //构造redis的KEY：dish_分类ID
        String key = "Hot_FILM";
        //查询redis中是否存在电影数据
        List<FilmVo> list = (List<FilmVo>) redisUtil.get(key);
        if (list != null && list.size() != 0) {
            log.info("Redis中存在对应缓存数据，正在查询中.....");
            //如果存在，直接返回，无需查询数据库
            return list;
        }
        //如果不存在，查询数据库，再将查到的数据库放入到redis中
        list = filmMapper.listWithFlavor();
       redisUtil.set(key,list);
        return list;
    }

    /**
     * 分页查询所有电影
     * @param page
     * @param size
     * @return
     */
    @Override
    public List<FilmVo> allFilmByPage(int page, int size) {
            int start = (page - 1) * size;
            Map<String, Integer> params = new HashMap<>();
            params.put("start", start);
            params.put("size", size);
            return filmMapper.selectFilmsByPage(params);
    }

    @Override
    public void setFilmStatus(FilmDto filmDto) {
        log.info("当前管理员正在修改电影状态："+filmDto.getMovieName()+"管理员id为："+ BaseContext.getCurrentId());
        //获取前端所有字段。
        String movieName = filmDto.getMovieName();
        int movieStatus = filmDto.getMovieStatus();
        //检查数据库中是否已经添加过相同的电影：
        if(filmMapper.selectBymovieName(movieName)==Boolean.FALSE)
        {
            throw new BusinessException(ErrorCode.FILM_FAIL,"禁止修改不存在的电影");
        }
        //写入数据库
        if(filmMapper.setFilmStatus(movieName,movieStatus)==Boolean.FALSE)
        {
            throw  new BusinessException(ErrorCode.FILM_FAIL, "电影状态修改失败");
        }

    }


}
