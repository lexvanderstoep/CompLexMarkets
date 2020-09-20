package uk.co.complex.lvs.cm;

/**
 * Created by Lex van der Stoep on 06/12/2017.
 *
 * Status represents the status of an order.
 * NEW is a newly placed order which has not been traded;
 * PARTIAL is a partially completed order.
 * COMPLETED is a completed order - all units have been bought/sold.
 * CANCELLED is a cancelled - no more units can be bought/sold.
 */
public enum Status {
    NEW, PARTIAL, COMPLETED, CANCELLED
}
