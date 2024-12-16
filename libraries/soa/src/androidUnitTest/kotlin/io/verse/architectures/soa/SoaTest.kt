package io.verse.architectures.soa

import io.tagd.arch.access.library
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import io.verse.architectures.soa.provider.ServiceProvider
import io.verse.architectures.soa.provider.ServiceProviderLibrary
import org.mockito.Mockito.mock
import kotlin.test.Test
import kotlin.test.assertEquals

class SoaTest {

    private lateinit var library: Soa

    private fun buildLibrary(
        name: String? = null,
        consumer: ApplicationServiceConsumer = mock(),
    ): Soa {

        val builder = Soa.Builder()
            .consumer(consumer)
        name?.let {
            builder.name(name)
        }
        return builder.build()
    }

    @Test
    fun `builder should bind library`() {
        library = buildLibrary()

        val bindedLibrary = library<Soa>()
        assertEquals(library, bindedLibrary)
    }

    @Test
    fun `builder should be able to set consumer to library`() {
        val consumer: ApplicationServiceConsumer = mock()

        library = buildLibrary(consumer = consumer)

        assertEquals(consumer, library.consumer)
    }

    @Test
    fun `builder should be able to set name to library`() {
        val name = "random name"

        library = buildLibrary(name = name)

        assertEquals(name, library.name)
    }

    @Test
    fun `library should be able to store and fetch ServiceProviderLibrary correctly`() {
        val serviceProviderLibrary: ServiceProviderLibrary<ServiceProvider> = mock()
        library = buildLibrary()

        library.put(serviceProviderLibrary)

        assertEquals(serviceProviderLibrary, library.get(serviceProviderLibrary::class))
    }

    @Test
    fun `release should clear the consumer`() {
        val consumer: ApplicationServiceConsumer = mock()
        library = buildLibrary(consumer = consumer)

        library.release()

        assertEquals(null, library.consumer)
    }

}