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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A {@link TechnicalFailureCase} is a {@link BusinessCase} that is actually in an unexpected exceptional state.
 * <p>
 * {@inheritDoc}
 */
class TechnicalFailureCase<Happy, Sad> implements BusinessCase<Happy, Sad> {

    final Exception technicalFailure;

    TechnicalFailureCase(Exception technicalFailure) {
        this.technicalFailure = technicalFailure;
    }

    @Override
    public PotentialFailure<Sad> toPotentialFailure(Function<Exception, Sad> technicalFailureMapping) {
        return PotentialFailure.failure(technicalFailureMapping.apply(technicalFailure));
    }

    @Override
    public <Result> Result join(Mapping<Happy, Result> happyJoiner, Mapping<Sad, Result> sadJoiner, Function<Exception, Result> technicalFailureJoiner) {
        return technicalFailureJoiner.apply(technicalFailure);
    }

    @Override
    public <Result> Result joinOrThrow(Mapping<Happy, Result> happyJoiner, Mapping<Sad, Result> sadJoiner) throws Exception {
        throw technicalFailure;
    }

    @Override
    public void consumeOrThrow(Peek<Happy> happyConsumer, Peek<Sad> sadConsumer) throws Exception {
        throw technicalFailure;
    }

    @Override
    public void consume(Peek<Happy> happyConsumer, Peek<Sad> sadConsumer, Consumer<Exception> technicalFailureConsumer) {
        technicalFailureConsumer.accept(technicalFailure);
    }

    @Override
    public String toString() {
        StringWriter stringWriter = new StringWriter();
        technicalFailure.printStackTrace(new PrintWriter(stringWriter));
        String stackTrace = stringWriter.toString();
        return "Technical Failure: " + stackTrace;
    }
}
