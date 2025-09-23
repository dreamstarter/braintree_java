package com.braintreegateway.integrationtest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationErrorCode;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;
import com.braintreegateway.testhelpers.TestHelper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StagedDigitalWalletOperatorTransactionIT extends IntegrationTest implements MerchantAccountTestConstants {

    private static final List<String> SDWO_SUPPORTED_TRANSFER_TYPES = Collections.unmodifiableList(Arrays.asList(
        "account_to_account", "boleto_ticket", "person_to_person", "wallet_transfer"
    ));
    
    @Test
    public void testTransactionWithTransferRequestForAllTransferTypesForSdwoMerchant() {
        for (String transferType : SDWO_SUPPORTED_TRANSFER_TYPES) {
            TransactionRequest request = TestHelper.sdwoTransactionWithTransferRequest(transferType, "card_processor_brl_sdwo");
            Result<Transaction> result = gateway.transaction().sale(request);
            assertTrue(result.isSuccess());
            Transaction transaction = result.getTarget();
            assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
        }
    }

     @Test
    public void testTransactionWithoutTransferTypeInTheRequestForSdwoMerchant() {
        TransactionRequest request = TestHelper.sdwoTransactionWithTransferRequest(null, "card_processor_brl_sdwo");
        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();
        assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
    }
    
    @Test
    public void testTransactionWithoutTransferRequestForAllTransferTypesForSdwoMerchant() {
        for (String transferType : SDWO_SUPPORTED_TRANSFER_TYPES) {
            TransactionRequest request = new TransactionRequest().
                amount(new BigDecimal("100.00")).
                merchantAccountId("card_processor_brl_sdwo").
                creditCard().
                    number("4111111111111111").
                    expirationDate("06/2026").
                    cvv("123").
                    done().
                descriptor().
                    name("companynme12*product1").
                    phone("1232344444").
                    url("example.com").
                    done().
                billingAddress(). 
                    firstName("Bob James").
                    countryCodeAlpha2("CA").
                    extendedAddress("").
                    locality("Trois-Rivires").
                    region("QC").
                    postalCode("G8Y 156").
                    streetAddress("2346 Boul Lane").
                    done().    
                options().
                    storeInVaultOnSuccess(true).
                    done();
            Result<Transaction> result = gateway.transaction().sale(request);
            assertFalse(result.isSuccess());
            assertEquals(ValidationErrorCode.TRANSACTION_TRANSFER_DETAILS_ARE_MANDATORY,
                         result.getErrors().getAllDeepValidationErrors().get(0).getCode());
        }
    }

    @Test
    public void testTransactionWithInvalidTransferTypeRequestForSdwoMerchant() {
        TransactionRequest request = TestHelper.sdwoTransactionWithTransferRequest("invalid_transfer_type", "card_processor_brl_sdwo");
        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_TRANSFER_TYPE_IS_INVALID,
                        result.getErrors().getAllDeepValidationErrors().get(0).getCode());
    }

    @Test
    public void testTransactionWithFundTransferRequestForSdwoMerchant() {
        TransactionRequest request = TestHelper.sdwoTransactionWithTransferRequest("fund_transfer", "card_processor_brl_sdwo");
        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_TRANSFER_TYPE_IS_INVALID,
                        result.getErrors().getAllDeepValidationErrors().get(0).getCode());
    }
    
}
