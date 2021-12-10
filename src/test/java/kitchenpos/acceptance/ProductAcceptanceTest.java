package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;

@DisplayName("상품 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("상품을 등록한다.")
    void create() {
        // given
        Product product = new Product("매운양념치킨", 18_000);

        // when
        ExtractableResponse<Response> response = 상품_등록_요청(product);

        // then
        상품_등록됨(response);
    }

    @Test
    @DisplayName("상품의 목록을 조회한다.")
    void list() {
        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_목록_조회됨(response);
    }

    public static ExtractableResponse<Response> 상품_등록_요청(Product product) {
        return post("/api/products", product);
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return get("/api/products");
    }

    private void 상품_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", Product.class).size()).isPositive();
    }
}
