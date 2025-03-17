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

/** ServicePolicyPatch */
@JsonPropertyOrder({
    ServicePolicyPatch.JSON_PROPERTY_IDENTITY_ROLES,
    ServicePolicyPatch.JSON_PROPERTY_NAME,
    ServicePolicyPatch.JSON_PROPERTY_POSTURE_CHECK_ROLES,
    ServicePolicyPatch.JSON_PROPERTY_SEMANTIC,
    ServicePolicyPatch.JSON_PROPERTY_SERVICE_ROLES,
    ServicePolicyPatch.JSON_PROPERTY_TAGS,
    ServicePolicyPatch.JSON_PROPERTY_TYPE
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:49.931993799-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class ServicePolicyPatch {
    public static final String JSON_PROPERTY_IDENTITY_ROLES = "identityRoles";
    @javax.annotation.Nullable private List<String> identityRoles = new ArrayList<>();

    public static final String JSON_PROPERTY_NAME = "name";
    @javax.annotation.Nullable private String name;

    public static final String JSON_PROPERTY_POSTURE_CHECK_ROLES = "postureCheckRoles";
    @javax.annotation.Nullable private List<String> postureCheckRoles = new ArrayList<>();

    public static final String JSON_PROPERTY_SEMANTIC = "semantic";
    @javax.annotation.Nullable private Semantic semantic;

    public static final String JSON_PROPERTY_SERVICE_ROLES = "serviceRoles";
    @javax.annotation.Nullable private List<String> serviceRoles = new ArrayList<>();

    public static final String JSON_PROPERTY_TAGS = "tags";
    @javax.annotation.Nullable private Tags tags;

    public static final String JSON_PROPERTY_TYPE = "type";
    @javax.annotation.Nullable private DialBind type;

    public ServicePolicyPatch() {}

    public ServicePolicyPatch identityRoles(@javax.annotation.Nullable List<String> identityRoles) {
        this.identityRoles = identityRoles;
        return this;
    }

    public ServicePolicyPatch addIdentityRolesItem(String identityRolesItem) {
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
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_IDENTITY_ROLES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<String> getIdentityRoles() {
        return identityRoles;
    }

    @JsonProperty(JSON_PROPERTY_IDENTITY_ROLES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setIdentityRoles(@javax.annotation.Nullable List<String> identityRoles) {
        this.identityRoles = identityRoles;
    }

    public ServicePolicyPatch name(@javax.annotation.Nullable String name) {
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

    public ServicePolicyPatch postureCheckRoles(
            @javax.annotation.Nullable List<String> postureCheckRoles) {
        this.postureCheckRoles = postureCheckRoles;
        return this;
    }

    public ServicePolicyPatch addPostureCheckRolesItem(String postureCheckRolesItem) {
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
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_POSTURE_CHECK_ROLES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<String> getPostureCheckRoles() {
        return postureCheckRoles;
    }

    @JsonProperty(JSON_PROPERTY_POSTURE_CHECK_ROLES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setPostureCheckRoles(@javax.annotation.Nullable List<String> postureCheckRoles) {
        this.postureCheckRoles = postureCheckRoles;
    }

    public ServicePolicyPatch semantic(@javax.annotation.Nullable Semantic semantic) {
        this.semantic = semantic;
        return this;
    }

    /**
     * Get semantic
     *
     * @return semantic
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_SEMANTIC)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Semantic getSemantic() {
        return semantic;
    }

    @JsonProperty(JSON_PROPERTY_SEMANTIC)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setSemantic(@javax.annotation.Nullable Semantic semantic) {
        this.semantic = semantic;
    }

    public ServicePolicyPatch serviceRoles(@javax.annotation.Nullable List<String> serviceRoles) {
        this.serviceRoles = serviceRoles;
        return this;
    }

    public ServicePolicyPatch addServiceRolesItem(String serviceRolesItem) {
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
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_SERVICE_ROLES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<String> getServiceRoles() {
        return serviceRoles;
    }

    @JsonProperty(JSON_PROPERTY_SERVICE_ROLES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setServiceRoles(@javax.annotation.Nullable List<String> serviceRoles) {
        this.serviceRoles = serviceRoles;
    }

    public ServicePolicyPatch tags(@javax.annotation.Nullable Tags tags) {
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

    public ServicePolicyPatch type(@javax.annotation.Nullable DialBind type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     *
     * @return type
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_TYPE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public DialBind getType() {
        return type;
    }

    @JsonProperty(JSON_PROPERTY_TYPE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setType(@javax.annotation.Nullable DialBind type) {
        this.type = type;
    }

    /** Return true if this servicePolicyPatch object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServicePolicyPatch servicePolicyPatch = (ServicePolicyPatch) o;
        return Objects.equals(this.identityRoles, servicePolicyPatch.identityRoles)
                && Objects.equals(this.name, servicePolicyPatch.name)
                && Objects.equals(this.postureCheckRoles, servicePolicyPatch.postureCheckRoles)
                && Objects.equals(this.semantic, servicePolicyPatch.semantic)
                && Objects.equals(this.serviceRoles, servicePolicyPatch.serviceRoles)
                && Objects.equals(this.tags, servicePolicyPatch.tags)
                && Objects.equals(this.type, servicePolicyPatch.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                identityRoles, name, postureCheckRoles, semantic, serviceRoles, tags, type);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ServicePolicyPatch {\n");
        sb.append("    identityRoles: ").append(toIndentedString(identityRoles)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    postureCheckRoles: ")
                .append(toIndentedString(postureCheckRoles))
                .append("\n");
        sb.append("    semantic: ").append(toIndentedString(semantic)).append("\n");
        sb.append("    serviceRoles: ").append(toIndentedString(serviceRoles)).append("\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
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

        // add `tags` to the URL query string
        if (getTags() != null) {
            joiner.add(getTags().toUrlQueryString(prefix + "tags" + suffix));
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
