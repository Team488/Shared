/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.tests.workers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import xbot.aerialassist.collection.Collector;
import xbot.aerialassist.workers.FieldOrientedBallEjection;
import xbot.test.common.BaseTest;

/**
 *
 * @author Alex
 */
public class FieldOrientedEjectionTest extends BaseTest {
    
    FieldOrientedBallEjection worker;
    Collector front;
    Collector back;
    
    public FieldOrientedEjectionTest() {
    }
    
    @Before
    public void setUp() {
        worker = new FieldOrientedBallEjection();
        front = context.getCollectionCore().getFrontCollector();
        back = context.getCollectionCore().getBackCollector();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void neutralState() {
        worker.exec();
        assertEquals(front.getCollectorRollerMotor(), 0, 0.01);
        assertEquals(back.getCollectorRollerMotor(), 0, 0.01);
    }
    
    @Test
    public void frontEject() {
        context.GetOICore().setOperatorJoyX(0);
        context.GetOICore().setOperatorJoyY(1);
        worker.exec();
        assertEquals(front.getCollectorRollerMotor(), -1, 0.01);
        assertEquals(back.getCollectorRollerMotor(), 1, 0.01);
    }
    
    @Test
    public void backEject() {
        context.GetOICore().setOperatorJoyX(0);
        context.GetOICore().setOperatorJoyY(-1);
        worker.exec();
        assertEquals(front.getCollectorRollerMotor(), 1, 0.01);
        assertEquals(back.getCollectorRollerMotor(), -1, 0.01);
    }
}