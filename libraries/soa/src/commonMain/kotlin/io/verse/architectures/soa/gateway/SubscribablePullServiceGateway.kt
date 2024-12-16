package io.verse.architectures.soa.gateway

import io.tagd.arch.data.gateway.AbstractGateway
import io.tagd.langx.Callback
import io.verse.architectures.soa.dispatcher.ServiceDataObjectDispatcher
import io.verse.architectures.soa.io.PullServiceResponse
import io.verse.architectures.soa.io.ServiceDataObject
import io.verse.architectures.soa.service.SubscribablePullService
import io.verse.architectures.soa.service.SubscribablePushService

interface SubscribablePullServiceGateway<
        P : Any,
        R : PullServiceResponse<T>,
        T : ServiceDataObject> :
    SubscribableServiceGateway {

    override val service: SubscribablePullService<P, T>?

    fun <D : ServiceDataObject, RESULT : Any?> put(
        pushService: SubscribablePushService<D, RESULT>
    ): SubscribablePullServiceGateway<P, R, T>

    fun <D : ServiceDataObject, RESULT : Any?, S : SubscribablePushService<D, RESULT>> get(
        pullKey: String
    ): S?

    fun fetch(
        request: P,
        success: Callback<List<T>>? = null,
        failure: Callback<Throwable>? = null
    )
}

abstract class DefaultSubscribablePullServiceGateway<
    P : Any,
    R : PullServiceResponse<T>,
    T : ServiceDataObject
> : AbstractGateway(), SubscribablePullServiceGateway<P, R, T> {

    override var service: SubscribablePullService<P, T>? = null
    protected open val pushServices = hashMapOf<String, SubscribablePushService<*, *>>()

    override fun <D : ServiceDataObject, RESULT : Any?> put(
        pushService: SubscribablePushService<D, RESULT>
    ): DefaultSubscribablePullServiceGateway<P, R, T> {

        pushServices[pushService.pullKey!!] = pushService
        return this
    }

    @Suppress("UNCHECKED_CAST")
    override fun <
        D : ServiceDataObject,
        RESULT : Any?,
        S : SubscribablePushService<D, RESULT>
    > get(pullKey: String): S? {

        return pushServices[pullKey] as? S
    }

    protected open fun dispatch(response: R) {
        if (response.dispatchable) {
            dispatch(response.results!!)
        }
    }

    protected open fun dispatch(results: Map<String, List<T>>) {
        pushServices.keys.forEach { pullKey ->
            results[pullKey]?.let { serviceResults ->
                val dispatcher: ServiceDataObjectDispatcher<T, *>? = getDispatcher(pullKey)
                serviceResults.forEach { result ->
                    dispatcher?.dispatch(result, error = {
                        it.printStackTrace()
                    })
                }
            }
        }
    }

    protected open fun getDispatcher(pullKey: String): ServiceDataObjectDispatcher<T, *>? {
        return null
    }

    override fun release() {
        service = null
        pushServices.clear()
        super.release()
    }
}