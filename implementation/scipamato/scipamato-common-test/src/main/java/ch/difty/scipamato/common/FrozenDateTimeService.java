package ch.difty.scipamato.common;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Implementation of {@link DateTimeService} constantly returning a frozen
 * moment. This is the implementation to be used in test context.
 *
 * @author u.joss
 */
public class FrozenDateTimeService implements DateTimeService {

    private static final LocalDateTime FROZEN = LocalDateTime.parse("2016-12-09T06:02:13");

    @Override
    public LocalDateTime getCurrentDateTime() {
        return FROZEN;
    }

    @Override
    public Timestamp getCurrentTimestamp() {
        return Timestamp.valueOf(FROZEN);
    }

    @Override
    public LocalDate getCurrentDate() {
        return LocalDate.from(FROZEN);
    }

}
