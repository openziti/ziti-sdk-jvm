# EdgeRouterApi

All URIs are relative to *https://demo.ziti.dev/edge/management/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createEdgeRouter**](EdgeRouterApi.md#createEdgeRouter) | **POST** /edge-routers | Create an edge router |
| [**createEdgeRouterWithHttpInfo**](EdgeRouterApi.md#createEdgeRouterWithHttpInfo) | **POST** /edge-routers | Create an edge router |
| [**deleteEdgeRouter**](EdgeRouterApi.md#deleteEdgeRouter) | **DELETE** /edge-routers/{id} | Delete an edge router |
| [**deleteEdgeRouterWithHttpInfo**](EdgeRouterApi.md#deleteEdgeRouterWithHttpInfo) | **DELETE** /edge-routers/{id} | Delete an edge router |
| [**detailEdgeRouter**](EdgeRouterApi.md#detailEdgeRouter) | **GET** /edge-routers/{id} | Retrieves a single edge router |
| [**detailEdgeRouterWithHttpInfo**](EdgeRouterApi.md#detailEdgeRouterWithHttpInfo) | **GET** /edge-routers/{id} | Retrieves a single edge router |
| [**listEdgeRouterEdgeRouterPolicies**](EdgeRouterApi.md#listEdgeRouterEdgeRouterPolicies) | **GET** /edge-routers/{id}/edge-router-policies | List the edge router policies that affect an edge router |
| [**listEdgeRouterEdgeRouterPoliciesWithHttpInfo**](EdgeRouterApi.md#listEdgeRouterEdgeRouterPoliciesWithHttpInfo) | **GET** /edge-routers/{id}/edge-router-policies | List the edge router policies that affect an edge router |
| [**listEdgeRouterIdentities**](EdgeRouterApi.md#listEdgeRouterIdentities) | **GET** /edge-routers/{id}/identities | List associated identities |
| [**listEdgeRouterIdentitiesWithHttpInfo**](EdgeRouterApi.md#listEdgeRouterIdentitiesWithHttpInfo) | **GET** /edge-routers/{id}/identities | List associated identities |
| [**listEdgeRouterServiceEdgeRouterPolicies**](EdgeRouterApi.md#listEdgeRouterServiceEdgeRouterPolicies) | **GET** /edge-routers/{id}/service-edge-router-policies | List the service policies that affect an edge router |
| [**listEdgeRouterServiceEdgeRouterPoliciesWithHttpInfo**](EdgeRouterApi.md#listEdgeRouterServiceEdgeRouterPoliciesWithHttpInfo) | **GET** /edge-routers/{id}/service-edge-router-policies | List the service policies that affect an edge router |
| [**listEdgeRouterServices**](EdgeRouterApi.md#listEdgeRouterServices) | **GET** /edge-routers/{id}/services | List associated services |
| [**listEdgeRouterServicesWithHttpInfo**](EdgeRouterApi.md#listEdgeRouterServicesWithHttpInfo) | **GET** /edge-routers/{id}/services | List associated services |
| [**listEdgeRouters**](EdgeRouterApi.md#listEdgeRouters) | **GET** /edge-routers | List edge routers |
| [**listEdgeRoutersWithHttpInfo**](EdgeRouterApi.md#listEdgeRoutersWithHttpInfo) | **GET** /edge-routers | List edge routers |
| [**patchEdgeRouter**](EdgeRouterApi.md#patchEdgeRouter) | **PATCH** /edge-routers/{id} | Update the supplied fields on an edge router |
| [**patchEdgeRouterWithHttpInfo**](EdgeRouterApi.md#patchEdgeRouterWithHttpInfo) | **PATCH** /edge-routers/{id} | Update the supplied fields on an edge router |
| [**reEnrollEdgeRouter**](EdgeRouterApi.md#reEnrollEdgeRouter) | **POST** /edge-routers/{id}/re-enroll | Re-enroll an edge router |
| [**reEnrollEdgeRouterWithHttpInfo**](EdgeRouterApi.md#reEnrollEdgeRouterWithHttpInfo) | **POST** /edge-routers/{id}/re-enroll | Re-enroll an edge router |
| [**updateEdgeRouter**](EdgeRouterApi.md#updateEdgeRouter) | **PUT** /edge-routers/{id} | Update all fields on an edge router |
| [**updateEdgeRouterWithHttpInfo**](EdgeRouterApi.md#updateEdgeRouterWithHttpInfo) | **PUT** /edge-routers/{id} | Update all fields on an edge router |



## createEdgeRouter

> CompletableFuture<CreateEnvelope> createEdgeRouter(edgeRouter)

Create an edge router

Create a edge router resource. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        EdgeRouterCreate edgeRouter = new EdgeRouterCreate(); // EdgeRouterCreate | A edge router to create
        try {
            CompletableFuture<CreateEnvelope> result = apiInstance.createEdgeRouter(edgeRouter);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#createEdgeRouter");
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
| **edgeRouter** | [**EdgeRouterCreate**](EdgeRouterCreate.md)| A edge router to create | |

### Return type

CompletableFuture<[**CreateEnvelope**](CreateEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | The create request was successful and the resource has been added at the following location |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## createEdgeRouterWithHttpInfo

> CompletableFuture<ApiResponse<CreateEnvelope>> createEdgeRouter createEdgeRouterWithHttpInfo(edgeRouter)

Create an edge router

Create a edge router resource. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        EdgeRouterCreate edgeRouter = new EdgeRouterCreate(); // EdgeRouterCreate | A edge router to create
        try {
            CompletableFuture<ApiResponse<CreateEnvelope>> response = apiInstance.createEdgeRouterWithHttpInfo(edgeRouter);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EdgeRouterApi#createEdgeRouter");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#createEdgeRouter");
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
| **edgeRouter** | [**EdgeRouterCreate**](EdgeRouterCreate.md)| A edge router to create | |

### Return type

CompletableFuture<ApiResponse<[**CreateEnvelope**](CreateEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | The create request was successful and the resource has been added at the following location |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## deleteEdgeRouter

> CompletableFuture<Empty> deleteEdgeRouter(id)

Delete an edge router

Delete an edge router by id. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<Empty> result = apiInstance.deleteEdgeRouter(id);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#deleteEdgeRouter");
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
| **200** | The delete request was successful and the resource has been removed |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **409** | The resource requested to be removed/altered cannot be as it is referenced by another object. |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## deleteEdgeRouterWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> deleteEdgeRouter deleteEdgeRouterWithHttpInfo(id)

Delete an edge router

Delete an edge router by id. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.deleteEdgeRouterWithHttpInfo(id);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EdgeRouterApi#deleteEdgeRouter");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#deleteEdgeRouter");
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
| **200** | The delete request was successful and the resource has been removed |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **409** | The resource requested to be removed/altered cannot be as it is referenced by another object. |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## detailEdgeRouter

> CompletableFuture<DetailedEdgeRouterEnvelope> detailEdgeRouter(id)

Retrieves a single edge router

Retrieves a single edge router by id. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<DetailedEdgeRouterEnvelope> result = apiInstance.detailEdgeRouter(id);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#detailEdgeRouter");
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

CompletableFuture<[**DetailedEdgeRouterEnvelope**](DetailedEdgeRouterEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A singular edge router resource |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## detailEdgeRouterWithHttpInfo

> CompletableFuture<ApiResponse<DetailedEdgeRouterEnvelope>> detailEdgeRouter detailEdgeRouterWithHttpInfo(id)

Retrieves a single edge router

Retrieves a single edge router by id. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ApiResponse<DetailedEdgeRouterEnvelope>> response = apiInstance.detailEdgeRouterWithHttpInfo(id);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EdgeRouterApi#detailEdgeRouter");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#detailEdgeRouter");
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

CompletableFuture<ApiResponse<[**DetailedEdgeRouterEnvelope**](DetailedEdgeRouterEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A singular edge router resource |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## listEdgeRouterEdgeRouterPolicies

> CompletableFuture<ListEdgeRouterPoliciesEnvelope> listEdgeRouterEdgeRouterPolicies(id)

List the edge router policies that affect an edge router

Retrieves a list of edge router policies that apply to the specified edge router.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ListEdgeRouterPoliciesEnvelope> result = apiInstance.listEdgeRouterEdgeRouterPolicies(id);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#listEdgeRouterEdgeRouterPolicies");
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

CompletableFuture<[**ListEdgeRouterPoliciesEnvelope**](ListEdgeRouterPoliciesEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of edge router policies |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## listEdgeRouterEdgeRouterPoliciesWithHttpInfo

> CompletableFuture<ApiResponse<ListEdgeRouterPoliciesEnvelope>> listEdgeRouterEdgeRouterPolicies listEdgeRouterEdgeRouterPoliciesWithHttpInfo(id)

List the edge router policies that affect an edge router

Retrieves a list of edge router policies that apply to the specified edge router.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ApiResponse<ListEdgeRouterPoliciesEnvelope>> response = apiInstance.listEdgeRouterEdgeRouterPoliciesWithHttpInfo(id);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EdgeRouterApi#listEdgeRouterEdgeRouterPolicies");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#listEdgeRouterEdgeRouterPolicies");
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

CompletableFuture<ApiResponse<[**ListEdgeRouterPoliciesEnvelope**](ListEdgeRouterPoliciesEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of edge router policies |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## listEdgeRouterIdentities

> CompletableFuture<ListIdentitiesEnvelope> listEdgeRouterIdentities(id)

List associated identities

Retrieves a list of identities that may access services via the given edge router. Supports filtering, sorting, and pagination. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ListIdentitiesEnvelope> result = apiInstance.listEdgeRouterIdentities(id);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#listEdgeRouterIdentities");
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

CompletableFuture<[**ListIdentitiesEnvelope**](ListIdentitiesEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of identities |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## listEdgeRouterIdentitiesWithHttpInfo

> CompletableFuture<ApiResponse<ListIdentitiesEnvelope>> listEdgeRouterIdentities listEdgeRouterIdentitiesWithHttpInfo(id)

List associated identities

Retrieves a list of identities that may access services via the given edge router. Supports filtering, sorting, and pagination. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ApiResponse<ListIdentitiesEnvelope>> response = apiInstance.listEdgeRouterIdentitiesWithHttpInfo(id);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EdgeRouterApi#listEdgeRouterIdentities");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#listEdgeRouterIdentities");
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

CompletableFuture<ApiResponse<[**ListIdentitiesEnvelope**](ListIdentitiesEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of identities |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## listEdgeRouterServiceEdgeRouterPolicies

> CompletableFuture<ListServicePoliciesEnvelope> listEdgeRouterServiceEdgeRouterPolicies(id)

List the service policies that affect an edge router

Retrieves a list of service policies policies that apply to the specified edge router.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ListServicePoliciesEnvelope> result = apiInstance.listEdgeRouterServiceEdgeRouterPolicies(id);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#listEdgeRouterServiceEdgeRouterPolicies");
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

CompletableFuture<[**ListServicePoliciesEnvelope**](ListServicePoliciesEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of service policies |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## listEdgeRouterServiceEdgeRouterPoliciesWithHttpInfo

> CompletableFuture<ApiResponse<ListServicePoliciesEnvelope>> listEdgeRouterServiceEdgeRouterPolicies listEdgeRouterServiceEdgeRouterPoliciesWithHttpInfo(id)

List the service policies that affect an edge router

Retrieves a list of service policies policies that apply to the specified edge router.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ApiResponse<ListServicePoliciesEnvelope>> response = apiInstance.listEdgeRouterServiceEdgeRouterPoliciesWithHttpInfo(id);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EdgeRouterApi#listEdgeRouterServiceEdgeRouterPolicies");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#listEdgeRouterServiceEdgeRouterPolicies");
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

CompletableFuture<ApiResponse<[**ListServicePoliciesEnvelope**](ListServicePoliciesEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of service policies |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## listEdgeRouterServices

> CompletableFuture<ListServicesEnvelope> listEdgeRouterServices(id)

List associated services

Retrieves a list of services that may be accessed via the given edge router. Supports filtering, sorting, and pagination. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ListServicesEnvelope> result = apiInstance.listEdgeRouterServices(id);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#listEdgeRouterServices");
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

CompletableFuture<[**ListServicesEnvelope**](ListServicesEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of services |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## listEdgeRouterServicesWithHttpInfo

> CompletableFuture<ApiResponse<ListServicesEnvelope>> listEdgeRouterServices listEdgeRouterServicesWithHttpInfo(id)

List associated services

Retrieves a list of services that may be accessed via the given edge router. Supports filtering, sorting, and pagination. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ApiResponse<ListServicesEnvelope>> response = apiInstance.listEdgeRouterServicesWithHttpInfo(id);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EdgeRouterApi#listEdgeRouterServices");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#listEdgeRouterServices");
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

CompletableFuture<ApiResponse<[**ListServicesEnvelope**](ListServicesEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of services |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## listEdgeRouters

> CompletableFuture<ListEdgeRoutersEnvelope> listEdgeRouters(limit, offset, filter, roleFilter, roleSemantic)

List edge routers

Retrieves a list of edge router resources; supports filtering, sorting, and pagination. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        List<String> roleFilter = Arrays.asList(); // List<String> | 
        String roleSemantic = "roleSemantic_example"; // String | 
        try {
            CompletableFuture<ListEdgeRoutersEnvelope> result = apiInstance.listEdgeRouters(limit, offset, filter, roleFilter, roleSemantic);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#listEdgeRouters");
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
| **limit** | **Integer**|  | [optional] |
| **offset** | **Integer**|  | [optional] |
| **filter** | **String**|  | [optional] |
| **roleFilter** | [**List&lt;String&gt;**](String.md)|  | [optional] |
| **roleSemantic** | **String**|  | [optional] |

### Return type

CompletableFuture<[**ListEdgeRoutersEnvelope**](ListEdgeRoutersEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of edge routers |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## listEdgeRoutersWithHttpInfo

> CompletableFuture<ApiResponse<ListEdgeRoutersEnvelope>> listEdgeRouters listEdgeRoutersWithHttpInfo(limit, offset, filter, roleFilter, roleSemantic)

List edge routers

Retrieves a list of edge router resources; supports filtering, sorting, and pagination. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        List<String> roleFilter = Arrays.asList(); // List<String> | 
        String roleSemantic = "roleSemantic_example"; // String | 
        try {
            CompletableFuture<ApiResponse<ListEdgeRoutersEnvelope>> response = apiInstance.listEdgeRoutersWithHttpInfo(limit, offset, filter, roleFilter, roleSemantic);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EdgeRouterApi#listEdgeRouters");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#listEdgeRouters");
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
| **limit** | **Integer**|  | [optional] |
| **offset** | **Integer**|  | [optional] |
| **filter** | **String**|  | [optional] |
| **roleFilter** | [**List&lt;String&gt;**](String.md)|  | [optional] |
| **roleSemantic** | **String**|  | [optional] |

### Return type

CompletableFuture<ApiResponse<[**ListEdgeRoutersEnvelope**](ListEdgeRoutersEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of edge routers |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## patchEdgeRouter

> CompletableFuture<Empty> patchEdgeRouter(id, edgeRouter)

Update the supplied fields on an edge router

Update the supplied fields on an edge router. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        EdgeRouterPatch edgeRouter = new EdgeRouterPatch(); // EdgeRouterPatch | An edge router patch object
        try {
            CompletableFuture<Empty> result = apiInstance.patchEdgeRouter(id, edgeRouter);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#patchEdgeRouter");
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
| **edgeRouter** | [**EdgeRouterPatch**](EdgeRouterPatch.md)| An edge router patch object | |

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
| **200** | The patch request was successful and the resource has been altered |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## patchEdgeRouterWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> patchEdgeRouter patchEdgeRouterWithHttpInfo(id, edgeRouter)

Update the supplied fields on an edge router

Update the supplied fields on an edge router. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        EdgeRouterPatch edgeRouter = new EdgeRouterPatch(); // EdgeRouterPatch | An edge router patch object
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.patchEdgeRouterWithHttpInfo(id, edgeRouter);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EdgeRouterApi#patchEdgeRouter");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#patchEdgeRouter");
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
| **edgeRouter** | [**EdgeRouterPatch**](EdgeRouterPatch.md)| An edge router patch object | |

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
| **200** | The patch request was successful and the resource has been altered |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## reEnrollEdgeRouter

> CompletableFuture<Empty> reEnrollEdgeRouter(id)

Re-enroll an edge router

Removes current certificate based authentication mechanisms and reverts the edge router into a state where enrollment must be performed. The router retains all other properties and associations. If the router is currently connected, it will be disconnected and any attemps to reconnect will fail until the enrollment process is completed with the newly generated JWT.  If the edge router has an existing outstanding enrollment JWT it will be replaced. The previous JWT will no longer be usable to complete the enrollment process. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<Empty> result = apiInstance.reEnrollEdgeRouter(id);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#reEnrollEdgeRouter");
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

## reEnrollEdgeRouterWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> reEnrollEdgeRouter reEnrollEdgeRouterWithHttpInfo(id)

Re-enroll an edge router

Removes current certificate based authentication mechanisms and reverts the edge router into a state where enrollment must be performed. The router retains all other properties and associations. If the router is currently connected, it will be disconnected and any attemps to reconnect will fail until the enrollment process is completed with the newly generated JWT.  If the edge router has an existing outstanding enrollment JWT it will be replaced. The previous JWT will no longer be usable to complete the enrollment process. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.reEnrollEdgeRouterWithHttpInfo(id);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EdgeRouterApi#reEnrollEdgeRouter");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#reEnrollEdgeRouter");
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


## updateEdgeRouter

> CompletableFuture<Empty> updateEdgeRouter(id, edgeRouter)

Update all fields on an edge router

Update all fields on an edge router by id. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        EdgeRouterUpdate edgeRouter = new EdgeRouterUpdate(); // EdgeRouterUpdate | An edge router update object
        try {
            CompletableFuture<Empty> result = apiInstance.updateEdgeRouter(id, edgeRouter);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#updateEdgeRouter");
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
| **edgeRouter** | [**EdgeRouterUpdate**](EdgeRouterUpdate.md)| An edge router update object | |

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
| **200** | The update request was successful and the resource has been altered |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## updateEdgeRouterWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> updateEdgeRouter updateEdgeRouterWithHttpInfo(id, edgeRouter)

Update all fields on an edge router

Update all fields on an edge router by id. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.EdgeRouterApi;
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

        EdgeRouterApi apiInstance = new EdgeRouterApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        EdgeRouterUpdate edgeRouter = new EdgeRouterUpdate(); // EdgeRouterUpdate | An edge router update object
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.updateEdgeRouterWithHttpInfo(id, edgeRouter);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling EdgeRouterApi#updateEdgeRouter");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling EdgeRouterApi#updateEdgeRouter");
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
| **edgeRouter** | [**EdgeRouterUpdate**](EdgeRouterUpdate.md)| An edge router update object | |

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
| **200** | The update request was successful and the resource has been altered |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

