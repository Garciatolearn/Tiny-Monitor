package org.garcia.monitor.server.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class BasePO {

    @TableId(value = "id",type = IdType.AUTO)
    Long id;

    LocalDate createTime;

    LocalDate updateTime;

    Integer isDeleted;
}
