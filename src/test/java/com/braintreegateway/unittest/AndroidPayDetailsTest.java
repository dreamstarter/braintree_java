package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.braintreegateway.AndroidPayDetails;
import com.braintreegateway.enums.Business;
import com.braintreegateway.enums.Consumer;
import com.braintreegateway.enums.Corporate;
import com.braintreegateway.enums.PrepaidReloadable;
import com.braintreegateway.enums.Purchase;
import com.braintreegateway.util.SimpleNodeWrapper;

public class AndroidPayDetailsTest {

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

        SimpleNodeWrapper androidPayDetailsNode = SimpleNodeWrapper.parse(builder.toString());
        AndroidPayDetails card = new AndroidPayDetails(androidPayDetailsNode);

        assertEquals(Business.NO, card.getBusiness());
        assertEquals(Consumer.NO, card.getConsumer());
        assertEquals(Corporate.NO, card.getCorporate());
        assertEquals(Purchase.NO, card.getPurchase());
        assertEquals("No", card.getPrepaid());
        assertEquals(PrepaidReloadable.NO, card.getPrepaidReloadable());
    }

    @Test
    public void testPaymentAccountReferencePopulatedWhenPresent() {
        String xml = "<android-pay-details>"
                     + "<virtual-card-last-4>1234</virtual-card-last-4>"
                     + "<payment-account-reference>V0010013019339005665779448477</payment-account-reference>"
                   + "</android-pay-details>";

        SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);
        AndroidPayDetails details = new AndroidPayDetails(node);

        assertEquals("V0010013019339005665779448477", details.getPaymentAccountReference());
    }

}
