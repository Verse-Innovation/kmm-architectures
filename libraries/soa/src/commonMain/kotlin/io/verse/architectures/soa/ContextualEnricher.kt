package io.verse.architectures.soa

/**
 * An enricher is something which transforms A to A'
 * C is a Context, which is sufficient to build the transformation flow
 */
interface ContextualEnricher<C, A> {

    fun enrich(context: C, `object`: A): A
}