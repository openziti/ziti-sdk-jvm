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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import org.openziti.management.ApiClient;
import org.openziti.management.JSON;

/** PostureCheckFailure */
@JsonPropertyOrder({
    PostureCheckFailure.JSON_PROPERTY_POSTURE_CHECK_ID,
    PostureCheckFailure.JSON_PROPERTY_POSTURE_CHECK_NAME,
    PostureCheckFailure.JSON_PROPERTY_POSTURE_CHECK_TYPE
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-01-30T10:50:07.620098843-05:00[America/New_York]",
        comments = "Generator version: 7.11.0")
@JsonIgnoreProperties(
        value = "postureCheckType", // ignore manually set postureCheckType, it will be
        // automatically generated by Jackson during serialization
        allowSetters = true // allows the postureCheckType to be set during deserialization
        )
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "postureCheckType",
        visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PostureCheckFailureDomain.class, name = "postureCheckFailureDomain"),
    @JsonSubTypes.Type(
            value = PostureCheckFailureMacAddress.class,
            name = "postureCheckFailureMacAddress"),
    @JsonSubTypes.Type(value = PostureCheckFailureMfa.class, name = "postureCheckFailureMfa"),
    @JsonSubTypes.Type(
            value = PostureCheckFailureOperatingSystem.class,
            name = "postureCheckFailureOperatingSystem"),
    @JsonSubTypes.Type(
            value = PostureCheckFailureProcess.class,
            name = "postureCheckFailureProcess"),
    @JsonSubTypes.Type(
            value = PostureCheckFailureProcessMulti.class,
            name = "postureCheckFailureProcessMulti"),
})
public class PostureCheckFailure {
    public static final String JSON_PROPERTY_POSTURE_CHECK_ID = "postureCheckId";
    @javax.annotation.Nonnull private String postureCheckId;

    public static final String JSON_PROPERTY_POSTURE_CHECK_NAME = "postureCheckName";
    @javax.annotation.Nonnull private String postureCheckName;

    public static final String JSON_PROPERTY_POSTURE_CHECK_TYPE = "postureCheckType";
    @javax.annotation.Nonnull private String postureCheckType;

    public PostureCheckFailure() {}

    public PostureCheckFailure postureCheckId(@javax.annotation.Nonnull String postureCheckId) {
        this.postureCheckId = postureCheckId;
        return this;
    }

    /**
     * Get postureCheckId
     *
     * @return postureCheckId
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_POSTURE_CHECK_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getPostureCheckId() {
        return postureCheckId;
    }

    @JsonProperty(JSON_PROPERTY_POSTURE_CHECK_ID)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setPostureCheckId(@javax.annotation.Nonnull String postureCheckId) {
        this.postureCheckId = postureCheckId;
    }

    public PostureCheckFailure postureCheckName(@javax.annotation.Nonnull String postureCheckName) {
        this.postureCheckName = postureCheckName;
        return this;
    }

    /**
     * Get postureCheckName
     *
     * @return postureCheckName
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_POSTURE_CHECK_NAME)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getPostureCheckName() {
        return postureCheckName;
    }

    @JsonProperty(JSON_PROPERTY_POSTURE_CHECK_NAME)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setPostureCheckName(@javax.annotation.Nonnull String postureCheckName) {
        this.postureCheckName = postureCheckName;
    }

    public PostureCheckFailure postureCheckType(@javax.annotation.Nonnull String postureCheckType) {
        this.postureCheckType = postureCheckType;
        return this;
    }

    /**
     * Get postureCheckType
     *
     * @return postureCheckType
     */
    @javax.annotation.Nonnull
    @JsonProperty(JSON_PROPERTY_POSTURE_CHECK_TYPE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getPostureCheckType() {
        return postureCheckType;
    }

    @JsonProperty(JSON_PROPERTY_POSTURE_CHECK_TYPE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setPostureCheckType(@javax.annotation.Nonnull String postureCheckType) {
        this.postureCheckType = postureCheckType;
    }

    /** Return true if this postureCheckFailure object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostureCheckFailure postureCheckFailure = (PostureCheckFailure) o;
        return Objects.equals(this.postureCheckId, postureCheckFailure.postureCheckId)
                && Objects.equals(this.postureCheckName, postureCheckFailure.postureCheckName)
                && Objects.equals(this.postureCheckType, postureCheckFailure.postureCheckType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postureCheckId, postureCheckName, postureCheckType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PostureCheckFailure {\n");
        sb.append("    postureCheckId: ").append(toIndentedString(postureCheckId)).append("\n");
        sb.append("    postureCheckName: ").append(toIndentedString(postureCheckName)).append("\n");
        sb.append("    postureCheckType: ").append(toIndentedString(postureCheckType)).append("\n");
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

        // add `postureCheckId` to the URL query string
        if (getPostureCheckId() != null) {
            joiner.add(
                    String.format(
                            "%spostureCheckId%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getPostureCheckId()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `postureCheckName` to the URL query string
        if (getPostureCheckName() != null) {
            joiner.add(
                    String.format(
                            "%spostureCheckName%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getPostureCheckName()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `postureCheckType` to the URL query string
        if (getPostureCheckType() != null) {
            joiner.add(
                    String.format(
                            "%spostureCheckType%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getPostureCheckType()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }

    static {
        // Initialize and register the discriminator mappings.
        Map<String, Class<?>> mappings = new HashMap<String, Class<?>>();
        mappings.put("postureCheckFailureDomain", PostureCheckFailureDomain.class);
        mappings.put("postureCheckFailureMacAddress", PostureCheckFailureMacAddress.class);
        mappings.put("postureCheckFailureMfa", PostureCheckFailureMfa.class);
        mappings.put(
                "postureCheckFailureOperatingSystem", PostureCheckFailureOperatingSystem.class);
        mappings.put("postureCheckFailureProcess", PostureCheckFailureProcess.class);
        mappings.put("postureCheckFailureProcessMulti", PostureCheckFailureProcessMulti.class);
        mappings.put("postureCheckFailure", PostureCheckFailure.class);
        JSON.registerDiscriminator(PostureCheckFailure.class, "postureCheckType", mappings);
    }
}
