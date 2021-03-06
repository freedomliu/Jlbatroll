package com.freedom.jlbatross.mapper;

import com.freedom.jlbatross.entity.Node;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuxiangtao90
 * @since 2020-01-17
 */
@Mapper
@Repository
public interface NodeMapper extends BaseMapper<Node> {

}
