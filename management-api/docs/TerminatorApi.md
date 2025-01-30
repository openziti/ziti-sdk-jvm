# TerminatorApi

All URIs are relative to *https://demo.ziti.dev/edge/management/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createTerminator**](TerminatorApi.md#createTerminator) | **POST** /terminators | Create a terminator resource |
| [**createTerminatorWithHttpInfo**](TerminatorApi.md#createTerminatorWithHttpInfo) | **POST** /terminators | Create a terminator resource |
| [**deleteTerminator**](TerminatorApi.md#deleteTerminator) | **DELETE** /terminators/{id} | Delete a terminator |
| [**deleteTerminatorWithHttpInfo**](TerminatorApi.md#deleteTerminatorWithHttpInfo) | **DELETE** /terminators/{id} | Delete a terminator |
| [**detailTerminator**](TerminatorApi.md#detailTerminator) | **GET** /terminators/{id} | Retrieves a single terminator |
| [**detailTerminatorWithHttpInfo**](TerminatorApi.md#detailTerminatorWithHttpInfo) | **GET** /terminators/{id} | Retrieves a single terminator |
| [**listTerminators**](TerminatorApi.md#listTerminators) | **GET** /terminators | List terminators |
| [**listTerminatorsWithHttpInfo**](TerminatorApi.md#listTerminatorsWithHttpInfo) | **GET** /terminators | List terminators |
| [**patchTerminator**](TerminatorApi.md#patchTerminator) | **PATCH** /terminators/{id} | Update the supplied fields on a terminator |
| [**patchTerminatorWithHttpInfo**](TerminatorApi.md#patchTerminatorWithHttpInfo) | **PATCH** /terminators/{id} | Update the supplied fields on a terminator |
| [**updateTerminator**](TerminatorApi.md#updateTerminator) | **PUT** /terminators/{id} | Update all fields on a terminator |
| [**updateTerminatorWithHttpInfo**](TerminatorApi.md#updateTerminatorWithHttpInfo) | **PUT** /terminators/{id} | Update all fields on a terminator |



## createTerminator

> CompletableFuture<CreateEnvelope> createTerminator(terminator)

Create a terminator resource

Create a terminator resource. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.TerminatorApi;
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

        TerminatorApi apiInstance = new TerminatorApi(defaultClient);
        TerminatorCreate terminator = new TerminatorCreate(); // TerminatorCreate | A terminator to create
        try {
            CompletableFuture<CreateEnvelope> result = apiInstance.createTerminator(terminator);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling TerminatorApi#createTerminator");
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
| **terminator** | [**TerminatorCreate**](TerminatorCreate.md)| A terminator to create | |

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

## createTerminatorWithHttpInfo

> CompletableFuture<ApiResponse<CreateEnvelope>> createTerminator createTerminatorWithHttpInfo(terminator)

Create a terminator resource

Create a terminator resource. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.TerminatorApi;
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

        TerminatorApi apiInstance = new TerminatorApi(defaultClient);
        TerminatorCreate terminator = new TerminatorCreate(); // TerminatorCreate | A terminator to create
        try {
            CompletableFuture<ApiResponse<CreateEnvelope>> response = apiInstance.createTerminatorWithHttpInfo(terminator);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling TerminatorApi#createTerminator");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling TerminatorApi#createTerminator");
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
| **terminator** | [**TerminatorCreate**](TerminatorCreate.md)| A terminator to create | |

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


## deleteTerminator

> CompletableFuture<Empty> deleteTerminator(id)

Delete a terminator

Delete a terminator by id. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.TerminatorApi;
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

        TerminatorApi apiInstance = new TerminatorApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<Empty> result = apiInstance.deleteTerminator(id);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling TerminatorApi#deleteTerminator");
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

## deleteTerminatorWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> deleteTerminator deleteTerminatorWithHttpInfo(id)

Delete a terminator

Delete a terminator by id. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.TerminatorApi;
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

        TerminatorApi apiInstance = new TerminatorApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.deleteTerminatorWithHttpInfo(id);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling TerminatorApi#deleteTerminator");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling TerminatorApi#deleteTerminator");
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


## detailTerminator

> CompletableFuture<DetailTerminatorEnvelope> detailTerminator(id)

Retrieves a single terminator

Retrieves a single terminator by id. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.TerminatorApi;
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

        TerminatorApi apiInstance = new TerminatorApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<DetailTerminatorEnvelope> result = apiInstance.detailTerminator(id);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling TerminatorApi#detailTerminator");
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

CompletableFuture<[**DetailTerminatorEnvelope**](DetailTerminatorEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A single terminator |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## detailTerminatorWithHttpInfo

> CompletableFuture<ApiResponse<DetailTerminatorEnvelope>> detailTerminator detailTerminatorWithHttpInfo(id)

Retrieves a single terminator

Retrieves a single terminator by id. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.TerminatorApi;
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

        TerminatorApi apiInstance = new TerminatorApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ApiResponse<DetailTerminatorEnvelope>> response = apiInstance.detailTerminatorWithHttpInfo(id);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling TerminatorApi#detailTerminator");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling TerminatorApi#detailTerminator");
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

CompletableFuture<ApiResponse<[**DetailTerminatorEnvelope**](DetailTerminatorEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A single terminator |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **404** | The requested resource does not exist |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## listTerminators

> CompletableFuture<ListTerminatorsEnvelope> listTerminators(limit, offset, filter)

List terminators

Retrieves a list of terminator resources; supports filtering, sorting, and pagination. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.TerminatorApi;
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

        TerminatorApi apiInstance = new TerminatorApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        try {
            CompletableFuture<ListTerminatorsEnvelope> result = apiInstance.listTerminators(limit, offset, filter);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling TerminatorApi#listTerminators");
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

### Return type

CompletableFuture<[**ListTerminatorsEnvelope**](ListTerminatorsEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of terminators |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## listTerminatorsWithHttpInfo

> CompletableFuture<ApiResponse<ListTerminatorsEnvelope>> listTerminators listTerminatorsWithHttpInfo(limit, offset, filter)

List terminators

Retrieves a list of terminator resources; supports filtering, sorting, and pagination. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.TerminatorApi;
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

        TerminatorApi apiInstance = new TerminatorApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        try {
            CompletableFuture<ApiResponse<ListTerminatorsEnvelope>> response = apiInstance.listTerminatorsWithHttpInfo(limit, offset, filter);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling TerminatorApi#listTerminators");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling TerminatorApi#listTerminators");
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

### Return type

CompletableFuture<ApiResponse<[**ListTerminatorsEnvelope**](ListTerminatorsEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of terminators |  -  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## patchTerminator

> CompletableFuture<Empty> patchTerminator(id, terminator)

Update the supplied fields on a terminator

Update the supplied fields on a terminator. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.TerminatorApi;
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

        TerminatorApi apiInstance = new TerminatorApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        TerminatorPatch terminator = new TerminatorPatch(); // TerminatorPatch | A terminator patch object
        try {
            CompletableFuture<Empty> result = apiInstance.patchTerminator(id, terminator);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling TerminatorApi#patchTerminator");
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
| **terminator** | [**TerminatorPatch**](TerminatorPatch.md)| A terminator patch object | |

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

## patchTerminatorWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> patchTerminator patchTerminatorWithHttpInfo(id, terminator)

Update the supplied fields on a terminator

Update the supplied fields on a terminator. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.TerminatorApi;
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

        TerminatorApi apiInstance = new TerminatorApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        TerminatorPatch terminator = new TerminatorPatch(); // TerminatorPatch | A terminator patch object
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.patchTerminatorWithHttpInfo(id, terminator);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling TerminatorApi#patchTerminator");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling TerminatorApi#patchTerminator");
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
| **terminator** | [**TerminatorPatch**](TerminatorPatch.md)| A terminator patch object | |

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


## updateTerminator

> CompletableFuture<Empty> updateTerminator(id, terminator)

Update all fields on a terminator

Update all fields on a terminator by id. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.TerminatorApi;
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

        TerminatorApi apiInstance = new TerminatorApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        TerminatorUpdate terminator = new TerminatorUpdate(); // TerminatorUpdate | A terminator update object
        try {
            CompletableFuture<Empty> result = apiInstance.updateTerminator(id, terminator);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling TerminatorApi#updateTerminator");
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
| **terminator** | [**TerminatorUpdate**](TerminatorUpdate.md)| A terminator update object | |

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

## updateTerminatorWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> updateTerminator updateTerminatorWithHttpInfo(id, terminator)

Update all fields on a terminator

Update all fields on a terminator by id. Requires admin access.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.TerminatorApi;
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

        TerminatorApi apiInstance = new TerminatorApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        TerminatorUpdate terminator = new TerminatorUpdate(); // TerminatorUpdate | A terminator update object
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.updateTerminatorWithHttpInfo(id, terminator);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling TerminatorApi#updateTerminator");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling TerminatorApi#updateTerminator");
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
| **terminator** | [**TerminatorUpdate**](TerminatorUpdate.md)| A terminator update object | |

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

