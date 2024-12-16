package io.verse.architectures.soa.provider

import io.tagd.core.Copyable
import io.verse.architectures.soa.ContextualEnricher
import io.verse.architectures.soa.Profile

interface ServiceProviderProfile : Copyable {

    val provider: ServiceProvider?
}

open class BaseServiceProviderProfile(override val provider: ServiceProvider) : Profile(),
    ServiceProviderProfile {

    open fun copy(provider: ServiceProvider = this.provider): BaseServiceProviderProfile {
        return BaseServiceProviderProfile(provider = provider)
    }

}

open class DeviceProfile : Profile(), ServiceProviderProfile {

    override var provider: ServiceProvider? = null

    fun copy(provider: ServiceProvider? = this.provider): DeviceProfile {
        return DeviceProfile().also { other ->
            other.provider = provider
        }
    }

}

open class ApplicationProfile : Profile(), ServiceProviderProfile {

    override var provider: ServiceProvider? = null

    fun copy(provider: ServiceProvider? = this.provider): ApplicationProfile {
        return ApplicationProfile().also { other ->
            other.provider = provider
        }
    }

}

open class UserProfile : Profile(), ServiceProviderProfile {

    override var provider: ServiceProvider? = null

    fun copy(provider: ServiceProvider? = this.provider): UserProfile {
        return UserProfile().also { other ->
            other.provider = provider
        }
    }

}

open class SubscriptionProfile(
    serviceProvider: ServiceProvider
) : BaseServiceProviderProfile(provider = serviceProvider) {

    override fun copy(provider: ServiceProvider): SubscriptionProfile {
        return SubscriptionProfile(serviceProvider = provider)
    }

}

interface UserProfileEnricher : ContextualEnricher<ServiceProvider, UserProfile>