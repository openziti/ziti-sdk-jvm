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

/** OtherPrime */
@JsonPropertyOrder({
    OtherPrime.JSON_PROPERTY_D,
    OtherPrime.JSON_PROPERTY_R,
    OtherPrime.JSON_PROPERTY_T
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:45.850758361-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class OtherPrime {
    public static final String JSON_PROPERTY_D = "d";
    @javax.annotation.Nullable private String d;

    public static final String JSON_PROPERTY_R = "r";
    @javax.annotation.Nullable private String r;

    public static final String JSON_PROPERTY_T = "t";
    @javax.annotation.Nullable private String t;

    public OtherPrime() {}

    public OtherPrime d(@javax.annotation.Nullable String d) {
        this.d = d;
        return this;
    }

    /**
     * Factor CRT exponent.
     *
     * @return d
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_D)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getD() {
        return d;
    }

    @JsonProperty(JSON_PROPERTY_D)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setD(@javax.annotation.Nullable String d) {
        this.d = d;
    }

    public OtherPrime r(@javax.annotation.Nullable String r) {
        this.r = r;
        return this;
    }

    /**
     * Prime factor.
     *
     * @return r
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_R)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getR() {
        return r;
    }

    @JsonProperty(JSON_PROPERTY_R)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setR(@javax.annotation.Nullable String r) {
        this.r = r;
    }

    public OtherPrime t(@javax.annotation.Nullable String t) {
        this.t = t;
        return this;
    }

    /**
     * Factor CRT coefficient.
     *
     * @return t
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_T)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getT() {
        return t;
    }

    @JsonProperty(JSON_PROPERTY_T)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setT(@javax.annotation.Nullable String t) {
        this.t = t;
    }

    /** Return true if this otherPrime object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OtherPrime otherPrime = (OtherPrime) o;
        return Objects.equals(this.d, otherPrime.d)
                && Objects.equals(this.r, otherPrime.r)
                && Objects.equals(this.t, otherPrime.t);
    }

    @Override
    public int hashCode() {
        return Objects.hash(d, r, t);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OtherPrime {\n");
        sb.append("    d: ").append(toIndentedString(d)).append("\n");
        sb.append("    r: ").append(toIndentedString(r)).append("\n");
        sb.append("    t: ").append(toIndentedString(t)).append("\n");
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

        // add `d` to the URL query string
        if (getD() != null) {
            joiner.add(
                    String.format(
                            "%sd%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getD()), StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `r` to the URL query string
        if (getR() != null) {
            joiner.add(
                    String.format(
                            "%sr%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getR()), StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `t` to the URL query string
        if (getT() != null) {
            joiner.add(
                    String.format(
                            "%st%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getT()), StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
