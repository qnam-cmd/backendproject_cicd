package org.spring.backendprojectex.open.bus.dto.busStation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusStationResultResponse {
    private ComMsgHeader comMsgHeader;
    private MsgHeader msgHeader;
    private MsgBody msgBody;
}
