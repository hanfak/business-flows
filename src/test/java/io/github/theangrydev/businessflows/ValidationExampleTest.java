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

import org.junit.Test;

import java.util.List;

import static io.github.theangrydev.businessflows.FieldValidator.fieldValidator;
import static io.github.theangrydev.businessflows.PotentialFailure.failure;
import static io.github.theangrydev.businessflows.PotentialFailure.success;
import static io.github.theangrydev.businessflows.ValidationPath.validators;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

//TODO: tests need improving. coverage is fine but they aren't very descriptive
public class ValidationExampleTest {

    private static class AggregateErrors {
        private final List<ValidationError> validationErrors;

        private AggregateErrors(List<ValidationError> validationErrors) {
            this.validationErrors = validationErrors;
        }

        static AggregateErrors errorsWithMessage1(List<ValidationError> validationErrors) {
            return new AggregateErrors(validationErrors);
        }

        static AggregateErrors errorsWithMessage2(List<ValidationError> validationErrors) {
            return new AggregateErrors(validationErrors);
        }

        @Override
        public String toString() {
            return validationErrors.toString();
        }
    }
    private static class ValidationError {
        private final String error;

        private ValidationError(String error) {
            this.error = error;
        }

        @Override
        public String toString() {
            return error;
        }
    }

    private static class RegistrationForm {
        private final String firstName;
        private final String lastName;
        private final String age;

        private RegistrationForm(String firstName, String lastName, String age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }

        @Override
        public String toString() {
            return "First Name: " + firstName + System.lineSeparator() +
                    "Last Name: " + lastName + System.lineSeparator() +
                    "Age:" + age + System.lineSeparator();
        }
    }


    private class NotBlankValidator implements Validator<String, ValidationError> {

        private final String fieldName;

        private NotBlankValidator(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override
        public PotentialFailure<ValidationError> attempt(String fieldValue) {
            if (fieldValue == null || fieldValue.trim().isEmpty()) {
                return failure(new ValidationError(format("Field '%s' was empty", fieldName)));
            }
            return success();
        }
    }

    @Test
    public void validateRegistrationForm() {
        String result = validate(registrationForm())
                .peek(this::registerUser)
                .ifTechnicalFailure().peek(this::logFailure)
                .join(this::renderJoinedPage, this::renderValidationErrors, this::renderFailure);
        assertThat(result).isEqualTo("You joined!");
    }

    @Test
    public void validateRegistrationFormBlankFirstName() {
        String result = validate(registrationFormWithBlankAge())
                .peek(this::registerUser)
                .ifTechnicalFailure().peek(this::logFailure)
                .join(this::renderJoinedPage, this::renderValidationErrors, this::renderFailure);
        assertThat(result).isEqualTo("Please fix the errors: [Field 'Age' was empty]");
    }

    @Test
    public void validateRegistrationFormWithTechnicalFailure() {
        Exception expectedTechnicalFailure = new Exception();

        Exception actualTechnicalFailure = validateWithTechnicalFailure(registrationForm(), expectedTechnicalFailure)
                .ifTechnicalFailure().get();

        assertThat(actualTechnicalFailure).isEqualTo(expectedTechnicalFailure);
    }

    @Test
    public void validateRegistrationFormWithTechnicalFailureDuringAggregateMapping() {
        Exception expectedTechnicalFailure = new Exception();

        Exception actualTechnicalFailure = validateWithTechnicalFailureDuringAggregateMapping(registrationForm(), expectedTechnicalFailure)
                .ifTechnicalFailure().get();

        assertThat(actualTechnicalFailure).isEqualTo(expectedTechnicalFailure);
    }

    private HappyPath<RegistrationForm, AggregateErrors> validate(RegistrationForm registrationForm) {
        return ValidationPath
                .validateAllInto(registrationForm, AggregateErrors::errorsWithMessage1, firstNameValidator(), lastNameValidator())
                .validateAll(validators(ageValidator()))
                .validateAllInto(AggregateErrors::errorsWithMessage2, validators(firstNameValidator(), lastNameValidator(), ageValidator()));
    }

    private HappyPath<RegistrationForm, List<ValidationError>> validateWithTechnicalFailure(RegistrationForm registrationForm, Exception technicalFailure) {
        return ValidationPath.<RegistrationForm, ValidationError>validationPath(registrationForm)
                .validateAll(singletonList(registrationForm1 -> {throw technicalFailure;}));
    }

    private HappyPath<RegistrationForm, List<ValidationError>> validateWithTechnicalFailureDuringAggregateMapping(RegistrationForm registrationForm, Exception technicalFailure) {
        return ValidationPath.<RegistrationForm, ValidationError>validationPath(registrationForm)
                .validateAllInto(sads -> {throw technicalFailure;}, singletonList(registrationForm1 -> failure(new ValidationError(""))));
    }

    private void logFailure(Exception exception) {
        System.out.println("e = " + exception);
    }

    private String renderJoinedPage(RegistrationForm registrationForm) {
        return "You joined!";
    }

    private String renderValidationErrors(AggregateErrors validationErrors) {
        return "Please fix the errors: " + validationErrors;
    }

    private String renderFailure(Exception e) {
        return "There was a technical failure. Please try again.";
    }

    private void registerUser(RegistrationForm registrationForm) {
        System.out.println("Register in database");
    }

    private RegistrationForm registrationForm() {
        return new RegistrationForm("first", "last", "25");
    }

    private RegistrationForm registrationFormWithBlankAge() {
        return new RegistrationForm("first", "last", "");
    }

    private Validator<RegistrationForm, ValidationError> ageValidator() {
        return fieldValidator(form -> form.age, new NotBlankValidator("Age"));
    }

    private Validator<RegistrationForm, ValidationError> lastNameValidator() {
        return fieldValidator(form -> form.firstName, new NotBlankValidator("Last Name"));
    }

    private Validator<RegistrationForm, ValidationError> firstNameValidator() {
        return fieldValidator(form -> form.lastName, new NotBlankValidator("First Name"));
    }
}
