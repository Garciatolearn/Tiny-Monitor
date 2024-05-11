package org.garcia.monitor.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.garcia.monitor.server.entity.po.UserPO;

public interface UserService extends IService<UserPO> {

    UserPO findUserByUsernameOrEmail(String nameOrEmail);
}
