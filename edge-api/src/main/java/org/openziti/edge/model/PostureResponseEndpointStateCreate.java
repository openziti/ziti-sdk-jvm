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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import org.openziti.edge.ApiClient;
import org.openziti.edge.JSON;

/** PostureResponseEndpointStateCreate */
@JsonPropertyOrder({
    PostureResponseEndpointStateCreate.JSON_PROPERTY_UNLOCKED,
    PostureResponseEndpointStateCreate.JSON_PROPERTY_WOKEN
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:45.850758361-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
@JsonIgnoreProperties(
        value = "typeId", // ignore manually set typeId, it will be automatically generated by
        // Jackson during serialization
        allowSetters = true // allows the typeId to be set during deserialization
        )
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "typeId",
        visible = true)
public class PostureResponseEndpointStateCreate extends PostureResponseCreate {
    public static final String JSON_PROPERTY_UNLOCKED = "unlocked";
    @javax.annotation.Nullable private Boolean unlocked;

    public static final String JSON_PROPERTY_WOKEN = "woken";
    @javax.annotation.Nullable private Boolean woken;

    public PostureResponseEndpointStateCreate() {}

    public PostureResponseEndpointStateCreate unlocked(
            @javax.annotation.Nullable Boolean unlocked) {
        this.unlocked = unlocked;
        return this;
    }

    /**
     * Get unlocked
     *
     * @return unlocked
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_UNLOCKED)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Boolean getUnlocked() {
        return unlocked;
    }

    @JsonProperty(JSON_PROPERTY_UNLOCKED)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setUnlocked(@javax.annotation.Nullable Boolean unlocked) {
        this.unlocked = unlocked;
    }

    public PostureResponseEndpointStateCreate woken(@javax.annotation.Nullable Boolean woken) {
        this.woken = woken;
        return this;
    }

    /**
     * Get woken
     *
     * @return woken
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_WOKEN)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Boolean getWoken() {
        return woken;
    }

    @JsonProperty(JSON_PROPERTY_WOKEN)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setWoken(@javax.annotation.Nullable Boolean woken) {
        this.woken = woken;
    }

    @Override
    public PostureResponseEndpointStateCreate id(@javax.annotation.Nonnull String id) {
        this.setId(id);
        return this;
    }

    @Override
    public PostureResponseEndpointStateCreate typeId(
            @javax.annotation.Nonnull PostureCheckType typeId) {
        this.setTypeId(typeId);
        return this;
    }

    /** Return true if this postureResponseEndpointStateCreate object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostureResponseEndpointStateCreate postureResponseEndpointStateCreate =
                (PostureResponseEndpointStateCreate) o;
        return Objects.equals(this.unlocked, postureResponseEndpointStateCreate.unlocked)
                && Objects.equals(this.woken, postureResponseEndpointStateCreate.woken)
                && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unlocked, woken, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PostureResponseEndpointStateCreate {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    unlocked: ").append(toIndentedString(unlocked)).append("\n");
        sb.append("    woken: ").append(toIndentedString(woken)).append("\n");
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

        // add `typeId` to the URL query string
        if (getTypeId() != null) {
            joiner.add(
                    String.format(
                            "%stypeId%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getTypeId()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `unlocked` to the URL query string
        if (getUnlocked() != null) {
            joiner.add(
                    String.format(
                            "%sunlocked%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getUnlocked()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `woken` to the URL query string
        if (getWoken() != null) {
            joiner.add(
                    String.format(
                            "%swoken%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getWoken()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }

    static {
        // Initialize and register the discriminator mappings.
        Map<String, Class<?>> mappings = new HashMap<String, Class<?>>();
        mappings.put(
                "postureResponseEndpointStateCreate", PostureResponseEndpointStateCreate.class);
        JSON.registerDiscriminator(PostureResponseEndpointStateCreate.class, "typeId", mappings);
    }
}
