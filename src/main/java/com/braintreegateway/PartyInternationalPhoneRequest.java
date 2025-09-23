package com.braintreegateway;

public class PartyInternationalPhoneRequest extends AddressInternationalPhoneRequest {
    private String countryCode;
    private String nationalNumber;
    private PartyAddressRequest parent;
    
    public PartyInternationalPhoneRequest() {}
    

    public PartyInternationalPhoneRequest(PartyAddressRequest parent) {
        super();
        this.parent = parent;
    }

    public PartyAddressRequest done() {
        return parent;
    }

    public PartyInternationalPhoneRequest countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public PartyInternationalPhoneRequest nationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("internationalPhone").toXML();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("countryCode", countryCode)
            .addElement("nationalNumber", nationalNumber);
    }
}
