package uk.co.complex.lvs.cm.datamodel;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Lex van der Stoep on 06/12/2017.
 *
 * Order represents an order to buy or sell a product on a market. The order encapsulates an actor
 * (which buys or sells) and an amount of a certain product to buy/sell at a certain price.
 */
public class Order {
    private final Product theProduct;
    private final float thePrice;
    private final int theVolume;
    private int theRemainingAmount;
    private final Account theActor;
    private final Side theSide;
    private Status theStatus;
    private final OffsetDateTime theTime;

    /**
     * Constructs a new order to buy/sell a certain amount of a product at a specified price.
     * @param aProduct the product to be bought/sold
     * @param aPrice the price of the order
     * @param aAmount the amount to be traded
     * @param aActor the actor which wants to execute the order
     * @param aSide the side of the order (buy/sell)
     * @param aTime the time at which the order was placed
     */
    public Order(Product aProduct, float aPrice, int aAmount, Account aActor, Side aSide, OffsetDateTime aTime) {
        this.theProduct = aProduct;
        thePrice = aPrice;
        theVolume = aAmount;
        theRemainingAmount = aAmount;
        theActor = aActor;
        theSide = aSide;
        theStatus = Status.NEW;
        theTime = aTime;
    }

    /**
     * Returns the product.
     * @return the product
     */
    public Product getProduct() {
        return theProduct;
    }

    /**
     * Returns the price.
     * @return the price
     */
    public float getPrice() {
        return thePrice;
    }

    /**
     * Returns the total amount.
     * @return the total amount
     */
    public int getAmount() {
        return theVolume;
    }

    /**
     * Returns the remaining amount.
     * @return the remaining amount
     */
    public int getRemainingAmount() {
        return theRemainingAmount;
    }

    /**
     * Returns the actor (buyer/seller).
     * @return the actor of the order
     */
    public Account getActor() {
        return theActor;
    }

    /**
     * Returns the side of the actor on this order.
     * @return the side of the actor on this order
     */
    public Side getSide() {
        return theSide;
    }

    /**
     * Returns the status of the order.
     * @return the status of the order
     */
    public Status getStatus() {
        return theStatus;
    }

    /**
     * Returns the time at which this order was placed.
     * @return the time at which this order was placed
     */
    public OffsetDateTime getTime() {
        return theTime;
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

        theRemainingAmount -= amount;
        theStatus = (theRemainingAmount == 0) ? Status.COMPLETED : Status.PARTIAL;
    }

    /**
     * Cancel this order.
     */
    public void cancelOrder() {
        theStatus = Status.CANCELLED;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append((theSide == Side.BUY)?"Buy ":"Sell ");
        builder.append(theProduct.getName() + " from ");
        builder.append(theActor.getName() + ": ");
        builder.append("(" + theRemainingAmount + "/" + theVolume + ")" + "x");
        builder.append(String.format("%.2f", thePrice) + " ");
        builder.append("@ " + theTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        return builder.toString();
    }
}
