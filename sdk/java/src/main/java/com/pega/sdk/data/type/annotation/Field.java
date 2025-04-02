/*
 *  Copyright (c) 2025 Pegasystems Inc.
 *  All rights reserved.
 *
 *  This  software  has  been  provided pursuant  to  a  License
 *  Agreement  containing  restrictions on  its  use.   The  software
 *  contains  valuable  trade secrets and proprietary information  of
 *  Pegasystems Inc and is protected by  federal   copyright law.  It
 *  may  not be copied,  modified,  translated or distributed in  any
 *  form or medium,  disclosed to third parties or used in any manner
 *  not provided for in  said  License Agreement except with  written
 *  authorization from Pegasystems Inc.
 *
 */
package com.pega.sdk.data.type.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation that can be used to define a non-static method
 * as a "setter" or "getter" for a logical property (depending on its signature),
 * or non-static object field to be used (serialized, deserialized) as a logical property.
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@JsonProperty
@JacksonAnnotationsInside
public @interface Field {
    /**
     * The Application or RuleSet that the Field was created, typically referred
     * to as the namespace.
     */
    String namespace();

    /**
     * Identifier that the {@link Field} uniquely identified by.
     */
    String ID();
}