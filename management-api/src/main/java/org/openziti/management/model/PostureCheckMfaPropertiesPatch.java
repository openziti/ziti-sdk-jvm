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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openziti.management.ApiClient;

/** PostureCheckMfaPropertiesPatch */
@JsonPropertyOrder({
    PostureCheckMfaPropertiesPatch.JSON_PROPERTY_IGNORE_LEGACY_ENDPOINTS,
    PostureCheckMfaPropertiesPatch.JSON_PROPERTY_PROMPT_ON_UNLOCK,
    PostureCheckMfaPropertiesPatch.JSON_PROPERTY_PROMPT_ON_WAKE,
    PostureCheckMfaPropertiesPatch.JSON_PROPERTY_TIMEOUT_SECONDS
})
@javax.annotation.Generated(
        value = "org.openapitools.codegen.languages.JavaClientCodegen",
        date = "2025-01-30T10:50:07.620098843-05:00[America/New_York]",
        comments = "Generator version: 7.11.0")
public class PostureCheckMfaPropertiesPatch {
    public static final String JSON_PROPERTY_IGNORE_LEGACY_ENDPOINTS = "ignoreLegacyEndpoints";
    private JsonNullable<Boolean> ignoreLegacyEndpoints = JsonNullable.<Boolean>undefined();

    public static final String JSON_PROPERTY_PROMPT_ON_UNLOCK = "promptOnUnlock";
    private JsonNullable<Boolean> promptOnUnlock = JsonNullable.<Boolean>undefined();

    public static final String JSON_PROPERTY_PROMPT_ON_WAKE = "promptOnWake";
    private JsonNullable<Boolean> promptOnWake = JsonNullable.<Boolean>undefined();

    public static final String JSON_PROPERTY_TIMEOUT_SECONDS = "timeoutSeconds";
    private JsonNullable<Integer> timeoutSeconds = JsonNullable.<Integer>undefined();

    public PostureCheckMfaPropertiesPatch() {}

    public PostureCheckMfaPropertiesPatch ignoreLegacyEndpoints(
            @javax.annotation.Nullable Boolean ignoreLegacyEndpoints) {
        this.ignoreLegacyEndpoints = JsonNullable.<Boolean>of(ignoreLegacyEndpoints);
        return this;
    }

    /**
     * Get ignoreLegacyEndpoints
     *
     * @return ignoreLegacyEndpoints
     */
    @javax.annotation.Nullable
    @JsonIgnore
    public Boolean getIgnoreLegacyEndpoints() {
        return ignoreLegacyEndpoints.orElse(null);
    }

    @JsonProperty(JSON_PROPERTY_IGNORE_LEGACY_ENDPOINTS)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public JsonNullable<Boolean> getIgnoreLegacyEndpoints_JsonNullable() {
        return ignoreLegacyEndpoints;
    }

    @JsonProperty(JSON_PROPERTY_IGNORE_LEGACY_ENDPOINTS)
    public void setIgnoreLegacyEndpoints_JsonNullable(JsonNullable<Boolean> ignoreLegacyEndpoints) {
        this.ignoreLegacyEndpoints = ignoreLegacyEndpoints;
    }

    public void setIgnoreLegacyEndpoints(@javax.annotation.Nullable Boolean ignoreLegacyEndpoints) {
        this.ignoreLegacyEndpoints = JsonNullable.<Boolean>of(ignoreLegacyEndpoints);
    }

    public PostureCheckMfaPropertiesPatch promptOnUnlock(
            @javax.annotation.Nullable Boolean promptOnUnlock) {
        this.promptOnUnlock = JsonNullable.<Boolean>of(promptOnUnlock);
        return this;
    }

    /**
     * Get promptOnUnlock
     *
     * @return promptOnUnlock
     */
    @javax.annotation.Nullable
    @JsonIgnore
    public Boolean getPromptOnUnlock() {
        return promptOnUnlock.orElse(null);
    }

    @JsonProperty(JSON_PROPERTY_PROMPT_ON_UNLOCK)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public JsonNullable<Boolean> getPromptOnUnlock_JsonNullable() {
        return promptOnUnlock;
    }

    @JsonProperty(JSON_PROPERTY_PROMPT_ON_UNLOCK)
    public void setPromptOnUnlock_JsonNullable(JsonNullable<Boolean> promptOnUnlock) {
        this.promptOnUnlock = promptOnUnlock;
    }

    public void setPromptOnUnlock(@javax.annotation.Nullable Boolean promptOnUnlock) {
        this.promptOnUnlock = JsonNullable.<Boolean>of(promptOnUnlock);
    }

