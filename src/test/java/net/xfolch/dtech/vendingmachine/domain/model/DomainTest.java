package net.xfolch.dtech.vendingmachine.domain.model;

import net.xfolch.dtech.vendingmachine.domain.model.calculators.CoinCalculatorFactory;
import net.xfolch.dtech.vendingmachine.domain.model.suppliers.SupplierFactory;

import java.math.BigDecimal;

/**
 * Created by xfolch on 6/8/16.
 */
abstract class DomainTest {

    final Product Coke = Product.builder()
            .setId(1)
            .setName("Coke")
            .setPrice(new BigDecimal(1.50))
            .build();

    final Product Pepsi = Product.builder()
            .setId(2)
            .setName("Pepsi")
            .setPrice(new BigDecimal(1.45))
            .build();

    final Product Water = Product.builder()
            .setId(3)
            .setName("Water")
            .setPrice(new BigDecimal(0.90))
            .build();

    final Supplier niceSupplier = SupplierFactory.niceSupplier();

    final Supplier hatefulSupplier = SupplierFactory.forTesting(CoinCalculatorFactory.lowestValuedCoinCalculator());

}
