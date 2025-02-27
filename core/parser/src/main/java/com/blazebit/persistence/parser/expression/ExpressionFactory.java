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

import com.blazebit.persistence.parser.predicate.Predicate;

import java.util.List;
import java.util.Set;

/**
 *
 * @author Christian Beikov
 * @author Moritz Becker
 * @since 1.0.0
 */
public interface ExpressionFactory {

    public <T extends ExpressionFactory> T unwrap(Class<T> clazz);

    public MacroConfiguration getDefaultMacroConfiguration();

    public Expression createJoinPathExpression(String expression);

    public Expression createJoinPathExpression(String expression, MacroConfiguration macroConfiguration, Set<String> usedMacros);

    public PathExpression createPathExpression(String expression);

    public Expression createPathExpression(String expression, MacroConfiguration macroConfiguration, Set<String> usedMacros);

    public Expression createSimpleExpression(String expression);

    public Expression createSimpleExpression(String expression, boolean allowQuantifiedPredicates);

    public Expression createSimpleExpression(String expression, boolean allowOuter, boolean allowQuantifiedPredicates);

    public Expression createSimpleExpression(String expression, boolean allowOuter, boolean allowQuantifiedPredicates, boolean allowObjectExpression);

    public Expression createSimpleExpression(String expression, boolean allowOuter, boolean allowQuantifiedPredicates, boolean allowObjectExpression, MacroConfiguration macroConfiguration, Set<String> usedMacros);

    public List<Expression> createInItemExpressions(String[] parameterOrLiteralExpressions);

    public List<Expression> createInItemExpressions(String[] parameterOrLiteralExpressions, MacroConfiguration macroConfiguration, Set<String> usedMacros);

    public Expression createInItemExpression(String parameterOrLiteralExpression);

    public Expression createInItemExpression(String parameterOrLiteralExpression, MacroConfiguration macroConfiguration, Set<String> usedMacros);

    public Expression createInItemOrPathExpression(String parameterOrLiteralExpression);

    public Expression createInItemOrPathExpression(String parameterOrLiteralExpression, MacroConfiguration macroConfiguration, Set<String> usedMacros);

    public Predicate createBooleanExpression(String expression, boolean allowQuantifiedPredicates);

    public Predicate createBooleanExpression(String expression, boolean allowQuantifiedPredicates, MacroConfiguration macroConfiguration, Set<String> usedMacros);
}
