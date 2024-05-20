package org.garcia.monitor.server.service.impl;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.garcia.monitor.server.dao.ClientMapper;
import org.garcia.monitor.server.entity.po.ClientPO;
import org.garcia.monitor.server.service.ClientService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
@Slf4j
public class ClientServiceImpl extends ServiceImpl<ClientMapper, ClientPO> implements ClientService {

    String registerToken = this.generateNewToken();

    Set<String> registerTokens = new ConcurrentSkipListSet<>();

    LambdaQueryWrapper<ClientPO> lambdaQueryWrapper = new LambdaQueryWrapper<>();


    LoadingCache<Integer,ClientPO> sequenceCache = Caffeine.newBuilder().maximumSize(10_000)
            .build(key -> this.getOne(lambdaQueryWrapper.eq(ClientPO::getClientSequence,key)));
    LoadingCache<String,ClientPO> registerCache = Caffeine.newBuilder().maximumSize(10_000)
            .build(key -> this.getOne(lambdaQueryWrapper.eq(ClientPO::getRegisterToken,key)));

    @Override
    public boolean verifyRegisterToken(@NotNull String token) {
        //保证多个主机同时注册时的并发安全
        if(registerTokens.contains(token)){
            //todo 修改成唯一id
            int clientSequence = RandomUtil.randomInt(1_0000_0000,10_0000_0001);
            ClientPO clientPO = new ClientPO("未命名主机",token,clientSequence);
            if (this.save(clientPO)){
                sequenceCache.put(clientSequence,clientPO);
                registerCache.put(registerToken,clientPO);
                registerTokens.remove(token);
                return true;
            }
        } else if (registerCache.get(token) != null){
            return true;
        }
        log.error("注册失败,并没有 \"{}\" 该注册码",token);
        return false;
    }

    @Override
    public String createRegisterToken() {
        String result = this.registerToken;
        registerToken = this.generateNewToken();
        registerTokens.add(result);
        return result;
    }

    @PostConstruct
    public void initCache(){
        sequenceCache.invalidateAll();
        registerCache.invalidateAll();
        this.list().forEach(each -> {
            this.addSequenceCache(each);
//            this.addRegisterCache(each);
        });
    }

    private String generateNewToken() {
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(24);
        for (int i = 0; i < 24; i++)
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        return sb.toString();
    }

    private void addSequenceCache(ClientPO entity){
        sequenceCache.put(entity.getClientSequence(),entity);
    }

    private void addRegisterCache(ClientPO entity){
        registerCache.put(entity.getRegisterToken(),entity);
    }
}
