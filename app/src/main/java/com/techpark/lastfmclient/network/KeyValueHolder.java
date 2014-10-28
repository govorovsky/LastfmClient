package com.techpark.lastfmclient.network;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by andrew on 28.10.14.
 */
public class KeyValueHolder implements Iterable<KeyValueHolder.Holder> {

    private LinkedList<Holder> keyValues = new LinkedList<>();


    @Override
    public Iterator<Holder> iterator() {
        return keyValues.iterator();
    }

    public static class Holder implements Comparable<Holder> {
        private final String key;
        private final String val;

        public Holder(String key, String val) {
            this.key = key;
            this.val = val;
        }

        public String getKey() {
            return key;
        }

        public String getVal() {
            return val;
        }

        @Override
        public int compareTo(Holder another) {
            return key.compareTo(another.key);
        }

        @Override
        public String toString() {
            return "Param{" +
                    "key='" + key + '\'' +
                    ", val='" + val + '\'' +
                    '}';
        }
    }


    public KeyValueHolder add(String key, String val) {
        keyValues.add(new Holder(key, val));
        return this;
    }

    public KeyValueHolder add(Holder header) {
        keyValues.add(header);
        return this;
    }

    public void sortByKey() { // this need to construct signature of a call
        Collections.sort(keyValues);
    }

    @Override
    public String toString() {
        return "KeyValueHolder{" +
                "keyValues=" + keyValues +
                '}';
    }


    public List<Holder> getList() {
        return keyValues;
    }
}
