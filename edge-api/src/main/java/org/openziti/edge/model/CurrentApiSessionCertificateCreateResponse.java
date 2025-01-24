/*
 * Ziti Edge Client
 * OpenZiti Edge Client API
 *
 * The version of the OpenAPI document: 0.26.38
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import org.openziti.edge.ApiClient;

/** CurrentApiSessionCertificateCreateResponse */
@JsonPropertyOrder({
    CurrentApiSessionCertificateCreateResponse.JSON_PROPERTY_LINKS,
    CurrentApiSessionCertificateCreateResponse.JSON_PROPERTY_ID,
    CurrentApiSessionCertificateCreateResponse.JSON_PROPERTY_CAS,
    CurrentApiSessionCertificateCreateResponse.JSON_PROPERTY_CERTIFICATE
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-01-24T15:38:47.422198133-05:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class CurrentApiSessionCertificateCreateResponse {
    public static final String JSON_PROPERTY_LINKS = "_links";
    @javax.annotation.Nullable private Map<String, Link> links = new HashMap<>();

    public static final String JSON_PROPERTY_ID = "id";
    @javax.annotation.Nullable private String id;

    public static final String JSON_PROPERTY_CAS = "cas";
    @javax.annotation.Nullable private String cas;

    public static final String JSON_PROPERTY_CERTIFICATE = "certificate";
    @javax.annotation.Nonnull private String certificate;

    public CurrentApiSessionCertificateCreateResponse() {}

    public CurrentApiSessionCertificateCreateResponse links(
            @javax.annotation.Nullable Map<String, Link> links) {
        this.links = links;
        return this;
    }

    public CurrentApiSessionCertificateCreateResponse putLinksItem(String key, Link linksItem) {
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
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_LINKS)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Map<String, Link> getLinks() {
        return links;
    }

    @JsonProperty(JSON_PROPERTY_LINKS)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setLinks(@javax.annotation.Nullable Map<String, Link> links) {
        this.links = links;
    }

    public CurrentApiSessionCertificateCreateResponse id(@javax.annotation.Nullable String id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getId() {
        return id;
    }

    @JsonProperty(JSON_PROPERTY_ID)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setId(@javax.annotation.Nullable String id) {
        this.id = id;
    }

    public CurrentApiSessionCertificateCreateResponse cas(@javax.annotation.Nullable String cas) {
        this.cas = cas;
        return this;
    }

    /**
     * Get cas
     *
     * @return cas
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_CAS)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getCas() {
        return cas;
    }

    @JsonProperty(JSON_PROPERTY_CAS)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setCas(@javax.annotation.Nullable String cas) {
        this.cas = cas;
    }

    public CurrentApiSessionCertificateCreateResponse certificate(
            @javax.annotation.Nonnull String certificate) {
        this.certificate = certificate;
        return this;
    }

    /**
     * Get certificate
     *
     * @return certificate
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_CERTIFICATE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getCertificate() {
        return certificate;
    }

    @JsonProperty(JSON_PROPERTY_CERTIFICATE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setCertificate(@javax.annotation.Nonnull String certificate) {
        this.certificate = certificate;
    }

    /** Return true if this currentApiSessionCertificateCreateResponse object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CurrentApiSessionCertificateCreateResponse currentApiSessionCertificateCreateResponse =
                (CurrentApiSessionCertificateCreateResponse) o;
        return Objects.equals(this.links, currentApiSessionCertificateCreateResponse.links)
                && Objects.equals(this.id, currentApiSessionCertificateCreateResponse.id)
                && Objects.equals(this.cas, currentApiSessionCertificateCreateResponse.cas)
                && Objects.equals(
                        this.certificate, currentApiSessionCertificateCreateResponse.certificate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(links, id, cas, certificate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CurrentApiSessionCertificateCreateResponse {\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    cas: ").append(toIndentedString(cas)).append("\n");
        sb.append("    certificate: ").append(toIndentedString(certificate)).append("\n");
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

        // add `cas` to the URL query string
        if (getCas() != null) {
            joiner.add(
                    String.format(
                            "%scas%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getCas()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `certificate` to the URL query string
        if (getCertificate() != null) {
            joiner.add(
                    String.format(
                            "%scertificate%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getCertificate()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
