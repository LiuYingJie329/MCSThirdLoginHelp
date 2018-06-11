package com.umeng.soexample.share.utils;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by ly on 2018/5/31.
 */

public class SerializableMap implements Serializable {
    private Map<String,String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
