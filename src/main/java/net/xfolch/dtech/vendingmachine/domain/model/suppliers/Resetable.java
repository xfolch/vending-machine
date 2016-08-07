package net.xfolch.dtech.vendingmachine.domain.model.suppliers;

/**
 * Trait that a vending machine can have
 * <p>
 * Notice that it is not public because this trait only belongs to the supplier domain
 * <p>
 * Created by xfolch on 7/8/16.
 */
interface Resetable {

    void reset();

}
