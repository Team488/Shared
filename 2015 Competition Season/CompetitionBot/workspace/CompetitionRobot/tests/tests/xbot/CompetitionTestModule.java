package tests.xbot;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import xbot.CommandFactory;
import xbot.common.injection.RobotModule;
import xbot.common.injection.UnitTestModule;

public class CompetitionTestModule extends UnitTestModule {

    @Override
    protected void configure() {
        super.configure();
        this.install(new FactoryModuleBuilder()
        .build(CommandFactory.class));
    }
}
