package uk.co.complex.lvs.cm;

import java.util.Objects;

/**
 * Created by Lex van der Stoep on 06/12/2017.
 *
 * Product represents a product which can be traded on a market.
 */

public class Product {
    private final String mName;

    /**
     * Constructs a product with the specified name.
     * @param name the name of the product
     */
    public Product(String name) {
        mName = name;
    }

    /**
     * Returns the name of the product.
     * @return the name of the product
     */
    public String getName() {
        return mName;
    }

    @Override
    public boolean equals(Object myOtherObject) {
        if (this == myOtherObject) {
            return true;
        }
        if (myOtherObject == null || getClass() != myOtherObject.getClass()) {
            return false;
        }
        final Product myOtherProduct = (Product) myOtherObject;
        return mName.equals(myOtherProduct.mName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mName);
    }
}
