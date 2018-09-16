package handler;

import TradeType.FutureTrade;
import TradeType.TradeBlock;
import apidemo.FutType;
import client.CommissionReport;
import client.Contract;
import client.Execution;
import controller.ApiController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static apidemo.AutoTraderXU.overnightTradesMap;
import static utility.Utility.ibContractToFutType;

public class XUOvernightTradeExecHandler implements ApiController.ITradeReportHandler {


    public static XUOvernightTradeExecHandler DefaultOvernightHandler = new XUOvernightTradeExecHandler();

    @Override
    public void tradeReport(String tradeKey, Contract contract, Execution execution) {
        LocalTime now = LocalTime.now();
        LocalDate TDate = now.isAfter(LocalTime.of(0, 0)) && now.isBefore(LocalTime.of(5, 0)) ?
                LocalDate.now().minusDays(1L) : LocalDate.now();
        int sign = (execution.side().equals("BOT")) ? 1 : -1;
        LocalDateTime ldt = LocalDateTime.parse(execution.time(), DateTimeFormatter.ofPattern("yyyyMMdd  HH:mm:ss"));

        if (ldt.isAfter(LocalDateTime.of(TDate, LocalTime.of(17, 0)))) {
            //System.out.println(" in XUOvernightTradeExecHandler ");

            FutType f = ibContractToFutType(contract);
//            System.out.println(" exec " + execution.side() + "　" + execution.time() + " " + execution.cumQty()
//                    + " " + execution.price() + " " + execution.orderRef() + " " + execution.orderId() + " " + execution.permId() + " "
//                    + execution.shares());

            if (overnightTradesMap.get(f).containsKey(ldt)) {
                overnightTradesMap.get(f).get(ldt)
                        .addTrade(new FutureTrade(execution.price(), (int) Math.round(sign * execution.shares())));
            } else {
                overnightTradesMap.get(f).put(ldt,
                        new TradeBlock(new FutureTrade(execution.price(),
                                (int) Math.round(sign * execution.shares()))));
            }
        }
    }

    @Override
    public void tradeReportEnd() {
        //System.out.println(" OVERNIGHT trade report ended ");
    }

    @Override
    public void commissionReport(String tradeKey, CommissionReport commissionReport) {

    }
}
