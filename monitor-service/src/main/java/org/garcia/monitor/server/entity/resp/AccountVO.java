package org.garcia.monitor.server.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountVO {

    private String username;

    private Long id;

    private String userRole;
}
