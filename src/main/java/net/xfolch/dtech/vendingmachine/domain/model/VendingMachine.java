package net.xfolch.dtech.vendingmachine.domain.model;

import java.text.MessageFormat;
import java.util.List;

/**
 * Vending machine module that contains not only consumer services but also
 * configuration services represented by the builder as well as its own set of failures
 * <p>
 * Created by xfolch on 7/8/16.
 */
public interface VendingMachine {

    /**
     * Action of inserting a coin into this vending machine. This method should return a vending
     * machine in which its credit has been incremented by the given coin
     *
     * @param coin that will be part of the vending machine credit
     * @return a vending machine in which its credit has been incremented by the given coin
     */
    VendingMachine insertCoin(Coin coin);

    /**
     * Trying to select a product hopefully once the consumer has introduced some coins.
     *
     * Some errors could happen:
     * <ul>
     *     <li>{@link NotEnoughCredit} in case of insufficient credit</li>
     *     <li>{@link ProductNotExists} in case of the given product does not exist in the
     *     vending machine</li>
     *     <li>{@link ProductNotAvailable} in case of the given product does not have stock</li>
     * </ul>
     *
     * @param productId identifies the product that the consumer want to purchase
     * @return a try of getting the product by consumer's credit along with the remaining coins
     */
    Try<Purchase> selectProduct(Integer productId);

    /**
     * Cancels the operation so the vending machine returns the previous inserted coins
     *
     * @return the previous inserted coins
     */
    List<Coin> cancel();

    /**
     * Represents a configuration contract to build a vending machine
     */
    interface Builder {
        Builder addProduct(Product product, Integer numUnits);
        Builder setCash(List<Coin> cash);
        VendingMachine build();
    }

    final class NotEnoughCredit extends RuntimeException {
        private NotEnoughCredit(Integer productId) {
            super("Not enough credit to select product " + productId);
        }

        public static NotEnoughCredit newOne(Integer productId) {
            return new NotEnoughCredit(productId);
        }
    }

    final class ProductNotAvailable extends RuntimeException {
        private ProductNotAvailable(Integer productId) {
            super(MessageFormat.format("Product {0} is not available", productId));
        }

        public static ProductNotAvailable newOne(Integer productId) {
            return new ProductNotAvailable(productId);
        }
    }

    final class ProductNotExists extends RuntimeException {
        private ProductNotExists(Integer productId) {
            super(MessageFormat.format("Product {0} does not exist", productId));
        }

        public static ProductNotExists newOne(Integer productId) {
            return new ProductNotExists(productId);
        }
    }
}
