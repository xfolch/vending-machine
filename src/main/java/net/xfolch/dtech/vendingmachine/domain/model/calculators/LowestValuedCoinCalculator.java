package net.xfolch.dtech.vendingmachine.domain.model.calculators;

import net.xfolch.dtech.vendingmachine.domain.model.Coin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xfolch on 7/8/16.
 */
final class LowestValuedCoinCalculator extends RecursiveCoinCalculator {

    private LowestValuedCoinCalculator() {
        super();
    }

    static LowestValuedCoinCalculator newOne() {
        return new LowestValuedCoinCalculator();
    }

    @Override
    List<Coin> sort(List<Coin> cash) {
        List<Coin> defensiveCopy = new ArrayList<>(cash);
        defensiveCopy.sort((c1, c2) -> c1.value().compareTo(c2.value()));

        return defensiveCopy;
    }

}
