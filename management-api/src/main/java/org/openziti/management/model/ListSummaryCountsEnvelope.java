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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import org.openziti.management.ApiClient;

/** ListSummaryCountsEnvelope */
@JsonPropertyOrder({
    ListSummaryCountsEnvelope.JSON_PROPERTY_DATA,
    ListSummaryCountsEnvelope.JSON_PROPERTY_META
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-01-30T10:50:07.620098843-05:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class ListSummaryCountsEnvelope {
    public static final String JSON_PROPERTY_DATA = "data";
    @javax.annotation.Nonnull private Map<String, Integer> data = new HashMap<>();

    public static final String JSON_PROPERTY_META = "meta";
    @javax.annotation.Nonnull private Meta meta;

    public ListSummaryCountsEnvelope() {}

    public ListSummaryCountsEnvelope data(@javax.annotation.Nonnull Map<String, Integer> data) {
        this.data = data;
        return this;
    }

    public ListSummaryCountsEnvelope putDataItem(String key, Integer dataItem) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, dataItem);
        return this;
    }

    /**
     * Get data
     *
     * @return data
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_DATA)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Map<String, Integer> getData() {
        return data;
    }

    @JsonProperty(JSON_PROPERTY_DATA)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setData(@javax.annotation.Nonnull Map<String, Integer> data) {
        this.data = data;
    }

    public ListSummaryCountsEnvelope meta(@javax.annotation.Nonnull Meta meta) {
        this.meta = meta;
        return this;
    }

    /**
     * Get meta
     *
     * @return meta
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_META)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Meta getMeta() {
        return meta;
    }

    @JsonProperty(JSON_PROPERTY_META)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setMeta(@javax.annotation.Nonnull Meta meta) {
        this.meta = meta;
    }

    /** Return true if this listSummaryCountsEnvelope object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListSummaryCountsEnvelope listSummaryCountsEnvelope = (ListSummaryCountsEnvelope) o;
        return Objects.equals(this.data, listSummaryCountsEnvelope.data)
                && Objects.equals(this.meta, listSummaryCountsEnvelope.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, meta);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ListSummaryCountsEnvelope {\n");
        sb.append("    data: ").append(toIndentedString(data)).append("\n");
        sb.append("    meta: ").append(toIndentedString(meta)).append("\n");
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

        // add `data` to the URL query string
        if (getData() != null) {
            for (String _key : getData().keySet()) {
                joiner.add(
                        String.format(
                                "%sdata%s%s=%s",
                                prefix,
                                suffix,
                                "".equals(suffix)
                                        ? ""
                                        : String.format(
                                                "%s%d%s", containerPrefix, _key, containerSuffix),
                                getData().get(_key),
                                URLEncoder.encode(
                                                ApiClient.valueToString(getData().get(_key)),
                                                StandardCharsets.UTF_8)
                                        .replaceAll("\\+", "%20")));
            }
        }

        // add `meta` to the URL query string
        if (getMeta() != null) {
            joiner.add(getMeta().toUrlQueryString(prefix + "meta" + suffix));
        }

        return joiner.toString();
    }
}
