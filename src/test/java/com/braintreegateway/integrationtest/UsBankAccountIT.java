package com.braintreegateway.integrationtest;

import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;

import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.exceptions.NotFoundException;

import java.util.regex.Pattern;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsBankAccountIT extends IntegrationTest {

    @Test
    public void findsUsBankAccountByToken() {
        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway);
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce).
            options().
                verificationMerchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT).
            done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        UsBankAccount usBankAccount = gateway.usBankAccount().find(result.getTarget().getToken());
        assertNotNull(usBankAccount);
        AchMandate achMandate = usBankAccount.getAchMandate();
        assertNotNull(achMandate.getAcceptedAt());
        assertEquals("Dan Schulman", usBankAccount.getAccountHolderName());
        assertEquals("checking", usBankAccount.getAccountType());
        assertTrue(Pattern.matches(".*CHASE.*", usBankAccount.getBankName()));
        assertNull(usBankAccount.getBusinessName());
        assertNotNull(usBankAccount.getCreatedAt());
        assertEquals(customer.getId(), usBankAccount.getCustomerId());
        assertNull(usBankAccount.getDescription());
        assertEquals("Dan", usBankAccount.getFirstName());
        assertNotNull(usBankAccount.getImageUrl());
        assertEquals("1234", usBankAccount.getLast4());
        assertEquals("Schulman", usBankAccount.getLastName());
        assertNull(usBankAccount.getOwnerId());
        assertEquals("personal", usBankAccount.getOwnershipType());
        assertNull(usBankAccount.getPlaidVerifiedAt());
        assertEquals("021000021", usBankAccount.getRoutingNumber());
        assertNotNull(usBankAccount.getSubscriptions());
        assertNotNull(usBankAccount.getToken());
        assertNotNull(usBankAccount.getUpdatedAt());
        assertTrue(usBankAccount.getVerifications().size() >= 1);
        assertNotNull(usBankAccount.isDefault());
        assertNotNull(usBankAccount.isVerifiable());
        assertNotNull(usBankAccount.isVerified());
    }

    @Test
    public void findsUsBankAccountByTokenForBusiness() {
        String nonce = TestHelper.generateValidUsBankAccountNonceForBusiness(gateway);
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce).
            options().
                verificationMerchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT).
            done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        UsBankAccount usBankAccount = gateway.usBankAccount().find(result.getTarget().getToken());
        assertNotNull(usBankAccount);
        AchMandate achMandate = usBankAccount.getAchMandate();
        assertNotNull(achMandate.getAcceptedAt());
        assertEquals("Big Tech", usBankAccount.getAccountHolderName());
        assertEquals("checking", usBankAccount.getAccountType());
        assertTrue(Pattern.matches(".*CHASE.*", usBankAccount.getBankName()));
        assertEquals("Big Tech", usBankAccount.getBusinessName());
        assertNotNull(usBankAccount.getCreatedAt());
        assertEquals(customer.getId(), usBankAccount.getCustomerId());
        assertNull(usBankAccount.getDescription());
        assertNotNull(usBankAccount.getImageUrl());
        assertEquals("1234", usBankAccount.getLast4());
        assertNull(usBankAccount.getOwnerId());
        assertEquals("business", usBankAccount.getOwnershipType());
        assertNull(usBankAccount.getPlaidVerifiedAt());
        assertEquals("021000021", usBankAccount.getRoutingNumber());
        assertNotNull(usBankAccount.getSubscriptions().size());
        assertNotNull(usBankAccount.getToken());
        assertNotNull(usBankAccount.getUpdatedAt());
        assertTrue(usBankAccount.getVerifications().size() >= 1);
        assertNotNull(usBankAccount.isDefault());
        assertNotNull(usBankAccount.isVerifiable());
        assertNotNull(usBankAccount.isVerified());
    }

    @Test
    public void findThrowsNotFoundExceptionWhenUsBankAccountIsMissing() {
        try {
            gateway.usBankAccount().find(TestHelper.generateInvalidUsBankAccountNonce());
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void saleWithUsBankAccountByToken() {
        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway);
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce).
            options().
                verificationMerchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT).
            done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        TransactionRequest transactionRequest = new TransactionRequest()
            .merchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT)
            .amount(SandboxValues.TransactionAmount.AUTHORIZE.amount);

        Result<Transaction> transactionResult = gateway.usBankAccount().sale(result.getTarget().getToken(), transactionRequest);
        assertTrue(result.isSuccess());
        Transaction transaction = transactionResult.getTarget();
        assertNotNull(transaction);

        assertEquals(new BigDecimal("1000.00"), transaction.getAmount());
        assertEquals("USD", transaction.getCurrencyIsoCode());
        assertEquals(Transaction.Type.SALE, transaction.getType());
        assertEquals(Transaction.Status.SETTLEMENT_PENDING, transaction.getStatus());

        UsBankAccountDetails usBankAccountDetails = transaction.getUsBankAccountDetails();
        AchMandate achMandate = usBankAccountDetails.getAchMandate();
        assertNotNull(achMandate.getAcceptedAt());
        assertEquals("Dan Schulman", usBankAccountDetails.getAccountHolderName());
        assertEquals("checking", usBankAccountDetails.getAccountType());
        assertTrue(Pattern.matches(".*CHASE.*", usBankAccountDetails.getBankName()));
        assertNull(usBankAccountDetails.getBusinessName());
        assertEquals("Dan", usBankAccountDetails.getFirstName());
        assertNotNull(usBankAccountDetails.getGlobalId());
        assertNotNull(usBankAccountDetails.getImageUrl());
        assertEquals("1234", usBankAccountDetails.getLast4());
        assertEquals("Schulman", usBankAccountDetails.getLastName());
        assertEquals("personal", usBankAccountDetails.getOwnershipType());
        assertEquals("021000021", usBankAccountDetails.getRoutingNumber());
        assertNotNull(usBankAccountDetails.getToken());
        assertNotNull(usBankAccountDetails.isVerified());
    }

    @Test
    public void createTransactionWithAchMandate() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway);
        PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest()
            .customerId(customer.getId())
            .paymentMethodNonce(nonce)
            .options()
                .verificationMerchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT)
            .done();

        Result<? extends PaymentMethod> paymentMethodResult = gateway.paymentMethod().create(paymentMethodRequest);
        assertTrue(paymentMethodResult.isSuccess());

        UsBankAccount usBankAccount = (UsBankAccount) paymentMethodResult.getTarget();

        java.util.Calendar mandateAcceptedAt = java.util.Calendar.getInstance();
        mandateAcceptedAt.add(java.util.Calendar.MINUTE, -10);

        TransactionRequest transactionRequest = new TransactionRequest()
            .amount(new BigDecimal("100.00"))
            .paymentMethodToken(usBankAccount.getToken())
            .merchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT)
            .usBankAccount()
                .achMandateText("I authorize this ACH debit transaction for the amount shown above")
                .achMandateAcceptedAt(mandateAcceptedAt)
                .done()
            .options()
                .submitForSettlement(true)
            .done();

        Result<Transaction> result = gateway.transaction().sale(transactionRequest);

        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();
        
        assertNotNull(transaction.getId());
        assertEquals(new BigDecimal("100.00"), transaction.getAmount());
        assertNotNull(transaction.getUsBankAccountDetails());
        assertEquals(usBankAccount.getToken(), transaction.getUsBankAccountDetails().getToken());
    }

    @Test
    public void usBankAccountVerificationWithInstantVerificationMethod() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway);
        PaymentMethodRequest request = new PaymentMethodRequest()
                .customerId(customer.getId())
                .paymentMethodNonce(nonce)
                .options()
                .verificationMerchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT)
                .done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        UsBankAccount usBankAccount = (UsBankAccount) result.getTarget();
        assertNotNull(usBankAccount.getVerifications());
        assertFalse(usBankAccount.getVerifications().isEmpty());

        UsBankAccountVerification verification = usBankAccount.getVerifications().get(0);
        assertNotNull(verification.getVerificationMethod());
        assertEquals(
                UsBankAccountVerification.VerificationMethod.INDEPENDENT_CHECK,
                verification.getVerificationMethod()
        );
        assertEquals(
                UsBankAccountVerification.Status.VERIFIED,
                verification.getStatus()
        );
    }
}
