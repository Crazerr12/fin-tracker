/**
 * Precompiled [kotlin.base.config.gradle.kts][Kotlin_base_config_gradle] script plugin.
 *
 * @see Kotlin_base_config_gradle
 */
public
class Kotlin_base_configPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Kotlin_base_config_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
