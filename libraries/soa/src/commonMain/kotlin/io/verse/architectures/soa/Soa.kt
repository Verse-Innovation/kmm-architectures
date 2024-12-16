package io.verse.architectures.soa

import io.tagd.arch.scopable.library.AbstractLibrary
import io.tagd.arch.scopable.library.Library
import io.tagd.di.Scope
import io.tagd.di.bind
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import io.verse.architectures.soa.provider.ServiceProvider
import io.verse.architectures.soa.provider.ServiceProviderLibrary
import kotlin.reflect.KClass

/**
 * SubscribableService Oriented Architecture
 */
open class Soa private constructor(name: String, outerScope: Scope) :
    AbstractLibrary(name, outerScope) {

    var consumer: ApplicationServiceConsumer? = null
        private set

    private val serviceProviderLibraries =
        hashMapOf<KClass<out ServiceProviderLibrary<*>>, ServiceProviderLibrary<*>>()

    fun <T : ServiceProvider> put(library: ServiceProviderLibrary<T>) {
        serviceProviderLibraries[library::class] = library
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : ServiceProvider, L : ServiceProviderLibrary<out T>> get(
        kClass: KClass<out ServiceProviderLibrary<T>>,
    ): L? {

        return serviceProviderLibraries[kClass] as L?
    }

    override fun release() {
        serviceProviderLibraries.clear()
        consumer = null
        super.release()
    }

    open class Builder : Library.Builder<Soa>() {

        @Suppress("MemberVisibilityCanBePrivate")
        protected lateinit var consumer: ApplicationServiceConsumer

        override fun name(name: String?): Builder {
            super.name(name)
            return this
        }

        override fun scope(outer: Scope?): Builder {
            super.scope(outer)
            return this
        }

        fun consumer(consumer: ApplicationServiceConsumer): Builder {
            this.consumer = consumer
            return this
        }

        override fun buildLibrary(): Soa {
            return Soa(name ?: "${outerScope.name}/$NAME", outerScope).also {
                it.consumer = consumer
                outerScope.bind<Library, Soa>(instance = it)
            }
        }

        companion object {
            const val NAME = "soa"
        }
    }
}