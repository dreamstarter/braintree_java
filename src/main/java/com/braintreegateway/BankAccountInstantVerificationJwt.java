package com.braintreegateway;

/**
 * Represents a Bank Account Instant Verification JWT.
 */
public class BankAccountInstantVerificationJwt {
    private final String jwt;

    public BankAccountInstantVerificationJwt(String jwt) {
        this.jwt = jwt;
    }

    /**
     * Returns the JWT for Bank Account Instant Verification.
     *
     * @return the JWT as a string
     */
    public String getJwt() {
        return jwt;
    }

}