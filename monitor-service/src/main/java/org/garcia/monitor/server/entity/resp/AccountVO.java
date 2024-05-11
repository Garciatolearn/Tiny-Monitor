package org.garcia.monitor.server.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class AccountVO {

    private String username;

    private Long id;
}
