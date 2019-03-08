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
    FUT_DEV,
    FUT_NIGHT_DEV,
    FUT_W_DEV,
    FUT_PC_PROFIT_TAKER,
    SGXA50_HILO,

    H900_DEV, //9:00
    H930_DEV, //9:30
    H1000_DEV, //10:00
    H1030_DEV, //10:30
    H1100_DEV, //11:00
    H1130_DEV, //11:30
    H1200_DEV, //12:00
    H1230_DEV, //12:30
    H1300_DEV, //13:00
    H1330_DEV, //13:30
    H1400_DEV, //14:00
    H1430_DEV, //14:30
    H1500_DEV, //15:00
    H1530_DEV, //15:30

    Q900_DEV, //9:00
    Q915_DEV, //9:15
    Q930_DEV, //9:30
    Q945_DEV, //9:45

    Q1000_DEV, //10:00
    Q1015_DEV, //10:15
    Q1030_DEV, //10:30
    Q1045_DEV, //10:45

    Q1100_DEV, //11:00
    Q1115_DEV, //11:15
    Q1130_DEV, //11:30
    Q1145_DEV, //11:45

    Q1200_DEV, //12:00
    Q1215_DEV, //12:15
    Q1230_DEV, //12:30
    Q1245_DEV, //12:45

    Q1300_DEV, //13:00
    Q1315_DEV, //13:15
    Q1330_DEV, //13:30
    Q1345_DEV, //13:45

    Q1400_DEV, //14:00
    Q1415_DEV, //14:15
    Q1430_DEV, //14:30
    Q1445_DEV, //14:45

    Q1500_DEV, //15:00
    Q1515_DEV, //15:15
    Q1530_DEV, //15:30
    Q1545_DEV, //15:45

    M930_DEV,
    M931_DEV,
    M932_DEV,
    M933_DEV,
    M934_DEV,
    M935_DEV,
    M936_DEV,
    M937_DEV,
    M938_DEV,
    M939_DEV,
    M940_DEV,
    M941_DEV,
    M942_DEV,
    M943_DEV,
    M944_DEV,
    M945_DEV,
    M946_DEV,
    M947_DEV,
    M948_DEV,
    M949_DEV,
    M950_DEV,
    M951_DEV,
    M952_DEV,
    M953_DEV,
    M954_DEV,
    M955_DEV,
    M956_DEV,
    M957_DEV,
    M958_DEV,
    M959_DEV,
    M1000_DEV,
    M1001_DEV,
    M1002_DEV,
    M1003_DEV,
    M1004_DEV,
    M1005_DEV,
    M1006_DEV,
    M1007_DEV,
    M1008_DEV,
    M1009_DEV,
    M1010_DEV,
    M1011_DEV,
    M1012_DEV,
    M1013_DEV,
    M1014_DEV,
    M1015_DEV,
    M1016_DEV,
    M1017_DEV,
    M1018_DEV,
    M1019_DEV,
    M1020_DEV,
    M1021_DEV,
    M1022_DEV,
    M1023_DEV,
    M1024_DEV,
    M1025_DEV,
    M1026_DEV,
    M1027_DEV,
    M1028_DEV,
    M1029_DEV,
    M1030_DEV,
    M1031_DEV,
    M1032_DEV,
    M1033_DEV,
    M1034_DEV,
    M1035_DEV,
    M1036_DEV,
    M1037_DEV,
    M1038_DEV,
    M1039_DEV,
    M1040_DEV,
    M1041_DEV,
    M1042_DEV,
    M1043_DEV,
    M1044_DEV,
    M1045_DEV,
    M1046_DEV,
    M1047_DEV,
    M1048_DEV,
    M1049_DEV,
    M1050_DEV,
    M1051_DEV,
    M1052_DEV,
    M1053_DEV,
    M1054_DEV,
    M1055_DEV,
    M1056_DEV,
    M1057_DEV,
    M1058_DEV,
    M1059_DEV,


    SGXA50_POST_CUTOFF_LIQ,
    SGXA50_W_POSTCUTOFF_LIQ,
    FUT_HILO_ACCU,
    FUT_TENTA,
    FUT_TENTA_COVER,
    FUT_KO,
    FTSEA50_OPEN_DEVI,
    FTSEA50_NEW_HILO,
    FORCE_FILL,
    US_DEV,
    US_RELATIVE_TAKE_PROFIT,
    US_STOCK_PMOPENDEV,
    US_STOCK_HILO,
    US_STOCK_PMHILO,
    US_CLOSE_LIQ,
    US_POST_AMCUTOFF_LIQ,
    US_POST_PMCUTOFF_LIQ,

    HK_FUT_WDEV,
    HK_STOCK_DEV,
    HK_STOCK_HILO,
    HK_STOCK_PMHILO,
    HK_CLOSE_LIQ,
    HK_W_POSTCUTOFF_LIQ,
    HK_POST_AMCUTOFF_LIQ,
    HK_POST_PMCUTOFF_LIQ,

    BREACH_ADDER,
    BREACH_CUTTER,
    BREACH_WEEKLY_CUTTER,
    BREACH_MDEV,
    UNKNOWN
}
