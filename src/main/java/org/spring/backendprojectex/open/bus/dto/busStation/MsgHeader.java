package org.spring.backendprojectex.open.bus.dto.busStation;

import lombok.Data;

@Data
public class MsgHeader {
    private String headerMsg;
    private String headerCd;
    private String itemCount;
}
