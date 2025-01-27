/*
 * Ziti Edge Client
 * OpenZiti Edge Client API
 *
 * The version of the OpenAPI document: 0.26.39
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

/** A network JWT */
@JsonPropertyOrder({NetworkJWT.JSON_PROPERTY_NAME, NetworkJWT.JSON_PROPERTY_TOKEN})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-01-27T11:11:53.726065456-05:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class NetworkJWT {
    public static final String JSON_PROPERTY_NAME = "name";
    @javax.annotation.Nonnull private String name;

    public static final String JSON_PROPERTY_TOKEN = "token";
    @javax.annotation.Nonnull private String token;

    public NetworkJWT() {}

    public NetworkJWT name(@javax.annotation.Nonnull String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     *
     * @return name
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_NAME)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getName() {
        return name;
    }

    @JsonProperty(JSON_PROPERTY_NAME)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setName(@javax.annotation.Nonnull String name) {
        this.name = name;
    }

    public NetworkJWT token(@javax.annotation.Nonnull String token) {
        this.token = token;
        return this;
    }

    /**
     * Get token
     *
     * @return token
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_TOKEN)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getToken() {
        return token;
    }

    @JsonProperty(JSON_PROPERTY_TOKEN)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setToken(@javax.annotation.Nonnull String token) {
        this.token = token;
    }

    /** Return true if this networkJWT object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NetworkJWT networkJWT = (NetworkJWT) o;
        return Objects.equals(this.name, networkJWT.name)
                && Objects.equals(this.token, networkJWT.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, token);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NetworkJWT {\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    token: ").append(toIndentedString(token)).append("\n");
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

        // add `name` to the URL query string
        if (getName() != null) {
            joiner.add(
                    String.format(
                            "%sname%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getName()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `token` to the URL query string
        if (getToken() != null) {
            joiner.add(
                    String.format(
                            "%stoken%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getToken()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
