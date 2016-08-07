package net.xfolch.dtech.vendingmachine.domain.model.calculators;

import net.xfolch.dtech.vendingmachine.domain.model.Coin;
import net.xfolch.dtech.vendingmachine.domain.model.CoinCalculator;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.math.BigDecimal.ZERO;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Created by xfolch on 7/8/16.
 */
abstract class RecursiveCoinCalculator implements CoinCalculator {

    RecursiveCoinCalculator() {
    }

    @Override
    public BigDecimal sum(Collection<Coin> coins) {
        return coins.stream()
                .map(Coin::value)
                .reduce(ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal remaining(List<Coin> credit, BigDecimal price) {
        return sum(credit).subtract(price);
    }

    @Override
    public List<Coin> change(List<Coin> cash, BigDecimal remaining) {
        return changeRec(sortedIterator(cash), remaining, emptyList());
    }

    abstract List<Coin> sortedIterator(List<Coin> cash);

    private List<Coin> changeRec(List<Coin> cash, BigDecimal remaining, List<Coin> change) {
        if (isZero(remaining) || cash.isEmpty()) {
            return change;
        } else {
            Coin coin = head(cash);
            int compare = coin.value().compareTo(remaining);

            if (compare == 0) {
                return changeRec(tail(cash), ZERO, add(change, coin));
            } else if (compare > 0) {
                return changeRec(removeCoin(cash, coin), remaining, change);
            } else {
                return changeRec(tail(cash), subtract(remaining, coin), add(change, coin));
            }
        }
    }

    private static Coin head(List<Coin> coins) {
        return coins.get(0);
    }

    private static List<Coin> tail(List<Coin> coins) {
        return coins.subList(1, coins.size());
    }

    private static List<Coin> removeCoin(List<Coin> coins, Coin coin) {
        return coins.stream().filter(c -> !c.equals(coin)).collect(toList());
    }

    private static boolean isZero(BigDecimal amount) {
        return ZERO.equals(amount);
    }

    private static List<Coin> add(List<Coin> coins, Coin coin) {
        return Stream.concat(coins.stream(), Stream.of(coin)).collect(toList());
    }

    private static BigDecimal subtract(BigDecimal amount, Coin coin) {
        return amount.subtract(coin.value());
    }

}
