package io.verse.architectures.soa.test

import io.verse.architectures.soa.provider.ServiceProvider
import io.verse.architectures.soa.provider.ServiceProviderProfile

class FakeServiceProviderProfile1(
    override val provider: ServiceProvider?,
) : ServiceProviderProfile

class FakeServiceProviderProfile2(
    override val provider: ServiceProvider?,
) : ServiceProviderProfile