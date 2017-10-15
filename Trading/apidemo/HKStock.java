package apidemo;

import graph.GraphBar;
import graph.GraphSize;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class HKStock extends JPanel {



    static GraphBar graph1 = new GraphBar();
    static GraphBar graph2 = new GraphBar();
    static GraphBar graph3 = new GraphBar();
    static GraphBar graph4 = new GraphBar();
    static GraphBar graph5 = new GraphBar();
    static GraphBar graph6 = new GraphBar();

    BarModel_HKStock m_model;
    int modelRow;
    int indexRow;
    static JTable tab;

    static TableRowSorter<BarModel_HKStock> sorter;

    public HKStock() {

        m_model = new BarModel_HKStock();

        JPanel controlPanel = new JPanel();

        tab = new JTable(){
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
        setLayout(new BorderLayout());

        //test

        //test


    }

    class BarModel_HKStock extends javax.swing.table.AbstractTableModel {

        @Override
        public int getRowCount() {
            return HKData.hkPriceBar.size();
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
                    return "Chn";
                case 2:

                default:
                    return "";
            }
        }

        @Override
        public Object getValueAt(int rowIn, int col) {
            String name = HKData.hkNames.get(rowIn);
            switch (col) {
                case 0:
                    return name;
                case 1:
                    return name;
                case 2:

                default:
                    return null;
            }
        }

        public Class getColumnClass(int col) {
            switch (col) {
                case 1:
                    return String.class;
                default:
                    return String.class;
            }
        }

    }

}
