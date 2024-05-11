
import common.core.jwt.JWTutils;
import common.core.oshi.OshiUtils;
import common.core.oshi.entity.ComputerDetail;
import jakarta.annotation.Resource;
import org.garcia.monitor.server.ServerApplication;
import org.garcia.monitor.server.dao.UserMapper;
import org.garcia.monitor.server.entity.po.UserPO;
import org.garcia.monitor.server.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.util.Properties;

@SpringBootTest(classes = {ServerApplication.class})
public class MapperTest {

    @Resource
    UserService userService;

    @Resource
    BCryptPasswordEncoder encoder;

    @Test
    public void test(){
        System.out.println(userService.findUserByUsernameOrEmail("root"));

    }

    @Test
    public void system(){
        SystemInfo info = new SystemInfo();
        HardwareAbstractionLayer hardware = info.getHardware();
        CentralProcessor processor = hardware.getProcessor();
        Properties properties = System.getProperties();
        OperatingSystem operatingSystem = info.getOperatingSystem();
//        properties.entrySet().forEach(ecah -> System.out.println(ecah));
        OshiUtils oshiUtils = new OshiUtils();
        ComputerDetail computerDetail = oshiUtils.getComputerDetail();
        System.out.println(computerDetail);

    }

    @Resource
    JWTutils jwTutils;
    @Resource
    OshiUtils oshiUtils;
    @Test
    public void token(){
        ComputerDetail computerDetail = oshiUtils.getComputerDetail();
        String token = jwTutils.createToken(computerDetail);
        System.out.println(token);
        System.out.println(jwTutils.toObject(token,new ComputerDetail()));
    }
}
