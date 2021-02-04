package wlw.zc.demo.system.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class YbyLog {
    //id
    private String id;
    //操作人姓名
    private String userName;
    //操作人账号
    private String account;
    //菜单
    private String menu;
    //操作行为
    private String operation;
    //方法
    private String method;
    //入参
    private String params;
    //数据变更前后
    private String data;
    //操作人ip
    private String ip;
    //记录创建时间
    private Date createTime;

}
