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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import org.openziti.management.ApiClient;
import org.openziti.management.JSON;

/** PostureCheckMacAddressDetail */
@JsonPropertyOrder({PostureCheckMacAddressDetail.JSON_PROPERTY_MAC_ADDRESSES})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:49.931993799-04:00[America/New_York]",
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
public class PostureCheckMacAddressDetail extends PostureCheckDetail {
    public static final String JSON_PROPERTY_MAC_ADDRESSES = "macAddresses";
    @javax.annotation.Nonnull private List<String> macAddresses = new ArrayList<>();

    public PostureCheckMacAddressDetail() {}

    public PostureCheckMacAddressDetail macAddresses(
            @javax.annotation.Nonnull List<String> macAddresses) {
        this.macAddresses = macAddresses;
        return this;
    }

    public PostureCheckMacAddressDetail addMacAddressesItem(String macAddressesItem) {
        if (this.macAddresses == null) {
            this.macAddresses = new ArrayList<>();
        }
        this.macAddresses.add(macAddressesItem);
        return this;
    }

    /**
     * Get macAddresses
     *
     * @return macAddresses
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_MAC_ADDRESSES)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public List<String> getMacAddresses() {
        return macAddresses;
    }

    @JsonProperty(JSON_PROPERTY_MAC_ADDRESSES)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setMacAddresses(@javax.annotation.Nonnull List<String> macAddresses) {
        this.macAddresses = macAddresses;
    }

    @Override
    public PostureCheckMacAddressDetail createdAt(
            @javax.annotation.Nonnull OffsetDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    @Override
    public PostureCheckMacAddressDetail id(@javax.annotation.Nonnull String id) {
        this.setId(id);
        return this;
    }

    @Override
    public PostureCheckMacAddressDetail tags(@javax.annotation.Nonnull Tags tags) {
        this.setTags(tags);
        return this;
    }

    @Override
    public PostureCheckMacAddressDetail updatedAt(
            @javax.annotation.Nonnull OffsetDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    @Override
    public PostureCheckMacAddressDetail version(@javax.annotation.Nonnull Integer version) {
        this.setVersion(version);
        return this;
    }

    /** Return true if this postureCheckMacAddressDetail object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostureCheckMacAddressDetail postureCheckMacAddressDetail =
                (PostureCheckMacAddressDetail) o;
        return Objects.equals(this.macAddresses, postureCheckMacAddressDetail.macAddresses)
                && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(macAddresses, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PostureCheckMacAddressDetail {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    macAddresses: ").append(toIndentedString(macAddresses)).append("\n");
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

        // add `_links` to the URL query string
        if (getLinks() != null) {
            for (String _key : getLinks().keySet()) {
                if (getLinks().get(_key) != null) {
                    joiner.add(
                            getLinks()
                                    .get(_key)
                                    .toUrlQueryString(
                                            String.format(
                                                    "%s_links%s%s",
                                                    prefix,
                                                    suffix,
                                                    "".equals(suffix)
                                                            ? ""
                                                            : String.format(
                                                                    "%s%d%s",
                                                                    containerPrefix,
                                                                    _key,
                                                                    containerSuffix))));
                }
            }
        }

        // add `createdAt` to the URL query string
        if (getCreatedAt() != null) {
            joiner.add(
                    String.format(
                            "%screatedAt%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getCreatedAt()),
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

        // add `roleAttributes` to the URL query string
        if (getRoleAttributes() != null) {
            for (int i = 0; i < getRoleAttributes().size(); i++) {
                joiner.add(
                        String.format(
                                "%sroleAttributes%s%s=%s",
                                prefix,
                                suffix,
                                "".equals(suffix)
                                        ? ""
                                        : String.format(
                                                "%s%d%s", containerPrefix, i, containerSuffix),
                                URLEncoder.encode(
                                                ApiClient.valueToString(getRoleAttributes().get(i)),
                                                StandardCharsets.UTF_8)
                                        .replaceAll("\\+", "%20")));
            }
        }

        // add `tags` to the URL query string
        if (getTags() != null) {
            joiner.add(getTags().toUrlQueryString(prefix + "tags" + suffix));
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

        // add `updatedAt` to the URL query string
        if (getUpdatedAt() != null) {
            joiner.add(
                    String.format(
                            "%supdatedAt%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getUpdatedAt()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `version` to the URL query string
        if (getVersion() != null) {
            joiner.add(
                    String.format(
                            "%sversion%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getVersion()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `macAddresses` to the URL query string
        if (getMacAddresses() != null) {
            for (int i = 0; i < getMacAddresses().size(); i++) {
                joiner.add(
                        String.format(
                                "%smacAddresses%s%s=%s",
                                prefix,
                                suffix,
                                "".equals(suffix)
                                        ? ""
                                        : String.format(
                                                "%s%d%s", containerPrefix, i, containerSuffix),
                                URLEncoder.encode(
                                                ApiClient.valueToString(getMacAddresses().get(i)),
                                                StandardCharsets.UTF_8)
                                        .replaceAll("\\+", "%20")));
            }
        }

        return joiner.toString();
    }

    static {
        // Initialize and register the discriminator mappings.
        Map<String, Class<?>> mappings = new HashMap<String, Class<?>>();
        mappings.put("postureCheckMacAddressDetail", PostureCheckMacAddressDetail.class);
        JSON.registerDiscriminator(PostureCheckMacAddressDetail.class, "typeId", mappings);
    }
}
