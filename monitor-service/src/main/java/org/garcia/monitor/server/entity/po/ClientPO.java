package org.garcia.monitor.server.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_client")
public class ClientPO extends BasePO{

     String name;

     String registerToken;

     Integer clientSequence;

}
