#define SEL 2  // When the SEL pin is held low, the data will be
               //  routed to the PC via the USB-serial bridge.
               //  That port is also the port used for programming
               //  by the Arduino IDE. When in bootloading mode, a
               //  pull-down resistor on the SmartBasic causes it
               //  to remain in programming mode.

#define ARDUINO_IDE   LOW  // Constants to make our routing change
#define AUX_TERMINAL  HIGH //  more obvious. When the SEL pin is 
                           //  LOW, data is routed to the
                           //  programming port.

int led = 4;

void setup(){
  Serial.begin(9600);    // Set up the hardware serial port.

  pinMode(SEL, OUTPUT);    // Make the select line an output...
  digitalWrite(SEL, ARDUINO_IDE); // ...and connect the board to
                           //  the Arduino IDE's terminal. 
  
  pinMode(led, OUTPUT);
}

void loop(){
  Serial.flush();
  digitalWrite(SEL, ARDUINO_IDE);
  Serial.println("Hello world");
  
  digitalWrite(led, HIGH);
  delay(1000);
  digitalWrite(led, LOW);
  delay(1000);
}
