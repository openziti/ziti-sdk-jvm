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

/** All of the fields on an authenticator that will be updated */
@JsonPropertyOrder({
    AuthenticatorUpdateWithCurrent.JSON_PROPERTY_PASSWORD,
    AuthenticatorUpdateWithCurrent.JSON_PROPERTY_TAGS,
    AuthenticatorUpdateWithCurrent.JSON_PROPERTY_USERNAME,
    AuthenticatorUpdateWithCurrent.JSON_PROPERTY_CURRENT_PASSWORD
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:49.931993799-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class AuthenticatorUpdateWithCurrent {
    public static final String JSON_PROPERTY_PASSWORD = "password";
    @javax.annotation.Nonnull private String password;

    public static final String JSON_PROPERTY_TAGS = "tags";
    @javax.annotation.Nullable private Tags tags;

    public static final String JSON_PROPERTY_USERNAME = "username";
    @javax.annotation.Nonnull private String username;

    public static final String JSON_PROPERTY_CURRENT_PASSWORD = "currentPassword";
    @javax.annotation.Nonnull private String currentPassword;

    public AuthenticatorUpdateWithCurrent() {}

    public AuthenticatorUpdateWithCurrent password(@javax.annotation.Nonnull String password) {
        this.password = password;
        return this;
    }

    /**
     * Get password
     *
     * @return password
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_PASSWORD)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getPassword() {
        return password;
    }

    @JsonProperty(JSON_PROPERTY_PASSWORD)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setPassword(@javax.annotation.Nonnull String password) {
        this.password = password;
    }

    public AuthenticatorUpdateWithCurrent tags(@javax.annotation.Nullable Tags tags) {
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

    public AuthenticatorUpdateWithCurrent username(@javax.annotation.Nonnull String username) {
        this.username = username;
        return this;
    }

    /**
     * Get username
     *
     * @return username
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_USERNAME)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getUsername() {
        return username;
    }

    @JsonProperty(JSON_PROPERTY_USERNAME)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setUsername(@javax.annotation.Nonnull String username) {
        this.username = username;
    }

    public AuthenticatorUpdateWithCurrent currentPassword(
            @javax.annotation.Nonnull String currentPassword) {
        this.currentPassword = currentPassword;
        return this;
    }

    /**
     * Get currentPassword
     *
     * @return currentPassword
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_CURRENT_PASSWORD)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getCurrentPassword() {
        return currentPassword;
    }

    @JsonProperty(JSON_PROPERTY_CURRENT_PASSWORD)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setCurrentPassword(@javax.annotation.Nonnull String currentPassword) {
        this.currentPassword = currentPassword;
    }

    /** Return true if this authenticatorUpdateWithCurrent object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthenticatorUpdateWithCurrent authenticatorUpdateWithCurrent =
                (AuthenticatorUpdateWithCurrent) o;
        return Objects.equals(this.password, authenticatorUpdateWithCurrent.password)
                && Objects.equals(this.tags, authenticatorUpdateWithCurrent.tags)
                && Objects.equals(this.username, authenticatorUpdateWithCurrent.username)
                && Objects.equals(
                        this.currentPassword, authenticatorUpdateWithCurrent.currentPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, tags, username, currentPassword);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AuthenticatorUpdateWithCurrent {\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    currentPassword: ").append(toIndentedString(currentPassword)).append("\n");
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

        // add `currentPassword` to the URL query string
        if (getCurrentPassword() != null) {
            joiner.add(
                    String.format(
                            "%scurrentPassword%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getCurrentPassword()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
