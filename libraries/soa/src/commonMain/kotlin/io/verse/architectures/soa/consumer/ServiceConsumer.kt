package io.verse.architectures.soa.consumer

import io.tagd.core.Service
import io.verse.architectures.soa.provider.ApplicationProfile
import io.verse.architectures.soa.provider.DeviceProfile
import io.verse.architectures.soa.provider.ServiceProvider
import io.verse.architectures.soa.provider.ServiceProviderFactoriesFactory
import io.verse.architectures.soa.provider.ServiceProviderFactory
import io.verse.architectures.soa.provider.UserProfile
import io.verse.architectures.soa.service.SubscribableService
import kotlin.reflect.KClass

interface ServiceConsumer : Service {

    val factories: ServiceProviderFactoriesFactory

    /**
     * The one device profile, which must be enough to be associated with all the Service Providers
     */
    val deviceProfile: DeviceProfile

    /**
     * The one app profile, which must be enough to be associated with all the Service Providers
     */
    val applicationProfile: ApplicationProfile

    /**
     * The Service Provider's must take this base user profile and convert it to Service Provider's
     * specific user profile.
     */
    val userProfile: UserProfile

    fun <BASE_SERVICE_PROVIDER : ServiceProvider> putFactory(
        genre: String,
        factory: ServiceProviderFactory<BASE_SERVICE_PROVIDER>
    ) {

        factories.put(genre, factory)
    }

    fun <BASE_SERVICE_PROVIDER : ServiceProvider> factory(
        genre: String
    ): ServiceProviderFactory<BASE_SERVICE_PROVIDER>? {

        return factories.get(genre)
    }

    fun removeFactory(genre: String) {
        factories.remove(genre)
    }

    fun <BASE_SERVICE_PROVIDER : ServiceProvider, T : BASE_SERVICE_PROVIDER> putServiceProvider(
        baseServiceProviderGenre: String,
        serviceProvider: T
    ) {

        factory<BASE_SERVICE_PROVIDER>(baseServiceProviderGenre)?.put(
            serviceProvider::class,
            serviceProvider
        )

        serviceProvider
            .putProfile(deviceProfile)
            .putProfile(applicationProfile)

        serviceProvider.userProfileEnricher?.enrich(serviceProvider, userProfile)?.let {
            serviceProvider.putProfile(it)
        } ?: kotlin.run {
            serviceProvider.putProfile(userProfile)
        }
    }

    fun <BASE_SERVICE_PROVIDER : ServiceProvider, T : BASE_SERVICE_PROVIDER> serviceProvider(
        baseServiceProviderGenre: String,
        identifier: KClass<T>
    ): T? {

        return factory<BASE_SERVICE_PROVIDER>(baseServiceProviderGenre)?.get(identifier)
    }

    fun <BASE_SERVICE_PROVIDER : ServiceProvider, T : BASE_SERVICE_PROVIDER> putService(
        genre: String,
        serviceProvider: KClass<T>,
        service: SubscribableService
    ) {

        factory<BASE_SERVICE_PROVIDER>(genre)?.get(serviceProvider)?.putService(service)
    }

    fun <BASE_SERVICE_PROVIDER : ServiceProvider,
            T : BASE_SERVICE_PROVIDER,
            S : SubscribableService> service(
        genre: String,
        serviceProvider: KClass<T>,
        service: KClass<S>
    ): S? {

        return factory<BASE_SERVICE_PROVIDER>(genre)?.get(serviceProvider)?.service(service)
    }

    override fun release() {
        //no-op
    }

}

open class DefaultServiceConsumer(
    override val factories: ServiceProviderFactoriesFactory,
    override val deviceProfile: DeviceProfile,
    override val applicationProfile: ApplicationProfile,
    override val userProfile: UserProfile
) : ServiceConsumer