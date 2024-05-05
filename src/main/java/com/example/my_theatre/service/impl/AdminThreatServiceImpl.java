package com.example.my_theatre.service.impl;
import com.example.my_theatre.entity.dto.ThreatDto;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.entity.vo.ThreatVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.mapper.FilmMapper;
import com.example.my_theatre.mapper.ThreatMapper;
import com.example.my_theatre.service.AdminThreatService;
import com.example.my_theatre.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class AdminThreatServiceImpl implements AdminThreatService {

    @Resource
    private ThreatMapper threatMapper;
    @Resource
    private FilmMapper filmMapper;
    @Resource
    private RedisUtil redisUtil;


    /**
     * 展现剧院所有上映电影
     * @return
     */
    @Override
    public List<ThreatVo> findAllfilm() {
        //在数据库中进行查询
        List<ThreatVo> list =threatMapper.selectall();
        return list;
    }

    /**
     * 上映电影
     * @param threatDto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addFilm(ThreatDto threatDto) throws BusinessException {
        //检查当前电影是否存在：
        if(filmMapper.selectBymovieName(threatDto.getMovieName()) == Boolean.FALSE)
        {
            throw new BusinessException(ErrorCode.FILM_NOT_EXIST);
        }
        LocalDateTime startTime = threatDto.startTime;
        LocalDateTime endTime =threatDto.endTime;
        String movieName= threatDto.movieName;
        int  movieId = threatDto.playingMovieId;
        String moviePicture = threatDto.moviePicture;
        //检查上映时间是否冲突：
        if(threatMapper.isRightTime(startTime) == Boolean.TRUE)
        {
            throw new BusinessException(ErrorCode.TIME_IS_FAIL);
        }
        //判断插入结果
        if(threatMapper.insertNewMovie(startTime,endTime,movieName,movieId,moviePicture) == Boolean.FALSE)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int status =2;
        //如果插入成功，更新电影状态
        if(filmMapper.setFilmStatusByid(movieId,status) == Boolean.FALSE)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //更新缓存
        redisUtil.set("movie_"+movieId,threatDto);


    }

    /**
     * 下映电影
     * @param threatDto
     * @throws BusinessException
     */
    @Override
    public void delFilm(ThreatDto threatDto) throws BusinessException {
        //检查当前电影是否存在
        if(filmMapper.selectBymovieName(threatDto.getMovieName()) == Boolean.FALSE)
        {
            throw new BusinessException(ErrorCode.FILM_NOT_EXIST);
        }
        int id = threatDto.getPlayingMovieId();
        //判断删除结果
        if(threatMapper.deletemovieByid(id) == Boolean.FALSE)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

    }
}
