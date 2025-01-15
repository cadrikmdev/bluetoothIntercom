import com.android.build.api.dsl.ApplicationExtension
import com.cadrikmdev.convention.addUiLayerDependencies
import com.cadrikmdev.convention.configureAndroidCompose
import com.cadrikmdev.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin

class AndroidApplicationComposeConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("cadrikmdev.android.application")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
            dependencies {
                addUiLayerDependencies(target)
                "testImplementation"(kotlin("test"))
                "implementation"(project.libs.findLibrary("kotlinx.serialization.json").get())
            }
        }
    }
}
