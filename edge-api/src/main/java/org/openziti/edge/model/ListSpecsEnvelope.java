/*
 * Ziti Edge Client
 * OpenZiti Edge Client API
 *
 * The version of the OpenAPI document: 0.26.39
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/** ListSpecsEnvelope */
@JsonPropertyOrder({ListSpecsEnvelope.JSON_PROPERTY_DATA, ListSpecsEnvelope.JSON_PROPERTY_META})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-01-27T11:11:53.726065456-05:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class ListSpecsEnvelope {
    public static final String JSON_PROPERTY_DATA = "data";
    @javax.annotation.Nonnull private List<SpecDetail> data = new ArrayList<>();

    public static final String JSON_PROPERTY_META = "meta";
    @javax.annotation.Nonnull private Meta meta;

    public ListSpecsEnvelope() {}

    public ListSpecsEnvelope data(@javax.annotation.Nonnull List<SpecDetail> data) {
        this.data = data;
        return this;
    }

    public ListSpecsEnvelope addDataItem(SpecDetail dataItem) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.add(dataItem);
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
    public List<SpecDetail> getData() {
        return data;
    }

    @JsonProperty(JSON_PROPERTY_DATA)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setData(@javax.annotation.Nonnull List<SpecDetail> data) {
        this.data = data;
    }

    public ListSpecsEnvelope meta(@javax.annotation.Nonnull Meta meta) {
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

    /** Return true if this listSpecsEnvelope object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListSpecsEnvelope listSpecsEnvelope = (ListSpecsEnvelope) o;
        return Objects.equals(this.data, listSpecsEnvelope.data)
                && Objects.equals(this.meta, listSpecsEnvelope.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, meta);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ListSpecsEnvelope {\n");
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
            for (int i = 0; i < getData().size(); i++) {
                if (getData().get(i) != null) {
                    joiner.add(
                            getData()
                                    .get(i)
                                    .toUrlQueryString(
                                            String.format(
                                                    "%sdata%s%s",
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

        // add `meta` to the URL query string
        if (getMeta() != null) {
            joiner.add(getMeta().toUrlQueryString(prefix + "meta" + suffix));
        }

        return joiner.toString();
    }
}
