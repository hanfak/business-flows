/*
 * Copyright 2016 Liam Williams <liam.williams@zoho.com>.
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

import java.util.Optional;
import java.util.function.Function;

class HappyCase<Sad, Happy> implements BusinessCase<Sad, Happy> {

    private final Happy happy;

    HappyCase(Happy happy) {
        this.happy = happy;
    }

    @Override
    public <Result> Result join(Function<Sad, Result> sadJoiner, Function<Happy, Result> happyJoiner, Function<Exception, Result> technicalFailureJoiner) {
        return happyJoiner.apply(happy);
    }

    @Override
    public <Result> Result join(Function<Sad, Result> sadJoiner, Function<Happy, Result> happyJoiner) throws Exception {
        return happyJoiner.apply(happy);
    }

    @Override
    public Optional<Happy> happyOptional() {
        return Optional.of(happy);
    }

    @Override
    public Optional<Sad> sadOptional() {
        return Optional.empty();
    }

    @Override
    public Optional<Exception> technicalFailureOptional() {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "Happy: " + happy;
    }
}