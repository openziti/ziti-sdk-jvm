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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import org.openziti.management.ApiClient;

/** SessionRoutePathDetail */
@JsonPropertyOrder({SessionRoutePathDetail.JSON_PROPERTY_ROUTE_PATH})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:49.931993799-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class SessionRoutePathDetail {
    public static final String JSON_PROPERTY_ROUTE_PATH = "routePath";
    @javax.annotation.Nullable private List<String> routePath = new ArrayList<>();

    public SessionRoutePathDetail() {}

    public SessionRoutePathDetail routePath(@javax.annotation.Nullable List<String> routePath) {
        this.routePath = routePath;
        return this;
    }

    public SessionRoutePathDetail addRoutePathItem(String routePathItem) {
        if (this.routePath == null) {
            this.routePath = new ArrayList<>();
        }
        this.routePath.add(routePathItem);
        return this;
    }

    /**
     * Get routePath
     *
     * @return routePath
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_ROUTE_PATH)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<String> getRoutePath() {
        return routePath;
    }

    @JsonProperty(JSON_PROPERTY_ROUTE_PATH)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setRoutePath(@javax.annotation.Nullable List<String> routePath) {
        this.routePath = routePath;
    }

    /** Return true if this sessionRoutePathDetail object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SessionRoutePathDetail sessionRoutePathDetail = (SessionRoutePathDetail) o;
        return Objects.equals(this.routePath, sessionRoutePathDetail.routePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routePath);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SessionRoutePathDetail {\n");
        sb.append("    routePath: ").append(toIndentedString(routePath)).append("\n");
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

        // add `routePath` to the URL query string
        if (getRoutePath() != null) {
            for (int i = 0; i < getRoutePath().size(); i++) {
                joiner.add(
                        String.format(
                                "%sroutePath%s%s=%s",
                                prefix,
                                suffix,
                                "".equals(suffix)
                                        ? ""
                                        : String.format(
                                                "%s%d%s", containerPrefix, i, containerSuffix),
                                URLEncoder.encode(
                                                ApiClient.valueToString(getRoutePath().get(i)),
                                                StandardCharsets.UTF_8)
                                        .replaceAll("\\+", "%20")));
            }
        }

        return joiner.toString();
    }
}
