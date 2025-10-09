package com.braintreegateway.unittest;

import java.util.Calendar;

import com.braintreegateway.UsBankAccount;
import com.braintreegateway.UsBankAccountVerification;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsBankAccountVerificationTest {
    @Test
    public void testVerificationAttributes() {
        String xml = "<us-bank-account-verification>"
                     + "  <status>verified</status>"
                     + "  <gateway-rejection-reason nil=\"true\"/>"
                     + "  <merchant-account-id>ygmxmpdxthqrrtfyisqahvclo</merchant-account-id>"
                     + "  <processor-response-code>1000</processor-response-code>"
                     + "  <processor-response-text>Approved</processor-response-text>"
                     + "  <additional-processor-response>Invalid routing number</additional-processor-response>"
                     + "  <id>6f34vp3z</id>"
                     + "  <verification-method>independent_check</verification-method>"
                     + "  <verification-add-ons>customer_verification</verification-add-ons>"
                     + "  <verification-determined-at type=\"datetime\">2018-11-16T23:22:48Z</verification-determined-at>"
                     + "  <us-bank-account>"
                     + "    <token>ch6byss</token>"
                     + "    <last-4>1234</last-4>"
                     + "    <account-type>checking</account-type>"
                     + "    <account-holder-name nil=\"true\"/>"
                     + "    <bank-name>Wells Fargo</bank-name>"
                     + "    <routing-number>123456789</routing-number>"
                     + "  </us-bank-account>"
                     + "  <created-at type=\"datetime\">2018-04-12T19:54:16Z</created-at>"
                     + "  <updated-at type=\"datetime\">2018-04-12T19:54:16Z</updated-at>"
                     + "</us-bank-account-verification>";

        SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);
        UsBankAccountVerification verification = new UsBankAccountVerification(node);

        assertVerificationMatches(verification, new ExpectedVerification()
            .withStatus(UsBankAccountVerification.Status.VERIFIED)
            .withVerificationMethod(UsBankAccountVerification.VerificationMethod.INDEPENDENT_CHECK)
            .withVerificationAddOns(UsBankAccountVerification.VerificationAddOns.CUSTOMER_VERIFICATION)
            .withProcessorResponseCode("1000")
            .withAdditionalProcessorResponse("Invalid routing number")
            .withId("6f34vp3z")
            .withCreatedAt(2018, 4, 12, 7, 54, 16)
            .withVerificationDeterminedAt(2018, 11, 16, 11, 22, 48)
            .withUsBankAccount(new ExpectedUsBankAccountForVerification()
                .withToken("ch6byss")));
    }

    @Test
    public void verificationMethodEnumIncludesInstantVerification() {
        UsBankAccountVerification.VerificationMethod instantVerification =
            UsBankAccountVerification.VerificationMethod.INSTANT_VERIFICATION_ACCOUNT_VALIDATION;

        assertNotNull(instantVerification);
        assertEquals("instant_verification_account_validation", instantVerification.toString());
    }

    @Test
    public void verificationMethodEnumAllMethodsPresent() {
        assertNotNull(UsBankAccountVerification.VerificationMethod.INDEPENDENT_CHECK);
        assertNotNull(UsBankAccountVerification.VerificationMethod.INSTANT_VERIFICATION_ACCOUNT_VALIDATION);
        assertNotNull(UsBankAccountVerification.VerificationMethod.MICRO_TRANSFERS);
        assertNotNull(UsBankAccountVerification.VerificationMethod.NETWORK_CHECK);
        assertNotNull(UsBankAccountVerification.VerificationMethod.TOKENIZED_CHECK);
    }

    @Test
    public void testVerificationWithInstantVerificationMethod() {
        String xml = "<us-bank-account-verification>"
                     + "  <status>verified</status>"
                     + "  <gateway-rejection-reason nil=\"true\"/>"
                     + "  <merchant-account-id>ygmxmpdxthqrrtfyisqahvclo</merchant-account-id>"
                     + "  <processor-response-code>1000</processor-response-code>"
                     + "  <processor-response-text>Approved</processor-response-text>"
                     + "  <id>inst_verification_id</id>"
                     + "  <verification-method>instant_verification_account_validation</verification-method>"
                     + "  <us-bank-account>"
                     + "    <token>instant_token</token>"
                     + "    <last-4>5678</last-4>"
                     + "    <account-type>savings</account-type>"
                     + "    <account-holder-name>John Doe</account-holder-name>"
                     + "    <bank-name>Chase Bank</bank-name>"
                     + "    <routing-number>987654321</routing-number>"
                     + "  </us-bank-account>"
                     + "  <created-at type=\"datetime\">2024-01-15T10:30:00Z</created-at>"
                     + "</us-bank-account-verification>";

        SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);
        UsBankAccountVerification verification = new UsBankAccountVerification(node);

        assertVerificationMatches(verification, new ExpectedVerification()
            .withStatus(UsBankAccountVerification.Status.VERIFIED)
            .withVerificationMethod(UsBankAccountVerification.VerificationMethod.INSTANT_VERIFICATION_ACCOUNT_VALIDATION)
            .withId("inst_verification_id")
            .withUsBankAccount(new ExpectedUsBankAccountForVerification()
                .withToken("instant_token")));
    }

    private void assertVerificationMatches(UsBankAccountVerification actual, ExpectedVerification expected) {
        if (expected.status != null) {
            assertEquals(expected.status, actual.getStatus());
        }
        if (expected.verificationMethod != null) {
            assertEquals(expected.verificationMethod, actual.getVerificationMethod());
        }
        if (expected.verificationAddOns != null) {
            assertEquals(expected.verificationAddOns, actual.getVerificationAddOns());
        }
        if (expected.processorResponseCode != null) {
            assertEquals(expected.processorResponseCode, actual.getProcessorResponseCode());
        }
        if (expected.additionalProcessorResponse != null) {
            assertEquals(expected.additionalProcessorResponse, actual.getAdditionalProcessorResponse());
        }
        if (expected.id != null) {
            assertEquals(expected.id, actual.getId());
        }
        if (expected.createdAt != null) {
            assertCalendarMatches(actual.getCreatedAt(), expected.createdAt);
        }
        if (expected.verificationDeterminedAt != null) {
            assertCalendarMatches(actual.getVerificationDeterminedAt(), expected.verificationDeterminedAt);
        }
        if (expected.usBankAccount != null) {
            assertUsBankAccountForVerificationMatches(actual.getUsBankAccount(), expected.usBankAccount);
        }
    }

    private void assertCalendarMatches(Calendar actual, ExpectedCalendar expected) {
        assertNotNull(actual);
        assertEquals((int) expected.year, actual.get(Calendar.YEAR));
        assertEquals((int) expected.month, actual.get(Calendar.MONTH) + 1);
        assertEquals((int) expected.day, actual.get(Calendar.DAY_OF_MONTH));
        assertEquals((int) expected.hour, actual.get(Calendar.HOUR));
        assertEquals((int) expected.minute, actual.get(Calendar.MINUTE));
        assertEquals((int) expected.second, actual.get(Calendar.SECOND));
    }

    private void assertUsBankAccountForVerificationMatches(UsBankAccount actual, ExpectedUsBankAccountForVerification expected) {
        if (expected.token != null) {
            assertEquals(expected.token, actual.getToken());
        }
    }

    private static class ExpectedVerification {
        private UsBankAccountVerification.Status status;
        private UsBankAccountVerification.VerificationMethod verificationMethod;
        private UsBankAccountVerification.VerificationAddOns verificationAddOns;
        private String processorResponseCode;
        private String additionalProcessorResponse;
        private String id;
        private ExpectedCalendar createdAt;
        private ExpectedCalendar verificationDeterminedAt;
        private ExpectedUsBankAccountForVerification usBankAccount;

        public ExpectedVerification withStatus(UsBankAccountVerification.Status status) {
            this.status = status;
            return this;
        }

        public ExpectedVerification withVerificationMethod(UsBankAccountVerification.VerificationMethod verificationMethod) {
            this.verificationMethod = verificationMethod;
            return this;
        }

        public ExpectedVerification withVerificationAddOns(UsBankAccountVerification.VerificationAddOns verificationAddOns) {
            this.verificationAddOns = verificationAddOns;
            return this;
        }

        public ExpectedVerification withProcessorResponseCode(String processorResponseCode) {
            this.processorResponseCode = processorResponseCode;
            return this;
        }

        public ExpectedVerification withAdditionalProcessorResponse(String additionalProcessorResponse) {
            this.additionalProcessorResponse = additionalProcessorResponse;
            return this;
        }

        public ExpectedVerification withId(String id) {
            this.id = id;
            return this;
        }

        public ExpectedVerification withCreatedAt(int year, int month, int day, int hour, int minute, int second) {
            this.createdAt = new ExpectedCalendar(year, month, day, hour, minute, second);
            return this;
        }

        public ExpectedVerification withVerificationDeterminedAt(int year, int month, int day, int hour, int minute, int second) {
            this.verificationDeterminedAt = new ExpectedCalendar(year, month, day, hour, minute, second);
            return this;
        }

        public ExpectedVerification withUsBankAccount(ExpectedUsBankAccountForVerification usBankAccount) {
            this.usBankAccount = usBankAccount;
            return this;
        }
    }

    private static class ExpectedCalendar {
        final private Integer year;
        final private Integer month;
        final private Integer day;
        final private Integer hour;
        final private Integer minute;
        final private Integer second;

        public ExpectedCalendar(int year, int month, int day, int hour, int minute, int second) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
            this.second = second;
        }
    }

    private static class ExpectedUsBankAccountForVerification {
        private String token;

        public ExpectedUsBankAccountForVerification withToken(String token) {
            this.token = token;
            return this;
        }
    }
}
