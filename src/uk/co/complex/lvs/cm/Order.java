package uk.co.complex.lvs.cm;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Lex van der Stoep on 06/12/2017.
 *
 * Order represents an order to buy or sell a product on a market. The order encapsulates an actor
 * (which buys or sells) and an amount of a certain product to buy/sell at a certain price.
 */
public class Order {
    private final Product mProduct;
    private final float mPrice;
    private final int mAmount;
    private int mRemainingAmount;
    private final Account mActor;
    private final Side mSide;
    private Status mStatus;
    private final OffsetDateTime mTime;

    /**
     * Constructs a new order to buy/sell a certain amount of a product at a specified price.
     * @param product the product to be bought/sold
     * @param price the price of the order
     * @param amount the amount to be traded
     * @param actor the actor which wants to execute the order
     * @param side the side of the order (buy/sell)
     */
    public Order(Product product, float price, int amount, Account actor, Side side,
                 OffsetDateTime time) {
        mProduct = product;
        mPrice = price;
        mAmount = amount;
        mRemainingAmount = amount;
        mActor = actor;
        mSide = side;
        mStatus = Status.NEW;
        mTime = time;
    }

    /**
     * Returns the product.
     * @return the product
     */
    public Product getProduct() {
        return mProduct;
    }

    /**
     * Returns the price.
     * @return the price
     */
    public float getPrice() {
        return mPrice;
    }

    /**
     * Returns the total amount.
     * @return the total amount
     */
    public int getAmount() {
        return mAmount;
    }

    /**
     * Returns the remaining amount.
     * @return the remaining amount
     */
    public int getRemainingAmount() {
        return mRemainingAmount;
    }

    /**
     * Returns the actor (buyer/seller).
     * @return the actor of the order
     */
    public Account getActor() {
        return mActor;
    }

    /**
     * Returns the side of the actor on this order.
     * @return the side of the actor on this order
     */
    public Side getSide() {
        return mSide;
    }

    /**
     * Returns the status of the order.
     * @return the status of the order
     */
    public Status getStatus() {
        return mStatus;
    }

    /**
     * Returns the time at which this order was placed.
     * @return the time at which this order was placed
     */
    public OffsetDateTime getTime() {
        return mTime;
    }

    /**
     * Execute the order (partially) for a given amount of units. It updates the remaining units
     * to be bought/sold and set the status of the order appropriately.
     * @param amount the amount of the product to be traded
     */
    public void tradeProduct(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("The amount to be traded should be positive (was " +
                    amount + ")");
        }
        if (amount > getRemainingAmount()) {
            throw new IllegalArgumentException("The amount to be traded cannot be greater than " +
                    "the available amount");
        }

        assert (0 <= amount & amount <= getRemainingAmount());

        mRemainingAmount -= amount;
        mStatus = (mRemainingAmount == 0) ? Status.COMPLETED : Status.PARTIAL;
    }

    /**
     * Cancel this order.
     */
    public void cancelOrder() {
        mStatus = Status.CANCELLED;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(mProduct.getName() + " from ");
        builder.append(mActor.getName() + ": ");
        builder.append("(" + mRemainingAmount + "/" + mAmount + ")" + "x");
        builder.append(String.format("%.2f", mPrice) + " ");
        builder.append("@ " + mTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        return builder.toString();
    }
}
