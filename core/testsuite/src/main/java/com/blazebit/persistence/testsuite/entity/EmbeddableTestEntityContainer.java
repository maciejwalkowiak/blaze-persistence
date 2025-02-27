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

package com.blazebit.persistence.testsuite.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "emb_tst_ent_cont")
public class EmbeddableTestEntityContainer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Set<EmbeddableTestEntity> embeddableTestEntities = new HashSet<EmbeddableTestEntity>();
    
    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "emb_tst_ent_cont_entities",
        joinColumns = @JoinColumn(name = "tst_ent_cont_id", referencedColumnName = "id"),
        inverseJoinColumns = {
            @JoinColumn(name = "tst_ent_key", referencedColumnName = "test_key"),
            @JoinColumn(name = "tst_ent_value", referencedColumnName = "test_value")
        }
    )
    public Set<EmbeddableTestEntity> getEmbeddableTestEntities() {
        return embeddableTestEntities;
    }
    public void setEmbeddableTestEntities(Set<EmbeddableTestEntity> embeddableTestEntities) {
        this.embeddableTestEntities = embeddableTestEntities;
    }
    
}
