package uk.co.complex.lvs.cm;

import org.junit.Test;
import uk.co.complex.lvs.cm.datamodel.Account;
import uk.co.complex.lvs.cm.datamodel.Order;
import uk.co.complex.lvs.cm.datamodel.Product;
import uk.co.complex.lvs.cm.datamodel.Side;
import uk.co.complex.lvs.cm.datamodel.Status;

import java.time.OffsetDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class OrderTest {
    @Test
    public void tradeProduct() {
        Account alice = new Account("Alice");
        Product xyz = new Product("XYZ");
        Order aliceBuy1 = new Order(xyz, 100.10f, 20, alice, Side.BUY, OffsetDateTime.now());
        assertEquals(aliceBuy1.getRemainingAmount(), 20);
        assertEquals(aliceBuy1.getStatus(), Status.NEW);

        try {
            aliceBuy1.tradeProduct(0);
            fail("It should not be allowed to trade nothing");
        } catch (IllegalArgumentException e) {

        }
        assertEquals(aliceBuy1.getRemainingAmount(), 20);
        assertEquals(aliceBuy1.getStatus(), Status.NEW);

        aliceBuy1.tradeProduct(10);
        assertEquals(aliceBuy1.getRemainingAmount(), 10);
        assertEquals(aliceBuy1.getStatus(), Status.PARTIAL);


        try {
            aliceBuy1.tradeProduct(20);
            fail("It should not be allowed to trade more than available");
        } catch (IllegalArgumentException e) {

        }
        assertEquals(aliceBuy1.getRemainingAmount(), 10);
        assertEquals(aliceBuy1.getStatus(), Status.PARTIAL);


        aliceBuy1.tradeProduct(10);
        assertEquals(aliceBuy1.getRemainingAmount(), 0);
        assertEquals(aliceBuy1.getStatus(), Status.COMPLETED);


        try {
            aliceBuy1.tradeProduct(10);
            fail("It should not be allowed to trade more than available");
        } catch (IllegalArgumentException e) {

        }
    }

}