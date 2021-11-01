package me.saro.kit.ee.excel.xlsx

import org.apache.poi.xssf.streaming.SXSSFCell
import org.apache.poi.xssf.streaming.SXSSFRow
import org.apache.poi.xssf.streaming.SXSSFSheet

class ExcelCheck {
    companion object {
        fun sheet(sheet: SXSSFSheet?) =
            sheet ?: throw IllegalStateException("sheet has not created/selected")

        fun row(row: SXSSFRow?) =
            row ?: throw IllegalStateException("row has not created/selected")

        fun cell(cell: SXSSFCell?) =
            cell ?: throw IllegalStateException("cell has not created/selected")
    }
}