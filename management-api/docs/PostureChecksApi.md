# PostureChecksApi

All URIs are relative to *https://demo.ziti.dev/edge/management/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createPostureCheck**](PostureChecksApi.md#createPostureCheck) | **POST** /posture-checks | Creates a Posture Checks |
| [**createPostureCheckWithHttpInfo**](PostureChecksApi.md#createPostureCheckWithHttpInfo) | **POST** /posture-checks | Creates a Posture Checks |
| [**deletePostureCheck**](PostureChecksApi.md#deletePostureCheck) | **DELETE** /posture-checks/{id} | Deletes an Posture Checks |
| [**deletePostureCheckWithHttpInfo**](PostureChecksApi.md#deletePostureCheckWithHttpInfo) | **DELETE** /posture-checks/{id} | Deletes an Posture Checks |
| [**detailPostureCheck**](PostureChecksApi.md#detailPostureCheck) | **GET** /posture-checks/{id} | Retrieves a single Posture Checks |
| [**detailPostureCheckWithHttpInfo**](PostureChecksApi.md#detailPostureCheckWithHttpInfo) | **GET** /posture-checks/{id} | Retrieves a single Posture Checks |
| [**detailPostureCheckType**](PostureChecksApi.md#detailPostureCheckType) | **GET** /posture-check-types/{id} | Retrieves a single posture check type |
| [**detailPostureCheckTypeWithHttpInfo**](PostureChecksApi.md#detailPostureCheckTypeWithHttpInfo) | **GET** /posture-check-types/{id} | Retrieves a single posture check type |
| [**listPostureCheckTypes**](PostureChecksApi.md#listPostureCheckTypes) | **GET** /posture-check-types | List a subset of posture check types |
| [**listPostureCheckTypesWithHttpInfo**](PostureChecksApi.md#listPostureCheckTypesWithHttpInfo) | **GET** /posture-check-types | List a subset of posture check types |
| [**listPostureChecks**](PostureChecksApi.md#listPostureChecks) | **GET** /posture-checks | List a subset of posture checks |
| [**listPostureChecksWithHttpInfo**](PostureChecksApi.md#listPostureChecksWithHttpInfo) | **GET** /posture-checks | List a subset of posture checks |
| [**patchPostureCheck**](PostureChecksApi.md#patchPostureCheck) | **PATCH** /posture-checks/{id} | Update the supplied fields on a Posture Checks |
| [**patchPostureCheckWithHttpInfo**](PostureChecksApi.md#patchPostureCheckWithHttpInfo) | **PATCH** /posture-checks/{id} | Update the supplied fields on a Posture Checks |
| [**updatePostureCheck**](PostureChecksApi.md#updatePostureCheck) | **PUT** /posture-checks/{id} | Update all fields on a Posture Checks |
| [**updatePostureCheckWithHttpInfo**](PostureChecksApi.md#updatePostureCheckWithHttpInfo) | **PUT** /posture-checks/{id} | Update all fields on a Posture Checks |



## createPostureCheck

> CompletableFuture<CreateEnvelope> createPostureCheck(postureCheck)

Creates a Posture Checks

Creates a Posture Checks

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        PostureCheckCreate postureCheck = new PostureCheckCreate(); // PostureCheckCreate | A Posture Check to create
        try {
            CompletableFuture<CreateEnvelope> result = apiInstance.createPostureCheck(postureCheck);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#createPostureCheck");
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
| **postureCheck** | [**PostureCheckCreate**](PostureCheckCreate.md)| A Posture Check to create | |

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
| **201** | The create request was successful and the resource has been added at the following location |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |

## createPostureCheckWithHttpInfo

> CompletableFuture<ApiResponse<CreateEnvelope>> createPostureCheckWithHttpInfo(postureCheck)

Creates a Posture Checks

Creates a Posture Checks

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        PostureCheckCreate postureCheck = new PostureCheckCreate(); // PostureCheckCreate | A Posture Check to create
        try {
            CompletableFuture<ApiResponse<CreateEnvelope>> response = apiInstance.createPostureCheckWithHttpInfo(postureCheck);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling PostureChecksApi#createPostureCheck");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#createPostureCheck");
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
| **postureCheck** | [**PostureCheckCreate**](PostureCheckCreate.md)| A Posture Check to create | |

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
| **201** | The create request was successful and the resource has been added at the following location |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |


## deletePostureCheck

> CompletableFuture<Empty> deletePostureCheck(id)

Deletes an Posture Checks

Deletes and Posture Checks by id

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<Empty> result = apiInstance.deletePostureCheck(id);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#deletePostureCheck");
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
| **200** | The delete request was successful and the resource has been removed |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **403** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **404** | The requested resource does not exist |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |

## deletePostureCheckWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> deletePostureCheckWithHttpInfo(id)

Deletes an Posture Checks

Deletes and Posture Checks by id

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.deletePostureCheckWithHttpInfo(id);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling PostureChecksApi#deletePostureCheck");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#deletePostureCheck");
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
| **200** | The delete request was successful and the resource has been removed |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **403** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **404** | The requested resource does not exist |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |


## detailPostureCheck

> CompletableFuture<DetailPostureCheckEnvelope> detailPostureCheck(id)

Retrieves a single Posture Checks

Retrieves a single Posture Checks by id

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<DetailPostureCheckEnvelope> result = apiInstance.detailPostureCheck(id);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#detailPostureCheck");
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

CompletableFuture<[**DetailPostureCheckEnvelope**](DetailPostureCheckEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Retrieves a singular posture check by id |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **404** | The requested resource does not exist |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |

## detailPostureCheckWithHttpInfo

> CompletableFuture<ApiResponse<DetailPostureCheckEnvelope>> detailPostureCheckWithHttpInfo(id)

Retrieves a single Posture Checks

Retrieves a single Posture Checks by id

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ApiResponse<DetailPostureCheckEnvelope>> response = apiInstance.detailPostureCheckWithHttpInfo(id);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling PostureChecksApi#detailPostureCheck");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#detailPostureCheck");
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

CompletableFuture<ApiResponse<[**DetailPostureCheckEnvelope**](DetailPostureCheckEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Retrieves a singular posture check by id |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **404** | The requested resource does not exist |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |


## detailPostureCheckType

> CompletableFuture<DetailPostureCheckTypeEnvelope> detailPostureCheckType(id)

Retrieves a single posture check type

Retrieves a single posture check type by id

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<DetailPostureCheckTypeEnvelope> result = apiInstance.detailPostureCheckType(id);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#detailPostureCheckType");
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

CompletableFuture<[**DetailPostureCheckTypeEnvelope**](DetailPostureCheckTypeEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Retrieves a singular posture check type by id |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **404** | The requested resource does not exist |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |

## detailPostureCheckTypeWithHttpInfo

> CompletableFuture<ApiResponse<DetailPostureCheckTypeEnvelope>> detailPostureCheckTypeWithHttpInfo(id)

Retrieves a single posture check type

Retrieves a single posture check type by id

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ApiResponse<DetailPostureCheckTypeEnvelope>> response = apiInstance.detailPostureCheckTypeWithHttpInfo(id);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling PostureChecksApi#detailPostureCheckType");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#detailPostureCheckType");
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

CompletableFuture<ApiResponse<[**DetailPostureCheckTypeEnvelope**](DetailPostureCheckTypeEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Retrieves a singular posture check type by id |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **404** | The requested resource does not exist |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |


## listPostureCheckTypes

> CompletableFuture<ListPostureCheckTypesEnvelope> listPostureCheckTypes(limit, offset, filter)

List a subset of posture check types

Retrieves a list of posture check types 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        try {
            CompletableFuture<ListPostureCheckTypesEnvelope> result = apiInstance.listPostureCheckTypes(limit, offset, filter);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#listPostureCheckTypes");
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

CompletableFuture<[**ListPostureCheckTypesEnvelope**](ListPostureCheckTypesEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=utf-8, application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of posture check types |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |

## listPostureCheckTypesWithHttpInfo

> CompletableFuture<ApiResponse<ListPostureCheckTypesEnvelope>> listPostureCheckTypesWithHttpInfo(limit, offset, filter)

List a subset of posture check types

Retrieves a list of posture check types 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        try {
            CompletableFuture<ApiResponse<ListPostureCheckTypesEnvelope>> response = apiInstance.listPostureCheckTypesWithHttpInfo(limit, offset, filter);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling PostureChecksApi#listPostureCheckTypes");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#listPostureCheckTypes");
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

CompletableFuture<ApiResponse<[**ListPostureCheckTypesEnvelope**](ListPostureCheckTypesEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=utf-8, application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of posture check types |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |


## listPostureChecks

> CompletableFuture<ListPostureCheckEnvelope> listPostureChecks(limit, offset, filter, roleFilter, roleSemantic)

List a subset of posture checks

Retrieves a list of posture checks 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        List<String> roleFilter = Arrays.asList(); // List<String> | 
        String roleSemantic = "roleSemantic_example"; // String | 
        try {
            CompletableFuture<ListPostureCheckEnvelope> result = apiInstance.listPostureChecks(limit, offset, filter, roleFilter, roleSemantic);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#listPostureChecks");
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

CompletableFuture<[**ListPostureCheckEnvelope**](ListPostureCheckEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=utf-8, application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of posture checks |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |

## listPostureChecksWithHttpInfo

> CompletableFuture<ApiResponse<ListPostureCheckEnvelope>> listPostureChecksWithHttpInfo(limit, offset, filter, roleFilter, roleSemantic)

List a subset of posture checks

Retrieves a list of posture checks 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        List<String> roleFilter = Arrays.asList(); // List<String> | 
        String roleSemantic = "roleSemantic_example"; // String | 
        try {
            CompletableFuture<ApiResponse<ListPostureCheckEnvelope>> response = apiInstance.listPostureChecksWithHttpInfo(limit, offset, filter, roleFilter, roleSemantic);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling PostureChecksApi#listPostureChecks");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#listPostureChecks");
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

CompletableFuture<ApiResponse<[**ListPostureCheckEnvelope**](ListPostureCheckEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json; charset=utf-8, application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of posture checks |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |


## patchPostureCheck

> CompletableFuture<Empty> patchPostureCheck(id, postureCheck)

Update the supplied fields on a Posture Checks

Update only the supplied fields on a Posture Checks by id

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        PostureCheckPatch postureCheck = new PostureCheckPatch(); // PostureCheckPatch | A Posture Check patch object
        try {
            CompletableFuture<Empty> result = apiInstance.patchPostureCheck(id, postureCheck);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#patchPostureCheck");
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
| **postureCheck** | [**PostureCheckPatch**](PostureCheckPatch.md)| A Posture Check patch object | |

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
| **200** | The patch request was successful and the resource has been altered |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **404** | The requested resource does not exist |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |

## patchPostureCheckWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> patchPostureCheckWithHttpInfo(id, postureCheck)

Update the supplied fields on a Posture Checks

Update only the supplied fields on a Posture Checks by id

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        PostureCheckPatch postureCheck = new PostureCheckPatch(); // PostureCheckPatch | A Posture Check patch object
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.patchPostureCheckWithHttpInfo(id, postureCheck);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling PostureChecksApi#patchPostureCheck");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#patchPostureCheck");
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
| **postureCheck** | [**PostureCheckPatch**](PostureCheckPatch.md)| A Posture Check patch object | |

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
| **200** | The patch request was successful and the resource has been altered |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **404** | The requested resource does not exist |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |


## updatePostureCheck

> CompletableFuture<Empty> updatePostureCheck(id, postureCheck)

Update all fields on a Posture Checks

Update all fields on a Posture Checks by id

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        PostureCheckUpdate postureCheck = new PostureCheckUpdate(); // PostureCheckUpdate | A Posture Check update object
        try {
            CompletableFuture<Empty> result = apiInstance.updatePostureCheck(id, postureCheck);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#updatePostureCheck");
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
| **postureCheck** | [**PostureCheckUpdate**](PostureCheckUpdate.md)| A Posture Check update object | |

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
| **200** | The update request was successful and the resource has been altered |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **404** | The requested resource does not exist |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |

## updatePostureCheckWithHttpInfo

> CompletableFuture<ApiResponse<Empty>> updatePostureCheckWithHttpInfo(id, postureCheck)

Update all fields on a Posture Checks

Update all fields on a Posture Checks by id

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.PostureChecksApi;
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

        PostureChecksApi apiInstance = new PostureChecksApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        PostureCheckUpdate postureCheck = new PostureCheckUpdate(); // PostureCheckUpdate | A Posture Check update object
        try {
            CompletableFuture<ApiResponse<Empty>> response = apiInstance.updatePostureCheckWithHttpInfo(id, postureCheck);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling PostureChecksApi#updatePostureCheck");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling PostureChecksApi#updatePostureCheck");
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
| **postureCheck** | [**PostureCheckUpdate**](PostureCheckUpdate.md)| A Posture Check update object | |

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
| **200** | The update request was successful and the resource has been altered |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **404** | The requested resource does not exist |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |

