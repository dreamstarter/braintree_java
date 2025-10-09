package com.braintreegateway;

import java.util.Calendar;

/**
 * Provides a fluent interface to build US bank account details for transactions.
 */
public class TransactionUsBankAccountRequest extends Request {
    private String achMandateText;
    private Calendar achMandateAcceptedAt;
    private TransactionRequest parent;

    public TransactionUsBankAccountRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    /**
     * Sets the ACH mandate text for Bank Account Instant Verification transactions.
     *
     * @param achMandateText the ACH mandate text
     * @return the TransactionUsBankAccountRequest
     */
    public TransactionUsBankAccountRequest achMandateText(String achMandateText) {
        this.achMandateText = achMandateText;
        return this;
    }

    /**
     * Sets the timestamp when the ACH mandate was accepted.
     *
     * @param achMandateAcceptedAt the timestamp when the mandate was accepted
     * @return the TransactionUsBankAccountRequest
     */
    public TransactionUsBankAccountRequest achMandateAcceptedAt(Calendar achMandateAcceptedAt) {
        this.achMandateAcceptedAt = achMandateAcceptedAt;
        return this;
    }

    /**
     * Returns control to the parent TransactionRequest.
     *
     * @return the parent TransactionRequest
     */
    public TransactionRequest done() {
        return parent;
    }

    public String getAchMandateText() {
        return achMandateText;
    }

    public Calendar getAchMandateAcceptedAt() {
        return achMandateAcceptedAt;
    }

    @Override
    public String toXML() {
        return buildRequest("usBankAccount").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("usBankAccount");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("achMandateText", achMandateText)
            .addElement("achMandateAcceptedAt", achMandateAcceptedAt);
    }
}