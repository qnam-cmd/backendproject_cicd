package org.spring.backendprojectex.open.bus.dto.busStation;

import lombok.Data;

import java.util.List;

@Data
public class MsgBody {
    private List<BusItemStation> itemList;
}
