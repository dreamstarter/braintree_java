package com.braintreegateway.unittest;

import java.io.IOException;

import com.braintreegateway.TransferRequest;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

public class StagedDigitalWalletOperatorTransactionRequestTest {

    @Test
    public void toXmlIncludesAllElements() throws IOException, SAXException {
        String[] transferTypes = {"account_to_account", "boleto_ticket", "person_to_person", "wallet_transfer"};
        for (String transferType : transferTypes) {
            TransferRequest transferRequest = new TransferRequest();

            transferRequest
                .type(transferType)
                .sender()
                    .firstName("Alice")
                    .lastName("Silva")
                    .accountReferenceNumber("1000012345")
                    .taxId("12345678900")
                    .address()
                        .streetAddress("Rua das Flores, 100")
                        .extendedAddress("2B")
                        .locality("São Paulo")
                        .region("SP")
                        .postalCode("01001-000")
                        .countryCodeAlpha2("BR")
                        .internationalPhone()
                            .countryCode("55")
                            .nationalNumber("1234567890")
                            .done()
                        .done()
                    .done()
                .receiver()
                    .firstName("Bob")
                    .lastName("Souza")
                    .accountReferenceNumber("2000012345")
                    .taxId("98765432100")
                    .address()
                        .streetAddress("Avenida Brasil, 200")
                        .extendedAddress("2B")
                        .locality("Rio de Janeiro")
                        .region("RJ")
                        .postalCode("20040-002")
                        .countryCodeAlpha2("BR")
                        .internationalPhone()
                            .countryCode("55")
                            .nationalNumber("9876543210")
                            .done()
                        .done()
                    .done();

            String expectedXML =
                    "<transfer>" +
                        "<type>" + transferType + "</type>" +
                        "<sender>" +
                            "<firstName>Alice</firstName>" +
                            "<lastName>Silva</lastName>" +
                            "<accountReferenceNumber>1000012345</accountReferenceNumber>" +
                            "<taxId>12345678900</taxId>" +
                            "<address>" +
                                "<countryCodeAlpha2>BR</countryCodeAlpha2>" +
                                "<locality>São Paulo</locality>" +
                                "<postalCode>01001-000</postalCode>" +
                                "<region>SP</region>" +
                                "<streetAddress>Rua das Flores, 100</streetAddress>" +
                                "<extendedAddress>2B</extendedAddress>" +
                                "<internationalPhone>" +
                                    "<countryCode>55</countryCode>" +
                                    "<nationalNumber>1234567890</nationalNumber>" +
                                "</internationalPhone>" +
                            "</address>" +
                        "</sender>" +
                        "<receiver>" +
                            "<firstName>Bob</firstName>" +
                            "<lastName>Souza</lastName>" +
                            "<accountReferenceNumber>2000012345</accountReferenceNumber>" +
                            "<taxId>98765432100</taxId>" +
                            "<address>" +
                                "<countryCodeAlpha2>BR</countryCodeAlpha2>" +
                                "<locality>Rio de Janeiro</locality>" +
                                "<postalCode>20040-002</postalCode>" +
                                "<region>RJ</region>" +
                                "<streetAddress>Avenida Brasil, 200</streetAddress>" +
                                "<extendedAddress>2B</extendedAddress>" +
                                "<internationalPhone>" +
                                    "<countryCode>55</countryCode>" +
                                    "<nationalNumber>9876543210</nationalNumber>" +
                                "</internationalPhone>" +
                            "</address>" +
                        "</receiver>" +
                    "</transfer>";

            XMLUnit.setIgnoreWhitespace(true);
            XMLAssert.assertXMLEqual(expectedXML, transferRequest.toXML());
        }
    }
}
