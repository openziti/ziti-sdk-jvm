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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.StringJoiner;
import org.openziti.management.ApiClient;

/** DataIntegrityCheckDetail */
@JsonPropertyOrder({
    DataIntegrityCheckDetail.JSON_PROPERTY_DESCRIPTION,
    DataIntegrityCheckDetail.JSON_PROPERTY_FIXED
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:49.931993799-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class DataIntegrityCheckDetail {
    public static final String JSON_PROPERTY_DESCRIPTION = "description";
    @javax.annotation.Nonnull private String description;

    public static final String JSON_PROPERTY_FIXED = "fixed";
    @javax.annotation.Nonnull private Boolean fixed;

    public DataIntegrityCheckDetail() {}

    public DataIntegrityCheckDetail description(@javax.annotation.Nonnull String description) {
        this.description = description;
        return this;
    }

    /**
     * Get description
     *
     * @return description
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_DESCRIPTION)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getDescription() {
        return description;
    }

    @JsonProperty(JSON_PROPERTY_DESCRIPTION)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setDescription(@javax.annotation.Nonnull String description) {
        this.description = description;
    }

    public DataIntegrityCheckDetail fixed(@javax.annotation.Nonnull Boolean fixed) {
        this.fixed = fixed;
        return this;
    }

    /**
     * Get fixed
     *
     * @return fixed
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_FIXED)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Boolean getFixed() {
        return fixed;
    }

    @JsonProperty(JSON_PROPERTY_FIXED)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setFixed(@javax.annotation.Nonnull Boolean fixed) {
        this.fixed = fixed;
    }

    /** Return true if this dataIntegrityCheckDetail object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataIntegrityCheckDetail dataIntegrityCheckDetail = (DataIntegrityCheckDetail) o;
        return Objects.equals(this.description, dataIntegrityCheckDetail.description)
                && Objects.equals(this.fixed, dataIntegrityCheckDetail.fixed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, fixed);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DataIntegrityCheckDetail {\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    fixed: ").append(toIndentedString(fixed)).append("\n");
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

        // add `description` to the URL query string
        if (getDescription() != null) {
            joiner.add(
                    String.format(
                            "%sdescription%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getDescription()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `fixed` to the URL query string
        if (getFixed() != null) {
            joiner.add(
                    String.format(
                            "%sfixed%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getFixed()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
