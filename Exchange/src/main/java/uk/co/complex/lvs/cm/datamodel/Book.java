package uk.co.complex.lvs.cm.datamodel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Lex van der Stoep on 06/12/2017.
 *
 * Book represents an order book of a market. It contains the results of orders which took place.
 * The book holds records of each trade, sorted by their execution time, newest first.
 */
public class Book {
    private final LinkedList<Trade> theRecords;

    /**
     * Constructs an empty book.
     */
    public Book() {
        theRecords = new LinkedList<>();
    }

    /**
     * Constructs a copy of a given book.
     * @param aBook the original record book
     */
    public Book(Book aBook) {
        theRecords = new LinkedList<>(aBook.theRecords);
    }

    /**
     * Returns all the records in the book.
     * @return a list of all the records in the book
     */
    public List<Trade> getAllRecords() {
        return new ArrayList<>(theRecords);
    }

    /**
     * Adds the given record to the book.
     * @param aRecord the record to be added
     */
    public void addRecord(Trade aRecord) {
        // Assert: record list sorted from new to old
        // Use linear search (as new record will most likely be inserted at the head)
        for (int i = 0; i < theRecords.size(); i++) {
            final Trade r = theRecords.get(i);
            if (r.getTime().compareTo(aRecord.getTime()) < 0) {
                theRecords.add(i, aRecord);
                return;
            }
        }
        theRecords.add(aRecord);
    }

    /**
     * Adds all the given records to the book.
     * @param records the records to be added
     */
    public void addAllRecords(List<Trade> records) {
        records.forEach((Trade r) -> addRecord(r));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book)) return false;

        final Book book = (Book) o;

        return theRecords.equals(book.theRecords);
    }
}
