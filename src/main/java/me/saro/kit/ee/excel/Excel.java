//package me.saro.kit.ee.excel;
//
//import me.saro.kit.Files;
//import me.saro.kit.ThrowableConsumer;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.util.Date;
//import java.util.Spliterator;
//import java.util.Spliterators;
//import java.util.stream.Stream;
//import java.util.stream.StreamSupport;
//
//public class Excel {
//
//    public static void open(File file, ThrowableConsumer<Workbook> callback) throws Exception {
//        switch (Files.toFilenameExtension(file).toLowerCase()) {
//            case "xlsx":
//                try (XSSFWorkbook book = new XSSFWorkbook(file)) {
//                    callback.accept(book);
//                } catch (Exception e) {
//                    throw e;
//                }
//                break;
//            case "xls":
//                System.out.println(1);
//                try (Workbook book = WorkbookFactory.create(new FileInputStream(file))) {
//                    System.out.println(2);
//                    callback.accept(book);
//                } catch (Exception e) {
//                    throw e;
//                }
//                break;
//            default: throw new IllegalArgumentException("this lib support by .xls or .xlsx");
//        }
//    }
//
//    public static void rowStream(File file, int sheetIndex, ThrowableConsumer<Stream<Row>> callback) throws Exception {
//        open(file, book -> callback.accept(StreamSupport.stream(Spliterators.spliteratorUnknownSize(book.getSheetAt(sheetIndex).rowIterator(), Spliterator.ORDERED), false)));
//    }
//
//    public static double toDouble(Cell cell, double defaultValue) {
//        try {
//            return toDouble(cell);
//        } catch (Exception e) {
//            return defaultValue;
//        }
//    }
//
//    public static double toDouble(Cell cell) {
//        if (cell != null) {
//            String tmp;
//            switch (cell.getCellType()) {
//                case BOOLEAN:
//                    return cell.getBooleanCellValue() ? 1 : 0;
//                case NUMERIC:
//                    return cell.getNumericCellValue();
//                case FORMULA:
//                    if ((tmp = cell.getCellFormula()) != null && !tmp.isEmpty()) {
//                        return Double.parseDouble(tmp);
//                    }
//                case STRING:
//                    if ((tmp = cell.getStringCellValue()) != null && !tmp.isEmpty()) {
//                        return Double.parseDouble(tmp);
//                    }
//                case _NONE: case ERROR: case BLANK: default:
//            }
//        }
//        throw new IllegalArgumentException(cell + " is not number");
//    }
//
//    public static Date toDate(Cell cell, Date defaultValue) {
//        if (cell.getCellType() == CellType.STRING) {
//            try {
//                return cell.getDateCellValue();
//            } catch (Exception e) {}
//        }
//        return defaultValue;
//    }
//
//    public static String toString(Cell cell) {
//        if (cell != null) {
//            switch (cell.getCellType()) {
//                case BOOLEAN:
//                    return cell.getBooleanCellValue() ? "1" : "0";
//                case FORMULA:
//                    return cell.getCellFormula();
//                case NUMERIC:
//                    return Double.toString(cell.getNumericCellValue());
//                case STRING:
//                    return cell.getStringCellValue();
//                case _NONE: case ERROR: case BLANK: default:
//            }
//        }
//        return "";
//    }
//}
