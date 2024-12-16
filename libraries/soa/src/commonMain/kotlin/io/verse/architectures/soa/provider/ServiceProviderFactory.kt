package io.verse.architectures.soa.provider

import io.tagd.core.Service
import kotlin.reflect.KClass

/**
 * The factory of certain service offering service providers.
 * Ex - Firebase, Facebook and Amazon, all 3 offers push messaging service. Hence they must be
 * registered here.
 */
open class ServiceProviderFactory<GENRE_BASE_TYPE : ServiceProvider>(val genre: String) : Service {

    private var map: HashMap<KClass<out GENRE_BASE_TYPE>, ServiceProvider> = hashMapOf()

    fun <T : GENRE_BASE_TYPE> put(identifier: KClass<out GENRE_BASE_TYPE>, provider: T) {
        map[identifier] = provider
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : GENRE_BASE_TYPE> get(identifier: KClass<T>): T? {
        return map[identifier] as? T
    }

    fun <T : GENRE_BASE_TYPE> remove(identifier: KClass<T>) {
        map.remove(identifier)
    }

    override fun release() {
        map.clear()
    }
}

