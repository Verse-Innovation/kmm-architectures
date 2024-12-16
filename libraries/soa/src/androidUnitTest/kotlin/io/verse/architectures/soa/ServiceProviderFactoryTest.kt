package io.verse.architectures.soa

import io.verse.architectures.soa.provider.ServiceProviderFactory
import io.verse.architectures.soa.test.FakeServiceProvider
import io.verse.architectures.soa.test.FakeServiceProvider1
import io.verse.architectures.soa.test.FakeServiceProvider2
import org.junit.Test
import kotlin.test.assertEquals

class ServiceProviderFactoryTest {

    private val factory = ServiceProviderFactory<FakeServiceProvider>(
        genre = "fake_service_provider_factory"
    )

    @Test
    fun `factory should store and fetch ServiceProvider correctly`() {
        val serviceProvider1 = FakeServiceProvider1()
        val serviceProvider2 = FakeServiceProvider2()

        factory.put(
            identifier = FakeServiceProvider1::class,
            provider = serviceProvider1,
        )
        factory.put(
            identifier = FakeServiceProvider2::class,
            provider = serviceProvider2,
        )

        val fetchedServiceProvider1 = factory.get(FakeServiceProvider1::class)
        val fetchedServiceProvider2 = factory.get(FakeServiceProvider2::class)
        assertEquals(serviceProvider1, fetchedServiceProvider1)
        assertEquals(serviceProvider2, fetchedServiceProvider2)
    }

}