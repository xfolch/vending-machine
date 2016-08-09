package net.xfolch.dtech.vendingmachine.domain.model.suppliers;

import net.xfolch.dtech.vendingmachine.domain.model.Coin;
import net.xfolch.dtech.vendingmachine.domain.model.Purchase;
import net.xfolch.dtech.vendingmachine.domain.model.Try;
import net.xfolch.dtech.vendingmachine.domain.model.VendingMachine;

import java.util.List;

/**
 * Represents a vending machine that can be reset. Reset action means that a vending machine
 * returns to the original state.
 * <p>
 * It is important that the builder got by factory method be immutable. Otherwise, this approach
 * would not work. In our domain model we guarantees that.
 * <p>
 * Created by xfolch on 7/8/16.
 */
final class ResetableVendingMachine implements VendingMachine, Resetable {

    private final VendingMachine.Builder builder;
    private VendingMachine vendingMachine;

    private ResetableVendingMachine(VendingMachine.Builder builder) {
        this.builder = builder;
        this.vendingMachine = builder.build();
    }

    static ResetableVendingMachine newOne(VendingMachine.Builder builder) {
        return new ResetableVendingMachine(builder);
    }

    @Override
    public VendingMachine insertCoin(Coin coin) {
        vendingMachine = vendingMachine.insertCoin(coin);
        return this;
    }

    @Override
    public Try<Purchase> selectProduct(Integer productId) {
        return vendingMachine.selectProduct(productId);
    }

    @Override
    public List<Coin> cancel() {
        return vendingMachine.cancel();
    }

    @Override
    public void reset() {
        vendingMachine = builder.build();
    }

}
