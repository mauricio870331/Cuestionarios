package Utils;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Mauricio
 */
public class TooltipJTable extends DefaultTableCellRenderer {

   

    public TooltipJTable() {
        
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int colum) {
        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, colum);
        int filan = table.getSelectedColumnCount();
//        int columnas = table.getColumnCount();
//            table.getTableHeader().setBackground(Color.cyan);
        Color c = new Color(50, 205, 50);
        if (filan>0 && colum == 1) {
            if (cell.getBackground() != c) {
                cell.setBackground(c);
            } else {
                cell.setBackground(Color.WHITE);
            }
        }

//        if (colum > 0) {
//            if (value instanceof Integer) {
//                Color c = new Color(152, 251, 152);
//                cell.setBackground(c);
//                cell.setForeground(c);
//            }
//            if (value == null) {
//                cell.setBackground(null);
//                cell.setForeground(Color.BLACK);
//            }
//        }
        return cell;
    }

}
