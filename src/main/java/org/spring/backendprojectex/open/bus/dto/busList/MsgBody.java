package org.spring.backendprojectex.open.bus.dto.busList;

import lombok.Data;

import java.util.List;

@Data
public class MsgBody {
    private List<BusItem> itemList;
}
