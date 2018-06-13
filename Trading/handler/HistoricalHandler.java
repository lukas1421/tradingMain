package handler;

import apidemo.ChinaData;
import auxiliary.SimpleBar;
import utility.Utility;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

import static utility.Utility.pr;

public interface HistoricalHandler extends GeneralHandler {
    void handleHist(String name, String date, double open,
                    double high, double low, double close);

    void actionUponFinish(String name);

    class DefaultHistHandle implements HistoricalHandler {
        //Semaphore semaphore;

        public DefaultHistHandle() {

        }

        @Override
        public void handleHist(String name, String date, double open, double high, double low, double close) {
            pr("handle hist ", name, date, open, close);
            String ticker = Utility.addSHSZ(name);
            if (ChinaData.priceMapBar.containsKey(ticker)) {
                if (!date.startsWith("finished")) {
                    Date dt = new Date(Long.parseLong(date) * 1000);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dt);
                    LocalDate ld = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
                    LocalTime lt = LocalTime.of(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
                    ChinaData.priceMapBar.get(ticker).put(lt, new SimpleBar(open, high, low, close));
                    //pr("hist ", name, date, lt, open, high, low, close);
                }
            }

        }

        @Override
        public void actionUponFinish(String name) {
        }
    }
}
