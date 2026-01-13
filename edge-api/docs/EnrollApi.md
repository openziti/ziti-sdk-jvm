# EnrollApi

All URIs are relative to *https://demo.ziti.dev/edge/client/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**enroll**](EnrollApi.md#enroll) | **POST** /enroll | Enroll an identity via one-time-token |
| [**enrollWithHttpInfo**](EnrollApi.md#enrollWithHttpInfo) | **POST** /enroll | Enroll an identity via one-time-token |
| [**enrollCa**](EnrollApi.md#enrollCa) | **POST** /enroll/ca | Enroll an identity with a pre-exchanged certificate |
| [**enrollCaWithHttpInfo**](EnrollApi.md#enrollCaWithHttpInfo) | **POST** /enroll/ca | Enroll an identity with a pre-exchanged certificate |
| [**enrollErOtt**](EnrollApi.md#enrollErOtt) | **POST** /enroll/erott | Enroll an edge-router |
| [**enrollErOttWithHttpInfo**](EnrollApi.md#enrollErOttWithHttpInfo) | **POST** /enroll/erott | Enroll an edge-router |
| [**enrollOtt**](EnrollApi.md#enrollOtt) | **POST** /enroll/ott | Enroll an identity via one-time-token |
| [**enrollOttWithHttpInfo**](EnrollApi.md#enrollOttWithHttpInfo) | **POST** /enroll/ott | Enroll an identity via one-time-token |
| [**enrollOttCa**](EnrollApi.md#enrollOttCa) | **POST** /enroll/ottca | Enroll an identity via one-time-token with a pre-exchanged client certificate |
| [**enrollOttCaWithHttpInfo**](EnrollApi.md#enrollOttCaWithHttpInfo) | **POST** /enroll/ottca | Enroll an identity via one-time-token with a pre-exchanged client certificate |
| [**enrollToken**](EnrollApi.md#enrollToken) | **POST** /enroll/token |  |
| [**enrollTokenWithHttpInfo**](EnrollApi.md#enrollTokenWithHttpInfo) | **POST** /enroll/token |  |
| [**enrollUpdb**](EnrollApi.md#enrollUpdb) | **POST** /enroll/updb | Enroll an identity via one-time-token |
| [**enrollUpdbWithHttpInfo**](EnrollApi.md#enrollUpdbWithHttpInfo) | **POST** /enroll/updb | Enroll an identity via one-time-token |
| [**enrollmentChallenge**](EnrollApi.md#enrollmentChallenge) | **POST** /enroll/challenge | Allows verification of a controller or cluster of controllers as being the valid target for enrollment. |
| [**enrollmentChallengeWithHttpInfo**](EnrollApi.md#enrollmentChallengeWithHttpInfo) | **POST** /enroll/challenge | Allows verification of a controller or cluster of controllers as being the valid target for enrollment. |
| [**extendCurrentIdentityAuthenticator**](EnrollApi.md#extendCurrentIdentityAuthenticator) | **POST** /current-identity/authenticators/{id}/extend | Allows the current identity to recieve a new certificate associated with a certificate based authenticator |
| [**extendCurrentIdentityAuthenticatorWithHttpInfo**](EnrollApi.md#extendCurrentIdentityAuthenticatorWithHttpInfo) | **POST** /current-identity/authenticators/{id}/extend | Allows the current identity to recieve a new certificate associated with a certificate based authenticator |
| [**extendRouterEnrollment**](EnrollApi.md#extendRouterEnrollment) | **POST** /enroll/extend/router | Extend the life of a currently enrolled router&#39;s certificates |
| [**extendRouterEnrollmentWithHttpInfo**](EnrollApi.md#extendRouterEnrollmentWithHttpInfo) | **POST** /enroll/extend/router | Extend the life of a currently enrolled router&#39;s certificates |
| [**extendVerifyCurrentIdentityAuthenticator**](EnrollApi.md#extendVerifyCurrentIdentityAuthenticator) | **POST** /current-identity/authenticators/{id}/extend-verify | Allows the current identity to validate reciept of a new client certificate |
| [**extendVerifyCurrentIdentityAuthenticatorWithHttpInfo**](EnrollApi.md#extendVerifyCurrentIdentityAuthenticatorWithHttpInfo) | **POST** /current-identity/authenticators/{id}/extend-verify | Allows the current identity to validate reciept of a new client certificate |
| [**getEnrollmentJwks**](EnrollApi.md#getEnrollmentJwks) | **GET** /enroll/jwks | List JSON Web Keys associated with enrollment |
| [**getEnrollmentJwksWithHttpInfo**](EnrollApi.md#getEnrollmentJwksWithHttpInfo) | **GET** /enroll/jwks | List JSON Web Keys associated with enrollment |



## enroll

> CompletableFuture<EnrollmentCertsEnvelope> enroll(token, method, body)

Enroll an identity via one-time-token

present a OTT and CSR to receive a long-lived client certificate

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        UUID token = UUID.randomUUID(); // UUID | 
        String method = "method_example"; // String | 
        GenericEnroll body = new GenericEnroll(); // GenericEnroll | 
        try {
            CompletableFuture<EnrollmentCertsEnvelope> result = apiInstance.enroll(token, method, body);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#enroll");
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
| **token** | **UUID**|  | [optional] |
| **method** | **String**|  | [optional] |
| **body** | [**GenericEnroll**](GenericEnroll.md)|  | [optional] |

### Return type

CompletableFuture<[**EnrollmentCertsEnvelope**](EnrollmentCertsEnvelope.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/pkcs7, application/json, application/x-pem-file, text/plain
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A response containing and identities client certificate chains |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **404** | The requested resource does not exist |  -  |
| **409** | The request could not be completed due to a conflict of configuration or state |  -  |
| **410** | The request could not be completed as the resource is no longer available |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## enrollWithHttpInfo

> CompletableFuture<ApiResponse<EnrollmentCertsEnvelope>> enroll enrollWithHttpInfo(token, method, body)

Enroll an identity via one-time-token

present a OTT and CSR to receive a long-lived client certificate

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        UUID token = UUID.randomUUID(); // UUID | 
        String method = "method_example"; // String | 
        GenericEnroll body = new GenericEnroll(); // GenericEnroll | 
        try {
            CompletableFuture<ApiResponse<EnrollmentCertsEnvelope>> response = apiInstance.enrollWithHttpInfo(token, method, body);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EnrollApi#enroll");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#enroll");
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
| **token** | **UUID**|  | [optional] |
| **method** | **String**|  | [optional] |
| **body** | [**GenericEnroll**](GenericEnroll.md)|  | [optional] |

### Return type

CompletableFuture<ApiResponse<[**EnrollmentCertsEnvelope**](EnrollmentCertsEnvelope.md)>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/pkcs7, application/json, application/x-pem-file, text/plain
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A response containing and identities client certificate chains |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **404** | The requested resource does not exist |  -  |
| **409** | The request could not be completed due to a conflict of configuration or state |  -  |
| **410** | The request could not be completed as the resource is no longer available |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## enrollCa

> CompletableFuture<Empty> enrollCa()

Enroll an identity with a pre-exchanged certificate

For CA auto enrollment, an identity is not created beforehand. Instead one will be created during enrollment. The client will present a client certificate that is signed by a Certificate Authority that has been added and verified (See POST /cas and POST /cas/{id}/verify).  During this process no CSRs are requires as the client should already be in possession of a valid certificate. 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        try {
            CompletableFuture<Empty> result = apiInstance.enrollCa();
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#enrollCa");
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

CompletableFuture<[**Empty**](Empty.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Base empty response |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## enrollCaWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> enrollCa enrollCaWithHttpInfo()

Enroll an identity with a pre-exchanged certificate

For CA auto enrollment, an identity is not created beforehand. Instead one will be created during enrollment. The client will present a client certificate that is signed by a Certificate Authority that has been added and verified (See POST /cas and POST /cas/{id}/verify).  During this process no CSRs are requires as the client should already be in possession of a valid certificate. 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.enrollCaWithHttpInfo();
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EnrollApi#enrollCa");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#enrollCa");
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

CompletableFuture<ApiResponse<[**Empty**](Empty.md)>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Base empty response |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## enrollErOtt

> CompletableFuture<EnrollmentCertsEnvelope> enrollErOtt(erOttEnrollmentRequest)

Enroll an edge-router

Enrolls an edge-router via a one-time-token to establish a certificate based identity. 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        ErOttEnrollmentRequest erOttEnrollmentRequest = new ErOttEnrollmentRequest(); // ErOttEnrollmentRequest | An OTT enrollment request
        try {
            CompletableFuture<EnrollmentCertsEnvelope> result = apiInstance.enrollErOtt(erOttEnrollmentRequest);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#enrollErOtt");
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
| **erOttEnrollmentRequest** | [**ErOttEnrollmentRequest**](ErOttEnrollmentRequest.md)| An OTT enrollment request | |

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
| **200** | A response containing the edge routers signed certificates (server chain, server cert, CAs). |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## enrollErOttWithHttpInfo

> CompletableFuture<ApiResponse<EnrollmentCertsEnvelope>> enrollErOtt enrollErOttWithHttpInfo(erOttEnrollmentRequest)

Enroll an edge-router

Enrolls an edge-router via a one-time-token to establish a certificate based identity. 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        ErOttEnrollmentRequest erOttEnrollmentRequest = new ErOttEnrollmentRequest(); // ErOttEnrollmentRequest | An OTT enrollment request
        try {
            CompletableFuture<ApiResponse<EnrollmentCertsEnvelope>> response = apiInstance.enrollErOttWithHttpInfo(erOttEnrollmentRequest);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EnrollApi#enrollErOtt");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#enrollErOtt");
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
| **erOttEnrollmentRequest** | [**ErOttEnrollmentRequest**](ErOttEnrollmentRequest.md)| An OTT enrollment request | |

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
| **200** | A response containing the edge routers signed certificates (server chain, server cert, CAs). |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## enrollOtt

> CompletableFuture<EnrollmentCertsEnvelope> enrollOtt(ottEnrollmentRequest)

Enroll an identity via one-time-token

Enroll an identity via a one-time-token which is supplied via a query string parameter. This enrollment method expects a PEM encoded CSRs to be provided for fulfillment. It is up to the enrolling identity to manage the private key backing the CSR request. 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        OttEnrollmentRequest ottEnrollmentRequest = new OttEnrollmentRequest(); // OttEnrollmentRequest | An OTT enrollment request
        try {
            CompletableFuture<EnrollmentCertsEnvelope> result = apiInstance.enrollOtt(ottEnrollmentRequest);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#enrollOtt");
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
| **ottEnrollmentRequest** | [**OttEnrollmentRequest**](OttEnrollmentRequest.md)| An OTT enrollment request | |

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
| **200** | A response containing and identities client certificate chains |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## enrollOttWithHttpInfo

> CompletableFuture<ApiResponse<EnrollmentCertsEnvelope>> enrollOtt enrollOttWithHttpInfo(ottEnrollmentRequest)

Enroll an identity via one-time-token

Enroll an identity via a one-time-token which is supplied via a query string parameter. This enrollment method expects a PEM encoded CSRs to be provided for fulfillment. It is up to the enrolling identity to manage the private key backing the CSR request. 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        OttEnrollmentRequest ottEnrollmentRequest = new OttEnrollmentRequest(); // OttEnrollmentRequest | An OTT enrollment request
        try {
            CompletableFuture<ApiResponse<EnrollmentCertsEnvelope>> response = apiInstance.enrollOttWithHttpInfo(ottEnrollmentRequest);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EnrollApi#enrollOtt");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#enrollOtt");
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
| **ottEnrollmentRequest** | [**OttEnrollmentRequest**](OttEnrollmentRequest.md)| An OTT enrollment request | |

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
| **200** | A response containing and identities client certificate chains |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## enrollOttCa

> CompletableFuture<Empty> enrollOttCa(ottEnrollmentRequest)

Enroll an identity via one-time-token with a pre-exchanged client certificate

Enroll an identity via a one-time-token that also requires a pre-exchanged client certificate to match a Certificate Authority that has been added and verified (See POST /cas and POST /cas{id}/verify). The client must present a client certificate signed by CA associated with the enrollment. This enrollment is similar to CA auto enrollment except that is required the identity to be pre-created.  As the client certificate has been pre-exchanged there is no CSR input to this enrollment method. 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        OttEnrollmentRequest ottEnrollmentRequest = new OttEnrollmentRequest(); // OttEnrollmentRequest | An OTT enrollment request
        try {
            CompletableFuture<Empty> result = apiInstance.enrollOttCa(ottEnrollmentRequest);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#enrollOttCa");
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
| **ottEnrollmentRequest** | [**OttEnrollmentRequest**](OttEnrollmentRequest.md)| An OTT enrollment request | |

### Return type

CompletableFuture<[**Empty**](Empty.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Base empty response |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## enrollOttCaWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> enrollOttCa enrollOttCaWithHttpInfo(ottEnrollmentRequest)

Enroll an identity via one-time-token with a pre-exchanged client certificate

Enroll an identity via a one-time-token that also requires a pre-exchanged client certificate to match a Certificate Authority that has been added and verified (See POST /cas and POST /cas{id}/verify). The client must present a client certificate signed by CA associated with the enrollment. This enrollment is similar to CA auto enrollment except that is required the identity to be pre-created.  As the client certificate has been pre-exchanged there is no CSR input to this enrollment method. 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        OttEnrollmentRequest ottEnrollmentRequest = new OttEnrollmentRequest(); // OttEnrollmentRequest | An OTT enrollment request
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.enrollOttCaWithHttpInfo(ottEnrollmentRequest);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EnrollApi#enrollOttCa");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#enrollOttCa");
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
| **ottEnrollmentRequest** | [**OttEnrollmentRequest**](OttEnrollmentRequest.md)| An OTT enrollment request | |

### Return type

CompletableFuture<ApiResponse<[**Empty**](Empty.md)>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Base empty response |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## enrollToken

> CompletableFuture<EnrollmentCertsEnvelope> enrollToken(authorization, tokenEnrollmentRequest, zitiTokenIssuerId)



### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        String authorization = "authorization_example"; // String | An identifying token to enroll with
        TokenEnrollmentRequest tokenEnrollmentRequest = new TokenEnrollmentRequest(); // TokenEnrollmentRequest | A  enrollment request with or without a CSR. Including a CSR indicated an attempt to enroll with certificate  credentials. If no CSR is included, the request is assumed to be a token enrollment request that will authenticate via tokens. 
        String zitiTokenIssuerId = "zitiTokenIssuerId_example"; // String | The id of the token issuer to use for enrollment, optional as long the the token is not opaque
        try {
            CompletableFuture<EnrollmentCertsEnvelope> result = apiInstance.enrollToken(authorization, tokenEnrollmentRequest, zitiTokenIssuerId);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#enrollToken");
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
| **authorization** | **String**| An identifying token to enroll with | |
| **tokenEnrollmentRequest** | [**TokenEnrollmentRequest**](TokenEnrollmentRequest.md)| A  enrollment request with or without a CSR. Including a CSR indicated an attempt to enroll with certificate  credentials. If no CSR is included, the request is assumed to be a token enrollment request that will authenticate via tokens.  | |
| **zitiTokenIssuerId** | **String**| The id of the token issuer to use for enrollment, optional as long the the token is not opaque | [optional] |

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
| **200** | A response containing and identities client certificate chains |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **404** | The requested resource does not exist |  -  |
| **409** | The request could not be completed due to a conflict of configuration or state |  -  |
| **410** | The request could not be completed as the resource is no longer available |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## enrollTokenWithHttpInfo

> CompletableFuture<ApiResponse<EnrollmentCertsEnvelope>> enrollToken enrollTokenWithHttpInfo(authorization, tokenEnrollmentRequest, zitiTokenIssuerId)



### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        String authorization = "authorization_example"; // String | An identifying token to enroll with
        TokenEnrollmentRequest tokenEnrollmentRequest = new TokenEnrollmentRequest(); // TokenEnrollmentRequest | A  enrollment request with or without a CSR. Including a CSR indicated an attempt to enroll with certificate  credentials. If no CSR is included, the request is assumed to be a token enrollment request that will authenticate via tokens. 
        String zitiTokenIssuerId = "zitiTokenIssuerId_example"; // String | The id of the token issuer to use for enrollment, optional as long the the token is not opaque
        try {
            CompletableFuture<ApiResponse<EnrollmentCertsEnvelope>> response = apiInstance.enrollTokenWithHttpInfo(authorization, tokenEnrollmentRequest, zitiTokenIssuerId);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EnrollApi#enrollToken");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#enrollToken");
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
| **authorization** | **String**| An identifying token to enroll with | |
| **tokenEnrollmentRequest** | [**TokenEnrollmentRequest**](TokenEnrollmentRequest.md)| A  enrollment request with or without a CSR. Including a CSR indicated an attempt to enroll with certificate  credentials. If no CSR is included, the request is assumed to be a token enrollment request that will authenticate via tokens.  | |
| **zitiTokenIssuerId** | **String**| The id of the token issuer to use for enrollment, optional as long the the token is not opaque | [optional] |

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
| **200** | A response containing and identities client certificate chains |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **404** | The requested resource does not exist |  -  |
| **409** | The request could not be completed due to a conflict of configuration or state |  -  |
| **410** | The request could not be completed as the resource is no longer available |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## enrollUpdb

> CompletableFuture<Empty> enrollUpdb(token, updbCredentials)

Enroll an identity via one-time-token

Enrolls an identity via a one-time-token to establish an initial username and password combination 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        UUID token = UUID.randomUUID(); // UUID | 
        EnrollUpdbRequest updbCredentials = new EnrollUpdbRequest(); // EnrollUpdbRequest | 
        try {
            CompletableFuture<Empty> result = apiInstance.enrollUpdb(token, updbCredentials);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#enrollUpdb");
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
| **token** | **UUID**|  | |
| **updbCredentials** | [**EnrollUpdbRequest**](EnrollUpdbRequest.md)|  | |

### Return type

CompletableFuture<[**Empty**](Empty.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Base empty response |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## enrollUpdbWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> enrollUpdb enrollUpdbWithHttpInfo(token, updbCredentials)

Enroll an identity via one-time-token

Enrolls an identity via a one-time-token to establish an initial username and password combination 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        UUID token = UUID.randomUUID(); // UUID | 
        EnrollUpdbRequest updbCredentials = new EnrollUpdbRequest(); // EnrollUpdbRequest | 
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.enrollUpdbWithHttpInfo(token, updbCredentials);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EnrollApi#enrollUpdb");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#enrollUpdb");
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
| **token** | **UUID**|  | |
| **updbCredentials** | [**EnrollUpdbRequest**](EnrollUpdbRequest.md)|  | |

### Return type

CompletableFuture<ApiResponse<[**Empty**](Empty.md)>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Base empty response |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## enrollmentChallenge

> CompletableFuture<NonceSignature> enrollmentChallenge(nonce)

Allows verification of a controller or cluster of controllers as being the valid target for enrollment.

A caller may submit a nonce and a key id (kid) from the enrollment JWKS endpoint or enrollment JWT that will be used to sign the nonce. The resulting signature may be validated with the associated public key in order to verify a networks identity during enrollment. The nonce must be a valid formatted UUID. 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        NonceChallenge nonce = new NonceChallenge(); // NonceChallenge | 
        try {
            CompletableFuture<NonceSignature> result = apiInstance.enrollmentChallenge(nonce);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#enrollmentChallenge");
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
| **nonce** | [**NonceChallenge**](NonceChallenge.md)|  | |

### Return type

CompletableFuture<[**NonceSignature**](NonceSignature.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A nonce challenge response. The contents will be the signature of the nonce, the key id used, and algorithm used to produce the signature. |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |

## enrollmentChallengeWithHttpInfo

> CompletableFuture<ApiResponse<NonceSignature>> enrollmentChallenge enrollmentChallengeWithHttpInfo(nonce)

Allows verification of a controller or cluster of controllers as being the valid target for enrollment.

A caller may submit a nonce and a key id (kid) from the enrollment JWKS endpoint or enrollment JWT that will be used to sign the nonce. The resulting signature may be validated with the associated public key in order to verify a networks identity during enrollment. The nonce must be a valid formatted UUID. 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        NonceChallenge nonce = new NonceChallenge(); // NonceChallenge | 
        try {
            CompletableFuture<ApiResponse<NonceSignature>> response = apiInstance.enrollmentChallengeWithHttpInfo(nonce);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EnrollApi#enrollmentChallenge");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#enrollmentChallenge");
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
| **nonce** | [**NonceChallenge**](NonceChallenge.md)|  | |

### Return type

CompletableFuture<ApiResponse<[**NonceSignature**](NonceSignature.md)>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A nonce challenge response. The contents will be the signature of the nonce, the key id used, and algorithm used to produce the signature. |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |


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
import org.openziti.edge.api.EnrollApi;
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

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        IdentityExtendEnrollmentRequest extend = new IdentityExtendEnrollmentRequest(); // IdentityExtendEnrollmentRequest | 
        try {
            CompletableFuture<IdentityExtendEnrollmentEnvelope> result = apiInstance.extendCurrentIdentityAuthenticator(id, extend);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#extendCurrentIdentityAuthenticator");
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
import org.openziti.edge.api.EnrollApi;
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

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        IdentityExtendEnrollmentRequest extend = new IdentityExtendEnrollmentRequest(); // IdentityExtendEnrollmentRequest | 
        try {
            CompletableFuture<ApiResponse<IdentityExtendEnrollmentEnvelope>> response = apiInstance.extendCurrentIdentityAuthenticatorWithHttpInfo(id, extend);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EnrollApi#extendCurrentIdentityAuthenticator");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#extendCurrentIdentityAuthenticator");
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
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        RouterExtendEnrollmentRequest routerExtendEnrollmentRequest = new RouterExtendEnrollmentRequest(); // RouterExtendEnrollmentRequest | 
        try {
            CompletableFuture<EnrollmentCertsEnvelope> result = apiInstance.extendRouterEnrollment(routerExtendEnrollmentRequest);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#extendRouterEnrollment");
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
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
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
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        RouterExtendEnrollmentRequest routerExtendEnrollmentRequest = new RouterExtendEnrollmentRequest(); // RouterExtendEnrollmentRequest | 
        try {
            CompletableFuture<ApiResponse<EnrollmentCertsEnvelope>> response = apiInstance.extendRouterEnrollmentWithHttpInfo(routerExtendEnrollmentRequest);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EnrollApi#extendRouterEnrollment");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#extendRouterEnrollment");
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
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
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
import org.openziti.edge.api.EnrollApi;
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

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        IdentityExtendValidateEnrollmentRequest extend = new IdentityExtendValidateEnrollmentRequest(); // IdentityExtendValidateEnrollmentRequest | 
        try {
            CompletableFuture<Empty> result = apiInstance.extendVerifyCurrentIdentityAuthenticator(id, extend);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#extendVerifyCurrentIdentityAuthenticator");
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
import org.openziti.edge.api.EnrollApi;
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

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        IdentityExtendValidateEnrollmentRequest extend = new IdentityExtendValidateEnrollmentRequest(); // IdentityExtendValidateEnrollmentRequest | 
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.extendVerifyCurrentIdentityAuthenticatorWithHttpInfo(id, extend);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EnrollApi#extendVerifyCurrentIdentityAuthenticator");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#extendVerifyCurrentIdentityAuthenticator");
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


## getEnrollmentJwks

> CompletableFuture<Jwks> getEnrollmentJwks()

List JSON Web Keys associated with enrollment

Returns a list of JSON Web Keys (JWKS) that are used for enrollment signing. The keys listed here are used to sign and co-sign enrollment JWTs. They can be verified through a challenge endpoint, using the public keys from this endpoint to verify the target machine has possession of the related private key. 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        try {
            CompletableFuture<Jwks> result = apiInstance.getEnrollmentJwks();
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#getEnrollmentJwks");
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

CompletableFuture<[**Jwks**](Jwks.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JWKS response for enrollment. |  -  |

## getEnrollmentJwksWithHttpInfo

> CompletableFuture<ApiResponse<Jwks>> getEnrollmentJwks getEnrollmentJwksWithHttpInfo()

List JSON Web Keys associated with enrollment

Returns a list of JSON Web Keys (JWKS) that are used for enrollment signing. The keys listed here are used to sign and co-sign enrollment JWTs. They can be verified through a challenge endpoint, using the public keys from this endpoint to verify the target machine has possession of the related private key. 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollApi apiInstance = new EnrollApi(defaultClient);
        try {
            CompletableFuture<ApiResponse<Jwks>> response = apiInstance.getEnrollmentJwksWithHttpInfo();
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EnrollApi#getEnrollmentJwks");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#getEnrollmentJwks");
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

CompletableFuture<ApiResponse<[**Jwks**](Jwks.md)>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JWKS response for enrollment. |  -  |

