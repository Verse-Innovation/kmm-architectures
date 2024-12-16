package io.verse.architectures.soa.test

import io.verse.architectures.soa.provider.ServiceProvider
import io.verse.architectures.soa.service.SubscribableService
import io.verse.architectures.soa.gateway.SubscribableServiceGateway
import io.verse.architectures.soa.provider.SubscriptionProfile

class FakeSubscribableService1(
    override val name: String,
    override val provider: ServiceProvider,
    override val subscriptionProfile: SubscriptionProfile?,
    override val gateway: SubscribableServiceGateway?,
) : SubscribableService {

    override fun release() {
        // no-op
    }

}

class FakeSubscribableService2(
    override val name: String,
    override val provider: ServiceProvider,
    override val subscriptionProfile: SubscriptionProfile?,
    override val gateway: SubscribableServiceGateway?,
) : SubscribableService {

    override fun release() {
        // no-op
    }

}