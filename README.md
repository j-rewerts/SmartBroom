# SmartBroom
An application that allows users to track their sweeping in the fine, fine sport of Curling.

What is curling? It's a sport that involves a broom, some stones, and a sheet of ice. Sweepers will
sweep the ice in front of a thrown stone to try to direct it to a location on the ice sheet. This
project uses some hardware to detect the frequency of the broom's oscillation, and the pressure that
the sweeper is using on the ice, then broadcasts it to an Android phone.

This code contains two primary components:
* `Firmware` contains code that runs on an arduino, to talk to a MelodySmart bluetooth module.
* `Android` contains code for a simple android app that talks to a MelodySmart bluetooth module.

Due to time constraints for demoing the project, the firmware will simply repeatedly broadcast its data
to the device that connects to it. The android app will display this data in the form of two bar graphs.

This project was built as part of a 24-hour hackathon (hence the time constraints above) in January 2015 
from the University of Alberta Computer Engineering Club. For more information see here: http://hackathon.compeclub.com/
