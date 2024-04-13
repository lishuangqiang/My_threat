package com.example.my_theatre.controller.admin;
import com.example.my_theatre.common.BaseResponse;
import com.example.my_theatre.common.ResultUtils;
import com.example.my_theatre.entity.dto.ThreatDto;
import com.example.my_theatre.entity.enums.ErrorCode;
import com.example.my_theatre.entity.vo.ThreatVo;
import com.example.my_theatre.exception.BusinessException;
import com.example.my_theatre.service.impl.AdminThreatServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/admin/Threat")
public class adminThreatController {
    @Resource
    private AdminThreatServiceImpl adminThreatService;

    /**
     * 查询所有上映电影
     * @return
     */
    @GetMapping("/findFilm")
    public BaseResponse<List<ThreatVo>> findFilm() {
        log.info("当前管理员正在查询所有上映电影");
        List<ThreatVo> lists;
        try {
            lists = adminThreatService.findAllfilm();

        } catch (BusinessException e) {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }
        return ResultUtils.success(lists);
    }

    /**
     * 上映电影
     * @param threatDto
     * @return
     */
    @PostMapping("/addFilm")
    public BaseResponse<String> addFilm(@RequestBody ThreatDto threatDto) {
        log.info("当前管理员正在尝试添加上映电影，电影名称为:"+threatDto.getMovieName());
        if (threatDto == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        try {

            adminThreatService.addFilm(threatDto);

        }
        catch (BusinessException e)
        {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }
        return ResultUtils.success("上映电影成功");
    }

    /**
     * 下映电影
     */
    @PostMapping("/delFilm")
    public BaseResponse<String>delFilm(@RequestBody ThreatDto threatDto)
    {
        log.info("当前管理员正在尝试下映电影");
        if (threatDto == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        try {
            adminThreatService.delFilm(threatDto);
        }
        catch (BusinessException e)
        {
            return ResultUtils.error(e.getCode(), e.getMessage());
        }
        return ResultUtils.success("下映成功");
    }
}






