import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender
import grails.util.BuildSettings
import grails.util.Environment

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%level %logger - %msg%n"
    }
}

root(ERROR, ['STDOUT'])

if (Environment.current == Environment.DEVELOPMENT) {
    if (targetDir) {

        appender("FULL_STACKTRACE", FileAppender) {

            file = "${targetDir}/stacktrace.log"
            append = true
            encoder(PatternLayoutEncoder) {
                pattern = "%level %logger - %msg%n"
            }
        }
        logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
        logger('org.grails.web.servlet', ERROR, ['STDOUT'])
        logger('org.grails.web.pages', ERROR, ['STDOUT'])//  GSP
        logger('org.grails.web.sitemesh', ERROR, ['STDOUT'])//  layouts
        logger('org.grails.web.mapping.filter', ERROR, ['STDOUT'])// URL mapping
        logger('org.grails.web.mapping', ERROR, ['STDOUT']) // URL mapping
        logger('org.grails.commons', ERROR, ['STDOUT']) // core / classloading
        logger('org.grails.plugins', ERROR, ['STDOUT']) // plugins
        logger('org.grails.spring', ERROR, ['STDOUT'])
        logger('org.mortbay.log', WARN,['STDOUT'])
    }
    def targetDir = BuildSettings.TARGET_DIR
}
