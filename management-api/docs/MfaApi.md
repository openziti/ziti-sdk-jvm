# MfaApi

All URIs are relative to *https://demo.ziti.dev/edge/management/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**authenticateMfa**](MfaApi.md#authenticateMfa) | **POST** /authenticate/mfa | Complete MFA authentication |
| [**authenticateMfaWithHttpInfo**](MfaApi.md#authenticateMfaWithHttpInfo) | **POST** /authenticate/mfa | Complete MFA authentication |
| [**createMfaRecoveryCodes**](MfaApi.md#createMfaRecoveryCodes) | **POST** /current-identity/mfa/recovery-codes | For a completed MFA enrollment regenerate the recovery codes |
| [**createMfaRecoveryCodesWithHttpInfo**](MfaApi.md#createMfaRecoveryCodesWithHttpInfo) | **POST** /current-identity/mfa/recovery-codes | For a completed MFA enrollment regenerate the recovery codes |
| [**deleteMfa**](MfaApi.md#deleteMfa) | **DELETE** /current-identity/mfa | Disable MFA for the current identity |
| [**deleteMfaWithHttpInfo**](MfaApi.md#deleteMfaWithHttpInfo) | **DELETE** /current-identity/mfa | Disable MFA for the current identity |
| [**detailMfa**](MfaApi.md#detailMfa) | **GET** /current-identity/mfa | Returns the current status of MFA enrollment |
| [**detailMfaWithHttpInfo**](MfaApi.md#detailMfaWithHttpInfo) | **GET** /current-identity/mfa | Returns the current status of MFA enrollment |
| [**detailMfaQrCode**](MfaApi.md#detailMfaQrCode) | **GET** /current-identity/mfa/qr-code | Show a QR code for unverified MFA enrollments |
| [**detailMfaQrCodeWithHttpInfo**](MfaApi.md#detailMfaQrCodeWithHttpInfo) | **GET** /current-identity/mfa/qr-code | Show a QR code for unverified MFA enrollments |
| [**detailMfaRecoveryCodes**](MfaApi.md#detailMfaRecoveryCodes) | **GET** /current-identity/mfa/recovery-codes | For a completed MFA enrollment view the current recovery codes |
| [**detailMfaRecoveryCodesWithHttpInfo**](MfaApi.md#detailMfaRecoveryCodesWithHttpInfo) | **GET** /current-identity/mfa/recovery-codes | For a completed MFA enrollment view the current recovery codes |
| [**enrollMfa**](MfaApi.md#enrollMfa) | **POST** /current-identity/mfa | Initiate MFA enrollment |
| [**enrollMfaWithHttpInfo**](MfaApi.md#enrollMfaWithHttpInfo) | **POST** /current-identity/mfa | Initiate MFA enrollment |
| [**removeIdentityMfa**](MfaApi.md#removeIdentityMfa) | **DELETE** /identities/{id}/mfa | Remove MFA from an identitity |
| [**removeIdentityMfaWithHttpInfo**](MfaApi.md#removeIdentityMfaWithHttpInfo) | **DELETE** /identities/{id}/mfa | Remove MFA from an identitity |
| [**verifyMfa**](MfaApi.md#verifyMfa) | **POST** /current-identity/mfa/verify | Complete MFA enrollment by verifying a time based one time token |
| [**verifyMfaWithHttpInfo**](MfaApi.md#verifyMfaWithHttpInfo) | **POST** /current-identity/mfa/verify | Complete MFA enrollment by verifying a time based one time token |



## authenticateMfa

> CompletableFuture<Empty> authenticateMfa(mfaAuth)

Complete MFA authentication

Completes MFA authentication by submitting a MFA time based one time token or backup code.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.MfaApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        MfaApi apiInstance = new MfaApi(defaultClient);
        MfaCode mfaAuth = new MfaCode(); // MfaCode | An MFA validation request
        try {
            CompletableFuture<Empty> result = apiInstance.authenticateMfa(mfaAuth);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling MfaApi#authenticateMfa");
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
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.MfaApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        MfaApi apiInstance = new MfaApi(defaultClient);
        MfaCode mfaAuth = new MfaCode(); // MfaCode | An MFA validation request
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.authenticateMfaWithHttpInfo(mfaAuth);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling MfaApi#authenticateMfa");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling MfaApi#authenticateMfa");
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


## createMfaRecoveryCodes

> CompletableFuture<Empty> createMfaRecoveryCodes(mfaValidation)

For a completed MFA enrollment regenerate the recovery codes

Allows regeneration of recovery codes of an MFA enrollment. Requires a current valid time based one time password to interact with. Available after a completed MFA enrollment. This replaces all existing recovery codes. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.MfaApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        MfaApi apiInstance = new MfaApi(defaultClient);
        MfaCode mfaValidation = new MfaCode(); // MfaCode | An MFA validation request
        try {
            CompletableFuture<Empty> result = apiInstance.createMfaRecoveryCodes(mfaValidation);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling MfaApi#createMfaRecoveryCodes");
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
| **mfaValidation** | [**MfaCode**](MfaCode.md)| An MFA validation request | |

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
| **404** | The requested resource does not exist |  -  |

## createMfaRecoveryCodesWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> createMfaRecoveryCodes createMfaRecoveryCodesWithHttpInfo(mfaValidation)

For a completed MFA enrollment regenerate the recovery codes

Allows regeneration of recovery codes of an MFA enrollment. Requires a current valid time based one time password to interact with. Available after a completed MFA enrollment. This replaces all existing recovery codes. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.MfaApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        MfaApi apiInstance = new MfaApi(defaultClient);
        MfaCode mfaValidation = new MfaCode(); // MfaCode | An MFA validation request
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.createMfaRecoveryCodesWithHttpInfo(mfaValidation);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling MfaApi#createMfaRecoveryCodes");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling MfaApi#createMfaRecoveryCodes");
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
| **mfaValidation** | [**MfaCode**](MfaCode.md)| An MFA validation request | |

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
| **404** | The requested resource does not exist |  -  |


## deleteMfa

> CompletableFuture<Empty> deleteMfa(mfaValidationCode)

Disable MFA for the current identity

Disable MFA for the current identity. Requires a current valid time based one time password if MFA enrollment has been completed. If not, code should be an empty string. If one time passwords are not available and admin account can be used to remove MFA from the identity via &#x60;DELETE /identities/&lt;id&gt;/mfa&#x60;. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.MfaApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        MfaApi apiInstance = new MfaApi(defaultClient);
        String mfaValidationCode = "mfaValidationCode_example"; // String | 
        try {
            CompletableFuture<Empty> result = apiInstance.deleteMfa(mfaValidationCode);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling MfaApi#deleteMfa");
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
| **mfaValidationCode** | **String**|  | [optional] |

### Return type

CompletableFuture<[**Empty**](Empty.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Base empty response |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |

## deleteMfaWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> deleteMfa deleteMfaWithHttpInfo(mfaValidationCode)

Disable MFA for the current identity

Disable MFA for the current identity. Requires a current valid time based one time password if MFA enrollment has been completed. If not, code should be an empty string. If one time passwords are not available and admin account can be used to remove MFA from the identity via &#x60;DELETE /identities/&lt;id&gt;/mfa&#x60;. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.MfaApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        MfaApi apiInstance = new MfaApi(defaultClient);
        String mfaValidationCode = "mfaValidationCode_example"; // String | 
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.deleteMfaWithHttpInfo(mfaValidationCode);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling MfaApi#deleteMfa");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling MfaApi#deleteMfa");
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
| **mfaValidationCode** | **String**|  | [optional] |

### Return type

CompletableFuture<ApiResponse<[**Empty**](Empty.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Base empty response |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |


## detailMfa

> CompletableFuture<DetailMfaEnvelope> detailMfa()

Returns the current status of MFA enrollment

Returns details about the current MFA enrollment. If enrollment has not been completed it will return the current MFA configuration details necessary to complete a &#x60;POST /current-identity/mfa/verify&#x60;. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.MfaApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        MfaApi apiInstance = new MfaApi(defaultClient);
        try {
            CompletableFuture<DetailMfaEnvelope> result = apiInstance.detailMfa();
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling MfaApi#detailMfa");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

CompletableFuture<[**DetailMfaEnvelope**](DetailMfaEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The details of an MFA enrollment |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |

## detailMfaWithHttpInfo

> CompletableFuture<ApiResponse<DetailMfaEnvelope>> detailMfa detailMfaWithHttpInfo()

Returns the current status of MFA enrollment

Returns details about the current MFA enrollment. If enrollment has not been completed it will return the current MFA configuration details necessary to complete a &#x60;POST /current-identity/mfa/verify&#x60;. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.MfaApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        MfaApi apiInstance = new MfaApi(defaultClient);
        try {
            CompletableFuture<ApiResponse<DetailMfaEnvelope>> response = apiInstance.detailMfaWithHttpInfo();
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling MfaApi#detailMfa");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling MfaApi#detailMfa");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Response headers: " + e.getResponseHeaders());
            System.err.println("Reason: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

CompletableFuture<ApiResponse<[**DetailMfaEnvelope**](DetailMfaEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The details of an MFA enrollment |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |


## detailMfaQrCode

> CompletableFuture<Void> detailMfaQrCode()

Show a QR code for unverified MFA enrollments

Shows an QR code image for unverified MFA enrollments. 404s if the MFA enrollment has been completed or not started. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.MfaApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        MfaApi apiInstance = new MfaApi(defaultClient);
        try {
            CompletableFuture<Void> result = apiInstance.detailMfaQrCode();
        } catch (ApiException e) {
            System.err.println("Exception when calling MfaApi#detailMfaQrCode");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type


CompletableFuture<void> (empty response body)

### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **404** | No MFA enrollment or MFA enrollment is completed |  -  |

## detailMfaQrCodeWithHttpInfo

> CompletableFuture<ApiResponse<Void>> detailMfaQrCode detailMfaQrCodeWithHttpInfo()

Show a QR code for unverified MFA enrollments

Shows an QR code image for unverified MFA enrollments. 404s if the MFA enrollment has been completed or not started. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.MfaApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        MfaApi apiInstance = new MfaApi(defaultClient);
        try {
            CompletableFuture<ApiResponse<Void>> response = apiInstance.detailMfaQrCodeWithHttpInfo();
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling MfaApi#detailMfaQrCode");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling MfaApi#detailMfaQrCode");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Response headers: " + e.getResponseHeaders());
            System.err.println("Reason: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type


CompletableFuture<ApiResponse<Void>>

### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |
| **404** | No MFA enrollment or MFA enrollment is completed |  -  |


## detailMfaRecoveryCodes

> CompletableFuture<DetailMfaRecoveryCodesEnvelope> detailMfaRecoveryCodes(mfaValidationCode, mfaValidation)

For a completed MFA enrollment view the current recovery codes

Allows the viewing of recovery codes of an MFA enrollment. Requires a current valid time based one time password to interact with. Available after a completed MFA enrollment. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.MfaApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        MfaApi apiInstance = new MfaApi(defaultClient);
        String mfaValidationCode = "mfaValidationCode_example"; // String | 
        MfaCode mfaValidation = new MfaCode(); // MfaCode | An MFA validation request
        try {
            CompletableFuture<DetailMfaRecoveryCodesEnvelope> result = apiInstance.detailMfaRecoveryCodes(mfaValidationCode, mfaValidation);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling MfaApi#detailMfaRecoveryCodes");
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
| **mfaValidationCode** | **String**|  | [optional] |
| **mfaValidation** | [**MfaCode**](MfaCode.md)| An MFA validation request | [optional] |

### Return type

CompletableFuture<[**DetailMfaRecoveryCodesEnvelope**](DetailMfaRecoveryCodesEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The recovery codes of an MFA enrollment |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |

## detailMfaRecoveryCodesWithHttpInfo

> CompletableFuture<ApiResponse<DetailMfaRecoveryCodesEnvelope>> detailMfaRecoveryCodes detailMfaRecoveryCodesWithHttpInfo(mfaValidationCode, mfaValidation)

For a completed MFA enrollment view the current recovery codes

Allows the viewing of recovery codes of an MFA enrollment. Requires a current valid time based one time password to interact with. Available after a completed MFA enrollment. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.MfaApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        MfaApi apiInstance = new MfaApi(defaultClient);
        String mfaValidationCode = "mfaValidationCode_example"; // String | 
        MfaCode mfaValidation = new MfaCode(); // MfaCode | An MFA validation request
        try {
            CompletableFuture<ApiResponse<DetailMfaRecoveryCodesEnvelope>> response = apiInstance.detailMfaRecoveryCodesWithHttpInfo(mfaValidationCode, mfaValidation);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling MfaApi#detailMfaRecoveryCodes");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling MfaApi#detailMfaRecoveryCodes");
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
| **mfaValidationCode** | **String**|  | [optional] |
| **mfaValidation** | [**MfaCode**](MfaCode.md)| An MFA validation request | [optional] |

### Return type

CompletableFuture<ApiResponse<[**DetailMfaRecoveryCodesEnvelope**](DetailMfaRecoveryCodesEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The recovery codes of an MFA enrollment |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |


## enrollMfa

> CompletableFuture<CreateEnvelope> enrollMfa()

Initiate MFA enrollment

Allows authenticator based MFA enrollment. If enrollment has already been completed, it must be disabled before attempting to re-enroll. Subsequent enrollment request is completed via &#x60;POST /current-identity/mfa/verify&#x60; 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.MfaApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        MfaApi apiInstance = new MfaApi(defaultClient);
        try {
            CompletableFuture<CreateEnvelope> result = apiInstance.enrollMfa();
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling MfaApi#enrollMfa");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

CompletableFuture<[**CreateEnvelope**](CreateEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | The create request was successful and the resource has been added at the following location |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **409** | The identity is already enrolled in MFA |  -  |

## enrollMfaWithHttpInfo

> CompletableFuture<ApiResponse<CreateEnvelope>> enrollMfa enrollMfaWithHttpInfo()

Initiate MFA enrollment

Allows authenticator based MFA enrollment. If enrollment has already been completed, it must be disabled before attempting to re-enroll. Subsequent enrollment request is completed via &#x60;POST /current-identity/mfa/verify&#x60; 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.MfaApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        MfaApi apiInstance = new MfaApi(defaultClient);
        try {
            CompletableFuture<ApiResponse<CreateEnvelope>> response = apiInstance.enrollMfaWithHttpInfo();
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling MfaApi#enrollMfa");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling MfaApi#enrollMfa");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Response headers: " + e.getResponseHeaders());
            System.err.println("Reason: " + e.getResponseBody());
            e.printStackTrace();
        }
    }
}
```

### Parameters

This endpoint does not need any parameter.

### Return type

CompletableFuture<ApiResponse<[**CreateEnvelope**](CreateEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | The create request was successful and the resource has been added at the following location |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **409** | The identity is already enrolled in MFA |  -  |


## removeIdentityMfa

> CompletableFuture<Empty> removeIdentityMfa(id)

Remove MFA from an identitity

Allows an admin to remove MFA enrollment from a specific identity. Requires admin. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.MfaApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        MfaApi apiInstance = new MfaApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<Empty> result = apiInstance.removeIdentityMfa(id);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling MfaApi#removeIdentityMfa");
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

### Return type

CompletableFuture<[**Empty**](Empty.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Base empty response |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## removeIdentityMfaWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> removeIdentityMfa removeIdentityMfaWithHttpInfo(id)

Remove MFA from an identitity

Allows an admin to remove MFA enrollment from a specific identity. Requires admin. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.MfaApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        MfaApi apiInstance = new MfaApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.removeIdentityMfaWithHttpInfo(id);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling MfaApi#removeIdentityMfa");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling MfaApi#removeIdentityMfa");
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

### Return type

CompletableFuture<ApiResponse<[**Empty**](Empty.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Base empty response |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## verifyMfa

> CompletableFuture<Empty> verifyMfa(mfaValidation)

Complete MFA enrollment by verifying a time based one time token

Completes MFA enrollment by accepting a time based one time password as verification. Called after MFA enrollment has been initiated via &#x60;POST /current-identity/mfa&#x60;. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.MfaApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        MfaApi apiInstance = new MfaApi(defaultClient);
        MfaCode mfaValidation = new MfaCode(); // MfaCode | An MFA validation request
        try {
            CompletableFuture<Empty> result = apiInstance.verifyMfa(mfaValidation);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling MfaApi#verifyMfa");
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
| **mfaValidation** | [**MfaCode**](MfaCode.md)| An MFA validation request | |

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
| **404** | The requested resource does not exist |  -  |

## verifyMfaWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> verifyMfa verifyMfaWithHttpInfo(mfaValidation)

Complete MFA enrollment by verifying a time based one time token

Completes MFA enrollment by accepting a time based one time password as verification. Called after MFA enrollment has been initiated via &#x60;POST /current-identity/mfa&#x60;. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.MfaApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");
        
        // Configure API key authorization: ztSession
        ApiKeyAuth ztSession = (ApiKeyAuth) defaultClient.getAuthentication("ztSession");
        ztSession.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //ztSession.setApiKeyPrefix("Token");

        // Configure OAuth2 access token for authorization: oauth2
        OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
        oauth2.setAccessToken("YOUR ACCESS TOKEN");

        MfaApi apiInstance = new MfaApi(defaultClient);
        MfaCode mfaValidation = new MfaCode(); // MfaCode | An MFA validation request
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.verifyMfaWithHttpInfo(mfaValidation);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling MfaApi#verifyMfa");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling MfaApi#verifyMfa");
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
| **mfaValidation** | [**MfaCode**](MfaCode.md)| An MFA validation request | |

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
| **404** | The requested resource does not exist |  -  |

