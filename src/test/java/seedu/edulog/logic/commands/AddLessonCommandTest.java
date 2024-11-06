package seedu.edulog.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.edulog.logic.commands.CommandTestUtil.assertCommandFailure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.edulog.logic.commands.exceptions.CommandException;
import seedu.edulog.model.Model;
import seedu.edulog.model.ModelManager;
import seedu.edulog.model.calendar.Day;
import seedu.edulog.model.calendar.Description;
import seedu.edulog.model.calendar.EdulogCalendar;
import seedu.edulog.model.calendar.Lesson;
import seedu.edulog.model.calendar.LessonTime;

public class AddLessonCommandTest {

    private Model model;
    private Lesson validLesson;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        validLesson = new Lesson(new Description("Math"),
            new Day("Monday"), new LessonTime("1200"), new LessonTime("1300"));
    }

    @Test
    public void execute_uniqueValidLesson_success() throws CommandException {
        AddLessonCommand command = new AddLessonCommand(validLesson);
        CommandResult result = command.execute(model);
        assertEquals(result.getFeedbackToUser(),
                String.format(AddLessonCommand.MESSAGE_SUCCESS, validLesson));
        assertTrue(model.hasLesson(validLesson));
    }

    @Test
    public void execute_duplicateValidLesson_throwsCommandException() throws CommandException {
        model.addLesson(validLesson);

        // note that the lesson only has identical descriptions as the validLesson
        Lesson duplicateLesson = new Lesson(new Description("Math"),
            validLesson.getStartDay(), validLesson.getStartTime(), validLesson.getEndTime());
        AddLessonCommand command = new AddLessonCommand(duplicateLesson);

        assertCommandFailure(command, model, AddLessonCommand.MESSAGE_DUPLICATE_LESSON);
    }

    @Test
    public void execute_overloadIdenticalTimingLesson_throwsCommandException() throws CommandException {
        String startDay = "Monday";
        String startTime = "1000";
        String endTime = "1330";

        Lesson lesson1 = new Lesson(new Description("Class X"),
            new Day(startDay), new LessonTime(startTime), new LessonTime(endTime));

        Lesson lesson2 = new Lesson(new Description("Class Y"),
            new Day(startDay), new LessonTime(startTime), new LessonTime(endTime));

        AddLessonCommand initialCommand = new AddLessonCommand(lesson1);
        initialCommand.execute(model);

        AddLessonCommand subsequentCommand = new AddLessonCommand(lesson2);
        assertCommandFailure(subsequentCommand, model, EdulogCalendar.OVERLOAD_SIMULTANEOUS_TIMING);
    }
}
