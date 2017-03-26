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
package api.usage.happypath;

import api.usage.Happy;
import api.usage.Sad;
import io.github.theangrydev.businessflows.Attempt;
import io.github.theangrydev.businessflows.HappyPath;
import io.github.theangrydev.businessflows.Mapping;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

/**
 * These are the happy path biased views of the three possible underlying cases (happy, sad or technical failure).
 */
public class HappyPathBaseCasesTest implements WithAssertions {

    @Test
    public void happyCaseHappyPath() {
        Happy happy = new Happy();

        HappyPath<Happy, Sad> happyPath = HappyPath.happyPath(happy);

        assertThat(happyPath.getHappy()).isEqualTo(happy);
    }

    @Test
    public void sadCaseHappyPath() {
        Sad sad = new Sad();

        HappyPath<Happy, Sad> happyPath = HappyPath.sadPath(sad);

        assertThat(happyPath.getSad()).isEqualTo(sad);
    }

    @Test
    public void technicalFailureCaseHappyPath() {
        Exception technicalFailure = new Exception();

        HappyPath<Happy, Sad> happyPath = HappyPath.technicalFailure(technicalFailure);

        assertThat(happyPath.getTechnicalFailure()).isEqualTo(technicalFailure);
    }

    /**
     * An attempt can fail and turn into a technical failure.
     */
    @Test
    public void happyAttemptCanFail() {
        RuntimeException technicalFailure = new RuntimeException();
        Attempt<Happy> attempt = () -> {throw technicalFailure;};

        HappyPath<Happy, Sad> happyPath = HappyPath.happyAttempt(attempt);

        assertThat(happyPath.getTechnicalFailure()).isEqualTo(technicalFailure);
    }

    /**
     * In this case, the technical failure is mapped to a sad path that contains the exception message.
     */
    @Test
    public void happyAttemptCanFailAndMapTechnicalFailureToASadPath() {
        Attempt<Happy> attempt = () -> {throw new RuntimeException("message");};
        Mapping<Exception, Sad> sadMapping = technicalFailure -> new Sad(technicalFailure.getMessage());

        HappyPath<Happy, Sad> happyPath = HappyPath.happyAttempt(attempt, sadMapping);

        assertThat(happyPath.getSad()).hasToString("message");
    }

    /**
     * An attempt that was once happy can be turned into a sad path.
     * The `happyAttempt` method is playing the role of the Try monad here, but is lifted into the Either monad immediately, which is why the `Sad` type has to be specified up front.
     */
    @Test
    public void happyAttemptCanIntroduceSadTypeViaThen() {
        Sad sad = new Sad();

        HappyPath<Happy, Sad> happyPath = HappyPath.<Happy, Sad>happyAttempt(Happy::new)
                .then(happy -> HappyPath.sadPath(sad));

        assertThat(happyPath.getSad()).isEqualTo(sad);
    }

    /**
     * An attempt can fail and turn into a technical failure.
     */
    @Test
    public void happyPathAttemptCanFail() {
        RuntimeException technicalFailure = new RuntimeException();
        Attempt<HappyPath<Happy, Sad>> attempt = () -> {throw technicalFailure;};

        HappyPath<Happy, Sad> happyPath = HappyPath.happyPathAttempt(attempt);

        assertThat(happyPath.getTechnicalFailure()).isEqualTo(technicalFailure);
    }

    /**
     * An attempt can succeed and produce a happy path
     */
    @Test
    public void happyPathAttemptCanSucceed() {
        Happy happy = new Happy();
        Attempt<HappyPath<Happy, Sad>> attempt = () -> HappyPath.happyPath(happy);

        HappyPath<Happy, Sad> happyPath = HappyPath.happyPathAttempt(attempt);

        assertThat(happyPath.getHappy()).isSameAs(happy);
    }
}
