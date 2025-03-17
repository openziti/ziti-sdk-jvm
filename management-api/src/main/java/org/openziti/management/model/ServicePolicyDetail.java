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
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import org.openziti.management.ApiClient;

/** ServicePolicyDetail */
@JsonPropertyOrder({
    ServicePolicyDetail.JSON_PROPERTY_LINKS,
    ServicePolicyDetail.JSON_PROPERTY_CREATED_AT,
    ServicePolicyDetail.JSON_PROPERTY_ID,
    ServicePolicyDetail.JSON_PROPERTY_TAGS,
    ServicePolicyDetail.JSON_PROPERTY_UPDATED_AT,
    ServicePolicyDetail.JSON_PROPERTY_IDENTITY_ROLES,
    ServicePolicyDetail.JSON_PROPERTY_IDENTITY_ROLES_DISPLAY,
    ServicePolicyDetail.JSON_PROPERTY_NAME,
    ServicePolicyDetail.JSON_PROPERTY_POSTURE_CHECK_ROLES,
    ServicePolicyDetail.JSON_PROPERTY_POSTURE_CHECK_ROLES_DISPLAY,
    ServicePolicyDetail.JSON_PROPERTY_SEMANTIC,
    ServicePolicyDetail.JSON_PROPERTY_SERVICE_ROLES,
    ServicePolicyDetail.JSON_PROPERTY_SERVICE_ROLES_DISPLAY,
    ServicePolicyDetail.JSON_PROPERTY_TYPE
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:49.931993799-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class ServicePolicyDetail {
    public static final String JSON_PROPERTY_LINKS = "_links";
    @javax.annotation.Nonnull private Map<String, Link> links = new HashMap<>();

    public static final String JSON_PROPERTY_CREATED_AT = "createdAt";
    @javax.annotation.Nonnull private OffsetDateTime createdAt;

    public static final String JSON_PROPERTY_ID = "id";
    @javax.annotation.Nonnull private String id;

    public static final String JSON_PROPERTY_TAGS = "tags";
    @javax.annotation.Nullable private Tags tags;

    public static final String JSON_PROPERTY_UPDATED_AT = "updatedAt";
    @javax.annotation.Nonnull private OffsetDateTime updatedAt;

    public static final String JSON_PROPERTY_IDENTITY_ROLES = "identityRoles";
    @javax.annotation.Nonnull private List<String> identityRoles = new ArrayList<>();

    public static final String JSON_PROPERTY_IDENTITY_ROLES_DISPLAY = "identityRolesDisplay";
    @javax.annotation.Nonnull private List<NamedRole> identityRolesDisplay = new ArrayList<>();

    public static final String JSON_PROPERTY_NAME = "name";
    @javax.annotation.Nonnull private String name;

    public static final String JSON_PROPERTY_POSTURE_CHECK_ROLES = "postureCheckRoles";
    @javax.annotation.Nonnull private List<String> postureCheckRoles = new ArrayList<>();

    public static final String JSON_PROPERTY_POSTURE_CHECK_ROLES_DISPLAY =
            "postureCheckRolesDisplay";
    @javax.annotation.Nonnull private List<NamedRole> postureCheckRolesDisplay = new ArrayList<>();

    public static final String JSON_PROPERTY_SEMANTIC = "semantic";
    @javax.annotation.Nonnull private Semantic semantic;

    public static final String JSON_PROPERTY_SERVICE_ROLES = "serviceRoles";
    @javax.annotation.Nonnull private List<String> serviceRoles = new ArrayList<>();

    public static final String JSON_PROPERTY_SERVICE_ROLES_DISPLAY = "serviceRolesDisplay";
    @javax.annotation.Nonnull private List<NamedRole> serviceRolesDisplay = new ArrayList<>();

    public static final String JSON_PROPERTY_TYPE = "type";
    @javax.annotation.Nonnull private DialBind type;

    public ServicePolicyDetail() {}

    public ServicePolicyDetail links(@javax.annotation.Nonnull Map<String, Link> links) {
        this.links = links;
        return this;
    }

    public ServicePolicyDetail putLinksItem(String key, Link linksItem) {
        if (this.links == null) {
            this.links = new HashMap<>();
        }
        this.links.put(key, linksItem);
        return this;
    }

    /**
     * A map of named links
     *
     * @return links
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_LINKS)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Map<String, Link> getLinks() {
        return links;
    }

    @JsonProperty(JSON_PROPERTY_LINKS)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setLinks(@javax.annotation.Nonnull Map<String, Link> links) {
        this.links = links;
    }

    public ServicePolicyDetail createdAt(@javax.annotation.Nonnull OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Get createdAt
     *
     * @return createdAt
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_CREATED_AT)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonProperty(JSON_PROPERTY_CREATED_AT)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setCreatedAt(@javax.annotation.Nonnull OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ServicePolicyDetail id(@javax.annotation.Nonnull String id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getId() {
        return id;
    }

    @JsonProperty(JSON_PROPERTY_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setId(@javax.annotation.Nonnull String id) {
        this.id = id;
    }

    public ServicePolicyDetail tags(@javax.annotation.Nullable Tags tags) {
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

    public ServicePolicyDetail updatedAt(@javax.annotation.Nonnull OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Get updatedAt
     *
     * @return updatedAt
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_UPDATED_AT)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty(JSON_PROPERTY_UPDATED_AT)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setUpdatedAt(@javax.annotation.Nonnull OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ServicePolicyDetail identityRoles(@javax.annotation.Nonnull List<String> identityRoles) {
        this.identityRoles = identityRoles;
        return this;
    }

    public ServicePolicyDetail addIdentityRolesItem(String identityRolesItem) {
        if (this.identityRoles == null) {
            this.identityRoles = new ArrayList<>();
        }
        this.identityRoles.add(identityRolesItem);
        return this;
    }

    /**
     * Get identityRoles
     *
     * @return identityRoles
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_IDENTITY_ROLES)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public List<String> getIdentityRoles() {
        return identityRoles;
    }

    @JsonProperty(JSON_PROPERTY_IDENTITY_ROLES)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setIdentityRoles(@javax.annotation.Nonnull List<String> identityRoles) {
        this.identityRoles = identityRoles;
    }

    public ServicePolicyDetail identityRolesDisplay(
            @javax.annotation.Nonnull List<NamedRole> identityRolesDisplay) {
        this.identityRolesDisplay = identityRolesDisplay;
        return this;
    }

    public ServicePolicyDetail addIdentityRolesDisplayItem(NamedRole identityRolesDisplayItem) {
        if (this.identityRolesDisplay == null) {
            this.identityRolesDisplay = new ArrayList<>();
        }
        this.identityRolesDisplay.add(identityRolesDisplayItem);
        return this;
    }

    /**
     * Get identityRolesDisplay
     *
     * @return identityRolesDisplay
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_IDENTITY_ROLES_DISPLAY)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public List<NamedRole> getIdentityRolesDisplay() {
        return identityRolesDisplay;
    }

    @JsonProperty(JSON_PROPERTY_IDENTITY_ROLES_DISPLAY)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setIdentityRolesDisplay(
            @javax.annotation.Nonnull List<NamedRole> identityRolesDisplay) {
        this.identityRolesDisplay = identityRolesDisplay;
    }

    public ServicePolicyDetail name(@javax.annotation.Nonnull String name) {
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

    public ServicePolicyDetail postureCheckRoles(
            @javax.annotation.Nonnull List<String> postureCheckRoles) {
        this.postureCheckRoles = postureCheckRoles;
        return this;
    }

    public ServicePolicyDetail addPostureCheckRolesItem(String postureCheckRolesItem) {
        if (this.postureCheckRoles == null) {
            this.postureCheckRoles = new ArrayList<>();
        }
        this.postureCheckRoles.add(postureCheckRolesItem);
        return this;
    }

    /**
     * Get postureCheckRoles
     *
     * @return postureCheckRoles
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_POSTURE_CHECK_ROLES)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public List<String> getPostureCheckRoles() {
        return postureCheckRoles;
    }

    @JsonProperty(JSON_PROPERTY_POSTURE_CHECK_ROLES)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setPostureCheckRoles(@javax.annotation.Nonnull List<String> postureCheckRoles) {
        this.postureCheckRoles = postureCheckRoles;
    }

    public ServicePolicyDetail postureCheckRolesDisplay(
            @javax.annotation.Nonnull List<NamedRole> postureCheckRolesDisplay) {
        this.postureCheckRolesDisplay = postureCheckRolesDisplay;
        return this;
    }

    public ServicePolicyDetail addPostureCheckRolesDisplayItem(
            NamedRole postureCheckRolesDisplayItem) {
        if (this.postureCheckRolesDisplay == null) {
            this.postureCheckRolesDisplay = new ArrayList<>();
        }
        this.postureCheckRolesDisplay.add(postureCheckRolesDisplayItem);
        return this;
    }

    /**
     * Get postureCheckRolesDisplay
     *
     * @return postureCheckRolesDisplay
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_POSTURE_CHECK_ROLES_DISPLAY)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public List<NamedRole> getPostureCheckRolesDisplay() {
        return postureCheckRolesDisplay;
    }

    @JsonProperty(JSON_PROPERTY_POSTURE_CHECK_ROLES_DISPLAY)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setPostureCheckRolesDisplay(
            @javax.annotation.Nonnull List<NamedRole> postureCheckRolesDisplay) {
        this.postureCheckRolesDisplay = postureCheckRolesDisplay;
    }

    public ServicePolicyDetail semantic(@javax.annotation.Nonnull Semantic semantic) {
        this.semantic = semantic;
        return this;
    }

    /**
     * Get semantic
     *
     * @return semantic
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_SEMANTIC)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Semantic getSemantic() {
        return semantic;
    }

    @JsonProperty(JSON_PROPERTY_SEMANTIC)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setSemantic(@javax.annotation.Nonnull Semantic semantic) {
        this.semantic = semantic;
    }

    public ServicePolicyDetail serviceRoles(@javax.annotation.Nonnull List<String> serviceRoles) {
        this.serviceRoles = serviceRoles;
        return this;
    }

    public ServicePolicyDetail addServiceRolesItem(String serviceRolesItem) {
        if (this.serviceRoles == null) {
            this.serviceRoles = new ArrayList<>();
        }
        this.serviceRoles.add(serviceRolesItem);
        return this;
    }

    /**
     * Get serviceRoles
     *
     * @return serviceRoles
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_SERVICE_ROLES)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public List<String> getServiceRoles() {
        return serviceRoles;
    }

    @JsonProperty(JSON_PROPERTY_SERVICE_ROLES)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setServiceRoles(@javax.annotation.Nonnull List<String> serviceRoles) {
        this.serviceRoles = serviceRoles;
    }

    public ServicePolicyDetail serviceRolesDisplay(
            @javax.annotation.Nonnull List<NamedRole> serviceRolesDisplay) {
        this.serviceRolesDisplay = serviceRolesDisplay;
        return this;
    }

    public ServicePolicyDetail addServiceRolesDisplayItem(NamedRole serviceRolesDisplayItem) {
        if (this.serviceRolesDisplay == null) {
            this.serviceRolesDisplay = new ArrayList<>();
        }
        this.serviceRolesDisplay.add(serviceRolesDisplayItem);
        return this;
    }

    /**
     * Get serviceRolesDisplay
     *
     * @return serviceRolesDisplay
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_SERVICE_ROLES_DISPLAY)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public List<NamedRole> getServiceRolesDisplay() {
        return serviceRolesDisplay;
    }

    @JsonProperty(JSON_PROPERTY_SERVICE_ROLES_DISPLAY)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setServiceRolesDisplay(
            @javax.annotation.Nonnull List<NamedRole> serviceRolesDisplay) {
        this.serviceRolesDisplay = serviceRolesDisplay;
    }

    public ServicePolicyDetail type(@javax.annotation.Nonnull DialBind type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     *
     * @return type
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_TYPE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public DialBind getType() {
        return type;
    }

    @JsonProperty(JSON_PROPERTY_TYPE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setType(@javax.annotation.Nonnull DialBind type) {
        this.type = type;
    }

    /** Return true if this servicePolicyDetail object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServicePolicyDetail servicePolicyDetail = (ServicePolicyDetail) o;
        return Objects.equals(this.links, servicePolicyDetail.links)
                && Objects.equals(this.createdAt, servicePolicyDetail.createdAt)
                && Objects.equals(this.id, servicePolicyDetail.id)
                && Objects.equals(this.tags, servicePolicyDetail.tags)
                && Objects.equals(this.updatedAt, servicePolicyDetail.updatedAt)
                && Objects.equals(this.identityRoles, servicePolicyDetail.identityRoles)
                && Objects.equals(
                        this.identityRolesDisplay, servicePolicyDetail.identityRolesDisplay)
                && Objects.equals(this.name, servicePolicyDetail.name)
                && Objects.equals(this.postureCheckRoles, servicePolicyDetail.postureCheckRoles)
                && Objects.equals(
                        this.postureCheckRolesDisplay, servicePolicyDetail.postureCheckRolesDisplay)
                && Objects.equals(this.semantic, servicePolicyDetail.semantic)
                && Objects.equals(this.serviceRoles, servicePolicyDetail.serviceRoles)
                && Objects.equals(this.serviceRolesDisplay, servicePolicyDetail.serviceRolesDisplay)
                && Objects.equals(this.type, servicePolicyDetail.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                links,
                createdAt,
                id,
                tags,
                updatedAt,
                identityRoles,
                identityRolesDisplay,
                name,
                postureCheckRoles,
                postureCheckRolesDisplay,
                semantic,
                serviceRoles,
                serviceRolesDisplay,
                type);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ServicePolicyDetail {\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
        sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
        sb.append("    identityRoles: ").append(toIndentedString(identityRoles)).append("\n");
        sb.append("    identityRolesDisplay: ")
                .append(toIndentedString(identityRolesDisplay))
                .append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    postureCheckRoles: ")
                .append(toIndentedString(postureCheckRoles))
                .append("\n");
        sb.append("    postureCheckRolesDisplay: ")
                .append(toIndentedString(postureCheckRolesDisplay))
                .append("\n");
        sb.append("    semantic: ").append(toIndentedString(semantic)).append("\n");
        sb.append("    serviceRoles: ").append(toIndentedString(serviceRoles)).append("\n");
        sb.append("    serviceRolesDisplay: ")
                .append(toIndentedString(serviceRolesDisplay))
                .append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

        // add `tags` to the URL query string
        if (getTags() != null) {
            joiner.add(getTags().toUrlQueryString(prefix + "tags" + suffix));
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

        // add `identityRoles` to the URL query string
        if (getIdentityRoles() != null) {
            for (int i = 0; i < getIdentityRoles().size(); i++) {
                joiner.add(
                        String.format(
                                "%sidentityRoles%s%s=%s",
                                prefix,
                                suffix,
                                "".equals(suffix)
                                        ? ""
                                        : String.format(
                                                "%s%d%s", containerPrefix, i, containerSuffix),
                                URLEncoder.encode(
                                                ApiClient.valueToString(getIdentityRoles().get(i)),
                                                StandardCharsets.UTF_8)
                                        .replaceAll("\\+", "%20")));
            }
        }

        // add `identityRolesDisplay` to the URL query string
        if (getIdentityRolesDisplay() != null) {
            for (int i = 0; i < getIdentityRolesDisplay().size(); i++) {
                if (getIdentityRolesDisplay().get(i) != null) {
                    joiner.add(
                            getIdentityRolesDisplay()
                                    .get(i)
                                    .toUrlQueryString(
                                            String.format(
                                                    "%sidentityRolesDisplay%s%s",
                                                    prefix,
                                                    suffix,
                                                    "".equals(suffix)
                                                            ? ""
                                                            : String.format(
                                                                    "%s%d%s",
                                                                    containerPrefix,
                                                                    i,
                                                                    containerSuffix))));
                }
            }
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

        // add `postureCheckRoles` to the URL query string
        if (getPostureCheckRoles() != null) {
            for (int i = 0; i < getPostureCheckRoles().size(); i++) {
                joiner.add(
                        String.format(
                                "%spostureCheckRoles%s%s=%s",
                                prefix,
                                suffix,
                                "".equals(suffix)
                                        ? ""
                                        : String.format(
                                                "%s%d%s", containerPrefix, i, containerSuffix),
                                URLEncoder.encode(
                                                ApiClient.valueToString(
                                                        getPostureCheckRoles().get(i)),
                                                StandardCharsets.UTF_8)
                                        .replaceAll("\\+", "%20")));
            }
        }

        // add `postureCheckRolesDisplay` to the URL query string
        if (getPostureCheckRolesDisplay() != null) {
            for (int i = 0; i < getPostureCheckRolesDisplay().size(); i++) {
                if (getPostureCheckRolesDisplay().get(i) != null) {
                    joiner.add(
                            getPostureCheckRolesDisplay()
                                    .get(i)
                                    .toUrlQueryString(
                                            String.format(
                                                    "%spostureCheckRolesDisplay%s%s",
                                                    prefix,
                                                    suffix,
                                                    "".equals(suffix)
                                                            ? ""
                                                            : String.format(
                                                                    "%s%d%s",
                                                                    containerPrefix,
                                                                    i,
                                                                    containerSuffix))));
                }
            }
        }

        // add `semantic` to the URL query string
        if (getSemantic() != null) {
            joiner.add(
                    String.format(
                            "%ssemantic%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getSemantic()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `serviceRoles` to the URL query string
        if (getServiceRoles() != null) {
            for (int i = 0; i < getServiceRoles().size(); i++) {
                joiner.add(
                        String.format(
                                "%sserviceRoles%s%s=%s",
                                prefix,
                                suffix,
                                "".equals(suffix)
                                        ? ""
                                        : String.format(
                                                "%s%d%s", containerPrefix, i, containerSuffix),
                                URLEncoder.encode(
                                                ApiClient.valueToString(getServiceRoles().get(i)),
                                                StandardCharsets.UTF_8)
                                        .replaceAll("\\+", "%20")));
            }
        }

        // add `serviceRolesDisplay` to the URL query string
        if (getServiceRolesDisplay() != null) {
            for (int i = 0; i < getServiceRolesDisplay().size(); i++) {
                if (getServiceRolesDisplay().get(i) != null) {
                    joiner.add(
                            getServiceRolesDisplay()
                                    .get(i)
                                    .toUrlQueryString(
                                            String.format(
                                                    "%sserviceRolesDisplay%s%s",
                                                    prefix,
                                                    suffix,
                                                    "".equals(suffix)
                                                            ? ""
                                                            : String.format(
                                                                    "%s%d%s",
                                                                    containerPrefix,
                                                                    i,
                                                                    containerSuffix))));
                }
            }
        }

        // add `type` to the URL query string
        if (getType() != null) {
            joiner.add(
                    String.format(
                            "%stype%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getType()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
