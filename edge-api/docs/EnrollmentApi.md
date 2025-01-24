# EnrollmentApi

All URIs are relative to *https://demo.ziti.dev/edge/client/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**listNetworkJWTs**](EnrollmentApi.md#listNetworkJWTs) | **GET** /network-jwts | Returns a list of JWTs suitable for bootstrapping network trust. |
| [**listNetworkJWTsWithHttpInfo**](EnrollmentApi.md#listNetworkJWTsWithHttpInfo) | **GET** /network-jwts | Returns a list of JWTs suitable for bootstrapping network trust. |



## listNetworkJWTs

> CompletableFuture<ListNetworkJWTsEnvelope> listNetworkJWTs()

Returns a list of JWTs suitable for bootstrapping network trust.

Returns a list of JWTs for trusting a network

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollmentApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollmentApi apiInstance = new EnrollmentApi(defaultClient);
        try {
            CompletableFuture<ListNetworkJWTsEnvelope> result = apiInstance.listNetworkJWTs();
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollmentApi#listNetworkJWTs");
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

CompletableFuture<[**ListNetworkJWTsEnvelope**](ListNetworkJWTsEnvelope.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of network JWTs |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |

## listNetworkJWTsWithHttpInfo

> CompletableFuture<ApiResponse<ListNetworkJWTsEnvelope>> listNetworkJWTs listNetworkJWTsWithHttpInfo()

Returns a list of JWTs suitable for bootstrapping network trust.

Returns a list of JWTs for trusting a network

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EnrollmentApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        EnrollmentApi apiInstance = new EnrollmentApi(defaultClient);
        try {
            CompletableFuture<ApiResponse<ListNetworkJWTsEnvelope>> response = apiInstance.listNetworkJWTsWithHttpInfo();
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EnrollmentApi#listNetworkJWTs");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EnrollmentApi#listNetworkJWTs");
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

CompletableFuture<ApiResponse<[**ListNetworkJWTsEnvelope**](ListNetworkJWTsEnvelope.md)>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of network JWTs |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |

