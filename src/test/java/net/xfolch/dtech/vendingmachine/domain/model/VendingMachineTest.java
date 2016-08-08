package net.xfolch.dtech.vendingmachine.domain.model;

import net.xfolch.dtech.vendingmachine.domain.model.VendingMachine.NotEnoughCredit;
import net.xfolch.dtech.vendingmachine.domain.model.VendingMachine.ProductNotAvailable;
import net.xfolch.dtech.vendingmachine.domain.model.VendingMachine.ProductNotExists;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Created by xfolch on 6/8/16.
 */
public class VendingMachineTest extends DomainTest {

    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @Test
    public void trying_to_select_product_that_does_not_exist() {

        VendingMachine vendingMachine = niceSupplier.newVendingMachine()
                .addProduct(Water, 1)
                .build();

        Try<Purchase> purchase = vendingMachine.selectProduct(Coke.getId());

        softly.assertThat(purchase.isSuccess()).isFalse();
        softly.assertThatThrownBy(purchase::getOrThrowException)
                .isInstanceOf(ProductNotExists.class)
                .hasMessageContaining(Coke.getId().toString());
    }

    @Test
    public void trying_to_select_product_that_is_not_available() {

        VendingMachine vendingMachine = niceSupplier.newVendingMachine()
                .addProduct(Coke, 0)
                .build();

        Try<Purchase> purchase = vendingMachine.selectProduct(Coke.getId());

        softly.assertThat(purchase.isSuccess()).isFalse();
        softly.assertThatThrownBy(purchase::getOrThrowException)
                .isInstanceOf(ProductNotAvailable.class)
                .hasMessageContaining(Coke.getId().toString());
    }

    @Test
    public void trying_to_select_product_with_no_credit() {

        VendingMachine vendingMachine = niceSupplier.newVendingMachine()
                .addProduct(Pepsi, 1)
                .build();

        Try<Purchase> purchase = vendingMachine.selectProduct(Pepsi.getId());

        softly.assertThat(purchase.isSuccess()).isFalse();
        softly.assertThatThrownBy(purchase::getOrThrowException)
                .isInstanceOf(NotEnoughCredit.class)
                .hasMessageContaining(Pepsi.getId().toString());
    }

    @Test
    public void trying_to_select_a_coke_with_not_enough_credit() {

        VendingMachine vendingMachine = niceSupplier.newVendingMachine()
                .addProduct(Coke, 1)
                .build();

        Try<Purchase> purchase = vendingMachine
                .insertCoin(Coin.FIFTY_CENTS)
                .selectProduct(Coke.getId());

        softly.assertThat(purchase.isSuccess()).isFalse();
        softly.assertThatThrownBy(purchase::getOrThrowException)
                .isInstanceOf(NotEnoughCredit.class)
                .hasMessageContaining(Coke.getId().toString());
    }

    @Test
    public void select_product_with_exact_credit() {

        VendingMachine vendingMachine = niceSupplier.newVendingMachine()
                .addProduct(Coke, 1)
                .build();

        Try<Purchase> purchase = vendingMachine
                .insertCoin(Coin.FIFTY_CENTS)
                .insertCoin(Coin.ONE_EURO)
                .selectProduct(Coke.getId());

        softly.assertThat(purchase.isSuccess()).isTrue();
        softly.assertThat(purchase.getOrThrowRuntimeException().getProduct()).isEqualTo(Coke);
        softly.assertThat(purchase.getOrThrowRuntimeException().getRemaining()).isEmpty();
    }

    @Test
    public void insert_coins_but_cancel_in_the_end() {

        VendingMachine vendingMachine = niceSupplier.newVendingMachine()
                .addProduct(Coke, 1)
                .build();

        List<Coin> refund = vendingMachine
                .insertCoin(Coin.FIFTY_CENTS)
                .insertCoin(Coin.TEN_CENTS)
                .insertCoin(Coin.TWENTY_CENTS)
                .cancel();

        softly.assertThat(refund).containsExactly(Coin.FIFTY_CENTS, Coin.TEN_CENTS, Coin.TWENTY_CENTS);
    }

