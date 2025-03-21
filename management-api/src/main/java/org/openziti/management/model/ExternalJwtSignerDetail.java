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
import java.net.URI;
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

/** A External JWT Signer resource */
@JsonPropertyOrder({
    ExternalJwtSignerDetail.JSON_PROPERTY_LINKS,
    ExternalJwtSignerDetail.JSON_PROPERTY_CREATED_AT,
    ExternalJwtSignerDetail.JSON_PROPERTY_ID,
    ExternalJwtSignerDetail.JSON_PROPERTY_TAGS,
    ExternalJwtSignerDetail.JSON_PROPERTY_UPDATED_AT,
    ExternalJwtSignerDetail.JSON_PROPERTY_AUDIENCE,
    ExternalJwtSignerDetail.JSON_PROPERTY_CERT_PEM,
    ExternalJwtSignerDetail.JSON_PROPERTY_CLAIMS_PROPERTY,
    ExternalJwtSignerDetail.JSON_PROPERTY_CLIENT_ID,
    ExternalJwtSignerDetail.JSON_PROPERTY_COMMON_NAME,
    ExternalJwtSignerDetail.JSON_PROPERTY_ENABLED,
    ExternalJwtSignerDetail.JSON_PROPERTY_EXTERNAL_AUTH_URL,
    ExternalJwtSignerDetail.JSON_PROPERTY_FINGERPRINT,
    ExternalJwtSignerDetail.JSON_PROPERTY_ISSUER,
    ExternalJwtSignerDetail.JSON_PROPERTY_JWKS_ENDPOINT,
    ExternalJwtSignerDetail.JSON_PROPERTY_KID,
    ExternalJwtSignerDetail.JSON_PROPERTY_NAME,
    ExternalJwtSignerDetail.JSON_PROPERTY_NOT_AFTER,
    ExternalJwtSignerDetail.JSON_PROPERTY_NOT_BEFORE,
    ExternalJwtSignerDetail.JSON_PROPERTY_SCOPES,
    ExternalJwtSignerDetail.JSON_PROPERTY_TARGET_TOKEN,
    ExternalJwtSignerDetail.JSON_PROPERTY_USE_EXTERNAL_ID
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:49.931993799-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class ExternalJwtSignerDetail {
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

    public static final String JSON_PROPERTY_AUDIENCE = "audience";
    @javax.annotation.Nonnull private String audience;

    public static final String JSON_PROPERTY_CERT_PEM = "certPem";
    @javax.annotation.Nullable private String certPem;

    public static final String JSON_PROPERTY_CLAIMS_PROPERTY = "claimsProperty";
    @javax.annotation.Nonnull private String claimsProperty;

    public static final String JSON_PROPERTY_CLIENT_ID = "clientId";
    @javax.annotation.Nonnull private String clientId;

    public static final String JSON_PROPERTY_COMMON_NAME = "commonName";
    @javax.annotation.Nonnull private String commonName;

    public static final String JSON_PROPERTY_ENABLED = "enabled";
    @javax.annotation.Nonnull private Boolean enabled;

    public static final String JSON_PROPERTY_EXTERNAL_AUTH_URL = "externalAuthUrl";
    @javax.annotation.Nonnull private String externalAuthUrl;

    public static final String JSON_PROPERTY_FINGERPRINT = "fingerprint";
    @javax.annotation.Nonnull private String fingerprint;

    public static final String JSON_PROPERTY_ISSUER = "issuer";
    @javax.annotation.Nonnull private String issuer;

    public static final String JSON_PROPERTY_JWKS_ENDPOINT = "jwksEndpoint";
    @javax.annotation.Nullable private URI jwksEndpoint;

    public static final String JSON_PROPERTY_KID = "kid";
    @javax.annotation.Nonnull private String kid;

    public static final String JSON_PROPERTY_NAME = "name";
    @javax.annotation.Nonnull private String name;

    public static final String JSON_PROPERTY_NOT_AFTER = "notAfter";
    @javax.annotation.Nonnull private OffsetDateTime notAfter;

    public static final String JSON_PROPERTY_NOT_BEFORE = "notBefore";
    @javax.annotation.Nonnull private OffsetDateTime notBefore;

    public static final String JSON_PROPERTY_SCOPES = "scopes";
    @javax.annotation.Nonnull private List<String> scopes = new ArrayList<>();

    public static final String JSON_PROPERTY_TARGET_TOKEN = "targetToken";
    @javax.annotation.Nullable private TargetToken targetToken;

    public static final String JSON_PROPERTY_USE_EXTERNAL_ID = "useExternalId";
    @javax.annotation.Nonnull private Boolean useExternalId;

    public ExternalJwtSignerDetail() {}

    public ExternalJwtSignerDetail links(@javax.annotation.Nonnull Map<String, Link> links) {
        this.links = links;
        return this;
    }

    public ExternalJwtSignerDetail putLinksItem(String key, Link linksItem) {
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

    public ExternalJwtSignerDetail createdAt(@javax.annotation.Nonnull OffsetDateTime createdAt) {
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

    public ExternalJwtSignerDetail id(@javax.annotation.Nonnull String id) {
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

    public ExternalJwtSignerDetail tags(@javax.annotation.Nullable Tags tags) {
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

    public ExternalJwtSignerDetail updatedAt(@javax.annotation.Nonnull OffsetDateTime updatedAt) {
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

    public ExternalJwtSignerDetail audience(@javax.annotation.Nonnull String audience) {
        this.audience = audience;
        return this;
    }

    /**
     * Get audience
     *
     * @return audience
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_AUDIENCE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getAudience() {
        return audience;
    }

    @JsonProperty(JSON_PROPERTY_AUDIENCE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setAudience(@javax.annotation.Nonnull String audience) {
        this.audience = audience;
    }

    public ExternalJwtSignerDetail certPem(@javax.annotation.Nullable String certPem) {
        this.certPem = certPem;
        return this;
    }

    /**
     * Get certPem
     *
     * @return certPem
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_CERT_PEM)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getCertPem() {
        return certPem;
    }

    @JsonProperty(JSON_PROPERTY_CERT_PEM)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setCertPem(@javax.annotation.Nullable String certPem) {
        this.certPem = certPem;
    }

    public ExternalJwtSignerDetail claimsProperty(@javax.annotation.Nonnull String claimsProperty) {
        this.claimsProperty = claimsProperty;
        return this;
    }

    /**
     * Get claimsProperty
     *
     * @return claimsProperty
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_CLAIMS_PROPERTY)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getClaimsProperty() {
        return claimsProperty;
    }

    @JsonProperty(JSON_PROPERTY_CLAIMS_PROPERTY)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setClaimsProperty(@javax.annotation.Nonnull String claimsProperty) {
        this.claimsProperty = claimsProperty;
    }

    public ExternalJwtSignerDetail clientId(@javax.annotation.Nonnull String clientId) {
        this.clientId = clientId;
        return this;
    }

    /**
     * Get clientId
     *
     * @return clientId
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_CLIENT_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getClientId() {
        return clientId;
    }

    @JsonProperty(JSON_PROPERTY_CLIENT_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setClientId(@javax.annotation.Nonnull String clientId) {
        this.clientId = clientId;
    }

    public ExternalJwtSignerDetail commonName(@javax.annotation.Nonnull String commonName) {
        this.commonName = commonName;
        return this;
    }

    /**
     * Get commonName
     *
     * @return commonName
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_COMMON_NAME)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getCommonName() {
        return commonName;
    }

    @JsonProperty(JSON_PROPERTY_COMMON_NAME)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setCommonName(@javax.annotation.Nonnull String commonName) {
        this.commonName = commonName;
    }

    public ExternalJwtSignerDetail enabled(@javax.annotation.Nonnull Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * Get enabled
     *
     * @return enabled
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_ENABLED)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Boolean getEnabled() {
        return enabled;
    }

    @JsonProperty(JSON_PROPERTY_ENABLED)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setEnabled(@javax.annotation.Nonnull Boolean enabled) {
        this.enabled = enabled;
    }

    public ExternalJwtSignerDetail externalAuthUrl(
            @javax.annotation.Nonnull String externalAuthUrl) {
        this.externalAuthUrl = externalAuthUrl;
        return this;
    }

    /**
     * Get externalAuthUrl
     *
     * @return externalAuthUrl
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_EXTERNAL_AUTH_URL)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getExternalAuthUrl() {
        return externalAuthUrl;
    }

    @JsonProperty(JSON_PROPERTY_EXTERNAL_AUTH_URL)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setExternalAuthUrl(@javax.annotation.Nonnull String externalAuthUrl) {
        this.externalAuthUrl = externalAuthUrl;
    }

    public ExternalJwtSignerDetail fingerprint(@javax.annotation.Nonnull String fingerprint) {
        this.fingerprint = fingerprint;
        return this;
    }

    /**
     * Get fingerprint
     *
     * @return fingerprint
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_FINGERPRINT)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getFingerprint() {
        return fingerprint;
    }

    @JsonProperty(JSON_PROPERTY_FINGERPRINT)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setFingerprint(@javax.annotation.Nonnull String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public ExternalJwtSignerDetail issuer(@javax.annotation.Nonnull String issuer) {
        this.issuer = issuer;
        return this;
    }

    /**
     * Get issuer
     *
     * @return issuer
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_ISSUER)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getIssuer() {
        return issuer;
    }

    @JsonProperty(JSON_PROPERTY_ISSUER)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setIssuer(@javax.annotation.Nonnull String issuer) {
        this.issuer = issuer;
    }

    public ExternalJwtSignerDetail jwksEndpoint(@javax.annotation.Nullable URI jwksEndpoint) {
        this.jwksEndpoint = jwksEndpoint;
        return this;
    }

    /**
     * Get jwksEndpoint
     *
     * @return jwksEndpoint
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_JWKS_ENDPOINT)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public URI getJwksEndpoint() {
        return jwksEndpoint;
    }

    @JsonProperty(JSON_PROPERTY_JWKS_ENDPOINT)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setJwksEndpoint(@javax.annotation.Nullable URI jwksEndpoint) {
        this.jwksEndpoint = jwksEndpoint;
    }

    public ExternalJwtSignerDetail kid(@javax.annotation.Nonnull String kid) {
        this.kid = kid;
        return this;
    }

    /**
     * Get kid
     *
     * @return kid
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_KID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getKid() {
        return kid;
    }

    @JsonProperty(JSON_PROPERTY_KID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setKid(@javax.annotation.Nonnull String kid) {
        this.kid = kid;
    }

    public ExternalJwtSignerDetail name(@javax.annotation.Nonnull String name) {
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

    public ExternalJwtSignerDetail notAfter(@javax.annotation.Nonnull OffsetDateTime notAfter) {
        this.notAfter = notAfter;
        return this;
    }

    /**
     * Get notAfter
     *
     * @return notAfter
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_NOT_AFTER)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public OffsetDateTime getNotAfter() {
        return notAfter;
    }

    @JsonProperty(JSON_PROPERTY_NOT_AFTER)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setNotAfter(@javax.annotation.Nonnull OffsetDateTime notAfter) {
        this.notAfter = notAfter;
    }

    public ExternalJwtSignerDetail notBefore(@javax.annotation.Nonnull OffsetDateTime notBefore) {
        this.notBefore = notBefore;
        return this;
    }

    /**
     * Get notBefore
     *
     * @return notBefore
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_NOT_BEFORE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public OffsetDateTime getNotBefore() {
        return notBefore;
    }

    @JsonProperty(JSON_PROPERTY_NOT_BEFORE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setNotBefore(@javax.annotation.Nonnull OffsetDateTime notBefore) {
        this.notBefore = notBefore;
    }

    public ExternalJwtSignerDetail scopes(@javax.annotation.Nonnull List<String> scopes) {
        this.scopes = scopes;
        return this;
    }

    public ExternalJwtSignerDetail addScopesItem(String scopesItem) {
        if (this.scopes == null) {
            this.scopes = new ArrayList<>();
        }
        this.scopes.add(scopesItem);
        return this;
    }

    /**
     * Get scopes
     *
     * @return scopes
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_SCOPES)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public List<String> getScopes() {
        return scopes;
    }

    @JsonProperty(JSON_PROPERTY_SCOPES)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setScopes(@javax.annotation.Nonnull List<String> scopes) {
        this.scopes = scopes;
    }

    public ExternalJwtSignerDetail targetToken(@javax.annotation.Nullable TargetToken targetToken) {
        this.targetToken = targetToken;
        return this;
    }

    /**
     * Get targetToken
     *
     * @return targetToken
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_TARGET_TOKEN)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public TargetToken getTargetToken() {
        return targetToken;
    }

    @JsonProperty(JSON_PROPERTY_TARGET_TOKEN)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setTargetToken(@javax.annotation.Nullable TargetToken targetToken) {
        this.targetToken = targetToken;
    }

    public ExternalJwtSignerDetail useExternalId(@javax.annotation.Nonnull Boolean useExternalId) {
        this.useExternalId = useExternalId;
        return this;
    }

    /**
     * Get useExternalId
     *
     * @return useExternalId
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_USE_EXTERNAL_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Boolean getUseExternalId() {
        return useExternalId;
    }

    @JsonProperty(JSON_PROPERTY_USE_EXTERNAL_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setUseExternalId(@javax.annotation.Nonnull Boolean useExternalId) {
        this.useExternalId = useExternalId;
    }

    /** Return true if this externalJwtSignerDetail object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExternalJwtSignerDetail externalJwtSignerDetail = (ExternalJwtSignerDetail) o;
        return Objects.equals(this.links, externalJwtSignerDetail.links)
                && Objects.equals(this.createdAt, externalJwtSignerDetail.createdAt)
                && Objects.equals(this.id, externalJwtSignerDetail.id)
                && Objects.equals(this.tags, externalJwtSignerDetail.tags)
                && Objects.equals(this.updatedAt, externalJwtSignerDetail.updatedAt)
                && Objects.equals(this.audience, externalJwtSignerDetail.audience)
                && Objects.equals(this.certPem, externalJwtSignerDetail.certPem)
                && Objects.equals(this.claimsProperty, externalJwtSignerDetail.claimsProperty)
                && Objects.equals(this.clientId, externalJwtSignerDetail.clientId)
                && Objects.equals(this.commonName, externalJwtSignerDetail.commonName)
                && Objects.equals(this.enabled, externalJwtSignerDetail.enabled)
                && Objects.equals(this.externalAuthUrl, externalJwtSignerDetail.externalAuthUrl)
                && Objects.equals(this.fingerprint, externalJwtSignerDetail.fingerprint)
                && Objects.equals(this.issuer, externalJwtSignerDetail.issuer)
                && Objects.equals(this.jwksEndpoint, externalJwtSignerDetail.jwksEndpoint)
                && Objects.equals(this.kid, externalJwtSignerDetail.kid)
                && Objects.equals(this.name, externalJwtSignerDetail.name)
                && Objects.equals(this.notAfter, externalJwtSignerDetail.notAfter)
                && Objects.equals(this.notBefore, externalJwtSignerDetail.notBefore)
                && Objects.equals(this.scopes, externalJwtSignerDetail.scopes)
                && Objects.equals(this.targetToken, externalJwtSignerDetail.targetToken)
                && Objects.equals(this.useExternalId, externalJwtSignerDetail.useExternalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                links,
                createdAt,
                id,
                tags,
                updatedAt,
                audience,
                certPem,
                claimsProperty,
                clientId,
                commonName,
                enabled,
                externalAuthUrl,
                fingerprint,
                issuer,
                jwksEndpoint,
                kid,
                name,
                notAfter,
                notBefore,
                scopes,
                targetToken,
                useExternalId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ExternalJwtSignerDetail {\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
        sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
        sb.append("    audience: ").append(toIndentedString(audience)).append("\n");
        sb.append("    certPem: ").append(toIndentedString(certPem)).append("\n");
        sb.append("    claimsProperty: ").append(toIndentedString(claimsProperty)).append("\n");
        sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
        sb.append("    commonName: ").append(toIndentedString(commonName)).append("\n");
        sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
        sb.append("    externalAuthUrl: ").append(toIndentedString(externalAuthUrl)).append("\n");
        sb.append("    fingerprint: ").append(toIndentedString(fingerprint)).append("\n");
        sb.append("    issuer: ").append(toIndentedString(issuer)).append("\n");
        sb.append("    jwksEndpoint: ").append(toIndentedString(jwksEndpoint)).append("\n");
        sb.append("    kid: ").append(toIndentedString(kid)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    notAfter: ").append(toIndentedString(notAfter)).append("\n");
        sb.append("    notBefore: ").append(toIndentedString(notBefore)).append("\n");
        sb.append("    scopes: ").append(toIndentedString(scopes)).append("\n");
        sb.append("    targetToken: ").append(toIndentedString(targetToken)).append("\n");
        sb.append("    useExternalId: ").append(toIndentedString(useExternalId)).append("\n");
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

        // add `audience` to the URL query string
        if (getAudience() != null) {
            joiner.add(
                    String.format(
                            "%saudience%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getAudience()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `certPem` to the URL query string
        if (getCertPem() != null) {
            joiner.add(
                    String.format(
                            "%scertPem%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getCertPem()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `claimsProperty` to the URL query string
        if (getClaimsProperty() != null) {
            joiner.add(
                    String.format(
                            "%sclaimsProperty%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getClaimsProperty()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `clientId` to the URL query string
        if (getClientId() != null) {
            joiner.add(
                    String.format(
                            "%sclientId%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getClientId()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `commonName` to the URL query string
        if (getCommonName() != null) {
            joiner.add(
                    String.format(
                            "%scommonName%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getCommonName()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `enabled` to the URL query string
        if (getEnabled() != null) {
            joiner.add(
                    String.format(
                            "%senabled%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getEnabled()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `externalAuthUrl` to the URL query string
        if (getExternalAuthUrl() != null) {
            joiner.add(
                    String.format(
                            "%sexternalAuthUrl%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getExternalAuthUrl()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `fingerprint` to the URL query string
        if (getFingerprint() != null) {
            joiner.add(
                    String.format(
                            "%sfingerprint%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getFingerprint()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `issuer` to the URL query string
        if (getIssuer() != null) {
            joiner.add(
                    String.format(
                            "%sissuer%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getIssuer()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `jwksEndpoint` to the URL query string
        if (getJwksEndpoint() != null) {
            joiner.add(
                    String.format(
                            "%sjwksEndpoint%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getJwksEndpoint()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `kid` to the URL query string
        if (getKid() != null) {
            joiner.add(
                    String.format(
                            "%skid%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getKid()),
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

        // add `notAfter` to the URL query string
        if (getNotAfter() != null) {
            joiner.add(
                    String.format(
                            "%snotAfter%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getNotAfter()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `notBefore` to the URL query string
        if (getNotBefore() != null) {
            joiner.add(
                    String.format(
                            "%snotBefore%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getNotBefore()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `scopes` to the URL query string
        if (getScopes() != null) {
            for (int i = 0; i < getScopes().size(); i++) {
                joiner.add(
                        String.format(
                                "%sscopes%s%s=%s",
                                prefix,
                                suffix,
                                "".equals(suffix)
                                        ? ""
                                        : String.format(
                                                "%s%d%s", containerPrefix, i, containerSuffix),
                                URLEncoder.encode(
                                                ApiClient.valueToString(getScopes().get(i)),
                                                StandardCharsets.UTF_8)
                                        .replaceAll("\\+", "%20")));
            }
        }

        // add `targetToken` to the URL query string
        if (getTargetToken() != null) {
            joiner.add(
                    String.format(
                            "%stargetToken%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getTargetToken()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `useExternalId` to the URL query string
        if (getUseExternalId() != null) {
            joiner.add(
                    String.format(
                            "%suseExternalId%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getUseExternalId()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
