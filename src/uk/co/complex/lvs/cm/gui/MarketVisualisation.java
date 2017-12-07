package uk.co.complex.lvs.cm.gui;

import net.miginfocom.swing.MigLayout;
import uk.co.complex.lvs.cm.*;
import uk.co.complex.lvs.cm.traders.RandomIntervalProductTrader;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Lex van der Stoep on 07/12/2017.
 *
 * MarketVisualisation is a GUI for visualising the market. It displays the buy and sell queues,
 * and it displays the trade record book.
 */

public class MarketVisualisation implements TradeListener {
    private final JList<String> sellQList;
    private final JList<String> buyQList;
    private final JList<String> bookList;

    public MarketVisualisation() {
        sellQList = new JList();
        sellQList.setForeground(new Color(241, 0, 0));
        buyQList = new JList();
        buyQList.setForeground(new Color(0, 200, 0));
        bookList = new JList();
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowGUI() {
        //Create and set up the window
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Market Visualisation");
        frame.setSize(new Dimension(1500, 550));
        frame.setMinimumSize(new Dimension(700, 450));

        // Set up the three column split
        JPanel panel = new JPanel();
        frame.setContentPane(panel);
        panel.setLayout(new MigLayout("", "[]", "[grow]"));
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setContinuousLayout(true);
        splitPane.setResizeWeight(1.0f);
        splitPane.setBorder(null);
        splitPane.setDividerLocation(450);
        JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane2.setContinuousLayout(true);
        splitPane2.setBorder(null);
        splitPane2.setBottomComponent(splitPane);

        // Add the components
        JScrollPane sellPane = new JScrollPane(sellQList);
        sellPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        JScrollPane buyPane = new JScrollPane(buyQList);
        buyPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        JScrollPane bookPane = new JScrollPane(bookList);
        bookPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        splitPane2.setTopComponent(sellPane);
        splitPane.setTopComponent(buyPane);
        splitPane.setBottomComponent(bookPane);
        panel.add(splitPane2, "push, grow");
        sellPane.setPreferredSize(new Dimension(300, 0));
        buyPane.setPreferredSize(new Dimension(300, 0));
        bookPane.setPreferredSize(new Dimension(300,0));

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void update(MarketManager manager) {
        buyQList.setListData(toString(manager.getBuyQueue()));
        sellQList.setListData(toString(manager.getSellQueue()));
        bookList.setListData(toString(manager.getBook().getAllRecords()));
    }

    private Vector<String> toString(Collection collection) {
        return new Vector<>((List<String>)collection.stream()
                .map(object -> Objects.toString(object, null))
                .collect(Collectors.toList()));
    }

    public static void main(String[] args) {
        final Product xyz = new Product("XYZ");
        MarketManager manager = new MarketManager(
                new ArrayList<>(Arrays.asList(xyz)));


        MarketVisualisation visualiser = new MarketVisualisation();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                visualiser.createAndShowGUI();
            }
        });
        manager.addTradeListener(visualiser);


        RandomIntervalProductTrader alice = new RandomIntervalProductTrader(
                new Account("Alice"), xyz, manager, Side.BUY, 50.0f, 100.0f,
                1, 10, 1000, 2000);
        RandomIntervalProductTrader bob = new RandomIntervalProductTrader(
                new Account("Bob"), xyz, manager, Side.SELL, 50.0f, 100.0f,
                1, 10, 1000, 2000);
        RandomIntervalProductTrader carl = new RandomIntervalProductTrader(
                new Account("Carl"), xyz, manager, Side.BUY, 50.0f, 100.0f,
                1, 10, 1000, 2000);
        RandomIntervalProductTrader dylan = new RandomIntervalProductTrader(
                new Account("Dylan"), xyz, manager, Side.SELL, 50.0f, 100.0f,
                1, 10, 1000, 2000);
        alice.start();
        bob.start();
        carl.start();
        dylan.start();
    }
}
