/*
 * Copyright 2014 Blazebit.
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

package com.blazebit.persistence.impl.keyset;

import com.blazebit.persistence.Keyset;
import com.blazebit.persistence.KeysetPage;
import java.io.Serializable;

/**
 *
 * @author Christian Beikov
 * @since 1.0
 */
public class KeysetPaginationHelper {
    
    public static Serializable[] extractKey(Object[] tuple, int offset) {
        Serializable[] key = new Serializable[tuple.length - offset];
        System.arraycopy(tuple, offset, key, 0, key.length);
        return key;
    }
    
    public static KeysetMode getKeysetMode(KeysetPage keysetPage, int firstRow, int pageSize) {
        // a keyset must be given
        if (keysetPage == null) {
            return KeysetMode.NONE;
        }
        // The last page size must equal the current page size
        if (keysetPage.getMaxResults() != pageSize) {
            return KeysetMode.NONE;
        }

        int offset = keysetPage.getFirstResult() - firstRow;

        if (offset == pageSize) {
            // We went to the previous page
            if (isValidKey(keysetPage.getLowest())) {
                return KeysetMode.PREVIOUS;
            } else {
                return KeysetMode.NONE;
            }
        } else if (offset == -pageSize) {
            // We went to the next page
            if (isValidKey(keysetPage.getHighest())) {
                return KeysetMode.NEXT;
            } else {
                return KeysetMode.NONE;
            }
        } else if (offset == 0) {
            // Same page again
            if (isValidKey(keysetPage.getLowest())) {
                return KeysetMode.SAME;
            } else {
                return KeysetMode.NONE;
            }
        } else {
            // The last key set is away more than one page
            return KeysetMode.NONE;
        }
    }

    private static boolean isValidKey(Keyset keyset) {
        if (keyset == null) {
            return false;
        }
        
        Serializable[] key = keyset.getTuple();
        return key != null && key.length > 0;
    }
}