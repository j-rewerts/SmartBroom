#include <SparkFun_BLEMate2.h>

/****************************************************
This code is the main controller for the BEDHOOD curling
broom.  It interfaces with the pressure sensors, accelerometer,
on board memory, and BLE radio.  Developed during the
u of a computer engineering hackathon

This uses the SparkFun Electronics BLE library developed by
Mike Hord.  If he ever finds himself in Edmonton Alberta I
will buy him a nice molson canadian.

January 31st 2014 - Jacob Ortt
***************************************************/

#include <SparkFun_BLEMated2.h>

BLEMate2 BTModu(&Serial);

/*both these will go away in final design*/
#define LED 4  //test LED pin is 4
#define MUX_SELECT 2 //select for serial mux on pin 2

int leds_recieved = 0;

void setup()
{
  pinMode(MUX_SELECT, OUTPUT);
  pinMode(LED, OUTPUT);
  
  Serial.begin(9600);
  selectBLE();
  
  // Regarding function return values: most functions that interact with the
  //  BC118 will return BLEMate2::opResult values. The possible values here
  //  are:
  //  REMOTE_ERROR - No remote devices exist.
  //  INVALID_PARAM - You've called the function with an invalid parameter.
  //  TIMEOUT_ERROR - The BC118 failed to respond to the command in a timely
  //                   manner; timely is redefined for each command.
  //  MODULE_ERROR - The BC118 didn't like the command string it received.
  //                  This will probably only occur when you attempt to send
  //                  commands and parameters outside the built-ins. 
  //  SUCCESS - What it says.
  
  // Reset is a blocking function which gives the BC118 a few seconds to reset.
  //  After a reset, the module will return to whatever settings are in
  //  non-volatile memory. One other *super* important thing it does is issue
  //  the "SCN OFF" command after the reset is completed. Why is this important?
  //  Because if the device is in central mode, it *will* be scanning on reset.
  //  No way to change that. The text traffic generated by the scanning will
  //  interfere with the firmware on the Arduino properly identifying response
  //  strings from the BC118.
  if (BTModu.reset() != BLEMate2::SUCCESS)
  {
    selectPC();
    Serial.println("Module reset error!");
    while (1);
  }

  // restore() resets the module to factory defaults; you'll need to perform
  //  a writeConfig() and reset() to make those settings take effect. We don't
  //  do that automatically because there may be things the user wants to
  //  change before committing the settings to non-volatile memory and
  //  resetting.
  if (BTModu.restore() != BLEMate2::SUCCESS)
  {
    selectPC();
    Serial.println("Module restore error!");
    while (1);
  }
  // writeConfig() stores the current settings in non-volatile memory, so they
  //  will be in place on the next reboot of the module. Note that some, but
  //  not all, settings changes require a reboot. It's probably in general best
  //  to write/reset when changing anything.
  if (BTModu.writeConfig() != BLEMate2::SUCCESS)
  {
    selectPC();
    Serial.println("Module write config error!");
    while (1);
  }
  // One more reset, to make the changes take effect.
  if (BTModu.reset() != BLEMate2::SUCCESS)
  {
    selectPC();
    Serial.println("Second module reset error!");
    while (1);
  }
  selectBLE();
  
  setupPeripheral();
  
  //check mode
  boolean inCentralMode = false;
  BTModu.amCentral(inCentralMode); 
  
  if(inCentralMode){
    selectPC();
    Serial.println("Is Central Mode");
    selectBLE();
  }
  else{
    selectPC();
    Serial.println("Is Peripheral Mode");
    selectBLE();
  }
}

void loop()
{

  // Since I'm going to be reporting strings back over serial to the PC, I want
  //  to make sure that I'm (probably) not going to be looking away from the BLE
  //  device during a data receive period. I'll *guess* that, if more than 1000
  //  milliseconds has elapsed since my last receive, that I'm in a quiet zone
  //  and I can switch over to the PC to report what I've heard.
  static String fullBuffer = "";
  static long lastRXTime = millis();
  static long lastADCRead = millis();
  if (lastRXTime + 1000 < millis())
  {
    if (fullBuffer != "")
    {
      selectPC();
      Serial.println(fullBuffer);
      selectBLE();
      fullBuffer = "";
      digitalWrite(LED, LOW);
    }
  }
  static String inputBuffer;
    // This is the peripheral example code.

    // When a remote module connects to us, we'll start to see a bunch of stuff.
    //  Most of that is just overhead; we don't really care about it. All we
    //  *really* care about is data, and data looks like this:
    // RCV=20 char max msg\n\r

    // The state machine for capturing that can be pretty easy: when we've read
    //  in \n\r, check to see if the string began with "RCV=". If yes, do
    //  something. If no, discard it.
  while (Serial.available() > 0)
  {
    inputBuffer.concat((char)Serial.read());
    lastRXTime = millis();
  }
  // We'll probably see a lot of lines that end with \n\r- that's the default
  //  line ending for all the connect info messages, for instance. We can
  //  ignore all of them that don't start with "RCV=". Remember to clear your
  //  String object after you find \n\r!!!
  int endLoc = inputBuffer.indexOf("\n\r");
  if (endLoc != -1)
  {
    /*
    inputBuffer.trim();
    fullBuffer += "in-" + inputBuffer;
    String possibleCommand = inputBuffer.substring(0, endLoc);
    fullBuffer += "pos-" + possibleCommand;
    */
    
    if (inputBuffer.startsWith("RCV="))
    {
      inputBuffer.trim(); // Remove \n\r from end.
      inputBuffer.remove(0,4); // Remove RCV= from front.
      fullBuffer += inputBuffer;
      
      
      if(inputBuffer.equals("LED ON")){
        leds_recieved++;
        digitalWrite(LED, HIGH);
        BTModu.sendData("Sure thing boss - " + String(leds_recieved));
      }
      inputBuffer = "";
    }
    else
    {
      inputBuffer = "";
    }
    
    //inputBuffer = "";
    //possibleCommand = "";
  }
  
  
}

void setupPeripheral()
{
  boolean inCentralMode = false;
  // A word here on amCentral: amCentral's parameter is passed by reference, so
  //  the answer to the question "am I in central mode" is handed back as the
  //  value in the boolean passed to it when it is called. The reason for this
  //  is the allow the user to check the return value and determine if a module
  //  error occurred: should I trust the answer or is there something larger
  //  wrong than merely being in the wrong mode?
  BTModu.amCentral(inCentralMode); 
  if (inCentralMode)
  {
    BTModu.BLEPeripheral();
    BTModu.BLEAdvertise();
  }

  // There are a few more advance settings we'll probably, but not definitely,
  //  want to tweak before we reset the device.

  // The CCON parameter will enable advertising immediately after a disconnect.
  BTModu.stdSetParam("CCON", "ON");
  // The ADVP parameter controls the advertising rate. Can be FAST or SLOW.
  BTModu.stdSetParam("ADVP", "FAST");
  // The ADVT parameter controls the timeout before advertising stops. Can be
  //  0 (for never) to 4260 (71min); integer value, in seconds.
  BTModu.stdSetParam("ADVT", "0");
  // The ADDR parameter controls the devices we'll allow to connect to us.
  //  All zeroes is "anyone".
  BTModu.stdSetParam("ADDR", "000000000000");

  BTModu.writeConfig();
  BTModu.reset();
  
  // We're set up to allow anything to connect to us now.
}

//funtions to change where serial port is directed
void selectPC()
{
  Serial.flush();
  digitalWrite(MUX_SELECT, LOW);
}

void selectBLE()
{
  Serial.flush();
  digitalWrite(MUX_SELECT, HIGH);
}
