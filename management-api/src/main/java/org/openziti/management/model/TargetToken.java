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
import com.fasterxml.jackson.annotation.JsonValue;

/** Defines the target token type */
public enum TargetToken {
    ACCESS("ACCESS"),

    ID("ID");

    private String value;

    TargetToken(String value) {
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
    public static TargetToken fromValue(String value) {
        for (TargetToken b : TargetToken.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }

    /**
     * Convert the instance into URL query string.
     *
     * @param prefix prefix of the query string
     * @return URL query string
     */
    public String toUrlQueryString(String prefix) {
        if (prefix == null) {
            prefix = "";
        }

        return String.format("%s=%s", prefix, this.toString());
    }
}
