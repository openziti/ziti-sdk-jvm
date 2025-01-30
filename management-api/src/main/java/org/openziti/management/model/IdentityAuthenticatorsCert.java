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

/** IdentityAuthenticatorsCert */
@JsonPropertyOrder({
    IdentityAuthenticatorsCert.JSON_PROPERTY_FINGERPRINT,
    IdentityAuthenticatorsCert.JSON_PROPERTY_ID
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-01-30T10:50:07.620098843-05:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class IdentityAuthenticatorsCert {
    public static final String JSON_PROPERTY_FINGERPRINT = "fingerprint";
    @javax.annotation.Nullable private String fingerprint;

    public static final String JSON_PROPERTY_ID = "id";
    @javax.annotation.Nullable private String id;

    public IdentityAuthenticatorsCert() {}

    public IdentityAuthenticatorsCert fingerprint(@javax.annotation.Nullable String fingerprint) {
        this.fingerprint = fingerprint;
        return this;
    }

    /**
     * Get fingerprint
     *
     * @return fingerprint
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_FINGERPRINT)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getFingerprint() {
        return fingerprint;
    }

    @JsonProperty(JSON_PROPERTY_FINGERPRINT)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setFingerprint(@javax.annotation.Nullable String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public IdentityAuthenticatorsCert id(@javax.annotation.Nullable String id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getId() {
        return id;
    }

    @JsonProperty(JSON_PROPERTY_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setId(@javax.annotation.Nullable String id) {
        this.id = id;
    }

    /** Return true if this identityAuthenticators_cert object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentityAuthenticatorsCert identityAuthenticatorsCert = (IdentityAuthenticatorsCert) o;
        return Objects.equals(this.fingerprint, identityAuthenticatorsCert.fingerprint)
                && Objects.equals(this.id, identityAuthenticatorsCert.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fingerprint, id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IdentityAuthenticatorsCert {\n");
        sb.append("    fingerprint: ").append(toIndentedString(fingerprint)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

        // add `fingerprint` to the URL query string
        if (getFingerprint() != null) {
            joiner.add(
                    String.format(
                            "%sfingerprint%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getFingerprint()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `id` to the URL query string
        if (getId() != null) {
            joiner.add(
                    String.format(
                            "%sid%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getId()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
