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

package com.blazebit.persistence.criteria.impl;

import com.blazebit.apt.service.ServiceProvider;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.criteria.BlazeCriteriaBuilder;
import com.blazebit.persistence.criteria.spi.BlazeCriteriaBuilderFactory;

/**
 * @author Christian Beikov
 * @since 1.2.1
 */
@ServiceProvider(BlazeCriteriaBuilderFactory.class)
public class BlazeCriteriaBuilderFactoryImpl implements BlazeCriteriaBuilderFactory {

    @Override
    public BlazeCriteriaBuilder createCriteriaBuilder(CriteriaBuilderFactory criteriaBuilderFactory) {
        return new BlazeCriteriaBuilderImpl(criteriaBuilderFactory);
    }
}