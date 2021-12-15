package kitchenpos.menu.application;

import kitchenpos.ServiceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("메뉴 서비스 테스트")
public class MenuServiceTest extends ServiceTest {

    @Test
    @DisplayName("메뉴을 등록한다.")
    void create() {
        // given
        ProductResponse savedProductResponse = 상품_저장();
        MenuGroupResponse savedMenuGroupResponse = 메뉴_그룹_저장();

        MenuProductRequest menuProductRequest = new MenuProductRequest(savedProductResponse.getId(), 1);
        MenuRequest menuRequest = new MenuRequest(
                savedProductResponse.getName(), savedProductResponse.getPrice(), savedMenuGroupResponse.getId(),
                Collections.singletonList(menuProductRequest));

        // when
        MenuResponse menuResponse = menuService.create(menuRequest);

        // then
        assertAll(
                () -> assertThat(menuResponse.getId()).isNotNull(),
                () -> assertThat(menuResponse.getName()).isEqualTo(savedProductResponse.getName()),
                () -> assertThat(menuResponse.getPrice().compareTo(menuRequest.getPrice())).isZero(),
                () -> assertThat(menuResponse.getMenuGroupId()).isEqualTo(savedMenuGroupResponse.getId()),
                () -> assertThat(menuResponse.getMenuProducts().get(0).getSeq()).isNotNull(),
                () -> assertThat(menuResponse.getMenuProducts().get(0).getMenuId()).isEqualTo(menuResponse.getId()),
                () -> assertThat(menuResponse.getMenuProducts().get(0).getProductId()).isEqualTo(savedProductResponse.getId()),
                () -> assertThat(menuResponse.getMenuProducts().get(0).getQuantity()).isEqualTo(menuProductRequest.getQuantity())
        );
    }

    @Test
    @DisplayName("0원 이하의 가격으로 메뉴을 등록하면 예외를 발생한다.")
    void createThrowException1() {
        // given
        ProductResponse savedProductResponse = 상품_저장();
        MenuGroupResponse savedMenuGroupResponse = 메뉴_그룹_저장();

        MenuProductRequest menuProductRequest = new MenuProductRequest(savedProductResponse.getId(), 1);
        MenuRequest menuRequest = new MenuRequest(
                savedProductResponse.getName(), new BigDecimal(-1), savedMenuGroupResponse.getId(),
                Collections.singletonList(menuProductRequest));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> menuService.create(menuRequest));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 그룹 ID로 메뉴을 등록하면 예외를 발생한다.")
    void createThrowException2() {
        // given
        ProductResponse savedProductResponse = 상품_저장();

        MenuProductRequest menuProductRequest = new MenuProductRequest(savedProductResponse.getId(), 1);
        MenuRequest menuRequest = new MenuRequest(
                savedProductResponse.getName(), savedProductResponse.getPrice(), 0L,
                Collections.singletonList(menuProductRequest));

        // when & then
        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() -> menuService.create(menuRequest));
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID로 메뉴을 등록하면 예외를 발생한다.")
    void createThrowException3() {
        // given
        ProductResponse savedProductResponse = 상품_저장();
        MenuGroupResponse savedMenuGroupResponse = 메뉴_그룹_저장();

        MenuProductRequest menuProductRequest = new MenuProductRequest(0L, 1);
        MenuRequest menuRequest = new MenuRequest(
                savedProductResponse.getName(), savedProductResponse.getPrice(), savedMenuGroupResponse.getId(),
                Collections.singletonList(menuProductRequest));

        // when & then
        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() -> menuService.create(menuRequest));
    }

    @Test
    @DisplayName("상품들의 합계 금액과 일치하지 않는 가격으로 메뉴을 등록하면 예외를 발생한다.")
    void createThrowException4() {
        // given
        ProductResponse savedProductResponse = 상품_저장();
        MenuGroupResponse savedMenuGroupResponse = 메뉴_그룹_저장();

        MenuProductRequest menuProductRequest = new MenuProductRequest(savedProductResponse.getId(), 1);
        MenuRequest menuRequest = new MenuRequest(
                savedProductResponse.getName(), savedProductResponse.getPrice().add(BigDecimal.ONE), savedMenuGroupResponse.getId(),
                Collections.singletonList(menuProductRequest));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> menuService.create(menuRequest));
    }

    @Test
    @DisplayName("메뉴의 목록을 조회한다.")
    void list() {
        // given
        메뉴_저장();

        // when
        List<MenuResponse> menuResponses = menuService.list();

        // then
        assertThat(menuResponses.size()).isOne();
    }
}
