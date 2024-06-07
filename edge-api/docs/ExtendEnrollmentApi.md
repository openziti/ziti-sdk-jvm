# ExtendEnrollmentApi

All URIs are relative to *https://demo.ziti.dev/edge/client/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**extendCurrentIdentityAuthenticator**](ExtendEnrollmentApi.md#extendCurrentIdentityAuthenticator) | **POST** /current-identity/authenticators/{id}/extend | Allows the current identity to recieve a new certificate associated with a certificate based authenticator |
| [**extendCurrentIdentityAuthenticatorWithHttpInfo**](ExtendEnrollmentApi.md#extendCurrentIdentityAuthenticatorWithHttpInfo) | **POST** /current-identity/authenticators/{id}/extend | Allows the current identity to recieve a new certificate associated with a certificate based authenticator |
| [**extendRouterEnrollment**](ExtendEnrollmentApi.md#extendRouterEnrollment) | **POST** /enroll/extend/router | Extend the life of a currently enrolled router&#39;s certificates |
| [**extendRouterEnrollmentWithHttpInfo**](ExtendEnrollmentApi.md#extendRouterEnrollmentWithHttpInfo) | **POST** /enroll/extend/router | Extend the life of a currently enrolled router&#39;s certificates |
| [**extendVerifyCurrentIdentityAuthenticator**](ExtendEnrollmentApi.md#extendVerifyCurrentIdentityAuthenticator) | **POST** /current-identity/authenticators/{id}/extend-verify | Allows the current identity to validate reciept of a new client certificate |
| [**extendVerifyCurrentIdentityAuthenticatorWithHttpInfo**](ExtendEnrollmentApi.md#extendVerifyCurrentIdentityAuthenticatorWithHttpInfo) | **POST** /current-identity/authenticators/{id}/extend-verify | Allows the current identity to validate reciept of a new client certificate |



## extendCurrentIdentityAuthenticator

> CompletableFuture<IdentityExtendEnrollmentEnvelope> extendCurrentIdentityAuthenticator(id, extend)

Allows the current identity to recieve a new certificate associated with a certificate based authenticator

This endpoint only functions for certificates issued by the controller. 3rd party certificates are not handled. Allows an identity to extend its certificate&#39;s expiration date by using its current and valid client certificate to submit a CSR. This CSR may be passed in using a new private key, thus allowing private key rotation. The response from this endpoint is a new client certificate which the client must  be verified via the /authenticators/{id}/extend-verify endpoint. After verification is completion any new connections must be made with new certificate. Prior to verification the old client certificate remains active.

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.auth.*;
import org.openziti.edge.models.*;
import org.openziti.edge.api.ExtendEnrollmentApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        ExtendEnrollmentApi apiInstance = new ExtendEnrollmentApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        IdentityExtendEnrollmentRequest extend = new IdentityExtendEnrollmentRequest(); // IdentityExtendEnrollmentRequest | 
        try {
            CompletableFuture<IdentityExtendEnrollmentEnvelope> result = apiInstance.extendCurrentIdentityAuthenticator(id, extend);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling ExtendEnrollmentApi#extendCurrentIdentityAuthenticator");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **String**| The id of the requested resource | |
| **extend** | [**IdentityExtendEnrollmentRequest**](IdentityExtendEnrollmentRequest.md)|  | |

### Return type

CompletableFuture<[**IdentityExtendEnrollmentEnvelope**](IdentityExtendEnrollmentEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A response containg the identity&#39;s new certificate |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |

## extendCurrentIdentityAuthenticatorWithHttpInfo

> CompletableFuture<ApiResponse<IdentityExtendEnrollmentEnvelope>> extendCurrentIdentityAuthenticator extendCurrentIdentityAuthenticatorWithHttpInfo(id, extend)

Allows the current identity to recieve a new certificate associated with a certificate based authenticator

This endpoint only functions for certificates issued by the controller. 3rd party certificates are not handled. Allows an identity to extend its certificate&#39;s expiration date by using its current and valid client certificate to submit a CSR. This CSR may be passed in using a new private key, thus allowing private key rotation. The response from this endpoint is a new client certificate which the client must  be verified via the /authenticators/{id}/extend-verify endpoint. After verification is completion any new connections must be made with new certificate. Prior to verification the old client certificate remains active.

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.auth.*;
import org.openziti.edge.models.*;
import org.openziti.edge.api.ExtendEnrollmentApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        ExtendEnrollmentApi apiInstance = new ExtendEnrollmentApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        IdentityExtendEnrollmentRequest extend = new IdentityExtendEnrollmentRequest(); // IdentityExtendEnrollmentRequest | 
        try {
            CompletableFuture<ApiResponse<IdentityExtendEnrollmentEnvelope>> response = apiInstance.extendCurrentIdentityAuthenticatorWithHttpInfo(id, extend);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling ExtendEnrollmentApi#extendCurrentIdentityAuthenticator");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling ExtendEnrollmentApi#extendCurrentIdentityAuthenticator");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Response headers: " + e.getResponseHeaders());
            System.err.println("Reason: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **String**| The id of the requested resource | |
| **extend** | [**IdentityExtendEnrollmentRequest**](IdentityExtendEnrollmentRequest.md)|  | |

### Return type

CompletableFuture<ApiResponse<[**IdentityExtendEnrollmentEnvelope**](IdentityExtendEnrollmentEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A response containg the identity&#39;s new certificate |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |


## extendRouterEnrollment

> CompletableFuture<EnrollmentCertsEnvelope> extendRouterEnrollment(routerExtendEnrollmentRequest)

Extend the life of a currently enrolled router&#39;s certificates

Allows a router to extend its certificates&#39; expiration date by using its current and valid client certificate to submit a CSR. This CSR may be passed in using a new private key, thus allowing private key rotation or swapping.  After completion any new connections must be made with certificates returned from a 200 OK response. The previous client certificate is rendered invalid for use with the controller even if it has not expired.  This request must be made using the existing, valid, client certificate. 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.ExtendEnrollmentApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        ExtendEnrollmentApi apiInstance = new ExtendEnrollmentApi(defaultClient);
        RouterExtendEnrollmentRequest routerExtendEnrollmentRequest = new RouterExtendEnrollmentRequest(); // RouterExtendEnrollmentRequest | 
        try {
            CompletableFuture<EnrollmentCertsEnvelope> result = apiInstance.extendRouterEnrollment(routerExtendEnrollmentRequest);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling ExtendEnrollmentApi#extendRouterEnrollment");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **routerExtendEnrollmentRequest** | [**RouterExtendEnrollmentRequest**](RouterExtendEnrollmentRequest.md)|  | |

### Return type

CompletableFuture<[**EnrollmentCertsEnvelope**](EnrollmentCertsEnvelope.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A response containing the edge routers new signed certificates (server chain, server cert, CAs). |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |

## extendRouterEnrollmentWithHttpInfo

> CompletableFuture<ApiResponse<EnrollmentCertsEnvelope>> extendRouterEnrollment extendRouterEnrollmentWithHttpInfo(routerExtendEnrollmentRequest)

Extend the life of a currently enrolled router&#39;s certificates

Allows a router to extend its certificates&#39; expiration date by using its current and valid client certificate to submit a CSR. This CSR may be passed in using a new private key, thus allowing private key rotation or swapping.  After completion any new connections must be made with certificates returned from a 200 OK response. The previous client certificate is rendered invalid for use with the controller even if it has not expired.  This request must be made using the existing, valid, client certificate. 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.ExtendEnrollmentApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        ExtendEnrollmentApi apiInstance = new ExtendEnrollmentApi(defaultClient);
        RouterExtendEnrollmentRequest routerExtendEnrollmentRequest = new RouterExtendEnrollmentRequest(); // RouterExtendEnrollmentRequest | 
        try {
            CompletableFuture<ApiResponse<EnrollmentCertsEnvelope>> response = apiInstance.extendRouterEnrollmentWithHttpInfo(routerExtendEnrollmentRequest);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling ExtendEnrollmentApi#extendRouterEnrollment");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling ExtendEnrollmentApi#extendRouterEnrollment");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Response headers: " + e.getResponseHeaders());
            System.err.println("Reason: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **routerExtendEnrollmentRequest** | [**RouterExtendEnrollmentRequest**](RouterExtendEnrollmentRequest.md)|  | |

### Return type

CompletableFuture<ApiResponse<[**EnrollmentCertsEnvelope**](EnrollmentCertsEnvelope.md)>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A response containing the edge routers new signed certificates (server chain, server cert, CAs). |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |


## extendVerifyCurrentIdentityAuthenticator

> CompletableFuture<Empty> extendVerifyCurrentIdentityAuthenticator(id, extend)

Allows the current identity to validate reciept of a new client certificate

After submitting a CSR for a new client certificate the resulting public certificate must be re-submitted to this endpoint to verify receipt. After receipt, the new client certificate must be used for new authentication requests.

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.auth.*;
import org.openziti.edge.models.*;
import org.openziti.edge.api.ExtendEnrollmentApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        ExtendEnrollmentApi apiInstance = new ExtendEnrollmentApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        IdentityExtendValidateEnrollmentRequest extend = new IdentityExtendValidateEnrollmentRequest(); // IdentityExtendValidateEnrollmentRequest | 
        try {
            CompletableFuture<Empty> result = apiInstance.extendVerifyCurrentIdentityAuthenticator(id, extend);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling ExtendEnrollmentApi#extendVerifyCurrentIdentityAuthenticator");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **String**| The id of the requested resource | |
| **extend** | [**IdentityExtendValidateEnrollmentRequest**](IdentityExtendValidateEnrollmentRequest.md)|  | |

### Return type

CompletableFuture<[**Empty**](Empty.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Base empty response |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |

## extendVerifyCurrentIdentityAuthenticatorWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> extendVerifyCurrentIdentityAuthenticator extendVerifyCurrentIdentityAuthenticatorWithHttpInfo(id, extend)

Allows the current identity to validate reciept of a new client certificate

After submitting a CSR for a new client certificate the resulting public certificate must be re-submitted to this endpoint to verify receipt. After receipt, the new client certificate must be used for new authentication requests.

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.auth.*;
import org.openziti.edge.models.*;
import org.openziti.edge.api.ExtendEnrollmentApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        ExtendEnrollmentApi apiInstance = new ExtendEnrollmentApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        IdentityExtendValidateEnrollmentRequest extend = new IdentityExtendValidateEnrollmentRequest(); // IdentityExtendValidateEnrollmentRequest | 
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.extendVerifyCurrentIdentityAuthenticatorWithHttpInfo(id, extend);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling ExtendEnrollmentApi#extendVerifyCurrentIdentityAuthenticator");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling ExtendEnrollmentApi#extendVerifyCurrentIdentityAuthenticator");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Response headers: " + e.getResponseHeaders());
            System.err.println("Reason: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
}
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **String**| The id of the requested resource | |
| **extend** | [**IdentityExtendValidateEnrollmentRequest**](IdentityExtendValidateEnrollmentRequest.md)|  | |

### Return type

CompletableFuture<ApiResponse<[**Empty**](Empty.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Base empty response |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |

