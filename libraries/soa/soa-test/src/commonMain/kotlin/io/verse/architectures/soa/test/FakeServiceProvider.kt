package io.verse.architectures.soa.test

import io.verse.architectures.soa.provider.DefaultServiceProvider
import io.verse.architectures.soa.provider.UserProfileEnricher

open class FakeServiceProvider(
    genre: String = "fake_service_provider",
    userProfileEnricher: UserProfileEnricher? = null,
) : DefaultServiceProvider(
    genre = genre,
    userProfileEnricher = userProfileEnricher
)

class FakeServiceProvider1(
    genre: String = "fake_service_provider1",
    userProfileEnricher: UserProfileEnricher? = null,
) : FakeServiceProvider(
    genre = genre,
    userProfileEnricher = userProfileEnricher
)

class FakeServiceProvider2(
    genre: String = "fake_service_provider2",
    userProfileEnricher: UserProfileEnricher? = null,
) : FakeServiceProvider(
    genre = genre,
    userProfileEnricher = userProfileEnricher
)