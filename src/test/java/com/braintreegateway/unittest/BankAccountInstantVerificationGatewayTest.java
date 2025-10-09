package com.braintreegateway.unittest;

import com.braintreegateway.*;
import com.braintreegateway.util.GraphQLClient;
import com.braintreegateway.util.Http;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BankAccountInstantVerificationGatewayTest {

    @Mock
    private BraintreeGateway mockGateway;
    @Mock
    private Http mockHttp;
    @Mock
    private Configuration mockConfiguration;
    @Mock
    private GraphQLClient mockGraphQLClient;

    private BankAccountInstantVerificationGateway gateway;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        gateway = new BankAccountInstantVerificationGateway(mockGateway, mockHttp, mockConfiguration, mockGraphQLClient);
    }

    @Test
    public void createJwtSuccess() {
        BankAccountInstantVerificationJwtRequest request = new BankAccountInstantVerificationJwtRequest()
            .businessName("Test Business")
            .returnUrl("https://example.com/success")
            .cancelUrl("https://example.com/cancel");

        Map<String, Object> mockResponse = createSuccessfulJwtResponse();
        when(mockGraphQLClient.query(anyString(), eq(request))).thenReturn(mockResponse);

        Result<BankAccountInstantVerificationJwt> result = gateway.createJwt(request);

        assertTrue(result.isSuccess());
        assertNotNull(result.getTarget());
        assertEquals("test-jwt-token", result.getTarget().getJwt());
    }

    @Test
    public void createJwtWithValidationErrors() {
        BankAccountInstantVerificationJwtRequest request = new BankAccountInstantVerificationJwtRequest()
            .businessName("")
            .returnUrl("invalid-url");

        Map<String, Object> mockResponse = createErrorResponse();
        when(mockGraphQLClient.query(anyString(), eq(request))).thenReturn(mockResponse);

        Result<BankAccountInstantVerificationJwt> result = gateway.createJwt(request);

        assertFalse(result.isSuccess());
        assertNotNull(result.getErrors());
    }


    @Test
    public void createJwtGraphQLQueryUsesCorrectMutation() {
        BankAccountInstantVerificationJwtRequest request = new BankAccountInstantVerificationJwtRequest()
            .businessName("Test Business")
            .returnUrl("https://example.com/success");

        Map<String, Object> mockResponse = createSuccessfulJwtResponse();
        when(mockGraphQLClient.query(anyString(), eq(request))).thenReturn(mockResponse);

        gateway.createJwt(request);

        verify(mockGraphQLClient).query(
            argThat(query ->
                query.contains("mutation CreateBankAccountInstantVerificationJwt") &&
                query.contains("createBankAccountInstantVerificationJwt(input: $input)")
            ),
            eq(request)
        );
    }

    @Test
    public void createJwtMinimalRequest() {
        BankAccountInstantVerificationJwtRequest request = new BankAccountInstantVerificationJwtRequest()
            .businessName("Test Business")
            .returnUrl("https://example.com/success");

        Map<String, Object> mockResponse = createSuccessfulJwtResponse();
        when(mockGraphQLClient.query(anyString(), eq(request))).thenReturn(mockResponse);

        Result<BankAccountInstantVerificationJwt> result = gateway.createJwt(request);

        assertTrue(result.isSuccess());
        assertEquals("test-jwt-token", result.getTarget().getJwt());
    }

    private Map<String, Object> createSuccessfulJwtResponse() {
        Map<String, Object> jwtData = new HashMap<>();
        jwtData.put("jwt", "test-jwt-token");

        Map<String, Object> data = new HashMap<>();
        data.put("createBankAccountInstantVerificationJwt", jwtData);

        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        
        return response;
    }

    private Map<String, Object> createErrorResponse() {
        Map<String, Object> error = new HashMap<>();
        error.put("message", "Validation error");
        error.put("extensions", new HashMap<>());

        Map<String, Object> response = new HashMap<>();
        response.put("errors", Collections.singletonList(error));
        
        return response;
    }
    
    @Test
    public void toGraphQLVariablesIncludesAllFields() {
        BankAccountInstantVerificationJwtRequest request = new BankAccountInstantVerificationJwtRequest()
            .businessName("Test Business")
            .returnUrl("https://example.com/success")
            .cancelUrl("https://example.com/cancel");

        Map<String, Object> variables = request.toGraphQLVariables();

        assertNotNull(variables);
        assertTrue(variables.containsKey("input"));

        @SuppressWarnings("unchecked")
        Map<String, Object> input = (Map<String, Object>) variables.get("input");

        assertEquals("Test Business", input.get("businessName"));
        assertEquals("https://example.com/success", input.get("returnUrl"));
        assertEquals("https://example.com/cancel", input.get("cancelUrl"));
    }

    @Test
    public void toGraphQLVariablesOnlyIncludesNonNullFields() {
        BankAccountInstantVerificationJwtRequest request = new BankAccountInstantVerificationJwtRequest()
            .businessName("Test Business")
            .returnUrl("https://example.com/success");

        Map<String, Object> variables = request.toGraphQLVariables();

        @SuppressWarnings("unchecked")
        Map<String, Object> input = (Map<String, Object>) variables.get("input");
        
        assertEquals("Test Business", input.get("businessName"));
        assertEquals("https://example.com/success", input.get("returnUrl"));
        assertFalse(input.containsKey("cancelUrl"));
    }
}