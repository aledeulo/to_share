package com.test.musala.support;


import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class SupportTools implements Serializable {

    private static final String IPV4_PATTERN = "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$";
    private static final Pattern IP_PATTERN = Pattern.compile(IPV4_PATTERN);
    private static final String NUMBER_PATTERN = "[0-9]*";
    private static final Pattern NUM_PATTERN = Pattern.compile(NUMBER_PATTERN);
    private static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}";
    private static final Pattern DATE_MATCHER = Pattern.compile(DATE_PATTERN);

    public static final Predicate<String> dateValidator = x -> DATE_MATCHER.matcher(x).matches();

    public static boolean validateIP4Address(String ip) {
        return IP_PATTERN.matcher(ip).find();
    }

    public static boolean isANumber(String id) {
        return NUM_PATTERN.matcher(id).matches();
    }

    public static void removeStringFromArray(List<Long> list, long id) {
        for (int index = 0; index < list.size(); index++) {
            if (list.get(index) == id) {
                list.remove(list.get(index));
                break;
            }
        }
    }

    public static String toHash(String data) throws NoSuchAlgorithmException {
        /* Check the validity of data */
        if (data == null || data.length() <= 0)
            throw new IllegalArgumentException("Null value provided for MD5 Encoding");
        final MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(data.getBytes(), 0, data.length());
        return new BigInteger(1, m.digest()).toString(16);
    }
}
