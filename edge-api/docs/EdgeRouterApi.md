# EdgeRouterApi

All URIs are relative to *https://demo.ziti.dev/edge/client/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**getCurrentIdentityEdgeRouters**](EdgeRouterApi.md#getCurrentIdentityEdgeRouters) | **GET** /current-identity/edge-routers | Return this list of Edge Routers the identity has access to |
| [**getCurrentIdentityEdgeRoutersWithHttpInfo**](EdgeRouterApi.md#getCurrentIdentityEdgeRoutersWithHttpInfo) | **GET** /current-identity/edge-routers | Return this list of Edge Routers the identity has access to |



## getCurrentIdentityEdgeRouters

> CompletableFuture<ListCurrentIdentityEdgeRoutersEnvelope> getCurrentIdentityEdgeRouters()

Return this list of Edge Routers the identity has access to

Lists the Edge Routers that the current identity has access to via policies. The data returned includes their address and online status 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.auth.*;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        try {
            CompletableFuture<ListCurrentIdentityEdgeRoutersEnvelope> result = apiInstance.getCurrentIdentityEdgeRouters();
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#getCurrentIdentityEdgeRouters");
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

CompletableFuture<[**ListCurrentIdentityEdgeRoutersEnvelope**](ListCurrentIdentityEdgeRoutersEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of edge routers |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |

## getCurrentIdentityEdgeRoutersWithHttpInfo

> CompletableFuture<ApiResponse<ListCurrentIdentityEdgeRoutersEnvelope>> getCurrentIdentityEdgeRouters getCurrentIdentityEdgeRoutersWithHttpInfo()

Return this list of Edge Routers the identity has access to

Lists the Edge Routers that the current identity has access to via policies. The data returned includes their address and online status 

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.auth.*;
import org.openziti.edge.models.*;
import org.openziti.edge.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        try {
            CompletableFuture<ApiResponse<ListCurrentIdentityEdgeRoutersEnvelope>> response = apiInstance.getCurrentIdentityEdgeRoutersWithHttpInfo();
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EdgeRouterApi#getCurrentIdentityEdgeRouters");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#getCurrentIdentityEdgeRouters");
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

CompletableFuture<ApiResponse<[**ListCurrentIdentityEdgeRoutersEnvelope**](ListCurrentIdentityEdgeRoutersEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of edge routers |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |

