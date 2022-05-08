package core.utils;

public class Test {
    /**
     * @param str
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str){
        if (str == null || str.trim().isEmpty())
            return false;
        else
            return true;
    }
}
