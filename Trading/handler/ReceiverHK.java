package handler;

import apidemo.ChinaData;
import client.TickType;

import java.time.LocalDateTime;

import static apidemo.AutoTraderHK.*;
import static utility.Utility.pr;


public class ReceiverHK implements LiveHandler {

    private ReceiverHK() {
    }

    private static ReceiverHK rec = new ReceiverHK();

    public static ReceiverHK getReceiverHK() {
        return rec;
    }

    @Override
    // name starts with hk
    public void handlePrice(TickType tt, String symbol, double price, LocalDateTime t) {
        pr("hk tt name, price t", tt, symbol, price, t);

        switch (tt) {
            case BID:
                hkBidMap.put(symbol, price);
                break;
            case ASK:
                hkAskMap.put(symbol, price);
                break;
            case OPEN:
                hkOpenMap.put(symbol, price);
                break;
            case LAST:
                //hkPriceMapDetail.get(name).put(t, price);
                ChinaData.priceMapBarDetail.get(symbol).put(t.toLocalTime(), price);
                hkFreshPriceMap.put(symbol, price);
                processeMainHK(symbol, t, price);
                break;
        }
    }

    @Override
    public void handleVol(String name, double vol, LocalDateTime t) {

    }
}