# AuthenticationApi

All URIs are relative to *https://demo.ziti.dev/edge/client/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**authenticate**](AuthenticationApi.md#authenticate) | **POST** /authenticate | Authenticate via a method supplied via a query string parameter |
| [**authenticateWithHttpInfo**](AuthenticationApi.md#authenticateWithHttpInfo) | **POST** /authenticate | Authenticate via a method supplied via a query string parameter |
| [**authenticateMfa**](AuthenticationApi.md#authenticateMfa) | **POST** /authenticate/mfa | Complete MFA authentication |
| [**authenticateMfaWithHttpInfo**](AuthenticationApi.md#authenticateMfaWithHttpInfo) | **POST** /authenticate/mfa | Complete MFA authentication |



## authenticate

> CompletableFuture<CurrentApiSessionDetailEnvelope> authenticate(method, auth)

Authenticate via a method supplied via a query string parameter

Allowed authentication methods include \&quot;password\&quot;, \&quot;cert\&quot;, and \&quot;ext-jwt\&quot; 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.AuthenticationApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        AuthenticationApi apiInstance = new AuthenticationApi(defaultClient);
        String method = "password"; // String | 
        Authenticate auth = new Authenticate(); // Authenticate | 
        try {
            CompletableFuture<CurrentApiSessionDetailEnvelope> result = apiInstance.authenticate(method, auth);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling AuthenticationApi#authenticate");
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
| **method** | **String**|  | [enum: password, cert, ext-jwt] |
| **auth** | [**Authenticate**](Authenticate.md)|  | [optional] |

### Return type

CompletableFuture<[**CurrentApiSessionDetailEnvelope**](CurrentApiSessionDetailEnvelope.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json, default

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The API session associated with the session used to issue the request |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **401** | The authentication request could not be processed as the credentials are invalid |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |

## authenticateWithHttpInfo

> CompletableFuture<ApiResponse<CurrentApiSessionDetailEnvelope>> authenticate authenticateWithHttpInfo(method, auth)

Authenticate via a method supplied via a query string parameter

Allowed authentication methods include \&quot;password\&quot;, \&quot;cert\&quot;, and \&quot;ext-jwt\&quot; 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.AuthenticationApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        AuthenticationApi apiInstance = new AuthenticationApi(defaultClient);
        String method = "password"; // String | 
        Authenticate auth = new Authenticate(); // Authenticate | 
        try {
            CompletableFuture<ApiResponse<CurrentApiSessionDetailEnvelope>> response = apiInstance.authenticateWithHttpInfo(method, auth);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling AuthenticationApi#authenticate");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling AuthenticationApi#authenticate");
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
| **method** | **String**|  | [enum: password, cert, ext-jwt] |
| **auth** | [**Authenticate**](Authenticate.md)|  | [optional] |

### Return type

CompletableFuture<ApiResponse<[**CurrentApiSessionDetailEnvelope**](CurrentApiSessionDetailEnvelope.md)>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json, default

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The API session associated with the session used to issue the request |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **401** | The authentication request could not be processed as the credentials are invalid |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |


## authenticateMfa

> CompletableFuture<Empty> authenticateMfa(mfaAuth)

Complete MFA authentication

Completes MFA authentication by submitting a MFA time based one time token or backup code.

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.auth.*;
import org.openziti.edge.models.*;
import org.openziti.edge.api.AuthenticationApi;
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

        AuthenticationApi apiInstance = new AuthenticationApi(defaultClient);
        MfaCode mfaAuth = new MfaCode(); // MfaCode | An MFA validation request
        try {
            CompletableFuture<Empty> result = apiInstance.authenticateMfa(mfaAuth);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling AuthenticationApi#authenticateMfa");
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
| **mfaAuth** | [**MfaCode**](MfaCode.md)| An MFA validation request | |

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
| **401** | Base empty response |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |

## authenticateMfaWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> authenticateMfa authenticateMfaWithHttpInfo(mfaAuth)

Complete MFA authentication

Completes MFA authentication by submitting a MFA time based one time token or backup code.

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.auth.*;
import org.openziti.edge.models.*;
import org.openziti.edge.api.AuthenticationApi;
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

        AuthenticationApi apiInstance = new AuthenticationApi(defaultClient);
        MfaCode mfaAuth = new MfaCode(); // MfaCode | An MFA validation request
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.authenticateMfaWithHttpInfo(mfaAuth);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling AuthenticationApi#authenticateMfa");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling AuthenticationApi#authenticateMfa");
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
| **mfaAuth** | [**MfaCode**](MfaCode.md)| An MFA validation request | |

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
| **401** | Base empty response |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |

