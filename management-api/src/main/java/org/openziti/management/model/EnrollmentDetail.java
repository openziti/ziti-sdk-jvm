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
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openziti.management.ApiClient;

/**
 * An enrollment object. Enrollments are tied to identities and potentially a CA. Depending on the
 * method, different fields are utilized. For example ottca enrollments use the &#x60;ca&#x60; field
 * and updb enrollments use the username field, but not vice versa.
 */
@JsonPropertyOrder({
    EnrollmentDetail.JSON_PROPERTY_LINKS,
    EnrollmentDetail.JSON_PROPERTY_CREATED_AT,
    EnrollmentDetail.JSON_PROPERTY_ID,
    EnrollmentDetail.JSON_PROPERTY_TAGS,
    EnrollmentDetail.JSON_PROPERTY_UPDATED_AT,
    EnrollmentDetail.JSON_PROPERTY_CA_ID,
    EnrollmentDetail.JSON_PROPERTY_EDGE_ROUTER,
    EnrollmentDetail.JSON_PROPERTY_EDGE_ROUTER_ID,
    EnrollmentDetail.JSON_PROPERTY_EXPIRES_AT,
    EnrollmentDetail.JSON_PROPERTY_IDENTITY,
    EnrollmentDetail.JSON_PROPERTY_IDENTITY_ID,
    EnrollmentDetail.JSON_PROPERTY_JWT,
    EnrollmentDetail.JSON_PROPERTY_METHOD,
    EnrollmentDetail.JSON_PROPERTY_TOKEN,
    EnrollmentDetail.JSON_PROPERTY_TRANSIT_ROUTER,
    EnrollmentDetail.JSON_PROPERTY_TRANSIT_ROUTER_ID,
    EnrollmentDetail.JSON_PROPERTY_USERNAME
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:49.931993799-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class EnrollmentDetail {
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

    public static final String JSON_PROPERTY_CA_ID = "caId";
    private JsonNullable<String> caId = JsonNullable.<String>undefined();

    public static final String JSON_PROPERTY_EDGE_ROUTER = "edgeRouter";
    @javax.annotation.Nullable private EntityRef edgeRouter;

    public static final String JSON_PROPERTY_EDGE_ROUTER_ID = "edgeRouterId";
    @javax.annotation.Nullable private String edgeRouterId;

    public static final String JSON_PROPERTY_EXPIRES_AT = "expiresAt";
    @javax.annotation.Nonnull private OffsetDateTime expiresAt;

    public static final String JSON_PROPERTY_IDENTITY = "identity";
    @javax.annotation.Nullable private EntityRef identity;

    public static final String JSON_PROPERTY_IDENTITY_ID = "identityId";
    @javax.annotation.Nullable private String identityId;

    public static final String JSON_PROPERTY_JWT = "jwt";
    @javax.annotation.Nullable private String jwt;

    public static final String JSON_PROPERTY_METHOD = "method";
    @javax.annotation.Nonnull private String method;

    public static final String JSON_PROPERTY_TOKEN = "token";
    @javax.annotation.Nonnull private String token;

    public static final String JSON_PROPERTY_TRANSIT_ROUTER = "transitRouter";
    @javax.annotation.Nullable private EntityRef transitRouter;

    public static final String JSON_PROPERTY_TRANSIT_ROUTER_ID = "transitRouterId";
    @javax.annotation.Nullable private String transitRouterId;

    public static final String JSON_PROPERTY_USERNAME = "username";
    @javax.annotation.Nullable private String username;

    public EnrollmentDetail() {}

    public EnrollmentDetail links(@javax.annotation.Nonnull Map<String, Link> links) {
        this.links = links;
        return this;
    }

    public EnrollmentDetail putLinksItem(String key, Link linksItem) {
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

    public EnrollmentDetail createdAt(@javax.annotation.Nonnull OffsetDateTime createdAt) {
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

    public EnrollmentDetail id(@javax.annotation.Nonnull String id) {
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

    public EnrollmentDetail tags(@javax.annotation.Nullable Tags tags) {
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

    public EnrollmentDetail updatedAt(@javax.annotation.Nonnull OffsetDateTime updatedAt) {
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

    public EnrollmentDetail caId(@javax.annotation.Nullable String caId) {
        this.caId = JsonNullable.<String>of(caId);
        return this;
    }

    /**
     * Get caId
     *
     * @return caId
     */
    @javax.annotation.Nullable
    @JsonIgnore
    public String getCaId() {
        return caId.orElse(null);
    }

    @JsonProperty(JSON_PROPERTY_CA_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public JsonNullable<String> getCaId_JsonNullable() {
        return caId;
    }

    @JsonProperty(JSON_PROPERTY_CA_ID)
    public void setCaId_JsonNullable(JsonNullable<String> caId) {
        this.caId = caId;
    }

    public void setCaId(@javax.annotation.Nullable String caId) {
        this.caId = JsonNullable.<String>of(caId);
    }

    public EnrollmentDetail edgeRouter(@javax.annotation.Nullable EntityRef edgeRouter) {
        this.edgeRouter = edgeRouter;
        return this;
    }

    /**
     * Get edgeRouter
     *
     * @return edgeRouter
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_EDGE_ROUTER)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public EntityRef getEdgeRouter() {
        return edgeRouter;
    }

    @JsonProperty(JSON_PROPERTY_EDGE_ROUTER)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setEdgeRouter(@javax.annotation.Nullable EntityRef edgeRouter) {
        this.edgeRouter = edgeRouter;
    }

    public EnrollmentDetail edgeRouterId(@javax.annotation.Nullable String edgeRouterId) {
        this.edgeRouterId = edgeRouterId;
        return this;
    }

    /**
     * Get edgeRouterId
     *
     * @return edgeRouterId
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_EDGE_ROUTER_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getEdgeRouterId() {
        return edgeRouterId;
    }

    @JsonProperty(JSON_PROPERTY_EDGE_ROUTER_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setEdgeRouterId(@javax.annotation.Nullable String edgeRouterId) {
        this.edgeRouterId = edgeRouterId;
    }

    public EnrollmentDetail expiresAt(@javax.annotation.Nonnull OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    /**
     * Get expiresAt
     *
     * @return expiresAt
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_EXPIRES_AT)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    @JsonProperty(JSON_PROPERTY_EXPIRES_AT)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setExpiresAt(@javax.annotation.Nonnull OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public EnrollmentDetail identity(@javax.annotation.Nullable EntityRef identity) {
        this.identity = identity;
        return this;
    }

    /**
     * Get identity
     *
     * @return identity
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_IDENTITY)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public EntityRef getIdentity() {
        return identity;
    }

    @JsonProperty(JSON_PROPERTY_IDENTITY)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setIdentity(@javax.annotation.Nullable EntityRef identity) {
        this.identity = identity;
    }

    public EnrollmentDetail identityId(@javax.annotation.Nullable String identityId) {
        this.identityId = identityId;
        return this;
    }

    /**
     * Get identityId
     *
     * @return identityId
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_IDENTITY_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getIdentityId() {
        return identityId;
    }

    @JsonProperty(JSON_PROPERTY_IDENTITY_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setIdentityId(@javax.annotation.Nullable String identityId) {
        this.identityId = identityId;
    }

    public EnrollmentDetail jwt(@javax.annotation.Nullable String jwt) {
        this.jwt = jwt;
        return this;
    }

    /**
     * Get jwt
     *
     * @return jwt
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_JWT)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getJwt() {
        return jwt;
    }

    @JsonProperty(JSON_PROPERTY_JWT)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setJwt(@javax.annotation.Nullable String jwt) {
        this.jwt = jwt;
    }

    public EnrollmentDetail method(@javax.annotation.Nonnull String method) {
        this.method = method;
        return this;
    }

    /**
     * Get method
     *
     * @return method
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_METHOD)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getMethod() {
        return method;
    }

    @JsonProperty(JSON_PROPERTY_METHOD)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setMethod(@javax.annotation.Nonnull String method) {
        this.method = method;
    }

    public EnrollmentDetail token(@javax.annotation.Nonnull String token) {
        this.token = token;
        return this;
    }

    /**
     * Get token
     *
     * @return token
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_TOKEN)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getToken() {
        return token;
    }

    @JsonProperty(JSON_PROPERTY_TOKEN)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setToken(@javax.annotation.Nonnull String token) {
        this.token = token;
    }

    public EnrollmentDetail transitRouter(@javax.annotation.Nullable EntityRef transitRouter) {
        this.transitRouter = transitRouter;
        return this;
    }

    /**
     * Get transitRouter
     *
     * @return transitRouter
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_TRANSIT_ROUTER)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public EntityRef getTransitRouter() {
        return transitRouter;
    }

    @JsonProperty(JSON_PROPERTY_TRANSIT_ROUTER)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setTransitRouter(@javax.annotation.Nullable EntityRef transitRouter) {
        this.transitRouter = transitRouter;
    }

    public EnrollmentDetail transitRouterId(@javax.annotation.Nullable String transitRouterId) {
        this.transitRouterId = transitRouterId;
        return this;
    }

    /**
     * Get transitRouterId
     *
     * @return transitRouterId
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_TRANSIT_ROUTER_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getTransitRouterId() {
        return transitRouterId;
    }

    @JsonProperty(JSON_PROPERTY_TRANSIT_ROUTER_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setTransitRouterId(@javax.annotation.Nullable String transitRouterId) {
        this.transitRouterId = transitRouterId;
    }

    public EnrollmentDetail username(@javax.annotation.Nullable String username) {
        this.username = username;
        return this;
    }

    /**
     * Get username
     *
     * @return username
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_USERNAME)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getUsername() {
        return username;
    }

    @JsonProperty(JSON_PROPERTY_USERNAME)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setUsername(@javax.annotation.Nullable String username) {
        this.username = username;
    }

    /** Return true if this enrollmentDetail object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnrollmentDetail enrollmentDetail = (EnrollmentDetail) o;
        return Objects.equals(this.links, enrollmentDetail.links)
                && Objects.equals(this.createdAt, enrollmentDetail.createdAt)
                && Objects.equals(this.id, enrollmentDetail.id)
                && Objects.equals(this.tags, enrollmentDetail.tags)
                && Objects.equals(this.updatedAt, enrollmentDetail.updatedAt)
                && equalsNullable(this.caId, enrollmentDetail.caId)
                && Objects.equals(this.edgeRouter, enrollmentDetail.edgeRouter)
                && Objects.equals(this.edgeRouterId, enrollmentDetail.edgeRouterId)
                && Objects.equals(this.expiresAt, enrollmentDetail.expiresAt)
                && Objects.equals(this.identity, enrollmentDetail.identity)
                && Objects.equals(this.identityId, enrollmentDetail.identityId)
                && Objects.equals(this.jwt, enrollmentDetail.jwt)
                && Objects.equals(this.method, enrollmentDetail.method)
                && Objects.equals(this.token, enrollmentDetail.token)
                && Objects.equals(this.transitRouter, enrollmentDetail.transitRouter)
                && Objects.equals(this.transitRouterId, enrollmentDetail.transitRouterId)
                && Objects.equals(this.username, enrollmentDetail.username);
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
                links,
                createdAt,
                id,
                tags,
                updatedAt,
                hashCodeNullable(caId),
                edgeRouter,
                edgeRouterId,
                expiresAt,
                identity,
                identityId,
                jwt,
                method,
                token,
                transitRouter,
                transitRouterId,
                username);
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
        sb.append("class EnrollmentDetail {\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
        sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
        sb.append("    caId: ").append(toIndentedString(caId)).append("\n");
        sb.append("    edgeRouter: ").append(toIndentedString(edgeRouter)).append("\n");
        sb.append("    edgeRouterId: ").append(toIndentedString(edgeRouterId)).append("\n");
        sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
        sb.append("    identity: ").append(toIndentedString(identity)).append("\n");
        sb.append("    identityId: ").append(toIndentedString(identityId)).append("\n");
        sb.append("    jwt: ").append(toIndentedString(jwt)).append("\n");
        sb.append("    method: ").append(toIndentedString(method)).append("\n");
        sb.append("    token: ").append(toIndentedString(token)).append("\n");
        sb.append("    transitRouter: ").append(toIndentedString(transitRouter)).append("\n");
        sb.append("    transitRouterId: ").append(toIndentedString(transitRouterId)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
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

        // add `caId` to the URL query string
        if (getCaId() != null) {
            joiner.add(
                    String.format(
                            "%scaId%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getCaId()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `edgeRouter` to the URL query string
        if (getEdgeRouter() != null) {
            joiner.add(getEdgeRouter().toUrlQueryString(prefix + "edgeRouter" + suffix));
        }

        // add `edgeRouterId` to the URL query string
        if (getEdgeRouterId() != null) {
            joiner.add(
                    String.format(
                            "%sedgeRouterId%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getEdgeRouterId()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `expiresAt` to the URL query string
        if (getExpiresAt() != null) {
            joiner.add(
                    String.format(
                            "%sexpiresAt%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getExpiresAt()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `identity` to the URL query string
        if (getIdentity() != null) {
            joiner.add(getIdentity().toUrlQueryString(prefix + "identity" + suffix));
        }

        // add `identityId` to the URL query string
        if (getIdentityId() != null) {
            joiner.add(
                    String.format(
                            "%sidentityId%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getIdentityId()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `jwt` to the URL query string
        if (getJwt() != null) {
            joiner.add(
                    String.format(
                            "%sjwt%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getJwt()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `method` to the URL query string
        if (getMethod() != null) {
            joiner.add(
                    String.format(
                            "%smethod%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getMethod()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `token` to the URL query string
        if (getToken() != null) {
            joiner.add(
                    String.format(
                            "%stoken%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getToken()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `transitRouter` to the URL query string
        if (getTransitRouter() != null) {
            joiner.add(getTransitRouter().toUrlQueryString(prefix + "transitRouter" + suffix));
        }

        // add `transitRouterId` to the URL query string
        if (getTransitRouterId() != null) {
            joiner.add(
                    String.format(
                            "%stransitRouterId%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getTransitRouterId()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
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

        return joiner.toString();
    }
}
