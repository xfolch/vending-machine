package net.xfolch.dtech.vendingmachine.domain.model.suppliers;

import net.xfolch.dtech.vendingmachine.domain.model.CoinCalculator;
import net.xfolch.dtech.vendingmachine.domain.model.Supplier;
import net.xfolch.dtech.vendingmachine.domain.model.calculators.CoinCalculatorFactory;

/**
 * Public singleton factory that provides different supplier instances to our domain model
 * <p>
 * Created by xfolch on 7/8/16.
 */
public final class SupplierFactory {

    private SupplierFactory() {
    }

    /**
     * @return a supplier who will provide vending machines that will have a highest-valued coin strategy for remaining change
     */
    public static Supplier niceSupplier() {
        return SupplierImpl.newOne(CoinCalculatorFactory.highestValuedCoinCalculator());
    }

    /**
     * Returns a supplier for testing, who will be configured by the given coin calculator
     *
     * @param calculator used by Supplier to configure their vending machines
     * @return a supplier for testing
     */
    public static Supplier forTesting(CoinCalculator calculator) {
        return SupplierImpl.newOne(calculator);
    }

}
