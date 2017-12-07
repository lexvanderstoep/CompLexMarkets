package uk.co.complex.lvs.cm.tests;

import org.junit.jupiter.api.Test;
import uk.co.complex.lvs.cm.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MarketManagerTest {
    @Test
    void cancelOrder() throws IllegalTradeException {
        Account alice = new Account("Alice");
        Account bob = new Account("Bob");
        Product xyz = new Product("XYZ");
        List<Product> allProducts = new ArrayList<>();
        allProducts.add(xyz);
        MarketManager manager = new MarketManager(allProducts);
        Book checkBook = new Book();

        // Add a buy order by alice
        Order aliceBuy1 = new Order(xyz, 100.00f, 20, alice, Side.BUY, OffsetDateTime.now());
        List<TradeRecord> records = manager.placeOrder(aliceBuy1);
        assertEquals(records, new ArrayList<TradeRecord>());
        assertEquals(aliceBuy1.getRemainingAmount(), 20);
        assertEquals(aliceBuy1.getStatus(), Status.NEW);
        assertEquals(manager.getBook(), checkBook);

        // Cancel the order from alice
        manager.cancelOrder(aliceBuy1);
        assertEquals(aliceBuy1.getRemainingAmount(), 20);
        assertEquals(aliceBuy1.getStatus(), Status.CANCELLED);

        // Add a sell order by bob which is good for alice (but which is already cancelled)
        Order bobSell2 = new Order(xyz, 100.00f, 30, bob, Side.SELL, OffsetDateTime.now());
        records = manager.placeOrder(bobSell2);
        checkBook.addAllRecords(records);
        assertEquals(records.size(), 0);
        assertEquals(aliceBuy1.getRemainingAmount(), 20);
        assertEquals(aliceBuy1.getStatus(), Status.CANCELLED);
        assertEquals(bobSell2.getRemainingAmount(), 30);
        assertEquals(bobSell2.getStatus(), Status.NEW);
        assertEquals(manager.getBook(), checkBook);
    }

    @Test
    void placeOrder() throws IllegalTradeException {
        Account alice = new Account("Alice");
        Account bob = new Account("Bob");
        Product xyz = new Product("XYZ");
        List<Product> allProducts = new ArrayList<>();
        allProducts.add(xyz);
        MarketManager manager = new MarketManager(allProducts);
        Book checkBook = new Book();

        // Add a buy order by alice
        Order aliceBuy1 = new Order(xyz, 100.00f, 20, alice, Side.BUY, OffsetDateTime.now());
        List<TradeRecord> records = manager.placeOrder(aliceBuy1);
        assertEquals(records, new ArrayList<TradeRecord>());
        assertEquals(aliceBuy1.getRemainingAmount(), 20);
        assertEquals(aliceBuy1.getStatus(), Status.NEW);
        assertEquals(manager.getBook(), checkBook);



        // Add a sell order by bob which is too high for alice
        Order bobSell1 = new Order(xyz, 100.10f, 40, bob, Side.SELL, OffsetDateTime.now());
        records = manager.placeOrder(bobSell1);
        assertEquals(records, new ArrayList<TradeRecord>());
        assertEquals(aliceBuy1.getRemainingAmount(), 20);
        assertEquals(aliceBuy1.getStatus(), Status.NEW);
        assertEquals(bobSell1.getRemainingAmount(), 40);
        assertEquals(bobSell1.getStatus(), Status.NEW);
        assertEquals(manager.getBook(), checkBook);



        // Add a sell order by bob which is good for alice (and some left over)
        Order bobSell2 = new Order(xyz, 100.00f, 30, bob, Side.SELL, OffsetDateTime.now());
        records = manager.placeOrder(bobSell2);
        checkBook.addAllRecords(records);
        assertEquals(records.size(), 1);
        assertEquals(records.get(0), new TradeRecord(xyz, alice, bob, 100.00f, 20,
                records.get(0).getTime()));
        assertEquals(aliceBuy1.getRemainingAmount(), 0);
        assertEquals(aliceBuy1.getStatus(), Status.COMPLETED);
        assertEquals(bobSell1.getRemainingAmount(), 40);
        assertEquals(bobSell1.getStatus(), Status.NEW);
        assertEquals(bobSell2.getRemainingAmount(), 10);
        assertEquals(bobSell2.getStatus(), Status.PARTIAL);
        assertEquals(manager.getBook(), checkBook);
    }

}