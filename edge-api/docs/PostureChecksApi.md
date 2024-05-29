# PostureChecksApi

All URIs are relative to *https://demo.ziti.dev/edge/client/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createPostureResponse**](PostureChecksApi.md#createPostureResponse) | **POST** /posture-response | Submit a posture response to a posture query |
| [**createPostureResponseWithHttpInfo**](PostureChecksApi.md#createPostureResponseWithHttpInfo) | **POST** /posture-response | Submit a posture response to a posture query |
| [**createPostureResponseBulk**](PostureChecksApi.md#createPostureResponseBulk) | **POST** /posture-response-bulk | Submit multiple posture responses |
| [**createPostureResponseBulkWithHttpInfo**](PostureChecksApi.md#createPostureResponseBulkWithHttpInfo) | **POST** /posture-response-bulk | Submit multiple posture responses |



## createPostureResponse

> CompletableFuture<PostureResponseEnvelope> createPostureResponse(postureResponse)

Submit a posture response to a posture query

Submits posture responses

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.auth.*;
import org.openziti.edge.models.*;
import org.openziti.edge.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        PostureResponseCreate postureResponse = new PostureResponseCreate(); // PostureResponseCreate | A Posture Response
        try {
            CompletableFuture<PostureResponseEnvelope> result = apiInstance.createPostureResponse(postureResponse);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#createPostureResponse");
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
| **postureResponse** | [**PostureResponseCreate**](PostureResponseCreate.md)| A Posture Response | |

### Return type

CompletableFuture<[**PostureResponseEnvelope**](PostureResponseEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Contains a list of services that have had their timers altered |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |

## createPostureResponseWithHttpInfo

> CompletableFuture<ApiResponse<PostureResponseEnvelope>> createPostureResponse createPostureResponseWithHttpInfo(postureResponse)

Submit a posture response to a posture query

Submits posture responses

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.auth.*;
import org.openziti.edge.models.*;
import org.openziti.edge.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        PostureResponseCreate postureResponse = new PostureResponseCreate(); // PostureResponseCreate | A Posture Response
        try {
            CompletableFuture<ApiResponse<PostureResponseEnvelope>> response = apiInstance.createPostureResponseWithHttpInfo(postureResponse);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling PostureChecksApi#createPostureResponse");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#createPostureResponse");
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
| **postureResponse** | [**PostureResponseCreate**](PostureResponseCreate.md)| A Posture Response | |

### Return type

CompletableFuture<ApiResponse<[**PostureResponseEnvelope**](PostureResponseEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | Contains a list of services that have had their timers altered |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |


## createPostureResponseBulk

> CompletableFuture<PostureResponseEnvelope> createPostureResponseBulk(postureResponse)

Submit multiple posture responses

Submits posture responses

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.auth.*;
import org.openziti.edge.models.*;
import org.openziti.edge.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        List<PostureResponseCreate> postureResponse = Arrays.asList(); // List<PostureResponseCreate> | A Posture Response
        try {
            CompletableFuture<PostureResponseEnvelope> result = apiInstance.createPostureResponseBulk(postureResponse);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#createPostureResponseBulk");
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
| **postureResponse** | [**List&lt;PostureResponseCreate&gt;**](PostureResponseCreate.md)| A Posture Response | |

### Return type

CompletableFuture<[**PostureResponseEnvelope**](PostureResponseEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Contains a list of services that have had their timers altered |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |

## createPostureResponseBulkWithHttpInfo

> CompletableFuture<ApiResponse<PostureResponseEnvelope>> createPostureResponseBulk createPostureResponseBulkWithHttpInfo(postureResponse)

Submit multiple posture responses

Submits posture responses

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.auth.*;
import org.openziti.edge.models.*;
import org.openziti.edge.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        List<PostureResponseCreate> postureResponse = Arrays.asList(); // List<PostureResponseCreate> | A Posture Response
        try {
            CompletableFuture<ApiResponse<PostureResponseEnvelope>> response = apiInstance.createPostureResponseBulkWithHttpInfo(postureResponse);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling PostureChecksApi#createPostureResponseBulk");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#createPostureResponseBulk");
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
| **postureResponse** | [**List&lt;PostureResponseCreate&gt;**](PostureResponseCreate.md)| A Posture Response | |

### Return type

CompletableFuture<ApiResponse<[**PostureResponseEnvelope**](PostureResponseEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Contains a list of services that have had their timers altered |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |

