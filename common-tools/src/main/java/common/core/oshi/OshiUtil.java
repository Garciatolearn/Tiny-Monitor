package common.core.oshi;
import common.core.oshi.entity.ComputerDetail;
import common.core.oshi.entity.RuntimeDetail;
import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;

import java.net.NetworkInterface;
import java.util.List;
import java.util.Properties;

@Slf4j
public class OshiUtil {
    private final SystemInfo systemInfo = new SystemInfo();

    private final Properties properties = System.getProperties();

    private final double SIZE_BASE = 1;

    private final double KB = 1024 * SIZE_BASE;

    private final double MB = 1024 * KB;

    private final double GB = 1024 * MB;

    public ComputerDetail getComputerDetail(){
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        List<HWDiskStore> diskStores = hardware.getDiskStores();
        NetworkIF address = getAddress(hardware.getNetworkIFs());
        double disk = diskStores.stream().mapToLong(HWDiskStore::getSize).sum() / GB;
        return new ComputerDetail().setOsArch(properties.getProperty("os.arch"))
                .setOsBit(operatingSystem.getBitness())
                .setOsName(operatingSystem.getFamily())
                .setOsVersion(operatingSystem.getVersionInfo().getVersion())
                .setCpuName(hardware.getProcessor().getProcessorIdentifier().getName())
                .setCpuCores(hardware.getProcessor().getLogicalProcessorCount())
                .setMemory(hardware.getMemory().getTotal()/GB)
                .setDiskMemory(disk)
                .setIpv4(address.getIPv4addr()[0]);
    }

    private NetworkIF getAddress(List<NetworkIF> NetworkIFS){
        try{
            for(NetworkIF networkIF : NetworkIFS){
                String[] iPv4addr = networkIF.getIPv4addr();
                NetworkInterface networkInterface = networkIF.queryNetworkInterface();
                //网卡信息:不能是本地回环,p2p,虚拟的,前缀是eth或者en
                if(!networkInterface.isLoopback() && !networkInterface.isPointToPoint()
                && networkInterface.isUp() && !networkInterface.isVirtual()
                        && (networkInterface.getName().startsWith("eth") ||
                        networkInterface.getName().startsWith("en"))
                ) return networkIF;
            }
        } catch (Exception e){
            log.info("服务器ip获取异常: {}",e.getMessage());
        }
        return null;
    }

    public RuntimeDetail getRuntimeDetail(){
        return null;
    }
}
