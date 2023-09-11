package cz.devforce.partnersbootcamp.common;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtils {

    public static String defaultZoneStr = "UTC";

    public static ZoneId defaultZoneId = ZoneId.of(defaultZoneStr);

    public static ZonedDateTime now() {
        return ZonedDateTime.now(defaultZoneId);
    }
}
