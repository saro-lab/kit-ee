import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * SARO JWT
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
 *    signing.keyId=<last 8/16 chars in key>
 *    signing.password=<secret>
 *    signing.secretKeyRingFile=<path of secring.gpg>
 *
 * + you can use "User Token" instead of id & password.
 *     - https://oss.sonatype.org -> profile -> User Token
 *
 * @See
 * https://github.com/saro-lab/jwt
 * https://docs.gradle.org/current/userguide/publishing_maven.html
 * https://docs.gradle.org/current/userguide/signing_plugin.html#signing_plugin
 * windows -> pgp4win
 * gpg --gen-key
 * gpg --list-keys --keyid-format short
 * gpg --export-secret-keys -o secring.gpg
 */

plugins {
	val kotlinVersion = "1.9.20-RC2"
	id("org.jetbrains.kotlin.jvm") version kotlinVersion
	id("org.jetbrains.kotlin.kapt") version kotlinVersion
	signing
	`maven-publish`
}

val kitGroupId = "me.saro"
val kitArtifactId = "kit-ee"
val kitVersion = "0.1.6"
val kitEeVersion = "0.1.8"

configure<JavaPluginExtension> {
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

java {
	withJavadocJar()
	withSourcesJar()
}

dependencies {
	// lib
	val poi = "5.0.0"
	api("me.saro:kit:$kitVersion")
	implementation("com.jcraft:jsch:0.1.55")
	implementation("commons-net:commons-net:3.8.0")

	// test
	testImplementation("org.junit.jupiter:junit-jupiter-engine:+")
}

publishing {
	publications {
		create<MavenPublication>("maven") {

			groupId = kitGroupId
			artifactId = kitArtifactId
			version = kitEeVersion

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

tasks.withType<Javadoc>().configureEach {
	options {
		this as StandardJavadocDocletOptions
		addBooleanOption("Xdoclint:none", true)
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "21"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
