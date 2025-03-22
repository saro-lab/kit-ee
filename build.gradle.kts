plugins {
	val kotlinVersion = "2.1.0"
	id("org.jetbrains.kotlin.jvm") version kotlinVersion
	signing
	`maven-publish`
}

val kitGroupId = "me.saro"
val kitArtifactId = "kit-ee"
val kitVersion = "1.0.0"

repositories {
	mavenCentral()
}


dependencies {
	// test
	testImplementation("org.junit.jupiter:junit-jupiter:5.11.1")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
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
						try {
							username = project.property("sonatype.username").toString()
							password = project.property("sonatype.password").toString()
						} catch (e: Exception) {
							println("warn: " + e.message)
						}
					}
					val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
					val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
					url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
				}
			}

			pom {
				name.set("SARO KIT")
				description.set("SARO KIT")
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
					connection.set("scm:git:git://github.com/saro-lab/kit.git")
					developerConnection.set("scm:git:git@github.com:saro-lab/kit.git")
					url.set("https://github.com/saro-lab/kit")
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

java {
	withJavadocJar()
	withSourcesJar()
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}


configure<JavaPluginExtension> {
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
	useJUnitPlatform()
}
