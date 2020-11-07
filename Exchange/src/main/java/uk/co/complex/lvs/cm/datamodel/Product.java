package uk.co.complex.lvs.cm.datamodel;

import java.util.Objects;

/**
 * Created by Lex van der Stoep on 06/12/2017.
 *
 * Product represents a product which can be traded on a market.
 */

public class Product {
    private final String theName;

    /**
     * Constructs a product with the specified name.
     * @param aName the name of the product
     */
    public Product(String aName) {
        theName = aName;
    }

    /**
     * Returns the name of the product.
     * @return the name of the product
     */
    public String getName() {
        return theName;
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
        return theName.equals(myOtherProduct.theName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(theName);
    }
}
