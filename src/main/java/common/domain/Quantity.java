package common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Quantity {

    @Column
    private long quantity;

    protected Quantity() {
    }

    public Quantity(long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }

    private void validate(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity1 = (Quantity) o;
        return quantity == quantity1.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
