package io.verse.architectures.soa.provider

import io.tagd.arch.scopable.library.AbstractLibrary
import io.tagd.arch.scopable.library.Library
import io.tagd.di.Scope
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import kotlin.reflect.KClass

open class ServiceProviderLibrary<GENRE_SUPER_TYPE : ServiceProvider> protected constructor(
    name: String, outerScope: Scope,
) : AbstractLibrary(name, outerScope) {

    var consumer: ApplicationServiceConsumer? = null
        protected set

    @Suppress("MemberVisibilityCanBePrivate")
    var factory: ServiceProviderFactory<GENRE_SUPER_TYPE>? = null
        protected set

    open fun <S : GENRE_SUPER_TYPE> put(
        serviceProvider: S
    ): ServiceProviderLibrary<GENRE_SUPER_TYPE> {

        factory?.put(serviceProvider::class, serviceProvider)
        return this
    }

    open fun <S : GENRE_SUPER_TYPE> get(identifier: KClass<S>): S? {
        return factory?.get(identifier)
    }

    @Suppress("unused")
    open fun <S : GENRE_SUPER_TYPE> remove(
        identifier: KClass<S>
    ): ServiceProviderLibrary<GENRE_SUPER_TYPE> {

        factory?.remove(identifier)
        return this
    }

    override fun release() {
        factory?.genre?.let {
            consumer?.removeFactory(it)
        }
        factory?.release()
        factory = null
        consumer = null
        super.release()
    }

    @Suppress("unused")
    abstract class Builder<
            GENRE_SUPER_TYPE : ServiceProvider,
            T : ServiceProviderLibrary<GENRE_SUPER_TYPE>
    > : Library.Builder<T>() {

        @Suppress("MemberVisibilityCanBePrivate")
        protected lateinit var consumer: ApplicationServiceConsumer
            private set

        /**
         * The derived classes must assign builder.factory to library.factory. That step is
         * not possible here due to
         * compiler error - Setter for 'factory' is removed by type projection
         */
        @Suppress("MemberVisibilityCanBePrivate")
        protected lateinit var factory: ServiceProviderFactory<GENRE_SUPER_TYPE>

        override fun name(name: String?): Builder<GENRE_SUPER_TYPE, T> {
            super.name(name)
            return this
        }

        override fun scope(outer: Scope?): Builder<GENRE_SUPER_TYPE, T> {
            super.scope(outer)
            return this
        }

        override fun inject(bindings: Scope.(T) -> Unit): Builder<GENRE_SUPER_TYPE, T> {
            super.inject(bindings)
            return this
        }

        @Suppress("unused")
        open fun consumer(
            consumer: ApplicationServiceConsumer,
            genre: String
        ): Builder<GENRE_SUPER_TYPE, T> {

            this.consumer = consumer
            newAndPutFactory(genre)
            return this
        }

        private fun newAndPutFactory(genre: String) {
            factory = ServiceProviderFactory(genre)
            consumer.putFactory(genre, factory)
        }

        override fun build(): T {
            return super.build().also {
                it.consumer = consumer
                /*
                 * The derived classes must assign builder.factory to library.factory. That step is
                 * not possible here due to
                 * compiler error - Setter for 'factory' is removed by type projection
                 */
            }
        }

        abstract override fun buildLibrary(): T
    }
}