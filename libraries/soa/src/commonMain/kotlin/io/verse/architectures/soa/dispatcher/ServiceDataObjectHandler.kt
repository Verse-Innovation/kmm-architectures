package io.verse.architectures.soa.dispatcher

import io.tagd.arch.scopable.Scopable
import io.tagd.core.Service
import io.tagd.langx.Callback
import io.verse.architectures.soa.io.ServiceDataObject

/**
 * Typically [ServiceDataObjectHandler] is associated to a business [Scopable].
 * Every messaging service needed [Scopable] would be registering it's [ServiceDataObjectHandler] at
 * [ServiceDataObjectHandlerFactory]
 */
interface ServiceDataObjectHandler<T : ServiceDataObject, R : Any?> : Service {

    val handle: String

    fun handle(
        dataObject: T,
        result: Callback<R>? = null,
        error: Callback<Throwable>? = null
    )

    fun canHandle(dataObject: T, result: Callback<Boolean>)

    companion object {
        const val DEFAULT_HANDLE = "default"
    }
}

/**
 * A registry for [ServiceDataObjectHandler]
 */
open class ServiceDataObjectHandlerFactory<T : ServiceDataObject, R : Any?> : Service {

    private val map: HashMap<String, ServiceDataObjectHandler<T, R>> = hashMapOf()

    fun put(handle: String, handler: ServiceDataObjectHandler<T, R>) {
        map[handle] = handler
    }

    fun get(handle: String): ServiceDataObjectHandler<T, R>? {
        return map[handle]
    }

    fun remove(handle: String) {
        map.remove(handle)
    }

    override fun release() {
        map.clear()
    }
}


