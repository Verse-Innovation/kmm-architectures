package io.verse.architectures.soa.io

import io.tagd.arch.datatype.DataObject

/**
 * A [io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler]'s handle associated Response.
 */
open class ServiceDataObject : DataObject() {

    open lateinit var handle: String

    open fun handleAvailable() = ::handle.isInitialized

    companion object {
        const val HANDLE_KEY = "handle"
    }

}

open class ServiceRequest : ServiceDataObject()


open class ServiceResponse : ServiceDataObject()

open class PullServiceRequest(open val url: String) : ServiceRequest()

open class PullServiceResponse<T> : ServiceResponse() {

    var dispatchable: Boolean = false

    var results: Map<String, List<T>>? = null

    var result: T? = null
}