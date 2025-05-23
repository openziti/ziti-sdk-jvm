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

/** EdgeRouterPolicyCreate */
@JsonPropertyOrder({
    EdgeRouterPolicyCreate.JSON_PROPERTY_EDGE_ROUTER_ROLES,
    EdgeRouterPolicyCreate.JSON_PROPERTY_IDENTITY_ROLES,
    EdgeRouterPolicyCreate.JSON_PROPERTY_NAME,
    EdgeRouterPolicyCreate.JSON_PROPERTY_SEMANTIC,
    EdgeRouterPolicyCreate.JSON_PROPERTY_TAGS
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:49.931993799-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class EdgeRouterPolicyCreate {
    public static final String JSON_PROPERTY_EDGE_ROUTER_ROLES = "edgeRouterRoles";
    @javax.annotation.Nullable private List<String> edgeRouterRoles = new ArrayList<>();

    public static final String JSON_PROPERTY_IDENTITY_ROLES = "identityRoles";
    @javax.annotation.Nullable private List<String> identityRoles = new ArrayList<>();

    public static final String JSON_PROPERTY_NAME = "name";
    @javax.annotation.Nonnull private String name;

    public static final String JSON_PROPERTY_SEMANTIC = "semantic";
    @javax.annotation.Nonnull private Semantic semantic;

    public static final String JSON_PROPERTY_TAGS = "tags";
    @javax.annotation.Nullable private Tags tags;

    public EdgeRouterPolicyCreate() {}

    public EdgeRouterPolicyCreate edgeRouterRoles(
            @javax.annotation.Nullable List<String> edgeRouterRoles) {
        this.edgeRouterRoles = edgeRouterRoles;
        return this;
    }

    public EdgeRouterPolicyCreate addEdgeRouterRolesItem(String edgeRouterRolesItem) {
        if (this.edgeRouterRoles == null) {
            this.edgeRouterRoles = new ArrayList<>();
        }
        this.edgeRouterRoles.add(edgeRouterRolesItem);
        return this;
    }

    /**
     * Get edgeRouterRoles
     *
     * @return edgeRouterRoles
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_EDGE_ROUTER_ROLES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<String> getEdgeRouterRoles() {
        return edgeRouterRoles;
    }

    @JsonProperty(JSON_PROPERTY_EDGE_ROUTER_ROLES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setEdgeRouterRoles(@javax.annotation.Nullable List<String> edgeRouterRoles) {
        this.edgeRouterRoles = edgeRouterRoles;
    }

    public EdgeRouterPolicyCreate identityRoles(
            @javax.annotation.Nullable List<String> identityRoles) {
        this.identityRoles = identityRoles;
        return this;
    }

    public EdgeRouterPolicyCreate addIdentityRolesItem(String identityRolesItem) {
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

    public EdgeRouterPolicyCreate name(@javax.annotation.Nonnull String name) {
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

    public EdgeRouterPolicyCreate semantic(@javax.annotation.Nonnull Semantic semantic) {
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

    public EdgeRouterPolicyCreate tags(@javax.annotation.Nullable Tags tags) {
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

    /** Return true if this edgeRouterPolicyCreate object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EdgeRouterPolicyCreate edgeRouterPolicyCreate = (EdgeRouterPolicyCreate) o;
        return Objects.equals(this.edgeRouterRoles, edgeRouterPolicyCreate.edgeRouterRoles)
                && Objects.equals(this.identityRoles, edgeRouterPolicyCreate.identityRoles)
                && Objects.equals(this.name, edgeRouterPolicyCreate.name)
                && Objects.equals(this.semantic, edgeRouterPolicyCreate.semantic)
                && Objects.equals(this.tags, edgeRouterPolicyCreate.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(edgeRouterRoles, identityRoles, name, semantic, tags);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EdgeRouterPolicyCreate {\n");
        sb.append("    edgeRouterRoles: ").append(toIndentedString(edgeRouterRoles)).append("\n");
        sb.append("    identityRoles: ").append(toIndentedString(identityRoles)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    semantic: ").append(toIndentedString(semantic)).append("\n");
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

        // add `edgeRouterRoles` to the URL query string
        if (getEdgeRouterRoles() != null) {
            for (int i = 0; i < getEdgeRouterRoles().size(); i++) {
                joiner.add(
                        String.format(
                                "%sedgeRouterRoles%s%s=%s",
                                prefix,
                                suffix,
                                "".equals(suffix)
                                        ? ""
                                        : String.format(
                                                "%s%d%s", containerPrefix, i, containerSuffix),
                                URLEncoder.encode(
                                                ApiClient.valueToString(
                                                        getEdgeRouterRoles().get(i)),
                                                StandardCharsets.UTF_8)
                                        .replaceAll("\\+", "%20")));
            }
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

        // add `tags` to the URL query string
        if (getTags() != null) {
            joiner.add(getTags().toUrlQueryString(prefix + "tags" + suffix));
        }

        return joiner.toString();
    }
}
