package apidemo;

import client.Order;
import client.OrderAugmented;
import client.OrderStatus;
import controller.ApiController;
import util.AutoOrderType;

import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static apidemo.ChinaData.priceMapBarDetail;
import static apidemo.ChinaStock.*;
import static apidemo.XuTraderHelper.*;
import static client.OrderStatus.*;
import static util.AutoOrderType.*;
import static utility.Utility.str;

public class AutoTraderMain extends JPanel {

    private static Set<LocalDate> holidaySet = new TreeSet<>();

    static BarModel_AUTO m_model;

    public AutoTraderMain() {
        String line;
        try (BufferedReader reader1 = new BufferedReader(new InputStreamReader(
                new FileInputStream(TradingConstants.GLOBALPATH + "holidaySchedule.txt"), "gbk"))) {
            while ((line = reader1.readLine()) != null) {
                LocalDate d1 = LocalDate.parse(line, DateTimeFormatter.ofPattern("yyyy/M/d"));
                holidaySet.add(d1);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //global
    public static AtomicBoolean globalTradingOn = new AtomicBoolean(false);
    public static volatile AtomicInteger autoTradeID = new AtomicInteger(100);
    public static volatile NavigableMap<Integer, OrderAugmented> globalIdOrderMap = new ConcurrentSkipListMap<>();
    public static volatile Map<Integer, Order> liveIDOrderMap = new ConcurrentHashMap<>();
    static volatile Map<String, TreeSet<Order>> liveSymbolOrderSet = new ConcurrentHashMap<>();
    static final double SGXA50_AUTO_VOL_THRESH = 0.25;

    static volatile AtomicBoolean noMoreSell = new AtomicBoolean(false);
    static volatile AtomicBoolean noMoreBuy = new AtomicBoolean(false);

    static ApiController apcon;

    static File xuDetailOutput = new File(TradingConstants.GLOBALPATH + "xuOrdersDetailed.txt");
    static File hkDetailOutput = new File(TradingConstants.GLOBALPATH + "hkOrdersDetailed.txt");
    static File usDetailOutput = new File(TradingConstants.GLOBALPATH + "usOrdersDetailed.txt");

    //zones
    public static final ZoneId chinaZone = ZoneId.of("Asia/Shanghai");
    public static final ZoneId nyZone = ZoneId.of("America/New_York");

    //position
    static volatile Map<String, Double> ibPositionMap = new ConcurrentHashMap<>();

    static long getOrderSizeForTradeType(String symbol, AutoOrderType type) {
        return globalIdOrderMap.entrySet().stream()
                .filter(e -> e.getValue().getSymbol().equals(symbol))
                .filter(e -> e.getValue().getOrderType() == type)
                .filter(e -> e.getValue().isPrimaryOrder())
                .count();
    }

//    static long getOrderSizeForTradeType(String symbol, AutoOrderType type) {
//        return globalIdOrderMap.entrySet().stream()
//                .filter(e -> e.getValue().getSymbol().equals(symbol))
//                .filter(e -> e.getValue().getOrderType() == type)
//                .filter(e -> e.getValue().isPrimaryOrder())
//                .count();
//    }

    static double getFilledForType(String symbol, AutoOrderType type) {
        return globalIdOrderMap.entrySet().stream()
                .filter(e -> e.getValue().getSymbol().equals(symbol))
                .filter(e -> e.getValue().getOrderType() == type)
                .filter(e -> e.getValue().getAugmentedOrderStatus() == Filled)
                .mapToDouble(e -> e.getValue().getOrder().signedTotalQuantity())
                .sum();
    }

    static boolean checkIfHoliday(LocalDate d) {
        //pr(d, " is a holiday? ", holidaySet.contains(d), "!");
        return holidaySet.contains(d);
    }

    /**
     * cancelling primary orders only (can only cancel 1 min)
     *
     * @param now      time now
     * @param deadline deadeline
     */
    static void cancelAllOrdersAfterDeadline(LocalTime now, LocalTime deadline) {
        if (now.isAfter(deadline) && now.isBefore(deadline.plusMinutes(1L))) {
            globalIdOrderMap.entrySet().stream()
                    .filter(e -> e.getValue().getAugmentedOrderStatus() != Filled)
                    .filter(e -> e.getValue().getAugmentedOrderStatus() != Inactive)
                    .filter(e -> e.getValue().isPrimaryOrder())
                    .filter(e -> !isCutoffOrLiqTrader(e.getValue().getOrderType()))
                    .forEach(e -> {
                        OrderStatus sta = e.getValue().getAugmentedOrderStatus();
                        if ((sta != Filled) && (sta != PendingCancel) && (sta != Cancelled) &&
                                (sta != DeadlineCancelled)) {
                            apcon.cancelOrder(e.getValue().getOrder().orderId());
                            e.getValue().setFinalActionTime(LocalDateTime.now());
                            e.getValue().setAugmentedOrderStatus(OrderStatus.DeadlineCancelled);
                            outputToAll(str(now, " Cancel ALL after deadline ",
                                    e.getValue().getOrder().orderId(), e.getValue().getSymbol(),
                                    e.getValue().getOrderType(),
                                    "status CHG:", sta, "->", e.getValue().getAugmentedOrderStatus()));
                        }
                    });
        }
    }

    private static boolean isCutoffOrLiqTrader(AutoOrderType tt) {
        return (tt == FTSEA50_POST_AMCUTOFF || tt == FTSEA50_POST_PMCUTOFF ||
                tt == HK_POST_AMCUTOFF_LIQ || tt == HK_POST_PMCUTOFF_LIQ ||
                tt == SGXA50_POST_CUTOFF_LIQ || tt == US_POST_AMCUTOFF_LIQ ||
                tt == US_POST_PMCUTOFF_LIQ || tt == SGXA50_CLOSE_LIQ
                || tt == US_CLOSE_LIQ || tt == HK_CLOSE_LIQ);
    }

    static AutoOrderType getOrderTypeByHalfHour(HalfHour h) {
        switch (h) {
            case H900:
                return H900_DEV;
            case H930:
                return H930_DEV;
            case H1000:
                return H1000_DEV;
            case H1030:
                return H1030_DEV;
            case H1100:
                return H1100_DEV;
            case H1130:
                return H1130_DEV;
            case H1200:
                return H1200_DEV;
            case H1230:
                return H1230_DEV;
            case H1300:
                return H1300_DEV;
            case H1330:
                return H1330_DEV;
            case H1400:
                return H1400_DEV;
            case H1430:
                return H1430_DEV;
            case H1500:
                return H1500_DEV;
            case H1530:
                return H1530_DEV;
        }
        throw new IllegalStateException(" not found");
    }

    static AutoOrderType getOrderTypeByQuarterHour(QuarterHour h) {
        switch (h) {
            case Q900:
                return Q900_DEV;
            case Q915:
                return Q915_DEV;
            case Q930:
                return Q930_DEV;
            case Q945:
                return Q945_DEV;

            case Q1000:
                return Q1000_DEV;
            case Q1015:
                return Q1015_DEV;
            case Q1030:
                return Q1030_DEV;
            case Q1045:
                return Q1045_DEV;

            case Q1100:
                return Q1100_DEV;
            case Q1115:
                return Q1115_DEV;
            case Q1130:
                return Q1130_DEV;
            case Q1145:
                return Q1145_DEV;

            case Q1200:
                return Q1200_DEV;
            case Q1215:
                return Q1215_DEV;
            case Q1230:
                return Q1230_DEV;
            case Q1245:
                return Q1245_DEV;

            case Q1300:
                return Q1300_DEV;
            case Q1315:
                return Q1315_DEV;
            case Q1330:
                return Q1330_DEV;
            case Q1345:
                return Q1345_DEV;

            case Q1400:
                return Q1400_DEV;
            case Q1415:
                return Q1415_DEV;
            case Q1430:
                return Q1430_DEV;
            case Q1445:
                return Q1445_DEV;
            case Q1500:
                return Q1500_DEV;
            case Q1515:
                return Q1515_DEV;
            case Q1530:
                return Q1530_DEV;
            case Q1545:
                return Q1545_DEV;
        }
        throw new IllegalStateException(" not found");
    }

    static AutoOrderType getOrderTypeByMinuteHour(MinuteHour m) {
        switch (m) {
            case M930:
                return M930_DEV;
            case M931:
                return M931_DEV;
            case M932:
                return M932_DEV;
            case M933:
                return M933_DEV;
            case M934:
                return M934_DEV;
            case M935:
                return M935_DEV;
            case M936:
                return M936_DEV;
            case M937:
                return M937_DEV;
            case M938:
                return M938_DEV;
            case M939:
                return M939_DEV;
            case M940:
                return M940_DEV;
            case M941:
                return M941_DEV;
            case M942:
                return M942_DEV;
            case M943:
                return M943_DEV;
            case M944:
                return M944_DEV;
            case M945:
                return M945_DEV;
            case M946:
                return M946_DEV;
            case M947:
                return M947_DEV;
            case M948:
                return M948_DEV;
            case M949:
                return M949_DEV;
            case M950:
                return M950_DEV;
            case M951:
                return M951_DEV;
            case M952:
                return M952_DEV;
            case M953:
                return M953_DEV;
            case M954:
                return M954_DEV;
            case M955:
                return M955_DEV;
            case M956:
                return M956_DEV;
            case M957:
                return M957_DEV;
            case M958:
                return M958_DEV;
            case M959:
                return M959_DEV;
            case M1000:
                return M1000_DEV;
            case M1001:
                return M1001_DEV;
            case M1002:
                return M1002_DEV;
            case M1003:
                return M1003_DEV;
            case M1004:
                return M1004_DEV;
            case M1005:
                return M1005_DEV;
            case M1006:
                return M1006_DEV;
            case M1007:
                return M1007_DEV;
            case M1008:
                return M1008_DEV;
            case M1009:
                return M1009_DEV;
            case M1010:
                return M1010_DEV;
            case M1011:
                return M1011_DEV;
            case M1012:
                return M1012_DEV;
            case M1013:
                return M1013_DEV;
            case M1014:
                return M1014_DEV;
            case M1015:
                return M1015_DEV;
            case M1016:
                return M1016_DEV;
            case M1017:
                return M1017_DEV;
            case M1018:
                return M1018_DEV;
            case M1019:
                return M1019_DEV;
            case M1020:
                return M1020_DEV;
            case M1021:
                return M1021_DEV;
            case M1022:
                return M1022_DEV;
            case M1023:
                return M1023_DEV;
            case M1024:
                return M1024_DEV;
            case M1025:
                return M1025_DEV;
            case M1026:
                return M1026_DEV;
            case M1027:
                return M1027_DEV;
            case M1028:
                return M1028_DEV;
            case M1029:
                return M1029_DEV;
            case M1030:
                return M1030_DEV;
            case M1031:
                return M1031_DEV;
            case M1032:
                return M1032_DEV;
            case M1033:
                return M1033_DEV;
            case M1034:
                return M1034_DEV;
            case M1035:
                return M1035_DEV;
            case M1036:
                return M1036_DEV;
            case M1037:
                return M1037_DEV;
            case M1038:
                return M1038_DEV;
            case M1039:
                return M1039_DEV;
            case M1040:
                return M1040_DEV;
            case M1041:
                return M1041_DEV;
            case M1042:
                return M1042_DEV;
            case M1043:
                return M1043_DEV;
            case M1044:
                return M1044_DEV;
            case M1045:
                return M1045_DEV;
            case M1046:
                return M1046_DEV;
            case M1047:
                return M1047_DEV;
            case M1048:
                return M1048_DEV;
            case M1049:
                return M1049_DEV;
            case M1050:
                return M1050_DEV;
            case M1051:
                return M1051_DEV;
            case M1052:
                return M1052_DEV;
            case M1053:
                return M1053_DEV;
            case M1054:
                return M1054_DEV;
            case M1055:
                return M1055_DEV;
            case M1056:
                return M1056_DEV;
            case M1057:
                return M1057_DEV;
            case M1058:
                return M1058_DEV;
            case M1059:
                return M1059_DEV;
        }
        throw new IllegalStateException(" not found");
    }

    public static LocalTime ltof(int h, int m) {
        return LocalTime.of(h, m);
    }

    static LocalTime ltof(int h, int m, int s) {
        return LocalTime.of(h, m, s);
    }

    static LocalDateTime getLastOrderTime(String symbol, AutoOrderType type) {
        return globalIdOrderMap.entrySet().stream()
                .filter(e -> e.getValue().getSymbol().equals(symbol))
                .filter(e -> e.getValue().getOrderType() == type)
                .filter(e -> e.getValue().isPrimaryOrder())
                .max(Comparator.comparing(e -> e.getValue().getOrderTime()))
                .map(e -> e.getValue().getOrderTime())
                .orElse(sessionOpenT());
    }


    static long lastTwoOrderMilliDiff(String symbol, AutoOrderType type) {
        long numOrders = globalIdOrderMap.entrySet().stream()
                .filter(e -> e.getValue().getSymbol().equals(symbol))
                .filter(e -> e.getValue().getOrderType() == type)
                .filter(e -> e.getValue().isPrimaryOrder())
                .count();
        if (numOrders < 2) {
            return Long.MAX_VALUE;
        } else {
            LocalDateTime last = globalIdOrderMap.entrySet().stream()
                    .filter(e -> e.getValue().getSymbol().equals(symbol))
                    .filter(e -> e.getValue().getOrderType() == type)
                    .filter(e -> e.getValue().isPrimaryOrder())
                    .max(Comparator.comparing(e -> e.getValue().getOrderTime()))
                    .map(e -> e.getValue().getOrderTime()).orElseThrow(() -> new IllegalArgumentException("no"));
            LocalDateTime secLast = globalIdOrderMap.entrySet().stream()
                    .filter(e -> e.getValue().getSymbol().equals(symbol))
                    .filter(e -> e.getValue().getOrderType() == type)
                    .filter(e -> e.getValue().isPrimaryOrder())
                    .map(e -> e.getValue().getOrderTime())
                    .filter(e -> e.isBefore(last))
                    .max(Comparator.comparing(Function.identity())).orElseThrow(() -> new IllegalArgumentException("no"));
            return ChronoUnit.MILLIS.between(secLast, last);
        }
    }

    /**
     * cancel order of type after deadline
     *
     * @param now      time now
     * @param symbol   symbol
     * @param type     order type
     * @param deadline deadline after which to cut
     */
    static void cancelAfterDeadline(LocalTime now, String symbol, AutoOrderType type, LocalTime deadline) {
        if (now.isAfter(deadline) && now.isBefore(deadline.plusMinutes(1L))) {
            globalIdOrderMap.entrySet().stream()
                    .filter(e -> e.getValue().getSymbol().equals(symbol))
                    .filter(e -> e.getValue().getOrderType() == type)
                    .filter(e -> e.getValue().getAugmentedOrderStatus() != Filled)
                    .filter(e -> e.getValue().getAugmentedOrderStatus() != Inactive)
                    .forEach(e -> {
                        OrderStatus sta = e.getValue().getAugmentedOrderStatus();
                        if ((sta != Filled) && (sta != PendingCancel) && (sta != Cancelled) &&
                                (sta != DeadlineCancelled)) {
                            apcon.cancelOrder(e.getValue().getOrder().orderId());
                            e.getValue().setFinalActionTime(LocalDateTime.now());
                            e.getValue().setAugmentedOrderStatus(OrderStatus.DeadlineCancelled);
                            String msg = str(now, " Cancel after deadline ",
                                    e.getValue().getOrder().orderId(), "status CHG:",
                                    sta, "->", e.getValue().getAugmentedOrderStatus());
                            outputSymbolMsg(e.getValue().getSymbol(), msg);
                            outputToAll(msg);
                        }
                    });
        }
    }

    static int minuteToQuarterHour(int min) {
        return (min - min % 15);
    }

    static int getWaitSec(long milliLast2) {
        if (milliLast2 < 10000) {
            return 300;
        } else if (milliLast2 < 60000) {
            return 60;
        } else {
            return 0;
        }
    }

    static double getPriceOffset(long milliLast2, double price) {
        if (milliLast2 < 10000) {
            return price * 0.002;
        } else if (milliLast2 < 60000) {
            return price * 0.001;
        } else {
            return 0;
        }
    }

    private class BarModel_AUTO extends javax.swing.table.AbstractTableModel {

        @Override
        public int getRowCount() {
            return priceMapBarDetail.size();
            //return symbolNamesFull.size();
        }

        @Override
        public int getColumnCount() {
            return 10;
        }

        @Override
        public String getColumnName(int col) {
            switch (col) {
                case 0:
                    return "T";
                case 1:
                    return "名";
                case 2:
                    return "业";
                case 3:
                    return "参";
                default:
                    return null;
            }
        }

        @Override
        public Object getValueAt(int rowIn, int col) {

            String name = symbolNamesFull.get(rowIn);
            //String name = priceMapBarDetail.keySet().stream().collect(toList()).get(rowIn);

            switch (col) {
                //T
                case 0:
                    return name;
                //名
                case 1:
                    return nameMap.get(name);
                //业
                case 2:
                    return industryNameMap.get(name);
                //bench simple
                case 3:
                    return benchSimpleMap.getOrDefault(name, "");

                default:
                    return null;
            }
        }

        @Override
        public Class getColumnClass(int col) {
            switch (col) {
                case 0:
                    return String.class;
                case 1:
                    return String.class;
                case 2:
                    return String.class;

                default:
                    return String.class;
            }
        }
    }
}



