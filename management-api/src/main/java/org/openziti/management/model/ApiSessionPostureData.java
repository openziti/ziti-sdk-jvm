/*
 * Ziti Edge Management
 * OpenZiti Edge Management API
 *
 * The version of the OpenAPI document: 0.26.42
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
import java.util.Objects;
import java.util.StringJoiner;

/** ApiSessionPostureData */
@JsonPropertyOrder({
    ApiSessionPostureData.JSON_PROPERTY_ENDPOINT_STATE,
    ApiSessionPostureData.JSON_PROPERTY_MFA,
    ApiSessionPostureData.JSON_PROPERTY_SDK_INFO
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:49.931993799-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class ApiSessionPostureData {
    public static final String JSON_PROPERTY_ENDPOINT_STATE = "endpointState";
    @javax.annotation.Nullable private PostureDataEndpointState endpointState;

    public static final String JSON_PROPERTY_MFA = "mfa";
    @javax.annotation.Nonnull private PostureDataMfa mfa;

    public static final String JSON_PROPERTY_SDK_INFO = "sdkInfo";
    @javax.annotation.Nullable private SdkInfo sdkInfo;

    public ApiSessionPostureData() {}

    public ApiSessionPostureData endpointState(
            @javax.annotation.Nullable PostureDataEndpointState endpointState) {
        this.endpointState = endpointState;
        return this;
    }

    /**
     * Get endpointState
     *
     * @return endpointState
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_ENDPOINT_STATE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public PostureDataEndpointState getEndpointState() {
        return endpointState;
    }

    @JsonProperty(JSON_PROPERTY_ENDPOINT_STATE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setEndpointState(
            @javax.annotation.Nullable PostureDataEndpointState endpointState) {
        this.endpointState = endpointState;
    }

    public ApiSessionPostureData mfa(@javax.annotation.Nonnull PostureDataMfa mfa) {
        this.mfa = mfa;
        return this;
    }

    /**
     * Get mfa
     *
     * @return mfa
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_MFA)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public PostureDataMfa getMfa() {
        return mfa;
    }

    @JsonProperty(JSON_PROPERTY_MFA)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setMfa(@javax.annotation.Nonnull PostureDataMfa mfa) {
        this.mfa = mfa;
    }

    public ApiSessionPostureData sdkInfo(@javax.annotation.Nullable SdkInfo sdkInfo) {
        this.sdkInfo = sdkInfo;
        return this;
    }

    /**
     * Get sdkInfo
     *
     * @return sdkInfo
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_SDK_INFO)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public SdkInfo getSdkInfo() {
        return sdkInfo;
    }

    @JsonProperty(JSON_PROPERTY_SDK_INFO)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setSdkInfo(@javax.annotation.Nullable SdkInfo sdkInfo) {
        this.sdkInfo = sdkInfo;
    }

    /** Return true if this apiSessionPostureData object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApiSessionPostureData apiSessionPostureData = (ApiSessionPostureData) o;
        return Objects.equals(this.endpointState, apiSessionPostureData.endpointState)
                && Objects.equals(this.mfa, apiSessionPostureData.mfa)
                && Objects.equals(this.sdkInfo, apiSessionPostureData.sdkInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endpointState, mfa, sdkInfo);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ApiSessionPostureData {\n");
        sb.append("    endpointState: ").append(toIndentedString(endpointState)).append("\n");
        sb.append("    mfa: ").append(toIndentedString(mfa)).append("\n");
        sb.append("    sdkInfo: ").append(toIndentedString(sdkInfo)).append("\n");
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

        // add `endpointState` to the URL query string
        if (getEndpointState() != null) {
            joiner.add(getEndpointState().toUrlQueryString(prefix + "endpointState" + suffix));
        }

        // add `mfa` to the URL query string
        if (getMfa() != null) {
            joiner.add(getMfa().toUrlQueryString(prefix + "mfa" + suffix));
        }

        // add `sdkInfo` to the URL query string
        if (getSdkInfo() != null) {
            joiner.add(getSdkInfo().toUrlQueryString(prefix + "sdkInfo" + suffix));
        }

        return joiner.toString();
    }
}
