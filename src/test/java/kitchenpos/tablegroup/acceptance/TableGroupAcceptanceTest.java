package kitchenpos.tablegroup.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.ordertable.acceptance.OrderTableAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("테이블 그룹 인수 테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse orderTableResponse1;
    private OrderTableResponse orderTableResponse2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        orderTableResponse1 = 테이블_등록되어_있음(new OrderTableRequest(2, true));
        orderTableResponse2 = 테이블_등록되어_있음(new OrderTableRequest(2, true));
    }

    @Test
    @DisplayName("테이블 그룹을 등록한다.")
    void create() {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                LocalDateTime.now(), Arrays.asList(orderTableResponse1.getId(), orderTableResponse2.getId()));

        // when
        ExtractableResponse<Response> response = 테이블_그룹_등록_요청(tableGroupRequest);

        // then
        테이블_그룹_등록됨(response);
    }

    @Test
    @DisplayName("테이블 그룹에서 테이블을 제거한다.")
    void list() {
        // given
        TableGroupResponse savedTableGroupResponse = 테이블_그룹_등록되어_있음(
                new TableGroupRequest(
                        LocalDateTime.now(), Arrays.asList(orderTableResponse1.getId(), orderTableResponse2.getId())));

        // when
        ExtractableResponse<Response> response = 테이블_그룹에서_테이블_제거_요청(savedTableGroupResponse.getId());

        // then
        테이블_그룹에서_테이블_제거됨(response);
    }

    public static TableGroupResponse 테이블_그룹_등록되어_있음(TableGroupRequest tableGroupRequest) {
        return post("/api/table-groups", tableGroupRequest).as(TableGroupResponse.class);
    }

    public static ExtractableResponse<Response> 테이블_그룹_등록_요청(TableGroupRequest tableGroupRequest) {
        return post("/api/table-groups", tableGroupRequest);
    }

    public static ExtractableResponse<Response> 테이블_그룹에서_테이블_제거_요청(Long id) {
        return delete("/api/table-groups/{tableGroupId}", id);
    }

    private void 테이블_그룹_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 테이블_그룹에서_테이블_제거됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
