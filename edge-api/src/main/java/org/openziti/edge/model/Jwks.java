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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/** Jwks */
@JsonPropertyOrder({Jwks.JSON_PROPERTY_KEYS})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:45.850758361-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class Jwks {
    public static final String JSON_PROPERTY_KEYS = "keys";
    @javax.annotation.Nullable private List<Jwk> keys = new ArrayList<>();

    public Jwks() {}

    public Jwks keys(@javax.annotation.Nullable List<Jwk> keys) {
        this.keys = keys;
        return this;
    }

    public Jwks addKeysItem(Jwk keysItem) {
        if (this.keys == null) {
            this.keys = new ArrayList<>();
        }
        this.keys.add(keysItem);
        return this;
    }

    /**
     * Get keys
     *
     * @return keys
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_KEYS)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<Jwk> getKeys() {
        return keys;
    }

    @JsonProperty(JSON_PROPERTY_KEYS)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setKeys(@javax.annotation.Nullable List<Jwk> keys) {
        this.keys = keys;
    }

    /** Return true if this jwks object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Jwks jwks = (Jwks) o;
        return Objects.equals(this.keys, jwks.keys);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keys);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Jwks {\n");
        sb.append("    keys: ").append(toIndentedString(keys)).append("\n");
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

        // add `keys` to the URL query string
        if (getKeys() != null) {
            for (int i = 0; i < getKeys().size(); i++) {
                if (getKeys().get(i) != null) {
                    joiner.add(
                            getKeys()
                                    .get(i)
                                    .toUrlQueryString(
                                            String.format(
                                                    "%skeys%s%s",
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

        return joiner.toString();
    }
}
