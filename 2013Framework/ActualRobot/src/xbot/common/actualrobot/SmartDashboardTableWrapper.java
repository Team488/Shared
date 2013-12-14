/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xbot.common.actualrobot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;
import xbot.common.ITableProxy;

/**
 *
 * @author Alex
 */
public class SmartDashboardTableWrapper implements ITableProxy {

    public void setDouble(String key, double value) {
        SmartDashboard.putNumber(key, value);
    }

    public Double getDouble(String key) {
        try {
            return Double.valueOf(SmartDashboard.getNumber(key));
        } catch (TableKeyNotDefinedException e) {
            return null;
        }
    }
    
}
