package org.garcia.monitor.server.entity.po;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class BasePO {

    Long id;

    LocalDate createTime;

    LocalDate updateTime;

    Integer isDeleted;
}
