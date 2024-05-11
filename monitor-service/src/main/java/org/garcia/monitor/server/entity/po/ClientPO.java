package org.garcia.monitor.server.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("t_client")
public class ClientPO extends BasePO{

     String name;

     String registerToken;

     Integer clientSequence;

}
