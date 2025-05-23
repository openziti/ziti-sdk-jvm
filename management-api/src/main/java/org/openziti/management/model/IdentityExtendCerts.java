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

/** IdentityExtendCerts */
@JsonPropertyOrder({
    IdentityExtendCerts.JSON_PROPERTY_CA,
    IdentityExtendCerts.JSON_PROPERTY_CLIENT_CERT
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:49.931993799-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class IdentityExtendCerts {
    public static final String JSON_PROPERTY_CA = "ca";
    @javax.annotation.Nullable private String ca;

    public static final String JSON_PROPERTY_CLIENT_CERT = "clientCert";
    @javax.annotation.Nullable private String clientCert;

    public IdentityExtendCerts() {}

    public IdentityExtendCerts ca(@javax.annotation.Nullable String ca) {
        this.ca = ca;
        return this;
    }

    /**
     * A PEM encoded set of CA certificates
     *
     * @return ca
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_CA)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getCa() {
        return ca;
    }

    @JsonProperty(JSON_PROPERTY_CA)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setCa(@javax.annotation.Nullable String ca) {
        this.ca = ca;
    }

    public IdentityExtendCerts clientCert(@javax.annotation.Nullable String clientCert) {
        this.clientCert = clientCert;
        return this;
    }

    /**
     * A PEM encoded client certificate
     *
     * @return clientCert
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_CLIENT_CERT)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getClientCert() {
        return clientCert;
    }

    @JsonProperty(JSON_PROPERTY_CLIENT_CERT)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setClientCert(@javax.annotation.Nullable String clientCert) {
        this.clientCert = clientCert;
    }

    /** Return true if this identityExtendCerts object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentityExtendCerts identityExtendCerts = (IdentityExtendCerts) o;
        return Objects.equals(this.ca, identityExtendCerts.ca)
                && Objects.equals(this.clientCert, identityExtendCerts.clientCert);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ca, clientCert);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IdentityExtendCerts {\n");
        sb.append("    ca: ").append(toIndentedString(ca)).append("\n");
        sb.append("    clientCert: ").append(toIndentedString(clientCert)).append("\n");
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

        // add `ca` to the URL query string
        if (getCa() != null) {
            joiner.add(
                    String.format(
                            "%sca%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getCa()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `clientCert` to the URL query string
        if (getClientCert() != null) {
            joiner.add(
                    String.format(
                            "%sclientCert%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getClientCert()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
