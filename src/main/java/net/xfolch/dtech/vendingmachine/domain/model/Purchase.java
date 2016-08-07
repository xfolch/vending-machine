package net.xfolch.dtech.vendingmachine.domain.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

/**
 * Represents a purchase made in a vending machine.
 * <p>
 * This will be the value object that a consumer will get as a selection result.
 * <p>
 * Created by xfolch on 7/8/16.
 */
public final class Purchase {

    private final Product product;
    private final List<Coin> remaining;

    private Purchase(Product product, List<Coin> remaining) {
        this.product = product;
        this.remaining = remaining;
    }

    public Product getProduct() {
        return product;
    }

    public List<Coin> getRemaining() {
        return remaining;
    }

    /**
     * @return a mutable builder to make a Purchase instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Mutable builder
     */
    public static final class Builder {
        Product product;
        List<Coin> remaining = Collections.emptyList();

        private Builder() {
        }

        public Builder setProduct(Product product) {
            this.product = product;
            return this;
        }

        public Builder setRemaining(List<Coin> remaining) {
            if (remaining != null) {
                this.remaining = Collections.unmodifiableList(new ArrayList<>(remaining));
            }

            return this;
        }

        public Purchase build() {
            return new Purchase(product, remaining);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase purchase = (Purchase) o;
        return Objects.equals(product, purchase.product) &&
                Objects.equals(remaining, purchase.remaining);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, remaining);
    }

    @Override
    public String toString() {
        return MessageFormat.format("Purchase({0}, {1})", product, remaining.stream().map(Objects::toString).collect(joining(", ")));
    }
}
