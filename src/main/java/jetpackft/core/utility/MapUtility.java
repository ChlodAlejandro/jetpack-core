package jetpackft.core.utility;

import java.util.HashMap;
import java.util.Map;

public class MapUtility{

    public static <K, V> Map<V, K> reverseKeys(Map<K, V> map) {
        Map<V, K> newMap = new HashMap<>();

        for (Map.Entry<K, V> entry : map.entrySet()) {
            newMap.put(entry.getValue(), entry.getKey());
        }

        return newMap;
    }

}
