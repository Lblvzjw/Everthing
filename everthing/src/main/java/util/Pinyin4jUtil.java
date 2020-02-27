package util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Creat with IntelliJ IDEA.
 * Description：
 * User:LiuBen
 * Date:2020-01-08
 * Time:19:21
 */
public class Pinyin4jUtil {

    private static final HanyuPinyinOutputFormat FORMAT = new HanyuPinyinOutputFormat();
    private static final String CHINESE_PATTERN = "[\\u4E00-\\u9FA5]";

    static{
        FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    //判断字符串是否包含中文
    public static boolean containsChinese(String pinyin){
        return pinyin.matches(".*" + CHINESE_PATTERN + ".*");
    }

    public static String[] getpinyin(String hanyu){
        String[] array = new String[2];

        StringBuilder firstpinyin = new StringBuilder();
        StringBuilder firstpinyin_firstletter = new StringBuilder();
        for (int i = 0; i < hanyu.length(); i++) {
            try{
                String[] pinyins = PinyinHelper.
                        toHanyuPinyinStringArray(hanyu.charAt(i),FORMAT);
                firstpinyin.append(pinyins[0]);
                firstpinyin_firstletter.append(pinyins[0].charAt(0));
            }catch (Exception e){
                firstpinyin.append(hanyu.charAt(i));
                firstpinyin_firstletter.append(hanyu.charAt(i));
            }
        }
        array[0] = firstpinyin.toString();
        array[1] = firstpinyin_firstletter.toString();
        return array;
    }

    public static String[][] getPinyinAndFirst(String hanyu,boolean fullspell){
        String[][] array = new String[hanyu.length()][];

        for (int i = 0; i < hanyu.length(); i++) {
            try{
                String[] pinyins = PinyinHelper.
                        toHanyuPinyinStringArray(hanyu.charAt(i),FORMAT);
                if(pinyins.length == 0 || pinyins == null){
                    array[i] = new String[]{String.valueOf(hanyu.charAt(i))};
                }else{
                    array[i] = deWeight(pinyins,fullspell);
                }
            }catch (Exception e){
                array[i] = new String[]{String.valueOf(hanyu.charAt(i))};
            }
        }
        return array;
    }

    private static String[] deWeight(String[] pinyins, boolean fullspell) {
        Set<String> set = new HashSet<>();

        for (String pinyin:pinyins) {
            if(fullspell){
                set.add(pinyin);
            }else{
                set.add(String.valueOf(pinyin.charAt(0)));
            }
        }
        return set.toArray(new String[set.size()]);
    }

    public static void main(String[] args) {
        /*
        String[] pinyins = getpinyin("1中华1a1人民1和");
        System.out.println(Arrays.toString(pinyins));
        System.out.println();

        String[][] pinyins1 = getPinyinAndFirst("1中华1a1人民1和",true);
        for (String[] strarray:pinyins1) {
            System.out.println(Arrays.toString(strarray));
        }
        System.out.println();

        String[][] pinyins2 = getPinyinAndFirst("1中华1a1人民1和",false);
        for (String[] strarray:pinyins2) {
            System.out.println(Arrays.toString(strarray));
        }
        */

        System.out.println(containsChinese("abcd"));
        System.out.println(containsChinese("a中cd1"));


    }

}
