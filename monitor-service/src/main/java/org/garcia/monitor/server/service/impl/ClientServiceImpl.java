package org.garcia.monitor.server.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.validation.constraints.NotNull;
import org.garcia.monitor.server.dao.ClientMapper;
import org.garcia.monitor.server.entity.po.ClientPO;
import org.garcia.monitor.server.service.ClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, ClientPO> implements ClientService {

    @Value("${register-token}")
    String registerToken;


    @Override
    public boolean verifyRegisterToken(@NotNull String token) {
        if(token.equals(registerToken)){
            int clientSequence = RandomUtil.randomInt(1_0000_0000,10_0000_0001);

        }
        return false;
    }

}
