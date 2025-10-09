package com.braintreegateway;

import com.braintreegateway.exceptions.ServerException;
import com.braintreegateway.util.GraphQLClient;
import com.braintreegateway.util.Http;

import java.util.Map;

/**
 * Provides methods to interact with Bank Account Instant Verification functionality.
 */
public class BankAccountInstantVerificationGateway {

    private final GraphQLClient graphQLClient;

    private static final String CREATE_JWT_MUTATION =
        "mutation CreateBankAccountInstantVerificationJwt($input: CreateBankAccountInstantVerificationJwtInput!) { "
        + "createBankAccountInstantVerificationJwt(input: $input) {"
        + "    jwt"
        + "  }"
        + "}";

    public BankAccountInstantVerificationGateway(BraintreeGateway gateway, Http http, Configuration configuration, GraphQLClient graphQLClient) {
        this.graphQLClient = graphQLClient;
    }

    /**
     * Creates a Bank Account Instant Verification JWT for initiating the Open Banking flow.
     *
     * @param request the JWT creation request containing business name and redirect URLs
     * @return a {@link Result} containing the JWT
     */
    public Result<BankAccountInstantVerificationJwt> createJwt(BankAccountInstantVerificationJwtRequest request) {
        Map<String, Object> response = graphQLClient.query(CREATE_JWT_MUTATION, request);
        ValidationErrors errors = GraphQLClient.getErrors(response);

        if (errors != null) {
            return new Result<>(errors);
        }

        try {
            Map<String, Object> data = (Map<String, Object>) response.get("data");
            Map<String, Object> result = (Map<String, Object>) data.get("createBankAccountInstantVerificationJwt");
            String jwt = (String) result.get("jwt");

            BankAccountInstantVerificationJwt jwtObject = new BankAccountInstantVerificationJwt(jwt);
            return new Result<>(jwtObject);

        } catch (ClassCastException | NullPointerException e) {
            throw new ServerException("Couldn't parse response: " + e.getMessage());
        }
  }
}