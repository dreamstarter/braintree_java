package com.braintreegateway;

import java.util.Calendar;

/**
 * A request builder for US Bank Account data used in payment method requests.
 */
public class PaymentMethodUsBankAccountRequest extends Request {
    private String achMandateText;
    private Calendar achMandateAcceptedAt;
    private PaymentMethodRequest parent;

    public PaymentMethodUsBankAccountRequest(PaymentMethodRequest parent) {
        this.parent = parent;
    }

    /**
     * Sets the ACH mandate text for authorization.
     *
     * @param achMandateText the ACH mandate text
     * @return the PaymentMethodUsBankAccountRequest
     */
    public PaymentMethodUsBankAccountRequest achMandateText(String achMandateText) {
        this.achMandateText = achMandateText;
        return this;
    }

    /**
     * Sets the timestamp when the ACH mandate was accepted.
     *
     * @param achMandateAcceptedAt the timestamp when the mandate was accepted
     * @return the PaymentMethodUsBankAccountRequest
     */
    public PaymentMethodUsBankAccountRequest achMandateAcceptedAt(Calendar achMandateAcceptedAt) {
        this.achMandateAcceptedAt = achMandateAcceptedAt;
        return this;
    }

    /**
     * Returns to the parent PaymentMethodRequest.
     *
     * @return the PaymentMethodRequest
     */
    public PaymentMethodRequest done() {
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

    public RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("achMandateText", achMandateText)
            .addElement("achMandateAcceptedAt", achMandateAcceptedAt);
    }
}