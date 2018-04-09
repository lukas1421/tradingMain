package handler;

import TradeType.FutureTrade;
import TradeType.TradeBlock;
import apidemo.FutType;
import apidemo.XUTrader;
import client.CommissionReport;
import client.Contract;
import client.Execution;
import controller.ApiController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static utility.Utility.ibContractToFutType;

public class XUOvernightTradeExecHandler implements ApiController.ITradeReportHandler {


    public static XUOvernightTradeExecHandler DefaultOvernightHandler = new XUOvernightTradeExecHandler();

    @Override
    public void tradeReport(String tradeKey, Contract contract, Execution execution) {

        LocalTime now = LocalTime.now();
        LocalDate TDate = now.isAfter(LocalTime.of(0, 0)) && now.isBefore(LocalTime.of(5, 0)) ? LocalDate.now().minusDays(1L)
                : LocalDate.now();

        int sign = (execution.side().equals("BOT")) ? 1 : -1;

        LocalDateTime ldt = LocalDateTime.parse(execution.time(), DateTimeFormatter.ofPattern("yyyyMMdd  HH:mm:ss"));

        //if (ldt.getDayOfMonth() == LocalDateTime.now().getDayOfMonth()) {
        if (ldt.isAfter(LocalDateTime.of(TDate, LocalTime.of(15, 0)))) {
            System.out.println(" in XUOvernightTradeExechandler ");

            FutType f = ibContractToFutType(contract);
            System.out.println(" exec " + execution.side() + "　" + execution.time() + " " + execution.cumQty()
                    + " " + execution.price() + " " + execution.orderRef() + " " + execution.orderId() + " " + execution.permId() + " "
                    + execution.shares());

            if (XUTrader.overnightTradesMap.get(f).containsKey(ldt)) {
                XUTrader.overnightTradesMap.get(f).get(ldt)
                        .addTrade(new FutureTrade(execution.price(), sign * execution.cumQty()));
            } else {
                XUTrader.overnightTradesMap.get(f).put(ldt,
                        new TradeBlock(new FutureTrade(execution.price(), sign * execution.cumQty())));
            }
        }
    }

    @Override
    public void tradeReportEnd() {
        System.out.println(" OVERNIGHT trade report ended ");
    }

    @Override
    public void commissionReport(String tradeKey, CommissionReport commissionReport) {

    }
}
