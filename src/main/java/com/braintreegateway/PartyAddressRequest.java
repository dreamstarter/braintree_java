package com.braintreegateway;


public class PartyAddressRequest extends AddressRequest {
    
    private PartyInternationalPhoneRequest internationalPhoneRequest; 
    private PartyRequest parent; 

    public PartyAddressRequest(PartyRequest parent) {
        this.parent = parent;
        this.tagName = "address";
    }

    @Override
    public PartyAddressRequest countryCodeAlpha2(String countryCodeAlpha2) {
        super.countryCodeAlpha2(countryCodeAlpha2);
        return this;
    }

    @Override
    public PartyAddressRequest extendedAddress(String extendedAddress) {
        super.extendedAddress(extendedAddress);
        return this;
    }

    @Override
    public PartyAddressRequest locality(String locality) {
        super.locality(locality);
        return this;
    }

    @Override
    public PartyAddressRequest postalCode(String postalCode) {
        super.postalCode(postalCode);
        return this;
    }

    @Override
    public PartyAddressRequest region(String region) {
        super.region(region);
        return this;
    }

    @Override
    public PartyAddressRequest streetAddress(String streetAddress) {
        super.streetAddress(streetAddress);
        return this;
    } 

    @Override
    public PartyInternationalPhoneRequest internationalPhone() {
        internationalPhoneRequest = new PartyInternationalPhoneRequest(this);
        return this.internationalPhoneRequest;
    }

    public PartyRequest done() {
        return parent;
    }

    @Override
    protected RequestBuilder buildRequest(String root) {
        RequestBuilder requestBuilder = super.buildRequest(root);
        if (internationalPhoneRequest != null) {
            requestBuilder = requestBuilder.addElement("internationalPhone", internationalPhoneRequest);
        }
        return requestBuilder;
    }
}
