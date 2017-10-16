package apidemo;

import auxiliary.SimpleBar;
import graph.GraphBar;
import graph.GraphSize;
import utility.SharpeUtility;
import utility.Utility;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

public class HKStock extends JPanel {

    public static volatile Map<String, Double> hkCurrPrice = new HashMap<>();

    public static volatile Map<String, Double> hkVol = new HashMap<>();

    static GraphBar graph1 = new GraphBar();
    static GraphBar graph2 = new GraphBar();
    static GraphBar graph3 = new GraphBar();
    static GraphBar graph4 = new GraphBar();
    static GraphBar graph5 = new GraphBar();
    static GraphBar graph6 = new GraphBar();

    BarModel_HKStock m_model;
    static JPanel graphPanel;
    int modelRow;
    int indexRow;
    static JTable tab;

    static TableRowSorter<BarModel_HKStock> sorter;
    final File hkstockFile= new File(ChinaMain.GLOBALPATH+"hkMainList.txt");
    final File ahFile= new File(ChinaMain.GLOBALPATH+"AHList.txt");
    String line;
    public static List<String> symbolNamesHK = new LinkedList<>();
    public static Map<String, String> haMap  = new HashMap<>();
    public static Map<String, String> hkNameMap = new HashMap<>();

    static String hkstock1 = "700";
    static String hkstock2 = "2823";
    static String hkstock3 = "2822";
    static String hkstock4 = "3188";
    static String hkstock5 = "3147";
    static String hkstock6 = "1398";




