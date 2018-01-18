package uk.co.complex.lvs.cm.gui;

import layout.TableLayout;
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
    private final long PLOT_INTERVAL = 2000;
    private JList<String> sellQList;
    private JList<String> buyQList;
    private JList<String> bookList;
    private JLabel priceLabel;
    private Optional<Float> lastPrice = Optional.empty();
    private DataPlot pricePlot;
    private ArrayList<Float> priceHistory = new ArrayList<>();

    public MarketVisualisation() {
        Thread pricePlotter = new Thread(() -> {
            while(true) {
                updatePricePlot();
                try {
                    Thread.sleep(PLOT_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        pricePlotter.start();
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

        // Set up the table layout
        JPanel panel = new JPanel();
        panel.setBackground(Color.darkGray);
        frame.setContentPane(panel);
        double size[][] =
                {{350, TableLayout.FILL}, {50, 0.5, 0.5}};
        panel.setLayout(new TableLayout(size));

        // Add the components
        pricePlot = new DataPlot(priceHistory, 50, 20, "time", "price", Color.gray);
        sellQList = new JList();
        sellQList.setForeground(new Color(241, 0, 0));
        buyQList = new JList();
        buyQList.setForeground(new Color(0, 200, 0));
        bookList = new JList();
        priceLabel = new JLabel();
        priceLabel.setFont(new Font("TimesRoman", Font.BOLD, 16));
        JScrollPane sellPane = new JScrollPane(sellQList);
        sellPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        JScrollPane buyPane = new JScrollPane(buyQList);
        buyPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        JScrollPane bookPane = new JScrollPane(bookList);
        bookPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        priceLabel.setHorizontalAlignment(JLabel.CENTER);
        priceLabel.setForeground(Color.white);
        panel.add(priceLabel, "0, 0, 1, 0");
        panel.add(sellPane, "0, 1, 0, 0");
        panel.add(buyPane, "0, 2, 0, 0");
        panel.add(pricePlot, "1, 1, 0, 0");
        panel.add(bookPane, "1, 2, 0, 0");

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void update(MarketManager manager) {
        PriceTimePriorityQueue buyQueue = manager.getBuyQueue(manager.getProducts().get(0));
        PriceTimePriorityQueue sellQueue = manager.getSellQueue(manager.getProducts().get(0));
        buyQList.setListData(toString(buyQueue));
        sellQList.setListData(toString(sellQueue));
        bookList.setListData(toString(manager.getBook().getAllRecords()));

        Optional<Float> price = getPrice(sellQueue, buyQueue);
        lastPrice = price;

        if (!price.isPresent()) {
            priceLabel.setText("NO PRICE");
        } else {
            priceLabel.setText(String.format("%.2f", price.get()));
        }
    }

    private Optional<Float> getPrice(PriceTimePriorityQueue sellQueue,
                                     PriceTimePriorityQueue buyQueue) {
        if (sellQueue.isEmpty() | buyQueue.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of((sellQueue.first().getPrice() + buyQueue.first().getPrice())/2.0f);
        }
    }

    private void updatePricePlot() {
        if (lastPrice.isPresent()) {
            priceHistory.add(lastPrice.get());
        }
        if (pricePlot != null) {
            pricePlot.updateData(priceHistory);
        }
    }

    private Vector<String> toString(Collection collection) {
        return new Vector<>((List<String>)collection.stream()
                .map(object -> Objects.toString(object, null))
                .collect(Collectors.toList()));
    }

    public static void main(String[] args) {
        Product ibm = new Product("IBM");
        Account alice = new Account("Alice");
        Account bob = new Account("Bob");
        bob.updateBook(ibm, 100);
        MarketManager manager = new MarketManager(
                new ArrayList<>(Arrays.asList(ibm)));


        MarketVisualisation visualiser = new MarketVisualisation();
        visualiser.createAndShowGUI();
        manager.addTradeListener(visualiser);


        RandomIntervalProductTrader randomAlice = new RandomIntervalProductTrader(
                alice, ibm, manager,50.0f, 100.0f,
                1, 10, 1000, 2000);
        RandomIntervalProductTrader randomBob = new RandomIntervalProductTrader(
                bob, ibm, manager,50.0f, 100.0f,
                1, 10, 1000, 2000);
        randomAlice.start();
        randomBob.start();
    }
}
