package io.verse.architectures.soa.gateway

import io.tagd.arch.data.gateway.Gateway
import io.verse.architectures.soa.service.SubscribableService

interface SubscribableServiceGateway : Gateway {

    val service: SubscribableService?
}

