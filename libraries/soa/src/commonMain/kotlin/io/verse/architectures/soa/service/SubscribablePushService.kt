package io.verse.architectures.soa.service

import io.verse.architectures.soa.io.ServiceDataObject
import io.verse.architectures.soa.dispatcher.ServiceDataObjectDispatcher
import io.verse.architectures.soa.provider.ServiceProvider
import io.verse.architectures.soa.gateway.SubscribablePushServiceGateway
import io.verse.architectures.soa.provider.SubscriptionProfile

interface SubscribablePushService<T : ServiceDataObject, R : Any?> : SubscribableService {

    val dispatcher: ServiceDataObjectDispatcher<T, R>?

    override val gateway: SubscribablePushServiceGateway<T, R>?

    /**
     * Typically used as a key to identify the pulled [ServiceDataObject]'s key
     * {
     *  ...
     *  "$pullKey" : [
     *      {}, {},... {}
     *  ]
     *  ...
     * }
     */
    val pullKey: String?
}

open class DefaultSubscribablePushService<T : ServiceDataObject, R : Any?>(
    override val name: String,
    override val provider: ServiceProvider,
    override val dispatcher: ServiceDataObjectDispatcher<T, R>?,
    override val subscriptionProfile: SubscriptionProfile? = null,
) : SubscribablePushService<T, R> {

    override var gateway: SubscribablePushServiceGateway<T, R>? = null

    override val pullKey: String? = null

    override fun release() {
        gateway = null
    }
}