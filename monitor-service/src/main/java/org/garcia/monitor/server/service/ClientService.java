package org.garcia.monitor.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.garcia.monitor.server.entity.po.ClientPO;

public interface ClientService extends IService<ClientPO> {

    boolean verifyRegisterToken(String token);



}
