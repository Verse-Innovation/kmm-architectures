package io.verse.architectures.soa

import io.verse.architectures.soa.dispatcher.ServiceDataObjectDispatcher
import io.verse.architectures.soa.gateway.DefaultSubscribablePushServiceGateway
import io.verse.architectures.soa.io.ServiceDataObject
import org.mockito.Mockito.mock
import kotlin.test.Test

class DefaultSubscribablePushServiceGatewayTest {

    private val dispatcher: ServiceDataObjectDispatcher<ServiceDataObject, Unit> = mock()
    private val gateway = DefaultSubscribablePushServiceGateway(dispatcher)

    @Test
    fun `onReceive should call dispatchers dispatch`() {
        val message: ServiceDataObject = mock()

        gateway.onReceive(message)

        dispatcher.dispatch(message)
    }

}