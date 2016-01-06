#include <SoftwareSerial.h>


int redPin = 3;
int grnPin = 5;
int bluPin = 6;

int sigPins[] = {8, 9};

void setBrightness(int redLevel, int grnLevel, int bluLevel)
{
  /*Serial.println(String(redLevel));
  Serial.println(String(grnLevel));
  Serial.println(String(bluLevel));
  Serial.println("FOO --------------\r\n");*/
  analogWrite(redPin, redLevel); 
  analogWrite(grnPin, grnLevel);
  analogWrite(bluPin, bluLevel);
}

void setup() {
  Serial.begin(57600);
  pinMode(redPin, OUTPUT);
  pinMode(grnPin, OUTPUT);
  pinMode(bluPin, OUTPUT);
  
  for(int i = 0; i < (sizeof(sigPins) / sizeof(int)); i++)
  {
    pinMode(sigPins[i], INPUT);
  }
}

void processToteSensor()
{
  byte signal = 0;
  
  for(int i = 0; i < (sizeof(sigPins) / sizeof(int)); i++)
  {
    int pinVal = digitalRead(sigPins[i]) != 0 ? 1 : 0;
    signal |= pinVal << i;
  }
  Serial.println(signal);
  switch(signal)
  {
    case 1:
      setBrightness(0, 0, 128);
      break;
    case 2:
      setBrightness(0, 128, 128);
      break;
    case 3:
      setBrightness(0, 128, 0);
      break;
    default:
      setBrightness(0, 0, 0);
  }
}

void loop() {  
  processToteSensor();   
}



