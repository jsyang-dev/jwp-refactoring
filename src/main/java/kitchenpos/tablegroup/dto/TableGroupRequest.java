package kitchenpos.tablegroup.dto;

import java.util.List;

public class TableGroupRequest {

    private List<Long> orderTableIds;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public void setOrderTableIds(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }
}
