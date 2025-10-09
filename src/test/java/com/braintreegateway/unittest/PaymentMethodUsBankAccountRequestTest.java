package com.braintreegateway.unittest;

import com.braintreegateway.PaymentMethodRequest;
import com.braintreegateway.PaymentMethodUsBankAccountRequest;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentMethodUsBankAccountRequestTest {

    @Test
    public void toXmlIncludesAchMandateText() {
        PaymentMethodRequest request = new PaymentMethodRequest()
            .usBankAccount()
                .achMandateText("I authorize this ACH debit transaction")
                .done();

        String xml = request.toXML();
        TestHelper.assertIncludes("<usBankAccount>", xml);
        TestHelper.assertIncludes("<achMandateText>I authorize this ACH debit transaction</achMandateText>", xml);
    }

    @Test
    public void toXmlIncludesAchMandateAcceptedAt() {
        Calendar acceptedAt = Calendar.getInstance();
        acceptedAt.set(2024, Calendar.JANUARY, 15, 14, 30, 0);
        acceptedAt.set(Calendar.MILLISECOND, 0);

        PaymentMethodRequest request = new PaymentMethodRequest()
            .usBankAccount()
                .achMandateAcceptedAt(acceptedAt)
                .done();

        String xml = request.toXML();
        TestHelper.assertIncludes("<usBankAccount>", xml);
        TestHelper.assertIncludes("<achMandateAcceptedAt", xml);
    }

    @Test
    public void toXmlIncludesBothAchMandateFields() {
        Calendar acceptedAt = Calendar.getInstance();
        acceptedAt.set(2024, Calendar.JANUARY, 15, 14, 30, 0);
        acceptedAt.set(Calendar.MILLISECOND, 0);

        PaymentMethodRequest request = new PaymentMethodRequest()
            .usBankAccount()
                .achMandateText("I authorize this ACH debit transaction")
                .achMandateAcceptedAt(acceptedAt)
                .done();

        String xml = request.toXML();
        TestHelper.assertIncludes("<usBankAccount>", xml);
        TestHelper.assertIncludes("<achMandateText>I authorize this ACH debit transaction</achMandateText>", xml);
        TestHelper.assertIncludes("<achMandateAcceptedAt", xml);
    }

    @Test
    public void fluentInterfaceReturnsCorrectInstance() {
        Calendar acceptedAt = Calendar.getInstance();
        PaymentMethodRequest request = new PaymentMethodRequest();

        PaymentMethodRequest result = request.usBankAccount()
            .achMandateText("Test mandate")
            .achMandateAcceptedAt(acceptedAt)
            .done();

        assertSame(request, result);
    }

    @Test
    public void achMandateEscapesSpecialCharacters() {
        String mandateText = "I authorize this ACH debit & transaction <with> special \"characters\"";

        PaymentMethodRequest request = new PaymentMethodRequest()
            .usBankAccount()
                .achMandateText(mandateText)
                .done();

        String xml = request.toXML();
        TestHelper.assertIncludes("<usBankAccount>", xml);
        // XML should escape special characters
        TestHelper.assertIncludes("&amp;", xml);
        TestHelper.assertIncludes("&lt;", xml);
        TestHelper.assertIncludes("&gt;", xml);
        TestHelper.assertIncludes("&quot;", xml);
    }

    @Test
    public void gettersReturnCorrectValues() {
        Calendar acceptedAt = Calendar.getInstance();
        PaymentMethodUsBankAccountRequest usBankAccountRequest = new PaymentMethodRequest()
            .usBankAccount()
                .achMandateText("Test mandate text")
                .achMandateAcceptedAt(acceptedAt);

        assertEquals("Test mandate text", usBankAccountRequest.getAchMandateText());
        assertEquals(acceptedAt, usBankAccountRequest.getAchMandateAcceptedAt());
    }
}