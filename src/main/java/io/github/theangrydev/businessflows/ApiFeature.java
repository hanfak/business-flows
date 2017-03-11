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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static io.github.theangrydev.businessflows.ApiFeatureStability.EXPERIMENTAL;
import static io.github.theangrydev.businessflows.ApiVersionHistory.VERSION_10_2_0;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Documentation about a feature of the API.
 */
@Documented
@Target({TYPE, METHOD, FIELD, CONSTRUCTOR})
@Retention(RUNTIME)
@ApiFeature(stability = EXPERIMENTAL, since = VERSION_10_2_0)
public @interface ApiFeature {

    /**
     * NOTE: This is used in preference of {@code @since} to make it easier for tools to extract the version, rather
     * than having to parse Javadoc comments.
     *
     * @return The version that this feature was added.
     */
    @ApiFeature(stability = EXPERIMENTAL, since = VERSION_10_2_0)
    ApiVersionHistory since();

    /**
     * @return How stable this feature is.
     */
    @ApiFeature(stability = EXPERIMENTAL, since = VERSION_10_2_0)
    ApiFeatureStability stability();

    /**
     * @return Comments about the feature (e.g. if it is deprecated, what else to use instead).
     */
    @ApiFeature(stability = EXPERIMENTAL, since = VERSION_10_2_0)
    String comments() default "";
}
