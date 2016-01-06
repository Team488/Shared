#include "LPD8806.h"
#include "SPI.h" // Comment out this line if using Trinket or Gemma
#ifdef __AVR_ATtiny85__
#include <avr/power.h>
#endif

// Example to control LPD8806-based RGB LED Modules in a strip

/*****************************************************************************/

const int sensorPinZ = A0;    // select the input pin for the accel
const int sensorPinX = A1;    // select the input pin for the accel
const int MinZvalue = 335;    // min value output from accel Z


// Number of RGB LEDs in strand:
const int nLEDs = 32;

// Chose 2 pins for output; can be any valid output pins:
const int dataPin  = 2;
const int clockPin = 3;

// First parameter is the number of LEDs in the strand.  The LED strips
// are 32 LEDs per meter but you can extend or cut the strip.  Next two
// parameters are SPI data and clock pins:
LPD8806 strip = LPD8806(nLEDs, dataPin, clockPin);
//LPD8806 strip = LPD8806(nLEDs);

// You can optionally use hardware SPI for faster writes, just leave out
// the data and clock pin parameters.  But this does limit use to very
// specific pins on the Arduino.  For "classic" Arduinos (Uno, Duemilanove,
// etc.), data = pin 11, clock = pin 13.  For Arduino Mega, data = pin 51,
// clock = pin 52.  For 32u4 Breakout Board+ and Teensy, data = pin B2,
// clock = pin B1.  For Leonardo, this can ONLY be done on the ICSP pins.
//LPD8806 strip = LPD8806(nLEDs);

// Variables

int outputValueZ, sensorValueZ; 
int outputValueX, sensorValueX;
unsigned int FilterVal = 31 << 5;
unsigned int PixNum, LastPixNum;
unsigned int scaledLED, LastScaledLED;

//function prototype
void knightRider(uint8_t r, uint8_t g, uint8_t b, uint8_t wait);


void setup() {
#if defined(__AVR_ATtiny85__) && (F_CPU == 16000000L)
  clock_prescale_set(clock_div_1); // Enable 16 MHz on Trinket
#endif

  // Start up the LED strip
  strip.begin();

  // Update the strip, to start they are all 'off'
  strip.show();
  delay(1);
}


void loop() {  
  sensorValueZ = analogRead(sensorPinZ); //read sensor Value of axis Z on accel.
  Serial.println(sensorValueZ);

  if (sensorValueZ < MinZvalue) sensorValueZ = MinZvalue; // check and set min value [335]
  if (sensorValueZ > (MinZvalue+63)) sensorValueZ = MinZvalue+63;  // check and set max value [390]

  sensorValueZ = (sensorValueZ - MinZvalue) >> 1;

  FilterVal = ((FilterVal<<3) - FilterVal + (sensorValueZ<<5))>>3;    // (7*OldVal + NewVal)/8 IIR running average in 11.5 fixed point

  PixNum = FilterVal >> 5;                //convert 11.5 into an int using Floor rounding
/*
  int xbotVal = PixNum >> 1; // just to get a nice number for my arrays

  const int upperLine[16] = {0, 0, 0, 0, 0, 0,  0, 28, 28, 28, 27, 26, 25, 24, 23, 21}; 
  const int lowerLine[16] = {0, 0, 0, 0, 0, 0, 12, 14, 14, 17, 17, 18, 19, 20, 21, 22};
*/
  int grid[30][30] = {     
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //0
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //1
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //2
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //3
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //4
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //5
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //6
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //7
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //8
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //9
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //10
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //11
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //12
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //13
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, 0}, //14
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //15
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //16
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //17
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //18
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //19
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //20
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, //21 
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,255,0,0,0,0,0,0,0,0,0,0,0,0,0,255}, //22
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,255,0,0,0,0,0,0,0,0,0,0,0,255,0}, //23
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,255,0,0,0,0,0,0,0,0,0,255,0,0}, //24
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,255,0,0,0,0,0,0,0,255,0,0,0}, //25
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,255,0,0,0,0,0,255,0,0,0,0}, //26
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,255,0,0,0,255,0,0,0,0,0}, //27
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,255,0,255,0,0,0,0,0,0}, //28
    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,255,0,0,0,0,0,0,0} // 29
    };

  int angle = PixNum;
  setStripColor(0);

/*  for(int light = 0; light < 31; light++)
  {
    strip.setPixelColor(light, grid[angle][light]);  
  }
  strip.show();
/*
  int topLine = upperLine[xbotVal];
  int lowLine = lowerLine[xbotVal];
  int last_topLine;
  int last_lowLine;
*/
//  Serial.println(xbotVal);
/*  
  if(xbotVal > 6){
   strip.setPixelColor(last_topLine, 0);
   strip.setPixelColor(topLine, 255);
   strip.setPixelColor(topLine + 1, 255);
   strip.setPixelColor(topLine + 2, 0);
   
   strip.setPixelColor(last_lowLine, 0);
   strip.setPixelColor(lowLine, 255);
   strip.setPixelColor(lowLine - 1, 255);
   strip.setPixelColor(lowLine - 2, 0);
   
   last_topLine = topLine;
   last_lowLine = lowLine;
   strip.show(); `
   }
  
 */
}

void setStripColor(uint32_t c){
  for (int i = 0; i < 30; i++){ //15-16 ints
    strip.setPixelColor(i, c);
    // strip.show();
  }
}

