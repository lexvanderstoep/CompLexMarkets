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
    private final Product mProduct;
    private final Account mBuyer;
    private final Account mSeller;
    private final float mPrice;
    private final int mAmount;
    private final OffsetDateTime mTime;

    /**
     * Constructs a new trade record.
     * @param product the traded product
     * @param buyer the buyer of the product
     * @param seller the seller of the product
     * @param price the price at which the product was traded
     * @param amount the amount which was traded
     * @param time the time at which the trade was executed
     */
    public Trade(Product product, Account buyer, Account seller, float price, int amount,
                       OffsetDateTime time) {
        mProduct = product;
        mBuyer = buyer;
        mSeller = seller;
        mPrice = price;
        mAmount = amount;
        mTime = time;
    }

    /**
     * Returns the traded product.
     * @return the traded product
     */
    public Product getProduct() {
        return mProduct;
    }

    /**
     * Returns the buyer of the product.
     * @return the buyer of the product
     */
    public Account getBuyer() {
        return mBuyer;
    }

    /**
     * Returns the seller of the product.
     * @return the seller of the product
     */
    public Account getSeller() {
        return mSeller;
    }

    /**
     * Returns the price of the product.
     * @return the price of the product
     */
    public float getPrice() {
        return mPrice;
    }

    /**
     * Returns the amount of the product which was traded.
     * @return the amount which was traded
     */
    public int getAmount() {
        return mAmount;
    }

    /**
     * Returns the time at which the trade was executed.
     * @return the time at which the trade was executed
     */
    public OffsetDateTime getTime() {
        return mTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(mProduct.getName() + ": ");
        builder.append(mAmount + "x");
        builder.append(String.format("%.2f", mPrice) + " ");
        builder.append(mSeller.getName() + "->" + mBuyer.getName() + " ");
        builder.append("@ " + mTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
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
        return Float.compare(myOtherTrade.mPrice, mPrice) == 0 &&
               mAmount == myOtherTrade.mAmount &&
               Objects.equals(mProduct, myOtherTrade.mProduct) &&
               Objects.equals(mBuyer, myOtherTrade.mBuyer) &&
               Objects.equals(mSeller, myOtherTrade.mSeller) &&
               Objects.equals(mTime, myOtherTrade.mTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mProduct, mBuyer, mSeller, mPrice, mAmount, mTime);
    }
}
