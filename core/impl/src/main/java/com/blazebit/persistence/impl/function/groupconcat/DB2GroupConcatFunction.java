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

package com.blazebit.persistence.impl.function.groupconcat;

import com.blazebit.persistence.impl.function.Order;
import com.blazebit.persistence.parser.util.TypeUtils;
import com.blazebit.persistence.spi.FunctionRenderContext;

import java.util.List;

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class DB2GroupConcatFunction extends AbstractGroupConcatFunction {

    public DB2GroupConcatFunction() {
        super("listagg(?1)");
    }

    @Override
    public void render(FunctionRenderContext context, GroupConcat groupConcat) {
        StringBuilder sb = new StringBuilder();

        if (groupConcat.isDistinct()) {
            sb.append("distinct ");
        }

        sb.append(groupConcat.getExpression());
        sb.append(", ");
        TypeUtils.STRING_CONVERTER.appendTo(groupConcat.getSeparator(), sb);

        List<Order> orderBys = groupConcat.getOrderBys();
        if (!orderBys.isEmpty()) {
            sb.append(") within group (order by ");
            
            render(sb, orderBys.get(0));
            
            for (int i = 1; i < orderBys.size(); i++) {
                sb.append(", ");
                render(sb, orderBys.get(i));
            }
        }

        renderer.start(context).addParameter(sb.toString()).build();
    }

    @Override
    protected void render(StringBuilder sb, Order order) {
        // NOTE: In listagg DB2 does not support the nulls clause at all 
        appendEmulatedOrderByElementWithNulls(sb, order);
    }
}
