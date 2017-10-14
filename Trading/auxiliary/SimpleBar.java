package auxiliary;

import static utility.Utility.getStr;
import java.io.Serializable;
import java.util.function.BinaryOperator;

public class SimpleBar implements Serializable, Comparable<SimpleBar> {

    static final long serialVersionUID = -34735107L;

    double open;
    double high;
    double low;
    double close;

    private static SimpleBar ZERO_BAR = new SimpleBar(0.0);

    public SimpleBar() {
        open = 0.0;
        high = 0.0;
        low = 0.0;
        close = 0.0;
    }

    public SimpleBar(double o, double h, double l, double c) {
        this.open = o;
        this.high = h;
        this.low = l;
        this.close = c;
    }

    public static BinaryOperator<SimpleBar> addSB() {
        return (a, b) -> new SimpleBar(r(a.getOpen() + b.getOpen()), r(a.getHigh() + b.getHigh()),
                r(a.getLow() + b.getLow()), r(a.getClose() + b.getClose()));
    }

    public SimpleBar(SimpleBar sb) {
        open = sb.getOpen();
        high = sb.getHigh();
        low = sb.getLow();
        close = sb.getClose();
    }

    public static final SimpleBar getZeroBar() {
        return ZERO_BAR;
    }

    public SimpleBar(double v) {
        open = v;
        high = v;
        low = v;
        close = v;
    }

    public void adjustByFactor(double f) {
        //System.out.println ( ChinaStockHelper.getStr("BEFORE open high low close ",open, high, low, close ));
        open = open * f;
        high = high * f;
        low = low * f;
        close = close * f;
        //System.out.println ( ChinaStockHelper.getStr("AFTER open high low close ",open, high, low, close ));
    }

    public void updateOpen(double o) {
        open = o;
    }

    public void updateHigh(double h) {
        high = h;
    }

    public void updateLow(double l) {
        low = l;
    }

    public void updateClose(double c) {
        close = c;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public void add(double last) {
        if (open == 0.0 || high == 0.0 || low == 0.0 || close == 0.0) {
            open = last;
            high = last;
            low = last;
            close = last;
        } else {
            close = last;
            if (last > high) {
                high = last;
            }
            if (last < low) {
                low = last;
            }
        }
    }

    public void round() {
        open = Math.round(100d * open) / 100d;
        high = Math.round(100d * high) / 100d;
        low = Math.round(100d * low) / 100d;
        close = Math.round(100d * close) / 100d;
    }

    public static double r(double n) {
        return Math.round(n * 100d) / 100d;
    }

    /**
     * if any contains zero
     */
    public boolean containsZero() {
        return (open == 0 || high == 0.0 || low == 0.0 || close == 0.0);
    }

    public boolean normalBar() {
        return (open != 0 && high != 0 && low != 0.0 && close != 0.0);
    }

    public double getHLRange() {
        return (low != 0.0) ? (high / low - 1) : 0.0;
    }

    public double getBarReturn() {
        return (open != 0.0) ? (close / open - 1) : 0.0;
    }

    public int getOP() {
        return (int) ((open - low) / (high - low) * 100d);
    }

    public int getCP() {
        return (int) ((close - low) / (high - low) * 100d);
    }

    @Override
    public String toString() {
        return getStr("open:", open, " high:", high, "low:", low, "close:", close);
    }

    @Override
    public int compareTo(SimpleBar o) {
        return this.high >= o.high ? 1 : -1;
    }
}