package ani.rss.util;

import ani.rss.entity.Config;
import ani.rss.entity.Log;
import ani.rss.list.FixedSizeLinkedList;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.StrFormatter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
public class LogUtil {

    @Getter
    private static final List<Log> logs = Collections.synchronizedList(new FixedSizeLinkedList<>(4096));

    public static void loadLogback() {
        Config config = ConfigUtil.getCONFIG();
        Boolean debug = config.getDebug();
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            String s = ResourceUtil.readUtf8Str("logback-template.xml");
            s = s.replace("${config}", ConfigUtil.getConfigDir() + "/");
            if (debug) {
                s = s.replace("${level}", "debug");
            } else {
                s = s.replace("${level}", "info");
            }

            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();

            byteArrayInputStream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
            configurator.doConfigure(byteArrayInputStream);

            Appender<ILoggingEvent> file = rootLogger.getAppender("FILE");
            file.clearAllFilters();
            file.addFilter(new AbstractMatcherFilter<>() {
                @Override
                public FilterReply decide(ILoggingEvent event) {
                    Instant instant = event.getInstant();
                    String date = DateUtil.format(new Date(instant.toEpochMilli()), DatePattern.NORM_DATETIME_PATTERN);
                    String level = event.getLevel().toString();
                    String loggerName = event.getLoggerName();
                    String formattedMessage = event.getFormattedMessage();
                    String log = StrFormatter.format("{} {} {} - {}", date, level, loggerName, formattedMessage);
                    logs.add(new Log().setMessage(log).setLevel(level));
                    return FilterReply.NEUTRAL;
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            log.debug(e.getMessage(), e);
        } finally {
            IoUtil.close(byteArrayInputStream);
        }
    }
}
