/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.actualrobot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc488.OnRobotCode.Robot;
import xbot.common.ExecState;
import xbot.common.WorkerBase;
import xbot.common.logging.LogProducer;

/**
 *
 * @author Alex
 */
public class CommandProxy extends Command {
    WorkerBase worker;
    
    private LogProducer logProducer;
    
    public CommandProxy(WorkerBase worker) {
        super(worker.getName());
        InitializeCommandProxy(worker, false);
    }
    
    public CommandProxy(WorkerBase worker, Subsystem requiresSubsystem) {
        super(worker.getName());
        InitializeCommandProxy(worker, false);
        this.requires(requiresSubsystem);
    }
    
    public CommandProxy(WorkerBase worker, Subsystem requiresSubsystem1, Subsystem requiresSubsystem2) {
        super(worker.getName());
        InitializeCommandProxy(worker, false);
        this.requires(requiresSubsystem1);
        this.requires(requiresSubsystem2);
    }
    
    public CommandProxy(WorkerBase worker, Subsystem[] requiresSubsystems) {
        super(worker.getName());
        InitializeCommandProxy(worker, false);

        for(int i = 0; i < requiresSubsystems.length; i++) {
            this.requires(requiresSubsystems[i]);
        }
    }
    
    private void InitializeCommandProxy(WorkerBase worker, boolean addToSmartDashboard)
    {
        this.logProducer = new LogProducer("CommandProxy", LogProducer.LOGGING);
        this.worker = worker;
        if(addToSmartDashboard) {
            SmartDashboard.putData(this);
        }
    }
  
    private void SetRequiresAll()
    {
        this.requires(Robot.actuators);
        this.requires(Robot.backCollectorDeploy);
        this.requires(Robot.backCollectorRoller);
        this.requires(Robot.catapult);
        this.requires(Robot.drive);
        this.requires(Robot.frontCollectorDeploy);
        this.requires(Robot.frontCollectorRoller);
        this.requires(Robot.pneumatics);
        this.requires(Robot.sensors);
    }

    protected void initialize() {
        try {
            worker.init();
        } catch (Exception e) {
            this.logProducer.Log(LogProducer.WARNING, e.toString());
        }
    }

    protected void execute() {
        try {
            worker.exec();
        } catch (Exception e) {
            this.logProducer.Log(LogProducer.WARNING, e.toString());
        }
    }
    
    protected boolean isFinished() {
        try {
            return worker.isFinished() == ExecState.SUCCESS;
        } catch (Exception e) {
            this.logProducer.Log(LogProducer.WARNING, e.toString());
        }
        
        return true;
    }    

    protected void end() {
        
    }

    protected void interrupted() {
        try {
            worker.interrupted();
        } catch (Exception e) {
            this.logProducer.Log(LogProducer.WARNING, e.toString());
        }
    }
}
