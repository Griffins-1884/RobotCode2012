/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.templates.Target;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author michaelcw
 */
class CameraThread extends TimerTask {

	Thread runner;
        AxisCamera cam;
        //TrackerDashboard trackerDashboard;
        double kScoreThreshold = .01;

        Gyro gyro;

	public CameraThread(String threadName, AxisCamera myCam/*, TrackerDashboard myTrackerDashboard*/, Gyro myGyro) {
            runner = new Thread(this, threadName);
            cam = myCam;
            //trackerDashboard = myTrackerDashboard;
            gyro = myGyro;

            // Camera code!
            

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(this, 5000, 500);

            System.out.println(runner.getName() + " created");
	}
        
	public void run() {

            try {
                handleCameraShowing();
            } catch (NIVisionException ex) {
                ex.printStackTrace();
            } catch (AxisCameraException ex) {
                ex.printStackTrace();
            }
	}

        public void handleCameraShowing() throws NIVisionException, AxisCameraException
        {
         
             /*ColorImage image = null;
             if (cam.freshImage()) {// && turnController.onTarget()) {
                   image = cam.getImage();
                   Target[] targets = Target.findCircularTargets(image);
                   if (targets.length == 0 || targets[0].m_score < kScoreThreshold) {
                        System.out.println("No target found");
                        Target[] newTargets = new Target[targets.length + 1];
                        newTargets[0] = new Target();
                        newTargets[0].m_majorRadius = 0;
                        newTargets[0].m_minorRadius = 0;
                        newTargets[0].m_score = 0;
                        for (int i = 0; i < targets.length; i++) {
                             newTargets[i + 1] = targets[i];
                        }
                        trackerDashboard.updateVisionDashboard(0.0, gyro.getAngle(), 0.0, 0.0, newTargets);
                   } else {
                       System.out.println(targets[0]);
                       System.out.println("Target Angle: " + targets[0].getHorizontalAngle());
                       trackerDashboard.updateVisionDashboard(0.0, gyro.getAngle(), 0.0, targets[0].m_xPos / targets[0].m_xMax, targets);
                   }

            }*/
        }
}

