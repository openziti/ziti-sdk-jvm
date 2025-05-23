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

package org.openziti.edge.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.StringJoiner;
import org.openziti.edge.ApiClient;

/** SDK information an authenticating client may provide */
@JsonPropertyOrder({
    SdkInfo.JSON_PROPERTY_APP_ID,
    SdkInfo.JSON_PROPERTY_APP_VERSION,
    SdkInfo.JSON_PROPERTY_BRANCH,
    SdkInfo.JSON_PROPERTY_REVISION,
    SdkInfo.JSON_PROPERTY_TYPE,
    SdkInfo.JSON_PROPERTY_VERSION
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:45.850758361-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class SdkInfo {
    public static final String JSON_PROPERTY_APP_ID = "appId";
    @javax.annotation.Nullable private String appId;

    public static final String JSON_PROPERTY_APP_VERSION = "appVersion";
    @javax.annotation.Nullable private String appVersion;

    public static final String JSON_PROPERTY_BRANCH = "branch";
    @javax.annotation.Nullable private String branch;

    public static final String JSON_PROPERTY_REVISION = "revision";
    @javax.annotation.Nullable private String revision;

    public static final String JSON_PROPERTY_TYPE = "type";
    @javax.annotation.Nullable private String type;

    public static final String JSON_PROPERTY_VERSION = "version";
    @javax.annotation.Nullable private String version;

    public SdkInfo() {}

    public SdkInfo appId(@javax.annotation.Nullable String appId) {
        this.appId = appId;
        return this;
    }

    /**
     * Get appId
     *
     * @return appId
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_APP_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getAppId() {
        return appId;
    }

    @JsonProperty(JSON_PROPERTY_APP_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setAppId(@javax.annotation.Nullable String appId) {
        this.appId = appId;
    }

    public SdkInfo appVersion(@javax.annotation.Nullable String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    /**
     * Get appVersion
     *
     * @return appVersion
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_APP_VERSION)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getAppVersion() {
        return appVersion;
    }

    @JsonProperty(JSON_PROPERTY_APP_VERSION)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setAppVersion(@javax.annotation.Nullable String appVersion) {
        this.appVersion = appVersion;
    }

    public SdkInfo branch(@javax.annotation.Nullable String branch) {
        this.branch = branch;
        return this;
    }

    /**
     * Get branch
     *
     * @return branch
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_BRANCH)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getBranch() {
        return branch;
    }

    @JsonProperty(JSON_PROPERTY_BRANCH)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setBranch(@javax.annotation.Nullable String branch) {
        this.branch = branch;
    }

    public SdkInfo revision(@javax.annotation.Nullable String revision) {
        this.revision = revision;
        return this;
    }

    /**
     * Get revision
     *
     * @return revision
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_REVISION)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getRevision() {
        return revision;
    }

    @JsonProperty(JSON_PROPERTY_REVISION)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setRevision(@javax.annotation.Nullable String revision) {
        this.revision = revision;
    }

    public SdkInfo type(@javax.annotation.Nullable String type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     *
     * @return type
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_TYPE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getType() {
        return type;
    }

    @JsonProperty(JSON_PROPERTY_TYPE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setType(@javax.annotation.Nullable String type) {
        this.type = type;
    }

    public SdkInfo version(@javax.annotation.Nullable String version) {
        this.version = version;
        return this;
    }

    /**
     * Get version
     *
     * @return version
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_VERSION)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getVersion() {
        return version;
    }

    @JsonProperty(JSON_PROPERTY_VERSION)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setVersion(@javax.annotation.Nullable String version) {
        this.version = version;
    }

    /** Return true if this sdkInfo object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SdkInfo sdkInfo = (SdkInfo) o;
        return Objects.equals(this.appId, sdkInfo.appId)
                && Objects.equals(this.appVersion, sdkInfo.appVersion)
                && Objects.equals(this.branch, sdkInfo.branch)
                && Objects.equals(this.revision, sdkInfo.revision)
                && Objects.equals(this.type, sdkInfo.type)
                && Objects.equals(this.version, sdkInfo.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appId, appVersion, branch, revision, type, version);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SdkInfo {\n");
        sb.append("    appId: ").append(toIndentedString(appId)).append("\n");
        sb.append("    appVersion: ").append(toIndentedString(appVersion)).append("\n");
        sb.append("    branch: ").append(toIndentedString(branch)).append("\n");
        sb.append("    revision: ").append(toIndentedString(revision)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
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

        // add `appId` to the URL query string
        if (getAppId() != null) {
            joiner.add(
                    String.format(
                            "%sappId%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getAppId()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `appVersion` to the URL query string
        if (getAppVersion() != null) {
            joiner.add(
                    String.format(
                            "%sappVersion%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getAppVersion()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `branch` to the URL query string
        if (getBranch() != null) {
            joiner.add(
                    String.format(
                            "%sbranch%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getBranch()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `revision` to the URL query string
        if (getRevision() != null) {
            joiner.add(
                    String.format(
                            "%srevision%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getRevision()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `type` to the URL query string
        if (getType() != null) {
            joiner.add(
                    String.format(
                            "%stype%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getType()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `version` to the URL query string
        if (getVersion() != null) {
            joiner.add(
                    String.format(
                            "%sversion%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getVersion()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
