package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@DisplayName("테이블 서비스 Mock 테스트")
class TableServiceMockTest extends ServiceTest {

    @MockBean
    private OrderDao orderDao;

    @Test
    @DisplayName("올바르지 않은 주문으로 테이블의 상태를 변경하면 예외를 발생한다.")
    void changeEmptyThrowException() {
        // given
        OrderTable savedOrderTable = 테이블_저장(false);
        OrderTable orderTable = new OrderTable(true);

        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTable));
    }
}
