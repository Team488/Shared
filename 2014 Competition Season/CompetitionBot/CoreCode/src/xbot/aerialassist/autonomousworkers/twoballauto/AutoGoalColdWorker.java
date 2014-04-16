/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xbot.aerialassist.autonomousworkers.twoballauto;

import xbot.aerialassist.autonomousworkers.WaitForBallToSettleWorker;
import xbot.aerialassist.workers.AerialStateMachineWorker;
import xbot.aerialassist.workers.FireCatapultWorker;
import xbot.aerialassist.workers.SetArmsAndFireCatapultWorker;
import xbot.common.StateMachineWorker;
import xbot.common.WhenAllWorker;
import xbot.common.properties.DoubleProperty;

/**
 *
 * @author sterlingdorminey
 */
public class AutoGoalColdWorker extends AerialStateMachineWorker {
    // Heading that the robot should be facing when it turns away.
    private final DoubleProperty awayHeading =
            new DoubleProperty("AwayHeading", 165);
    
    public AutoGoalColdWorker() {
        super("AutoGoalColdWorker");
        
        // 0. Turn away. Shun the cold goal! Shunnn
        int turnAway = this.stateMachine.addWorker(
                new GoToHeadingWorker(this.awayHeading));
        
        // 1. Fire!
        int fire = this.stateMachine.addWorker(
                new FireCatapultWorker());
        
        // 2. Simultaneously:
        //    a. Turn back
        //    b. Consume ball.
        //    c. Wait for ball to settle.
        WhenAllWorker turnAndConsumeBallWorker = new WhenAllWorker();
        turnAndConsumeBallWorker.addWorker(
                new GoToHeadingWorker(
                        this.robot().GetSensorCore().startingOrientation));
        turnAndConsumeBallWorker.addWorker(
                new BringArmsUpAndStopRollersWorker());
        turnAndConsumeBallWorker.addWorker(
                new WaitForBallToSettleWorker());
        int turnBackAndConsumeBall = this.stateMachine.addWorker(
                turnAndConsumeBallWorker);
                
        // 3. Drop arms and fire!
        int dropArmsAndFire = this.stateMachine.addWorker(
                new SetArmsAndFireCatapultWorker());
        
        this.stateMachine.onStart(turnAway);
        this.stateMachine.onSuccess(turnAway, fire);
        this.stateMachine.onSuccess(fire, turnBackAndConsumeBall);
        this.stateMachine.onSuccess(turnBackAndConsumeBall, dropArmsAndFire);
        this.stateMachine.onSuccess(dropArmsAndFire, StateMachineWorker.SUCCESS);
    }
    
}
