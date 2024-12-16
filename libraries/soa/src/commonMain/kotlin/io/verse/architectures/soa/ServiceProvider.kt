package io.verse.architectures.soa

import io.tagd.core.Identifiable
import io.tagd.core.Nameable
import io.tagd.core.Service
import kotlin.reflect.KClass

interface ServiceProvider : Service, Nameable, Identifiable<String> {

    val classifier: ServiceProviderClassifier

    val profiles: Map<KClass<out ServiceProviderProfile>, ServiceProviderProfile>

    val services: Map<KClass<out ServiceProviderService>, ServiceProviderServiceGateway>

    fun putProfile(profile: ServiceProviderProfile)

    fun <T : ServiceProviderProfile> getProfile(profile: KClass<T>): T?

    fun <T : ServiceProviderService> putServiceGateway(
        service: KClass<T>,
        gateway: ServiceProviderServiceGateway
    )

    fun <T : ServiceProviderService> getServiceGateway(service: KClass<T>): T?

}

interface ServiceProviderClassifier : Nameable

interface ServiceProviderPartner : ServiceProvider

open class ServiceProviderFactory<CLASSIFIER : ServiceProvider> : Service {

    private var map: HashMap<String, ServiceProvider> = hashMapOf()

    fun <T : CLASSIFIER> register(identifier: String, provider: T) {
        map[identifier] = provider
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : CLASSIFIER> get(identifier: String): T? {
        return map[identifier] as? T
    }

    fun unregister(identifier: String) {
        map.remove(identifier)
    }

    override fun release() {
        map.clear()
    }

}

open class ServiceProviderContext(
    provider: ServiceProvider,
)
