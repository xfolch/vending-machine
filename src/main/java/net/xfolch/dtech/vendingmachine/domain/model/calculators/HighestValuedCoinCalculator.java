package net.xfolch.dtech.vendingmachine.domain.model.calculators;

import net.xfolch.dtech.vendingmachine.domain.model.Coin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xfolch on 6/8/16.
 */
final class HighestValuedCoinCalculator extends RecursiveCoinCalculator {

    private HighestValuedCoinCalculator() {
        super();
    }

    static HighestValuedCoinCalculator newOne() {
        return new HighestValuedCoinCalculator();
    }

    @Override
    List<Coin> sortedIterator(List<Coin> cash) {
        List<Coin> defensiveCopy = new ArrayList<>(cash);
        defensiveCopy.sort((c1, c2) -> c2.value().compareTo(c1.value()));

        return defensiveCopy;
    }

}
