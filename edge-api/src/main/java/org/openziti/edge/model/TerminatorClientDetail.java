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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import org.openziti.edge.ApiClient;

/** TerminatorClientDetail */
@JsonPropertyOrder({
    TerminatorClientDetail.JSON_PROPERTY_LINKS,
    TerminatorClientDetail.JSON_PROPERTY_CREATED_AT,
    TerminatorClientDetail.JSON_PROPERTY_ID,
    TerminatorClientDetail.JSON_PROPERTY_TAGS,
    TerminatorClientDetail.JSON_PROPERTY_UPDATED_AT,
    TerminatorClientDetail.JSON_PROPERTY_IDENTITY,
    TerminatorClientDetail.JSON_PROPERTY_ROUTER_ID,
    TerminatorClientDetail.JSON_PROPERTY_SERVICE,
    TerminatorClientDetail.JSON_PROPERTY_SERVICE_ID
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:45.850758361-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class TerminatorClientDetail {
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

    public static final String JSON_PROPERTY_IDENTITY = "identity";
    @javax.annotation.Nonnull private String identity;

    public static final String JSON_PROPERTY_ROUTER_ID = "routerId";
    @javax.annotation.Nonnull private String routerId;

    public static final String JSON_PROPERTY_SERVICE = "service";
    @javax.annotation.Nonnull private EntityRef service;

    public static final String JSON_PROPERTY_SERVICE_ID = "serviceId";
    @javax.annotation.Nonnull private String serviceId;

    public TerminatorClientDetail() {}

    public TerminatorClientDetail links(@javax.annotation.Nonnull Map<String, Link> links) {
        this.links = links;
        return this;
    }

    public TerminatorClientDetail putLinksItem(String key, Link linksItem) {
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

    public TerminatorClientDetail createdAt(@javax.annotation.Nonnull OffsetDateTime createdAt) {
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

    public TerminatorClientDetail id(@javax.annotation.Nonnull String id) {
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

    public TerminatorClientDetail tags(@javax.annotation.Nullable Tags tags) {
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

    public TerminatorClientDetail updatedAt(@javax.annotation.Nonnull OffsetDateTime updatedAt) {
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

    public TerminatorClientDetail identity(@javax.annotation.Nonnull String identity) {
        this.identity = identity;
        return this;
    }

    /**
     * Get identity
     *
     * @return identity
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_IDENTITY)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getIdentity() {
        return identity;
    }

    @JsonProperty(JSON_PROPERTY_IDENTITY)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setIdentity(@javax.annotation.Nonnull String identity) {
        this.identity = identity;
    }

    public TerminatorClientDetail routerId(@javax.annotation.Nonnull String routerId) {
        this.routerId = routerId;
        return this;
    }

    /**
     * Get routerId
     *
     * @return routerId
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_ROUTER_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getRouterId() {
        return routerId;
    }

    @JsonProperty(JSON_PROPERTY_ROUTER_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setRouterId(@javax.annotation.Nonnull String routerId) {
        this.routerId = routerId;
    }

    public TerminatorClientDetail service(@javax.annotation.Nonnull EntityRef service) {
        this.service = service;
        return this;
    }

    /**
     * Get service
     *
     * @return service
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_SERVICE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public EntityRef getService() {
        return service;
    }

    @JsonProperty(JSON_PROPERTY_SERVICE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setService(@javax.annotation.Nonnull EntityRef service) {
        this.service = service;
    }

    public TerminatorClientDetail serviceId(@javax.annotation.Nonnull String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    /**
     * Get serviceId
     *
     * @return serviceId
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_SERVICE_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getServiceId() {
        return serviceId;
    }

    @JsonProperty(JSON_PROPERTY_SERVICE_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setServiceId(@javax.annotation.Nonnull String serviceId) {
        this.serviceId = serviceId;
    }

    /** Return true if this terminatorClientDetail object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TerminatorClientDetail terminatorClientDetail = (TerminatorClientDetail) o;
        return Objects.equals(this.links, terminatorClientDetail.links)
                && Objects.equals(this.createdAt, terminatorClientDetail.createdAt)
                && Objects.equals(this.id, terminatorClientDetail.id)
                && Objects.equals(this.tags, terminatorClientDetail.tags)
                && Objects.equals(this.updatedAt, terminatorClientDetail.updatedAt)
                && Objects.equals(this.identity, terminatorClientDetail.identity)
                && Objects.equals(this.routerId, terminatorClientDetail.routerId)
                && Objects.equals(this.service, terminatorClientDetail.service)
                && Objects.equals(this.serviceId, terminatorClientDetail.serviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                links, createdAt, id, tags, updatedAt, identity, routerId, service, serviceId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TerminatorClientDetail {\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
        sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
        sb.append("    identity: ").append(toIndentedString(identity)).append("\n");
        sb.append("    routerId: ").append(toIndentedString(routerId)).append("\n");
        sb.append("    service: ").append(toIndentedString(service)).append("\n");
        sb.append("    serviceId: ").append(toIndentedString(serviceId)).append("\n");
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

        // add `identity` to the URL query string
        if (getIdentity() != null) {
            joiner.add(
                    String.format(
                            "%sidentity%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getIdentity()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `routerId` to the URL query string
        if (getRouterId() != null) {
            joiner.add(
                    String.format(
                            "%srouterId%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getRouterId()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `service` to the URL query string
        if (getService() != null) {
            joiner.add(getService().toUrlQueryString(prefix + "service" + suffix));
        }

        // add `serviceId` to the URL query string
        if (getServiceId() != null) {
            joiner.add(
                    String.format(
                            "%sserviceId%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getServiceId()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
