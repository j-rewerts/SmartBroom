#define ACCEL_X 0
#define ACCEL_Y 1
#define ACCEL_Z 2

#define PRESS_1 3
#define PRESS_2 4
#define PRESS_3 5
#define PRESS_4 6

long lastADCRead;

void setup()
{
  Serial.begin(9600);
  lastADCRead = millis();
}

void loop()
{
  if(lastADCRead + 1000 < millis()){
    String values = "";
    values += "AX-" + String(analogRead(ACCEL_X));
    values += " AY-" + String(analogRead(ACCEL_Y));
    values += " AZ-" + String(analogRead(ACCEL_Z));
    values += " P1-" + String(analogRead(PRESS_1));
    values += " P2-" + String(analogRead(PRESS_2));
    values += " P3-" + String(analogRead(PRESS_3));
    values += " P4-" + String(analogRead(PRESS_4));
    
    Serial.println(values);
    lastADCRead = millis();
  }
  
  delay(10);
}
