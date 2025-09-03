import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2023/12/6 14:25
 * Describe : AndroidApplicationSigningConventionPlugin
 */
class AndroidApplicationSigningConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        with(target) {
            extensions.configure<ApplicationExtension> {
                signingConfigs {
                    create("sign") {
                        storeFile = rootProject.file("${project.rootDir}/build-key/ledvance.jks")
                        storePassword = "ledvance@2021"
                        keyAlias = "ledvance"
                        keyPassword = "ledvance@2021"
                    }.apply {
                        enableV1Signing = true
                        enableV2Signing = true
                    }
                }
                buildTypes {
                    debug {
                        signingConfig = signingConfigs.getByName("sign")
                    }
                    release {
                        signingConfig = signingConfigs.getByName("sign")
                        isMinifyEnabled = true
                        isShrinkResources = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
            }
        }
    }
}