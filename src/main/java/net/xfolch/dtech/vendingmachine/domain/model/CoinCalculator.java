package net.xfolch.dtech.vendingmachine.domain.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * Responsible for doing some calculations over coins
 * <p>
 * Created by xfolch on 7/8/16.
 */
public interface CoinCalculator {

    /**
     * Calculates the total amount of the given coins
     */
    BigDecimal sum(Collection<Coin> coins);

    /**
     * Calculates the remaining amount by subtract the price amount from the credit
     */
    BigDecimal remaining(List<Coin> credit, BigDecimal price);

    /**
     * Calculates how many coins are needed to get the given remaining from a given cash
     */
    List<Coin> change(List<Coin> cash, BigDecimal remaining);

}
