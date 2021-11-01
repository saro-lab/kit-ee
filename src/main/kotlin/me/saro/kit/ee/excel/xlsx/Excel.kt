package me.saro.kit.ee.excel.xlsx

import org.apache.poi.ss.usermodel.RichTextString
import org.apache.poi.xssf.streaming.SXSSFCell
import org.apache.poi.xssf.streaming.SXSSFRow
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

class Excel(
    private val workbook: SXSSFWorkbook,
    private var sheet: SXSSFSheet? = null,
    private var row: SXSSFRow? = null,
    private var cell: SXSSFCell? = null
) {
    companion object {
        @JvmStatic
        fun create(fileInputStream: FileInputStream): Excel =
            Excel(SXSSFWorkbook(XSSFWorkbook(fileInputStream)))

        @JvmStatic
        fun create(): Excel =
            Excel(SXSSFWorkbook())
    }

    fun createSheet(sheetName: String): Excel {
        sheet = workbook.createSheet(sheetName)
        return this
    }

    fun getSheet(sheetName: String): Excel {
        sheet = workbook.getSheet(sheetName)
        return this
    }

    fun getSheetAt(index: Int): Excel {
        sheet = workbook.getSheetAt(index)
        return this
    }

    fun executeWithRevertCursor(execute: Runnable): Excel {
        val tRow = row
        val tCell = cell
        execute.run()
        row = tRow
        cell = tCell
        return this
    }

    fun moveRow(rowIndex: Int): Excel {
        ExcelCheck.sheet(sheet)
        if (row == null || row!!.rowNum != rowIndex) {
            row = sheet!!.getRow(rowIndex)
                ?: sheet!!.createRow(rowIndex)
            if (cell != null) {
                moveCell(cell!!.columnIndex)
            }
        }
        return this
    }

    fun nextRow(): Excel =
        moveRow((row?.rowNum ?: -1) + 1)

    fun moveCell(columnIndex: Int): Excel {
        ExcelCheck.sheet(sheet)
        if (row == null) {
            moveRow(0)
        }
        cell = row!!.getCell(columnIndex)
            ?: row!!.createCell(columnIndex)
        return this
    }

    fun nextCell(): Excel =
        moveCell((cell?.columnIndex ?: -1) + 1)

    fun move(rowIndex: Int, columnIndex: Int): Excel =
        moveRow(rowIndex).moveCell(columnIndex)

    fun setValue(value: Any?): Excel {
        ExcelCheck.cell(cell)
        if (value != null) {
            when {
                (value is Number) -> cell!!.setCellValue(value.toDouble())
                (value is Boolean) -> cell!!.setCellValue(value)
                (value is RichTextString) -> cell!!.setCellValue(value)
                (value is Date) -> cell!!.setCellValue(value)
                (value is Calendar) -> cell!!.setCellValue(value)
                else -> cell!!.setCellValue(value.toString())
            }
        } else {
            cell!!.setCellValue("")
        }
        return this
    }

    fun setStyle(excelStyle: ExcelStyle): Excel {
        ExcelCheck.cell(cell)
        cell!!.cellStyle = excelStyle.bind(workbook.createCellStyle())
        return this
    }

    fun setStyleRange(excelStyle: ExcelStyle, toX: Int, toY: Int): Excel {
        ExcelCheck.cell(cell)
        return executeWithRevertCursor {
            val sRow = cell!!.rowIndex
            val sColumn = cell!!.columnIndex
            val style = excelStyle.bind(workbook.createCellStyle())
            for (y in 0 until toY) {
                moveRow(sRow + y)
                for (x in 0 until toX) {
                    moveCell(sColumn + x)
                    cell!!.cellStyle = style
                }
            }
        }
    }

    fun setValues(list: List<Any?>, excelStyle: ExcelStyle?): Excel {
        return executeWithRevertCursor {
            val style = excelStyle?.bind(workbook.createCellStyle())
            for (x in list.indices) {
                if (x != 0) {
                    nextCell()
                }
                setValue(list[x])
                if (style != null) {
                    cell!!.cellStyle = style
                }
            }
        }
    }

    fun setValues(list: List<Any?>): Excel =
        setValues(list, null)

    fun setValuesVertical(list: List<Any?>, excelStyle: ExcelStyle?): Excel {
        return executeWithRevertCursor {
            val style = excelStyle?.bind(workbook.createCellStyle())
            for (y in list.indices) {
                if (y != 0) {
                    nextRow()
                }
                setValue(list[y])
                if (style != null) {
                    cell!!.cellStyle = style
                }
            }
        }
    }

    fun setValuesVertical(list: List<Any?>): Excel =
        setValuesVertical(list, null)

    fun setWidth(width: Int, columnIndex: Int): Excel {
        ExcelCheck.sheet(sheet)
        sheet!!.setColumnWidth(columnIndex, width)
        return this
    }

    fun setWidths(width: Int, columnIndex: Int, toX: Int): Excel {
        ExcelCheck.sheet(sheet)
        for (i in 0 until toX) {
            sheet!!.setColumnWidth(columnIndex + i, width)
        }
        return this
    }

    fun save(outputStream: OutputStream) {
        workbook.write(outputStream)
    }

    fun save(file: File, override: Boolean) {
        if (override) {
            if (file.exists()) {
                file.delete()
            }
        } else {
            if (file.exists()) {
                throw FileAlreadyExistsException(file)
            }
        }
        workbook.write(FileOutputStream(file))
    }

    fun save(file: File) {
        workbook.write(FileOutputStream(file))
    }
}