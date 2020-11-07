package uk.co.complex.lvs.cm.datamodel;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Created by Lex van der Stoep on 06/12/2017.
 *
 * TradeRecord represents a record of trade. A trade happens when a buyer and a seller have
 * matching orders. An order is matching when they can agree on a price.
 */
public class Trade {
    private final Product theProduct;
    private final Account theBuyer;
    private final Account theSeller;
    private final float thePrice;
    private final int theAmount;
    private final OffsetDateTime theTime;

    /**
     * Constructs a new trade record.
     * @param aProduct the traded product
     * @param aBuyer the buyer of the product
     * @param aSeller the seller of the product
     * @param aPrice the price at which the product was traded
     * @param aAmount the amount which was traded
     * @param aTime the time at which the trade was executed
     */
    public Trade(Product aProduct, Account aBuyer, Account aSeller, float aPrice, int aAmount, OffsetDateTime aTime) {
        theProduct = aProduct;
        theBuyer = aBuyer;
        theSeller = aSeller;
        thePrice = aPrice;
        theAmount = aAmount;
        theTime = aTime;
    }

    /**
     * Returns the traded product.
     * @return the traded product
     */
    public Product getProduct() {
        return theProduct;
    }

    /**
     * Returns the buyer of the product.
     * @return the buyer of the product
     */
    public Account getBuyer() {
        return theBuyer;
    }

    /**
     * Returns the seller of the product.
     * @return the seller of the product
     */
    public Account getSeller() {
        return theSeller;
    }

    /**
     * Returns the price of the product.
     * @return the price of the product
     */
    public float getPrice() {
        return thePrice;
    }

    /**
     * Returns the amount of the product which was traded.
     * @return the amount which was traded
     */
    public int getAmount() {
        return theAmount;
    }

    /**
     * Returns the time at which the trade was executed.
     * @return the time at which the trade was executed
     */
    public OffsetDateTime getTime() {
        return theTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(theProduct.getName() + ": ");
        builder.append(theAmount + "x");
        builder.append(String.format("%.2f", thePrice) + " ");
        builder.append(theSeller.getName() + "->" + theBuyer.getName() + " ");
        builder.append("@ " + theTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        return builder.toString();
    }

    @Override
    public boolean equals(Object myOtherObject) {
        if (this == myOtherObject) {
            return true;
        }
        if (myOtherObject == null || getClass() != myOtherObject.getClass()) {
            return false;
        }
        final Trade myOtherTrade = (Trade) myOtherObject;
        return Float.compare(myOtherTrade.thePrice, thePrice) == 0 &&
               theAmount == myOtherTrade.theAmount &&
               Objects.equals(theProduct, myOtherTrade.theProduct) &&
               Objects.equals(theBuyer, myOtherTrade.theBuyer) &&
               Objects.equals(theSeller, myOtherTrade.theSeller) &&
               Objects.equals(theTime, myOtherTrade.theTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(theProduct, theBuyer, theSeller, thePrice, theAmount, theTime);
    }
}
