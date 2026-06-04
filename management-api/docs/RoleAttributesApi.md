# RoleAttributesApi

All URIs are relative to *https://demo.ziti.dev/edge/management/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**listEdgeRouterRoleAttributeUsage**](RoleAttributesApi.md#listEdgeRouterRoleAttributeUsage) | **GET** /edge-router-role-attribute-usage | List edge router role attributes with usage breakdowns |
| [**listEdgeRouterRoleAttributeUsageWithHttpInfo**](RoleAttributesApi.md#listEdgeRouterRoleAttributeUsageWithHttpInfo) | **GET** /edge-router-role-attribute-usage | List edge router role attributes with usage breakdowns |
| [**listEdgeRouterRoleAttributes**](RoleAttributesApi.md#listEdgeRouterRoleAttributes) | **GET** /edge-router-role-attributes | List role attributes in use by edge routers |
| [**listEdgeRouterRoleAttributesWithHttpInfo**](RoleAttributesApi.md#listEdgeRouterRoleAttributesWithHttpInfo) | **GET** /edge-router-role-attributes | List role attributes in use by edge routers |
| [**listIdentityRoleAttributeUsage**](RoleAttributesApi.md#listIdentityRoleAttributeUsage) | **GET** /identity-role-attribute-usage | List identity role attributes with usage breakdowns |
| [**listIdentityRoleAttributeUsageWithHttpInfo**](RoleAttributesApi.md#listIdentityRoleAttributeUsageWithHttpInfo) | **GET** /identity-role-attribute-usage | List identity role attributes with usage breakdowns |
| [**listIdentityRoleAttributes**](RoleAttributesApi.md#listIdentityRoleAttributes) | **GET** /identity-role-attributes | List role attributes in use by identities |
| [**listIdentityRoleAttributesWithHttpInfo**](RoleAttributesApi.md#listIdentityRoleAttributesWithHttpInfo) | **GET** /identity-role-attributes | List role attributes in use by identities |
| [**listPostureCheckRoleAttributeUsage**](RoleAttributesApi.md#listPostureCheckRoleAttributeUsage) | **GET** /posture-check-role-attribute-usage | List posture check role attributes with usage breakdowns |
| [**listPostureCheckRoleAttributeUsageWithHttpInfo**](RoleAttributesApi.md#listPostureCheckRoleAttributeUsageWithHttpInfo) | **GET** /posture-check-role-attribute-usage | List posture check role attributes with usage breakdowns |
| [**listPostureCheckRoleAttributes**](RoleAttributesApi.md#listPostureCheckRoleAttributes) | **GET** /posture-check-role-attributes | List role attributes in use by posture checks |
| [**listPostureCheckRoleAttributesWithHttpInfo**](RoleAttributesApi.md#listPostureCheckRoleAttributesWithHttpInfo) | **GET** /posture-check-role-attributes | List role attributes in use by posture checks |
| [**listServiceRoleAttributeUsage**](RoleAttributesApi.md#listServiceRoleAttributeUsage) | **GET** /service-role-attribute-usage | List service role attributes with usage breakdowns |
| [**listServiceRoleAttributeUsageWithHttpInfo**](RoleAttributesApi.md#listServiceRoleAttributeUsageWithHttpInfo) | **GET** /service-role-attribute-usage | List service role attributes with usage breakdowns |
| [**listServiceRoleAttributes**](RoleAttributesApi.md#listServiceRoleAttributes) | **GET** /service-role-attributes | List role attributes in use by services |
| [**listServiceRoleAttributesWithHttpInfo**](RoleAttributesApi.md#listServiceRoleAttributesWithHttpInfo) | **GET** /service-role-attributes | List role attributes in use by services |



## listEdgeRouterRoleAttributeUsage

> CompletableFuture<ListRoleAttributeUsageEnvelope> listEdgeRouterRoleAttributeUsage(limit, offset, filter, withIds)

List edge router role attributes with usage breakdowns

Retrieves edge router role attributes along with per-source usage counts. Each response item reports how many edge routers declare the attribute and how many edge-router policies or service-edge-router policies reference it. When &#x60;withIds&#x3D;true&#x60;, the entity ids backing each count are also returned. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.RoleAttributesApi;
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

        RoleAttributesApi apiInstance = new RoleAttributesApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        Boolean withIds = true; // Boolean | When true, each usage breakdown in the response also includes the list of entity ids that contribute to the count. Defaults to false to keep responses small when the id list is not needed. 
        try {
            CompletableFuture<ListRoleAttributeUsageEnvelope> result = apiInstance.listEdgeRouterRoleAttributeUsage(limit, offset, filter, withIds);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling RoleAttributesApi#listEdgeRouterRoleAttributeUsage");
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
| **withIds** | **Boolean**| When true, each usage breakdown in the response also includes the list of entity ids that contribute to the count. Defaults to false to keep responses small when the id list is not needed.  | [optional] |

### Return type

CompletableFuture<[**ListRoleAttributeUsageEnvelope**](ListRoleAttributeUsageEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of role attributes with per-source usage breakdowns |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |

## listEdgeRouterRoleAttributeUsageWithHttpInfo

> CompletableFuture<ApiResponse<ListRoleAttributeUsageEnvelope>> listEdgeRouterRoleAttributeUsageWithHttpInfo(limit, offset, filter, withIds)

List edge router role attributes with usage breakdowns

Retrieves edge router role attributes along with per-source usage counts. Each response item reports how many edge routers declare the attribute and how many edge-router policies or service-edge-router policies reference it. When &#x60;withIds&#x3D;true&#x60;, the entity ids backing each count are also returned. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.RoleAttributesApi;
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

        RoleAttributesApi apiInstance = new RoleAttributesApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        Boolean withIds = true; // Boolean | When true, each usage breakdown in the response also includes the list of entity ids that contribute to the count. Defaults to false to keep responses small when the id list is not needed. 
        try {
            CompletableFuture<ApiResponse<ListRoleAttributeUsageEnvelope>> response = apiInstance.listEdgeRouterRoleAttributeUsageWithHttpInfo(limit, offset, filter, withIds);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling RoleAttributesApi#listEdgeRouterRoleAttributeUsage");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling RoleAttributesApi#listEdgeRouterRoleAttributeUsage");
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
| **withIds** | **Boolean**| When true, each usage breakdown in the response also includes the list of entity ids that contribute to the count. Defaults to false to keep responses small when the id list is not needed.  | [optional] |

### Return type

CompletableFuture<ApiResponse<[**ListRoleAttributeUsageEnvelope**](ListRoleAttributeUsageEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of role attributes with per-source usage breakdowns |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |


## listEdgeRouterRoleAttributes

> CompletableFuture<ListRoleAttributesEnvelope> listEdgeRouterRoleAttributes(limit, offset, filter)

List role attributes in use by edge routers

Retrieves a list of role attributes in use by edge routers; supports filtering, sorting, and pagination. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.RoleAttributesApi;
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

        RoleAttributesApi apiInstance = new RoleAttributesApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        try {
            CompletableFuture<ListRoleAttributesEnvelope> result = apiInstance.listEdgeRouterRoleAttributes(limit, offset, filter);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling RoleAttributesApi#listEdgeRouterRoleAttributes");
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

CompletableFuture<[**ListRoleAttributesEnvelope**](ListRoleAttributesEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of role attributes |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |

## listEdgeRouterRoleAttributesWithHttpInfo

> CompletableFuture<ApiResponse<ListRoleAttributesEnvelope>> listEdgeRouterRoleAttributesWithHttpInfo(limit, offset, filter)

List role attributes in use by edge routers

Retrieves a list of role attributes in use by edge routers; supports filtering, sorting, and pagination. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.RoleAttributesApi;
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

        RoleAttributesApi apiInstance = new RoleAttributesApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        try {
            CompletableFuture<ApiResponse<ListRoleAttributesEnvelope>> response = apiInstance.listEdgeRouterRoleAttributesWithHttpInfo(limit, offset, filter);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling RoleAttributesApi#listEdgeRouterRoleAttributes");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling RoleAttributesApi#listEdgeRouterRoleAttributes");
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

CompletableFuture<ApiResponse<[**ListRoleAttributesEnvelope**](ListRoleAttributesEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of role attributes |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |


## listIdentityRoleAttributeUsage

> CompletableFuture<ListRoleAttributeUsageEnvelope> listIdentityRoleAttributeUsage(limit, offset, filter, withIds)

List identity role attributes with usage breakdowns

Retrieves identity role attributes along with per-source usage counts. Each response item reports how many identities declare the attribute and how many service policies or edge-router policies reference it. When &#x60;withIds&#x3D;true&#x60;, the entity ids backing each count are also returned. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.RoleAttributesApi;
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

        RoleAttributesApi apiInstance = new RoleAttributesApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        Boolean withIds = true; // Boolean | When true, each usage breakdown in the response also includes the list of entity ids that contribute to the count. Defaults to false to keep responses small when the id list is not needed. 
        try {
            CompletableFuture<ListRoleAttributeUsageEnvelope> result = apiInstance.listIdentityRoleAttributeUsage(limit, offset, filter, withIds);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling RoleAttributesApi#listIdentityRoleAttributeUsage");
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
| **withIds** | **Boolean**| When true, each usage breakdown in the response also includes the list of entity ids that contribute to the count. Defaults to false to keep responses small when the id list is not needed.  | [optional] |

### Return type

CompletableFuture<[**ListRoleAttributeUsageEnvelope**](ListRoleAttributeUsageEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of role attributes with per-source usage breakdowns |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |

## listIdentityRoleAttributeUsageWithHttpInfo

> CompletableFuture<ApiResponse<ListRoleAttributeUsageEnvelope>> listIdentityRoleAttributeUsageWithHttpInfo(limit, offset, filter, withIds)

List identity role attributes with usage breakdowns

Retrieves identity role attributes along with per-source usage counts. Each response item reports how many identities declare the attribute and how many service policies or edge-router policies reference it. When &#x60;withIds&#x3D;true&#x60;, the entity ids backing each count are also returned. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.RoleAttributesApi;
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

        RoleAttributesApi apiInstance = new RoleAttributesApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        Boolean withIds = true; // Boolean | When true, each usage breakdown in the response also includes the list of entity ids that contribute to the count. Defaults to false to keep responses small when the id list is not needed. 
        try {
            CompletableFuture<ApiResponse<ListRoleAttributeUsageEnvelope>> response = apiInstance.listIdentityRoleAttributeUsageWithHttpInfo(limit, offset, filter, withIds);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling RoleAttributesApi#listIdentityRoleAttributeUsage");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling RoleAttributesApi#listIdentityRoleAttributeUsage");
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
| **withIds** | **Boolean**| When true, each usage breakdown in the response also includes the list of entity ids that contribute to the count. Defaults to false to keep responses small when the id list is not needed.  | [optional] |

### Return type

CompletableFuture<ApiResponse<[**ListRoleAttributeUsageEnvelope**](ListRoleAttributeUsageEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of role attributes with per-source usage breakdowns |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |


## listIdentityRoleAttributes

> CompletableFuture<ListRoleAttributesEnvelope> listIdentityRoleAttributes(limit, offset, filter)

List role attributes in use by identities

Retrieves a list of role attributes in use by identities; supports filtering, sorting, and pagination. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.RoleAttributesApi;
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

        RoleAttributesApi apiInstance = new RoleAttributesApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        try {
            CompletableFuture<ListRoleAttributesEnvelope> result = apiInstance.listIdentityRoleAttributes(limit, offset, filter);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling RoleAttributesApi#listIdentityRoleAttributes");
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

CompletableFuture<[**ListRoleAttributesEnvelope**](ListRoleAttributesEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of role attributes |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |

## listIdentityRoleAttributesWithHttpInfo

> CompletableFuture<ApiResponse<ListRoleAttributesEnvelope>> listIdentityRoleAttributesWithHttpInfo(limit, offset, filter)

List role attributes in use by identities

Retrieves a list of role attributes in use by identities; supports filtering, sorting, and pagination. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.RoleAttributesApi;
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

        RoleAttributesApi apiInstance = new RoleAttributesApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        try {
            CompletableFuture<ApiResponse<ListRoleAttributesEnvelope>> response = apiInstance.listIdentityRoleAttributesWithHttpInfo(limit, offset, filter);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling RoleAttributesApi#listIdentityRoleAttributes");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling RoleAttributesApi#listIdentityRoleAttributes");
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

CompletableFuture<ApiResponse<[**ListRoleAttributesEnvelope**](ListRoleAttributesEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of role attributes |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |


## listPostureCheckRoleAttributeUsage

> CompletableFuture<ListRoleAttributeUsageEnvelope> listPostureCheckRoleAttributeUsage(limit, offset, filter, withIds)

List posture check role attributes with usage breakdowns

Retrieves posture check role attributes along with per-source usage counts. Each response item reports how many posture checks declare the attribute and how many service policies reference it. When &#x60;withIds&#x3D;true&#x60;, the entity ids backing each count are also returned. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.RoleAttributesApi;
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

        RoleAttributesApi apiInstance = new RoleAttributesApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        Boolean withIds = true; // Boolean | When true, each usage breakdown in the response also includes the list of entity ids that contribute to the count. Defaults to false to keep responses small when the id list is not needed. 
        try {
            CompletableFuture<ListRoleAttributeUsageEnvelope> result = apiInstance.listPostureCheckRoleAttributeUsage(limit, offset, filter, withIds);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling RoleAttributesApi#listPostureCheckRoleAttributeUsage");
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
| **withIds** | **Boolean**| When true, each usage breakdown in the response also includes the list of entity ids that contribute to the count. Defaults to false to keep responses small when the id list is not needed.  | [optional] |

### Return type

CompletableFuture<[**ListRoleAttributeUsageEnvelope**](ListRoleAttributeUsageEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of role attributes with per-source usage breakdowns |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |

## listPostureCheckRoleAttributeUsageWithHttpInfo

> CompletableFuture<ApiResponse<ListRoleAttributeUsageEnvelope>> listPostureCheckRoleAttributeUsageWithHttpInfo(limit, offset, filter, withIds)

List posture check role attributes with usage breakdowns

Retrieves posture check role attributes along with per-source usage counts. Each response item reports how many posture checks declare the attribute and how many service policies reference it. When &#x60;withIds&#x3D;true&#x60;, the entity ids backing each count are also returned. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.RoleAttributesApi;
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

        RoleAttributesApi apiInstance = new RoleAttributesApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        Boolean withIds = true; // Boolean | When true, each usage breakdown in the response also includes the list of entity ids that contribute to the count. Defaults to false to keep responses small when the id list is not needed. 
        try {
            CompletableFuture<ApiResponse<ListRoleAttributeUsageEnvelope>> response = apiInstance.listPostureCheckRoleAttributeUsageWithHttpInfo(limit, offset, filter, withIds);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling RoleAttributesApi#listPostureCheckRoleAttributeUsage");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling RoleAttributesApi#listPostureCheckRoleAttributeUsage");
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
| **withIds** | **Boolean**| When true, each usage breakdown in the response also includes the list of entity ids that contribute to the count. Defaults to false to keep responses small when the id list is not needed.  | [optional] |

### Return type

CompletableFuture<ApiResponse<[**ListRoleAttributeUsageEnvelope**](ListRoleAttributeUsageEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of role attributes with per-source usage breakdowns |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |


## listPostureCheckRoleAttributes

> CompletableFuture<ListRoleAttributesEnvelope> listPostureCheckRoleAttributes(limit, offset, filter)

List role attributes in use by posture checks

Retrieves a list of role attributes in use by posture checks; supports filtering, sorting, and pagination. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.RoleAttributesApi;
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

        RoleAttributesApi apiInstance = new RoleAttributesApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        try {
            CompletableFuture<ListRoleAttributesEnvelope> result = apiInstance.listPostureCheckRoleAttributes(limit, offset, filter);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling RoleAttributesApi#listPostureCheckRoleAttributes");
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

CompletableFuture<[**ListRoleAttributesEnvelope**](ListRoleAttributesEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of role attributes |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |

## listPostureCheckRoleAttributesWithHttpInfo

> CompletableFuture<ApiResponse<ListRoleAttributesEnvelope>> listPostureCheckRoleAttributesWithHttpInfo(limit, offset, filter)

List role attributes in use by posture checks

Retrieves a list of role attributes in use by posture checks; supports filtering, sorting, and pagination. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.RoleAttributesApi;
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

        RoleAttributesApi apiInstance = new RoleAttributesApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        try {
            CompletableFuture<ApiResponse<ListRoleAttributesEnvelope>> response = apiInstance.listPostureCheckRoleAttributesWithHttpInfo(limit, offset, filter);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling RoleAttributesApi#listPostureCheckRoleAttributes");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling RoleAttributesApi#listPostureCheckRoleAttributes");
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

CompletableFuture<ApiResponse<[**ListRoleAttributesEnvelope**](ListRoleAttributesEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of role attributes |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |


## listServiceRoleAttributeUsage

> CompletableFuture<ListRoleAttributeUsageEnvelope> listServiceRoleAttributeUsage(limit, offset, filter, withIds)

List service role attributes with usage breakdowns

Retrieves service role attributes along with per-source usage counts. Each response item reports how many services declare the attribute and how many service policies or service-edge-router policies reference it. When &#x60;withIds&#x3D;true&#x60;, the entity ids backing each count are also returned. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.RoleAttributesApi;
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

        RoleAttributesApi apiInstance = new RoleAttributesApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        Boolean withIds = true; // Boolean | When true, each usage breakdown in the response also includes the list of entity ids that contribute to the count. Defaults to false to keep responses small when the id list is not needed. 
        try {
            CompletableFuture<ListRoleAttributeUsageEnvelope> result = apiInstance.listServiceRoleAttributeUsage(limit, offset, filter, withIds);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling RoleAttributesApi#listServiceRoleAttributeUsage");
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
| **withIds** | **Boolean**| When true, each usage breakdown in the response also includes the list of entity ids that contribute to the count. Defaults to false to keep responses small when the id list is not needed.  | [optional] |

### Return type

CompletableFuture<[**ListRoleAttributeUsageEnvelope**](ListRoleAttributeUsageEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of role attributes with per-source usage breakdowns |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |

## listServiceRoleAttributeUsageWithHttpInfo

> CompletableFuture<ApiResponse<ListRoleAttributeUsageEnvelope>> listServiceRoleAttributeUsageWithHttpInfo(limit, offset, filter, withIds)

List service role attributes with usage breakdowns

Retrieves service role attributes along with per-source usage counts. Each response item reports how many services declare the attribute and how many service policies or service-edge-router policies reference it. When &#x60;withIds&#x3D;true&#x60;, the entity ids backing each count are also returned. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.RoleAttributesApi;
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

        RoleAttributesApi apiInstance = new RoleAttributesApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        Boolean withIds = true; // Boolean | When true, each usage breakdown in the response also includes the list of entity ids that contribute to the count. Defaults to false to keep responses small when the id list is not needed. 
        try {
            CompletableFuture<ApiResponse<ListRoleAttributeUsageEnvelope>> response = apiInstance.listServiceRoleAttributeUsageWithHttpInfo(limit, offset, filter, withIds);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling RoleAttributesApi#listServiceRoleAttributeUsage");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling RoleAttributesApi#listServiceRoleAttributeUsage");
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
| **withIds** | **Boolean**| When true, each usage breakdown in the response also includes the list of entity ids that contribute to the count. Defaults to false to keep responses small when the id list is not needed.  | [optional] |

### Return type

CompletableFuture<ApiResponse<[**ListRoleAttributeUsageEnvelope**](ListRoleAttributeUsageEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of role attributes with per-source usage breakdowns |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |


## listServiceRoleAttributes

> CompletableFuture<ListRoleAttributesEnvelope> listServiceRoleAttributes(limit, offset, filter)

List role attributes in use by services

Retrieves a list of role attributes in use by services; supports filtering, sorting, and pagination. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.RoleAttributesApi;
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

        RoleAttributesApi apiInstance = new RoleAttributesApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        try {
            CompletableFuture<ListRoleAttributesEnvelope> result = apiInstance.listServiceRoleAttributes(limit, offset, filter);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling RoleAttributesApi#listServiceRoleAttributes");
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

CompletableFuture<[**ListRoleAttributesEnvelope**](ListRoleAttributesEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of role attributes |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |

## listServiceRoleAttributesWithHttpInfo

> CompletableFuture<ApiResponse<ListRoleAttributesEnvelope>> listServiceRoleAttributesWithHttpInfo(limit, offset, filter)

List role attributes in use by services

Retrieves a list of role attributes in use by services; supports filtering, sorting, and pagination. Requires admin access. 

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.RoleAttributesApi;
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

        RoleAttributesApi apiInstance = new RoleAttributesApi(defaultClient);
        Integer limit = 56; // Integer | 
        Integer offset = 56; // Integer | 
        String filter = "filter_example"; // String | 
        try {
            CompletableFuture<ApiResponse<ListRoleAttributesEnvelope>> response = apiInstance.listServiceRoleAttributesWithHttpInfo(limit, offset, filter);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling RoleAttributesApi#listServiceRoleAttributes");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling RoleAttributesApi#listServiceRoleAttributes");
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

CompletableFuture<ApiResponse<[**ListRoleAttributesEnvelope**](ListRoleAttributesEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of role attributes |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **400** | The supplied request contains invalid fields or could not be parsed (json and non-json bodies). The error&#39;s code, message, and cause fields can be inspected for further information |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **401** | The supplied session does not have the correct access rights to request this resource |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  * WWW-Authenticate - Denotes different type of security token related information <br>  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  * WWW-Authenticate - Denotes different type of security token related information <br>  |

