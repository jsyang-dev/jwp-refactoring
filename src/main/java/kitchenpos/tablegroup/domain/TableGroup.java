package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    public static final String MESSAGE_VALIDATE = "테이블이 2개 이상이어야 합니다.";
    public static final String MESSAGE_VALIDATE_ORDER_TABLE = "테이블이 그룹에 등록 가능한 상태여야 합니다.";
    private static final int MIN_ORDER_TABLE_COUNT = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    public static TableGroup of(List<OrderTable> orderTables) {
        validate(orderTables);
        TableGroup tableGroup = new TableGroup();
        orderTables.forEach(it -> it.changeTableGroup(tableGroup));
        return tableGroup;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    public void ungroup() {
        orderTables.ungroup();
    }

    public void addToOrderTables(OrderTable orderTable) {
        orderTables.add(orderTable);
    }

    private static void validate(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_ORDER_TABLE_COUNT) {
            throw new IllegalArgumentException(MESSAGE_VALIDATE);
        }
        orderTables.forEach(TableGroup::validateOrderTable);
    }

    private static void validateOrderTable(OrderTable orderTable) {
        if (!orderTable.isGroupable()) {
            throw new IllegalArgumentException(MESSAGE_VALIDATE_ORDER_TABLE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id) && Objects.equals(createdDate, that.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate);
    }
}
