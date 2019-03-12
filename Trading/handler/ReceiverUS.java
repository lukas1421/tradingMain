package handler;

import api.ChinaData;
import api.ChinaStock;
import client.Contract;
import client.TickType;
import utility.Utility;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import static AutoTraderOld.AutoTraderUS.*;
import static utility.Utility.*;


public class ReceiverUS implements LiveHandler {
    private String symbolToReceive;

    public ReceiverUS(String symbol) {
        symbolToReceive = symbol;
    }

//    private static final ReceiverUS rec = new ReceiverUS();
//    public static ReceiverUS getReceiverUS() {
//        return rec;
//    }

    @Override
    public void handlePrice(TickType tt, Contract ct, double price, LocalDateTime t) {
        String symbol = ibContractToSymbol(ct);
        ZonedDateTime chinaZdt = ZonedDateTime.of(t, Utility.chinaZone);
        ZonedDateTime usZdt = chinaZdt.withZoneSameInstant(Utility.nyZone);
        LocalDateTime usLdt = usZdt.toLocalDateTime();
        LocalTime usLt = usLdt.toLocalTime();

        switch (tt) {
            case LAST:
                usFreshPriceMap.put(symbol, price);
                if (usLt.isAfter(ltof(3, 0)) && usLt.isBefore(ltof(17, 0))) {
                    ChinaData.priceMapBarDetail.get(symbol).put(usLdt, price);
//                    outputToUSPriceTest("***********");
//                    outputToUSPriceTest(str(" US -> PMB Detailed, SYMB, tt, symb, price,lastEntry "
//                            , symbolToReceive, tt, symbol, price, ChinaData.priceMapBarDetail.get(symbol).lastEntry()));
                }
                processMainUS(symbol, usLdt, price);
                ChinaStock.priceMap.put(symbol, price);
//                outputToUSPriceTest(str(" US handle price ", symbolToReceive,
//                        tt, symbol, price, "ChinaT: ", t, "US T:", usLdt));
                break;
            case BID:
                usBidMap.put(symbol, price);
                break;
            case ASK:
                usAskMap.put(symbol, price);
                break;
            case OPEN:
                usOpenMap.put(symbol, price);
                break;
        }

    }

    @Override
    public void handleVol(TickType tt, String name, double vol, LocalDateTime t) {


    }

    @Override
    public void handleGeneric(TickType tt, String symbol, double value, LocalDateTime t) {
        switch (tt) {
            case SHORTABLE:
                outputDetailedUSSymbol(symbol, str("US handle generic", tt, symbol, value, t));
                usShortableValueMap.put(symbol, value);
                break;

        }
    }
}
