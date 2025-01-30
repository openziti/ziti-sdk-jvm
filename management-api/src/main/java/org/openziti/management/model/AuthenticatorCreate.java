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

/** Creates an authenticator for a specific identity which can be used for API authentication */
@JsonPropertyOrder({
    AuthenticatorCreate.JSON_PROPERTY_CERT_PEM,
    AuthenticatorCreate.JSON_PROPERTY_IDENTITY_ID,
    AuthenticatorCreate.JSON_PROPERTY_METHOD,
    AuthenticatorCreate.JSON_PROPERTY_PASSWORD,
    AuthenticatorCreate.JSON_PROPERTY_TAGS,
    AuthenticatorCreate.JSON_PROPERTY_USERNAME
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-01-30T10:50:07.620098843-05:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class AuthenticatorCreate {
    public static final String JSON_PROPERTY_CERT_PEM = "certPem";
    @javax.annotation.Nullable private String certPem;

    public static final String JSON_PROPERTY_IDENTITY_ID = "identityId";
    @javax.annotation.Nonnull private String identityId;

    public static final String JSON_PROPERTY_METHOD = "method";
    @javax.annotation.Nonnull private String method;

    public static final String JSON_PROPERTY_PASSWORD = "password";
    @javax.annotation.Nullable private String password;

    public static final String JSON_PROPERTY_TAGS = "tags";
    @javax.annotation.Nullable private Tags tags;

    public static final String JSON_PROPERTY_USERNAME = "username";
    @javax.annotation.Nullable private String username;

    public AuthenticatorCreate() {}

    public AuthenticatorCreate certPem(@javax.annotation.Nullable String certPem) {
        this.certPem = certPem;
        return this;
    }

    /**
     * The client certificate the identity will login with. Used only for method&#x3D;&#39;cert&#39;
     *
     * @return certPem
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_CERT_PEM)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getCertPem() {
        return certPem;
    }

    @JsonProperty(JSON_PROPERTY_CERT_PEM)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setCertPem(@javax.annotation.Nullable String certPem) {
        this.certPem = certPem;
    }

    public AuthenticatorCreate identityId(@javax.annotation.Nonnull String identityId) {
        this.identityId = identityId;
        return this;
    }

    /**
     * The id of an existing identity that will be assigned this authenticator
     *
     * @return identityId
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_IDENTITY_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getIdentityId() {
        return identityId;
    }

    @JsonProperty(JSON_PROPERTY_IDENTITY_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setIdentityId(@javax.annotation.Nonnull String identityId) {
        this.identityId = identityId;
    }

    public AuthenticatorCreate method(@javax.annotation.Nonnull String method) {
        this.method = method;
        return this;
    }

    /**
     * The type of authenticator to create; which will dictate which properties on this object are
     * required.
     *
     * @return method
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_METHOD)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getMethod() {
        return method;
    }

    @JsonProperty(JSON_PROPERTY_METHOD)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setMethod(@javax.annotation.Nonnull String method) {
        this.method = method;
    }

    public AuthenticatorCreate password(@javax.annotation.Nullable String password) {
        this.password = password;
        return this;
    }

    /**
     * The password the identity will login with. Used only for method&#x3D;&#39;updb&#39;
     *
     * @return password
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_PASSWORD)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getPassword() {
        return password;
    }

    @JsonProperty(JSON_PROPERTY_PASSWORD)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setPassword(@javax.annotation.Nullable String password) {
        this.password = password;
    }

    public AuthenticatorCreate tags(@javax.annotation.Nullable Tags tags) {
        this.tags = tags;
        return this;
    }

    /**
     * Get tags
     *
     * @return tags
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_TAGS)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Tags getTags() {
        return tags;
    }

    @JsonProperty(JSON_PROPERTY_TAGS)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setTags(@javax.annotation.Nullable Tags tags) {
        this.tags = tags;
    }

    public AuthenticatorCreate username(@javax.annotation.Nullable String username) {
        this.username = username;
        return this;
    }

    /**
     * The username that the identity will login with. Used only for method&#x3D;&#39;updb&#39;
     *
     * @return username
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_USERNAME)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getUsername() {
        return username;
    }

    @JsonProperty(JSON_PROPERTY_USERNAME)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setUsername(@javax.annotation.Nullable String username) {
        this.username = username;
    }

    /** Return true if this authenticatorCreate object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthenticatorCreate authenticatorCreate = (AuthenticatorCreate) o;
        return Objects.equals(this.certPem, authenticatorCreate.certPem)
                && Objects.equals(this.identityId, authenticatorCreate.identityId)
                && Objects.equals(this.method, authenticatorCreate.method)
                && Objects.equals(this.password, authenticatorCreate.password)
                && Objects.equals(this.tags, authenticatorCreate.tags)
                && Objects.equals(this.username, authenticatorCreate.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certPem, identityId, method, password, tags, username);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AuthenticatorCreate {\n");
        sb.append("    certPem: ").append(toIndentedString(certPem)).append("\n");
        sb.append("    identityId: ").append(toIndentedString(identityId)).append("\n");
        sb.append("    method: ").append(toIndentedString(method)).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
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

        // add `certPem` to the URL query string
        if (getCertPem() != null) {
            joiner.add(
                    String.format(
                            "%scertPem%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getCertPem()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `identityId` to the URL query string
        if (getIdentityId() != null) {
            joiner.add(
                    String.format(
                            "%sidentityId%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getIdentityId()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `method` to the URL query string
        if (getMethod() != null) {
            joiner.add(
                    String.format(
                            "%smethod%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getMethod()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `password` to the URL query string
        if (getPassword() != null) {
            joiner.add(
                    String.format(
                            "%spassword%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getPassword()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `tags` to the URL query string
        if (getTags() != null) {
            joiner.add(getTags().toUrlQueryString(prefix + "tags" + suffix));
        }

        // add `username` to the URL query string
        if (getUsername() != null) {
            joiner.add(
                    String.format(
                            "%susername%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getUsername()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
