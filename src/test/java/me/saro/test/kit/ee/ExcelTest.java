package me.saro.test.kit.ee;

import me.saro.kit.Lists;
import me.saro.kit.ee.excel.xlsx.Excel;
import me.saro.kit.ee.excel.xlsx.ExcelStyle;
import me.saro.kit.ee.excel.xlsx.style.BorderStyle;
import me.saro.kit.ee.excel.xlsx.style.Direction;
import me.saro.kit.ee.excel.xlsx.style.HorizontalAlignment;
import me.saro.kit.ee.excel.xlsx.style.VerticalAlignment;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Date;


public class ExcelTest {
    
    @Test
    public void test() throws Exception {
//        File file = new File("C:\\Users\\SARO\\Desktop\\11\\abc.xlsx");
//
//        var style = ExcelStyle.create()
//                .setAlignment(HorizontalAlignment.CENTER)
//                .setVerticalAlignment(VerticalAlignment.CENTER)
//                .setBorderStyle(Direction.ALL, BorderStyle.THIN)
//                .setFont("맑은 고딕", 10)
//                .setWrapText(true);
//
//        Excel.create()
//                .newSheet("시트1")
//                .move(0, 0)
//                .setValues(Lists.asList("1", "2", "3"), style)
//                .nextRow()
//                .setValues(Lists.asList(1, 2, 3))
//                .nextRow()
//                .setValues(Lists.asList("A", "B", "C", new Date()))
//
//                .save(file, true);

    }

}
/*
ToWorkBookStyle standardStyle = new ToWorkBookStyle();

			standardStyle.setFont(ToWorkBookFont.builder().fontName("맑은 고딕").fontHeightInPoints((short) 10).build());
			standardStyle.setWrapText(true);
 */
