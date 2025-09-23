package com.braintreegateway;

public class PartyRequest extends Request {
    private String accountReferenceNumber;
    private PartyAddressRequest address;
    private String firstName;
    private String lastName;
    private String taxId;
    private TransferRequest parent;
    private String tagName = "party";

    public PartyRequest(TransferRequest parent, String tagName) {
        this.parent = parent;
        this.tagName = tagName;
    }

    public PartyRequest(TransferRequest parent) {
        this.parent = parent;
    }
    
    public PartyRequest accountReferenceNumber(String accountReferenceNumber) {
       this.accountReferenceNumber = accountReferenceNumber; 
       return this; 
    }

    public PartyAddressRequest address() {
       address = new PartyAddressRequest(this); 
       return address; 
    }

    public PartyRequest firstName(String firstName) {
       this.firstName = firstName; 
       return this; 
    }
    
    public PartyRequest lastName(String lastName) { 
      this.lastName = lastName; 
      return this; 
    }

    public PartyRequest taxId(String taxId) {
       this.taxId = taxId; 
       return this; 
    }
    
    public TransferRequest done() { 
      return parent; 
    }

    @Override
    public String toXML() {
        return buildRequest(tagName).toXML();
    }

     @Override
    public String toQueryString() {
        return toQueryString(tagName);
    }
    
    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root);
        if (accountReferenceNumber != null) {
            builder.addElement("accountReferenceNumber", accountReferenceNumber);
        }
        if (address != null) {
            builder.addElement("address", address);
        }
        if (firstName != null) {
            builder.addElement("firstName", firstName);
        }
        if (lastName != null) {
            builder.addElement("lastName", lastName);
        }
        if (taxId != null) {
            builder.addElement("taxId", taxId);
        }
        return builder;
    }
}
