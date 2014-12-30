/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.aerialassist.diagnostics;

/**
 * Diagnostic test that runs the compressor (or at least tries to, since if pressure
 * is high it may refuse due to safety).
 * @author Maya
 */
public class CompressorTest extends DiagnosticTest {

    /**
     *
     * @return
     */
    public String getDiagnosticTestName() {
       return "Running compressor!";
    }

    /**
     *
     */
    public void executeTest() {
       robot().GetPneumaticCore().TurnOnCompressor();
    }

    /**
     *
     */
    public void cleanup() {
       robot().GetPneumaticCore().TurnOffCompressor();
    }

    public String getCurrentTestInformation() {
        return "";
    }
    
}
