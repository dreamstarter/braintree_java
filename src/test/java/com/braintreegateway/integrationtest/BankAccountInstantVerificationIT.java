package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class BankAccountInstantVerificationIT extends IntegrationTest {

    private BraintreeGateway usBankGateway;

    @BeforeEach
    @Override
    public void createGateway() {
        this.gateway = new BraintreeGateway(
                Environment.DEVELOPMENT,
                "integration2_merchant_id",
                "integration2_public_key",
                "integration2_private_key"
        );

        this.usBankGateway = new BraintreeGateway(
                Environment.DEVELOPMENT,
                "integration_merchant_id",
                "integration_public_key",
                "integration_private_key"
        );
    }

    @Test
    public void createJwtWithValidRequest() {
        BankAccountInstantVerificationJwtRequest request = new BankAccountInstantVerificationJwtRequest()
            .businessName("15Ladders")
            .returnUrl("https://example.com/success")
            .cancelUrl("https://example.com/cancel");

        Result<BankAccountInstantVerificationJwt> result = gateway.bankAccountInstantVerification().createJwt(request);

        assertTrue(result.isSuccess());
        assertNotNull(result.getTarget());
        assertNotNull(result.getTarget().getJwt());
        assertFalse(result.getTarget().getJwt().isEmpty());
    }

    @Test
    public void createJwtWithInvalidBusinessName() {
        BankAccountInstantVerificationJwtRequest request = new BankAccountInstantVerificationJwtRequest()
            .businessName("")
            .returnUrl("https://example.com/return")
            .cancelUrl("https://example.com/cancel");

        Result<BankAccountInstantVerificationJwt> result = gateway.bankAccountInstantVerification().createJwt(request);

        assertFalse(result.isSuccess());
        assertNotNull(result.getErrors());
    }

    @Test
    public void createJwtWithInvalidUrls() {
        BankAccountInstantVerificationJwtRequest request = new BankAccountInstantVerificationJwtRequest()
            .businessName("15Ladders")
            .returnUrl("not-a-valid-url")
            .cancelUrl("also-not-valid");

        Result<BankAccountInstantVerificationJwt> result = usBankGateway.bankAccountInstantVerification().createJwt(request);

        assertFalse(result.isSuccess());
        assertNotNull(result.getErrors());
    }

    @Test
    public void createTransactionWithInstantVerificationNonceAndAchMandate() {
        String nonce = TestHelper.generatesBankAccountInstantVerificationNonce(usBankGateway);

        Calendar mandateAcceptedAt = Calendar.getInstance();
        mandateAcceptedAt.add(Calendar.MINUTE, -5);

        TransactionRequest transactionRequest = new TransactionRequest()
            .amount(new java.math.BigDecimal("12.34"))
            .paymentMethodNonce(nonce)
            .merchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT)
            .usBankAccount()
                .achMandateText("I authorize this transaction and future debits")
                .achMandateAcceptedAt(mandateAcceptedAt)
                .done()
            .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> transactionResult = usBankGateway.transaction().sale(transactionRequest);

        assertTrue(transactionResult.isSuccess(), "Expected transaction success but got failure with validation errors (see console output)");
        Transaction transaction = transactionResult.getTarget();

        assertTransactionMatches(transaction, new ExpectedTransaction()
            .withAmount(new java.math.BigDecimal("12.34"))
            .withUsBankAccountDetails(new ExpectedUsBankAccountDetails()
                .withAchMandate(new ExpectedAchMandate()
                    .withText("I authorize this transaction and future debits")
                    .withAcceptedAtNotNull())
                .withAccountHolderName("Dan Schulman")
                .withLast4("1234")
                .withRoutingNumber("021000021")
                .withAccountType("checking")));
    }

    @Test
    public void testInstantVerificationTokenizeAndCharge() {

        String nonce = TestHelper.generatesBankAccountInstantVerificationNonce(usBankGateway);

        Result<Customer> customerResult = usBankGateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        Calendar mandateAcceptedAt = Calendar.getInstance();
        mandateAcceptedAt.add(Calendar.MINUTE, -5);

        PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest()
            .customerId(customer.getId())
            .paymentMethodNonce(nonce)
            .usBankAccount()
                .achMandateText("I authorize this transaction and future debits")
                .achMandateAcceptedAt(mandateAcceptedAt)
                .done()
            .options()
                .verificationMerchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT)
                .usBankAccountVerificationMethod(UsBankAccountVerification.VerificationMethod.INSTANT_VERIFICATION_ACCOUNT_VALIDATION)
                .done();

        Result<? extends PaymentMethod> paymentMethodResult = usBankGateway.paymentMethod().create(paymentMethodRequest);
        assertTrue(paymentMethodResult.isSuccess(), "Expected payment method creation success but got failure with validation errors");

        UsBankAccount usBankAccount = (UsBankAccount) paymentMethodResult.getTarget();

        assertUsBankAccountMatches(usBankAccount, new ExpectedUsBankAccount()
            .withVerifications(new ExpectedVerification()
                .withVerificationMethod(UsBankAccountVerification.VerificationMethod.INSTANT_VERIFICATION_ACCOUNT_VALIDATION)
                .withStatus(UsBankAccountVerification.Status.VERIFIED))
            .withAchMandate(new ExpectedAchMandate()
                .withText("I authorize this transaction and future debits")
                .withAcceptedAtNotNull()));

        UsBankAccountVerification verification = usBankAccount.getVerifications().get(0);
        assertEquals(UsBankAccountVerification.VerificationMethod.INSTANT_VERIFICATION_ACCOUNT_VALIDATION, verification.getVerificationMethod());

        TransactionRequest transactionRequest = new TransactionRequest()
            .amount(new java.math.BigDecimal("12.34"))
            .paymentMethodToken(usBankAccount.getToken())
            .merchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT)
            .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> transactionResult = usBankGateway.transaction().sale(transactionRequest);
        assertTrue(transactionResult.isSuccess(), "Expected transaction success but got failure");
        Transaction transaction = transactionResult.getTarget();

        assertTransactionMatches(transaction, new ExpectedTransaction()
            .withAmount(new java.math.BigDecimal("12.34"))
            .withUsBankAccountDetails(new ExpectedUsBankAccountDetails()
                .withToken(usBankAccount.getToken())
                .withAchMandate(new ExpectedAchMandate()
                    .withText("I authorize this transaction and future debits")
                    .withAcceptedAtNotNull())
                .withLast4("1234")
                .withRoutingNumber("021000021")
                .withAccountType("checking")));
    }

    private void assertTransactionMatches(Transaction actual, ExpectedTransaction expected) {
        assertNotNull(actual.getId());
        if (expected.amount != null) {
            assertEquals(expected.amount, actual.getAmount());
        }
        if (expected.usBankAccountDetails != null) {
            assertNotNull(actual.getUsBankAccountDetails());
            assertUsBankAccountDetailsMatches(actual.getUsBankAccountDetails(), expected.usBankAccountDetails);
        }
    }

    private void assertUsBankAccountMatches(UsBankAccount actual, ExpectedUsBankAccount expected) {
        if (expected.verification != null) {
            assertNotNull(actual.getVerifications());
            assertFalse(actual.getVerifications().isEmpty());
            UsBankAccountVerification verification = actual.getVerifications().get(0);
            if (expected.verification.verificationMethod != null) {
                assertEquals(expected.verification.verificationMethod, verification.getVerificationMethod());
            }
            if (expected.verification.status != null) {
                assertEquals(expected.verification.status, verification.getStatus());
            }
        }
        if (expected.achMandate != null) {
            assertAchMandateMatches(actual.getAchMandate(), expected.achMandate);
        }
    }

    private void assertUsBankAccountDetailsMatches(UsBankAccountDetails actual, ExpectedUsBankAccountDetails expected) {
        if (expected.token != null) {
            assertEquals(expected.token, actual.getToken());
        }
        if (expected.achMandate != null) {
            assertAchMandateMatches(actual.getAchMandate(), expected.achMandate);
        }
        if (expected.accountHolderName != null) {
            assertEquals(expected.accountHolderName, actual.getAccountHolderName());
        }
        if (expected.last4 != null) {
            assertEquals(expected.last4, actual.getLast4());
        }
        if (expected.routingNumber != null) {
            assertEquals(expected.routingNumber, actual.getRoutingNumber());
        }
        if (expected.accountType != null) {
            assertEquals(expected.accountType, actual.getAccountType());
        }
    }

    private void assertAchMandateMatches(AchMandate actual, ExpectedAchMandate expected) {
        assertNotNull(actual);
        if (expected.text != null) {
            assertEquals(expected.text, actual.getText());
        }
        if (expected.acceptedAtNotNull) {
            assertNotNull(actual.getAcceptedAt());
        }
    }

    private static class ExpectedTransaction {
        private java.math.BigDecimal amount;
        private ExpectedUsBankAccountDetails usBankAccountDetails;

        public ExpectedTransaction withAmount(java.math.BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public ExpectedTransaction withUsBankAccountDetails(ExpectedUsBankAccountDetails usBankAccountDetails) {
            this.usBankAccountDetails = usBankAccountDetails;
            return this;
        }
    }

    private static class ExpectedUsBankAccount {
        private ExpectedVerification verification;
        private ExpectedAchMandate achMandate;

        public ExpectedUsBankAccount withVerifications(ExpectedVerification verification) {
            this.verification = verification;
            return this;
        }

        public ExpectedUsBankAccount withAchMandate(ExpectedAchMandate achMandate) {
            this.achMandate = achMandate;
            return this;
        }
    }

    private static class ExpectedUsBankAccountDetails {
        private String token;
        private ExpectedAchMandate achMandate;
        private String accountHolderName;
        private String last4;
        private String routingNumber;
        private String accountType;

        public ExpectedUsBankAccountDetails withToken(String token) {
            this.token = token;
            return this;
        }

        public ExpectedUsBankAccountDetails withAchMandate(ExpectedAchMandate achMandate) {
            this.achMandate = achMandate;
            return this;
        }

        public ExpectedUsBankAccountDetails withAccountHolderName(String accountHolderName) {
            this.accountHolderName = accountHolderName;
            return this;
        }

        public ExpectedUsBankAccountDetails withLast4(String last4) {
            this.last4 = last4;
            return this;
        }

        public ExpectedUsBankAccountDetails withRoutingNumber(String routingNumber) {
            this.routingNumber = routingNumber;
            return this;
        }

        public ExpectedUsBankAccountDetails withAccountType(String accountType) {
            this.accountType = accountType;
            return this;
        }
    }

    private static class ExpectedVerification {
        private UsBankAccountVerification.VerificationMethod verificationMethod;
        private UsBankAccountVerification.Status status;

        public ExpectedVerification withVerificationMethod(UsBankAccountVerification.VerificationMethod verificationMethod) {
            this.verificationMethod = verificationMethod;
            return this;
        }

        public ExpectedVerification withStatus(UsBankAccountVerification.Status status) {
            this.status = status;
            return this;
        }
    }

    private static class ExpectedAchMandate {
        private String text;
        private boolean acceptedAtNotNull;

        public ExpectedAchMandate withText(String text) {
            this.text = text;
            return this;
        }

        public ExpectedAchMandate withAcceptedAtNotNull() {
            this.acceptedAtNotNull = true;
            return this;
        }
    }
}