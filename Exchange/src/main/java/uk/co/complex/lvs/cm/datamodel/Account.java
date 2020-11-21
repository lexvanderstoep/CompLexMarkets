package uk.co.complex.lvs.cm.datamodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lex van der Stoep on 06/12/2017.
 *
 * Account represents an account which can place orders on markets. It keeps track of the stocks
 * it owns.
 */
public class Account {
    private final String theName;
    private final Map<Product, Integer> thePosition = new HashMap<>();

    /**
     * Constructs an account with the specified name.
     * @param aName the name of the account
     */
    public Account(String aName) {
        theName = aName;
    }

    /**
     * Returns the name of the account.
     * @return the name of the account
     */
    public String getName() {
        return theName;
    }

    /**
     * Returns the amount of the specified product there is in the account.
     * @param aProduct the product
     * @return the amount of the product in the account
     */
    public int getPosition(Product aProduct) {
        if (!thePosition.containsKey(aProduct)) return 0;

        return thePosition.get(aProduct);
    }

    public void updateBook(Product aProduct, int aChange) {
        if (thePosition.containsKey(aProduct)) {
            final int current = thePosition.get(aProduct);
            thePosition.put(aProduct, current + aChange);
        } else {
            thePosition.put(aProduct, aChange);
        }
    }
}
