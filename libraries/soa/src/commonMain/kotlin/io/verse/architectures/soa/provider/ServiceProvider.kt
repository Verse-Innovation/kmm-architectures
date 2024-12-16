package io.verse.architectures.soa.provider

import io.tagd.core.Nameable
import io.tagd.core.Service
import io.tagd.core.Validatable
import io.tagd.core.ValidateException
import io.verse.architectures.soa.consumer.ServiceConsumer
import io.verse.architectures.soa.service.SubscribableService
import kotlin.reflect.KClass

/**
 * ServiceProviders are Facebook, Firebase, Amazon
 */
interface ServiceProvider : Service, Nameable, Validatable {

    /**
     * The nature of the Service ex - deeplink, push messaging, pull messaging
     */
    val genre: String

    val consumer: ServiceConsumer

    val services: Map<KClass<out SubscribableService>, SubscribableService>

    /**
     * Contains the provider level profiles - [UserProfile], [ApplicationProfile], [DeviceProfile]
     */
    val profiles: Map<KClass<out ServiceProviderProfile>, ServiceProviderProfile>

    val userProfileEnricher: UserProfileEnricher?

    fun <S : SubscribableService> putService(service: S): ServiceProvider

    fun <S : SubscribableService> service(service: KClass<S>): S?

    fun putProfile(profile: ServiceProviderProfile): ServiceProvider

    fun <T : ServiceProviderProfile> profile(profile: KClass<T>): T?
}

/**
 * ServiceProviderPartner is typically an aggregator platform like CleverTap, Apsflyer etc
 */
@Suppress("unused")
interface ServiceProviderPartner : ServiceProvider

open class DefaultServiceProvider(
    final override val genre: String,
    override val userProfileEnricher: UserProfileEnricher? = null,
) : ServiceProvider {

    override lateinit var consumer: ServiceConsumer

    override val name: String = "sp/${genre}"

    override val profiles: Map<KClass<out ServiceProviderProfile>, ServiceProviderProfile>
        get() = _profiles

    private val _profiles = hashMapOf<KClass<out ServiceProviderProfile>, ServiceProviderProfile>()

    override val services: Map<KClass<out SubscribableService>, SubscribableService>
        get() = _services

    private val _services =
        hashMapOf<KClass<out SubscribableService>, SubscribableService>()

    override fun putProfile(profile: ServiceProviderProfile): DefaultServiceProvider {
        _profiles[profile::class] = profile
        return this
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ServiceProviderProfile> profile(profile: KClass<T>): T? {
        return _profiles[profile] as? T
    }

    override fun <S : SubscribableService> putService(service: S): DefaultServiceProvider {
        _services[service::class] = service
        return this
    }

    @Suppress("UNCHECKED_CAST")
    override fun <S : SubscribableService> service(service: KClass<S>): S? {
        return _services[service] as? S
    }

    override fun validate() {
        if (!::consumer.isInitialized) {
            throw ValidateException(this, "consumer must be initialized")
        }
    }

    override fun release() {
        _profiles.clear()
        _services.clear()
    }
}