package kit.model;

import me.saro.kit.bytes.fixed.annotations.DateData;
import me.saro.kit.bytes.fixed.annotations.DateDataType;
import me.saro.kit.bytes.fixed.annotations.FixedDataClass;
import me.saro.kit.dates.DateFormat;

import java.util.Calendar;
import java.util.Date;

@FixedDataClass(size=20)
public class DateStruct {

    @DateData(offset=0, type= DateDataType.unix4)
    Date date;

    @DateData(offset=4, type=DateDataType.unix8)
    Calendar calendar;

    @DateData(offset=12, type=DateDataType.millis8)
    DateFormat dateFormat;

    public DateStruct() {
    }

    public DateStruct(Date date, Calendar calendar, DateFormat dateFormat) {
        this.date = date;
        this.calendar = calendar;
        this.dateFormat = dateFormat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public String toString() {
        return "DateStruct{" +
                "date=" + date +
                ", calendar=" + calendar +
                ", dateFormat=" + dateFormat +
                '}';
    }
}