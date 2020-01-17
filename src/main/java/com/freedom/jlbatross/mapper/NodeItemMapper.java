package com.freedom.jlbatross.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.freedom.jlbatross.entity.NodeItem;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Auther: liuxiangtao90
 * @Date: 2020/1/16 14:14
 * @Description:
 */
@Mapper
@Repository
public interface NodeItemMapper extends BaseMapper<NodeItem> {
}
