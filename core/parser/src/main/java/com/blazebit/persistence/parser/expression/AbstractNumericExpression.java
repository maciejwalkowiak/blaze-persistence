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

package com.blazebit.persistence.parser.expression;

/**
 *
 * @author Moritz Becker
 * @since 1.2.0
 */
public abstract class AbstractNumericExpression extends AbstractExpression implements NumericExpression {

    private final NumericType numericType;

    public AbstractNumericExpression(NumericType numericType) {
        this.numericType = numericType;
    }

    @Override
    public NumericType getNumericType() {
        return numericType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractNumericExpression)) {
            return false;
        }

        AbstractNumericExpression that = (AbstractNumericExpression) o;

        return numericType == that.numericType;

    }

    @Override
    public int hashCode() {
        return numericType != null ? numericType.hashCode() : 0;
    }

    @Override
    public abstract Expression copy(ExpressionCopyContext copyContext);
}
