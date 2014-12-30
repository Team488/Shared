/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.diagnostics;

import xbot.aerialassist.AerialWorkerBase;

/**
 * This worker is pretty thin - it mostly just runs the Diagnostic system.
 * @author John
 */
public class DiagnosticWorker extends AerialWorkerBase {
    
    private boolean testDrive;
    /**
     *
     */
    public DiagnosticWorker(boolean testDrive)
    {
        super("DiagnosticWorker" + GetSuffix(testDrive));
        this.testDrive = testDrive;
    }
    
    private static String GetSuffix(boolean testDrive)
    {
        String suffix = "WithDrive";
        if (!testDrive)
        {
            suffix = "WithoutDrive";
        }
        return suffix;
    }
    
    /**
     *
     */
    public void init()
    {
        robot().GetDiagnosticCore().ResetDiagnostics(testDrive);
    }
    
    /**
     *
     */
    public void exec()
    {
        robot().GetDiagnosticCore().RunDiagnostics();
    }
}
