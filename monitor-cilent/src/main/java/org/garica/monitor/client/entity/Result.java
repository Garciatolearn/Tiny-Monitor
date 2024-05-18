package org.garica.monitor.client.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public final class Result<T> {

    public static final String SUCCESS_CODE = "0";

    //todo 枚举类创建错误码的类型(服务端,客户端,第三方.....)

    public static final String ERROR_CODE = "1";

    public static final String NET_ERROR_CODE = "2";

    public static final String NET_SUCCESS_CODE = "3";

    private String code;

    private String message;

    private T data;

}
