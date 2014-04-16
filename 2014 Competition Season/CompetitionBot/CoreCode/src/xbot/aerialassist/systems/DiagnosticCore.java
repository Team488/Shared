/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.systems;

import java.util.Vector;
import xbot.aerialassist.RobotContext;
import xbot.aerialassist.diagnostics.*;
import xbot.common.Loggable;
import xbot.common.properties.BooleanProperty;
import xbot.common.properties.StringProperty;

/**
 * The diagnostic system allows us to test our robot rapidly in the pit and diagnose problems rapidly.
 * @author John
 */
public class DiagnosticCore extends Loggable {
    
    private final String initialDiagnosticMessage = "No diagnostics are running.";
    
    /**
     *
     */
    public StringProperty diagnosticMessage = new StringProperty("DiagnosticTestName", initialDiagnosticMessage);
    /**
     *
     */
    public StringProperty diagnosticCurrentInfo = new StringProperty("DiagnosticCurrentInfo", initialDiagnosticMessage);
    /**
     *
     */
    public BooleanProperty diagnosticComplete = new BooleanProperty("DiagnosticComplete", false);
    private Vector diagnosticTests;
    private int activeDiagnosticIndex;
    
    /**
     *
     */
    public DiagnosticCore()
    {
        super("DiagnosticCore");
        diagnosticMessage.set(initialDiagnosticMessage);
        diagnosticComplete.set(false);
        ResetDiagnostics(true);
    }
    
    public final void PrepareCalibration()
    {
        CollectionCore collection = RobotContext.Get().getCollectionCore();
        diagnosticTests = new Vector();
        activeDiagnosticIndex = 0;
        diagnosticTests.addElement(
                new CalibrateArmUpTest(collection.getFrontCollector()));
        diagnosticTests.addElement(
                new CalibrateArmDownTest(collection.getFrontCollector()));
        diagnosticTests.addElement(
                new CalibrateArmUpTest(collection.getBackCollector()));
        diagnosticTests.addElement(
                new CalibrateArmDownTest(collection.getBackCollector()));
        diagnosticTests.addElement(
                new CalibrateCatapultTest());
    }
    
    /**
     * Reset the system to run all diagnostics again upon command.
     */
    public final void ResetDiagnostics(boolean canDrive)
    {
        diagnosticTests = new Vector();
        activeDiagnosticIndex = 0;
        if (canDrive)
        {
            diagnosticTests.addElement(new FrontDriveTest());
            diagnosticTests.addElement(new RearDriveTest());
            diagnosticTests.addElement(new LeftDriveTest());
            diagnosticTests.addElement(new RightDriveTest());
        }
        diagnosticTests.addElement(new CollectorDeploySensorTest(true));
        diagnosticTests.addElement(new CollectorDeploySensorTest(false));
        diagnosticTests.addElement(new FrontCollectorIntakeTest());
        diagnosticTests.addElement(new BackCollectorIntakeTest());
        diagnosticTests.addElement(new CompressorTest());
        diagnosticTests.addElement(new CatapultLimitSwitchTest());
        diagnosticTests.addElement(new GyroTest());
        diagnosticTests.addElement(new JoystickTest());
        diagnosticTests.addElement(new CollectorDistanceSensorTest(true));
        diagnosticTests.addElement(new CollectorDistanceSensorTest(false));
    }
    
    /**
     * Run all diagnostics in succession. Each diagnostic test will complete when the user clicks the
     * "Diagnostic Complete" button on the SmartDashboard.
     */
    public void RunDiagnostics()
    {
        if (activeDiagnosticIndex >= diagnosticTests.size())
        {
            diagnosticMessage.set("All diagnostics complete!");
            return;
        }
        
        DiagnosticTest currentDiag = (DiagnosticTest)diagnosticTests.elementAt(activeDiagnosticIndex);
        
        diagnosticMessage.set(currentDiag.getDiagnosticTestName());
        diagnosticCurrentInfo.set(currentDiag.getCurrentTestInformation());
        currentDiag.executeTest();
        
        if (diagnosticComplete.get())
        {
            diagnosticComplete.set(false);
            currentDiag.cleanup();
            activeDiagnosticIndex++;
        }
    }
    
}
