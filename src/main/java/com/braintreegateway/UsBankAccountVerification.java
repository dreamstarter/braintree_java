package com.braintreegateway;

import com.braintreegateway.Transaction.GatewayRejectionReason;
import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;
import java.util.Calendar;

public class UsBankAccountVerification {

    public enum Status {
        FAILED("failed"),
        GATEWAY_REJECTED("gateway_rejected"),
        PENDING("pending"),
        PROCESSOR_DECLINED("processor_declined"),
        UNRECOGNIZED("unrecognized"),
        VERIFIED("verified");

        private final String name;

        Status(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum VerificationMethod {
        INDEPENDENT_CHECK("independent_check"),
        INSTANT_VERIFICATION_ACCOUNT_VALIDATION("instant_verification_account_validation"),
        MICRO_TRANSFERS("micro_transfers"),
        NETWORK_CHECK("network_check"),
        TOKENIZED_CHECK("tokenized_check"),
        UNRECOGNIZED("unrecognized");

        private final String name;

        VerificationMethod(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public static VerificationMethod fromString(String value) {
            if (value == null) {
                return UNRECOGNIZED;
            }
            for (VerificationMethod method : values()) {
                if (method.name.equals(value)) {
                    return method;
                }
            }
            return UNRECOGNIZED;
        }
    }

    public enum VerificationAddOns {
        CUSTOMER_VERIFICATION("customer_verification"),
        UNRECOGNIZED("unrecognized");

        private final String name;

        VerificationAddOns(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }


    private Calendar createdAt;
    private Calendar verificationDeterminedAt;
    private GatewayRejectionReason gatewayRejectionReason;
    private Status status;
    private String additionalProcessorResponse;
    private String id;
    private String processorResponseCode;
    private String processorResponseText;
    private UsBankAccount usBankAccount;
    private VerificationAddOns verificationAddOns;
    private VerificationMethod verificationMethod;

    public UsBankAccountVerification(NodeWrapper node) {
        this.id = node.findString("id");
        this.status = EnumUtils.findByName(Status.class, node.findString("status"), Status.UNRECOGNIZED);
        this.verificationMethod = VerificationMethod.fromString(node.findString("verification-method"));
        this.verificationAddOns = EnumUtils.findByName(
            VerificationAddOns.class,
            node.findString("verification-add-ons"),
            VerificationAddOns.UNRECOGNIZED);

        this.processorResponseCode = node.findString("processor-response-code");
        this.additionalProcessorResponse = node.findString("additional-processor-response");
        this.processorResponseText = node.findString("processor-response-text");
        this.createdAt = node.findDateTime("created-at");
        this.verificationDeterminedAt = node.findDateTime("verification-determined-at");
        this.gatewayRejectionReason = EnumUtils.findByName(GatewayRejectionReason.class, node.findString("gateway-rejection-reason"), GatewayRejectionReason.UNRECOGNIZED);
        NodeWrapper usBankAccountNode = node.findFirst("us-bank-account");
        if (usBankAccountNode != null) {
            this.usBankAccount = new UsBankAccount(usBankAccountNode);
        }
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public GatewayRejectionReason getGatewayRejectionReason() {
        return gatewayRejectionReason;
    }

    public VerificationMethod getVerificationMethod() {
        return verificationMethod;
    }

    public VerificationAddOns getVerificationAddOns() {
        return verificationAddOns;
    }

    public Calendar getVerificationDeterminedAt() {
        return verificationDeterminedAt;
    }

    public String getProcessorResponseCode() {
        return processorResponseCode;
    }

    public String getAdditionalProcessorResponse() {
        return additionalProcessorResponse;
    }

    public String getProcessorResponseText() {
        return processorResponseText;
    }

    public Status getStatus() {
        return status;
    }

    public UsBankAccount getUsBankAccount() {
        return usBankAccount;
    }
}
