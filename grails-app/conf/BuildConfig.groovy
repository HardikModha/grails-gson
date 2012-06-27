grails.project.work.dir = 'target'
grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'
grails.project.target.level = 1.6

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        compile 'com.google.code.gson:gson:2.2.1'
    }

    plugins {
        build(':release:2.0.3', ':rest-client-builder:1.0.2') {
            export = false
        }
        test(':spock:0.6') {
            export = false
        }
    }
}
