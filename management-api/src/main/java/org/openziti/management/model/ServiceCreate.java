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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import org.openziti.management.ApiClient;

/** ServiceCreate */
@JsonPropertyOrder({
    ServiceCreate.JSON_PROPERTY_CONFIGS,
    ServiceCreate.JSON_PROPERTY_ENCRYPTION_REQUIRED,
    ServiceCreate.JSON_PROPERTY_MAX_IDLE_TIME_MILLIS,
    ServiceCreate.JSON_PROPERTY_NAME,
    ServiceCreate.JSON_PROPERTY_ROLE_ATTRIBUTES,
    ServiceCreate.JSON_PROPERTY_TAGS,
    ServiceCreate.JSON_PROPERTY_TERMINATOR_STRATEGY
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:49.931993799-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class ServiceCreate {
    public static final String JSON_PROPERTY_CONFIGS = "configs";
    @javax.annotation.Nullable private List<String> configs = new ArrayList<>();

    public static final String JSON_PROPERTY_ENCRYPTION_REQUIRED = "encryptionRequired";
    @javax.annotation.Nonnull private Boolean encryptionRequired;

    public static final String JSON_PROPERTY_MAX_IDLE_TIME_MILLIS = "maxIdleTimeMillis";
    @javax.annotation.Nullable private Integer maxIdleTimeMillis;

    public static final String JSON_PROPERTY_NAME = "name";
    @javax.annotation.Nonnull private String name;

    public static final String JSON_PROPERTY_ROLE_ATTRIBUTES = "roleAttributes";
    @javax.annotation.Nullable private List<String> roleAttributes = new ArrayList<>();

    public static final String JSON_PROPERTY_TAGS = "tags";
    @javax.annotation.Nullable private Tags tags;

    public static final String JSON_PROPERTY_TERMINATOR_STRATEGY = "terminatorStrategy";
    @javax.annotation.Nullable private String terminatorStrategy;

    public ServiceCreate() {}

    public ServiceCreate configs(@javax.annotation.Nullable List<String> configs) {
        this.configs = configs;
        return this;
    }

    public ServiceCreate addConfigsItem(String configsItem) {
        if (this.configs == null) {
            this.configs = new ArrayList<>();
        }
        this.configs.add(configsItem);
        return this;
    }

    /**
     * Get configs
     *
     * @return configs
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_CONFIGS)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<String> getConfigs() {
        return configs;
    }

    @JsonProperty(JSON_PROPERTY_CONFIGS)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setConfigs(@javax.annotation.Nullable List<String> configs) {
        this.configs = configs;
    }

    public ServiceCreate encryptionRequired(@javax.annotation.Nonnull Boolean encryptionRequired) {
        this.encryptionRequired = encryptionRequired;
        return this;
    }

    /**
     * Describes whether connections must support end-to-end encryption on both sides of the
     * connection.
     *
     * @return encryptionRequired
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_ENCRYPTION_REQUIRED)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Boolean getEncryptionRequired() {
        return encryptionRequired;
    }

    @JsonProperty(JSON_PROPERTY_ENCRYPTION_REQUIRED)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setEncryptionRequired(@javax.annotation.Nonnull Boolean encryptionRequired) {
        this.encryptionRequired = encryptionRequired;
    }

    public ServiceCreate maxIdleTimeMillis(@javax.annotation.Nullable Integer maxIdleTimeMillis) {
        this.maxIdleTimeMillis = maxIdleTimeMillis;
        return this;
    }

    /**
     * Get maxIdleTimeMillis
     *
     * @return maxIdleTimeMillis
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_MAX_IDLE_TIME_MILLIS)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Integer getMaxIdleTimeMillis() {
        return maxIdleTimeMillis;
    }

    @JsonProperty(JSON_PROPERTY_MAX_IDLE_TIME_MILLIS)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setMaxIdleTimeMillis(@javax.annotation.Nullable Integer maxIdleTimeMillis) {
        this.maxIdleTimeMillis = maxIdleTimeMillis;
    }

    public ServiceCreate name(@javax.annotation.Nonnull String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     *
     * @return name
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_NAME)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getName() {
        return name;
    }

    @JsonProperty(JSON_PROPERTY_NAME)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setName(@javax.annotation.Nonnull String name) {
        this.name = name;
    }

    public ServiceCreate roleAttributes(@javax.annotation.Nullable List<String> roleAttributes) {
        this.roleAttributes = roleAttributes;
        return this;
    }

    public ServiceCreate addRoleAttributesItem(String roleAttributesItem) {
        if (this.roleAttributes == null) {
            this.roleAttributes = new ArrayList<>();
        }
        this.roleAttributes.add(roleAttributesItem);
        return this;
    }

    /**
     * Get roleAttributes
     *
     * @return roleAttributes
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_ROLE_ATTRIBUTES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<String> getRoleAttributes() {
        return roleAttributes;
    }

    @JsonProperty(JSON_PROPERTY_ROLE_ATTRIBUTES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setRoleAttributes(@javax.annotation.Nullable List<String> roleAttributes) {
        this.roleAttributes = roleAttributes;
    }

    public ServiceCreate tags(@javax.annotation.Nullable Tags tags) {
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

    public ServiceCreate terminatorStrategy(@javax.annotation.Nullable String terminatorStrategy) {
        this.terminatorStrategy = terminatorStrategy;
        return this;
    }

    /**
     * Get terminatorStrategy
     *
     * @return terminatorStrategy
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_TERMINATOR_STRATEGY)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getTerminatorStrategy() {
        return terminatorStrategy;
    }

    @JsonProperty(JSON_PROPERTY_TERMINATOR_STRATEGY)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setTerminatorStrategy(@javax.annotation.Nullable String terminatorStrategy) {
        this.terminatorStrategy = terminatorStrategy;
    }

    /** Return true if this serviceCreate object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceCreate serviceCreate = (ServiceCreate) o;
        return Objects.equals(this.configs, serviceCreate.configs)
                && Objects.equals(this.encryptionRequired, serviceCreate.encryptionRequired)
                && Objects.equals(this.maxIdleTimeMillis, serviceCreate.maxIdleTimeMillis)
                && Objects.equals(this.name, serviceCreate.name)
                && Objects.equals(this.roleAttributes, serviceCreate.roleAttributes)
                && Objects.equals(this.tags, serviceCreate.tags)
                && Objects.equals(this.terminatorStrategy, serviceCreate.terminatorStrategy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                configs,
                encryptionRequired,
                maxIdleTimeMillis,
                name,
                roleAttributes,
                tags,
                terminatorStrategy);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ServiceCreate {\n");
        sb.append("    configs: ").append(toIndentedString(configs)).append("\n");
        sb.append("    encryptionRequired: ")
                .append(toIndentedString(encryptionRequired))
                .append("\n");
        sb.append("    maxIdleTimeMillis: ")
                .append(toIndentedString(maxIdleTimeMillis))
                .append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    roleAttributes: ").append(toIndentedString(roleAttributes)).append("\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
        sb.append("    terminatorStrategy: ")
                .append(toIndentedString(terminatorStrategy))
                .append("\n");
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

        // add `configs` to the URL query string
        if (getConfigs() != null) {
            for (int i = 0; i < getConfigs().size(); i++) {
                joiner.add(
                        String.format(
                                "%sconfigs%s%s=%s",
                                prefix,
                                suffix,
                                "".equals(suffix)
                                        ? ""
                                        : String.format(
                                                "%s%d%s", containerPrefix, i, containerSuffix),
                                URLEncoder.encode(
                                                ApiClient.valueToString(getConfigs().get(i)),
                                                StandardCharsets.UTF_8)
                                        .replaceAll("\\+", "%20")));
            }
        }

        // add `encryptionRequired` to the URL query string
        if (getEncryptionRequired() != null) {
            joiner.add(
                    String.format(
                            "%sencryptionRequired%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getEncryptionRequired()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `maxIdleTimeMillis` to the URL query string
        if (getMaxIdleTimeMillis() != null) {
            joiner.add(
                    String.format(
                            "%smaxIdleTimeMillis%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getMaxIdleTimeMillis()),
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

        // add `terminatorStrategy` to the URL query string
        if (getTerminatorStrategy() != null) {
            joiner.add(
                    String.format(
                            "%sterminatorStrategy%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getTerminatorStrategy()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
