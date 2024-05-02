package com.modeling.utils;

import com.csvreader.CsvReader;
import com.modeling.exception.BusinessException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 描述：ExcelReadUtil
 *
 * @author zrx
 */
public class ExcelReadUtil {


    /**
     * 获取一个xlsx文件中所有sheet表的表头
     *
     * @param inputStream 文件输入流
     * @return java.util.List<java.util.List < java.lang.String>>
     * @author zrx
     **/
    public static List<List<String>> getSheetHeaderFromXlsx(InputStream inputStream) {
        //定义储存表头的数字
        List<List<String>> allSheetHeader = new ArrayList<>();
        //创建工作簿
        XSSFWorkbook hb;
        try {
            hb = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.EXCEL_COVER_EXCEPTION);
        }
        //遍历sheet页
        for (int i = 0; i < hb.getNumberOfSheets(); i++) {
            //获取sheet表
            XSSFSheet xh = hb.getSheetAt(i);
            //获取第一行数据，即表头
            Row headerRow = xh.getRow(0);
            List<String> sheetHeader = new ArrayList<>();
            if (headerRow != null) {
                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                    Cell cell = headerRow.getCell(j);
                    if (cell != null) {
                        String cellValue = cell.getStringCellValue().trim();
                        if (!cellValue.isEmpty()) {
                            sheetHeader.add(cellValue);
                        }
                    }
                }
            }
            allSheetHeader.add(sheetHeader);
        }

        return allSheetHeader;
    }


    /**
     * 读取一个csv文件里所有的表头
     *
     * @param inputStream 文件输入流
     * @return java.util.List<java.util.List < java.lang.String>>
     * @author zrx
     **/
    public static List<List<String>> getSheetHeaderFromCsv(InputStream inputStream) {
        //定义储存表头的数字
        List<List<String>> csvAllSheetHeader = new ArrayList<>();
        //读取csv文件
        CsvReader csvReader;
        try {
            csvReader = new CsvReader(
                    new BufferedReader(
                            new InputStreamReader(inputStream, "utf-8")));
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(ErrorCode.EXCEL_COVER_EXCEPTION);
        }

        //获取表头
        List<String> sheetHeader = new ArrayList<>();

        try {
            if (csvReader.readRecord() ) {
                sheetHeader = Arrays.asList(csvReader.getValues());
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.EXCEL_READ_EXCEPTION);
        }
        //将获取的表头数据添加到数组
        csvAllSheetHeader.add(sheetHeader);

        return csvAllSheetHeader;
    }


    /**
     * 检查Excel表头是否正确
     *
     * @param files 文件
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    public static BaseResponse checkExcelSheetHeader(MultipartFile[] files) {

//        定义储存所有表头的数组
        List<List<String>> allHeader = new ArrayList<>();
//        检查文件数据是否为空
        if (files == null || files[0].isEmpty()) {
            return ResultUtil.error(ErrorCode.EXCEL_FILE_IS_NULL);
        }

//        遍历所有Excel文件,获取所有表头
        for (MultipartFile multipartFile : files) {

            try {
                InputStream inputStream = multipartFile.getInputStream();
                String fileName = multipartFile.getOriginalFilename();
                List<List<String>> sheetHeaders;

                // 判断文件类型，调用相应的方法获取表头
                if (fileName != null && fileName.toLowerCase().endsWith(".xlsx")) {
                    sheetHeaders = getSheetHeaderFromXlsx(inputStream);
                } else if (fileName != null && fileName.toLowerCase().endsWith(".csv")) {
                    sheetHeaders = getSheetHeaderFromCsv(inputStream);
                } else {
                    throw new BusinessException(ErrorCode.FILE_FORMAT_NOT_SUPPORTED);
                }
                // 将获取到的表头添加到总表头数组中
                allHeader.addAll(sheetHeaders);
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.EXCEL_READ_EXCEPTION);
            }

        }
//      如果表头数据为空，返回错误
        if (allHeader.size() == 0) {
            return ResultUtil.error(ErrorCode.EXCEL_SHEET_HEADER_CONTENT_NULL);
        }
//      遍历所有表头,查看是否所有表头数据相同
        List<String> checkHeader = allHeader.get(0);
        for (int i = 0; i < allHeader.size(); i++) {
            List<String> currentHeader = allHeader.get(i);
            if (!checkHeader.equals(currentHeader)) {
                return ResultUtil.error(ErrorCode.EXCEL_SHEET_HEADER_INCONSISTENT,
                        "第 " + (i + 1) + " 个工作表的表头与标准不一致。" +
                                "\n标准表头：" + checkHeader +
                                "\n实际表头：" + currentHeader.toString());
            }
        }

        return ResultUtil.success(checkHeader);
    }


    /**
     * 检验Excel表格的数据格式
     *
     * @param files       文件
     * @param sheetHeader 表头
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    public static BaseResponse checkExcelSheetData(MultipartFile[] files, List<String> sheetHeader) {
        // 定义存放错误信息的数组
        List<String> errorMessages = new ArrayList<>();
        // 定义储存文件列名类型的数组
        List<String> headerType = new ArrayList<>();

        // 检查文件数据是否为空
        if (files == null || files.length == 0 || files[0].isEmpty()) {
            return ResultUtil.error(ErrorCode.EXCEL_FILE_IS_NULL);
        }

        List<List<String>> allSheetData = new ArrayList<>();

        // 遍历所有Excel文件，获取所有列数据
        for (MultipartFile multipartFile : files) {
            try {
                InputStream inputStream = multipartFile.getInputStream();
                String fileName = multipartFile.getOriginalFilename();

                List<List<String>> sheetData;

                // 判断文件类型，调用相应的方法获取数据
                if (fileName != null && fileName.toLowerCase().endsWith(".xlsx")) {
                    sheetData = getSheetDataFromXlsx(inputStream, sheetHeader, false);
                } else if (fileName != null && fileName.toLowerCase().endsWith(".csv")) {
                    sheetData = getSheetDataFromCsv(inputStream, false);
                } else {
                    throw new BusinessException(ErrorCode.FILE_FORMAT_NOT_SUPPORTED);
                }

                if (!sheetData.isEmpty() || sheetData.size() != 0) {
                    allSheetData.addAll(sheetData);
                }

            } catch (IOException e) {
                throw new BusinessException(ErrorCode.EXCEL_READ_EXCEPTION);
            }
        }

        if (allSheetData.isEmpty() || allSheetData.get(0).isEmpty()) {
            return ResultUtil.error(ErrorCode.EXCEL_DATA_NULL);
        }

        // 检验数据格式,检验每个列下的数据内容是否相同
        for (int i = 0; i < sheetHeader.size(); i++) {
            String columnHeader = sheetHeader.get(i);
            List<String> columnData = getColumnData(allSheetData, i);

            if (columnData.isEmpty()) {
                errorMessages.add("在表头为'" + columnHeader + "'的列下没有数据");
            } else {
                if (!checkColumnDataConsistency(columnData)) {
                    errorMessages.add("表头为'" + columnHeader + "'的列下数据不一致");
                } else {
                    headerType.add(detectDataType(columnData.get(0)));
                }
            }
        }

        if (!errorMessages.isEmpty()) {
            return ResultUtil.error(ErrorCode.EXCEL_SHEET_DATA_FORMAT_ERROR, errorMessages.toString());
        }

        return ResultUtil.success("数据格式检验通过", headerType);
    }

    /**
     * 从xlsx文件中获取指定表头的数据
     *
     * @param inputStream 输入流
     * @param sheetHeader 表头信息
     * @return List<List < String>> 数据
     **/
    private static List<List<String>> getSheetDataFromXlsx(InputStream inputStream, List<String> sheetHeader, boolean isGetAll) {
        List<List<String>> sheetData = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                XSSFSheet sheet = workbook.getSheetAt(i);

                // 遍历行，获取数据类型
                //设置获取数据的行数
                int maxRowNumber;
                if (!isGetAll) {
                    maxRowNumber = Math.min(sheet.getPhysicalNumberOfRows() - 1, 10);
                } else {
                    maxRowNumber = sheet.getLastRowNum() - 1;
                }
                for (int j = 1; j <= maxRowNumber; j++) {
                    Row row = sheet.getRow(j);
                    List<String> rowData = new ArrayList<>();
                    for (int k = 0; k < sheetHeader.size(); k++) {
                        Cell cell = row.getCell(k);
                        if (!cell.getStringCellValue().equals("")) {
                            rowData.add(getCellValue(cell));
                        } else {
                            rowData.add(" ");
                        }
                    }
                    if (checkRowElementsIsEmpty(rowData)) {
                        continue;
                    }
                    sheetData.add(rowData);
                }
            }

        } catch (IOException e) {
            throw new BusinessException(ErrorCode.EXCEL_READ_EXCEPTION);
        }
        return sheetData;
    }

    /**
     * 根据指定表头从csv文件中获取数据
     *
     * @param inputStream 输入流
     * @return List<List < String>> 数据
     **/
    private static List<List<String>> getSheetDataFromCsv(InputStream inputStream,
                                                          boolean isGetAll) {
        List<List<String>> sheetData = new ArrayList<>();
        try {
            CsvReader csvReader = new CsvReader(new InputStreamReader(inputStream, "utf-8") );

            String[] nextRecord;
            int maxRowNumber = 0;
//            跳过第一行
            if (csvReader.readRecord()) {
                csvReader.getValues();
            }
//            获取数据(获取全部/前10行)
            if (isGetAll) {
                while (csvReader.readRecord()) {

                    List<String> rowData = Arrays.asList(csvReader.getValues());

                    sheetData.add(rowData);
                }
            } else {
                while (csvReader.readRecord() &&
                        maxRowNumber < 10) {
//                    获取一行数据
                    List<String> rowData = Arrays.asList(csvReader.getValues());
                    if (!checkRowElementsIsEmpty(rowData)) {
                        sheetData.add(rowData);
                    }
                    maxRowNumber++;
                }
            }

            csvReader.close();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.EXCEL_READ_EXCEPTION);
        }
        return sheetData;
    }

    /**
     * 获取指定列的数据
     *
     * @param sheetData   表格数据
     * @param columnIndex 列索引
     * @return List<String> 指定列的数据
     **/
    private static List<String> getColumnData(List<List<String>> sheetData, int columnIndex) {
        List<String> columnData = new ArrayList<>();
        for (List<String> rowData : sheetData) {
            if (rowData.size() > columnIndex) {
                columnData.add(rowData.get(columnIndex));
            } else {
                columnData.add("");
            }
        }
        return columnData;
    }


    /**
     * 检验列数据格式是否一致
     *
     * @param columnData 列数据
     * @return boolean 是否一致
     **/
    private static boolean checkColumnDataConsistency(List<String> columnData) {
        if (columnData.size() < 2) {
            return true;
        }
        String firstValueType = detectDataType(columnData.get(0));
        for (int i = 1; i < columnData.size(); i++) {
            if (!firstValueType.equals(detectDataType(columnData.get(i)))) {
                return false;
            }
        }
        return true;
    }


    /**
     * 获取单元格值数值
     *
     * @param cell 单元格对象
     * @return String 单元格类型
     **/
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case 1:
                return cell.getStringCellValue();
            case 0:
                if (DateUtil.isCellDateFormatted(cell)) {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return df.format(cell.getDateCellValue());
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case 4:
                return String.valueOf(cell.getBooleanCellValue());
            case 2:
                return cell.getCellFormula();
            default:
                return "";
        }
    }


    /**
     * 检查字符数据的类型
     *
     * @param data 要检测的数据
     * @return java.lang.String
     * @author zrx
     **/
    public static String detectDataType(String data) {
        if (data.matches("^-?\\d+$")) {
            return "Integer";
        } else if (data.matches("^-?\\d+\\.\\d+$")) {
            return "Float";
        } else if (data.matches("^\\d{10,}$")) {
            return "Timestamp";
        } else if (Pattern.matches("[a-fA-F0-9]+", data)) {
            return "Hexadecimal";
        } else if (Pattern.matches("0[0-7]+", data)) {
            return "Octal";
        } else if (Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", data)) {
            return "Date";
        } else if (Pattern.matches("^1[3456789]\\d{9}$", data)) {
            return "Phone Number";
        } else if (Pattern.matches("^\\w+@\\w+\\.\\w+$", data)) {
            return "Email";
        } else {
            return "String";
        }
    }


    /**
     * 获取多个文件的全部数据（建议在校验后调用）
     *
     * @param files       Excel文件
     * @param sheetHeader 表头
     * @return java.util.List<java.util.List < java.lang.String>>
     * @author zrx
     **/
    public static List<List<String>> getAllExcelDataByHeaderName(MultipartFile[] files,
                                                                 List<String> sheetHeader) {

        List<List<String>> sheetData = new ArrayList<>();

        // 遍历所有Excel文件，获取数据
        for (MultipartFile multipartFile : files) {
            try {
                InputStream inputStream = multipartFile.getInputStream();
                String fileName = multipartFile.getOriginalFilename();

                // 判断文件类型，调用相应的方法获取数据
                if (fileName != null && fileName.toLowerCase().endsWith(".xlsx")) {
                    sheetData.addAll(getSheetDataFromXlsx(inputStream, sheetHeader, true));
                } else if (fileName != null && fileName.toLowerCase().endsWith(".csv")) {
                    sheetData.addAll(getSheetDataFromCsv(inputStream, true));
                } else {
                    throw new BusinessException(ErrorCode.FILE_FORMAT_NOT_SUPPORTED);
                }

            } catch (IOException e) {
                throw new BusinessException(ErrorCode.EXCEL_READ_EXCEPTION);
            }
        }

        return sheetData;
    }


    /**
     * 检测行内所有单元格数据都不为空
     *
     * @param row 行数据
     * @return boolean
     * @author zrx
     **/
    public static boolean checkRowElementsIsEmpty(List<String> row) {
        for (String value : row) {
            if (!value.equals("")) {
                return false; // 只要发现有一个元素不为空字符串，就返回 false
            }
        }
        return true; // 如果循环结束都没有返回 false，说明所有元素都是空字符串
    }


}


