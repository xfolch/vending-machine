package net.xfolch.dtech.vendingmachine.domain.model.suppliers;

import net.xfolch.dtech.vendingmachine.domain.model.*;

import java.math.BigDecimal;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.nCopies;
import static java.util.Collections.unmodifiableList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

/**
 * Implementation of the vending machine that maintains the state in memory.
 * <p>
 * Each action mutates the state, which means that this approach is not thread-safe
 * but is not important in this domain
 * <p>
 * Created by xfolch on 7/8/16.
 */
final class InMemoryVendingMachine implements VendingMachine {

    private final Set<ProductLine> products;
    private final List<Coin> credit;
    private final Map<Coin, Long> cash;
    private final CoinCalculator calculator;

    private InMemoryVendingMachine(Set<ProductLine> products,
                                   List<Coin> credit,
                                   List<Coin> cash,
                                   CoinCalculator calculator) {

        this.products = new HashSet<>(products);
        this.credit = new ArrayList<>(credit);
        this.cash = cash.stream().collect(groupingBy(identity(), counting()));
        this.calculator = calculator;
    }

    static InMemoryVendingMachine noCredit(Set<ProductLine> products,
                                           List<Coin> cash,
                                           CoinCalculator calculator) {

        return new InMemoryVendingMachine(products, emptyList(), cash, calculator);
    }

    @Override
    public VendingMachine insertCoin(Coin coin) {
        credit.add(coin);
        incrementCash(coin);

        return this;
    }

    @Override
    public Try<Purchase> selectProduct(Integer productId) {
        return Try.that(() -> find(productId))
                .filter(Optional::isPresent, () -> ProductNotExists.newOne(productId))
                .map(Optional::get)
                .filter(ProductLine::hasUnits, () -> ProductNotAvailable.newOne(productId))
                .filter(this::hasEnoughCredit, () -> NotEnoughCredit.newOne(productId))
                .map(this::makePurchase);
    }

    @Override
    public List<Coin> cancel() {
        List<Coin> refund = unmodifiableList(new ArrayList<>(credit));
        credit.clear();

        decrementCash(refund);
        return refund;
    }

    private Optional<ProductLine> find(Integer productId) {
        return products.stream()
                .filter(line -> line.getProduct().getId().equals(productId))
                .findFirst();
    }

    private boolean hasEnoughCredit(ProductLine line) {
        return calculator.sum(credit).compareTo(line.getProduct().getPrice()) >= 0;
    }

    private Purchase makePurchase(ProductLine line) {
        BigDecimal change = consumesUnit(line);

        List<Coin> remaining = calculator.change(availableCash(), change);
        decrementCash(remaining);

        return Purchase.builder()
                .setProduct(line.getProduct())
                .setRemaining(remaining)
                .build();
    }

    private List<Coin> availableCash() {
        return cash.entrySet().stream()
                .flatMap(entry -> nCopies(entry.getValue().intValue(), entry.getKey()).stream())
                .collect(toList());
    }

    private BigDecimal consumesUnit(ProductLine line) {
        BigDecimal change = calculator.remaining(credit, line.getProduct().getPrice());

        decrementUnit(line);
        credit.clear();

        return change;
    }

    private void decrementUnit(ProductLine line) {
        products.remove(line);
        products.add(ProductLine.newOne(line.getProduct(), line.getNumUnits() - 1));
    }

    private void incrementCash(Coin coin) {
        cash.compute(coin, (c, acc) -> acc != null ? acc + 1 : 1);
    }

    private void decrementCash(List<Coin> coins) {
        coins.forEach(this::decrementCash);
    }

    private void decrementCash(Coin coin) {
        cash.computeIfPresent(coin, (c, acc) -> acc - 1);
    }

}
