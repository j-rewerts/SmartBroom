#define ACCEL_X 0
#define ACCEL_Y 1
#define ACCEL_Z 2

#define PRESS_1 3
#define PRESS_2 4
#define PRESS_3 5
#define PRESS_4 6

long lastADCRead;
long lastDump;
int arrayIndex = 0;

int accelXs[50];
int accelYs[50];
int accelZs[50];
int accelXPtr;

void setup()
{
  Serial.begin(9600);
  lastADCRead = millis();
  lastDump = millis();
  accelXPtr = 0;
}

void loop()
{
  /*
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
  */
  
  accelXs[accelXPtr] = analogRead(ACCEL_X);
  accelXPtr++;
  
  if(accelXPtr >= 50)
  {
    String dump = "";
    for(int i = 0; i < 50; i++)
    {
      dump += String(accelXs[i]) + " ";
    }
    //Serial.println(dump);
    accelXPtr = 0;
    
    
    Serial.println(bufferFreq());
  }
  
  
  delay(20);
}

int bufferFreq()
{
  int freq = 0;
  
  //get average
  long sum = 0;
  for(int i = 0; i < 50; i++)
  {
    sum += accelXs[i];
  }
  int ave = sum / 50;
  
  int prevprev = accelXs[0];
  int prev = accelXs[1];
  int lowVal = ave;
  int highVal = ave;
  int curr;
  for(int i = 2; i < 50; i++)
  {
    curr = accelXs[i];
    
    if(prev < prevprev && prev < curr)
    {
      lowVal = prev;
      if(highVal - lowVal > 20)
      {
        freq += 5;
      }
    }
    else if(prev > prevprev && prev > curr)
    {
      highVal = prev;
      if(highVal - lowVal > 20)
      {
        freq += 5;
      }
    }
    prevprev = prev;
    prev = curr;
  }
  return freq;
}
