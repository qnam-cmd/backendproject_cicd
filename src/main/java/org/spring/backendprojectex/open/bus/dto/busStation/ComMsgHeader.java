package org.spring.backendprojectex.open.bus.dto.busStation;

import lombok.Data;

@Data
public class ComMsgHeader {
    private String errMsg;
    private String requestMsgID;
    private String responseMsgID;
    private String responseTime;
    private String successYN;
    private String returnCode;

}
