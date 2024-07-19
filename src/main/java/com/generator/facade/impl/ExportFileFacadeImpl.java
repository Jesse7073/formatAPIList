package com.generator.facade.impl;

import com.generator.facade.IExportFileFacade;
import com.generator.facade.bo.ApiDetailOutput;
import com.generator.model.ApiDetail;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class ExportFileFacadeImpl implements IExportFileFacade {
    @Override
    public void exportExcel(List<ApiDetailOutput> apiDetailOutputList, String absolutePath) {
        try (Workbook workbook = new XSSFWorkbook()) { // 建立 Excel 工作簿
            Sheet sheet = workbook.createSheet("API List"); // sheet名稱

            Row headerRow = sheet.createRow(0); // 標題行
            this.createCell(headerRow, 0, "Controller");
            this.createCell(headerRow, 1, "URL");
            this.createCell(headerRow, 2, "Description");

            int rowIndex = 1; // 標記第一行開始處
            for (ApiDetailOutput output : apiDetailOutputList) {
                List<ApiDetail> apiDetailList = output.getApiDetailList();

                // 紀錄合併儲存格起始列數
                int startRowIndex = rowIndex;
                Row startRow = null;

                for (ApiDetail api : apiDetailList) {
                    Row row = sheet.createRow(rowIndex);
                    if (rowIndex == startRowIndex) {
                        startRow = row;
                    }
                    this.createCell(row, 1, api.getApiUrl());
                    this.createCell(row, 2, api.getApiDescription());
                    rowIndex++;
                }

                if (apiDetailList.size() > 1) {
                    // 合併儲存格
                    CellRangeAddress rowRegion = new CellRangeAddress(startRowIndex, startRowIndex + apiDetailList.size() - 1, 0, 0);
                    sheet.addMergedRegion(rowRegion);
                }
                if (startRow != null) {
                    this.createCell(startRow, 0, output.getCtrlName());
                }
            }

            String outputFile = this.setOutputDir(absolutePath);
            try (FileOutputStream out = new FileOutputStream(outputFile)) {
                workbook.write(out); // 寫入文件
                System.out.println("API list generated in " + outputFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createCell(Row row, int column, Object value) {
        Cell cell = row.createCell(column);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue((String) value);
        }
    }

    private String setOutputDir(String absolutePath) {
        int lastSlashIndex = absolutePath.lastIndexOf("\\");
        return absolutePath.substring(0, lastSlashIndex + 1) + "API_List.xlsx";
    }
}
