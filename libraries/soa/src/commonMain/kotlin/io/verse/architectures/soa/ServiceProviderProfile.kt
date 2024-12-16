package io.verse.architectures.soa

import io.tagd.arch.datatype.DataObject

open class ServiceProviderProfile(val provider: ServiceProvider) : DataObject()

open class DeviceProfile(serviceProvider: ServiceProvider) :
    ServiceProviderProfile(provider = serviceProvider)

open class ApplicationProfile(serviceProvider: ServiceProvider) :
    ServiceProviderProfile(provider = serviceProvider)

open class UserProfile(serviceProvider: ServiceProvider) :
    ServiceProviderProfile(provider = serviceProvider)

open class SubscriptionProfile(serviceProvider: ServiceProvider) :
    ServiceProviderProfile(provider = serviceProvider)