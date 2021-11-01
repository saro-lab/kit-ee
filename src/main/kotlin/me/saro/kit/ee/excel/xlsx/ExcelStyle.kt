package me.saro.kit.ee.excel.xlsx

import me.saro.kit.ee.excel.xlsx.style.Direction
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.streaming.SXSSFCell
import org.apache.poi.xssf.usermodel.XSSFFont
import java.util.function.Consumer

class ExcelStyle(
    private val styles: MutableList<Consumer<CellStyle>>
) {
    companion object {
        @JvmStatic
        fun create(): ExcelStyle =
            ExcelStyle(ArrayList())
    }

    constructor(): this(ArrayList())

    fun apply(workbook: Workbook, cell: SXSSFCell) {
        val style = workbook.createCellStyle()
        styles.forEach { it.accept(style) }
        cell.cellStyle = style
    }

    fun set(style: Consumer<CellStyle>): ExcelStyle {
        styles.add(style)
        return this
    }

    fun setAlignment(horizontalAlignment: me.saro.kit.ee.excel.xlsx.style.HorizontalAlignment) =
        set { it.alignment = HorizontalAlignment.valueOf(horizontalAlignment.name) }

    fun setVerticalAlignment(verticalAlignment: me.saro.kit.ee.excel.xlsx.style.VerticalAlignment) =
        set { it.verticalAlignment = VerticalAlignment.valueOf(verticalAlignment.name) }

    fun setBorderStyle(direction: Direction, borderStyle: me.saro.kit.ee.excel.xlsx.style.BorderStyle) =
        set {
            when (direction) {
                Direction.TOP -> it.borderTop = BorderStyle.valueOf(borderStyle.name)
                Direction.RIGHT -> it.borderRight = BorderStyle.valueOf(borderStyle.name)
                Direction.BOTTOM -> it.borderBottom = BorderStyle.valueOf(borderStyle.name)
                Direction.LEFT -> it.borderLeft = BorderStyle.valueOf(borderStyle.name)
                Direction.ALL -> {
                    val style = BorderStyle.valueOf(borderStyle.name)
                    it.borderTop = style
                    it.borderRight = style
                    it.borderBottom = style
                    it.borderLeft = style
                }
            }
        }

    fun setBorderColor(direction: Direction, color: IndexedColors) = set {
        when (direction) {
            Direction.TOP -> it.topBorderColor = color.index
            Direction.RIGHT -> it.rightBorderColor = color.index
            Direction.BOTTOM -> it.bottomBorderColor = color.index
            Direction.LEFT -> it.leftBorderColor = color.index
            Direction.ALL -> {
                val ci = color.index
                it.topBorderColor = ci
                it.rightBorderColor = ci
                it.bottomBorderColor = ci
                it.leftBorderColor = ci
            }
        }
    }

    fun setDataFormat(format: Short) =
        set { it.dataFormat = format }

    fun setDataFormat(format: String) =
        set { it.dataFormat = BuiltinFormats.getBuiltinFormat(format).toShort() }

    fun setFillForegroundColor(color: ExtendedColor) =
        set { it.fillForegroundColor = color.index }

    fun setFillBackgroundColor(color: ExtendedColor) =
        set { it.fillBackgroundColor = color.index }

    fun setFillPattern(fillPatternType: me.saro.kit.ee.excel.xlsx.style.FillPatternType) =
        set { it.fillPattern = FillPatternType.valueOf(fillPatternType.name) }

    fun setFont(font: Font) =
        set { it.setFont(font) }

    fun setFont(name: String, point: Int) =
        set {
            val font = XSSFFont()
            font.fontName = name
            font.fontHeightInPoints = point.toShort()
            it.setFont(font)
        }

    fun setHidden(hidden: Boolean) =
        set { it.hidden = hidden }

    fun setLocked(locked: Boolean) =
        set { it.locked = locked }

    fun setWrapText(wrapText: Boolean) =
        set { it.wrapText = wrapText }

    fun setRotation(rotation: Short) =
        set { it.rotation = rotation }

    fun setIndention(indention: Short) =
        set { it.indention = indention }
}
