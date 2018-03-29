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
package com.baomidou.kisso.security.license;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.baomidou.kisso.exception.LicenseException;

/**
 * <p>
 * License 许可证
 * </p>
 *
 * @author hubin
 * @since 2018-03-28
 */
public class License {

    public static final String CODE = "code";
    public static final String SIGNATURE = "signature";
    public static final String EXPIRATION_DATE = "expirationDate";
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    private Properties features;

    public License(Properties features) throws LicenseException {
        this.features = features;
    }

    public Date getExpirationDate() {
        try {
            return DATE_FORMAT.parse(getFeature(EXPIRATION_DATE));
        } catch (ParseException e) {
            throw new LicenseException(e);
        }
    }

    public String getExpirationDateAsString() {
        return getFeature(EXPIRATION_DATE);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > getExpirationDate().getTime();
    }

    public int getDaysTillExpire() {
        return getNumberOfDays(new Date(), getExpirationDate());
    }

    public String getFeature(String name) {
        return features.getProperty(name);
    }

    public List<String> getFeatureNames() {
        List<String> featureNames = new ArrayList<>();
        Enumeration<?> keys = features.propertyNames();
        while (keys.hasMoreElements()) {
            featureNames.add((String) keys.nextElement());
        }
        return featureNames;
    }

    private int getNumberOfDays(Date first, Date second) {
        int compare = first.compareTo(second);
        if (compare > 0) {
            return 0;
        } else if (compare == 0) {
            return 1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(first);
        int firstDay = calendar.get(Calendar.DAY_OF_YEAR);
        int firstYear = calendar.get(Calendar.YEAR);
        int firstDays = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
        calendar.setTime(second);
        int secondDay = calendar.get(Calendar.DAY_OF_YEAR);
        int secondYear = calendar.get(Calendar.YEAR);
        int result = 0;
        // if dates in the same year
        if (firstYear == secondYear) {
            result = secondDay - firstDay + 1;
        } else {
            // days from the first year
            result += firstDays - firstDay + 1;
            // add days from all years between the two dates years
            for (int i = firstYear + 1; i < secondYear; i++) {
                calendar.set(i, 0, 0);
                result += calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
            }
            // days from last year
            result += secondDay;
        }
        return result;
    }

    @Override
    public String toString() {
        return features.toString();
    }

}
