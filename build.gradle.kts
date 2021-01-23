import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * SARO KIT-EE
 *
 * + publish
 * 1. gradle publish
 * 2. https://oss.sonatype.org/
 * 3. Staging Repositories
 * 4. Close -> Release
 *
 * + publish setting
 * 1. create gpg
 * 2. set gradle.properties
 *    - ex windows path) C:/Users/<USER_NAME>/.gradle/gradle.properties
 *    sonatype.username=<username>
 *    sonatype.password=<password>
 *    signing.keyId=<last 16 chars in key>
 *    signing.password=<secret>
 *    signing.secretKeyRingFile=<path of secring.gpg>
 *
 * @See
 * https://github.com/saro-lab/kit-ee
 * https://docs.gradle.org/current/userguide/publishing_maven.html
 * https://docs.gradle.org/current/userguide/signing_plugin.html#signing_plugin
 */

plugins {
	val kotlinVersion = "1.4.20"
	kotlin("jvm") version kotlinVersion
	kotlin("kapt") version kotlinVersion
	//id("org.jetbrains.dokka") version kotlinVersion
	signing
	`maven-publish`
}

val kitGroupId = "me.saro"
val kitArtifactId = "kit-ee"
val kitVersion = "0.1.5.1"

configure<JavaPluginExtension> {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
	mavenCentral()
}

java {
	withJavadocJar()
	withSourcesJar()
}

dependencies {
	// koltin
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// lib
	val poi = "5.0.0";
	implementation("me.saro:kit:0.1.5")
	implementation("com.jcraft:jsch:0.1.55")
	implementation("commons-net:commons-net:3.7.2")
	implementation("org.apache.poi:poi:${poi}")
	implementation("org.apache.poi:poi-ooxml:${poi}")

	// test
	testImplementation("org.junit.jupiter:junit-jupiter-engine:+")
}

publishing {
	publications {
		create<MavenPublication>("maven") {

			groupId = kitGroupId
			artifactId = kitArtifactId
			version = kitVersion

			from(components["java"])

			repositories {
				maven {
					credentials {
						username = project.property("sonatype.username").toString()
						password = project.property("sonatype.password").toString()
					}
					val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
					val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
					url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
				}
			}

			pom {
				name.set("SARO KIT-EE")
				description.set("SARO KIT Enterprise Edition")
				url.set("https://saro.me")

				licenses {
					license {
						name.set("The Apache License, Version 2.0")
						url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
					}
				}
				developers {
					developer {
						name.set("PARK Yong Seo")
						email.set("j@saro.me")
					}
				}
				scm {
					connection.set("scm:git:git://github.com/saro-lab/kit-ee.git")
					developerConnection.set("scm:git:git@github.com:saro-lab/kit-ee.git")
					url.set("https://github.com/saro-lab/kit-ee")
				}
			}
		}
	}
}

signing {
	sign(publishing.publications["maven"])
}

//tasks.register<Jar>("dokkaJar") {
//	archiveClassifier.set("javadoc")
//	dependsOn("dokkaJavadoc")
//	from("$buildDir/dokka/javadoc/")
//}

tasks.withType<Javadoc>().configureEach {
	options {
		this as StandardJavadocDocletOptions
		addBooleanOption("Xdoclint:none", true)
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
