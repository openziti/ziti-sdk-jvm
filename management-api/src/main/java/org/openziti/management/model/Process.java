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

/** Process */
@JsonPropertyOrder({
    Process.JSON_PROPERTY_HASHES,
    Process.JSON_PROPERTY_OS_TYPE,
    Process.JSON_PROPERTY_PATH,
    Process.JSON_PROPERTY_SIGNER_FINGERPRINT
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:49.931993799-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class Process {
    public static final String JSON_PROPERTY_HASHES = "hashes";
    @javax.annotation.Nullable private List<String> hashes = new ArrayList<>();

    public static final String JSON_PROPERTY_OS_TYPE = "osType";
    @javax.annotation.Nonnull private OsType osType;

    public static final String JSON_PROPERTY_PATH = "path";
    @javax.annotation.Nonnull private String path;

    public static final String JSON_PROPERTY_SIGNER_FINGERPRINT = "signerFingerprint";
    @javax.annotation.Nullable private String signerFingerprint;

    public Process() {}

    public Process hashes(@javax.annotation.Nullable List<String> hashes) {
        this.hashes = hashes;
        return this;
    }

    public Process addHashesItem(String hashesItem) {
        if (this.hashes == null) {
            this.hashes = new ArrayList<>();
        }
        this.hashes.add(hashesItem);
        return this;
    }

    /**
     * Get hashes
     *
     * @return hashes
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_HASHES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<String> getHashes() {
        return hashes;
    }

    @JsonProperty(JSON_PROPERTY_HASHES)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setHashes(@javax.annotation.Nullable List<String> hashes) {
        this.hashes = hashes;
    }

    public Process osType(@javax.annotation.Nonnull OsType osType) {
        this.osType = osType;
        return this;
    }

    /**
     * Get osType
     *
     * @return osType
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_OS_TYPE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public OsType getOsType() {
        return osType;
    }

    @JsonProperty(JSON_PROPERTY_OS_TYPE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setOsType(@javax.annotation.Nonnull OsType osType) {
        this.osType = osType;
    }

    public Process path(@javax.annotation.Nonnull String path) {
        this.path = path;
        return this;
    }

    /**
     * Get path
     *
     * @return path
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_PATH)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getPath() {
        return path;
    }

    @JsonProperty(JSON_PROPERTY_PATH)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setPath(@javax.annotation.Nonnull String path) {
        this.path = path;
    }

    public Process signerFingerprint(@javax.annotation.Nullable String signerFingerprint) {
        this.signerFingerprint = signerFingerprint;
        return this;
    }

    /**
     * Get signerFingerprint
     *
     * @return signerFingerprint
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_SIGNER_FINGERPRINT)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getSignerFingerprint() {
        return signerFingerprint;
    }

    @JsonProperty(JSON_PROPERTY_SIGNER_FINGERPRINT)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setSignerFingerprint(@javax.annotation.Nullable String signerFingerprint) {
        this.signerFingerprint = signerFingerprint;
    }

    /** Return true if this process object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Process process = (Process) o;
        return Objects.equals(this.hashes, process.hashes)
                && Objects.equals(this.osType, process.osType)
                && Objects.equals(this.path, process.path)
                && Objects.equals(this.signerFingerprint, process.signerFingerprint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashes, osType, path, signerFingerprint);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Process {\n");
        sb.append("    hashes: ").append(toIndentedString(hashes)).append("\n");
        sb.append("    osType: ").append(toIndentedString(osType)).append("\n");
        sb.append("    path: ").append(toIndentedString(path)).append("\n");
        sb.append("    signerFingerprint: ")
                .append(toIndentedString(signerFingerprint))
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

        // add `hashes` to the URL query string
        if (getHashes() != null) {
            for (int i = 0; i < getHashes().size(); i++) {
                joiner.add(
                        String.format(
                                "%shashes%s%s=%s",
                                prefix,
                                suffix,
                                "".equals(suffix)
                                        ? ""
                                        : String.format(
                                                "%s%d%s", containerPrefix, i, containerSuffix),
                                URLEncoder.encode(
                                                ApiClient.valueToString(getHashes().get(i)),
                                                StandardCharsets.UTF_8)
                                        .replaceAll("\\+", "%20")));
            }
        }

        // add `osType` to the URL query string
        if (getOsType() != null) {
            joiner.add(
                    String.format(
                            "%sosType%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getOsType()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `path` to the URL query string
        if (getPath() != null) {
            joiner.add(
                    String.format(
                            "%spath%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getPath()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `signerFingerprint` to the URL query string
        if (getSignerFingerprint() != null) {
            joiner.add(
                    String.format(
                            "%ssignerFingerprint%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getSignerFingerprint()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
