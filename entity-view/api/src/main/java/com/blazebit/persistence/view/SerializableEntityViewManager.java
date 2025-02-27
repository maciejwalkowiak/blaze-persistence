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

package com.blazebit.persistence.view;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.FullQueryBuilder;
import com.blazebit.persistence.view.change.SingularChangeModel;
import com.blazebit.persistence.view.metamodel.ViewMetamodel;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * A serializable entity view manager implementation that accesses the actual entity view manager through a static field in the entity view implementation class.
 *
 * @author Christian Beikov
 * @since 1.5.0
 */
public class SerializableEntityViewManager implements EntityViewManager, Serializable {

    public static final String EVM_FIELD_NAME = "ENTITY_VIEW_MANAGER";
    public static final String SERIALIZABLE_EVM_FIELD_NAME = "SERIALIZABLE_ENTITY_VIEW_MANAGER";
    public static final String SERIALIZABLE_EVM_DELEGATE_FIELD_NAME = "evm";

    private final Class<?> entityViewClass;
    private transient volatile EntityViewManager evm;

    /**
     * Creates a new serializable entity view manager.
     *
     * @param entityViewClass The entity view class from which to access the entity view manager from
     * @param evm The transient entity view manager
     */
    public SerializableEntityViewManager(Class<?> entityViewClass, EntityViewManager evm) {
        this.entityViewClass = entityViewClass;
        this.evm = evm;
    }

    private EntityViewManager getEvm() {
        EntityViewManager evm = this.evm;
        if (evm == null) {
            try {
                Field field = entityViewClass.getDeclaredField(EVM_FIELD_NAME);
                this.evm = evm = (EntityViewManager) field.get(null);
            } catch (Exception ex) {
                throw new IllegalStateException("Could not access entity view manager of entity view class: " + entityViewClass.getName(), ex);
            }
        }
        return evm;
    }

    @Override
    public ViewMetamodel getMetamodel() {
        return getEvm().getMetamodel();
    }

    @Override
    public Map<String, Object> getOptionalParameters() {
        return getEvm().getOptionalParameters();
    }

    @Override
    public <T> T find(EntityManager entityManager, Class<T> entityViewClass, Object entityId) {
        return getEvm().find(entityManager, entityViewClass, entityId);
    }

    @Override
    public <T> T find(EntityManager entityManager, EntityViewSetting<T, CriteriaBuilder<T>> entityViewSetting, Object entityId) {
        return getEvm().find(entityManager, entityViewSetting, entityId);
    }

    @Override
    public <T> T getReference(Class<T> entityViewClass, Object id) {
        return getEvm().getReference(entityViewClass, id);
    }

    @Override
    public <T> T getEntityReference(EntityManager entityManager, Object entityView) {
        return getEvm().getEntityReference(entityManager, entityView);
    }

    @Override
    public <T> SingularChangeModel<T> getChangeModel(T entityView) {
        return getEvm().getChangeModel(entityView);
    }

    @Override
    public <T> T create(Class<T> entityViewClass) {
        return getEvm().create(entityViewClass);
    }

    @Override
    public <T> T create(Class<T> entityViewClass, Map<String, Object> optionalParameters) {
        return getEvm().create(entityViewClass, optionalParameters);
    }

    @Override
    public <X> EntityViewBuilder<X> createBuilder(Class<X> clazz) {
        return getEvm().createBuilder(clazz);
    }

    @Override
    public <X> EntityViewBuilder<X> createBuilder(Class<X> clazz, String constructorName) {
        return getEvm().createBuilder(clazz, constructorName);
    }

    @Override
    public <X> EntityViewBuilder<X> createBuilder(X view) {
        return getEvm().createBuilder(view);
    }

    @Override
    public <X> EntityViewBuilder<X> createBuilder(X view, String constructorName) {
        return getEvm().createBuilder(view, constructorName);
    }

    @Override
    public <X> EntityViewBuilder<X> createBuilder(Class<X> clazz, Map<String, Object> optionalParameters) {
        return getEvm().createBuilder(clazz, optionalParameters);
    }

    @Override
    public <X> EntityViewBuilder<X> createBuilder(Class<X> clazz, Map<String, Object> optionalParameters, String constructorName) {
        return getEvm().createBuilder(clazz, optionalParameters, constructorName);
    }

    @Override
    public <X> EntityViewBuilder<X> createBuilder(X view, Map<String, Object> optionalParameters) {
        return getEvm().createBuilder(view, optionalParameters);
    }

    @Override
    public <X> EntityViewBuilder<X> createBuilder(X view, Map<String, Object> optionalParameters, String constructorName) {
        return getEvm().createBuilder(view, optionalParameters, constructorName);
    }

    @Override
    public <T> T convert(Object source, Class<T> entityViewClass, ConvertOption... convertOptions) {
        return getEvm().convert(source, entityViewClass, convertOptions);
    }