    public PostureCheckMfaPropertiesPatch promptOnWake(
            @javax.annotation.Nullable Boolean promptOnWake) {
        this.promptOnWake = JsonNullable.<Boolean>of(promptOnWake);
        return this;
    }

    /**
     * Get promptOnWake
     *
     * @return promptOnWake
     */
    @javax.annotation.Nullable
    @JsonIgnore
    public Boolean getPromptOnWake() {
        return promptOnWake.orElse(null);
    }

    @JsonProperty(JSON_PROPERTY_PROMPT_ON_WAKE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public JsonNullable<Boolean> getPromptOnWake_JsonNullable() {
        return promptOnWake;
    }

    @JsonProperty(JSON_PROPERTY_PROMPT_ON_WAKE)
    public void setPromptOnWake_JsonNullable(JsonNullable<Boolean> promptOnWake) {
        this.promptOnWake = promptOnWake;
    }

    public void setPromptOnWake(@javax.annotation.Nullable Boolean promptOnWake) {
        this.promptOnWake = JsonNullable.<Boolean>of(promptOnWake);
    }

    public PostureCheckMfaPropertiesPatch timeoutSeconds(
            @javax.annotation.Nullable Integer timeoutSeconds) {
        this.timeoutSeconds = JsonNullable.<Integer>of(timeoutSeconds);
        return this;
    }

    /**
     * Get timeoutSeconds
     *
     * @return timeoutSeconds
     */
    @javax.annotation.Nullable
    @JsonIgnore
    public Integer getTimeoutSeconds() {
        return timeoutSeconds.orElse(null);
    }

    @JsonProperty(JSON_PROPERTY_TIMEOUT_SECONDS)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public JsonNullable<Integer> getTimeoutSeconds_JsonNullable() {
        return timeoutSeconds;
    }

    @JsonProperty(JSON_PROPERTY_TIMEOUT_SECONDS)
    public void setTimeoutSeconds_JsonNullable(JsonNullable<Integer> timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public void setTimeoutSeconds(@javax.annotation.Nullable Integer timeoutSeconds) {
        this.timeoutSeconds = JsonNullable.<Integer>of(timeoutSeconds);
    }

    /** Return true if this postureCheckMfaPropertiesPatch object is equal to o. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostureCheckMfaPropertiesPatch postureCheckMfaPropertiesPatch =
                (PostureCheckMfaPropertiesPatch) o;
        return equalsNullable(
                        this.ignoreLegacyEndpoints,
                        postureCheckMfaPropertiesPatch.ignoreLegacyEndpoints)
                && equalsNullable(
                        this.promptOnUnlock, postureCheckMfaPropertiesPatch.promptOnUnlock)
                && equalsNullable(this.promptOnWake, postureCheckMfaPropertiesPatch.promptOnWake)
                && equalsNullable(
                        this.timeoutSeconds, postureCheckMfaPropertiesPatch.timeoutSeconds);
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
                hashCodeNullable(ignoreLegacyEndpoints),
                hashCodeNullable(promptOnUnlock),
                hashCodeNullable(promptOnWake),
                hashCodeNullable(timeoutSeconds));
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
        sb.append("class PostureCheckMfaPropertiesPatch {\n");
        sb.append("    ignoreLegacyEndpoints: ")
                .append(toIndentedString(ignoreLegacyEndpoints))
                .append("\n");
        sb.append("    promptOnUnlock: ").append(toIndentedString(promptOnUnlock)).append("\n");
        sb.append("    promptOnWake: ").append(toIndentedString(promptOnWake)).append("\n");
        sb.append("    timeoutSeconds: ").append(toIndentedString(timeoutSeconds)).append("\n");
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

        // add `ignoreLegacyEndpoints` to the URL query string
        if (getIgnoreLegacyEndpoints() != null) {
            joiner.add(
                    String.format(
                            "%signoreLegacyEndpoints%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getIgnoreLegacyEndpoints()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `promptOnUnlock` to the URL query string
        if (getPromptOnUnlock() != null) {
            joiner.add(
                    String.format(
                            "%spromptOnUnlock%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getPromptOnUnlock()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `promptOnWake` to the URL query string
        if (getPromptOnWake() != null) {
            joiner.add(
                    String.format(
                            "%spromptOnWake%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getPromptOnWake()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        // add `timeoutSeconds` to the URL query string
        if (getTimeoutSeconds() != null) {
            joiner.add(
                    String.format(
                            "%stimeoutSeconds%s=%s",
                            prefix,
                            suffix,
                            URLEncoder.encode(
                                            ApiClient.valueToString(getTimeoutSeconds()),
                                            StandardCharsets.UTF_8)
                                    .replaceAll("\\+", "%20")));
        }

        return joiner.toString();
    }
}
