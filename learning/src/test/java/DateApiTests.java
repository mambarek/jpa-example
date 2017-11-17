import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

public class DateApiTests {

    public static void main(String[] args) {

        Clock clock = Clock.systemDefaultZone();
        final long millis = clock.millis();
        System.out.println("millis = " + millis);

        final Instant instant = clock.instant();
        final Date legacyDate = Date.from(instant);

        // util date now
        Date now = new Date();
        // equivalent
        final Date date = Date.from(Clock.systemDefaultZone().instant());

//        System.out.println(ZoneId.getAvailableZoneIds());
        System.out.println("-------------- All TimezoneIds -------------------------");
        ZoneId.getAvailableZoneIds().parallelStream().filter(s -> s.toLowerCase().contains("tun")).forEach(System.out::println);

        ZoneId zone1 = ZoneId.of("Europe/Berlin");
        ZoneId zone2 = ZoneId.of("Brazil/East");

        System.out.println(zone1.getRules());
        System.out.println(zone2.getRules());

        ZonedDateTime now1 = ZonedDateTime.now(zone1);
        ZonedDateTime now2 = ZonedDateTime.now(zone2);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd : hh:mm z");

        final String formattedDateNow = dateFormat.format(now);

        System.out.println("formattedDateNow = " + formattedDateNow);

        DateTimeFormatter germanFormatter =
                DateTimeFormatter
                        .ISO_DATE_TIME
//                        .ofPattern("hh:mm z")
//                        .ofLocalizedTime(FormatStyle.SHORT)
                        .withLocale(Locale.GERMAN);

/*        final String formattedInstant = germanFormatter.format(clock.instant());
        System.out.println("formattedInstant = " + formattedInstant);*/

        final String formattedTime1 = germanFormatter.format(now1);
        System.out.println("formattedTime1 = " + formattedTime1);

        final String formattedTime2 = germanFormatter.format(now2);
        System.out.println("formattedTime2 = " + formattedTime2);

//        System.out.println(now1.isBefore(now2));  // false

        long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
        long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);

        final Duration duration = Duration.between(now1, now2);

        System.out.println("duration = " + duration);

        System.out.println(hoursBetween);       // -3
        System.out.println(minutesBetween);
    }
}
