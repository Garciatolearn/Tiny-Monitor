package org.garcia.monitor.server.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("computer_detail")
public class ComputerDetailPO extends BasePO {

    String clientSequence;

    String osArch;

    String osName;

    String osVersion;

    Integer osBit;

    String cpuName;

    Integer cpuCores;

    Double memory;

    Double diskMemory;

    String ipv4;
}