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

/** RouterPatch */
@JsonPropertyOrder({
    RouterPatch.JSON_PROPERTY_COST,
    RouterPatch.JSON_PROPERTY_DISABLED,
    RouterPatch.JSON_PROPERTY_NAME,
    RouterPatch.JSON_PROPERTY_NO_TRAVERSAL,
    RouterPatch.JSON_PROPERTY_TAGS
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:49.931993799-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class RouterPatch {
    public static final String JSON_PROPERTY_COST = "cost";
    private JsonNullable<Integer> cost = JsonNullable.<Integer>undefined();

    public static final String JSON_PROPERTY_DISABLED = "disabled";
    private JsonNullable<Boolean> disabled = JsonNullable.<Boolean>undefined();

    public static final String JSON_PROPERTY_NAME = "name";
    @javax.annotation.Nullable private String name;

    public static final String JSON_PROPERTY_NO_TRAVERSAL = "noTraversal";
    private JsonNullable<Boolean> noTraversal = JsonNullable.<Boolean>undefined();

    public static final String JSON_PROPERTY_TAGS = "tags";
    @javax.annotation.Nullable private Tags tags;

    public RouterPatch() {}

    public RouterPatch cost(@javax.annotation.Nullable Integer cost) {
        this.cost = JsonNullable.<Integer>of(cost);
        return this;
    }

    /**
     * Get cost minimum: 0 maximum: 65535
     *
     * @return cost
     */
    @javax.annotation.Nullable
    @JsonIgnore
    public Integer getCost() {
        return cost.orElse(null);
    }

    @JsonProperty(JSON_PROPERTY_COST)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public JsonNullable<Integer> getCost_JsonNullable() {
        return cost;
    }

    @JsonProperty(JSON_PROPERTY_COST)
    public void setCost_JsonNullable(JsonNullable<Integer> cost) {
        this.cost = cost;
    }

    public void setCost(@javax.annotation.Nullable Integer cost) {
        this.cost = JsonNullable.<Integer>of(cost);
    }

    public RouterPatch disabled(@javax.annotation.Nullable Boolean disabled) {
        this.disabled = JsonNullable.<Boolean>of(disabled);
        return this;
    }

    /**
     * Get disabled
     *
     * @return disabled
     */
    @javax.annotation.Nullable
    @JsonIgnore
    public Boolean getDisabled() {
        return disabled.orElse(null);
    }

    @JsonProperty(JSON_PROPERTY_DISABLED)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public JsonNullable<Boolean> getDisabled_JsonNullable() {
        return disabled;
    }

    @JsonProperty(JSON_PROPERTY_DISABLED)
    public void setDisabled_JsonNullable(JsonNullable<Boolean> disabled) {
        this.disabled = disabled;
    }

    public void setDisabled(@javax.annotation.Nullable Boolean disabled) {
        this.disabled = JsonNullable.<Boolean>of(disabled);
    }

    public RouterPatch name(@javax.annotation.Nullable String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     *
     * @return name
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_NAME)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getName() {
        return name;
    }

    @JsonProperty(JSON_PROPERTY_NAME)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setName(@javax.annotation.Nullable String name) {
        this.name = name;
    }

    public RouterPatch noTraversal(@javax.annotation.Nullable Boolean noTraversal) {
        this.noTraversal = JsonNullable.<Boolean>of(noTraversal);
        return this;
    }

    /**
     * Get noTraversal
     *
     * @return noTraversal
     */
    @javax.annotation.Nullable
    @JsonIgnore
    public Boolean getNoTraversal() {
        return noTraversal.orElse(null);
    }

    @JsonProperty(JSON_PROPERTY_NO_TRAVERSAL)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public JsonNullable<Boolean> getNoTraversal_JsonNullable() {
        return noTraversal;
    }

    @JsonProperty(JSON_PROPERTY_NO_TRAVERSAL)
    public void setNoTraversal_JsonNullable(JsonNullable<Boolean> noTraversal) {
        this.noTraversal = noTraversal;
    }

    public void setNoTraversal(@javax.annotation.Nullable Boolean noTraversal) {
        this.noTraversal = JsonNullable.<Boolean>of(noTraversal);
    }

    public RouterPatch tags(@javax.annotation.Nullable Tags tags) {
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

    /** Return true if this routerPatch object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RouterPatch routerPatch = (RouterPatch) o;
        return equalsNullable(this.cost, routerPatch.cost)
                && equalsNullable(this.disabled, routerPatch.disabled)
                && Objects.equals(this.name, routerPatch.name)
                && equalsNullable(this.noTraversal, routerPatch.noTraversal)
                && Objects.equals(this.tags, routerPatch.tags);
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
        return Objects.hash(
                hashCodeNullable(cost),
                hashCodeNullable(disabled),
                name,
                hashCodeNullable(noTraversal),
                tags);
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
        sb.append("class RouterPatch {\n");
        sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
        sb.append("    disabled: ").append(toIndentedString(disabled)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    noTraversal: ").append(toIndentedString(noTraversal)).append("\n");
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

        // add `cost` to the URL query string
        if (getCost() != null) {
            joiner.add(
                    String.format(
                            "%scost%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getCost()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `disabled` to the URL query string
        if (getDisabled() != null) {
            joiner.add(
                    String.format(
                            "%sdisabled%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getDisabled()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

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

        // add `noTraversal` to the URL query string
        if (getNoTraversal() != null) {
            joiner.add(
                    String.format(
                            "%snoTraversal%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getNoTraversal()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `tags` to the URL query string
        if (getTags() != null) {
            joiner.add(getTags().toUrlQueryString(prefix + "tags" + suffix));
        }

        return joiner.toString();
    }
}
