package kitchenpos.order.application;

import kitchenpos.ServiceTest;
import kitchenpos.common.domain.EntityNotFoundException;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("주문 서비스 테스트")
class OrderServiceTest extends ServiceTest {

    @Test
    @DisplayName("주문을 등록한다.")
    void create() {
        // given
        OrderTableResponse savedOrderTableResponse = 테이블_저장(false);
        MenuResponse savedMenuResponse = 메뉴_저장();

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenuResponse.getId(), 2);
        OrderRequest orderRequest = new OrderRequest(
                savedOrderTableResponse.getId(), OrderStatus.COOKING.name(), Collections.singletonList(orderLineItemRequest));

        // when
        OrderResponse orderResponse = orderService.create(orderRequest);

        // then
        assertAll(
                () -> assertThat(orderResponse.getId()).isNotNull(),
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(orderRequest.getOrderStatus()),
                () -> assertThat(orderResponse.getOrderedTime()).isNotNull(),
                () -> assertThat(orderResponse.getOrderLineItems().get(0).getSeq()).isNotNull(),
                () -> assertThat(orderResponse.getOrderLineItems().get(0).getOrderId()).isEqualTo(orderResponse.getId()),
                () -> assertThat(orderResponse.getOrderLineItems().get(0).getMenuId()).isEqualTo(savedMenuResponse.getId()),
                () -> assertThat(orderResponse.getOrderLineItems().get(0).getQuantity()).isEqualTo(orderLineItemRequest.getQuantity())
        );
    }

    @Test
    @DisplayName("주문 항목 없이 주문을 등록하면 예외를 발생한다.")
    void createThrowException1() {
        // given
        OrderTableResponse savedOrderTableResponse = 테이블_저장(false);

        OrderRequest orderRequest = new OrderRequest(savedOrderTableResponse.getId(), OrderStatus.COOKING.name(), null);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> orderService.create(orderRequest));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴로 주문을 등록하면 예외를 발생한다.")
    void createThrowException2() {
        // given
        OrderTableResponse savedOrderTableResponse = 테이블_저장(false);

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(0L, 2);
        OrderRequest orderRequest = new OrderRequest(
                savedOrderTableResponse.getId(), OrderStatus.COOKING.name(), Collections.singletonList(orderLineItemRequest));

        // when & then
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> orderService.create(orderRequest))
                .withMessageMatching(EntityNotFoundException.MESSAGE.replace("%s", "\\w+"));
    }

    @Test
    @DisplayName("존재하지 않는 테이블로 주문을 등록하면 예외를 발생한다.")
    void createThrowException3() {
        // given
        MenuResponse savedMenuResponse = 메뉴_저장();

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenuResponse.getId(), 2);
        OrderRequest orderRequest = new OrderRequest(0L, OrderStatus.COOKING.name(), Collections.singletonList(orderLineItemRequest));

        // when & then
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> orderService.create(orderRequest))
                .withMessageMatching(EntityNotFoundException.MESSAGE.replace("%s", "\\w+"));
    }

    @Test
    @DisplayName("비어있는 테이블에 주문을 등록하면 예외를 발생한다.")
    void createThrowException4() {
        // given
        OrderTableResponse savedOrderTableResponse = 테이블_저장(true);
        MenuResponse savedMenuResponse = 메뉴_저장();

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(savedMenuResponse.getId(), 2);
        OrderRequest orderRequest = new OrderRequest(
                savedOrderTableResponse.getId(), OrderStatus.COOKING.name(), Collections.singletonList(orderLineItemRequest));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> orderService.create(orderRequest));
    }

    @Test
    @DisplayName("주문의 목록을 조회한다.")
    void list() {
        // given
        주문_저장();

        // when
        List<OrderResponse> orderResponses = orderService.list();

        // then
        assertThat(orderResponses.size()).isOne();
    }

    @Test
    @DisplayName("주문의 주문 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        OrderResponse savedOrderResponse = 주문_저장();

        OrderRequest orderRequest = new OrderRequest(OrderStatus.MEAL.name());

        // when
        OrderResponse orderResponse = orderService.changeOrderStatus(savedOrderResponse.getId(), orderRequest);

        // then
        assertThat(orderResponse.getOrderStatus()).isEqualTo(orderRequest.getOrderStatus());
    }

    @Test
    @DisplayName("존재하지 않는 주문 ID로 주문의 주문 상태를 변경하면 예외를 발생한다.")
    void changeOrderStatusThrowException1() {
        // given
        OrderRequest orderRequest = new OrderRequest(OrderStatus.MEAL.name());

        // when & then
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> orderService.changeOrderStatus(0L, orderRequest))
                .withMessageMatching(EntityNotFoundException.MESSAGE.replace("%s", "\\w+"));
    }

    @Test
    @DisplayName("주문 완료 상태인 주문의 주문 상태를 변경하면 예외를 발생한다.")
    void changeOrderStatusThrowException2() {
        // given
        OrderResponse savedOrderResponse = 주문_저장();
        주문_상태를_COMPLETION_으로_상태_변경(savedOrderResponse.getId());

        OrderRequest orderRequest = new OrderRequest(OrderStatus.MEAL.name());

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.changeOrderStatus(savedOrderResponse.getId(), orderRequest));
    }
}
