package io.verse.architectures.soa.service

import io.tagd.langx.Callback
import io.verse.architectures.soa.gateway.SubscribablePullServiceGateway
import io.verse.architectures.soa.io.PullServiceResponse
import io.verse.architectures.soa.io.ServiceDataObject
import io.verse.architectures.soa.provider.ServiceProvider
import io.verse.architectures.soa.provider.SubscriptionProfile

interface SubscribablePullService<P : Any, T : ServiceDataObject> :
    SubscribableService {

    override val gateway: SubscribablePullServiceGateway<P, PullServiceResponse<T>, T>?
}

open class DefaultSubscribablePullService<P : Any, T : ServiceDataObject>(
    override val name: String,
    override val provider: ServiceProvider,
    override val subscriptionProfile: SubscriptionProfile? = null,
) : SubscribablePullService<P, T> {

    override var gateway: SubscribablePullServiceGateway<P, PullServiceResponse<T>, T>? = null

    fun fetch(
        request: P,
        success: Callback<List<T>>? = null,
        failure: Callback<Throwable>? = null
    ) {

        gateway?.fetch(request, success, failure)
    }

    override fun release() {
        gateway = null
    }

}