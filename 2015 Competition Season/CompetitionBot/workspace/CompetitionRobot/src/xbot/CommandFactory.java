package xbot;

import xbot.commands.rotation.SetTargetHeadingCommand;
import xbot.commands.rotation.SetTargetHeadingCommand.Target;

public interface CommandFactory {
    SetTargetHeadingCommand createSetTargetHeadingCommand(double newTarget);
    SetTargetHeadingCommand createSetTargetHeadingCommand(Target newTarget);
}
