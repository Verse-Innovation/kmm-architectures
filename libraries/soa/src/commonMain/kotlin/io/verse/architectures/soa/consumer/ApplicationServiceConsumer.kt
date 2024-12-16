package io.verse.architectures.soa.consumer

import io.verse.architectures.soa.provider.ApplicationProfile
import io.verse.architectures.soa.provider.DeviceProfile
import io.verse.architectures.soa.provider.ServiceProviderFactoriesFactory
import io.verse.architectures.soa.provider.UserProfile

/**
 * There will be only one single application level service consumer called
 * [ApplicationServiceConsumer].
 */
open class ApplicationServiceConsumer(
    factories: ServiceProviderFactoriesFactory,
    deviceProfile: DeviceProfile,
    applicationProfile: ApplicationProfile,
    userProfile: UserProfile
) : DefaultServiceConsumer(
    factories = factories,
    deviceProfile = deviceProfile,
    applicationProfile = applicationProfile,
    userProfile = userProfile,
)

/**
 * Let the [io.tagd.arch.control.IApplication] implementer implement this. So logically there will
 * be only one instance of [ApplicationServiceConsumer] / [io.tagd.arch.control.IApplication]
 */
interface ApplicationServiceConsumerProvider {

    var consumer: ApplicationServiceConsumer?
}