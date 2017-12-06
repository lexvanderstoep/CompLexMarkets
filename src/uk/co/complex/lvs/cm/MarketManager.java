package uk.co.complex.lvs.cm;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Lex van der Stoep on 06/12/2017.
 *
 * MarketManager represents the managing system of a market. It matches buy and sell order for
 * certain products with each other. When a new order is placed, it tries to match it with earlier
 * placed orders. If it cannot be fully processed, then it is added to the buy/sell queue. A market
 * has a list of products which can be traded on it.
 *
 * Two orders match if the buy price of an order for a product X is greater than or equal to the
 * sell price of another order for that same product X. For example:
 * - Buy 10 XYZ at 100.01, and Sell 20 XYZ at 100.00 match
 * - Buy 10 XYZ at 100.01 and Sell 20 XYZ at 100.02 do not match
 * - Buy 10 ABC at 100.00 and Sell 10 XYZ at 100.00 do not match
 *
 */
public class MarketManager {
    private final List<Product> mProducts;
    private final ConcurrentLinkedQueue<Order> mBuyQueue;
    private final ConcurrentLinkedQueue<Order> mSellQueue;
    private final Book mBook;

    /**
     * Constructs a market manager for an empty (no products) market.
     */
    public MarketManager() {
        this(new ArrayList<>());
    }

    /**
     * Constructs a market manager for a market with the given products.
     * @param products the products which can be traded on this market.
     */
    public MarketManager(List<Product> products) {
        mProducts = new ArrayList<>(products);
        mBuyQueue = new ConcurrentLinkedQueue<>();
        mSellQueue = new ConcurrentLinkedQueue<>();
        mBook = new Book();
    }

    /**
     * Returns the products which can be traded on this market.
     * @return the products which can be traded
     */
    public List<Product> getProducts() {
        return new ArrayList<>(mProducts);
    }

    /**
     * Returns the record book of this market.
     * @return the record book of this market
     */
    public Book getBook() {
        return new Book(mBook);
    }

    /**
     * Cancel the given order and remove it from the buy/sell queue.
     * @param order the order to be cancelled
     * @return true iff the order was successfully cancelled and removed from the buy/sell queue
     */
    public boolean cancelOrder(Order order) {
        ConcurrentLinkedQueue<Order> orderQueue = (order.getSide() == Side.BUY)?
                mBuyQueue : mSellQueue;
        order.cancelOrder();
        return orderQueue.remove(order);
    }

    /**
     * Places an order on the market. The market manager tries to match the new order with any of
     * the existing orders (see the class description for definition of matching). When an order
     * cannot be further matched, it is put on the buy/sell queue if it is not fully processed.
     * @param order the order to be processed
     * @return a list with records of all the trades which happen initially when the order is placed
     */
    public List<TradeRecord> placeOrder(Order order) {
        List<TradeRecord> trades = new ArrayList<>();

        ConcurrentLinkedQueue<Order> oppositeSide = (order.getSide() == Side.BUY)?
                mSellQueue : mBuyQueue;
        List<Order> completedOrders = new LinkedList<>();
        for (Order oppositeOrder: oppositeSide) {
            if (order.getProduct().equals(oppositeOrder.getProduct())) {
                Order buyOrder = (order.getSide() == Side.BUY)? order: oppositeOrder;
                Order sellOrder = (order.getSide() == Side.SELL)? order: oppositeOrder;

                if (buyOrder.getPrice() >= sellOrder.getPrice()) {
                    int tradeAmount = Math.min(buyOrder.getRemainingAmount(),
                            sellOrder.getRemainingAmount());
                    sellOrder.tradeProduct(tradeAmount);
                    buyOrder.tradeProduct(tradeAmount);
                    float price = (buyOrder.getPrice() + sellOrder.getPrice())/2;
                    TradeRecord record = new TradeRecord(order.getProduct(), buyOrder.getActor(),
                            sellOrder.getActor(), price, tradeAmount, OffsetDateTime.now());
                    trades.add(record);
                    if (order.getStatus() == Status.COMPLETED) {
                        break;
                    }
                    if (oppositeOrder.getStatus() == Status.COMPLETED) {
                        completedOrders.add(oppositeOrder);
                    }
                }
            }
        }
        if (order.getStatus() != Status.COMPLETED) {
            ConcurrentLinkedQueue<Order> actorSide = (order.getSide() == Side.BUY)?
                    mBuyQueue : mSellQueue;
            actorSide.add(order);
        }

        oppositeSide.removeAll(completedOrders);

        mBook.addAllRecords(trades);

        return trades;
    }
}