    @Test
    public void cancel_operation_and_then_select_a_product_returns_exception() {

        VendingMachine vendingMachine = niceSupplier.newVendingMachine()
                .addProduct(Coke, 1)
                .build();

        List<Coin> refund = vendingMachine
                .insertCoin(Coin.FIFTY_CENTS)
                .insertCoin(Coin.ONE_EURO)
                .cancel();

        softly.assertThat(refund).containsExactly(Coin.FIFTY_CENTS, Coin.ONE_EURO);

        Try<Purchase> purchase = vendingMachine.selectProduct(Coke.getId());

        softly.assertThat(purchase.isSuccess()).isFalse();
        softly.assertThatThrownBy(purchase::getOrThrowException)
                .isInstanceOf(NotEnoughCredit.class)
                .hasMessageContaining(Coke.getId().toString());
    }

    @Test
    public void decrement_units_when_selecting_products() {

        VendingMachine vendingMachine = niceSupplier.newVendingMachine()
                .addProduct(Coke, 1)
                .build();

        Try<Purchase> firstPurchase = vendingMachine
                .insertCoin(Coin.FIFTY_CENTS)
                .insertCoin(Coin.ONE_EURO)
                .selectProduct(Coke.getId());

        softly.assertThat(firstPurchase.isSuccess()).isTrue();
        softly.assertThat(firstPurchase.getOrThrowRuntimeException().getProduct()).isEqualTo(Coke);
        softly.assertThat(firstPurchase.getOrThrowRuntimeException().getRemaining()).isEmpty();

        Try<Purchase> secondPurchase = vendingMachine
                .insertCoin(Coin.FIFTY_CENTS)
                .insertCoin(Coin.ONE_EURO)
                .selectProduct(Coke.getId());

        softly.assertThat(secondPurchase.isSuccess()).isFalse();
        softly.assertThatThrownBy(secondPurchase::getOrThrowException)
                .isInstanceOf(ProductNotAvailable.class)
                .hasMessageContaining(Coke.getId().toString());
    }

    @Test
    public void select_product_but_not_returns_change_because_it_has_not_enough_cash() {

        // no initial cash
        VendingMachine vendingMachine = niceSupplier.newVendingMachine()
                .addProduct(Coke, 1)
                .build();

        Try<Purchase> purchase = vendingMachine
                .insertCoin(Coin.FIFTY_CENTS)
                .insertCoin(Coin.FIFTY_CENTS)
                .insertCoin(Coin.TWENTY_CENTS)
                .insertCoin(Coin.TWENTY_CENTS)
                .insertCoin(Coin.TWENTY_CENTS)
                .selectProduct(Coke.getId());

        softly.assertThat(purchase.isSuccess()).isTrue();
        softly.assertThat(purchase.getOrThrowRuntimeException().getProduct()).isEqualTo(Coke);
        softly.assertThat(purchase.getOrThrowRuntimeException().getRemaining()).isEmpty();
    }

    @Test
    public void select_product_and_returns_change_because_it_has_enough_cash() {

        VendingMachine vendingMachine = niceSupplier.newVendingMachine()
                .addProduct(Coke, 1)
                .setCash(singletonList(Coin.TEN_CENTS))
                .build();

        Try<Purchase> purchase = vendingMachine
                .insertCoin(Coin.FIFTY_CENTS)
                .insertCoin(Coin.FIFTY_CENTS)
                .insertCoin(Coin.TWENTY_CENTS)
                .insertCoin(Coin.TWENTY_CENTS)
                .insertCoin(Coin.TWENTY_CENTS)
                .selectProduct(Coke.getId());

        softly.assertThat(purchase.isSuccess()).isTrue();
        softly.assertThat(purchase.getOrThrowRuntimeException().getProduct()).isEqualTo(Coke);
        softly.assertThat(purchase.getOrThrowRuntimeException().getRemaining()).containsExactly(Coin.TEN_CENTS);
    }

