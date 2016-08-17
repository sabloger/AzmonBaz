package com.kokabi.p.azmonbaz.Components;

import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by R.Miri on 7/27/2016.
 */

public class CustomLinkedMap<K, V> extends LinkedHashMap<K, V> {

    public V getValueByIndex(int i) {
        Entry<K, V> entry = this.getEntryByIndex(i);
        if (entry == null) return null;
        return entry.getValue();
    }

    public K getKeyByIndex(int i) {
        Entry<K, V> entry = this.getEntryByIndex(i);
        if (entry == null) return null;
        return entry.getKey();
    }

    public Entry<K, V> getEntryByIndex(int i) {
        Set<Entry<K, V>> entries = entrySet();
        int j = 0;
        for (Entry<K, V> entry : entries)
            if (j++ == i) return entry;
        return null;
    }

}

