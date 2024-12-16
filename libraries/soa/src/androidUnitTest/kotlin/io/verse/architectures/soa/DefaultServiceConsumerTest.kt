package io.verse.architectures.soa

import io.verse.architectures.soa.consumer.DefaultServiceConsumer
import io.verse.architectures.soa.provider.ApplicationProfile
import io.verse.architectures.soa.provider.DefaultServiceProvider
import io.verse.architectures.soa.provider.DeviceProfile
import io.verse.architectures.soa.provider.ServiceProvider
import io.verse.architectures.soa.provider.ServiceProviderFactoriesFactory
import io.verse.architectures.soa.provider.ServiceProviderFactory
import io.verse.architectures.soa.provider.UserProfile
import io.verse.architectures.soa.provider.UserProfileEnricher
import io.verse.architectures.soa.service.SubscribableService
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class DefaultServiceConsumerTest {

    private val factories = mock<ServiceProviderFactoriesFactory>()
    private val deviceProfile = mock<DeviceProfile>()
    private val applicationProfile = mock<ApplicationProfile>()
    private val userProfile = mock<UserProfile>()
    private val consumer = defaultServiceConsumer()

    private fun defaultServiceConsumer(): DefaultServiceConsumer {
        return DefaultServiceConsumer(
            factories = factories,
            deviceProfile = deviceProfile,
            applicationProfile = applicationProfile,
            userProfile = userProfile,
        )
    }

    @Test
    fun `putFactory should store the factory in ServiceProviderFactoriesFactory`() {
        val genre = "factory_genre"
        val factory = mock<ServiceProviderFactory<DefaultServiceProvider>>()

        consumer.putFactory(genre = genre, factory = factory)

        verify(factories).put(genre, factory)
    }

    @Test
    fun `factory should fetch the factory from ServiceProviderFactoriesFactory`() {
        val genre = "factory_genre"

        consumer.factory<DefaultServiceProvider>(genre)

        verify(factories).get<DefaultServiceProvider>(genre)
    }

    @Test
    fun `removeFactory should remove the factory from ServiceProviderFactoriesFactory`() {
        val genre = "factory_genre"

        consumer.removeFactory(genre)

        verify(factories).remove(genre)
    }

    @Test
    fun `putServiceProvider should put the serviceProvider in the correct factory`() {
        val factoryGenre = "factory_genre"
        val serviceProviderFactory = ServiceProviderFactory<DefaultServiceProvider>(factoryGenre)
        val serviceProviderGenre = "service_provider_genre"
        val serviceProvider = DefaultServiceProvider(genre = serviceProviderGenre)
        serviceProviderFactory.put(serviceProvider::class, serviceProvider)

        consumer.putServiceProvider<ServiceProvider, DefaultServiceProvider>(
            baseServiceProviderGenre = factoryGenre,
            serviceProvider = serviceProvider
        )

        assertEquals(serviceProvider, serviceProviderFactory.get(serviceProvider::class))
    }

    @Test
    fun `putServiceProvider should put deviceProfile and applicationProfile`() {
        val factoryGenre = "factory_genre"
        val serviceProviderGenre = "service_provider_genre"
        val serviceProvider = DefaultServiceProvider(genre = serviceProviderGenre)

        consumer.putServiceProvider<ServiceProvider, DefaultServiceProvider>(
            baseServiceProviderGenre = factoryGenre,
            serviceProvider = serviceProvider
        )

        assertEquals(deviceProfile, serviceProvider.profile(deviceProfile::class))
        assertEquals(applicationProfile, serviceProvider.profile(applicationProfile::class))
    }

    @Test
    fun `putServiceProvider should put enriched userProfile`() {
        val factoryGenre = "factory_genre"
        val serviceProviderGenre = "service_provider_genre"
        val userProfileEnricher = mock<UserProfileEnricher>()
        val serviceProvider = DefaultServiceProvider(
            genre = serviceProviderGenre,
            userProfileEnricher = userProfileEnricher,
        )
        val enrichedUserProfile = mock<UserProfile>()
        whenever(userProfileEnricher.enrich(serviceProvider, userProfile))
            .thenReturn(enrichedUserProfile)

        consumer.putServiceProvider<ServiceProvider, DefaultServiceProvider>(
            baseServiceProviderGenre = factoryGenre,
            serviceProvider = serviceProvider
        )

        assertEquals(enrichedUserProfile, serviceProvider.profile(UserProfile::class))
    }

    @Test
    fun `putServiceProvider should put userProfile if enricher is absent`() {
        val factoryGenre = "factory_genre"
        val serviceProviderGenre = "service_provider_genre"
        val serviceProvider = DefaultServiceProvider(
            genre = serviceProviderGenre,
            userProfileEnricher = null,
        )

        consumer.putServiceProvider<ServiceProvider, DefaultServiceProvider>(
            baseServiceProviderGenre = factoryGenre,
            serviceProvider = serviceProvider
        )

        assertEquals(userProfile, serviceProvider.profile(UserProfile::class))
    }

    @Test
    fun `serviceProvider should return the correct serviceProvider`() {
        val factoryGenre = "factory_genre"
        val serviceProviderFactory = mock<ServiceProviderFactory<DefaultServiceProvider>>()
        val serviceProviderGenre = "service_provider_genre"
        val serviceProvider = DefaultServiceProvider(
            genre = serviceProviderGenre,
            userProfileEnricher = null,
        )
        whenever(factories.get<DefaultServiceProvider>(factoryGenre))
            .thenReturn(serviceProviderFactory)
        whenever(serviceProviderFactory.get(serviceProvider::class))
            .thenReturn(serviceProvider)

        val actualProvider = consumer.serviceProvider(
            baseServiceProviderGenre = factoryGenre,
            identifier = DefaultServiceProvider::class,
        )

        assertEquals(serviceProvider, actualProvider)
    }

    @Test
    fun `putService should store the ServiceProviderService in the correct ServiceProvider`() {
        val factoryGenre = "factory_genre"
        val serviceProviderFactory = mock<ServiceProviderFactory<DefaultServiceProvider>>()
        val serviceProvider = mock<DefaultServiceProvider>()
        val service = mock<SubscribableService>()
        whenever(factories.get<DefaultServiceProvider>(factoryGenre))
            .thenReturn(serviceProviderFactory)
        whenever(serviceProviderFactory.get(serviceProvider::class))
            .thenReturn(serviceProvider)

        consumer.putService(
            genre = factoryGenre,
            serviceProvider = DefaultServiceProvider::class,
            service = service,
        )

        verify(serviceProvider).putService(service)
    }

    @Test
    fun `service should fetch correct ServiceProviderService from the correct ServiceProvider`() {
        val factoryGenre = "factory_genre"
        val serviceProviderFactory = mock<ServiceProviderFactory<DefaultServiceProvider>>()
        val serviceProvider = mock<DefaultServiceProvider>()
        val service = mock<SubscribableService>()
        whenever(factories.get<DefaultServiceProvider>(factoryGenre))
            .thenReturn(serviceProviderFactory)
        whenever(serviceProviderFactory.get(serviceProvider::class))
            .thenReturn(serviceProvider)

        consumer.service(
            genre = factoryGenre,
            serviceProvider = DefaultServiceProvider::class,
            service = service::class,
        )

        verify(serviceProvider).service(service::class)
    }

}