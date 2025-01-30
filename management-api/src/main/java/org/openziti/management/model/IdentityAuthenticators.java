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
import java.util.Objects;
import java.util.StringJoiner;

/** IdentityAuthenticators */
@JsonPropertyOrder({
    IdentityAuthenticators.JSON_PROPERTY_CERT,
    IdentityAuthenticators.JSON_PROPERTY_UPDB
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-01-30T10:50:07.620098843-05:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class IdentityAuthenticators {
    public static final String JSON_PROPERTY_CERT = "cert";
    @javax.annotation.Nullable private IdentityAuthenticatorsCert cert;

    public static final String JSON_PROPERTY_UPDB = "updb";
    @javax.annotation.Nullable private IdentityAuthenticatorsUpdb updb;

    public IdentityAuthenticators() {}

    public IdentityAuthenticators cert(@javax.annotation.Nullable IdentityAuthenticatorsCert cert) {
        this.cert = cert;
        return this;
    }

    /**
     * Get cert
     *
     * @return cert
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_CERT)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public IdentityAuthenticatorsCert getCert() {
        return cert;
    }

    @JsonProperty(JSON_PROPERTY_CERT)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setCert(@javax.annotation.Nullable IdentityAuthenticatorsCert cert) {
        this.cert = cert;
    }

    public IdentityAuthenticators updb(@javax.annotation.Nullable IdentityAuthenticatorsUpdb updb) {
        this.updb = updb;
        return this;
    }

    /**
     * Get updb
     *
     * @return updb
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_UPDB)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public IdentityAuthenticatorsUpdb getUpdb() {
        return updb;
    }

    @JsonProperty(JSON_PROPERTY_UPDB)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setUpdb(@javax.annotation.Nullable IdentityAuthenticatorsUpdb updb) {
        this.updb = updb;
    }

    /** Return true if this identityAuthenticators object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentityAuthenticators identityAuthenticators = (IdentityAuthenticators) o;
        return Objects.equals(this.cert, identityAuthenticators.cert)
                && Objects.equals(this.updb, identityAuthenticators.updb);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cert, updb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IdentityAuthenticators {\n");
        sb.append("    cert: ").append(toIndentedString(cert)).append("\n");
        sb.append("    updb: ").append(toIndentedString(updb)).append("\n");
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

        // add `cert` to the URL query string
        if (getCert() != null) {
            joiner.add(getCert().toUrlQueryString(prefix + "cert" + suffix));
        }

        // add `updb` to the URL query string
        if (getUpdb() != null) {
            joiner.add(getUpdb().toUrlQueryString(prefix + "updb" + suffix));
        }

        return joiner.toString();
    }
}
