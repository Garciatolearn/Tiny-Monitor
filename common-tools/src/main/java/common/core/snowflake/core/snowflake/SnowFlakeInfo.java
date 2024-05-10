package common.core.snowflake.core.snowflake;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Garcia
 * @version 1.0
 * @email 1603393839@qq.com
 * @date 2023/9/21
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SnowFlakeInfo {

    private Long timeStamp;

    private Integer dataCenterId;

    private Integer workerId;

    private Integer sequence;


}
