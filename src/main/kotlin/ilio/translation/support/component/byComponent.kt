package ilio.translation.support.component

import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

data class ComponentInstance<T>(val component: T)

operator fun <T> ComponentInstance<T>.getValue(any: Any?, property: KProperty<*>): T = component

object container {
    private val componentContainer = mutableMapOf<KClass<*>, Any>()

    fun <T: Component> getOrCreate(block: () -> T): T {
        val bean = block()
        componentContainer[bean.javaClass.kotlin] = bean
        return bean
    }

    inline operator fun <reified T : Component> getValue(any: Any?, property: KProperty<*>): T {
        val clazz: Class<T> = T::class.java
        if (Modifier.isAbstract(clazz.modifiers) || Modifier.isInterface(clazz.modifiers)) {
            throw Exception("Abstract class or Interface cannot be initialized: " + clazz.name)
        }
        val constructor = clazz.getDeclaredConstructor()
        if (!constructor.canAccess(null)) {
            throw Exception("Constructor cannot access: " + clazz.name)
        }
        if (constructor.parameters.isNotEmpty()) {
            throw Exception("Constructor cannot have parameter: " + clazz.name)
        }

        return getOrCreate { constructor.newInstance() }
    }
}

fun <T : Component> container(block: () -> T): ComponentInstance<T> {
    return ComponentInstance(container.getOrCreate(block))
}
