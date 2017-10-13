package apidemo;

import static apidemo.ChinaData.sizeTotalMap;
import static apidemo.ChinaData.tradeTime;
import static apidemo.ChinaStock.nameMap;
import static apidemo.ChinaStock.symbolNames;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Box;
import javax.swing.SwingUtilities;

final class ChinaSizeData extends JPanel {

    BarModel_Size m_model;

    ChinaSizeData() {
        m_model = new BarModel_Size();
        JTable tab = new JTable(m_model);
        JScrollPane scroll = new JScrollPane(tab) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width = ChinaMain.GLOBALWIDTH;
                return d;
            }
        };

        setLayout(new BorderLayout());
        add(scroll, BorderLayout.WEST);
        tab.setAutoCreateRowSorter(true);
        JPanel jp = new JPanel();
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(l -> {
            SwingUtilities.invokeLater(() -> {
                m_model.fireTableDataChanged();
            });
        });
        jp.add(Box.createHorizontalStrut(100));
        jp.add(btnRefresh);
        add(jp, BorderLayout.NORTH);
    }

    private class BarModel_Size extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return symbolNames.size();
        }

        @Override
        public int getColumnCount() {
            return tradeTime.size() + 2;
        }

        @Override
        public String getColumnName(int col) {
            switch (col) {
                case 0:
                    return "T";
                case 1:
                    return "name";
                default:
                    return tradeTime.get(col - 2).toString();
            }
        }

        @Override
        public Class getColumnClass(int col) {
            switch (col) {
                case 0:
                    return String.class;
                case 1:
                    return String.class;
                default:
                    return Integer.class;
            }
        }

        @Override
        public Object getValueAt(int rowIn, int col) {
            String name = symbolNames.get(rowIn);
            switch (col) {
                case 0:
                    return name;
                case 1:
                    return nameMap.get(name);
                default:
                    return (sizeTotalMap.containsKey(name) && sizeTotalMap.get(name).size() > 0 && sizeTotalMap.get(name).containsKey(tradeTime.get(col - 2)))
                            ? (int) Math.round(sizeTotalMap.get(name).get(tradeTime.get(col - 2))) : 0;
            }
        }
    }
}
