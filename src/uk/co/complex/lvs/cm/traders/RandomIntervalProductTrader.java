package uk.co.complex.lvs.cm.traders;

import uk.co.complex.lvs.cm.*;

import java.time.OffsetDateTime;
import java.util.Random;

/**
 * Created by Lex van der Stoep on 07/12/2017.
 */
public class RandomIntervalProductTrader {
    private final Account mAccount;
    private final Product mProduct;
    private final MarketManager mManager;
    private final Side mSide;
    private final float mMinValue;
    private final float mMaxValue;
    private final int mMinAmount;
    private final int mMaxAmount;
    private final int mMinWait;
    private final int mMaxWait;
    private final Thread tradingThread;
    private final Random rnd = new Random();
    private boolean stop = false;

    /**
     * Constructs a new random trader. The trader will randomly trade (buy/sell) a specified product
     * on the market repeatedly over certain time intervals.
     *
     * The price is in the interval [minValue, maxValue], the amount is in the interval
     * [minAmount, maxAmount], the time interval is [timeInterval-latency,timeInterval+latency].
     * @param account the account of the trader
     * @param product the product to trade in
     * @param market the market to trade in
     * @param side the side of the order (buy/sell)
     * @param minValue the minimum of the order price
     * @param maxValue the maximum of the order price
     * @param minAmount the minimum of the order amount
     * @param maxAmount the maximum of the order amount
     * @param minWait the minimum time (in milliseconds) to wait between order placements
     * @param maxWait the maximum time (in milliseconds) to wait between order placements
     */
    public RandomIntervalProductTrader(Account account, Product product, MarketManager market,
                                       Side side, float minValue, float maxValue, int minAmount,
                                       int maxAmount, int minWait, int maxWait) {
        mAccount = account;
        mProduct = product;
        mManager = market;
        mSide = side;
        mMinValue = minValue;
        mMaxValue = maxValue;
        mMinAmount = minAmount;
        mMaxAmount = maxAmount;
        mMinWait = minWait;
        mMaxWait = maxWait;

        tradingThread = new Thread() {
            @Override
            public void run() {
                while(true) {
                    if (stop) return;
                    int waitTime = rnd.nextInt(maxWait-minWait) + minWait;
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    performTrade();
                }
            }
        };
    }

    private void performTrade() {
        int amount = rnd.nextInt(mMaxAmount - mMinAmount) + mMinAmount;
        float price = rnd.nextFloat() * (mMaxValue - mMinValue) + mMinValue;
        Order order = new Order(mProduct, price, amount, mAccount, mSide, OffsetDateTime.now());
        try {
            mManager.placeOrder(order);
        } catch (IllegalTradeException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        tradingThread.start();
    }

    public void stop() {
        stop = true;
    }
}
