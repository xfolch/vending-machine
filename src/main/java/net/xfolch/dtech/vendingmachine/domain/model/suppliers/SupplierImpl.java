package net.xfolch.dtech.vendingmachine.domain.model.suppliers;

import net.xfolch.dtech.vendingmachine.domain.model.*;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

/**
 * Implementation of the supplier api.
 *
 * Created by xfolch on 7/8/16.
 */
final class SupplierImpl implements Supplier {

    private final CoinCalculator calculator;

    private SupplierImpl(CoinCalculator calculator) {
        this.calculator = calculator;
    }

    static SupplierImpl newOne(CoinCalculator calculator) {
        return new SupplierImpl(calculator);
    }

    @Override
    public VendingMachine.Builder newVendingMachine() {
        // decorator pattern using Java 8 libraries
        return UnaryOperator.<VendingMachine.Builder>identity()
                .andThen(ResetableVendingMachineBuilder::newOne)
                .apply(InMemoryVendingMachineBuilder.empty(calculator));
    }

    @Override
    public void reset(VendingMachine vendingMachine) {
        if (vendingMachine instanceof Resetable) {
            ((Resetable) vendingMachine).reset();
        }
    }

    /**
     * Mutable vending machine builder
     */
    private static final class ResetableVendingMachineBuilder implements VendingMachine.Builder {
        private VendingMachine.Builder builder;

        private ResetableVendingMachineBuilder(VendingMachine.Builder builder) {
            this.builder = builder;
        }

        static ResetableVendingMachineBuilder newOne(VendingMachine.Builder builder) {
            return new ResetableVendingMachineBuilder(builder);
        }

        @Override
        public VendingMachine.Builder addProduct(Product product, Integer numUnits) {
            builder = builder.addProduct(product, numUnits);
            return this;
        }

        @Override
        public VendingMachine.Builder setCash(List<Coin> cash) {
            builder = builder.setCash(cash);
            return this;
        }

        @Override
        public VendingMachine build() {
            return ResetableVendingMachine.newOne(builder);
        }
    }

    /**
     * Immutable vending machine builder
     */
    private static final class InMemoryVendingMachineBuilder implements VendingMachine.Builder {
        private final Set<ProductLine> products;
        private final List<Coin> cash;
        private final CoinCalculator calculator;

        private InMemoryVendingMachineBuilder(Set<ProductLine> products, List<Coin> cash, CoinCalculator calculator) {
            this.products = products;
            this.cash = cash;
            this.calculator = calculator;
        }

        static InMemoryVendingMachineBuilder empty(CoinCalculator calculator) {
            return new InMemoryVendingMachineBuilder(Collections.emptySet(), Collections.emptyList(), calculator);
        }

        @Override
        public VendingMachine.Builder addProduct(Product product, Integer numUnits) {
            if (product != null) {
                return copyAddingProduct(product, numUnits);
            } else {
                return this;
            }
        }

        @Override
        public VendingMachine.Builder setCash(List<Coin> cash) {
            if (cash != null) {
                return copySettingCash(cash);
            } else {
                return this;
            }
        }

        @Override
        public VendingMachine build() {
            return InMemoryVendingMachine.noCredit(products, cash, calculator);
        }

        private VendingMachine.Builder copySettingCash(List<Coin> newCash) {
            return new InMemoryVendingMachineBuilder(products, unmodifiableList(new ArrayList<>(newCash)), calculator);
        }

        private VendingMachine.Builder copyAddingProduct(Product product, Integer numUnits) {
            Set<ProductLine> newProducts = Stream.concat(products.stream(), Stream.of(ProductLine.newOne(product, numUnits)))
                    .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));

            return new InMemoryVendingMachineBuilder(newProducts, cash, calculator);
        }
    }

}
