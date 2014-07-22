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
package com.blazebit.persistence.impl;

import com.blazebit.persistence.JoinType;
import com.blazebit.persistence.ObjectBuilder;
import com.blazebit.persistence.PaginatedCriteriaBuilder;
import com.blazebit.persistence.QueryBuilder;
import com.blazebit.persistence.SelectObjectBuilder;
import java.lang.reflect.Constructor;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

/**
 *
 * @author ccbem
 */
public abstract class AbstractCriteriaBuilder<T, U extends QueryBuilder<T, U>> extends BaseQueryBuilderImpl<T, U> implements QueryBuilder<T, U> {

    /**
     * Create flat copy of builder
     *
     * @param builder
     */
    protected AbstractCriteriaBuilder(AbstractCriteriaBuilder<T, ? extends QueryBuilder<T, ?>> builder) {
        super(builder);
    }

    public AbstractCriteriaBuilder(EntityManager em, Class<T> clazz, String alias) {
        super(em, clazz, alias);
    }

    @Override
    public List<T> getResultList(EntityManager em) {
        return getQuery(em).getResultList();
    }

    @Override
    public PaginatedCriteriaBuilder<T> page(int firstRow, int pageSize) {
        return new PaginatedCriteriaBuilderImpl<T>(this, firstRow, pageSize);
    }

    @Override
    public U setParameter(String name, Object value) {
        parameterManager.satisfyParameter(name, value);
        return (U) this;
    }

    @Override
    public U setParameter(String name, Calendar value, TemporalType temporalType) {
        parameterManager.satisfyParameter(name, new ParameterManager.TemporalCalendarParameterWrapper(value, temporalType));
        return (U) this;
    }

    @Override
    public U setParameter(String name, Date value, TemporalType temporalType) {
        parameterManager.satisfyParameter(name, new ParameterManager.TemporalDateParameterWrapper(value, temporalType));
        return (U) this;
    }

    @Override
    public <Y> SelectObjectBuilder<? extends QueryBuilder<Y, ?>> selectNew(Class<Y> clazz) {
        verifyBuilderEnded();
        resultClazz = (Class<T>) clazz;
        return selectManager.selectNew(this, clazz);
    }

    @Override
    public <Y> SelectObjectBuilder<? extends QueryBuilder<Y, ?>> selectNew(Constructor<Y> constructor) {
        verifyBuilderEnded();
        resultClazz = (Class<T>) constructor.getDeclaringClass();
        return selectManager.selectNew(this, constructor);
    }

    @Override
    public <Y> QueryBuilder<Y, ?> selectNew(ObjectBuilder<Y> builder) {
        verifyBuilderEnded();
        selectManager.selectNew(builder);
        return (QueryBuilder<Y, ?>) this;
    }

    @Override
    public U innerJoinFetch(String path, String alias) {
        return join(path, alias, JoinType.INNER, true);
    }

    @Override
    public U leftJoinFetch(String path, String alias) {
        return join(path, alias, JoinType.LEFT, true);
    }

    @Override
    public U rightJoinFetch(String path, String alias) {
        return join(path, alias, JoinType.RIGHT, true);
    }

    @Override
    public U outerJoinFetch(String path, String alias) {
        return join(path, alias, JoinType.OUTER, true);
    }

    @Override
    public U join(String path, String alias, JoinType type, boolean fetch) {
        if (path == null || alias == null || type == null) {
            throw new NullPointerException();
        }
        if (alias.isEmpty()) {
            throw new IllegalArgumentException();
        }
        verifyBuilderEnded();
        joinManager.join(path, alias, type, fetch);
        return (U) this;
    }

    @Override
    public TypedQuery<T> getQuery(EntityManager em) {
        TypedQuery<T> query = (TypedQuery) em.createQuery(getQueryString(), Object[].class);
        if (selectManager.getSelectObjectBuilder() != null) {
            queryTransformer.transformQuery(query, selectManager.getSelectObjectBuilder());
        }

        parameterizeQuery(query);
        return query;
    }

    void parameterizeQuery(javax.persistence.Query q) {
        Map<String, Object> parameters = parameterManager.getParameters();
        for (Parameter<?> p : q.getParameters()) {
            if (!isParameterSet(p.getName())) {
                throw new IllegalStateException("Unsatisfied parameter " + p.getName());
            }
            Object paramValue = parameters.get(p.getName());
            if (paramValue instanceof ParameterManager.TemporalCalendarParameterWrapper) {
                ParameterManager.TemporalCalendarParameterWrapper wrappedValue = (ParameterManager.TemporalCalendarParameterWrapper) paramValue;
                q.setParameter(p.getName(), wrappedValue.getValue(), wrappedValue.getType());
            } else if (paramValue instanceof ParameterManager.TemporalDateParameterWrapper) {
                ParameterManager.TemporalDateParameterWrapper wrappedValue = (ParameterManager.TemporalDateParameterWrapper) paramValue;
                q.setParameter(p.getName(), wrappedValue.getValue(), wrappedValue.getType());
            } else {
                q.setParameter(p.getName(), paramValue);
            }
        }
    }

    @Override
    public boolean isParameterSet(String name) {
        Map<String, Object> parameters = parameterManager.getParameters();
        if (!parameters.containsKey(name)) {
            throw new IllegalArgumentException(String.format("Parameter name \"%s\" does not exist", name));
        }
        return parameters.get(name) != null;
    }

    @Override
    public Set<? extends Parameter<?>> getParameters() {
        Map<String, Object> parameters = parameterManager.getParameters();
        Set<Parameter<?>> result = new HashSet<Parameter<?>>();

        for (Map.Entry<String, Object> paramEntry : parameters.entrySet()) {
            result.add(new ParameterImpl(paramEntry.getValue() == null ? null : paramEntry.getValue().getClass(), paramEntry.getKey()));
        }
        return result;
    }
    
    private class ParameterImpl<T> implements Parameter<T>{
        private final Class<T> paramClass;
        private final String paramName;

        public ParameterImpl(Class<T> paramClass, String paramName) {
            this.paramClass = paramClass;
            this.paramName = paramName;
        }
        
        @Override
        public String getName() {
            return paramName;
        }

        @Override
        public Integer getPosition() {
            return null;
        }

        @Override
        public Class<T> getParameterType() {
            return paramClass;
        }
        
    }
}
