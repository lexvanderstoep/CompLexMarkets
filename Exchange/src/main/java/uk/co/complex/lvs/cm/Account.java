package uk.co.complex.lvs.cm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lex van der Stoep on 06/12/2017.
 *
 * Account represents an account which can place orders on markets. It keeps track of the stocks
 * it owns.
 */
public class Account {
    private final String mName;
    private final Map<Product, Integer> book = new HashMap<>();

    /**
     * Constructs an account with the specified name.
     * @param name the name of the account
     */
    public Account(String name) {
        mName = name;
    }

    /**
     * Returns the name of the account.
     * @return the name of the account
     */
    public String getName() {
        return mName;
    }

    /**
     * Returns the amount of the specified product there is in the account.
     * @param product the product
     * @return the amount of the product in the account
     */
    public int getProductAmount(Product product) {
        if (!book.containsKey(product)) return 0;

        return book.get(product);
    }

    public void updateBook(Product product, int change) {
        if (book.containsKey(product)) {
            int current = book.get(product);
            book.put(product, current + change);
        } else {
            book.put(product, change);
        }
    }
}
