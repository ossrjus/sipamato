object Lib {

    // SciPaMaTo

    fun scipamatoCommon(module: String) = ":scipamato-common-$module"
    fun scipamatoCommon(vararg modules: String) = modules.map { "scipamato-common-$it" }.toTypedArray()


    // Spring

    fun springBootStarter(module: String) = "org.springframework.boot:spring-boot-starter-$module:2.1.4.RELEASE"
    fun spring(module: String) = "org.springframework:spring-$module:5.1.6.RELEASE"


    // Lombok

    @Deprecated("convert to kotlin", ReplaceWith("kotlin data classes, kotlin-logging"))
    fun lombok() = "org.projectlombok:lombok:1.18.6"


    // Logging

    fun slf4j() = "org.slf4j:slf4j-api:1.7.26"
    fun logback() = "ch.qos.logback:logback-core:1.2.3"


    // DB

    fun jOOQ() = "org.jooq:jooq:3.11.10"
    fun flyway() = "org.flywaydb:flyway-core:5.2.4"
    fun postgres() = "org.postgresql:postgresql:42.2.5"


    // Utility libraries

    fun commonsLang3() = "org.apache.commons:commons-lang3:3.8.1"
    fun commonsIo() = "commons-io:commons-io:2.6"
    fun commonsCollection() = "org.apache.commons:commons-collections4:4.3"
    fun jool() = "org.jooq:jool-java-8:0.9.14"
    fun bval() = "org.apache.bval:bval-jsr:2.0.0"


    // Test Libraries

    fun junit5(module: String = "") = "org.junit.jupiter:junit-jupiter${if (module.isNotBlank()) "-$module" else ""}:5.4.2"
    fun mockito2(module: String) = "org.mockito:mockito-$module:2.23.4"
    fun assertj() = "org.assertj:assertj-core:3.11.1"
    fun equalsverifier() = "nl.jqno.equalsverifier:equalsverifier:3.1.8"
}