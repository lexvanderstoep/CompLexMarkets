package uk.co.complex.lvs.cm;

import uk.co.complex.lvs.cm.datamodel.Order;
import uk.co.complex.lvs.cm.datamodel.Side;

import java.util.TreeSet;

/**
 * Created by Lex van der Stoep on 10/12/2017.
 *
 * PriceTimePriorityQueue is a PriorityQueue for trade orders. It orders these first by price
 * (best first) and then by time (earliest first).
 */
public class PriceTimePriorityQueue extends TreeSet<Order> {
    private final Side mSide;

    public PriceTimePriorityQueue(Side side) {
        super((o1, o2) -> {
            if (side == Side.SELL) {
                if (o1.getPrice() < o2.getPrice()) return -1;
                if (o1.getPrice() > o2.getPrice()) return 1;
                return o1.getTime().compareTo(o2.getTime());
            } else {
                if (o1.getPrice() < o2.getPrice()) return 1;
                if (o1.getPrice() > o2.getPrice()) return -1;
                return o1.getTime().compareTo(o2.getTime());
            }
        });

        mSide = side;
    }

    /**
     * Returns the side of the orders that this order queue contains.
     * @return the side of the orders that this order queue contains
     */
    public Side getSide() {
        return mSide;
    }
}
