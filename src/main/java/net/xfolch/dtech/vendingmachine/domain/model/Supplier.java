package net.xfolch.dtech.vendingmachine.domain.model;

/**
 * Represents a vending machine supplier, who is responsible for providing vending machines
 * and maintaining them
 * <p>
 * Created by xfolch on 7/8/16.
 */
public interface Supplier {

    /**
     * @return a builder in which the consumer will be able to configure the final vending machine
     */
    VendingMachine.Builder newVendingMachine();

    /**
     * As supplier, they have the ability to reset their vending machines, which means that
     * the given vending machine will return to the original state (same products, cash and so forth)
     *
     * @param vendingMachine to be reset
     */
    void reset(VendingMachine vendingMachine);

}
