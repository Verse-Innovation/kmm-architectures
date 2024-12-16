package io.verse.architectures.soa

import io.tagd.arch.access.ReferencePool
import io.tagd.arch.scopable.AbstractWithinScopableInitializer
import io.tagd.arch.scopable.Scopable
import io.tagd.arch.scopable.library.Library
import io.tagd.core.Dependencies
import io.tagd.di.Scope
import io.tagd.di.bindLazy
import io.tagd.langx.Callback
import io.verse.architectures.soa.consumer.ApplicationServiceConsumer
import io.verse.architectures.soa.provider.ApplicationProfile
import io.verse.architectures.soa.provider.DeviceProfile
import io.verse.architectures.soa.provider.ServiceProviderFactoriesFactory
import io.verse.architectures.soa.provider.UserProfile

@Suppress("unused")
open class SoaInitializer<S : Scopable>(within: S) :
    AbstractWithinScopableInitializer<S, Soa>(within) {

    override fun initialize(callback: Callback<Unit>) {
        outerScope.bindLazy<Library, Soa> {
            new(newDependencies())
        }
        super.initialize(callback)
    }

    override fun new(dependencies: Dependencies): Soa {
        val outerScope = dependencies.get<Scope>(ARG_OUTER_SCOPE)!!
        val consumer = newApplicationServiceConsumer().also { consumer ->
            ReferencePool.put(consumer)
        }
        return Soa.Builder().scope(outerScope).consumer(consumer).build()
    }

    protected open fun newApplicationServiceConsumer(): ApplicationServiceConsumer {
        return ApplicationServiceConsumer(
            factories = ServiceProviderFactoriesFactory(),
            deviceProfile = DeviceProfile(),
            applicationProfile = ApplicationProfile(),
            userProfile = UserProfile(),
        )
    }
}