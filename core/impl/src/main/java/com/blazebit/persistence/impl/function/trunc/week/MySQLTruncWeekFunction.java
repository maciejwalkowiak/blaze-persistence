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

package com.blazebit.persistence.impl.function.trunc.week;

/**
 * @author Jan-Willem Gmelig Meyling
 * @since 1.4.0
 */
public class MySQLTruncWeekFunction extends TruncWeekFunction {

    public MySQLTruncWeekFunction() {
        // Implementation from https://stackoverflow.com/a/32955740/2104280
        super("date_add('1900-01-01', interval TIMESTAMPDIFF(WEEK, '1900-01-01', ?1) WEEK)");
    }

}
