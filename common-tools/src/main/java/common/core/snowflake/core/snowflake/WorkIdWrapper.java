package common.core.snowflake.core.snowflake;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Garcia
 * @version 1.0
 * @email 1603393839@qq.com
 * @date 2023/9/20
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkIdWrapper {

    private Long workId;

    private Long dataCenterId;
}
