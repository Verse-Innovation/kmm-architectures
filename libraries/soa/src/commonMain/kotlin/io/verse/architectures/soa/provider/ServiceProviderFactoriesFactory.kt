package io.verse.architectures.soa.provider

import io.tagd.core.Releasable

/**
 * All sorts of service provider factories are registered here. Some example are
 * push messaging -> push messaging provider factory (facebook, firebase, amazon, xiaomi, clevertap)
 * pull messaging -> bfe (Backend for front end),
 * deep linking -> (firebase, apsflyer, bfe, app-it-self)
 *
 *
 * [deep linking] -> [firebase, apsflyer, bfe, on-device],
 * [push] -> [firebase, amazon, facebook, xiaomi, huawei, clevertap],
 * [pull] -> [bfe],
 */
open class ServiceProviderFactoriesFactory : Releasable {

    private var map = hashMapOf<String, ServiceProviderFactory<out ServiceProvider>>()

    fun <BASE_SERVICE_PROVIDER : ServiceProvider> put(
        genre: String,
        factory: ServiceProviderFactory<BASE_SERVICE_PROVIDER>,
    ) {
        map[genre] = factory
    }

    @Suppress("UNCHECKED_CAST")
    fun <BASE_SERVICE_PROVIDER : ServiceProvider> get(
        genre: String,
    ): ServiceProviderFactory<BASE_SERVICE_PROVIDER>? {

        return map[genre] as? ServiceProviderFactory<BASE_SERVICE_PROVIDER>
    }

    fun remove(genre: String) {
        map.remove(genre)
    }

    override fun release() {
        map.clear()
    }
}