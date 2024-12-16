package io.verse.architectures.soa.service

import io.tagd.arch.data.gateway.Gateway
import io.tagd.core.Nameable
import io.tagd.core.Service
import io.verse.architectures.soa.provider.ServiceProvider
import io.verse.architectures.soa.gateway.SubscribableServiceGateway
import io.verse.architectures.soa.provider.SubscriptionProfile

/**
 * Typically represents a [ServiceProvider]'s Service. The examples are
 * Firebase's FCM, Amazon's SNS etc
 *
 * The clients must define the offered [SubscribableService]
 * and must place it at [ServiceProvider] using [ServiceProvider.putService]
 *
 * Typical usage would be, considering a push scenario,
 * gateway -> dispatcher -> [handler1, handler2, ... handlerN] -> handle
 */
interface SubscribableService : Service, Nameable {

    /**
     * The service's owner
     */
    val provider: ServiceProvider

    /**
     * The service's subscription specific profile
     */
    val subscriptionProfile: SubscriptionProfile?

    /**
     * The service's delegate to perform the actual IO
     */
    val gateway: SubscribableServiceGateway?

    @Suppress("UNCHECKED_CAST")
    fun <T : ServiceProvider> provider(): T {
        return provider as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Gateway> gateway(): T? {
        return gateway as? T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : SubscriptionProfile> profile(): T? {
        return subscriptionProfile as? T
    }
}

