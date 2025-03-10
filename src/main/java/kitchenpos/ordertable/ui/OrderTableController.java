package kitchenpos.ordertable.ui;

import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class OrderTableController {

    private final OrderTableService orderTableService;

    public OrderTableController(final OrderTableService orderTableService) {
        this.orderTableService = orderTableService;
    }

    @PostMapping
    public ResponseEntity<OrderTableResponse> create(@RequestBody final OrderTableRequest orderTableRequest) {
        final OrderTableResponse orderTableResponse = orderTableService.create(orderTableRequest);
        final URI uri = URI.create("/api/tables/" + orderTableResponse.getId());
        return ResponseEntity.created(uri).body(orderTableResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok().body(orderTableService.list());
    }

    @PutMapping("/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable final Long orderTableId,
                                                          @RequestBody final OrderTableRequest orderTableRequest) {
        return ResponseEntity.ok().body(orderTableService.changeEmpty(orderTableId, orderTableRequest));
    }

    @PutMapping("/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                                   @RequestBody final OrderTableRequest orderTableRequest) {
        return ResponseEntity.ok().body(orderTableService.changeNumberOfGuests(orderTableId, orderTableRequest));
    }
}
