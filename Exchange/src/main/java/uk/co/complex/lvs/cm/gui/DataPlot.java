package uk.co.complex.lvs.cm.gui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Lex van der Stoep on 08/12/2017.
 *
 * DataPlot can plot a given data set. It plots the points as a line graph. The data consists of
 * y-coordinates. The index of an element is the x-coordinate.
 *
 * This class is a modified version of <i>hardwired</i>'s answer (#4) in the forum thread
 * <a href="https://www.java-forums.org/new-java/7995-how-plot-graph-java-given-samples.html">
 *     https://www.java-forums.org/new-java/7995-how-plot-graph-java-given-samples.html</a>.
 */
public class DataPlot extends JPanel {
    private List<Float> mData;
    private int mXPadding;
    private int mYPadding;
    private String mXLabel;
    private String mYLabel;
    private Color mLineColor;

    /**
     * Constructs a new DataPlot, initialised with a certain data set.
     * @param data the data to be plotted
     * @param horPadding the horizontal padding around the graph
     * @param verPadding the vertical padding around the graph
     * @param xLabel the label at the x-axis
     * @param yLabel the label at the y-axis
     * @param line the color of the line connecting the data points
     */
    public DataPlot(List<Float> data, int horPadding, int verPadding, String xLabel, String yLabel, Color line) {
        mData = data;
        mXPadding = horPadding;
        mYPadding = verPadding;
        mXLabel = xLabel;
        mYLabel = yLabel;
        mLineColor = line;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();

        // Draw vertical axis
        g2.draw(new Line2D.Double(mXPadding, mYPadding, mXPadding, h-mYPadding));

        // Draw horizontal axis
        g2.draw(new Line2D.Double(mXPadding, h-mYPadding, w-mXPadding, h-mYPadding));

        // Draw labels
        Font font = g2.getFont();
        FontRenderContext frc = g2.getFontRenderContext();
        LineMetrics lm = font.getLineMetrics("0", frc);
        float sh = lm.getAscent() + lm.getDescent();

        // Vertical label
        String s = mYLabel;
        float sy = mYPadding + ((h - 2*mYPadding) - s.length()*sh)/2 + lm.getAscent();
        for (int i = 0; i < s.length(); i++) {
            String letter = String.valueOf(s.charAt(i));
            float sw = (float) font.getStringBounds(letter, frc).getWidth();
            float sx = (mXPadding - sw)/2;
            g2.drawString(letter, sx, sy);
            sy += sh;
        }

        // Horizontal label
        s = mXLabel;
        sy = h - mYPadding + (mYPadding - sh)/2 + lm.getAscent();
        float sw = (float) font.getStringBounds(s, frc).getWidth();
        float sx = (w - sw)/2;
        g2.drawString(s, sx, sy);

        // Draw values
        float min = getMin() * 0.9f;
        float max = getMax() * 1.1f;
        if (mData.size() > 1) {
            String bottomString = new DecimalFormat("#.##").format(min);
            String topString = new DecimalFormat("#.##").format(max);
            float widthBottom = (float) font.getStringBounds(bottomString, frc).getWidth();
            float widthTop = (float) font.getStringBounds(topString, frc).getWidth();
            g2.drawString(bottomString, mXPadding - widthBottom - 5, h - mYPadding);
            g2.drawString(topString, mXPadding - widthTop - 5, mYPadding);
        }

        // Draw lines
        double xInc = (double) (w - 2*mXPadding)/(mData.size()-1);
        double scale = (double) (h - 2*mYPadding)/max;
        g2.setPaint(mLineColor);
        for (int i = 0; i < mData.size()-1; i++) {
            double x1 = mXPadding + i*xInc;
            double y1 = h - mYPadding - scale*mData.get(i);
            double x2 = mXPadding + (i+1)*xInc;
            double y2 = h - mYPadding - scale*mData.get(i+1);
            g2.draw(new Line2D.Double(x1, y1, x2, y2));
        }
    }

    private float getMax() {
        float max = -Integer.MAX_VALUE;
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i) > max)
                max = mData.get(i);
        }
        return max;
    }

    private float getMin() {
        float min = Integer.MAX_VALUE;
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i) < min)
                min = mData.get(i);
        }
        return min;
    }

    public void updateData(List<Float> data) {
        mData = data;
        revalidate();
        repaint();
    }
}
