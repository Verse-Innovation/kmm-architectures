
**Getting Started Service Oriented Architecture**

**Step 1 - Define**
1. Define the ServiceProvider which is specific to your library's genre.  
   Examples are - deeplink, message, navigation etc
2. Define the ServiceProviderPartner which is specific to your library's genre.
3. Define the ServiceProviderService(s) w.r.t to the defined ServiceProvider
4. Define the ServiceProviderServiceGateway w.r.t to the defined ServiceProviderService
5. For 3&4, be clear whether you need push/pull communication according to that choose the  
   Service and ServiceGateway contracts.
6. Identify if there are any genre level service offering flavors.
   1. Example - In messaging - push, pull, crud, inapp are the various messaging flavors.
   2. Example - In deeplinking - normal and deferred are two deeplinking flavors
7. If genre flavors are there, then define components per flavor level, nothing but defining the 
steps from 1 to 6.

**Step 2 - Build Library**
1. Preconditions - ensure consumer and service provider factories's factory are available.
2. Inject consumer and genre & name (optional)
3. While implementing buildLibrary ensure that,  
   3.1. you are binding using Tagd's bind<Library, YourLibrary>(yourLibraryInstance)  
   3.2. you are registering it at Soa.put(yourLibraryInstance).  
   3.3. If you have an aggregator library ex - Messaging. then inject the same at aggregator  
   library as well. ex - Messaging.pushMessaging = yourPushMessagingInstance

**Step 3 - Build Components**
1. Preconditions - ensure consumer and service provider factories's factory are available.
2. Create Provider
3. Create Service
4. Create Gateway and link to Service and link service to Gateway
5. Register service at provider
6. Register provider at provider factory through consumer.putServiceProvider (or)  
   serviceProviderLibrary.put(sp) (or) consumer.factories.put(sp) (or) factories.put(sp).  
   The 3rd way can be done even before creating library. The 4th way can be done even before  
   creating consumer.

The preferred approaches are *serviceLibrary.put(sp)* or *consumer.putServiceProvider(sp)*.