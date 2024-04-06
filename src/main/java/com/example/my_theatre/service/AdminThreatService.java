package com.example.my_theatre.service;

import com.example.my_theatre.entity.dto.ThreatDto;
import com.example.my_theatre.entity.vo.ThreatVo;

import java.util.List;

public interface AdminThreatService {
    List<ThreatVo> findAllfilm();

    void addFilm(ThreatDto threatDto);
}
