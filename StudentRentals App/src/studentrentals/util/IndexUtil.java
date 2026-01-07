package studentrentals.util;

import java.util.*;

public final class IndexUtil {
    private IndexUtil(){}

    public static void addToIndex(Map<String, List<String>> index, String key, String value) { // Method for adding to an index in the case of 
        List<String> list = index.get(key);
        if (list == null) { // First entry for this key (Homemowner / Student)
            list = new ArrayList<>();
            index.put(key, list);
        }
        list.add(value);
    }
}
