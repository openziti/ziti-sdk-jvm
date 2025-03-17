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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openziti.management.ApiClient;

/** A Auth Policy resource */
@JsonPropertyOrder({
    AuthPolicyPatch.JSON_PROPERTY_NAME,
    AuthPolicyPatch.JSON_PROPERTY_PRIMARY,
    AuthPolicyPatch.JSON_PROPERTY_SECONDARY,
    AuthPolicyPatch.JSON_PROPERTY_TAGS
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:49.931993799-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class AuthPolicyPatch {
    public static final String JSON_PROPERTY_NAME = "name";
    private JsonNullable<String> name = JsonNullable.<String>undefined();

    public static final String JSON_PROPERTY_PRIMARY = "primary";
    @javax.annotation.Nullable private AuthPolicyPrimaryPatch primary;

    public static final String JSON_PROPERTY_SECONDARY = "secondary";
    private JsonNullable<AuthPolicySecondaryPatch> secondary =
            JsonNullable.<AuthPolicySecondaryPatch>undefined();

    public static final String JSON_PROPERTY_TAGS = "tags";
    @javax.annotation.Nullable private Tags tags;

    public AuthPolicyPatch() {}

    public AuthPolicyPatch name(@javax.annotation.Nullable String name) {
        this.name = JsonNullable.<String>of(name);
        return this;
    }

    /**
     * Get name
     *
     * @return name
     */
    @javax.annotation.Nullable
    @JsonIgnore
    public String getName() {
        return name.orElse(null);
    }

    @JsonProperty(JSON_PROPERTY_NAME)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public JsonNullable<String> getName_JsonNullable() {
        return name;
    }

    @JsonProperty(JSON_PROPERTY_NAME)
    public void setName_JsonNullable(JsonNullable<String> name) {
        this.name = name;
    }

    public void setName(@javax.annotation.Nullable String name) {
        this.name = JsonNullable.<String>of(name);
    }

    public AuthPolicyPatch primary(@javax.annotation.Nullable AuthPolicyPrimaryPatch primary) {
        this.primary = primary;
        return this;
    }

    /**
     * Get primary
     *
     * @return primary
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_PRIMARY)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public AuthPolicyPrimaryPatch getPrimary() {
        return primary;
    }

    @JsonProperty(JSON_PROPERTY_PRIMARY)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setPrimary(@javax.annotation.Nullable AuthPolicyPrimaryPatch primary) {
        this.primary = primary;
    }

    public AuthPolicyPatch secondary(
            @javax.annotation.Nullable AuthPolicySecondaryPatch secondary) {
        this.secondary = JsonNullable.<AuthPolicySecondaryPatch>of(secondary);
        return this;
    }

    /**
     * Get secondary
     *
     * @return secondary
     */
    @javax.annotation.Nullable
    @JsonIgnore
    public AuthPolicySecondaryPatch getSecondary() {
        return secondary.orElse(null);
    }

    @JsonProperty(JSON_PROPERTY_SECONDARY)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public JsonNullable<AuthPolicySecondaryPatch> getSecondary_JsonNullable() {
        return secondary;
    }

    @JsonProperty(JSON_PROPERTY_SECONDARY)
    public void setSecondary_JsonNullable(JsonNullable<AuthPolicySecondaryPatch> secondary) {
        this.secondary = secondary;
    }

    public void setSecondary(@javax.annotation.Nullable AuthPolicySecondaryPatch secondary) {
        this.secondary = JsonNullable.<AuthPolicySecondaryPatch>of(secondary);
    }

    public AuthPolicyPatch tags(@javax.annotation.Nullable Tags tags) {
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

    /** Return true if this authPolicyPatch object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthPolicyPatch authPolicyPatch = (AuthPolicyPatch) o;
        return equalsNullable(this.name, authPolicyPatch.name)
                && Objects.equals(this.primary, authPolicyPatch.primary)
                && equalsNullable(this.secondary, authPolicyPatch.secondary)
                && Objects.equals(this.tags, authPolicyPatch.tags);
    }

    private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
        return a == b
                || (a != null
                        && b != null
                        && a.isPresent()
                        && b.isPresent()
                        && Objects.deepEquals(a.get(), b.get()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashCodeNullable(name), primary, hashCodeNullable(secondary), tags);
    }

    private static <T> int hashCodeNullable(JsonNullable<T> a) {
        if (a == null) {
            return 1;
        }
        return a.isPresent() ? Arrays.deepHashCode(new Object[] {a.get()}) : 31;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AuthPolicyPatch {\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    primary: ").append(toIndentedString(primary)).append("\n");
        sb.append("    secondary: ").append(toIndentedString(secondary)).append("\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
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

        // add `primary` to the URL query string
        if (getPrimary() != null) {
            joiner.add(getPrimary().toUrlQueryString(prefix + "primary" + suffix));
        }

        // add `secondary` to the URL query string
        if (getSecondary() != null) {
            joiner.add(getSecondary().toUrlQueryString(prefix + "secondary" + suffix));
        }

        // add `tags` to the URL query string
        if (getTags() != null) {
            joiner.add(getTags().toUrlQueryString(prefix + "tags" + suffix));
        }

        return joiner.toString();
    }
}
