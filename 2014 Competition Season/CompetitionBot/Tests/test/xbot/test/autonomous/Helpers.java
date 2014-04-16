/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.test.autonomous;

import static org.junit.Assert.assertEquals;
import xbot.aerialassist.RobotContext;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.collection.CollectorDeployState;
import xbot.aerialassist.workers.AerialStateMachineWorker;
import xbot.common.CommonTools;
import xbot.common.ExecState;
import xbot.common.WorkerBase;
import xbot.common.properties.ITableProxy;

/**
 *
 * @author sterlingdorminey
 */
public class Helpers {
    public static void execAndAssert(AerialStateMachineWorker worker, int state) {
        worker.exec();
        assertEquals(state, worker.getCurrentState());
    }
    
    public static void execAndAssert(WorkerBase worker, ExecState state) {
        worker.exec();
        assertEquals(state, worker.isFinished());
    }
    
    public static void setCatapultCocked() {
        RobotContext.Get().GetCatapultCore().setCatapultScaledPositionTESTONLY(
            RobotContext.Get().GetCatapultCore().getCatapultCockedPosition());
    }
    
    public static void setCatapultNotCocked() {
        RobotContext.Get().GetCatapultCore().setCatapultScaledPositionTESTONLY(
            getCatapultLoadPosition() + 0.01);
    }
    
    public static void setCatapultCockedEnough() {
        RobotContext.Get().GetCatapultCore().setCatapultScaledPositionTESTONLY(
            getCatapultLoadPosition() - 0.01);
    }
    
    private static double getCatapultLoadPosition() {
        return RobotContext.Get().GetCatapultCore().catapultLoadPosition.get();
    }
    
    public static void setBallSettled() {
        RobotContext.Get().GetCatapultCore().SetBallSensorVoltage(5);
    }
    
    public static void setBallNotSettled() {
        RobotContext.Get().GetCatapultCore().SetBallSensorVoltage(0);
    }
    
    public static void setCollectorState(
            Collector collector,
            CollectorDeployState state) {
        if (CollectorDeployState.UP == state) {
            collector.getSensors().SetScaledPositionTESTONLY(0);
        }
        
        if (CollectorDeployState.DOWN == state) {
            collector.getSensors().SetScaledPositionTESTONLY(1);
        }
        
        if (CollectorDeployState.FIRING == state) {
            collector.getSensors().SetScaledPositionTESTONLY(
                collector.getSensors().firingPositionLowerLimit.get());
        }
        
        if (CollectorDeployState.SAFE == state) {
            collector.getSensors().SetScaledPositionTESTONLY(
                collector.getSensors().safePosition.get());
        }
    }
    
    public static void setRobotHeading(double heading) {
        //RobotContext.Get().GetSensorCore().resetHeading();
        //RobotContext.Get().GetSensorCore().resetCurrentAngleTESTONLY();
        RobotContext.Get().GetSensorCore().DepositLocationInformationFromRobot(90, 0, 0, 0);
        RobotContext.Get().GetSensorCore().DepositLocationInformationFromRobot(heading - 90, 0, 0, 0);
        assertEquals(heading, RobotContext.Get().GetSensorCore().getCurrentHeading(), 0.01);
    }
}
