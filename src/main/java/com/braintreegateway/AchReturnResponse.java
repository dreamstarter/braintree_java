package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public class AchReturnResponse {

    private String createdAt;
    private String reasonCode;
    private String rejectReason;

    public AchReturnResponse(NodeWrapper node) {
        createdAt = node.findString("created-at");
        reasonCode = node.findString("reason-code");
        rejectReason = node.findString("reject-reason");
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public String getRejectReason() {
        return rejectReason;
    }
}
