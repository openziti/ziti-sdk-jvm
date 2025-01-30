/*
 * Ziti Edge Management
 * OpenZiti Edge Management API
 *
 * The version of the OpenAPI document: 0.26.39
 * Contact: help@openziti.org
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package org.openziti.management.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import org.openziti.management.ApiClient;

/** FailedServiceRequest */
@JsonPropertyOrder({
    FailedServiceRequest.JSON_PROPERTY_API_SESSION_ID,
    FailedServiceRequest.JSON_PROPERTY_POLICY_FAILURES,
    FailedServiceRequest.JSON_PROPERTY_SERVICE_ID,
    FailedServiceRequest.JSON_PROPERTY_SERVICE_NAME,
    FailedServiceRequest.JSON_PROPERTY_SESSION_TYPE,
    FailedServiceRequest.JSON_PROPERTY_WHEN
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-01-30T10:50:07.620098843-05:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class FailedServiceRequest {
    public static final String JSON_PROPERTY_API_SESSION_ID = "apiSessionId";
    @javax.annotation.Nullable private String apiSessionId;

    public static final String JSON_PROPERTY_POLICY_FAILURES = "policyFailures";
    @javax.annotation.Nullable private List<PolicyFailure> policyFailures = new ArrayList<>();

    public static final String JSON_PROPERTY_SERVICE_ID = "serviceId";
    @javax.annotation.Nullable private String serviceId;

    public static final String JSON_PROPERTY_SERVICE_NAME = "serviceName";
    @javax.annotation.Nullable private String serviceName;

    public static final String JSON_PROPERTY_SESSION_TYPE = "sessionType";
    @javax.annotation.Nullable private DialBind sessionType;

    public static final String JSON_PROPERTY_WHEN = "when";
    @javax.annotation.Nullable private OffsetDateTime when;

    public FailedServiceRequest() {}

    public FailedServiceRequest apiSessionId(@javax.annotation.Nullable String apiSessionId) {
        this.apiSessionId = apiSessionId;
        return this;
    }

    /**
     * Get apiSessionId
     *
     * @return apiSessionId
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_API_SESSION_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getApiSessionId() {
        return apiSessionId;
    }

    @JsonProperty(JSON_PROPERTY_API_SESSION_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setApiSessionId(@javax.annotation.Nullable String apiSessionId) {
        this.apiSessionId = apiSessionId;
    }

    public FailedServiceRequest policyFailures(
            @javax.annotation.Nullable List<PolicyFailure> policyFailures) {
        this.policyFailures = policyFailures;
        return this;
    }

    public FailedServiceRequest addPolicyFailuresItem(PolicyFailure policyFailuresItem) {
        if (this.policyFailures == null) {
            this.policyFailures = new ArrayList<>();
        }
        this.policyFailures.add(policyFailuresItem);
        return this;
    }

    /**
     * Get policyFailures
     *
     * @return policyFailures
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_POLICY_FAILURES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<PolicyFailure> getPolicyFailures() {
        return policyFailures;
    }

    @JsonProperty(JSON_PROPERTY_POLICY_FAILURES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setPolicyFailures(@javax.annotation.Nullable List<PolicyFailure> policyFailures) {
        this.policyFailures = policyFailures;
    }

    public FailedServiceRequest serviceId(@javax.annotation.Nullable String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    /**
     * Get serviceId
     *
     * @return serviceId
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_SERVICE_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getServiceId() {
        return serviceId;
    }

    @JsonProperty(JSON_PROPERTY_SERVICE_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setServiceId(@javax.annotation.Nullable String serviceId) {
        this.serviceId = serviceId;
    }

    public FailedServiceRequest serviceName(@javax.annotation.Nullable String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    /**
     * Get serviceName
     *
     * @return serviceName
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_SERVICE_NAME)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getServiceName() {
        return serviceName;
    }

    @JsonProperty(JSON_PROPERTY_SERVICE_NAME)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setServiceName(@javax.annotation.Nullable String serviceName) {
        this.serviceName = serviceName;
    }

    public FailedServiceRequest sessionType(@javax.annotation.Nullable DialBind sessionType) {
        this.sessionType = sessionType;
        return this;
    }

    /**
     * Get sessionType
     *
     * @return sessionType
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_SESSION_TYPE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public DialBind getSessionType() {
        return sessionType;
    }

    @JsonProperty(JSON_PROPERTY_SESSION_TYPE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setSessionType(@javax.annotation.Nullable DialBind sessionType) {
        this.sessionType = sessionType;
    }

    public FailedServiceRequest when(@javax.annotation.Nullable OffsetDateTime when) {
        this.when = when;
        return this;
    }

    /**
     * Get when
     *
     * @return when
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_WHEN)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public OffsetDateTime getWhen() {
        return when;
    }

    @JsonProperty(JSON_PROPERTY_WHEN)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setWhen(@javax.annotation.Nullable OffsetDateTime when) {
        this.when = when;
    }

    /** Return true if this failedServiceRequest object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FailedServiceRequest failedServiceRequest = (FailedServiceRequest) o;
        return Objects.equals(this.apiSessionId, failedServiceRequest.apiSessionId)
                && Objects.equals(this.policyFailures, failedServiceRequest.policyFailures)
                && Objects.equals(this.serviceId, failedServiceRequest.serviceId)
                && Objects.equals(this.serviceName, failedServiceRequest.serviceName)
                && Objects.equals(this.sessionType, failedServiceRequest.sessionType)
                && Objects.equals(this.when, failedServiceRequest.when);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                apiSessionId, policyFailures, serviceId, serviceName, sessionType, when);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FailedServiceRequest {\n");
        sb.append("    apiSessionId: ").append(toIndentedString(apiSessionId)).append("\n");
        sb.append("    policyFailures: ").append(toIndentedString(policyFailures)).append("\n");
        sb.append("    serviceId: ").append(toIndentedString(serviceId)).append("\n");
        sb.append("    serviceName: ").append(toIndentedString(serviceName)).append("\n");
        sb.append("    sessionType: ").append(toIndentedString(sessionType)).append("\n");
        sb.append("    when: ").append(toIndentedString(when)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces (except the first
     * line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    /**
     * Convert the instance into URL query string.
     *
     * @return URL query string
     */
    public String toUrlQueryString() {
        return toUrlQueryString(null);
    }

    /**
     * Convert the instance into URL query string.
     *
     * @param prefix prefix of the query string
     * @return URL query string
     */
    public String toUrlQueryString(String prefix) {
        String suffix = "";
        String containerSuffix = "";
        String containerPrefix = "";
        if (prefix == null) {
            // style=form, explode=true, e.g. /pet?name=cat&type=manx
            prefix = "";
        } else {
            // deepObject style e.g. /pet?id[name]=cat&id[type]=manx
            prefix = prefix + "[";
            suffix = "]";
            containerSuffix = "]";
            containerPrefix = "[";
        }

        StringJoiner joiner = new StringJoiner("&");

        // add `apiSessionId` to the URL query string
        if (getApiSessionId() != null) {
            joiner.add(
                    String.format(
                            "%sapiSessionId%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getApiSessionId()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `policyFailures` to the URL query string
        if (getPolicyFailures() != null) {
            for (int i = 0; i < getPolicyFailures().size(); i++) {
                if (getPolicyFailures().get(i) != null) {
                    joiner.add(
                            getPolicyFailures()
                                    .get(i)
                                    .toUrlQueryString(
                                            String.format(
                                                    "%spolicyFailures%s%s",
                                                    prefix,
                                                    suffix,
                                                    "".equals(suffix)
                                                            ? ""
                                                            : String.format(
                                                                    "%s%d%s",
                                                                    containerPrefix,
                                                                    i,
                                                                    containerSuffix))));
                }
            }
        }

        // add `serviceId` to the URL query string
        if (getServiceId() != null) {
            joiner.add(
                    String.format(
                            "%sserviceId%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getServiceId()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `serviceName` to the URL query string
        if (getServiceName() != null) {
            joiner.add(
                    String.format(
                            "%sserviceName%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getServiceName()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `sessionType` to the URL query string
        if (getSessionType() != null) {
            joiner.add(
                    String.format(
                            "%ssessionType%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getSessionType()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `when` to the URL query string
        if (getWhen() != null) {
            joiner.add(
                    String.format(
                            "%swhen%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getWhen()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
