/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.diagnostics;

/**
 * This interface defines a Diagnostic Test. These tests are designed to allow for rapid
 * robot debugging in the pit to make sure it's behaving as expected.
 * @author John
 */
public interface IDiagnosticTest {
    
    /**
     * The name/instructions for the Diagnostic Test.
     * @return
     */
    public String getDiagnosticTestName();
    
    /**
     * Any rapidly-updating test information (value of a switch or a gyro).
     * @return
     */
    public String getCurrentTestInformation();
    
    /**
     * Run the test.
     */
    public void executeTest();
    
    /**
     * Cleanup after the operator ends the test.
     */
    public void cleanup();
    
}
