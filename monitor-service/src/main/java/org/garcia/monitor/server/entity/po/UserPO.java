package org.garcia.monitor.server.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName("t_user_detail")
@NoArgsConstructor
public class UserPO extends BasePO{

    String userName;

    String alisa;

    String password;

    String email;

    String userRole;

    LocalDateTime registerTime;

}
