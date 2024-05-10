package common.core.snowflake.core.snowflake;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Garcia
 * @version 1.0
 * @email 1603393839@qq.com
 * @date 2023/9/20
 **/
@Slf4j
public abstract class AbstractWorkIdChooseTemplate {
    @Value("${framework.distributed.id.snowflake.is-use-system-clock:false}")
    private boolean isUseSystemClock;

    protected abstract WorkIdWrapper chooseWorkId();

    public void chooseAndInit() {
        WorkIdWrapper wrapper = chooseWorkId();
        long workId = wrapper.getWorkId();
        long dataCenterId = wrapper.getDataCenterId();
        SnowFlake snowFlake = new SnowFlake(workId, dataCenterId);
        log.info("snowFlake type:{}, workId:{}, dataCenterId:{}", this.getClass().getName(), workId, dataCenterId);

    }
}
