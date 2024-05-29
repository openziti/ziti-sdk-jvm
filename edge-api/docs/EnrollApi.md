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
| [**ernollUpdb**](EnrollApi.md#ernollUpdb) | **POST** /enroll/updb | Enroll an identity via one-time-token |
| [**ernollUpdbWithHttpInfo**](EnrollApi.md#ernollUpdbWithHttpInfo) | **POST** /enroll/updb | Enroll an identity via one-time-token |
| [**extendCurrentIdentityAuthenticator**](EnrollApi.md#extendCurrentIdentityAuthenticator) | **POST** /current-identity/authenticators/{id}/extend | Allows the current identity to recieve a new certificate associated with a certificate based authenticator |
| [**extendCurrentIdentityAuthenticatorWithHttpInfo**](EnrollApi.md#extendCurrentIdentityAuthenticatorWithHttpInfo) | **POST** /current-identity/authenticators/{id}/extend | Allows the current identity to recieve a new certificate associated with a certificate based authenticator |
| [**extendRouterEnrollment**](EnrollApi.md#extendRouterEnrollment) | **POST** /enroll/extend/router | Extend the life of a currently enrolled router&#39;s certificates |
| [**extendRouterEnrollmentWithHttpInfo**](EnrollApi.md#extendRouterEnrollmentWithHttpInfo) | **POST** /enroll/extend/router | Extend the life of a currently enrolled router&#39;s certificates |
| [**extendVerifyCurrentIdentityAuthenticator**](EnrollApi.md#extendVerifyCurrentIdentityAuthenticator) | **POST** /current-identity/authenticators/{id}/extend-verify | Allows the current identity to validate reciept of a new client certificate |
| [**extendVerifyCurrentIdentityAuthenticatorWithHttpInfo**](EnrollApi.md#extendVerifyCurrentIdentityAuthenticatorWithHttpInfo) | **POST** /current-identity/authenticators/{id}/extend-verify | Allows the current identity to validate reciept of a new client certificate |



## enroll

> CompletableFuture<Empty> enroll(token, method)

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
        try {
            CompletableFuture<Empty> result = apiInstance.enroll(token, method);
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

### Return type

CompletableFuture<[**Empty**](Empty.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/x-pem-file, application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Base empty response |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |

## enrollWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> enroll enrollWithHttpInfo(token, method)

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
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.enrollWithHttpInfo(token, method);
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

### Return type

CompletableFuture<ApiResponse<[**Empty**](Empty.md)>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/x-pem-file, application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Base empty response |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |


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
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |

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
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |


## enrollErOtt

> CompletableFuture<EnrollmentCertsEnvelope> enrollErOtt(token)

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
        UUID token = UUID.randomUUID(); // UUID | 
        try {
            CompletableFuture<EnrollmentCertsEnvelope> result = apiInstance.enrollErOtt(token);
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
| **token** | **UUID**|  | |

### Return type

CompletableFuture<[**EnrollmentCertsEnvelope**](EnrollmentCertsEnvelope.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A response containing the edge routers signed certificates (server chain, server cert, CAs). |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |

## enrollErOttWithHttpInfo

> CompletableFuture<ApiResponse<EnrollmentCertsEnvelope>> enrollErOtt enrollErOttWithHttpInfo(token)

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
        UUID token = UUID.randomUUID(); // UUID | 
        try {
            CompletableFuture<ApiResponse<EnrollmentCertsEnvelope>> response = apiInstance.enrollErOttWithHttpInfo(token);
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
| **token** | **UUID**|  | |

### Return type

CompletableFuture<ApiResponse<[**EnrollmentCertsEnvelope**](EnrollmentCertsEnvelope.md)>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A response containing the edge routers signed certificates (server chain, server cert, CAs). |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |


## enrollOtt

> CompletableFuture<String> enrollOtt(token)

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
        UUID token = UUID.randomUUID(); // UUID | 
        try {
            CompletableFuture<String> result = apiInstance.enrollOtt(token);
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
| **token** | **UUID**|  | |

### Return type

CompletableFuture<**String**>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/x-x509-user-cert, application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A PEM encoded certificate signed by the internal Ziti CA |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |

## enrollOttWithHttpInfo

> CompletableFuture<ApiResponse<String>> enrollOtt enrollOttWithHttpInfo(token)

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
        UUID token = UUID.randomUUID(); // UUID | 
        try {
            CompletableFuture<ApiResponse<String>> response = apiInstance.enrollOttWithHttpInfo(token);
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
| **token** | **UUID**|  | |

### Return type

CompletableFuture<ApiResponse<**String**>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/x-x509-user-cert, application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A PEM encoded certificate signed by the internal Ziti CA |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |


## enrollOttCa

> CompletableFuture<Empty> enrollOttCa(token)

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
        UUID token = UUID.randomUUID(); // UUID | 
        try {
            CompletableFuture<Empty> result = apiInstance.enrollOttCa(token);
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
| **token** | **UUID**|  | |

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
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |

## enrollOttCaWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> enrollOttCa enrollOttCaWithHttpInfo(token)

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
        UUID token = UUID.randomUUID(); // UUID | 
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.enrollOttCaWithHttpInfo(token);
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
| **token** | **UUID**|  | |

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
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |


## ernollUpdb

> CompletableFuture<Empty> ernollUpdb(token)

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
        try {
            CompletableFuture<Empty> result = apiInstance.ernollUpdb(token);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#ernollUpdb");
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
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |

## ernollUpdbWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> ernollUpdb ernollUpdbWithHttpInfo(token)

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
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.ernollUpdbWithHttpInfo(token);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EnrollApi#ernollUpdb");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollApi#ernollUpdb");
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
| **404** | The requested resource does not exist |  -  |
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
| **200** | A response containg the edge routers new signed certificates (server chain, server cert, CAs). |  -  |
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
| **200** | A response containg the edge routers new signed certificates (server chain, server cert, CAs). |  -  |
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

