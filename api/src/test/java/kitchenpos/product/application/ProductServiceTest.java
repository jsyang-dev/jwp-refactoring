package kitchenpos.product.application;

import kitchenpos.ServiceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("상품 서비스 테스트")
class ProductServiceTest extends ServiceTest {

    @Test
    @DisplayName("상품을 등록한다.")
    void create() {
        // given
        ProductRequest productRequest = new ProductRequest("후라이드치킨", new BigDecimal(16_000));

        // when
        ProductResponse productResponse = productService.create(productRequest);

        // then
        assertAll(
                () -> assertThat(productResponse.getId()).isNotNull(),
                () -> assertThat(productResponse.getName()).isEqualTo(productRequest.getName()),
                () -> assertThat(productResponse.getPrice().compareTo(productRequest.getPrice())).isZero()
        );
    }

    @Test
    @DisplayName("0보다 작은 가격으로 상품을 등록하면 예외를 발생한다.")
    void createThrowException() {
        // given
        ProductRequest productRequest = new ProductRequest("후라이드치킨", new BigDecimal(-1));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> productService.create(productRequest));
    }

    @Test
    @DisplayName("상품의 목록을 조회한다.")
    void list() {
        // given
        상품_저장();

        // when
        List<ProductResponse> productResponses = productService.list();

        // then
        assertThat(productResponses.size()).isOne();
    }
}
