package io.verse.architectures.soa.gateway

import io.tagd.arch.data.gateway.AbstractGateway
import io.tagd.langx.Callback
import io.verse.architectures.soa.io.ServiceDataObject
import io.verse.architectures.soa.dispatcher.ServiceDataObjectDispatcher
import io.verse.architectures.soa.service.SubscribablePushService

interface SubscribablePushServiceGateway<T : ServiceDataObject, R : Any?> :
    SubscribableServiceGateway {

    override val service: SubscribablePushService<T, R>?

    val dispatcher: ServiceDataObjectDispatcher<T, R>?

    /**
     * After enriching and handling the boundary checks for the given [ServiceDataObject],
     * dispatcher?.dispatch(response) would be called
     */
    fun onReceive(dataObject: T)

    fun dispatch(
        dataObject: T,
        result: Callback<R?>? = null,
        error: Callback<Throwable>? = null
    ) {

        dispatcher?.dispatch(dataObject, result, error)
    }

}

open class DefaultSubscribablePushServiceGateway<T : ServiceDataObject, R : Any?>(
    override var dispatcher: ServiceDataObjectDispatcher<T, R>?
) : AbstractGateway(), SubscribablePushServiceGateway<T, R> {

    override var service: SubscribablePushService<T, R>? = null

    override fun onReceive(dataObject: T) {
        dispatcher?.dispatch(dataObject)
    }
}