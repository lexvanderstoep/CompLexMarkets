package uk.co.complex.lvs.cm;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import uk.co.complex.lvs.cm.datamodel.Account;
import uk.co.complex.lvs.cm.datamodel.Order;
import uk.co.complex.lvs.cm.datamodel.Side;
import uk.co.complex.lvs.cm.datamodel.Status;
import uk.co.complex.lvs.cm.datamodel.Trade;

/**
 * Created by Lex van der Stoep on 09/12/2017.
 *
 * MatchingAlgorithm is responsible for matching orders managed by the market manager. It takes a
 * new order as input and tries to match it with orders from the other side.
 *
 * This is a FIFO order matching algorithm, which matches orders based on price and then time. A new
 * order is matched with the best opposing price. If there are multiple of such prices, then the
 * same-price orders are matched based on a FIFO scheme.
 */
public class MatchingAlgorithm {
    /**
     * Matches a new order with order from the queue of the opposing side (e.g., matches a buy order
     * with sell orders in the sell queue).
     *
     * This is a FIFO order matching algorithm, which matches orders based on price and then time. A new
     * order is matched with the best opposing price. If there are multiple of such prices, then the
     * same-price orders are matched based on a FIFO scheme.
     *
     * When orders are matched, the new order is updated and the order in the queue is updated as
     * well.
     * @param order the new order to be matched
     * @param opposingOrderQueue the queue of the opposing side
     * @return all the matched order records from the queue
     */
    public static List<Trade> matchOrder(Order order, PriceTimePriorityQueue opposingOrderQueue) {
        // Perform a sanity check on the sides of the order
        if (order.getSide().equals(opposingOrderQueue.getSide())) {
            throw new IllegalArgumentException("The new order and the order queue should not " +
                    "contain order of the same side");
        }

        final List<Trade> matchedOrders = new ArrayList<>();

        while (!opposingOrderQueue.isEmpty()) {
            final Order oppositeOrder = opposingOrderQueue.first();
            if (order.getSide().equals(Side.BUY)) {
                if (oppositeOrder.getPrice() > order.getPrice()) break;
            } else {
                if (oppositeOrder.getPrice() < order.getPrice()) break;
            }

                final int tradeAmount = Math.min(order.getRemainingAmount(),
                                           oppositeOrder.getRemainingAmount());

                oppositeOrder.tradeProduct(tradeAmount);
                order.tradeProduct(tradeAmount);

                final float price = (order.getPrice() + oppositeOrder.getPrice())/2;

                final Account buyer = (order.getSide() == Side.BUY)?
                        order.getActor() : oppositeOrder.getActor();
                final Account seller = (order.getSide() == Side.SELL)?
                        order.getActor() : oppositeOrder.getActor();
                final Trade record = new Trade(order.getProduct(), buyer, seller, price,
                        tradeAmount, OffsetDateTime.now());
                matchedOrders.add(record);

                if (oppositeOrder.getStatus() == Status.COMPLETED) {
                    opposingOrderQueue.pollFirst();
                } else {
                    break;
                }
                if (order.getStatus() == Status.COMPLETED) {
                    break;
                }
        }

        return matchedOrders;
    }
}
