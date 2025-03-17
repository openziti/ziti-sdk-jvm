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

/** PostureQueryProcess */
@JsonPropertyOrder({
    PostureQueryProcess.JSON_PROPERTY_OS_TYPE,
    PostureQueryProcess.JSON_PROPERTY_PATH
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:45.850758361-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class PostureQueryProcess {
    public static final String JSON_PROPERTY_OS_TYPE = "osType";
    @javax.annotation.Nullable private OsType osType;

    public static final String JSON_PROPERTY_PATH = "path";
    @javax.annotation.Nullable private String path;

    public PostureQueryProcess() {}

    public PostureQueryProcess osType(@javax.annotation.Nullable OsType osType) {
        this.osType = osType;
        return this;
    }

    /**
     * Get osType
     *
     * @return osType
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_OS_TYPE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public OsType getOsType() {
        return osType;
    }

    @JsonProperty(JSON_PROPERTY_OS_TYPE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setOsType(@javax.annotation.Nullable OsType osType) {
        this.osType = osType;
    }

    public PostureQueryProcess path(@javax.annotation.Nullable String path) {
        this.path = path;
        return this;
    }

    /**
     * Get path
     *
     * @return path
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_PATH)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getPath() {
        return path;
    }

    @JsonProperty(JSON_PROPERTY_PATH)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setPath(@javax.annotation.Nullable String path) {
        this.path = path;
    }

    /** Return true if this postureQueryProcess object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostureQueryProcess postureQueryProcess = (PostureQueryProcess) o;
        return Objects.equals(this.osType, postureQueryProcess.osType)
                && Objects.equals(this.path, postureQueryProcess.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(osType, path);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PostureQueryProcess {\n");
        sb.append("    osType: ").append(toIndentedString(osType)).append("\n");
        sb.append("    path: ").append(toIndentedString(path)).append("\n");
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

        // add `osType` to the URL query string
        if (getOsType() != null) {
            joiner.add(
                    String.format(
                            "%sosType%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getOsType()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `path` to the URL query string
        if (getPath() != null) {
            joiner.add(
                    String.format(
                            "%spath%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getPath()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
