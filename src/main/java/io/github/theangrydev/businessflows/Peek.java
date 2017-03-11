/*
 * Copyright 2016-2017 Liam Williams <liam.williams@zoho.com>.
 *
 * This file is part of business-flows.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.theangrydev.businessflows;

import static io.github.theangrydev.businessflows.ApiFeatureStability.STABLE;
import static io.github.theangrydev.businessflows.ApiVersionHistory.VERSION_1_0_0;

/**
 * Similar to a {@link java.util.function.Consumer} but is allowed to throw an {@link Exception}.
 *
 * @param <T> The type to look at
 */
@FunctionalInterface
@ApiFeature(since = VERSION_1_0_0, stability = STABLE)
public interface Peek<T> {

    /**
     * Take a look at the instance if it is present.
     *
     * @param instance The instance to look at (if it is present)
     * @throws Exception If there is a technical failure during the peek
     */
    @ApiFeature(since = VERSION_1_0_0, stability = STABLE)
    void peek(T instance) throws Exception;
}
