package peoplesoft.logic.commands;

import org.junit.jupiter.api.Test;

import peoplesoft.model.Model;
import peoplesoft.model.ModelManager;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_help_success() {
        // todo: add tests
        //CommandResult expectedCommandResult = new CommandResult(SHOWING_HELP_MESSAGE, true, false);
        //assertCommandSuccess(new HelpCommand(), model, expectedCommandResult, expectedModel);
    }
}
