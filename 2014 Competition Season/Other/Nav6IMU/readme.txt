The nav6 project contains the following components:

arduino:

- As the nav6 behaves almost exactly like an Arduino UNO Rev 3 board, it can be programmed via the Arduino IDE, freely available at www.arduino.cc.  This directory contains the source code that can be built and downloaded to the nav6 via the Arduino IDE.

NOTE:  To interface to the nav6 from a computer without a RS-232 serial port, you can use an inexpensive USB-to-RS-232 converter cable such as found at http://www.iogear.com/product/GUC232A/.

c++:

- This directory contains code compatible with the 2014 version of the C++ WPI Libraries, which runs in on the FIRST robotics cRio platform.  This code allows cRio-hosted code to acquire yaw/pitch/roll values from the board in real-time.

Usage Note:  As currently implemented, the nav6 must be held still for about 20 seconds after it is first powered on, during which time the nav6 is calibrating.  After that point, the yaw, pitch and roll values can be used.  This works out well for FIRST robotics competition matches, as the robot is turned on at least 20 seconds before each match starts.

java:

- This directory contains code compatible with the 2014 version of the Java WPI Libraries, which runs in on the FIRST robotics cRio platform.  This code allows cRio-hosted code to acquire yaw/pitch/roll values from the board in real-time.

Usage Note:  As currently implemented, the nav6 must be held still for about 20 seconds after it is first powered on, during which time the nav6 is calibrating.  After that point, the yaw, pitch and roll values can be used.  This works out well for FIRST robotics competition matches, as the robot is turned on at least 20 seconds before each match starts.

labview:

- This directory contains code compatible with the 2014 version of the LabView VI Libraries, which runs in on the FIRST robotics cRio platform.  This code allows cRio-hosted code to acquire yaw/pitch/roll values from the board in real-time.

Usage Note:  As currently implemented, the nav6 must be held still for about 20 seconds after it is first powered on, during which time the nav6 is calibrating.  After that point, the yaw, pitch and roll values can be used.  This works out well for FIRST robotics competition matches, as the robot is turned on at least 20 seconds before each match starts.

docs:

- This directory contains various and sundry documentation files for third party components used in the nav6 IMU.

enclosure:

- This directory contains design files for a 3d-printed enclosure for the nav6 IMU.

processing:

- This directory contains utility applications developed in the Processing 2.0 language - including the nav6UI demonstration application.  These apps are cross-platform and can be compiled by downloading the open source Processing 2.0 development environment.
