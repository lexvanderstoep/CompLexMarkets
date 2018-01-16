package uk.co.complex.lvs.cm.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
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
    private final int mPadding;
    private final String mXLabel;
    private final String mYLabel;
    private final Color mLineColor;
    private final Color mPointColor;
    private boolean markEnabled;

    /**
     * Constructs a new DataPlot, initialised with a certain data set.
     * @param data the data to be plotted
     * @param padding the padding around the graph
     * @param xLabel the label at the x-axis
     * @param yLabel the label at the y-axis
     * @param line the color of the line connecting the data points
     * @param point the color of the data points
     */
    public DataPlot(List<Float> data, int padding, String xLabel, String yLabel, Color line,
                    Color point) {
        mData = data;
        mPadding = padding;
        mXLabel = xLabel;
        mYLabel = yLabel;
        mLineColor = line;
        mPointColor = point;
        markEnabled = true;
    }
    public DataPlot(List<Float> data, int padding, String xLabel, String yLabel, Color line) {
        this(data, padding, xLabel, yLabel, line, null);
        markEnabled = false;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        // Draw ordinate.
        g2.draw(new Line2D.Double(mPadding, mPadding, mPadding, h-mPadding));
        // Draw abcissa.
        g2.draw(new Line2D.Double(mPadding, h-mPadding, w-mPadding, h-mPadding));
        // Draw labels.
        Font font = g2.getFont();
        FontRenderContext frc = g2.getFontRenderContext();
        LineMetrics lm = font.getLineMetrics("0", frc);
        float sh = lm.getAscent() + lm.getDescent();
        // Ordinate label.
        String s = mYLabel;
        float sy = mPadding + ((h - 2*mPadding) - s.length()*sh)/2 + lm.getAscent();
        for(int i = 0; i < s.length(); i++) {
            String letter = String.valueOf(s.charAt(i));
            float sw = (float)font.getStringBounds(letter, frc).getWidth();
            float sx = (mPadding - sw)/2;
            g2.drawString(letter, sx, sy);
            sy += sh;
        }
        // Abcissa label.
        s = mXLabel;
        sy = h - mPadding + (mPadding - sh)/2 + lm.getAscent();
        float sw = (float)font.getStringBounds(s, frc).getWidth();
        float sx = (w - sw)/2;
        g2.drawString(s, sx, sy);
        // Draw lines.
        double xInc = (double)(w - 2*mPadding)/(mData.size()-1);
        double scale = (double)(h - 2*mPadding)/getMax();
        g2.setPaint(mLineColor);
        for(int i = 0; i < mData.size()-1; i++) {
            double x1 = mPadding + i*xInc;
            double y1 = h - mPadding - scale*mData.get(i);
            double x2 = mPadding + (i+1)*xInc;
            double y2 = h - mPadding - scale*mData.get(i+1);
            g2.draw(new Line2D.Double(x1, y1, x2, y2));
        }
        if (markEnabled) {
            // Mark data points.
            g2.setPaint(mPointColor);
            for (int i = 0; i < mData.size(); i++) {
                double x = mPadding + i * xInc;
                double y = h - mPadding - scale * mData.get(i);
                g2.fill(new Ellipse2D.Double(x - 2, y - 2, 4, 4));
            }
        }
    }

    private float getMax() {
        float max = -Integer.MAX_VALUE;
        for(int i = 0; i < mData.size(); i++) {
            if(mData.get(i) > max)
                max = mData.get(i);
        }
        return max;
    }

    public void updateData(List<Float> data) {
        mData = data;
        revalidate();
        repaint();
    }
}
