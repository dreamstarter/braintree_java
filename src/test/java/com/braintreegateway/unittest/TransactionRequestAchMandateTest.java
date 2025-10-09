package com.braintreegateway.unittest;

import com.braintreegateway.TransactionRequest;
import com.braintreegateway.testhelpers.TestHelper;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

public class TransactionRequestAchMandateTest {

    @Test
    public void toXmlIncludesAchMandate() {
        TransactionRequest request = new TransactionRequest()
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

        TransactionRequest request = new TransactionRequest()
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

        TransactionRequest request = new TransactionRequest()
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
        TransactionRequest request = new TransactionRequest();
        Calendar acceptedAt = Calendar.getInstance();

        TransactionRequest result = request.usBankAccount()
            .achMandateText("Test mandate")
            .achMandateAcceptedAt(acceptedAt)
            .done();

        assertSame(request, result);
    }

    @Test
    public void toXmlOmitsNullAchMandateFields() {
        TransactionRequest request = new TransactionRequest()
            .usBankAccount()
                .achMandateText(null)
                .achMandateAcceptedAt(null)
                .done();

        String xml = request.toXML();
        assertFalse(xml.contains("<achMandateText>"));
        assertFalse(xml.contains("<achMandateAcceptedAt>"));
    }

    @Test
    public void achMandateEscapesSpecialCharacters() {
        TransactionRequest request = new TransactionRequest()
            .usBankAccount()
                .achMandateText("I authorize this ACH debit & transaction <with> special \"characters\"")
                .done();

        String xml = request.toXML();
        TestHelper.assertIncludes("<achMandateText>I authorize this ACH debit &amp; transaction &lt;with&gt; special &quot;characters&quot;</achMandateText>", xml);
    }
}