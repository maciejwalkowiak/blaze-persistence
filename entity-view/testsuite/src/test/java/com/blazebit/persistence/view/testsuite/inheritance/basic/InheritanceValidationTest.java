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

package com.blazebit.persistence.view.testsuite.inheritance.basic;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.EntityViewInheritance;
import com.blazebit.persistence.view.EntityViewInheritanceMapping;
import com.blazebit.persistence.view.EntityViews;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import com.blazebit.persistence.view.testsuite.AbstractEntityViewTest;
import com.blazebit.persistence.testsuite.entity.Document;
import com.blazebit.persistence.testsuite.entity.Person;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Christian Beikov
 * @since 1.2.0
 */
public class InheritanceValidationTest extends AbstractEntityViewTest {

    @EntityView(Document.class)
    @EntityViewInheritance({ DocBase1Sub1.class })
    interface DocBase1 {
        @IdMapping Long getId();
    }

    @EntityView(Document.class)
    interface DocBase1Sub1 extends DocBase1 {
    }

    @Test
    public void missingInheritanceMappingValidation() {
        try {
            build(
                    DocBase1.class,
                    DocBase1Sub1.class
            );
            Assert.fail("Expected validation exception!");
        } catch (IllegalArgumentException ex) {
            if (!ex.getMessage().contains("@EntityViewInheritanceMapping")) {
                throw ex;
            }
        }
    }

    @EntityView(Document.class)
    @EntityViewInheritance({ DocBase2Sub1.class })
    interface DocBase2 {
        @IdMapping Long getId();
    }

    @EntityView(Document.class)
    @EntityViewInheritanceMapping("1 = 1")
    interface DocBase2Sub1 {
    }

    @Test
    public void notASubtypeValidation() {
        try {
            build(
                    DocBase2.class,
                    DocBase2Sub1.class
            );
            Assert.fail("Expected validation exception!");
        } catch (IllegalArgumentException ex) {
            if (!ex.getMessage().contains("Java subtype")) {
                throw ex;
            }
        }
    }

    @EntityView(Document.class)
    @EntityViewInheritance({ DocBase3.class })
    interface DocBase3 {
        @IdMapping Long getId();
    }

    @Test
    public void selfDeclarationValidation() {
        try {
            build(DocBase3.class);
            Assert.fail("Expected validation exception!");
        } catch (IllegalArgumentException ex) {
            if (!ex.getMessage().contains("declared itself in @EntityViewInheritance")) {
                throw ex;
            }
        }
    }

    @EntityView(Document.class)
    @EntityViewInheritance
    @EntityViewInheritanceMapping("1 = 1")
    interface DocBase4 {
        @IdMapping Long getId();
    }

    @Test
    public void notUsedInInheritanceShouldHaveMappingValidation() {
        try {
            build(DocBase4.class);
            Assert.fail("Expected validation exception!");
        } catch (IllegalArgumentException ex) {
            if (!ex.getMessage().contains("@EntityViewInheritanceMapping but is never used as subtype")) {
                throw ex;
            }
        }
    }

    @EntityView(Document.class)
    @EntityViewInheritance
    interface DocBase5 {
        @IdMapping Long getId();
    }

    @EntityView(Document.class)
    @EntityViewInheritanceMapping("")
    interface DocBase5Sub1 extends DocBase5 {
        @IdMapping Long getId();
    }

    @Test
    public void usedInInheritanceShouldHaveMappingValidation() {
        try {
            build(
                    DocBase5.class,
                    DocBase5Sub1.class
            );
            Assert.fail("Expected validation exception!");
        } catch (IllegalArgumentException ex) {
            if (!ex.getMessage().contains("no @EntityViewInheritanceMapping but is used as inheritance subtype")) {
                throw ex;
            }
        }
    }

    @EntityView(Person.class)
    interface PersBase6 {
        DocBase6 getPartnerDocument();
    }

    @EntityView(Document.class)
    @EntityViewInheritance
    interface DocBase6 {
        @IdMapping Long getId();
    }

    @EntityView(Document.class)
    @EntityViewInheritanceMapping("age < 15")
    interface DocBase6Sub1 extends DocBase6 {
        String getName();
    }

    @EntityView(Document.class)
    @EntityViewInheritanceMapping("age > 15")
    interface DocBase6Sub2 extends DocBase6 {
        PersBase6 getOwner();
    }

    @Test
    public void inheritanceIntroducedCycleValidation() {
        try {
            build(
                    PersBase6.class,
                    DocBase6.class,
                    DocBase6Sub1.class,
                    DocBase6Sub2.class
            );
            Assert.fail("Expected validation exception!");
        } catch (IllegalArgumentException ex) {
            if (!ex.getMessage().contains("circular dependency")) {
                throw ex;
            }
        }
    }

}