    @Test
    public void in_first_purchase_returns_change_but_not_in_second_one() {

        VendingMachine vendingMachine = niceSupplier.newVendingMachine()
                .addProduct(Coke, 2)
                .setCash(singletonList(Coin.TEN_CENTS))
                .build();

        Try<Purchase> firstPurchase = vendingMachine
                .insertCoin(Coin.FIFTY_CENTS)
                .insertCoin(Coin.FIFTY_CENTS)
                .insertCoin(Coin.TWENTY_CENTS)
                .insertCoin(Coin.TWENTY_CENTS)
                .insertCoin(Coin.TWENTY_CENTS)
                .selectProduct(Coke.getId());

        softly.assertThat(firstPurchase.isSuccess()).isTrue();
        softly.assertThat(firstPurchase.getOrThrowRuntimeException().getProduct()).isEqualTo(Coke);
        softly.assertThat(firstPurchase.getOrThrowRuntimeException().getRemaining()).containsExactly(Coin.TEN_CENTS);

        Try<Purchase> secondPurchase = vendingMachine
                .insertCoin(Coin.FIFTY_CENTS)
                .insertCoin(Coin.FIFTY_CENTS)
                .insertCoin(Coin.TWENTY_CENTS)
                .insertCoin(Coin.TWENTY_CENTS)
                .insertCoin(Coin.TWENTY_CENTS)
                .selectProduct(Coke.getId());

        softly.assertThat(secondPurchase.isSuccess()).isTrue();
        softly.assertThat(secondPurchase.getOrThrowRuntimeException().getProduct()).isEqualTo(Coke);
        softly.assertThat(secondPurchase.getOrThrowRuntimeException().getRemaining()).isEmpty();
    }

    @Test
    public void select_product_and_returns_nice_change() {

        VendingMachine vendingMachine = niceSupplier.newVendingMachine()
                .addProduct(Water, 1)
                .setCash(Arrays.asList(Coin.TEN_CENTS, Coin.TWENTY_CENTS, Coin.TEN_CENTS))
                .build();

        Try<Purchase> purchase = vendingMachine
                .insertCoin(Coin.TEN_CENTS)
                .insertCoin(Coin.ONE_EURO)
                .selectProduct(Water.getId());

        softly.assertThat(purchase.isSuccess()).isTrue();
        softly.assertThat(purchase.getOrThrowRuntimeException().getProduct()).isEqualTo(Water);
        softly.assertThat(purchase.getOrThrowRuntimeException().getRemaining()).containsExactly(Coin.TWENTY_CENTS);
    }

    @Test
    public void select_product_and_returns_hateful_change() {

        VendingMachine vendingMachine = hatefulSupplier.newVendingMachine()
                .addProduct(Water, 1)
                .setCash(Arrays.asList(Coin.TEN_CENTS, Coin.TWENTY_CENTS, Coin.TEN_CENTS))
                .build();

        Try<Purchase> purchase = vendingMachine
                .insertCoin(Coin.TEN_CENTS)
                .insertCoin(Coin.ONE_EURO)
                .selectProduct(Water.getId());

        softly.assertThat(purchase.isSuccess()).isTrue();
        softly.assertThat(purchase.getOrThrowRuntimeException().getProduct()).isEqualTo(Water);
        softly.assertThat(purchase.getOrThrowRuntimeException().getRemaining()).containsExactly(Coin.TEN_CENTS, Coin.TEN_CENTS);
    }

    @Test
    public void reset_machine_to_get_back_same_units() {

        VendingMachine vendingMachine = niceSupplier.newVendingMachine()
                .addProduct(Water, 1)
                .setCash(Arrays.asList(Coin.TEN_CENTS, Coin.TWENTY_CENTS, Coin.TEN_CENTS))
                .build();

        Try<Purchase> purchaseBeforeReset = vendingMachine
                .insertCoin(Coin.TEN_CENTS)
                .insertCoin(Coin.ONE_EURO)
                .selectProduct(Water.getId());

        softly.assertThat(purchaseBeforeReset.isSuccess()).isTrue();
        softly.assertThat(purchaseBeforeReset.getOrThrowRuntimeException().getProduct()).isEqualTo(Water);
        softly.assertThat(purchaseBeforeReset.getOrThrowRuntimeException().getRemaining()).containsExactly(Coin.TWENTY_CENTS);

        niceSupplier.reset(vendingMachine);

        Try<Purchase> purchaseAfterReset = vendingMachine
                .insertCoin(Coin.ONE_EURO)
                .selectProduct(Water.getId());

        softly.assertThat(purchaseAfterReset.isSuccess()).isTrue();
        softly.assertThat(purchaseAfterReset.getOrThrowRuntimeException().getProduct()).isEqualTo(Water);
        softly.assertThat(purchaseAfterReset.getOrThrowRuntimeException().getRemaining()).containsExactly(Coin.TEN_CENTS);
    }

}
