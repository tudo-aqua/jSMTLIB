/*
 * This file is the main configuration for the gradle build system.
 * It allows to run the test suite and build a jar for distribution.
 *
 * Author Malte Mues (@mmuesly)
 * Created August 2019
 */

plugins {
    `java`
    `maven-publish`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

allprojects {
    apply(plugin = "java-library")
    version = "0.9.10.4"
    group = "tools.aqua.redistribution"
    repositories {
        jcenter()
        mavenCentral()
    }
    dependencies {
        // Use JUnit test framework
        testImplementation("junit:junit:4.12")
    }
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("SMT/src/"))
        }
        resources {
            setSrcDirs(listOf("SMT/logics"))
        }
    }
    test {
        java {
            setSrcDirs(listOf("SMTTests/src", "SMT/test"))
        }
        resources {
            setSrcDirs(listOf("SMTTests/tests", "SMT/solvers"))
        }
    }
    create("solvers") {
        resources {
            setSrcDirs(listOf("SMT/solvers"))
        }
    }
}

tasks.register<Jar>("jarWithSolvers") {
    archiveClassifier.set("with-solvers")
    dependsOn("solversClasses", "classes")
    from(sourceSets.main.get().output, sourceSets.getByName("solvers").output.resourcesDir)
}

publishing{
    publications{
        create<MavenPublication>("mavenJava") {
            artifactId = "jSMTLIB"
            from(components["java"])
            pom {
                name.set("jSMTLIB")
                description.set("This is a redistribution build of the original jSMTLIB.")
                url.set("https://github.com/smtlib/jSMTLIB")
                licenses {
                    license{
                        name.set("EPL-1.0")
                        url.set("www.opensource.org/licenses/EPL-1.0")
                    }
                }
                scm {
                    url.set("https://github.com/tudo-aqua/jSMTLIB")
                }
            }
        }
    }
}
