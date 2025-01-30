# InformationalApi

All URIs are relative to *https://demo.ziti.dev/edge/management/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**detailSpec**](InformationalApi.md#detailSpec) | **GET** /specs/{id} | Return a single spec resource |
| [**detailSpecWithHttpInfo**](InformationalApi.md#detailSpecWithHttpInfo) | **GET** /specs/{id} | Return a single spec resource |
| [**detailSpecBody**](InformationalApi.md#detailSpecBody) | **GET** /specs/{id}/spec | Returns the spec&#39;s file |
| [**detailSpecBodyWithHttpInfo**](InformationalApi.md#detailSpecBodyWithHttpInfo) | **GET** /specs/{id}/spec | Returns the spec&#39;s file |
| [**listEnumeratedCapabilities**](InformationalApi.md#listEnumeratedCapabilities) | **GET** /enumerated-capabilities | Returns all capabilities this version of the controller is aware of, enabled or not. |
| [**listEnumeratedCapabilitiesWithHttpInfo**](InformationalApi.md#listEnumeratedCapabilitiesWithHttpInfo) | **GET** /enumerated-capabilities | Returns all capabilities this version of the controller is aware of, enabled or not. |
| [**listRoot**](InformationalApi.md#listRoot) | **GET** / | Returns version information |
| [**listRootWithHttpInfo**](InformationalApi.md#listRootWithHttpInfo) | **GET** / | Returns version information |
| [**listSpecs**](InformationalApi.md#listSpecs) | **GET** /specs | Returns a list of API specs |
| [**listSpecsWithHttpInfo**](InformationalApi.md#listSpecsWithHttpInfo) | **GET** /specs | Returns a list of API specs |
| [**listSummary**](InformationalApi.md#listSummary) | **GET** /summary | Returns a list of accessible resource counts |
| [**listSummaryWithHttpInfo**](InformationalApi.md#listSummaryWithHttpInfo) | **GET** /summary | Returns a list of accessible resource counts |
| [**listVersion**](InformationalApi.md#listVersion) | **GET** /version | Returns version information |
| [**listVersionWithHttpInfo**](InformationalApi.md#listVersionWithHttpInfo) | **GET** /version | Returns version information |



## detailSpec

> CompletableFuture<DetailSpecEnvelope> detailSpec(id)

Return a single spec resource

Returns single spec resource embedded within the controller for consumption/documentation/code geneartion

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.models.*;
import org.openziti.management.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");

        InformationalApi apiInstance = new InformationalApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<DetailSpecEnvelope> result = apiInstance.detailSpec(id);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling InformationalApi#detailSpec");
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

CompletableFuture<[**DetailSpecEnvelope**](DetailSpecEnvelope.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A single specification |  -  |

## detailSpecWithHttpInfo

> CompletableFuture<ApiResponse<DetailSpecEnvelope>> detailSpec detailSpecWithHttpInfo(id)

Return a single spec resource

Returns single spec resource embedded within the controller for consumption/documentation/code geneartion

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.models.*;
import org.openziti.management.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");

        InformationalApi apiInstance = new InformationalApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ApiResponse<DetailSpecEnvelope>> response = apiInstance.detailSpecWithHttpInfo(id);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling InformationalApi#detailSpec");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling InformationalApi#detailSpec");
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

CompletableFuture<ApiResponse<[**DetailSpecEnvelope**](DetailSpecEnvelope.md)>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A single specification |  -  |


## detailSpecBody

> CompletableFuture<DetailSpecBodyEnvelope> detailSpecBody(id)

Returns the spec&#39;s file

Return the body of the specification (i.e. Swagger, OpenAPI 2.0, 3.0, etc).

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.models.*;
import org.openziti.management.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");

        InformationalApi apiInstance = new InformationalApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<DetailSpecBodyEnvelope> result = apiInstance.detailSpecBody(id);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling InformationalApi#detailSpecBody");
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

CompletableFuture<[**DetailSpecBodyEnvelope**](DetailSpecBodyEnvelope.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: text/yaml, application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Returns the document that represents the specification |  -  |

## detailSpecBodyWithHttpInfo

> CompletableFuture<ApiResponse<DetailSpecBodyEnvelope>> detailSpecBody detailSpecBodyWithHttpInfo(id)

Returns the spec&#39;s file

Return the body of the specification (i.e. Swagger, OpenAPI 2.0, 3.0, etc).

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.models.*;
import org.openziti.management.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");

        InformationalApi apiInstance = new InformationalApi(defaultClient);
        String id = "id_example"; // String | The id of the requested resource
        try {
            CompletableFuture<ApiResponse<DetailSpecBodyEnvelope>> response = apiInstance.detailSpecBodyWithHttpInfo(id);
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling InformationalApi#detailSpecBody");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling InformationalApi#detailSpecBody");
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

CompletableFuture<ApiResponse<[**DetailSpecBodyEnvelope**](DetailSpecBodyEnvelope.md)>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: text/yaml, application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Returns the document that represents the specification |  -  |


## listEnumeratedCapabilities

> CompletableFuture<ListEnumeratedCapabilitiesEnvelope> listEnumeratedCapabilities()

Returns all capabilities this version of the controller is aware of, enabled or not.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.models.*;
import org.openziti.management.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");

        InformationalApi apiInstance = new InformationalApi(defaultClient);
        try {
            CompletableFuture<ListEnumeratedCapabilitiesEnvelope> result = apiInstance.listEnumeratedCapabilities();
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling InformationalApi#listEnumeratedCapabilities");
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

CompletableFuture<[**ListEnumeratedCapabilitiesEnvelope**](ListEnumeratedCapabilitiesEnvelope.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A typed and enumerated list of capabilities |  -  |

## listEnumeratedCapabilitiesWithHttpInfo

> CompletableFuture<ApiResponse<ListEnumeratedCapabilitiesEnvelope>> listEnumeratedCapabilities listEnumeratedCapabilitiesWithHttpInfo()

Returns all capabilities this version of the controller is aware of, enabled or not.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.models.*;
import org.openziti.management.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");

        InformationalApi apiInstance = new InformationalApi(defaultClient);
        try {
            CompletableFuture<ApiResponse<ListEnumeratedCapabilitiesEnvelope>> response = apiInstance.listEnumeratedCapabilitiesWithHttpInfo();
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling InformationalApi#listEnumeratedCapabilities");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling InformationalApi#listEnumeratedCapabilities");
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

CompletableFuture<ApiResponse<[**ListEnumeratedCapabilitiesEnvelope**](ListEnumeratedCapabilitiesEnvelope.md)>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A typed and enumerated list of capabilities |  -  |


## listRoot

> CompletableFuture<ListVersionEnvelope> listRoot()

Returns version information

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.models.*;
import org.openziti.management.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");

        InformationalApi apiInstance = new InformationalApi(defaultClient);
        try {
            CompletableFuture<ListVersionEnvelope> result = apiInstance.listRoot();
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling InformationalApi#listRoot");
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

CompletableFuture<[**ListVersionEnvelope**](ListVersionEnvelope.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Version information for the controller |  -  |

## listRootWithHttpInfo

> CompletableFuture<ApiResponse<ListVersionEnvelope>> listRoot listRootWithHttpInfo()

Returns version information

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.models.*;
import org.openziti.management.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");

        InformationalApi apiInstance = new InformationalApi(defaultClient);
        try {
            CompletableFuture<ApiResponse<ListVersionEnvelope>> response = apiInstance.listRootWithHttpInfo();
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling InformationalApi#listRoot");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling InformationalApi#listRoot");
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

CompletableFuture<ApiResponse<[**ListVersionEnvelope**](ListVersionEnvelope.md)>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Version information for the controller |  -  |


## listSpecs

> CompletableFuture<ListSpecsEnvelope> listSpecs()

Returns a list of API specs

Returns a list of spec files embedded within the controller for consumption/documentation/code geneartion

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.models.*;
import org.openziti.management.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");

        InformationalApi apiInstance = new InformationalApi(defaultClient);
        try {
            CompletableFuture<ListSpecsEnvelope> result = apiInstance.listSpecs();
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling InformationalApi#listSpecs");
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

CompletableFuture<[**ListSpecsEnvelope**](ListSpecsEnvelope.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of specifications |  -  |

## listSpecsWithHttpInfo

> CompletableFuture<ApiResponse<ListSpecsEnvelope>> listSpecs listSpecsWithHttpInfo()

Returns a list of API specs

Returns a list of spec files embedded within the controller for consumption/documentation/code geneartion

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.models.*;
import org.openziti.management.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");

        InformationalApi apiInstance = new InformationalApi(defaultClient);
        try {
            CompletableFuture<ApiResponse<ListSpecsEnvelope>> response = apiInstance.listSpecsWithHttpInfo();
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling InformationalApi#listSpecs");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling InformationalApi#listSpecs");
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

CompletableFuture<ApiResponse<[**ListSpecsEnvelope**](ListSpecsEnvelope.md)>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of specifications |  -  |


## listSummary

> CompletableFuture<ListSummaryCountsEnvelope> listSummary()

Returns a list of accessible resource counts

This endpoint is usefull for UIs that wish to display UI elements with counts.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.InformationalApi;
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

        InformationalApi apiInstance = new InformationalApi(defaultClient);
        try {
            CompletableFuture<ListSummaryCountsEnvelope> result = apiInstance.listSummary();
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling InformationalApi#listSummary");
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

CompletableFuture<[**ListSummaryCountsEnvelope**](ListSummaryCountsEnvelope.md)>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Entity counts scopped to the current identitie&#39;s access |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## listSummaryWithHttpInfo

> CompletableFuture<ApiResponse<ListSummaryCountsEnvelope>> listSummary listSummaryWithHttpInfo()

Returns a list of accessible resource counts

This endpoint is usefull for UIs that wish to display UI elements with counts.

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.auth.*;
import org.openziti.management.models.*;
import org.openziti.management.api.InformationalApi;
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

        InformationalApi apiInstance = new InformationalApi(defaultClient);
        try {
            CompletableFuture<ApiResponse<ListSummaryCountsEnvelope>> response = apiInstance.listSummaryWithHttpInfo();
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling InformationalApi#listSummary");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling InformationalApi#listSummary");
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

CompletableFuture<ApiResponse<[**ListSummaryCountsEnvelope**](ListSummaryCountsEnvelope.md)>>


### Authorization

[ztSession](../README.md#ztSession), [oauth2](../README.md#oauth2)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Entity counts scopped to the current identitie&#39;s access |  -  |
| **401** | The supplied session does not have the correct access rights to request this resource |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## listVersion

> CompletableFuture<ListVersionEnvelope> listVersion()

Returns version information

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.Configuration;
import org.openziti.management.models.*;
import org.openziti.management.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");

        InformationalApi apiInstance = new InformationalApi(defaultClient);
        try {
            CompletableFuture<ListVersionEnvelope> result = apiInstance.listVersion();
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling InformationalApi#listVersion");
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

CompletableFuture<[**ListVersionEnvelope**](ListVersionEnvelope.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Version information for the controller |  -  |

## listVersionWithHttpInfo

> CompletableFuture<ApiResponse<ListVersionEnvelope>> listVersion listVersionWithHttpInfo()

Returns version information

### Example

```java
// Import classes:
import org.openziti.management.ApiClient;
import org.openziti.management.ApiException;
import org.openziti.management.ApiResponse;
import org.openziti.management.Configuration;
import org.openziti.management.models.*;
import org.openziti.management.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/management/v1");

        InformationalApi apiInstance = new InformationalApi(defaultClient);
        try {
            CompletableFuture<ApiResponse<ListVersionEnvelope>> response = apiInstance.listVersionWithHttpInfo();
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling InformationalApi#listVersion");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling InformationalApi#listVersion");
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

CompletableFuture<ApiResponse<[**ListVersionEnvelope**](ListVersionEnvelope.md)>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Version information for the controller |  -  |

