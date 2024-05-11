package org.garcia.monitor.server.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_user_detail")
@Builder
public class UserDetailPO extends BasePO{

    String userName;

    String alisa;

    String password;

    String userRole;

}
