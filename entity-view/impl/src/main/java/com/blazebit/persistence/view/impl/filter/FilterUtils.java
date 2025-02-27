/*
 * Copyright 2014 - 2023 Blazebit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blazebit.persistence.view.impl.filter;

import java.io.Serializable;
import java.text.ParseException;

import com.blazebit.text.FormatUtils;

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public final class FilterUtils {

    private FilterUtils() {
    }

    @SuppressWarnings("unchecked")
    public static Object parseValue(Class<?> clazz, Object value) {
        try {
            return FormatUtils.getParsedValue((Class<? extends Serializable>) clazz, value.toString());
        } catch (ParseException ex) {
            throw new IllegalArgumentException("The given value '" + value + "' could not be parsed into an object of type '" + clazz.getName() + "'", ex);
        }
    }
}
