package com.example.my_theatre.service.impl;

import com.example.my_theatre.context.BaseContext;
import com.example.my_theatre.entity.dto.ThreatDto;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.entity.vo.ThreatVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.mapper.ThreatMapper;
import com.example.my_theatre.service.AdminThreatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class AdminThreatServiceImpl implements AdminThreatService {

    @Resource
    public ThreatMapper threatMapper;

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
    @Override
    public void addFilm(ThreatDto threatDto) throws BusinessException {
        log.info("当前管理员正在上映新电影 ，管理员账号为："+ BaseContext.getCurrentId());
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

    }
}
