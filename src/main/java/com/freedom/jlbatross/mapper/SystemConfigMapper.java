package com.freedom.jlbatross.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.freedom.jlbatross.entity.SystemConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Auther: liuxiangtao90
 * @Date: 2020/1/16 17:44
 * @Description:
 */
@Mapper
@Repository
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {
}
