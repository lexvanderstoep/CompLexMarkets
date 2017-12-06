package uk.co.complex.lvs.cm;

/**
 * Created by Lex van der Stoep on 06/12/2017.
 *
 * IllegalTradeException should be thrown when an illegal trade occurs (e.g. a trade of zero
 * units).
 */
public class IllegalTradeException extends Exception {
    public IllegalTradeException(String s) {
        super(s);
    }
}