    @Override
    public <T> T convert(Object source, Class<T> entityViewClass, String constructorName, ConvertOption... convertOptions) {
        return getEvm().convert(source, entityViewClass, constructorName, convertOptions);
    }

    @Override
    public <T> T convert(Object source, Class<T> entityViewClass, Map<String, Object> optionalParameters, ConvertOption... convertOptions) {
        return getEvm().convert(source, entityViewClass, optionalParameters, convertOptions);
    }

    @Override
    public <T> T convert(Object source, Class<T> entityViewClass, String constructorName, Map<String, Object> optionalParameters, ConvertOption... convertOptions) {
        return getEvm().convert(source, entityViewClass, constructorName, optionalParameters, convertOptions);
    }

    @Override
    public <T> ConvertOperationBuilder<T> convertWith(Object source, Class<T> entityViewClass, ConvertOption... convertOptions) {
        return getEvm().convertWith(source, entityViewClass, convertOptions);
    }

    @Override
    public <T> ConvertOperationBuilder<T> convertWith(Object source, Class<T> entityViewClass, String constructorName, ConvertOption... convertOptions) {
        return getEvm().convertWith(source, entityViewClass, constructorName, convertOptions);
    }

    @Override
    public <T> ConvertOperationBuilder<T> convertWith(Object source, Class<T> entityViewClass, Map<String, Object> optionalParameters, ConvertOption... convertOptions) {
        return getEvm().convertWith(source, entityViewClass, optionalParameters, convertOptions);
    }

    @Override
    public <T> ConvertOperationBuilder<T> convertWith(Object source, Class<T> entityViewClass, String constructorName, Map<String, Object> optionalParameters, ConvertOption... convertOptions) {
        return getEvm().convertWith(source, entityViewClass, constructorName, optionalParameters, convertOptions);
    }

    @Override
    public void save(EntityManager entityManager, Object view) {
        getEvm().save(entityManager, view);
    }

    @Override
    public void saveFull(EntityManager entityManager, Object view) {
        getEvm().saveFull(entityManager, view);
    }

    @Override
    public void saveTo(EntityManager entityManager, Object view, Object entity) {
        getEvm().saveTo(entityManager, view, entity);
    }

    @Override
    public void saveFullTo(EntityManager entityManager, Object view, Object entity) {
        getEvm().saveFullTo(entityManager, view, entity);
    }

    @Override
    @Deprecated
    public void update(EntityManager entityManager, Object view) {
        getEvm().update(entityManager, view);
    }

    @Override
    @Deprecated
    public void updateFull(EntityManager entityManager, Object view) {
        getEvm().updateFull(entityManager, view);
    }

    @Override
    public FlushOperationBuilder saveWith(EntityManager entityManager, Object view) {
        return getEvm().saveWith(entityManager, view);
    }

    @Override
    public FlushOperationBuilder saveFullWith(EntityManager entityManager, Object view) {
        return getEvm().saveFullWith(entityManager, view);
    }

    @Override
    public FlushOperationBuilder saveWithTo(EntityManager entityManager, Object view, Object entity) {
        return getEvm().saveWithTo(entityManager, view, entity);
    }

    @Override
    public FlushOperationBuilder saveFullWithTo(EntityManager entityManager, Object view, Object entity) {
        return getEvm().saveFullWithTo(entityManager, view, entity);
    }

    @Override
    public void remove(EntityManager entityManager, Object view) {
        getEvm().remove(entityManager, view);
    }

    @Override
    public FlushOperationBuilder removeWith(EntityManager entityManager, Object view) {
        return getEvm().removeWith(entityManager, view);
    }

    @Override
    public void remove(EntityManager entityManager, Class<?> entityViewClass, Object viewId) {
        getEvm().remove(entityManager, entityViewClass, viewId);
    }

    @Override
    public FlushOperationBuilder removeWith(EntityManager entityManager, Class<?> entityViewClass, Object viewId) {
        return getEvm().removeWith(entityManager, entityViewClass, viewId);
    }

    @Override
    public <T, Q extends FullQueryBuilder<T, Q>> Q applySetting(EntityViewSetting<T, Q> setting, CriteriaBuilder<?> criteriaBuilder) {
        return getEvm().applySetting(setting, criteriaBuilder);
    }

    @Override
    public <T, Q extends FullQueryBuilder<T, Q>> Q applySetting(EntityViewSetting<T, Q> setting, CriteriaBuilder<?> criteriaBuilder, String entityViewRoot) {
        return getEvm().applySetting(setting, criteriaBuilder, entityViewRoot);
    }

    @Override
    public <T> T getService(Class<T> serviceClass) {
        return getEvm().getService(serviceClass);
    }

    @Override
    public void close() {
        getEvm().close();
    }
}
