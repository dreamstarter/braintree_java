package com.braintreegateway.unittest;

import com.braintreegateway.ApplePayDetails;
import com.braintreegateway.enums.Business;
import com.braintreegateway.enums.Consumer;
import com.braintreegateway.enums.Corporate;
import com.braintreegateway.enums.PrepaidReloadable;
import com.braintreegateway.enums.Purchase;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ApplePayDetailsTest {

    @Test
    public void testBinFields() {
        StringBuilder builder = new StringBuilder();

        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<transaction>");
        builder.append("<prepaid>No</prepaid>");
        builder.append("<prepaid-reloadable>No</prepaid-reloadable>");
        builder.append("<business>No</business>");
        builder.append("<consumer>No</consumer>");
        builder.append("<corporate>No</corporate>");
        builder.append("<purchase>No</purchase>");
        builder.append("</transaction>");

        SimpleNodeWrapper applePayDetailsNode = SimpleNodeWrapper.parse(builder.toString());
        ApplePayDetails card = new ApplePayDetails(applePayDetailsNode);

        assertEquals("No", card.getPrepaid());
        assertEquals(PrepaidReloadable.NO, card.getPrepaidReloadable());
        assertEquals(Business.NO, card.getBusiness());
        assertEquals(Consumer.NO, card.getConsumer());
        assertEquals(Corporate.NO, card.getCorporate());
        assertEquals(Purchase.NO, card.getPurchase());
    }

    @Test
    public void testPaymentAccountReferencePopulatedWhenPresent() {
        String xml = "<apple-pay-details>"
                     + "<last-4>1234</last-4>"
                     + "<payment-account-reference>V0010013019339005665779448477</payment-account-reference>"
                   + "</apple-pay-details>";

        SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);
        ApplePayDetails details = new ApplePayDetails(node);

        assertEquals("V0010013019339005665779448477", details.getPaymentAccountReference());
    }
}
