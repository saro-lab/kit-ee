package kit;

import me.saro.kit.bytes.Bytes;
import me.saro.kit.bytes.fixed.FixedData;
import me.saro.kit.dates.DateFormat;
import me.saro.kit.model.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FixedDataTest {

    @Test
    public void binary() {
        FixedData fd = FixedData.getInstance(BinaryStruct.class);
        
        BinaryStruct bs = new BinaryStruct((byte)-1, (short)321, 1234, 76543L, 2.1F, 3.6D, new byte[] {0x1f, 0x3b, 0x33});
        
        byte[] bytes = fd.toBytes(bs);
        
        assertEquals(bytes.length, 30);
        
        System.out.println(Bytes.toHex(bytes));
        assertEquals(Bytes.toHex(bytes), "ff0141000004d20000000000012aff40066666400ccccccccccccd1f3b33");
        
        assertEquals(bs, fd.toClass(bytes));
        
        byte[] bytes2 = new byte[60];
        fd.bindBytes(bs, bytes2, 0);
        fd.bindBytes(bs, bytes2, 30);
        
        System.out.println(Bytes.toHex(bytes2));
        assertEquals(Bytes.toHex(bytes2), "ff0141000004d20000000000012aff40066666400ccccccccccccd1f3b33ff0141000004d20000000000012aff40066666400ccccccccccccd1f3b33");
        
        assertEquals(bs, fd.toClass(bytes2, 30));
    }
    
    
    @Test
    public void text() {
        FixedData fd = FixedData.getInstance(TextStruct.class);
        
        TextStruct ts = new TextStruct((byte)-1/* -1 == 255 */, (short)-321, 32123, -21L, 12.3F, -342.5D, "가나다", "abc");
        
        byte[] bytes = fd.toBytes(ts);
        assertEquals(bytes.length, 100);
        
        String text = Bytes.toString(bytes, "UTF-8");
        System.out.println(text);
        assertEquals(text, "255-321   0000007d7b-21                 12.3                -342.5              가나다        abc");
        
        TextStruct ts2 = fd.toClass(Texts.getBytes(text,"UTF-8"));
        System.out.println(ts2);
        assertEquals(ts, ts2);
    }
    
    
    @Test
    public void mixed() {
        FixedData fd = FixedData.getInstance(MixedStruct.class);
        MixedStruct ms = new MixedStruct("Yong Seo", "PARK", 1);
        
        byte[] bytes = fd.toBytes(ms);
        
        assertEquals(bytes.length, 34);
        
        System.out.println(Bytes.toHex(bytes));
        assertEquals(Bytes.toHex(bytes), "596f6e672053656f202020202020205041524b202020202020202020202000000001");
        
        assertEquals(ms, fd.toClass(bytes, 0));
        
        System.out.println(ms);
        System.out.println(fd.<FixedData>toClass(bytes, 0));
    }
    
    @Test
    public void mutipleThreadStored() {
        Threads.executeAllThreads(5, IntStream.range(0, 10).boxed().collect(Collectors.toList()), i -> {
           System.out.println("exec : " + i);
           array();
           return i;
        });
    }
    
    @Test
    public void array() {
        FixedData format = FixedData.getInstance(ArrayStruct.class);
        ArrayStruct ms = new ArrayStruct(1, new int[] {2,3,4,5}, Arrays.asList(1L, -2L), new Short[] {21, 72});
        
        byte[] bytes = format.toBytes(ms);
        
        assertEquals(bytes.length, 40);
        
        System.out.println(Bytes.toHex(bytes));
        
        assertEquals(Bytes.toHex(bytes), "00000001000000020000000300000004000000050000000000000001fffffffffffffffe00150048");
        
        assertEquals(ms, format.toClass(bytes, 0));
        
        System.out.println(ms);
        System.out.println(format.<ArrayStruct>toClass(bytes, 0));
    }
    
    @Test
    public void inClass() {
        
        FixedData fd = FixedData.getInstance(ParentStruct.class);
        ParentStruct ms = new ParentStruct(
                new ChildStruct(2, "단일"), 
                Arrays.asList(new ChildStruct(32, "배열1"), new ChildStruct(1, "배열2")),
                new ChildStruct[] {new ChildStruct(32, "List3"), new ChildStruct(111, "List 4")},
                -1);
        
        byte[] bytes = fd.toBytes(ms);
        
        assertEquals(bytes.length, 74);
        
        System.out.println(Bytes.toHex(bytes));
        
        assertEquals(Bytes.toHex(bytes), "00000002eb8ba8ec9dbc2020202000000020ebb0b0ec97b43120202000000001ebb0b0ec97b432202020000000204c6973743320202020200000006f4c697374203420202020ffffffff");
        
        assertEquals(ms, fd.toClass(bytes, 0));
        
        System.out.println(ms);
        System.out.println(fd.<ParentStruct>toClass(bytes, 0));
    }
    
    @Test
    public void date() {
        
        FixedData fd = FixedData.getInstance(DateStruct.class);
        DateStruct ms = new DateStruct(new Date(1555956870000L), Calendar.getInstance(), DateFormat.now());
        
        byte[] bytes = fd.toBytes(ms);
        
        assertEquals(bytes.length, 20);
        
        System.out.println(Bytes.toHex(bytes));
        System.out.println(fd.toClass(bytes).toString());
        
        assertEquals(Bytes.toHex(fd.toBytes(ms)), Bytes.toHex(fd.toBytes(fd.toClass(bytes, 0))));
        
        System.out.println(ms);
        System.out.println(fd.<ParentStruct>toClass(bytes, 0));
    }
    
    @Test
    public void littleEndian() {
        
        FixedData fd = FixedData.getInstance(LittleStruct.class);
        LittleStruct ms = new LittleStruct(1, "가나다");
        
        byte[] bytes = fd.toBytes(ms);
        
        assertEquals(bytes.length, 14);
        
        System.out.println(Bytes.toHex(bytes));
        
        assertEquals(Bytes.toHex(fd.toBytes(ms)), Bytes.toHex(fd.toBytes(fd.toClass(bytes, 0))));
        
        System.out.println(ms);
        System.out.println(fd.<ParentStruct>toClass(bytes, 0));
    }
}

