package common.core.snowflake.core.snowflake;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Garcia
 * @version 1.0
 * @email 1603393839@qq.com
 * @date 2023/9/21
 **/
@Slf4j
public class RandomWorkIdChoose extends AbstractWorkIdChooseTemplate implements InitializingBean {
    @Override
    protected WorkIdWrapper chooseWorkId() {
        int start = 0, end = 31;
        return new WorkIdWrapper(getRandom(start, end), getRandom(start, end));
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        chooseAndInit();
    }

    private long getRandom(int start, int end) {
        long random = (long) (Math.random() * (end - start + 1) + start);
        return random;
    }
}
