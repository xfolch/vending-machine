package net.xfolch.dtech.vendingmachine.domain.model.suppliers;

import net.xfolch.dtech.vendingmachine.domain.model.Product;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * Immutable class that represents a product line within a vending machine
 * <p>
 * Created by xfolch on 7/8/16.
 */
final class ProductLine {

    private final Product product;
    private final Integer numUnits;

    private ProductLine(Product product, Integer numUnits) {
        this.product = product;
        this.numUnits = numUnits;
    }

    /**
     * Smart constructor that has a private-package access
     */
    static ProductLine newOne(Product product, Integer numUnits) {
        return new ProductLine(product, numUnits != null ? numUnits : 0);
    }

    Product getProduct() {
        return product;
    }

    Integer getNumUnits() {
        return numUnits;
    }

    boolean hasUnits() {
        return numUnits > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductLine that = (ProductLine) o;
        return Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0} units of {1}", numUnits, product);
    }

}
