package uk.co.complex.lvs.cm;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Lex van der Stoep on 06/12/2017.
 *
 * BookPrinter is a trade listener which prints the entire buy queue, sell queue and the book
 * to System.out on each update.
 */
public class BookPrinter implements TradeListener {
    @Override
    public void update(MarketManager manager) {
        ConcurrentLinkedQueue<Order> buyQueue = manager.getBuyQueue();
        ConcurrentLinkedQueue<Order> sellQueue = manager.getSellQueue();
        Book currentBook = manager.getBook();

        System.out.println("==============");
        System.out.println("Buy queue:");
        for (Order buyOrder: buyQueue) {
            System.out.println(buyOrder);
        }
        System.out.println("Sell queue:");
        for (Order sellOrder: sellQueue) {
            System.out.println(sellOrder);
        }
        System.out.println("Book:");
        for (TradeRecord record: currentBook.getAllRecords()) {
            System.out.println(record);
        }
    }
}
