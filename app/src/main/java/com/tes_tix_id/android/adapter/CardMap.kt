package com.tes_tix_id.android.adapter

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass

@Retention(RetentionPolicy.CLASS)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class CardMap(val value: KClass<*>)