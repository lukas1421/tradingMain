package AutoTraderOld;

import enums.FutType;
import client.*;
import controller.ApiController;
import util.AutoOrderType;
import utility.TradingUtility;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.concurrent.CountDownLatch;

import static AutoTraderOld.XuTraderHelper.outputToAll;
import static utility.Utility.*;

public class XUTraderRoll extends JPanel {

    private static ApiController apcon;
    private static Contract frontContract = TradingUtility.getFrontFutContract();
    private static Contract backContract = TradingUtility.getBackFutContract();
    public static volatile EnumMap<FutType, Integer> contractID = new EnumMap<FutType, Integer>(FutType.class);
    public static CountDownLatch latch = new CountDownLatch(2);

    XUTraderRoll(ApiController ap) {
        apcon = ap;
    }


    private static Contract longRollContract() {
        Contract ct = new Contract();
        ct.symbol("XINA50");
        ct.secType(Types.SecType.BAG);
        ct.exchange("SGX");
        ct.currency("USD");

        ArrayList<ComboLeg> l = new ArrayList<>();
        ComboLeg leg1 = new ComboLeg();
        ComboLeg leg2 = new ComboLeg();

        leg1.ratio(1);
        leg1.conid(contractID.get(FutType.FrontFut));
        leg1.action(Types.Action.SELL);
        leg1.exchange("SGX");

        leg2.ratio(1);
        leg2.conid(contractID.get(FutType.BackFut));
        leg2.action(Types.Action.BUY);
        leg2.exchange("SGX");

        l.add(leg1);
        l.add(leg2);
        pr(" front back conID ", contractID.get(FutType.FrontFut), contractID.get(FutType.BackFut));

        ct.comboLegs(l);
        pr(ct);
        return ct;
    }

    static void getContractDetails() {
        apcon.reqContractDetails(frontContract, new ApiController.IContractDetailsHandler.DefaultContractDetailsHandler());
        apcon.reqContractDetails(backContract, new ApiController.IContractDetailsHandler.DefaultContractDetailsHandler());
    }

    void shortRoll(double p) {
        int id = AutoTraderMain.autoTradeID.incrementAndGet();
        Order o = TradingUtility.placeOfferLimit(p, 1);
        AutoTraderMain.globalIdOrderMap.put(id, new OrderAugmented(
                (AutoTraderXU.activeFutCt), LocalDateTime.now(), o, AutoOrderType.SHORT_ROLL));
        apcon.placeOrModifyOrder(longRollContract(), o, new AutoOrderDefaultHandler(id));
        outputToAll(str(o.orderId(), " Short Roll ", AutoTraderMain.globalIdOrderMap.get(id)));
    }

    public static void longRoll(double p) {
        int id = AutoTraderMain.autoTradeID.incrementAndGet();
        Order o = TradingUtility.placeBidLimit(p, 1);
        AutoTraderMain.globalIdOrderMap.put(id, new OrderAugmented(
                (AutoTraderXU.activeFutCt), LocalDateTime.now(), o, AutoOrderType.LONG_ROLL));
        apcon.placeOrModifyOrder(longRollContract(), o, new AutoOrderDefaultHandler(id));
        outputToAll(str(o.orderId(), " Long Roll ", AutoTraderMain.globalIdOrderMap.get(id)));
    }

    public static void resetLatch() {
        latch = new CountDownLatch(2);
    }


}