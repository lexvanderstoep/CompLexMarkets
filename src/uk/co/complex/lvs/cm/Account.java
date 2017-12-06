package uk.co.complex.lvs.cm;

/**
 * Created by Lex van der Stoep on 06/12/2017.
 *
 * Account represents an account which can place orders on markets.
 */
public class Account {
    private final String mName;

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
}
