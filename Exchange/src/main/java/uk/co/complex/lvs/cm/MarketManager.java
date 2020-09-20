package uk.co.complex.lvs.cm;

import uk.co.complex.lvs.cm.traders.RandomIntervalProductTrader;

import java.util.*;

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
    private final Map<Product, PriceTimePriorityQueue> mBuyQueues;
    private final Map<Product, PriceTimePriorityQueue> mSellQueues;
    private final Book mBook;
    private final List<TradeListener> mTradeListeners;

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
    public MarketManager(Collection<Product> products) {
        mProducts = new ArrayList<>(products);
        mBuyQueues = new HashMap<>();
        mSellQueues = new HashMap<>();
        for (Product p: products) {
            mBuyQueues.put(p, new PriceTimePriorityQueue(Side.BUY));
            mSellQueues.put(p, new PriceTimePriorityQueue(Side.SELL));
        }
        mBook = new Book();
        mTradeListeners = new ArrayList<>();
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
     * Returns the buy queue of a product p
     * @param p the product p
     * @return the buy queue of the product
     */
    public PriceTimePriorityQueue getBuyQueue(Product p) {
        return mBuyQueues.get(p);
    }

    /**
     * Returns the sell queue of a product p
     * @param p the product p
     * @return the sell queue of the product
     */
    public PriceTimePriorityQueue getSellQueue(Product p) {
        return mSellQueues.get(p);
    }

    /**
     * Adds the given trade listener to the list of listeners of the market manager.
     * @param listener the trade listener to be added
     */
    public void addTradeListener(TradeListener listener) {
        mTradeListeners.add(listener);
    }

    /**
     * Removes the given trade listener from the list of listeners of the market manager.
     * @param listener the trade listener to be removed
     */
    public void removeTradeListener(TradeListener listener) {
        mTradeListeners.remove(listener);
    }

    private void notifyTradeListeners() {
        mTradeListeners.forEach((TradeListener t) -> t.update(this));
    }

    /**
     * Notify the accounts involved in the given trades that those trades have been made. This
     * enabled them to, for example, update their book.
     * @param trades
     */
    private void notifyAccounts(List<TradeRecord> trades) {
        trades.forEach((TradeRecord r) -> {
            Product p = r.getProduct();
            int amount = r.getAmount();
            r.getBuyer().updateBook(p, amount);
            r.getSeller().updateBook(p, -amount);
        });
    }

    /**
     * Cancel the given order and remove it from the buy/sell queue.
     * @param order the order to be cancelled
     * @return true iff the order was successfully cancelled and removed from the buy/sell queue
     */
    public synchronized boolean cancelOrder(Order order) {
        Map<Product, PriceTimePriorityQueue> orderQueues = (order.getSide() == Side.BUY)?
                mBuyQueues : mSellQueues;
        PriceTimePriorityQueue productQueue = orderQueues.get(order.getProduct());
        order.cancelOrder();
        return productQueue.remove(order);
    }

    /**
     * Places an order on the market. The market manager tries to match the new order with any of
     * the existing orders (see the class description for definition of matching). When an order
     * cannot be further matched, it is put on the buy/sell queue if it is not fully processed.
     * @param order the order to be processed
     * @return a list with records of all the trades which happen initially when the order is placed
     */
    public synchronized List<TradeRecord> placeOrder(Order order) throws IllegalTradeException {
        if (order.getAmount() <= 0) throw new IllegalTradeException("The trade should have a" +
                "positive amount of units (had " + order.getAmount() + ")");
        if (order.getActor() == null) throw new IllegalTradeException("The trade should be from " +
                "a valid account (account was null)");
        if (!mProducts.contains(order.getProduct())) throw new IllegalTradeException("The " +
                "product to be traded is not listed on this market (was " +
                order.getProduct().toString() + ")");
        if (order.getSide() == Side.SELL &
                order.getActor().getProductAmount(order.getProduct()) < order.getAmount())
            throw new IllegalTradeException("The actor does not have enough of the product it is " +
                    "trying to sell (has: " + order.getActor().getProductAmount(order.getProduct())
                    + " , wants: " + order.getAmount() + ")");

        PriceTimePriorityQueue oppositeSide = ((order.getSide() == Side.BUY)?
                mSellQueues : mBuyQueues).get(order.getProduct());

        List<TradeRecord> trades = MatchingAlgorithm.matchOrder(order, oppositeSide);

        if (order.getStatus() != Status.COMPLETED) {
            PriceTimePriorityQueue actorSide = ((order.getSide() == Side.BUY)?
                    mBuyQueues : mSellQueues).get(order.getProduct());
            actorSide.add(order);
        }

        mBook.addAllRecords(trades);
        notifyAccounts(trades);
        notifyTradeListeners();

        return trades;
    }

    public static void main(String[] args) {
        final Product xyz = new Product("XYZ");
        final MarketManager manager = new MarketManager(
                new ArrayList<>(Arrays.asList(xyz)));
        BookPrinter marketPrinter = new BookPrinter();
        manager.addTradeListener(marketPrinter);

        RandomIntervalProductTrader buyer = new RandomIntervalProductTrader(
                new Account("Buyer"), xyz, manager, 50.0f, 100.0f,
                1, 10, 1000, 2000);
        RandomIntervalProductTrader seller = new RandomIntervalProductTrader(
                new Account("Seller"), xyz, manager, 50.0f, 100.0f,
                1, 10, 1000, 2000);
        buyer.start();
        seller.start();
    }
}
