package io.verse.architectures.soa

import io.tagd.core.ValidateException
import io.verse.architectures.soa.provider.DefaultServiceProvider
import io.verse.architectures.soa.provider.UserProfileEnricher
import io.verse.architectures.soa.test.FakeServiceProviderProfile1
import io.verse.architectures.soa.test.FakeServiceProviderProfile2
import io.verse.architectures.soa.test.FakeSubscribableService1
import io.verse.architectures.soa.test.FakeSubscribableService2
import org.junit.Test
import org.mockito.Mockito.mock
import kotlin.test.assertEquals

class DefaultServiceProviderTest {

    private val genre = "service_provider_genre"
    private val userProfileEnricher = mock<UserProfileEnricher>()
    private val serviceProvider = DefaultServiceProvider(
        genre = genre,
        userProfileEnricher = userProfileEnricher,
    )

    @Test
    fun `putProfile should store and fetch the profile correctly`() {
        val profile1 = FakeServiceProviderProfile1(mock())
        val profile2 = FakeServiceProviderProfile2(mock())

        serviceProvider.putProfile(profile1)
        serviceProvider.putProfile(profile2)

        assertEquals(2, serviceProvider.profiles.size)
        assertEquals(profile1, serviceProvider.profile(FakeServiceProviderProfile1::class))
        assertEquals(profile2, serviceProvider.profile(FakeServiceProviderProfile2::class))
    }

    @Test
    fun `putService should store and fetch the service correctly`() {
        val service1 = FakeSubscribableService1(
            name = "service1",
            provider = mock(),
            subscriptionProfile = null,
            gateway = null
        )
        val service2 = FakeSubscribableService2(
            name = "service2",
            provider = mock(),
            subscriptionProfile = null,
            gateway = null
        )

        serviceProvider.putService(service1)
        serviceProvider.putService(service2)

        assertEquals(2, serviceProvider.services.size)
        assertEquals(service1, serviceProvider.service(FakeSubscribableService1::class))
        assertEquals(service2, serviceProvider.service(FakeSubscribableService2::class))
    }

    @Test(expected = ValidateException::class)
    fun `validate should throw error if consumer not initialized`() {
        serviceProvider.validate()
    }

}
