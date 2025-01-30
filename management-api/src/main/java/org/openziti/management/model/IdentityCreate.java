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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openziti.management.ApiClient;

/** An identity to create */
@JsonPropertyOrder({
    IdentityCreate.JSON_PROPERTY_APP_DATA,
    IdentityCreate.JSON_PROPERTY_AUTH_POLICY_ID,
    IdentityCreate.JSON_PROPERTY_DEFAULT_HOSTING_COST,
    IdentityCreate.JSON_PROPERTY_DEFAULT_HOSTING_PRECEDENCE,
    IdentityCreate.JSON_PROPERTY_ENROLLMENT,
    IdentityCreate.JSON_PROPERTY_EXTERNAL_ID,
    IdentityCreate.JSON_PROPERTY_IS_ADMIN,
    IdentityCreate.JSON_PROPERTY_NAME,
    IdentityCreate.JSON_PROPERTY_ROLE_ATTRIBUTES,
    IdentityCreate.JSON_PROPERTY_SERVICE_HOSTING_COSTS,
    IdentityCreate.JSON_PROPERTY_SERVICE_HOSTING_PRECEDENCES,
    IdentityCreate.JSON_PROPERTY_TAGS,
    IdentityCreate.JSON_PROPERTY_TYPE
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-01-30T10:50:07.620098843-05:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class IdentityCreate {
    public static final String JSON_PROPERTY_APP_DATA = "appData";
    @javax.annotation.Nullable private Tags appData;

    public static final String JSON_PROPERTY_AUTH_POLICY_ID = "authPolicyId";
    private JsonNullable<String> authPolicyId = JsonNullable.<String>undefined();

    public static final String JSON_PROPERTY_DEFAULT_HOSTING_COST = "defaultHostingCost";
    @javax.annotation.Nullable private Integer defaultHostingCost;

    public static final String JSON_PROPERTY_DEFAULT_HOSTING_PRECEDENCE =
            "defaultHostingPrecedence";
    @javax.annotation.Nullable private TerminatorPrecedence defaultHostingPrecedence;

    public static final String JSON_PROPERTY_ENROLLMENT = "enrollment";
    @javax.annotation.Nullable private IdentityCreateEnrollment enrollment;

    public static final String JSON_PROPERTY_EXTERNAL_ID = "externalId";
    private JsonNullable<String> externalId = JsonNullable.<String>undefined();

    public static final String JSON_PROPERTY_IS_ADMIN = "isAdmin";
    @javax.annotation.Nonnull private Boolean isAdmin;

    public static final String JSON_PROPERTY_NAME = "name";
    @javax.annotation.Nonnull private String name;

    public static final String JSON_PROPERTY_ROLE_ATTRIBUTES = "roleAttributes";
    private JsonNullable<List<String>> roleAttributes = JsonNullable.<List<String>>undefined();

    public static final String JSON_PROPERTY_SERVICE_HOSTING_COSTS = "serviceHostingCosts";
    @javax.annotation.Nullable private Map<String, Integer> serviceHostingCosts = new HashMap<>();

    public static final String JSON_PROPERTY_SERVICE_HOSTING_PRECEDENCES =
            "serviceHostingPrecedences";

    @javax.annotation.Nullable
    private Map<String, TerminatorPrecedence> serviceHostingPrecedences = new HashMap<>();

    public static final String JSON_PROPERTY_TAGS = "tags";
    @javax.annotation.Nullable private Tags tags;

    public static final String JSON_PROPERTY_TYPE = "type";
    @javax.annotation.Nonnull private IdentityType type;

    public IdentityCreate() {}

    public IdentityCreate appData(@javax.annotation.Nullable Tags appData) {
        this.appData = appData;
        return this;
    }

    /**
     * Get appData
     *
     * @return appData
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_APP_DATA)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Tags getAppData() {
        return appData;
    }

    @JsonProperty(JSON_PROPERTY_APP_DATA)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setAppData(@javax.annotation.Nullable Tags appData) {
        this.appData = appData;
    }

    public IdentityCreate authPolicyId(@javax.annotation.Nullable String authPolicyId) {
        this.authPolicyId = JsonNullable.<String>of(authPolicyId);
        return this;
    }

    /**
     * Get authPolicyId
     *
     * @return authPolicyId
     */
    @javax.annotation.Nullable
    @JsonIgnore
    public String getAuthPolicyId() {
        return authPolicyId.orElse(null);
    }

    @JsonProperty(JSON_PROPERTY_AUTH_POLICY_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public JsonNullable<String> getAuthPolicyId_JsonNullable() {
        return authPolicyId;
    }

    @JsonProperty(JSON_PROPERTY_AUTH_POLICY_ID)
    public void setAuthPolicyId_JsonNullable(JsonNullable<String> authPolicyId) {
        this.authPolicyId = authPolicyId;
    }

    public void setAuthPolicyId(@javax.annotation.Nullable String authPolicyId) {
        this.authPolicyId = JsonNullable.<String>of(authPolicyId);
    }

    public IdentityCreate defaultHostingCost(
            @javax.annotation.Nullable Integer defaultHostingCost) {
        this.defaultHostingCost = defaultHostingCost;
        return this;
    }

    /**
     * Get defaultHostingCost minimum: 0 maximum: 65535
     *
     * @return defaultHostingCost
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_DEFAULT_HOSTING_COST)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Integer getDefaultHostingCost() {
        return defaultHostingCost;
    }

    @JsonProperty(JSON_PROPERTY_DEFAULT_HOSTING_COST)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setDefaultHostingCost(@javax.annotation.Nullable Integer defaultHostingCost) {
        this.defaultHostingCost = defaultHostingCost;
    }

    public IdentityCreate defaultHostingPrecedence(
            @javax.annotation.Nullable TerminatorPrecedence defaultHostingPrecedence) {
        this.defaultHostingPrecedence = defaultHostingPrecedence;
        return this;
    }

    /**
     * Get defaultHostingPrecedence
     *
     * @return defaultHostingPrecedence
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_DEFAULT_HOSTING_PRECEDENCE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public TerminatorPrecedence getDefaultHostingPrecedence() {
        return defaultHostingPrecedence;
    }

    @JsonProperty(JSON_PROPERTY_DEFAULT_HOSTING_PRECEDENCE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setDefaultHostingPrecedence(
            @javax.annotation.Nullable TerminatorPrecedence defaultHostingPrecedence) {
        this.defaultHostingPrecedence = defaultHostingPrecedence;
    }

    public IdentityCreate enrollment(
            @javax.annotation.Nullable IdentityCreateEnrollment enrollment) {
        this.enrollment = enrollment;
        return this;
    }

    /**
     * Get enrollment
     *
     * @return enrollment
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_ENROLLMENT)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public IdentityCreateEnrollment getEnrollment() {
        return enrollment;
    }

    @JsonProperty(JSON_PROPERTY_ENROLLMENT)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setEnrollment(@javax.annotation.Nullable IdentityCreateEnrollment enrollment) {
        this.enrollment = enrollment;
    }

    public IdentityCreate externalId(@javax.annotation.Nullable String externalId) {
        this.externalId = JsonNullable.<String>of(externalId);
        return this;
    }

    /**
     * Get externalId
     *
     * @return externalId
     */
    @javax.annotation.Nullable
    @JsonIgnore
    public String getExternalId() {
        return externalId.orElse(null);
    }

    @JsonProperty(JSON_PROPERTY_EXTERNAL_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public JsonNullable<String> getExternalId_JsonNullable() {
        return externalId;
    }

    @JsonProperty(JSON_PROPERTY_EXTERNAL_ID)
    public void setExternalId_JsonNullable(JsonNullable<String> externalId) {
        this.externalId = externalId;
    }

    public void setExternalId(@javax.annotation.Nullable String externalId) {
        this.externalId = JsonNullable.<String>of(externalId);
    }

    public IdentityCreate isAdmin(@javax.annotation.Nonnull Boolean isAdmin) {
        this.isAdmin = isAdmin;
        return this;
    }

    /**
     * Get isAdmin
     *
     * @return isAdmin
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_IS_ADMIN)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Boolean getIsAdmin() {
        return isAdmin;
    }

    @JsonProperty(JSON_PROPERTY_IS_ADMIN)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setIsAdmin(@javax.annotation.Nonnull Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public IdentityCreate name(@javax.annotation.Nonnull String name) {
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

    public IdentityCreate roleAttributes(@javax.annotation.Nullable List<String> roleAttributes) {
        this.roleAttributes = JsonNullable.<List<String>>of(roleAttributes);
        return this;
    }

    public IdentityCreate addRoleAttributesItem(String roleAttributesItem) {
        if (this.roleAttributes == null || !this.roleAttributes.isPresent()) {
            this.roleAttributes = JsonNullable.<List<String>>of(new ArrayList<>());
        }
        try {
            this.roleAttributes.get().add(roleAttributesItem);
        } catch (java.util.NoSuchElementException e) {
            // this can never happen, as we make sure above that the value is present
        }
        return this;
    }

    /**
     * A set of strings used to loosly couple this resource to policies
     *
     * @return roleAttributes
     */
    @javax.annotation.Nullable
    @JsonIgnore
    public List<String> getRoleAttributes() {
        return roleAttributes.orElse(null);
    }

    @JsonProperty(JSON_PROPERTY_ROLE_ATTRIBUTES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public JsonNullable<List<String>> getRoleAttributes_JsonNullable() {
        return roleAttributes;
    }

    @JsonProperty(JSON_PROPERTY_ROLE_ATTRIBUTES)
    public void setRoleAttributes_JsonNullable(JsonNullable<List<String>> roleAttributes) {
        this.roleAttributes = roleAttributes;
    }

    public void setRoleAttributes(@javax.annotation.Nullable List<String> roleAttributes) {
        this.roleAttributes = JsonNullable.<List<String>>of(roleAttributes);
    }

    public IdentityCreate serviceHostingCosts(
            @javax.annotation.Nullable Map<String, Integer> serviceHostingCosts) {
        this.serviceHostingCosts = serviceHostingCosts;
        return this;
    }

    public IdentityCreate putServiceHostingCostsItem(String key, Integer serviceHostingCostsItem) {
        if (this.serviceHostingCosts == null) {
            this.serviceHostingCosts = new HashMap<>();
        }
        this.serviceHostingCosts.put(key, serviceHostingCostsItem);
        return this;
    }

    /**
     * Get serviceHostingCosts
     *
     * @return serviceHostingCosts
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_SERVICE_HOSTING_COSTS)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Map<String, Integer> getServiceHostingCosts() {
        return serviceHostingCosts;
    }

    @JsonProperty(JSON_PROPERTY_SERVICE_HOSTING_COSTS)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setServiceHostingCosts(
            @javax.annotation.Nullable Map<String, Integer> serviceHostingCosts) {
        this.serviceHostingCosts = serviceHostingCosts;
    }

    public IdentityCreate serviceHostingPrecedences(
            @javax.annotation.Nullable
                    Map<String, TerminatorPrecedence> serviceHostingPrecedences) {
        this.serviceHostingPrecedences = serviceHostingPrecedences;
        return this;
    }

    public IdentityCreate putServiceHostingPrecedencesItem(
            String key, TerminatorPrecedence serviceHostingPrecedencesItem) {
        if (this.serviceHostingPrecedences == null) {
            this.serviceHostingPrecedences = new HashMap<>();
        }
        this.serviceHostingPrecedences.put(key, serviceHostingPrecedencesItem);
        return this;
    }

    /**
     * Get serviceHostingPrecedences
     *
     * @return serviceHostingPrecedences
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_SERVICE_HOSTING_PRECEDENCES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Map<String, TerminatorPrecedence> getServiceHostingPrecedences() {
        return serviceHostingPrecedences;
    }

    @JsonProperty(JSON_PROPERTY_SERVICE_HOSTING_PRECEDENCES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setServiceHostingPrecedences(
            @javax.annotation.Nullable
                    Map<String, TerminatorPrecedence> serviceHostingPrecedences) {
        this.serviceHostingPrecedences = serviceHostingPrecedences;
    }

    public IdentityCreate tags(@javax.annotation.Nullable Tags tags) {
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

    public IdentityCreate type(@javax.annotation.Nonnull IdentityType type) {
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
    public IdentityType getType() {
        return type;
    }

    @JsonProperty(JSON_PROPERTY_TYPE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setType(@javax.annotation.Nonnull IdentityType type) {
        this.type = type;
    }

    /** Return true if this identityCreate object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentityCreate identityCreate = (IdentityCreate) o;
        return Objects.equals(this.appData, identityCreate.appData)
                && equalsNullable(this.authPolicyId, identityCreate.authPolicyId)
                && Objects.equals(this.defaultHostingCost, identityCreate.defaultHostingCost)
                && Objects.equals(
                        this.defaultHostingPrecedence, identityCreate.defaultHostingPrecedence)
                && Objects.equals(this.enrollment, identityCreate.enrollment)
                && equalsNullable(this.externalId, identityCreate.externalId)
                && Objects.equals(this.isAdmin, identityCreate.isAdmin)
                && Objects.equals(this.name, identityCreate.name)
                && equalsNullable(this.roleAttributes, identityCreate.roleAttributes)
                && Objects.equals(this.serviceHostingCosts, identityCreate.serviceHostingCosts)
                && Objects.equals(
                        this.serviceHostingPrecedences, identityCreate.serviceHostingPrecedences)
                && Objects.equals(this.tags, identityCreate.tags)
                && Objects.equals(this.type, identityCreate.type);
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
                appData,
                hashCodeNullable(authPolicyId),
                defaultHostingCost,
                defaultHostingPrecedence,
                enrollment,
                hashCodeNullable(externalId),
                isAdmin,
                name,
                hashCodeNullable(roleAttributes),
                serviceHostingCosts,
                serviceHostingPrecedences,
                tags,
                type);
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
        sb.append("class IdentityCreate {\n");
        sb.append("    appData: ").append(toIndentedString(appData)).append("\n");
        sb.append("    authPolicyId: ").append(toIndentedString(authPolicyId)).append("\n");
        sb.append("    defaultHostingCost: ")
                .append(toIndentedString(defaultHostingCost))
                .append("\n");
        sb.append("    defaultHostingPrecedence: ")
                .append(toIndentedString(defaultHostingPrecedence))
                .append("\n");
        sb.append("    enrollment: ").append(toIndentedString(enrollment)).append("\n");
        sb.append("    externalId: ").append(toIndentedString(externalId)).append("\n");
        sb.append("    isAdmin: ").append(toIndentedString(isAdmin)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    roleAttributes: ").append(toIndentedString(roleAttributes)).append("\n");
        sb.append("    serviceHostingCosts: ")
                .append(toIndentedString(serviceHostingCosts))
                .append("\n");
        sb.append("    serviceHostingPrecedences: ")
                .append(toIndentedString(serviceHostingPrecedences))
                .append("\n");
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

        // add `appData` to the URL query string
        if (getAppData() != null) {
            joiner.add(getAppData().toUrlQueryString(prefix + "appData" + suffix));
        }

        // add `authPolicyId` to the URL query string
        if (getAuthPolicyId() != null) {
            joiner.add(
                    String.format(
                            "%sauthPolicyId%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getAuthPolicyId()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `defaultHostingCost` to the URL query string
        if (getDefaultHostingCost() != null) {
            joiner.add(
                    String.format(
                            "%sdefaultHostingCost%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getDefaultHostingCost()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `defaultHostingPrecedence` to the URL query string
        if (getDefaultHostingPrecedence() != null) {
            joiner.add(
                    String.format(
                            "%sdefaultHostingPrecedence%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getDefaultHostingPrecedence()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `enrollment` to the URL query string
        if (getEnrollment() != null) {
            joiner.add(getEnrollment().toUrlQueryString(prefix + "enrollment" + suffix));
        }

        // add `externalId` to the URL query string
        if (getExternalId() != null) {
            joiner.add(
                    String.format(
                            "%sexternalId%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getExternalId()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `isAdmin` to the URL query string
        if (getIsAdmin() != null) {
            joiner.add(
                    String.format(
                            "%sisAdmin%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getIsAdmin()),
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

        // add `serviceHostingCosts` to the URL query string
        if (getServiceHostingCosts() != null) {
            for (String _key : getServiceHostingCosts().keySet()) {
                joiner.add(
                        String.format(
                                "%sserviceHostingCosts%s%s=%s",
                                prefix,
                                suffix,
                                "".equals(suffix)
                                        ? ""
                                        : String.format(
                                                "%s%d%s", containerPrefix, _key, containerSuffix),
                                getServiceHostingCosts().get(_key),
                                URLEncoder.encode(
                                                ApiClient.valueToString(
                                                        getServiceHostingCosts().get(_key)),
                                                StandardCharsets.UTF_8)
                                        .replaceAll("\\+", "%20")));
            }
        }

        // add `serviceHostingPrecedences` to the URL query string
        if (getServiceHostingPrecedences() != null) {
            for (String _key : getServiceHostingPrecedences().keySet()) {
                joiner.add(
                        String.format(
                                "%sserviceHostingPrecedences%s%s=%s",
                                prefix,
                                suffix,
                                "".equals(suffix)
                                        ? ""
                                        : String.format(
                                                "%s%d%s", containerPrefix, _key, containerSuffix),
                                getServiceHostingPrecedences().get(_key),
                                URLEncoder.encode(
                                                ApiClient.valueToString(
                                                        getServiceHostingPrecedences().get(_key)),
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
