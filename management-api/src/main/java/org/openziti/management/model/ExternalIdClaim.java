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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.StringJoiner;
import org.openziti.management.ApiClient;

/** ExternalIdClaim */
@JsonPropertyOrder({
    ExternalIdClaim.JSON_PROPERTY_INDEX,
    ExternalIdClaim.JSON_PROPERTY_LOCATION,
    ExternalIdClaim.JSON_PROPERTY_MATCHER,
    ExternalIdClaim.JSON_PROPERTY_MATCHER_CRITERIA,
    ExternalIdClaim.JSON_PROPERTY_PARSER,
    ExternalIdClaim.JSON_PROPERTY_PARSER_CRITERIA
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-03-17T12:51:49.931993799-04:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class ExternalIdClaim {
    public static final String JSON_PROPERTY_INDEX = "index";
    @javax.annotation.Nullable private Integer index;

    /** Gets or Sets location */
    public enum LocationEnum {
        COMMON_NAME(String.valueOf("COMMON_NAME")),

        SAN_URI(String.valueOf("SAN_URI")),

        SAN_EMAIL(String.valueOf("SAN_EMAIL"));

        private String value;

        LocationEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static LocationEnum fromValue(String value) {
            for (LocationEnum b : LocationEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            return null;
        }
    }

    public static final String JSON_PROPERTY_LOCATION = "location";
    @javax.annotation.Nullable private LocationEnum location;

    /** Gets or Sets matcher */
    public enum MatcherEnum {
        ALL(String.valueOf("ALL")),

        PREFIX(String.valueOf("PREFIX")),

        SUFFIX(String.valueOf("SUFFIX")),

        SCHEME(String.valueOf("SCHEME"));

        private String value;

        MatcherEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static MatcherEnum fromValue(String value) {
            for (MatcherEnum b : MatcherEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            return null;
        }
    }

    public static final String JSON_PROPERTY_MATCHER = "matcher";
    @javax.annotation.Nullable private MatcherEnum matcher;

    public static final String JSON_PROPERTY_MATCHER_CRITERIA = "matcherCriteria";
    @javax.annotation.Nullable private String matcherCriteria;

    /** Gets or Sets parser */
    public enum ParserEnum {
        NONE(String.valueOf("NONE")),

        SPLIT(String.valueOf("SPLIT"));

        private String value;

        ParserEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static ParserEnum fromValue(String value) {
            for (ParserEnum b : ParserEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            return null;
        }
    }

    public static final String JSON_PROPERTY_PARSER = "parser";
    @javax.annotation.Nullable private ParserEnum parser;

    public static final String JSON_PROPERTY_PARSER_CRITERIA = "parserCriteria";
    @javax.annotation.Nullable private String parserCriteria;

    public ExternalIdClaim() {}

    public ExternalIdClaim index(@javax.annotation.Nullable Integer index) {
        this.index = index;
        return this;
    }

    /**
     * Get index
     *
     * @return index
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_INDEX)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Integer getIndex() {
        return index;
    }

    @JsonProperty(JSON_PROPERTY_INDEX)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setIndex(@javax.annotation.Nullable Integer index) {
        this.index = index;
    }

    public ExternalIdClaim location(@javax.annotation.Nullable LocationEnum location) {
        this.location = location;
        return this;
    }

    /**
     * Get location
     *
     * @return location
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_LOCATION)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public LocationEnum getLocation() {
        return location;
    }

    @JsonProperty(JSON_PROPERTY_LOCATION)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setLocation(@javax.annotation.Nullable LocationEnum location) {
        this.location = location;
    }

    public ExternalIdClaim matcher(@javax.annotation.Nullable MatcherEnum matcher) {
        this.matcher = matcher;
        return this;
    }

    /**
     * Get matcher
     *
     * @return matcher
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_MATCHER)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public MatcherEnum getMatcher() {
        return matcher;
    }

    @JsonProperty(JSON_PROPERTY_MATCHER)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setMatcher(@javax.annotation.Nullable MatcherEnum matcher) {
        this.matcher = matcher;
    }

    public ExternalIdClaim matcherCriteria(@javax.annotation.Nullable String matcherCriteria) {
        this.matcherCriteria = matcherCriteria;
        return this;
    }

    /**
     * Get matcherCriteria
     *
     * @return matcherCriteria
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_MATCHER_CRITERIA)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getMatcherCriteria() {
        return matcherCriteria;
    }

    @JsonProperty(JSON_PROPERTY_MATCHER_CRITERIA)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setMatcherCriteria(@javax.annotation.Nullable String matcherCriteria) {
        this.matcherCriteria = matcherCriteria;
    }

    public ExternalIdClaim parser(@javax.annotation.Nullable ParserEnum parser) {
        this.parser = parser;
        return this;
    }

    /**
     * Get parser
     *
     * @return parser
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_PARSER)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public ParserEnum getParser() {
        return parser;
    }

    @JsonProperty(JSON_PROPERTY_PARSER)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setParser(@javax.annotation.Nullable ParserEnum parser) {
        this.parser = parser;
    }

    public ExternalIdClaim parserCriteria(@javax.annotation.Nullable String parserCriteria) {
        this.parserCriteria = parserCriteria;
        return this;
    }

    /**
     * Get parserCriteria
     *
     * @return parserCriteria
     */
    @javax.annotation.Nullable
    @JsonProperty(JSON_PROPERTY_PARSER_CRITERIA)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getParserCriteria() {
        return parserCriteria;
    }

    @JsonProperty(JSON_PROPERTY_PARSER_CRITERIA)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setParserCriteria(@javax.annotation.Nullable String parserCriteria) {
        this.parserCriteria = parserCriteria;
    }

    /** Return true if this externalIdClaim object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExternalIdClaim externalIdClaim = (ExternalIdClaim) o;
        return Objects.equals(this.index, externalIdClaim.index)
                && Objects.equals(this.location, externalIdClaim.location)
                && Objects.equals(this.matcher, externalIdClaim.matcher)
                && Objects.equals(this.matcherCriteria, externalIdClaim.matcherCriteria)
                && Objects.equals(this.parser, externalIdClaim.parser)
                && Objects.equals(this.parserCriteria, externalIdClaim.parserCriteria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, location, matcher, matcherCriteria, parser, parserCriteria);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ExternalIdClaim {\n");
        sb.append("    index: ").append(toIndentedString(index)).append("\n");
        sb.append("    location: ").append(toIndentedString(location)).append("\n");
        sb.append("    matcher: ").append(toIndentedString(matcher)).append("\n");
        sb.append("    matcherCriteria: ").append(toIndentedString(matcherCriteria)).append("\n");
        sb.append("    parser: ").append(toIndentedString(parser)).append("\n");
        sb.append("    parserCriteria: ").append(toIndentedString(parserCriteria)).append("\n");
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

        // add `index` to the URL query string
        if (getIndex() != null) {
            joiner.add(
                    String.format(
                            "%sindex%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getIndex()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `location` to the URL query string
        if (getLocation() != null) {
            joiner.add(
                    String.format(
                            "%slocation%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getLocation()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `matcher` to the URL query string
        if (getMatcher() != null) {
            joiner.add(
                    String.format(
                            "%smatcher%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getMatcher()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `matcherCriteria` to the URL query string
        if (getMatcherCriteria() != null) {
            joiner.add(
                    String.format(
                            "%smatcherCriteria%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getMatcherCriteria()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `parser` to the URL query string
        if (getParser() != null) {
            joiner.add(
                    String.format(
                            "%sparser%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getParser()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `parserCriteria` to the URL query string
        if (getParserCriteria() != null) {
            joiner.add(
                    String.format(
                            "%sparserCriteria%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getParserCriteria()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
