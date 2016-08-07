package net.xfolch.dtech.vendingmachine.domain.model;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_FLOOR;

/**
 * Represents a coin in our domain model
 * <p>
 * Created by xfolch on 7/8/16.
 */
public enum Coin {

    FIVE_CENTS(new BigDecimal(0.05).setScale(2, ROUND_FLOOR)),
    TEN_CENTS(new BigDecimal(0.10).setScale(2, ROUND_FLOOR)),
    TWENTY_CENTS(new BigDecimal(0.20).setScale(2, ROUND_FLOOR)),
    FIFTY_CENTS(new BigDecimal(0.50).setScale(2, ROUND_FLOOR)),
    ONE_EURO(new BigDecimal(1.00).setScale(2, ROUND_FLOOR)),
    TWO_EUROS(new BigDecimal(2.00).setScale(2, ROUND_FLOOR));

    private final BigDecimal value;

    Coin(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal value() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
