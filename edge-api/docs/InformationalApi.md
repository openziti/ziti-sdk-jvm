# InformationalApi

All URIs are relative to *https://demo.ziti.dev/edge/client/v1*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**detailSpec**](InformationalApi.md#detailSpec) | **GET** /specs/{id} | Return a single spec resource |
| [**detailSpecWithHttpInfo**](InformationalApi.md#detailSpecWithHttpInfo) | **GET** /specs/{id} | Return a single spec resource |
| [**detailSpecBody**](InformationalApi.md#detailSpecBody) | **GET** /specs/{id}/spec | Returns the spec&#39;s file |
| [**detailSpecBodyWithHttpInfo**](InformationalApi.md#detailSpecBodyWithHttpInfo) | **GET** /specs/{id}/spec | Returns the spec&#39;s file |
| [**listEnumeratedCapabilities**](InformationalApi.md#listEnumeratedCapabilities) | **GET** /enumerated-capabilities | Returns all capabilities this version of the controller is aware of, enabled or not. |
| [**listEnumeratedCapabilitiesWithHttpInfo**](InformationalApi.md#listEnumeratedCapabilitiesWithHttpInfo) | **GET** /enumerated-capabilities | Returns all capabilities this version of the controller is aware of, enabled or not. |
| [**listProtocols**](InformationalApi.md#listProtocols) | **GET** /protocols | Return a list of the listening Edge protocols |
| [**listProtocolsWithHttpInfo**](InformationalApi.md#listProtocolsWithHttpInfo) | **GET** /protocols | Return a list of the listening Edge protocols |
| [**listRoot**](InformationalApi.md#listRoot) | **GET** / | Returns version information |
| [**listRootWithHttpInfo**](InformationalApi.md#listRootWithHttpInfo) | **GET** / | Returns version information |
| [**listSpecs**](InformationalApi.md#listSpecs) | **GET** /specs | Returns a list of API specs |
| [**listSpecsWithHttpInfo**](InformationalApi.md#listSpecsWithHttpInfo) | **GET** /specs | Returns a list of API specs |
| [**listVersion**](InformationalApi.md#listVersion) | **GET** /version | Returns version information |
| [**listVersionWithHttpInfo**](InformationalApi.md#listVersionWithHttpInfo) | **GET** /version | Returns version information |



## detailSpec

> CompletableFuture<DetailSpecEnvelope> detailSpec(id)

Return a single spec resource

Returns single spec resource embedded within the controller for consumption/documentation/code geneartion

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

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
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

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
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

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
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

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
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

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
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

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


## listProtocols

> CompletableFuture<ListProtocolsEnvelope> listProtocols()

Return a list of the listening Edge protocols

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        InformationalApi apiInstance = new InformationalApi(defaultClient);
        try {
            CompletableFuture<ListProtocolsEnvelope> result = apiInstance.listProtocols();
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling InformationalApi#listProtocols");
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

CompletableFuture<[**ListProtocolsEnvelope**](ListProtocolsEnvelope.md)>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of supported Edge protocols |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |

## listProtocolsWithHttpInfo

> CompletableFuture<ApiResponse<ListProtocolsEnvelope>> listProtocols listProtocolsWithHttpInfo()

Return a list of the listening Edge protocols

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

        InformationalApi apiInstance = new InformationalApi(defaultClient);
        try {
            CompletableFuture<ApiResponse<ListProtocolsEnvelope>> response = apiInstance.listProtocolsWithHttpInfo();
            System.out.println("Status code: " + response.get().getStatusCode());
            System.out.println("Response headers: " + response.get().getHeaders());
            System.out.println("Response body: " + response.get().getData());
        } catch (InterruptedException | ExecutionException e) {
            ApiException apiException = (ApiException)e.getCause();
            System.err.println("Exception when calling InformationalApi#listProtocols");
            System.err.println("Status code: " + apiException.getCode());
            System.err.println("Response headers: " + apiException.getResponseHeaders());
            System.err.println("Reason: " + apiException.getResponseBody());
            e.printStackTrace();
        } catch (ApiException e) {
            System.err.println("Exception when calling InformationalApi#listProtocols");
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

CompletableFuture<ApiResponse<[**ListProtocolsEnvelope**](ListProtocolsEnvelope.md)>>


### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of supported Edge protocols |  -  |
| **429** | The resource requested is rate limited and the rate limit has been exceeded |  -  |
| **503** | The request could not be completed due to the server being busy or in a temporarily bad state |  -  |


## listRoot

> CompletableFuture<ListVersionEnvelope> listRoot()

Returns version information

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

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
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

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
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

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
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

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


## listVersion

> CompletableFuture<ListVersionEnvelope> listVersion()

Returns version information

### Example

```java
// Import classes:
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

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
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.Configuration;
import org.openziti.edge.models.*;
import org.openziti.edge.api.InformationalApi;
import java.util.concurrent.CompletableFuture;

public class Example {
    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://demo.ziti.dev/edge/client/v1");

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

