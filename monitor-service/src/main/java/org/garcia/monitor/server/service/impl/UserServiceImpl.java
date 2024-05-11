package org.garcia.monitor.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.garcia.monitor.server.dao.UserMapper;
import org.garcia.monitor.server.entity.po.UserPO;
import org.garcia.monitor.server.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements UserService {
    @Override
    public UserPO findUserByUsernameOrEmail(String nameOrEmail) {
        return this.query().eq("user_name",nameOrEmail)
                .or().eq("email",nameOrEmail).one();
    }
}
