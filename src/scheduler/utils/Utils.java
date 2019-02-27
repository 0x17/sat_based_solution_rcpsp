package scheduler.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class Utils {
    public static ArrayList<Integer> intParts(String line) {
        return Utils.intParts(line, " ");
    }

    public static ArrayList<Integer> intParts(String line, String sep) {
        String[] parts = line.split(sep);
        ArrayList<Integer> res = new ArrayList<>();
        for (String part : parts)
            if(part.trim().length() > 0)
                res.add(Integer.parseInt(part));
        return res;
    }

    public static Integer[] intPartsArr(String line) {
        ArrayList<Integer> al = intParts(line);
        return al.toArray(new Integer[0]);
    }

    public static int extractInt(String line) {
        return Integer.parseInt(line.split(":")[1].trim());
    }

    public static int[] integerToIntArray(Integer[] values) {
        return Arrays.stream(values).mapToInt(i->i).toArray();
    }
}
