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

package com.blazebit.persistence.integration.jaxrs.jsonb.testsuite.resource;

import com.blazebit.persistence.integration.jaxrs.jsonb.testsuite.JsonbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

import javax.enterprise.inject.Alternative;
import javax.ws.rs.ApplicationPath;

/**
 * @author Moritz Becker
 * @since 1.6.4
 */
@Alternative
@ApplicationPath("/")
public class Application extends ResourceConfig {

    public Application() {
        this.packages("com.blazebit.persistence.integration.jaxrs.jsonb");
        this.register(JsonbJsonProvider.class);
    }
}
