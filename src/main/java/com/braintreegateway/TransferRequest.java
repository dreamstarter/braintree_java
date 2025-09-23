package com.braintreegateway;

public class TransferRequest extends Request {

    private TransactionRequest parent;
    private PartyRequest receiver;
    private PartyRequest sender;
    private String type;

    public TransferRequest() {
    }

    public TransferRequest(TransactionRequest parent) {
        this.parent = parent;
    }

    public PartyRequest receiver() {
        receiver = new PartyRequest(this, "receiver");
        return receiver;
    }

    public PartyRequest sender() {
        sender = new PartyRequest(this, "sender");
        return sender;
    }

    public TransferRequest type(String type) {
        this.type = type;
        return this;
    }

    public TransactionRequest done() {
        return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("transfer").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("transfer");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        RequestBuilder builder = new RequestBuilder(root); 
        if (receiver != null) {
            builder.addElement("receiver", receiver);
        }
        if (sender != null) {
            builder.addElement("sender", sender);
        }
        if (type != null) {
            builder.addElement("type", type);
        }
        return builder;
    }
}
