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
import static io.github.theangrydev.businessflows.ApiVersionHistory.VERSION_10_3_0;
import static io.github.theangrydev.businessflows.ApiVersionHistory.VERSION_1_0_0;
import static io.github.theangrydev.businessflows.ApiVersionHistory.VERSION_8_2_0;

/**
 * A {@link BusinessFlow} is a biased view of a {@link BusinessCase}.
 * This is the base {@link BusinessFlow} that contains operations that are common to all the biased views.
 *
 * @param <Happy> The type of happy object this {@link BusinessFlow} may represent
 * @param <Sad>   The type of sad object this {@link BusinessFlow} may represent
 */
@ApiFeature(since = VERSION_1_0_0, stability = STABLE)
public interface BusinessFlow<Happy, Sad> extends BusinessCase<Happy, Sad> {

    /**
     * A {@link TechnicalFailure} view of the {@link BusinessFlow}.
     * <p>
     * NOTE: The technical failure will only be present if the failure occurred inside of a {@link BusinessFlow}.
     * To ensure failures are caught inside a flow, use e.g. {@link HappyPath#happyPathAttempt(Attempt)} at the top of
     * the scope, before this method is called.
     * </p>
     *
     * @return A view of the underlying business case as a {@link TechnicalFailure}
     */
    @ApiFeature(since = VERSION_1_0_0, stability = STABLE)
    TechnicalFailure<Happy, Sad> ifTechnicalFailure();

    /**
     * A {@link SadPath} view of the {@link BusinessFlow}.
     *
     * @return A view of the underlying business case as a {@link SadPath}
     */
    @ApiFeature(since = VERSION_1_0_0, stability = STABLE)
    SadPath<Happy, Sad> ifSad();

    /**
     * A {@link HappyPath} view of the {@link BusinessFlow}.
     *
     * @return A view of the underlying business case as a {@link HappyPath}
     */
    @ApiFeature(since = VERSION_1_0_0, stability = STABLE)
    HappyPath<Happy, Sad> ifHappy();

    /**
     * @return true if the underlying business case is a {@link HappyCase}, false otherwise
     */
    @ApiFeature(since = VERSION_8_2_0, stability = STABLE)
    default boolean isHappy() {
        return ifHappy().isPresent();
    }

    /**
     * @return true if the underlying business case is a {@link SadCase}, false otherwise
     */
    @ApiFeature(since = VERSION_8_2_0, stability = STABLE)
    default boolean isSad() {
        return ifSad().isPresent();
    }

    /**
     * @return true if the underlying business case is a {@link SadCase}, false otherwise
     */
    @ApiFeature(since = VERSION_10_3_0, stability = STABLE)
    default boolean isTechnicalFailure() {
        return ifTechnicalFailure().isPresent();
    }

    /**
     * @return If the underlying business case is a {@link HappyCase} then the {@link Happy} object, else an {@link IllegalStateException}
     * @throws IllegalStateException If the underlying business case is not a {@link HappyCase}
     */
    @ApiFeature(since = VERSION_8_2_0, stability = STABLE)
    default Happy getHappy() {
        return ifHappy().get();
    }

    /**
     * @return If the underlying business case is a {@link SadCase} then the {@link Sad} object, else an {@link IllegalStateException}
     * @throws IllegalStateException If the underlying business case is not a {@link SadCase}
     */
    @ApiFeature(since = VERSION_8_2_0, stability = STABLE)
    default Sad getSad() {
        return ifSad().get();
    }

    /**
     * @return If the underlying business case is a {@link TechnicalFailureCase} then the {@link Exception} object, else an {@link IllegalStateException}
     * @throws IllegalStateException If the underlying business case is not a {@link TechnicalFailureCase}
     */
    @ApiFeature(since = VERSION_10_3_0, stability = STABLE)
    default Exception getTechnicalFailure() {
        return ifTechnicalFailure().get();
    }
}
