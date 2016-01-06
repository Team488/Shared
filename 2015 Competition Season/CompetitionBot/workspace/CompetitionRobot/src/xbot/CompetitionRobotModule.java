package xbot;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import xbot.common.injection.RobotModule;

public class CompetitionRobotModule extends RobotModule {

    @Override
    protected void configure() {
        super.configure();
        this.install(new FactoryModuleBuilder()
        .build(CommandFactory.class));
    }
}
