package net.xfolch.dtech.vendingmachine.domain.model.calculators;

import net.xfolch.dtech.vendingmachine.domain.model.CoinCalculator;

/**
 * Factory that provides domain instances of the {@link CoinCalculator}
 * <p>
 * Created by xfolch on 7/8/16.
 */
public final class CoinCalculatorFactory {

    private CoinCalculatorFactory() {
    }

    public static CoinCalculator highestValuedCoinCalculator() {
        return HighestValuedCoinCalculator.newOne();
    }

    public static CoinCalculator lowestValuedCoinCalculator() {
        return LowestValuedCoinCalculator.newOne();
    }

}
