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
import java.util.Objects;
import java.util.StringJoiner;

/** AuthPolicyPrimary */
@JsonPropertyOrder({
    AuthPolicyPrimary.JSON_PROPERTY_CERT,
    AuthPolicyPrimary.JSON_PROPERTY_EXT_JWT,
    AuthPolicyPrimary.JSON_PROPERTY_UPDB
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:49.931993799-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class AuthPolicyPrimary {
    public static final String JSON_PROPERTY_CERT = "cert";
    @javax.annotation.Nonnull private AuthPolicyPrimaryCert cert;

    public static final String JSON_PROPERTY_EXT_JWT = "extJwt";
    @javax.annotation.Nonnull private AuthPolicyPrimaryExtJwt extJwt;

    public static final String JSON_PROPERTY_UPDB = "updb";
    @javax.annotation.Nonnull private AuthPolicyPrimaryUpdb updb;

    public AuthPolicyPrimary() {}

    public AuthPolicyPrimary cert(@javax.annotation.Nonnull AuthPolicyPrimaryCert cert) {
        this.cert = cert;
        return this;
    }

    /**
     * Get cert
     *
     * @return cert
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_CERT)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public AuthPolicyPrimaryCert getCert() {
        return cert;
    }

    @JsonProperty(JSON_PROPERTY_CERT)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setCert(@javax.annotation.Nonnull AuthPolicyPrimaryCert cert) {
        this.cert = cert;
    }

    public AuthPolicyPrimary extJwt(@javax.annotation.Nonnull AuthPolicyPrimaryExtJwt extJwt) {
        this.extJwt = extJwt;
        return this;
    }

    /**
     * Get extJwt
     *
     * @return extJwt
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_EXT_JWT)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public AuthPolicyPrimaryExtJwt getExtJwt() {
        return extJwt;
    }

    @JsonProperty(JSON_PROPERTY_EXT_JWT)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setExtJwt(@javax.annotation.Nonnull AuthPolicyPrimaryExtJwt extJwt) {
        this.extJwt = extJwt;
    }

    public AuthPolicyPrimary updb(@javax.annotation.Nonnull AuthPolicyPrimaryUpdb updb) {
        this.updb = updb;
        return this;
    }

    /**
     * Get updb
     *
     * @return updb
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_UPDB)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public AuthPolicyPrimaryUpdb getUpdb() {
        return updb;
    }

    @JsonProperty(JSON_PROPERTY_UPDB)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setUpdb(@javax.annotation.Nonnull AuthPolicyPrimaryUpdb updb) {
        this.updb = updb;
    }

    /** Return true if this authPolicyPrimary object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthPolicyPrimary authPolicyPrimary = (AuthPolicyPrimary) o;
        return Objects.equals(this.cert, authPolicyPrimary.cert)
                && Objects.equals(this.extJwt, authPolicyPrimary.extJwt)
                && Objects.equals(this.updb, authPolicyPrimary.updb);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cert, extJwt, updb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AuthPolicyPrimary {\n");
        sb.append("    cert: ").append(toIndentedString(cert)).append("\n");
        sb.append("    extJwt: ").append(toIndentedString(extJwt)).append("\n");
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

        // add `extJwt` to the URL query string
        if (getExtJwt() != null) {
            joiner.add(getExtJwt().toUrlQueryString(prefix + "extJwt" + suffix));
        }

        // add `updb` to the URL query string
        if (getUpdb() != null) {
            joiner.add(getUpdb().toUrlQueryString(prefix + "updb" + suffix));
        }

        return joiner.toString();
    }
}
