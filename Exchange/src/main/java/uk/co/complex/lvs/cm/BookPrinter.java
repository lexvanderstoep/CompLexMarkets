package uk.co.complex.lvs.cm;

import uk.co.complex.lvs.cm.datamodel.Book;
import uk.co.complex.lvs.cm.datamodel.Order;
import uk.co.complex.lvs.cm.datamodel.Product;
import uk.co.complex.lvs.cm.datamodel.Trade;

/**
 * Created by Lex van der Stoep on 06/12/2017.
 *
 * BookPrinter is a trade listener which prints the entire buy queue, sell queue and the book
 * to System.out on each update.
 */
public class BookPrinter implements TradeListener {
    @Override
    public void update(MarketManager manager) {
        Book currentBook = manager.getBook();

        System.out.println("==============");
        System.out.println("Buy queue:");
        for (Product p: manager.getProducts()) {
            PriceTimePriorityQueue buyQueue = manager.getBuyQueue(p);
            for (Order buyOrder : buyQueue) {
                System.out.println(buyOrder);
            }
        }
        System.out.println("Sell queue:");
        for (Product p: manager.getProducts()) {
            PriceTimePriorityQueue sellQueue = manager.getSellQueue(p);
            for (Order sellOrder : sellQueue) {
                System.out.println(sellOrder);
            }
        }
        System.out.println("Book:");
        for (Trade record: currentBook.getAllRecords()) {
            System.out.println(record);
        }
    }
}
