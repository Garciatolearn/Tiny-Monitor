
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import common.core.jwt.JWTutils;
import common.core.oshi.OshiUtil;
import common.core.oshi.entity.ComputerDetail;
import jakarta.annotation.Resource;
import org.garcia.monitor.server.ServerApplication;
import org.garcia.monitor.server.dao.UserDetailMapper;
import org.garcia.monitor.server.entity.po.UserDetailPO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.sql.Wrapper;
import java.util.Properties;

@SpringBootTest(classes = {ServerApplication.class})
public class MapperTest {

    @Resource
    UserDetailMapper userDetailMapper;

    @Test
    public void test(){
//        int insert = userDetailMapper.insert(UserDetailPO.builder()
//                        .userName("1603393839")
//                        .alisa("小陈")
//                        .password("123456")
//                .build());
        QueryWrapper<UserDetailPO> objectQueryWrapper = new QueryWrapper<>();

        System.out.println(userDetailMapper.selectOne(objectQueryWrapper.eq("alisa","小陈")));
    }

    @Test
    public void system(){
        SystemInfo info = new SystemInfo();
        HardwareAbstractionLayer hardware = info.getHardware();
        CentralProcessor processor = hardware.getProcessor();
        Properties properties = System.getProperties();
        OperatingSystem operatingSystem = info.getOperatingSystem();
//        properties.entrySet().forEach(ecah -> System.out.println(ecah));
        OshiUtil oshiUtil = new OshiUtil();
        ComputerDetail computerDetail = oshiUtil.getComputerDetail();
        System.out.println(computerDetail);

    }

    @Resource
    JWTutils jwTutils;
    @Resource
    OshiUtil oshiUtil;
    @Test
    public void token(){
        ComputerDetail computerDetail = oshiUtil.getComputerDetail();
        String token = jwTutils.createToken(computerDetail);
        System.out.println(token);
        System.out.println(jwTutils.toObject(token,new ComputerDetail()));
    }
}
