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

package com.blazebit.persistence.impl;

import com.blazebit.persistence.MiddleOngoingSetOperationCTECriteriaBuilder;
import com.blazebit.persistence.OngoingFinalSetOperationCTECriteriaBuilder;
import com.blazebit.persistence.OngoingSetOperationCTECriteriaBuilder;
import com.blazebit.persistence.StartOngoingSetOperationCTECriteriaBuilder;
import com.blazebit.persistence.parser.expression.ExpressionCopyContext;
import com.blazebit.persistence.spi.SetOperationType;

import java.util.Map;

/**
 *
 * @param <T> The query result type
 * @author Christian Beikov
 * @since 1.1.0
 */
public class OngoingSetOperationCTECriteriaBuilderImpl<T, Z extends AbstractCommonQueryBuilder<?, ?, ?, ?, ?>> extends AbstractCTECriteriaBuilder<T, OngoingSetOperationCTECriteriaBuilder<T, Z>, OngoingSetOperationCTECriteriaBuilder<T, Z>, StartOngoingSetOperationCTECriteriaBuilder<T, MiddleOngoingSetOperationCTECriteriaBuilder<T, Z>>> implements OngoingSetOperationCTECriteriaBuilder<T, Z> {

    protected final Z endSetResult;

    public OngoingSetOperationCTECriteriaBuilderImpl(MainQuery mainQuery, QueryContext queryContext, CTEManager.CTEKey cteKey, boolean inline, Class<Object> clazz, T result, CTEBuilderListener listener, OngoingFinalSetOperationCTECriteriaBuilderImpl<Object> finalSetOperationBuilder, Z endSetResult, AliasManager parentAliasManager, JoinManager parentJoinManager) {
        super(mainQuery, queryContext, cteKey, inline, clazz, result, listener, finalSetOperationBuilder, parentAliasManager, parentJoinManager);
        this.endSetResult = endSetResult;
    }

    public OngoingSetOperationCTECriteriaBuilderImpl(AbstractCTECriteriaBuilder<T, OngoingSetOperationCTECriteriaBuilder<T, Z>, OngoingSetOperationCTECriteriaBuilder<T, Z>, StartOngoingSetOperationCTECriteriaBuilder<T, MiddleOngoingSetOperationCTECriteriaBuilder<T, Z>>> builder, MainQuery mainQuery, QueryContext queryContext,
                                                     Map<JoinManager, JoinManager> joinManagerMapping, ExpressionCopyContext copyContext) {
        super(builder, mainQuery, queryContext, joinManagerMapping, copyContext);
        this.endSetResult = null;
    }

    @Override
    AbstractCommonQueryBuilder<Object, OngoingSetOperationCTECriteriaBuilder<T, Z>, OngoingSetOperationCTECriteriaBuilder<T, Z>, StartOngoingSetOperationCTECriteriaBuilder<T, MiddleOngoingSetOperationCTECriteriaBuilder<T, Z>>, BaseFinalSetOperationCTECriteriaBuilderImpl<Object, ?>> copy(QueryContext queryContext, Map<JoinManager, JoinManager> joinManagerMapping,
                                                                                                                                                                                                                                                                                                ExpressionCopyContext copyContext) {
        return new OngoingSetOperationCTECriteriaBuilderImpl<>(this, queryContext.getParent().mainQuery, queryContext, joinManagerMapping, copyContext);
    }

    @Override
    public OngoingSetOperationCTECriteriaBuilder<T, Z> from(Class<?> clazz) {
        return super.from(clazz);
    }

    @Override
    public OngoingSetOperationCTECriteriaBuilder<T, Z> from(Class<?> clazz, String alias) {
        return super.from(clazz, alias);
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && endSetResult.isEmpty();
    }

    @Override
    public Z endSet() {
        subListener.verifyBuilderEnded();
        this.setOperationEnded = true;
        // Only check the query if it's not empty
        if (!isEmpty()) {
            prepareAndCheck(null);
        }
        listener.onBuilderEnded(this);

        if (isEmpty() && finalSetOperationBuilder.isEmpty()) {
            finalSetOperationBuilder.setOperationManager.replaceOperand(this, endSetResult);
        }

        finalSetOperationBuilder.setOperationEnded = true;
        return endSetResult;
    }
    
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public OngoingFinalSetOperationCTECriteriaBuilder<Z> endSetWith() {
        subListener.verifyBuilderEnded();
        this.setOperationEnded = true;
        // Only check the query if it's not empty
        if (!isEmpty()) {
            prepareAndCheck(null);
        }
        listener.onBuilderEnded(this);
        return (OngoingFinalSetOperationCTECriteriaBuilder<Z>) (OngoingFinalSetOperationCTECriteriaBuilder) finalSetOperationBuilder;
    }

    @Override
    protected BaseFinalSetOperationCTECriteriaBuilderImpl<Object, ?> createFinalSetOperationBuilder(SetOperationType operator, boolean nested) {
        return createFinalSetOperationBuilder(operator, nested, true);
    }
    
    @Override
    protected OngoingSetOperationCTECriteriaBuilder<T, Z> createSetOperand(BaseFinalSetOperationCTECriteriaBuilderImpl<Object, ?> finalSetOperationBuilder) {
        subListener.verifyBuilderEnded();
        listener.onBuilderEnded(this);
        return createOngoing(finalSetOperationBuilder, endSetResult);
    }

    @Override
    protected StartOngoingSetOperationCTECriteriaBuilder<T, MiddleOngoingSetOperationCTECriteriaBuilder<T, Z>> createSubquerySetOperand(BaseFinalSetOperationCTECriteriaBuilderImpl<Object, ?> finalSetOperationBuilder, BaseFinalSetOperationCTECriteriaBuilderImpl<Object, ?> resultFinalSetOperationBuilder) {
        subListener.verifyBuilderEnded();
        listener.onBuilderEnded(this);
        OngoingSetOperationCTECriteriaBuilderImpl<T, Z> resultCb = createOngoing(resultFinalSetOperationBuilder, endSetResult);
        return (StartOngoingSetOperationCTECriteriaBuilder) createStartOngoing(finalSetOperationBuilder, resultCb);
    }

}
