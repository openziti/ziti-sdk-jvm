/*
 * Ziti Edge Client
 * OpenZiti Edge Client API
 *
 * The version of the OpenAPI document: 0.26.42
 * Contact: help@openziti.org
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package org.openziti.edge.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.openziti.edge.ApiClient;
import org.openziti.edge.ApiException;
import org.openziti.edge.ApiResponse;
import org.openziti.edge.model.PostureResponseCreate;
import org.openziti.edge.model.PostureResponseEnvelope;

@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:45.850758361-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class PostureChecksApi {
    private final HttpClient memberVarHttpClient;
    private final ObjectMapper memberVarObjectMapper;
    private final String memberVarBaseUri;
    private final Consumer<HttpRequest.Builder> memberVarInterceptor;
    private final Duration memberVarReadTimeout;
    private final Consumer<HttpResponse<InputStream>> memberVarResponseInterceptor;
    private final Consumer<HttpResponse<String>> memberVarAsyncResponseInterceptor;

    public PostureChecksApi() {
        this(new ApiClient());
    }

    public PostureChecksApi(ApiClient apiClient) {
        memberVarHttpClient = apiClient.getHttpClient();
        memberVarObjectMapper = apiClient.getObjectMapper();
        memberVarBaseUri = apiClient.getBaseUri();
        memberVarInterceptor = apiClient.getRequestInterceptor();
        memberVarReadTimeout = apiClient.getReadTimeout();
        memberVarResponseInterceptor = apiClient.getResponseInterceptor();
        memberVarAsyncResponseInterceptor = apiClient.getAsyncResponseInterceptor();
    }

    private ApiException getApiException(String operationId, HttpResponse<String> response) {
        String message =
                formatExceptionMessage(operationId, response.statusCode(), response.body());
        return new ApiException(
                response.statusCode(), message, response.headers(), response.body());
    }

    private String formatExceptionMessage(String operationId, int statusCode, String body) {
        if (body == null || body.isEmpty()) {
            body = "[no body]";
        }
        return operationId + " call failed with: " + statusCode + " - " + body;
    }

    /**
     * Submit a posture response to a posture query Submits posture responses
     *
     * @param postureResponse A Posture Response (required)
     * @return CompletableFuture&lt;PostureResponseEnvelope&gt;
     * @throws ApiException if fails to make API call
     */
    public CompletableFuture<PostureResponseEnvelope> createPostureResponse(
            PostureResponseCreate postureResponse) throws ApiException {
        try {
            HttpRequest.Builder localVarRequestBuilder =
                    createPostureResponseRequestBuilder(postureResponse);
            return memberVarHttpClient
                    .sendAsync(localVarRequestBuilder.build(), HttpResponse.BodyHandlers.ofString())
                    .thenComposeAsync(
                            localVarResponse -> {
                                if (localVarResponse.statusCode() / 100 != 2) {
                                    return CompletableFuture.failedFuture(
                                            getApiException(
                                                    "createPostureResponse", localVarResponse));
                                }
                                try {
                                    String responseBody = localVarResponse.body();
                                    return CompletableFuture.completedFuture(
                                            responseBody == null || responseBody.isBlank()
                                                    ? null
                                                    : memberVarObjectMapper.readValue(
                                                            responseBody,
                                                            new TypeReference<
                                                                    PostureResponseEnvelope>() {}));
                                } catch (IOException e) {
                                    return CompletableFuture.failedFuture(new ApiException(e));
                                }
                            });
        } catch (ApiException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Submit a posture response to a posture query Submits posture responses
     *
     * @param postureResponse A Posture Response (required)
     * @return CompletableFuture&lt;ApiResponse&lt;PostureResponseEnvelope&gt;&gt;
     * @throws ApiException if fails to make API call
     */
    public CompletableFuture<ApiResponse<PostureResponseEnvelope>>
            createPostureResponseWithHttpInfo(PostureResponseCreate postureResponse)
                    throws ApiException {
        try {
            HttpRequest.Builder localVarRequestBuilder =
                    createPostureResponseRequestBuilder(postureResponse);
            return memberVarHttpClient
                    .sendAsync(localVarRequestBuilder.build(), HttpResponse.BodyHandlers.ofString())
                    .thenComposeAsync(
                            localVarResponse -> {
                                if (memberVarAsyncResponseInterceptor != null) {
                                    memberVarAsyncResponseInterceptor.accept(localVarResponse);
                                }
                                if (localVarResponse.statusCode() / 100 != 2) {
                                    return CompletableFuture.failedFuture(
                                            getApiException(
                                                    "createPostureResponse", localVarResponse));
                                }
                                try {
                                    String responseBody = localVarResponse.body();
                                    return CompletableFuture.completedFuture(
                                            new ApiResponse<PostureResponseEnvelope>(
                                                    localVarResponse.statusCode(),
                                                    localVarResponse.headers().map(),
                                                    responseBody == null || responseBody.isBlank()
                                                            ? null
                                                            : memberVarObjectMapper.readValue(
                                                                    responseBody,
                                                                    new TypeReference<
                                                                            PostureResponseEnvelope>() {})));
                                } catch (IOException e) {
                                    return CompletableFuture.failedFuture(new ApiException(e));
                                }
                            });
        } catch (ApiException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    private HttpRequest.Builder createPostureResponseRequestBuilder(
            PostureResponseCreate postureResponse) throws ApiException {
        // verify the required parameter 'postureResponse' is set
        if (postureResponse == null) {
            throw new ApiException(
                    400,
                    "Missing the required parameter 'postureResponse' when calling"
                            + " createPostureResponse");
        }

        HttpRequest.Builder localVarRequestBuilder = HttpRequest.newBuilder();

        String localVarPath = "/posture-response";

        localVarRequestBuilder.uri(URI.create(memberVarBaseUri + localVarPath));

        localVarRequestBuilder.header("Content-Type", "application/json");
        localVarRequestBuilder.header("Accept", "application/json");

        try {
            byte[] localVarPostBody = memberVarObjectMapper.writeValueAsBytes(postureResponse);
            localVarRequestBuilder.method(
                    "POST", HttpRequest.BodyPublishers.ofByteArray(localVarPostBody));
        } catch (IOException e) {
            throw new ApiException(e);
        }
        if (memberVarReadTimeout != null) {
            localVarRequestBuilder.timeout(memberVarReadTimeout);
        }
        if (memberVarInterceptor != null) {
            memberVarInterceptor.accept(localVarRequestBuilder);
        }
        return localVarRequestBuilder;
    }

    /**
     * Submit multiple posture responses Submits posture responses
     *
     * @param postureResponse A Posture Response (required)
     * @return CompletableFuture&lt;PostureResponseEnvelope&gt;
     * @throws ApiException if fails to make API call
     */
    public CompletableFuture<PostureResponseEnvelope> createPostureResponseBulk(
            List<PostureResponseCreate> postureResponse) throws ApiException {
        try {
            HttpRequest.Builder localVarRequestBuilder =
                    createPostureResponseBulkRequestBuilder(postureResponse);
            return memberVarHttpClient
                    .sendAsync(localVarRequestBuilder.build(), HttpResponse.BodyHandlers.ofString())
                    .thenComposeAsync(
                            localVarResponse -> {
                                if (localVarResponse.statusCode() / 100 != 2) {
                                    return CompletableFuture.failedFuture(
                                            getApiException(
                                                    "createPostureResponseBulk", localVarResponse));
                                }
                                try {
                                    String responseBody = localVarResponse.body();
                                    return CompletableFuture.completedFuture(
                                            responseBody == null || responseBody.isBlank()
                                                    ? null
                                                    : memberVarObjectMapper.readValue(
                                                            responseBody,
                                                            new TypeReference<
                                                                    PostureResponseEnvelope>() {}));
                                } catch (IOException e) {
                                    return CompletableFuture.failedFuture(new ApiException(e));
                                }
                            });
        } catch (ApiException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Submit multiple posture responses Submits posture responses
     *
     * @param postureResponse A Posture Response (required)
     * @return CompletableFuture&lt;ApiResponse&lt;PostureResponseEnvelope&gt;&gt;
     * @throws ApiException if fails to make API call
     */
    public CompletableFuture<ApiResponse<PostureResponseEnvelope>>
            createPostureResponseBulkWithHttpInfo(List<PostureResponseCreate> postureResponse)
                    throws ApiException {
        try {
            HttpRequest.Builder localVarRequestBuilder =
                    createPostureResponseBulkRequestBuilder(postureResponse);
            return memberVarHttpClient
                    .sendAsync(localVarRequestBuilder.build(), HttpResponse.BodyHandlers.ofString())
                    .thenComposeAsync(
                            localVarResponse -> {
                                if (memberVarAsyncResponseInterceptor != null) {
                                    memberVarAsyncResponseInterceptor.accept(localVarResponse);
                                }
                                if (localVarResponse.statusCode() / 100 != 2) {
                                    return CompletableFuture.failedFuture(
                                            getApiException(
                                                    "createPostureResponseBulk", localVarResponse));
                                }
                                try {
                                    String responseBody = localVarResponse.body();
                                    return CompletableFuture.completedFuture(
                                            new ApiResponse<PostureResponseEnvelope>(
                                                    localVarResponse.statusCode(),
                                                    localVarResponse.headers().map(),
                                                    responseBody == null || responseBody.isBlank()
                                                            ? null
                                                            : memberVarObjectMapper.readValue(
                                                                    responseBody,
                                                                    new TypeReference<
                                                                            PostureResponseEnvelope>() {})));
                                } catch (IOException e) {
                                    return CompletableFuture.failedFuture(new ApiException(e));
                                }
                            });
        } catch (ApiException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    private HttpRequest.Builder createPostureResponseBulkRequestBuilder(
            List<PostureResponseCreate> postureResponse) throws ApiException {
        // verify the required parameter 'postureResponse' is set
        if (postureResponse == null) {
            throw new ApiException(
                    400,
                    "Missing the required parameter 'postureResponse' when calling"
                            + " createPostureResponseBulk");
        }

        HttpRequest.Builder localVarRequestBuilder = HttpRequest.newBuilder();

        String localVarPath = "/posture-response-bulk";

        localVarRequestBuilder.uri(URI.create(memberVarBaseUri + localVarPath));

        localVarRequestBuilder.header("Content-Type", "application/json");
        localVarRequestBuilder.header("Accept", "application/json");

        try {
            byte[] localVarPostBody = memberVarObjectMapper.writeValueAsBytes(postureResponse);
            localVarRequestBuilder.method(
                    "POST", HttpRequest.BodyPublishers.ofByteArray(localVarPostBody));
        } catch (IOException e) {
            throw new ApiException(e);
        }
        if (memberVarReadTimeout != null) {
            localVarRequestBuilder.timeout(memberVarReadTimeout);
        }
        if (memberVarInterceptor != null) {
            memberVarInterceptor.accept(localVarRequestBuilder);
        }
        return localVarRequestBuilder;
    }
}
