package com.braintreegateway;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides a fluent interface to build requests for creating Bank Account Instant Verification JWTs.
 */
public class BankAccountInstantVerificationJwtRequest extends Request {
    private String businessName;
    private String returnUrl;
    private String cancelUrl;

    /**
     * Sets the officially registered business name for the merchant.
     *
     * @param businessName the business name
     * @return the BankAccountInstantVerificationJwtRequest
     */
    public BankAccountInstantVerificationJwtRequest businessName(String businessName) {
        this.businessName = businessName;
        return this;
    }

    /**
     * Sets the URL to redirect the consumer after successful account selection.
     *
     * @param returnUrl the return URL
     * @return the BankAccountInstantVerificationJwtRequest
     */
    public BankAccountInstantVerificationJwtRequest returnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
        return this;
    }

    /**
     * Sets the URL to redirect the consumer upon cancellation of the Open Banking flow.
     *
     * @param cancelUrl the cancel URL
     * @return the BankAccountInstantVerificationJwtRequest
     */
    public BankAccountInstantVerificationJwtRequest cancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
        return this;
    }


    public String getBusinessName() {
        return businessName;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }


    @Override
    public Map<String, Object> toGraphQLVariables() {
        Map<String, Object> variables = new HashMap<>();
        Map<String, Object> input = new HashMap<>();

        if (businessName != null) {
            input.put("businessName", businessName);
        }
        if (returnUrl != null) {
            input.put("returnUrl", returnUrl);
        }
        if (cancelUrl != null) {
            input.put("cancelUrl", cancelUrl);
        }

        variables.put("input", input);
        return variables;
    }
}