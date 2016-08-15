/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JTable;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Mauricio Herrera
 */
public class ExportExcel {

    Workbook wb;

    public ExportExcel() {

    }

    public String Export(File archivo, JTable tbControlPatienst) {
        String response;
        int numFilas = tbControlPatienst.getRowCount(), numColumnas = tbControlPatienst.getColumnCount();
        if (archivo.getName().endsWith("xls")) {
            wb = new HSSFWorkbook();
        } else {
            wb = new XSSFWorkbook();
        }
        Sheet hoja = wb.createSheet("Reporte General");
        try {
            for (int i = -1; i < numFilas; i++) {
                Row fila = hoja.createRow(i + 1);
                for (int j = 0; j < numColumnas; j++) {
                    Cell celda = fila.createCell(j);
                    if (i == -1) {
                        celda.setCellValue(String.valueOf(tbControlPatienst.getColumnName(j)));
                    } else {
                        celda.setCellValue(String.valueOf(tbControlPatienst.getValueAt(i, j)));
                    }
                    wb.write(new FileOutputStream(archivo));
                }
            }
            response = "Exportacion Exitosa";
        } catch (Exception e) {
            response = "Error:" + e;
        }
        return response;
    }
}
