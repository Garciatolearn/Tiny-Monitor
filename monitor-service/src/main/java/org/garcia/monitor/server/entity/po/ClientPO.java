package org.garcia.monitor.server.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientPO extends BasePO{

     String name;

     String registerToken;

     Integer clientSequence;

}
