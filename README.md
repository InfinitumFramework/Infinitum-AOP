<p align="center">
  <img id="Infinitum Framework" src="http://infinitumframework.com/images/logo.png" />
</p>

Infinitum AOP
-------------

Infinitum AOP is used to separate cross-cutting concerns through aspect-oriented programming. With it, developers can alter or extend the behavior of core application code by creating aspects, which are used to apply advice at specific join points.

The AOP module also includes a cache abstraction. When enabled, methods can be marked for caching so that results can be retrieved without invoking the method on subsequent calls with the same arguments. This is particularly valuable for computation- or resource- intensive code.

Infinitum AOP provides access to the event framework, allowing events to be published for consumption by registered subscribers when certain methods are invoked.

AOP Features
------------

* Aspects: create aspects which specify advice and join points to alter or extend application code while separating cross-cutting concerns
* Before, around, and after advice: Infinitum supports three types of advice; _before_, which executes before a join point; _after_, which executes after a join point; and _around_, which can execute both before and after a join point
* Pointcuts: aspects can define pointcuts, which specify where advice should be applied
* Configurable: aspects and advice can be defined through annotations or in XML
* Cache abstraction: methods can be marked as cacheable so that subsequent calls with the same arguments need not be invoked
* Event hooks: publish events to the event system when certain methods are invoked
