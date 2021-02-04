package wlw.zc.demo.system.dao;

import org.apache.ibatis.annotations.Mapper;
import wlw.zc.demo.system.entity.YbyLogDto;
@Mapper
public interface YbyLogDtoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(YbyLogDto record);

    int insertSelective(YbyLogDto record);

    YbyLogDto selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(YbyLogDto record);

    int updateByPrimaryKey(YbyLogDto record);
}