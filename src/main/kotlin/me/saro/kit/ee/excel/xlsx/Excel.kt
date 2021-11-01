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
) {
    private var sheet: SXSSFSheet? = null
    private var row: SXSSFRow? = null
    private var cell: SXSSFCell? = null

    companion object {
        @JvmStatic
        fun create(fileInputStream: FileInputStream): Excel =
            Excel(SXSSFWorkbook(XSSFWorkbook(fileInputStream)))

        @JvmStatic
        fun create(): Excel =
            Excel(SXSSFWorkbook())
    }

    constructor(fileInputStream: FileInputStream): this(SXSSFWorkbook(XSSFWorkbook(fileInputStream)))

    constructor(): this(SXSSFWorkbook())

    fun newSheet(sheetName: String): Excel {
        sheet = workbook.createSheet(sheetName)
        return this
    }

    fun getSheet(sheetName: String): Excel {
        sheet = workbook.getSheet(sheetName)
        return this
    }

    fun moveRow(index: Int): Excel {
        if (sheet != null) {
            if (row == null || row!!.rowNum != index) {
                row = sheet!!.getRow(index)
                    ?: sheet!!.createRow(index)
            }
            if (cell != null) {
                cell = row!!.createCell(cell!!.columnIndex)
            }
        } else {
            throw IllegalStateException("sheet has not created/selected")
        }
        return this
    }

    fun nextRow(): Excel =
        moveRow((row?.rowNum ?: -1) + 1)

    fun moveCell(index: Int): Excel {
        if (sheet != null) {
            if (row == null) {
                moveRow(0)
            }
            if (cell == null || cell!!.columnIndex != index) {
                cell = row!!.getCell(index)
                    ?: row!!.createCell(index)
            }
        } else {
            throw IllegalStateException("sheet has not created/selected")
        }
        return this
    }

    fun nextCell(): Excel =
        moveCell((cell?.columnIndex ?: -1) + 1)

    fun move(rowIndex: Int, columnIndex: Int): Excel =
        moveRow(rowIndex).moveCell(columnIndex)

    fun setValue(value: Any?): Excel {
        if (cell != null) {
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
        } else {
            throw IllegalStateException("cell has not created/selected")
        }
        return this
    }

    fun setStyle(excelStyle: ExcelStyle): Excel {
        if (cell != null) {
            excelStyle.apply(workbook, cell!!)
        } else {
            throw IllegalStateException("cell has not created/selected")
        }
        return this
    }

    fun setStyleRange(excelStyle: ExcelStyle, toX: Int, toY: Int): Excel {
        if (cell != null) {
            val cell = this.cell!!
            val row = this.row
            for (x in 0 until toX) {
                for (y in 0 until toY) {
                    move(cell.rowIndex + y, cell.columnIndex)
                    setStyle(excelStyle)
                }
            }
            this.cell = cell
            this.row = row
        } else {
            throw IllegalStateException("cell has not created/selected")
        }
        return this
    }

    fun setValues(list: List<Any?>, excelStyle: ExcelStyle?): Excel {
        val cell = this.cell
        val row = this.row
        for (i in list.indices) {
            if (i != 0) {
                nextCell()
            }
            setValue(list[i])
            if (excelStyle != null) {
                setStyle(excelStyle)
            }
        }
        this.cell = cell
        this.row = row
        return this
    }

    fun setValues(list: List<Any?>): Excel =
        setValues(list, null)

    fun setValuesVertical(list: List<Any?>, excelStyle: ExcelStyle?): Excel {
        val cell = this.cell
        val row = this.row
        for (i in list.indices) {
            if (i != 0) {
                nextRow()
            }
            setValue(list[i])
            if (excelStyle != null) {
                setStyle(excelStyle)
            }
        }
        this.cell = cell
        this.row = row
        return this
    }

    fun setWidth(width: Int, columnIndex: Int): Excel {
        sheet!!.setColumnWidth(columnIndex, width)
        return this
    }

    fun setWidths(width: Int, columnIndex: Int, toX: Int): Excel {
        for (i in 0 until toX) {
            sheet!!.setColumnWidth(columnIndex, width + i)
        }
        return this
    }

    fun setValuesVertical(list: List<Any?>): Excel =
        setValuesVertical(list, null)

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