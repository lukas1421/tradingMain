package util;

public enum AutoOrderType {
    UNCON_MA,
    FAST,
    INVENTORY_OPEN,
    INVENTORY_CLOSE,
    OVERNIGHT,
    TEST,
    PERC_ACC,
    PERC_DECC,
    PERC,
    PD_OPEN,
    PD_CLOSE,
    FLATTEN,
    FLATTEN_AGGRESSIVE,
    DRIFT,
    SHORT_ROLL,
    LONG_ROLL,
    HIT_BID,
    LIFT_OFFER,
    ON_OFFER,
    ON_BID,
    DAY_COVER,
    DAY_BUY,
    DAY_SELL,
    SLOW_COVER,
    PERC_MA,
    INTRADAY_MA,
    LAST_HOUR_MA,
    TRIM,
    FAST_COVER,
    AM_HEDGE,
    DAY_SELLBACK,
    FTSEA50_FIRST_TICK,
    FTSEA50_HILO,
    FTSEA50_POST_AMCUTOFF,
    FTSEA50_POST_PMCUTOFF,
    SGXA50_CLOSE_LIQ,
    SGXA50_RELATIVE_TAKE_PROFIT,
    FTSEA50_PM_HILO,
    FTSEA50_PM_OPEN_DEVI,
    FTSEA50_HILO_ACCU,
    HEDGE_ON_OFF,
    FTICK_TAKE_PROFIT,
    CLOSE_TAKE_PROFIT,
    INTRADAY_FIRSTTICK_ACCU,
    FUT_DAY_MA,
    FUT_FAST_MA,
    SGXA50_OPEN,
    FUT_OPEN_DEVI,
    FUT_PC_PROFIT_TAKER,
    SGXA50_HILO,

    H9_DEV, //9
    H930_DEV, //9:30
    H10_DEV, //10
    H1030_DEV, //10:30
    H11_DEV, //11:00
    H1130_DEV, //11:30
    H12_DEV, //12:00
    H1230_DEV, //12:30
    H13_DEV, //13:00
    H1330_DEV, //13:30
    H14_DEV, //14:00
    H1430_DEV, //14:30
    H15_DEV, //15:00
    H1530_DEV, //15:30

    SGXA50_POST_CUTOFF_LIQ,
    FUT_HILO_ACCU,
    FUT_TENTA,
    FUT_TENTA_COVER,
    FUT_KO,
    FTSEA50_OPEN_DEVI,
    FTSEA50_NEW_HILO,
    FORCE_FILL,
    US_STOCK_OPENDEV,
    US_RELATIVE_TAKE_PROFIT,
    US_STOCK_PMOPENDEV,
    US_STOCK_HILO,
    US_STOCK_PMHILO,
    US_CLOSE_LIQ,
    US_POST_AMCUTOFF_LIQ,
    US_POST_PMCUTOFF_LIQ,
    HK_STOCK_DEV,
    HK_STOCK_HILO,
    HK_STOCK_PMHILO,
    HK_CLOSE_LIQ,
    HK_POST_AMCUTOFF_LIQ,
    HK_POST_PMCUTOFF_LIQ,
    UNKNOWN
}
