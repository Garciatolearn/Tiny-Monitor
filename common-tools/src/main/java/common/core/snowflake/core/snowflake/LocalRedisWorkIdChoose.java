package common.core.snowflake.core.snowflake;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Garcia
 * @version 1.0
 * @email 1603393839@qq.com
 * @date 2023/9/21
 **/
@Slf4j
public class LocalRedisWorkIdChoose extends AbstractWorkIdChooseTemplate implements InitializingBean {

    private final ApplicationContext context;

    private RedisTemplate redisTemplate;

    public LocalRedisWorkIdChoose(ApplicationContext context){
        this.context = context;
        this.redisTemplate = context.getBean(RedisTemplate.class);
    }

    @Override
    protected WorkIdWrapper chooseWorkId() {
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new EncodedResource(new ClassPathResource("lua/chooseWorkIdLua.lua"))));
        List<Long> luaResultList = null;
        try {
            redisScript.setResultType(List.class);
            luaResultList = (ArrayList) this.redisTemplate.execute(redisScript, null);
        } catch (Exception ex){
            log.error("lua脚本获取id失败",ex);
        }
        return CollUtil.isEmpty(luaResultList) ? new RandomWorkIdChoose().chooseWorkId() : new WorkIdWrapper(luaResultList.get(0), luaResultList.get(1));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        chooseAndInit();
    }
}
