package io.verse.architectures.soa.dispatcher

import io.tagd.core.Service
import io.tagd.langx.Callback
import io.tagd.langx.IllegalAccessException
import io.tagd.langx.assert
import io.verse.architectures.soa.io.ServiceDataObject

/**
 * Dispatches the [ServiceDataObject] to the registered [ServiceDataObjectHandler]
 */
interface ServiceDataObjectDispatcher<T : ServiceDataObject, R : Any?> : Service {

    val factory: ServiceDataObjectHandlerFactory<T, R>?

    fun dispatch(
        dataObject: T,
        result: Callback<R>? = null,
        error: Callback<Throwable>? = null
    )

    fun canDispatch(dataObject: T, result: Callback<Boolean>)
}

open class DefaultServiceDataObjectDispatcher<T : ServiceDataObject, R : Any?>(
    override val factory: ServiceDataObjectHandlerFactory<T, R>?
) : ServiceDataObjectDispatcher<T, R> {

    override fun dispatch(
        dataObject: T,
        result: Callback<R>?,
        error: Callback<Throwable>?
    ) {

        assert(dataObject.handleAvailable())

        handler(dataObject.handle)?.handle(dataObject, result, error) ?: run {
            defaultHandler()?.handle(dataObject, result, error)
        } ?: run {
            throw IllegalAccessException("there is no suitable handler with the name " +
                    "${dataObject.handle} for $dataObject")
        }
    }

    override fun canDispatch(dataObject: T, result: Callback<Boolean>) {
        handler(dataObject.handle)?.canHandle(dataObject, result)
    }

    protected open fun defaultHandler(): ServiceDataObjectHandler<T, R>? {
        return handler(ServiceDataObjectHandler.DEFAULT_HANDLE)
    }

    protected open fun handler(handle: String): ServiceDataObjectHandler<T, R>? {
        return factory?.get(handle)
    }

    override fun release() {
        // no-op
    }
}