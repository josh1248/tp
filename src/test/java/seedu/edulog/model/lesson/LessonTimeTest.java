package seedu.edulog.model.lesson;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.edulog.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.edulog.model.calendar.LessonTime;

public class LessonTimeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LessonTime(null));
    }

    @Test
    public void constructor_emptyTime_throwsIllegalArgumentException() {
        String invalidTimeEmpty = "";
        assertThrows(IllegalArgumentException.class, () -> new LessonTime(invalidTimeEmpty));

    }

    @Test
    public void checkValidLessonTime() {
        // EP: Values in the morning, shared with 12H format
        assertTrue(LessonTime.checkValidLessonTime("1000"));
        assertTrue(LessonTime.checkValidLessonTime("0511"));
        assertTrue(LessonTime.checkValidLessonTime("0159"));

        // EP: Values in the afternoon and evening, unique to 24H format
        assertTrue(LessonTime.checkValidLessonTime("1330"));
        assertTrue(LessonTime.checkValidLessonTime("2200"));
        assertTrue(LessonTime.checkValidLessonTime("2310"));

        // EP: Valid boundary values
        assertTrue(LessonTime.checkValidLessonTime("0000"));
        assertTrue(LessonTime.checkValidLessonTime("2359"));

        // EP: Out of bound, 4-character values
        assertFalse(LessonTime.checkValidLessonTime("2400")); // hour boundary value
        assertFalse(LessonTime.checkValidLessonTime("0260")); // minute boundary value
        assertFalse(LessonTime.checkValidLessonTime("9900")); // invalid hour
        assertFalse(LessonTime.checkValidLessonTime("1099")); // invalid minute
        assertFalse(LessonTime.checkValidLessonTime("2460")); // both invalid

        // EP: not a 4-character value
        assertFalse(LessonTime.checkValidLessonTime("21:00"));
        assertFalse(LessonTime.checkValidLessonTime("11-00"));
        assertFalse(LessonTime.checkValidLessonTime("              "));
        assertFalse(LessonTime.checkValidLessonTime("1000 "));

        // EP: Letters in value
        assertFalse(LessonTime.checkValidLessonTime("12PM"));

        // EP: null
        assertThrows(NullPointerException.class, () -> LessonTime.checkValidLessonTime(null));
    }
    @Test
    public void checkValidLessonTimes() {
        // EP: valid start time before end time
        assertTrue(LessonTime.checkValidLessonTimes("1000", "1330"));
        assertTrue(LessonTime.checkValidLessonTimes("0000", "2359"));

        // EP: valid start time after end time
        assertTrue(LessonTime.checkValidLessonTimes("1330", "1000"));
        assertTrue(LessonTime.checkValidLessonTimes("2359", "0000")); // double boundary values

        // EP: invalid start time same as end time
        assertFalse(LessonTime.checkValidLessonTimes("2100", "2100"));

        // EP: 1 lesson time is invalid
        assertThrows(IllegalArgumentException.class, () -> LessonTime.checkValidLessonTimes("2460", "1330"));
        assertThrows(IllegalArgumentException.class, () -> LessonTime.checkValidLessonTimes("1000", "430"));

        // EP: both lesson times are invalid
        assertThrows(IllegalArgumentException.class, () -> LessonTime.checkValidLessonTimes("2460", "430"));

        // EP: nulls
        assertThrows(NullPointerException.class, () -> LessonTime.checkValidLessonTimes("0100", null));
        assertThrows(NullPointerException.class, () -> LessonTime.checkValidLessonTimes(null, "1430"));
        assertThrows(NullPointerException.class, () -> LessonTime.checkValidLessonTimes(null, null));
    }

    @Test
    public void spansTwoDays() {
        // EP: start time before end time
        assertFalse(LessonTime.spansTwoDays(
            new LessonTime("1000"),
            new LessonTime("1330")));

        // EP: start time after end time
        assertTrue(LessonTime.spansTwoDays(
            new LessonTime("1330"),
            new LessonTime("1000")));

        // EP: start time equal to end time
        assertThrows(IllegalArgumentException.class, () -> LessonTime.spansTwoDays(
            new LessonTime("1000"), new LessonTime("1000"))
        );

        // EP: nulls
        assertThrows(NullPointerException.class, () -> LessonTime.spansTwoDays(new LessonTime("1111"), null));
        assertThrows(NullPointerException.class, () -> LessonTime.spansTwoDays(null, new LessonTime("1111")));
        assertThrows(NullPointerException.class, () -> LessonTime.spansTwoDays(null, null));
    }

    @Test
    public void contains() {
        // EP: between time is fully contained, does not equal either border
        assertTrue(LessonTime.contains(
            new LessonTime("1000"),
            new LessonTime("1330"),
            new LessonTime("1230")
        ));

        // EP: between time is contained, equalling left border
        assertTrue(LessonTime.contains(
            new LessonTime("1000"),
            new LessonTime("1330"),
            new LessonTime("1000")
        ));

        // EP: between time is contained, equalling right border
        assertTrue(LessonTime.contains(
            new LessonTime("1000"),
            new LessonTime("1330"),
            new LessonTime("1330")
        ));

        // EP: between time is contained, equalling both borders
        assertTrue(LessonTime.contains(
            new LessonTime("1000"),
            new LessonTime("1000"),
            new LessonTime("1000")
        ));

        // EP: nulls
        assertThrows(NullPointerException.class, () -> LessonTime.contains(
            new LessonTime("2359"),
            null,
            new LessonTime("2359")
        ));
    }

    @Test
    public void equals() {
        LessonTime time = new LessonTime("1200");

        // same value
        assertTrue(time.equals(new LessonTime("1200")));

        // same object
        assertTrue(time.equals(time));

        // different value
        assertFalse(time.equals(new LessonTime("1201")));

        // different types
        assertFalse(time.equals(5.0f));

        // null
        assertFalse(time.equals(null));
    }

}
