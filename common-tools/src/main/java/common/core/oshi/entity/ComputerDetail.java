package common.core.oshi.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ComputerDetail {

    String osArch;

    String osName;

    String osVersion;

    Integer osBit;

    String cpuName;

    Integer cpuCores;

    Double memory;

    Double diskMemory;

    String ipv4;
}
