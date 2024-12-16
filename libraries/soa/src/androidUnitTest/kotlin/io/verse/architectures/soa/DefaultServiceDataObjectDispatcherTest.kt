package io.verse.architectures.soa

import io.tagd.langx.IllegalAccessException
import io.verse.architectures.soa.dispatcher.DefaultServiceDataObjectDispatcher
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandler.Companion.DEFAULT_HANDLE
import io.verse.architectures.soa.dispatcher.ServiceDataObjectHandlerFactory
import io.verse.architectures.soa.io.ServiceDataObject
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class DefaultServiceDataObjectDispatcherTest {

    private val handlerFactory = mock<ServiceDataObjectHandlerFactory<ServiceDataObject, Unit>>()
    private val dispatcher = DefaultServiceDataObjectDispatcher(handlerFactory)

    @Test
    fun `if matching handler present then dispatch should route message to correct handler`() {
        val data: ServiceDataObject = mock()
        val handler: ServiceDataObjectHandler<ServiceDataObject, Unit> = mock()
        whenever(data.handle).thenReturn("handler")
        whenever(data.handleAvailable()).thenReturn(true)
        whenever(handlerFactory.get("handler")).thenReturn(handler)

        dispatcher.dispatch(data)

        verify(handler).handle(data)
        verifyNoMoreInteractions(handler)
    }

    @Test
    fun `if no matching handler present then dispatch should route message to default handler`() {
        val data: ServiceDataObject = mock()
        val defaultHandler: ServiceDataObjectHandler<ServiceDataObject, Unit> = mock()
        whenever(data.handle).thenReturn("handler")
        whenever(data.handleAvailable()).thenReturn(true)
        whenever(handlerFactory.get("handler")).thenReturn(null)
        whenever(handlerFactory.get(DEFAULT_HANDLE)).thenReturn(defaultHandler)

        dispatcher.dispatch(data)

        verify(defaultHandler).handle(data)
        verifyNoMoreInteractions(defaultHandler)
    }

    @Test(expected = IllegalAccessException::class)
    fun `if no default handler present then dispatch should throw exception`() {
        val data: ServiceDataObject = mock()
        whenever(data.handle).thenReturn("default")
        whenever(data.handleAvailable()).thenReturn(true)
        whenever(handlerFactory.get("default")).thenReturn(null)
        whenever(handlerFactory.get(DEFAULT_HANDLE)).thenReturn(null)

        dispatcher.dispatch(data)
    }

}