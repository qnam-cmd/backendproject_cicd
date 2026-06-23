package org.spring.backendprojectex.open.bus.dto.busList;

import lombok.Data;

@Data
public class BusListResultResponse {
    private ComMsgHeader comMsgHeader;
    private MsgHeader msgHeader;
    private MsgBody msgBody;
}
