package uk.co.complex.lvs.cm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lex van der Stoep on 06/12/2017.
 *
 * Book represents an order book of a market. It contains the results of orders which took place.
 * The book holds records of each trade, sorted by their execution time.
 */
public class Book {
    private final List<TradeRecord> mRecords;

    /**
     * Constructs an empty book.
     */
    public Book() {
        mRecords = new ArrayList<>();
    }

    /**
     * Constructs a copy of a given book.
     * @param original the original record book
     */
    public Book(Book original) {
        mRecords = new ArrayList<>(original.mRecords);
    }

    /**
     * Returns all the records in the book.
     * @return a list of all the records in the book
     */
    public List<TradeRecord> getAllRecords() {
        return new ArrayList<>(mRecords);
    }

    /**
     * Adds the given record to the book.
     * @param record the record to be added
     */
    public void addRecord(TradeRecord record) {
        mRecords.add(record);
    }

    /**
     * Adds all the given records to the book.
     * @param records the records to be added
     */
    public void addAllRecords(List<TradeRecord> records) {
        mRecords.addAll(records);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book)) return false;

        Book book = (Book) o;

        return mRecords.equals(book.mRecords);
    }
}
