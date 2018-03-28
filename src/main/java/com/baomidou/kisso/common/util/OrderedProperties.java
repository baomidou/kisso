/*
 * Copyright (c) 2017-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.kisso.common.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * <p>
 * 有序的 Properties
 * </p>
 *
 * @author hubin
 * @since 2018-03-28
 */
public class OrderedProperties extends Properties {

    private final Map<Object, Object> linkedMap = new LinkedHashMap<>();

    @Override
    public Object get(Object key) {
        return linkedMap.get(key);
    }

    @Override
    public Object put(Object key, Object value) {
        return linkedMap.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return linkedMap.remove(key);
    }

    @Override
    public void clear() {
        linkedMap.clear();
    }

    @Override
    public Enumeration<Object> keys() {
        return Collections.enumeration(linkedMap.keySet());
    }

    @Override
    public Enumeration<Object> elements() {
        return Collections.enumeration(linkedMap.values());
    }

    @Override
    public Set<Map.Entry<Object, Object>> entrySet() {
        return linkedMap.entrySet();
    }

    @Override
    public int size() {
        return linkedMap.size();
    }

    @Override
    public String getProperty(String key) {
        return (String) linkedMap.get(key);
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        return linkedMap.containsKey(key);
    }

    @Override
    public String toString() {
        return linkedMap.toString();
    }

}
