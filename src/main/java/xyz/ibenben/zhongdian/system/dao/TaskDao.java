package xyz.ibenben.zhongdian.system.dao;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import xyz.ibenben.zhongdian.common.BaseDao;
import xyz.ibenben.zhongdian.system.entity.Task;

@Component
public interface TaskDao extends BaseDao<Task>{

}
