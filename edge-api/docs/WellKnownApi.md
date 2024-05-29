# WellKnownApi

All URIs are relative to *https://demo.ziti.dev/edge/client/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**listWellKnownCas**](WellKnownApi.md#listWellKnownCas) | **GET** /.well-known/est/cacerts | Get CA Cert Store |
| [**listWellKnownCasWithHttpInfo**](WellKnownApi.md#listWellKnownCasWithHttpInfo) | **GET** /.well-known/est/cacerts | Get CA Cert Store |



## listWellKnownCas

> CompletableFuture<String> listWellKnownCas()

Get CA Cert Store

This endpoint is used during enrollments to bootstrap trust between enrolling clients and the Ziti Edge API. This endpoint returns a base64 encoded PKCS7 store. The content can be base64 decoded and parsed by any library that supports parsing PKCS7 stores. 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.WellKnownApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        WellKnownApi apiInstance = new WellKnownApi(defaultClient);
        try {
            CompletableFuture<String> result = apiInstance.listWellKnownCas();
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling WellKnownApi#listWellKnownCas");
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

CompletableFuture<**String**>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/pkcs7-mime

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A base64 encoded PKCS7 store |  -  |

## listWellKnownCasWithHttpInfo

> CompletableFuture<ApiResponse<String>> listWellKnownCas listWellKnownCasWithHttpInfo()

Get CA Cert Store

This endpoint is used during enrollments to bootstrap trust between enrolling clients and the Ziti Edge API. This endpoint returns a base64 encoded PKCS7 store. The content can be base64 decoded and parsed by any library that supports parsing PKCS7 stores. 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.WellKnownApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        WellKnownApi apiInstance = new WellKnownApi(defaultClient);
        try {
            CompletableFuture<ApiResponse<String>> response = apiInstance.listWellKnownCasWithHttpInfo();
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling WellKnownApi#listWellKnownCas");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling WellKnownApi#listWellKnownCas");
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

CompletableFuture<ApiResponse<**String**>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/pkcs7-mime

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A base64 encoded PKCS7 store |  -  |