    public HKStock() {

        try(BufferedReader reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(hkstockFile),"GBK"))){
            while((line=reader1.readLine())!=null) {
                //System.out.println(" hk line is " + line);
                List<String> l = Arrays.asList(line.split("\t"));
                symbolNamesHK.add(l.get(0));
                hkNameMap.put(l.get(0),l.get(1));
            }
            System.out.println(" hk size " + symbolNamesHK.size());

        } catch(IOException e) {
            e.printStackTrace();
        }

        try(BufferedReader reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(ahFile),"GBK"))) {
            while((line=reader1.readLine())!=null) {
                List<String> l = Arrays.asList(line.split("\t"));
                haMap.put(l.get(0),l.get(1));
            }

        } catch(IOException e) {
            e.printStackTrace();
        }

        //System.out.println(symbolNamesHK);

        m_model = new BarModel_HKStock();
        graphPanel = new JPanel();
        graphPanel.setLayout(new GridLayout(6,1));

        graph1.fillInGraphHK(hkstock1);
        graph2.fillInGraphHK(hkstock2);
        graph3.fillInGraphHK(hkstock3);
        graph4.fillInGraphHK(hkstock4);
        graph5.fillInGraphHK(hkstock5);
        graph6.fillInGraphHK(hkstock6);


        JScrollPane gp1 = genNewScrollPane(graph1);
        JScrollPane gp2 = genNewScrollPane(graph2);
        JScrollPane gp3 = genNewScrollPane(graph3);
        JScrollPane gp4 = genNewScrollPane(graph4);
        JScrollPane gp5 = genNewScrollPane(graph5);
        JScrollPane gp6 = genNewScrollPane(graph6);


        graphPanel.add(gp1);
        graphPanel.add(gp2);
        graphPanel.add(gp3);
        graphPanel.add(gp4);
        graphPanel.add(gp5);
        graphPanel.add(gp6);

        JPanel controlPanel = new JPanel();

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(al->{
            SwingUtilities.invokeLater(()->{
                graphPanel.repaint();
                tab.repaint();
                graph1.refresh(s-> graph1.fillInGraphHK(s));
                graph2.refresh(s-> graph2.fillInGraphHK(s));
                graph3.refresh(s-> graph3.fillInGraphHK(s));
                graph4.refresh(s-> graph4.fillInGraphHK(s));
                graph5.refresh(s-> graph5.fillInGraphHK(s));
                graph6.refresh(s-> graph6.fillInGraphHK(s));

            });
        });
        controlPanel.add(refreshButton);


        tab = new JTable(m_model){
            @Override
            public Component prepareRenderer(TableCellRenderer tableCellRenderer, int row, int col) {
                Component comp = super.prepareRenderer(tableCellRenderer, row, col);
                if(isCellSelected(row,col)){
                    modelRow = this.convertRowIndexToModel(row);
                    indexRow = row;
                    comp.setBackground(Color.green);
                } else {
                    comp.setBackground((row%2==0)?Color.lightGray:Color.white);
                }
                return comp;
            }
        };

        tab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    m_model.fireTableDataChanged();
                    tab.repaint();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tab) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width = 600;
                return d;
            }
        };

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.WEST);
        add(graphPanel, BorderLayout.CENTER);
        tab.setAutoCreateRowSorter(true);
        sorter = (TableRowSorter<BarModel_HKStock>)tab.getRowSorter();

    }

    static JScrollPane genNewScrollPane(JComponent g) {
        return new JScrollPane(g) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.height = 300;
                return d;
            }
        };
    }

    class BarModel_HKStock extends javax.swing.table.AbstractTableModel {

        @Override
        public int getRowCount() {
            return symbolNamesHK.size();
        }

        @Override
        public int getColumnCount() {
            return 15;
        }

        @Override
        public String getColumnName(int col) {
            switch (col) {
                case 0:
                    return "H";
                case 1:
                    return "A";
                case 2:
                    return "中";
                case 3:
                    return "H价";
                case 4:
                    return "Volume";
                case 5:
                    return "Rtn";
                case 6:
                    return "A价";
                case 7:
                    return "H/A";
                case 8:
                    return "Ytd Sharpe";
                case 9:
                    return "Wtd Sharpe";
                case 10:
                    return "Today Sharpe";
                default:
                    return "";
            }
        }

        @Override
        public Object getValueAt(int rowIn, int col) {
            String name = symbolNamesHK.get(rowIn);

            switch (col) {
                case 0:
                    return name;

                case 1:
                    return haMap.getOrDefault(name,"");

                case 2:
                    return hkNameMap.get(name);

                case 3:
                    return hkCurrPrice.getOrDefault(name, 0.0);
//                    if(HKData.hkPriceBar.containsKey(name) && HKData.hkPriceBar.get(name).size() > 0) {
//                        System.out.println(" hk size " + HKData.hkPriceBar.get(name).size());
//                        return HKData.hkPriceBar.get(name).lastEntry().getValue().getClose();
//                    } else {
//                        return 0;
//                    }

                case 4:
                    return Math.round((hkCurrPrice.getOrDefault(name,0.0)*hkVol.getOrDefault(name,0.0))/1000000d);
                case 5:
                    return computeTodayReturn(name);
                case 6:
                    return getASharePrice(name);
                case 7:
                    return getHPremiumOverA(name);
                case 8:
                    return 0.0;
                case 9:
                    return 0.0;
                case 10:
                    return computeTodayHKSharpe(name);

                default:
                    return null;
            }
        }

        public Class getColumnClass(int col) {
            switch (col) {
                case 0:
                    return String.class;
                case 1:
                    return String.class;
                case 2:
                    return String.class;

                default:
                    return Double.class;
            }
        }

    }

    static double getHPremiumOverA(String name) {
        double currentHKPrice = hkCurrPrice.getOrDefault(name, 0.0);
        double currentAPrice = getASharePrice(name)*1.2;

        if(currentHKPrice != 0.0 && currentAPrice!= 0.0) {
            return Math.round(1000d * (currentHKPrice / currentAPrice-1)) / 10d;
        }
        return 0.0;
    }

    static double getASharePrice(String hkTicker) {
        String aShare = haMap.getOrDefault(hkTicker,"");
        if(!aShare.equals("")) {
            String aShareTicker = Utility.addSHSZ(aShare);
            return ChinaStock.priceMap.getOrDefault(aShareTicker,0.0);
        }
        return 0.0;
    }

    static double computeTodayHKSharpe(String name) {
        if(HKData.hkPriceBar.containsKey(name)) {
            return SharpeUtility.computeMinuteSharpe(HKData.hkPriceBar.get(name));
        }
        return 0.0;
    }

    static double computeTodayReturn(String name) {

        if(HKData.hkPriceBar.containsKey(name) && HKData.hkPriceBar.get(name).size()>0) {
            //double open = HKData.hkPriceBar.get(name).firstEntry().getValue().getOpen();
            double prevClose = HKData.hkPreviousCloseMap.getOrDefault(name,0.0);
            //double last = HKData.hkPriceBar.get(name).lastEntry().getValue().getClose();
            double priceNow = hkCurrPrice.getOrDefault(name,0.0);
            return Math.round(1000d*(priceNow/prevClose-1))/10d;
        }
        return 0.0;
    }


    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.setSize(1900,1500);
        HKStock hks = new HKStock();
        jf.add(hks);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}
