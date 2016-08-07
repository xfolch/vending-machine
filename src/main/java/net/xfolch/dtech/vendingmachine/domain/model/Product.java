package net.xfolch.dtech.vendingmachine.domain.model;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Objects;

import static java.math.BigDecimal.ROUND_FLOOR;

/**
 * Immutable class that represents a product that a vending machine holds
 * <p>
 * This class assumes that a product is identified by an id, and such id is unique
 * <p>
 * Created by xfolch on 7/8/16.
 */
public final class Product {

    private final Integer id;
    private final String name;
    private final Integer units;
    private final BigDecimal price;

    /**
     * Owns the construction of its instances by means of the builder
     */
    private Product(Integer id, String name, Integer units, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.units = units;
        this.price = price.setScale(2, ROUND_FLOOR);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getUnits() {
        return units;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder copy() {
        return builder()
                .setId(id)
                .setName(name)
                .setUnits(units)
                .setPrice(price);
    }

    /**
     * Mutable builder pattern to construct a Product instance
     */
    public static final class Builder {
        private Integer id;
        private String name;
        private Integer units;
        private BigDecimal price;

        private Builder() {
        }

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder setUnits(Integer units) {
            this.units = units;
            return this;
        }

        public Product build() {
            return new Product(id, name, units, price);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}({1})", name, id);
    }
}
