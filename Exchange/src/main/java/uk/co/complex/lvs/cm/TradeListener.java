package uk.co.complex.lvs.cm;

/**
 * Created by Lex van der Stoep on 06/12/2017.
 */
public interface TradeListener {

    /**
     * Notifies the listener that a trade took place.
     * @param manager the manager which notifies the listener
     */
    void update(MarketManager manager);
}
