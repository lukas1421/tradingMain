package handler;

import client.TickType;

import java.time.LocalDateTime;

import static apidemo.AutoTraderUS.*;
import static utility.Utility.pr;

public class USStockReceiver implements LiveHandler {
    @Override
    public void handlePrice(TickType tt, String symbol, double price, LocalDateTime t) {
        switch (tt) {
            case BID:
                usBidMap.put(symbol, price);
                break;

            case ASK:
                usAskMap.put(symbol, price);
                break;

            case OPEN:
                usOpenMap.put(symbol, price);
                break;

            case CLOSE:
                pr("close in US price receiver: ", symbol, " close ", price);
                break;
            case LAST:
                usFreshPriceMap.put(symbol, price);
                usPriceMapDetail.get(symbol).put(t, price);
                break;
        }
    }

    @Override
    public void handleVol(String name, double vol, LocalDateTime t) {

    }
}
