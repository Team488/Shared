/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.diagnostics;

import xbot.aerialassist.AerialWorkerBase;

/**
 *
 * @author sterlingdorminey
 */
public class CalibrationWorker extends AerialWorkerBase {

    public CalibrationWorker() {
        super("CalibrationWorker");
    }
    
    /**
     *
     */
    public void init()
    {
        robot().GetDiagnosticCore().PrepareCalibration();
    }
    
    /**
     *
     */
    public void exec()
    {
        robot().GetDiagnosticCore().RunDiagnostics();
    }
}
