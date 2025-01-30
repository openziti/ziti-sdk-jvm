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
import java.util.Objects;
import java.util.StringJoiner;
import org.openziti.management.ApiClient;

/** DisableParams */
@JsonPropertyOrder({DisableParams.JSON_PROPERTY_DURATION_MINUTES})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-01-30T10:50:07.620098843-05:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class DisableParams {
    public static final String JSON_PROPERTY_DURATION_MINUTES = "durationMinutes";
    @javax.annotation.Nonnull private Integer durationMinutes;

    public DisableParams() {}

    public DisableParams durationMinutes(@javax.annotation.Nonnull Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
        return this;
    }

    /**
     * Get durationMinutes
     *
     * @return durationMinutes
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_DURATION_MINUTES)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    @JsonProperty(JSON_PROPERTY_DURATION_MINUTES)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setDurationMinutes(@javax.annotation.Nonnull Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    /** Return true if this disableParams object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DisableParams disableParams = (DisableParams) o;
        return Objects.equals(this.durationMinutes, disableParams.durationMinutes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(durationMinutes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DisableParams {\n");
        sb.append("    durationMinutes: ").append(toIndentedString(durationMinutes)).append("\n");
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

        // add `durationMinutes` to the URL query string
        if (getDurationMinutes() != null) {
            joiner.add(
                    String.format(
                            "%sdurationMinutes%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getDurationMinutes()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
