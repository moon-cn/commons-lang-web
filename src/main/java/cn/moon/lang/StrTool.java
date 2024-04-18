package cn.moon.lang;



import java.util.List;
import java.util.UUID;

public class StrTool {

    private static final String CHINESE_REGEX = ".*[\\u4e00-\\u9fa5].*";



    /**
     * 时间区间 逗号分割 ,2022-10-11
     *
     * @return
     */
    public static Between parseBetween(String date) {
        Between between = new Between();
        if (date != null && !date.isEmpty()) {
            String[] s = date.split(",");
            if (s.length >= 1) {
                between.setBegin(s[0]);
            }
            if (s.length >= 2) {
                between.setEnd(s[1]);
            }
        }
        return between;
    }

    public static boolean isChinese(String str) {
        if (str == null) {
            return false;
        }


        return str.contains("〇") || str.matches(CHINESE_REGEX);
    }


    public static boolean anyContains(List<String> list, String search) {
        for (String str : list) {
            if (str.contains(search)) {
                return true;
            }
        }

        return false;
    }

    public static boolean anyContains(List<String> list, String... search) {
        for (String str : list) {
            for(String searchItem: search){
                if (str.contains(searchItem)) {
                    return true;
                }
            }

        }

        return false;

    }



    public static String removePreAndLowerFirst(String str, String prefix) {
        str = removePrefix(str,prefix);
        return str.substring(0,1).toLowerCase() + str.substring(1);
    }

    public static String removePrefix(String str, String prefix) {
        if (isEmpty(str) || isEmpty(prefix) || !str.startsWith(prefix)) {
            return str;
        }

        return str.substring(prefix.length());
    }


    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-","");
    }


    public static boolean isNotBlank(String str) {
        return str != null && !str.isEmpty() && !str.trim().isEmpty();
    }
}
