#include <FastLED.h>
#include <Adafruit_NeoPixel.h>

#define BALL_DETECTED 1
#define DISABLED 2
#define BLUE_ALLIANCE 3
#define RED_ALLIANCE 4
#define COCKED 7
#define RED_CATA 8
#define BLUE_CATA 9

#define DATA_PIN 6

#define NUM_LEDS 48
#define MIN_BRIGHT_PULSE 10
#define MAX_BRIGHT_PULSE 230

CRGB leds[NUM_LEDS];

int wheelR = 0;
int wheelG = 0;
int wheelB = 0;

int backStart = 0;
int backMid = 10;
int backEnd = 19;

int frontStart = 20;
int frontMid = 30;
int frontEnd = 39;

int catapultStart = 40;
int catapultEnd = 48;

int start;
int count;

Adafruit_NeoPixel strip = Adafruit_NeoPixel(49, DATA_PIN, NEO_GRB + NEO_KHZ400);

void setup()
{
  FastLED.addLeds<WS2811, DATA_PIN, RGB>(leds, NUM_LEDS);
  pinMode(RED_ALLIANCE, INPUT);
  pinMode(BLUE_ALLIANCE, INPUT);
  pinMode(BALL_DETECTED, INPUT);
  pinMode(DISABLED, INPUT);
  pinMode(COCKED, INPUT);
  pinMode(RED_CATA, INPUT);
  pinMode(BLUE_CATA, INPUT);
  strip.begin();
  Serial.begin(9600);
  strip.show();
 
}

void loop()
{
  //if these pins detect a volt of 3 or higher it will return true (HIGH =>  <3V)
  boolean isRED = digitalRead(RED_ALLIANCE) == HIGH;
  boolean isBLUE = digitalRead(BLUE_ALLIANCE) == HIGH;
  boolean isBALLDETECTED = digitalRead(BALL_DETECTED) == HIGH;
  boolean isDISABLED = digitalRead(DISABLED) == HIGH;
  boolean isCOCKED = digitalRead(COCKED) == HIGH;

  boolean wantRedCatapultDIR = digitalRead(RED_CATA) == HIGH;
  boolean wantBlueCatapultDIR = digitalRead(BLUE_CATA) == HIGH;


  
  //setDigitalWrite(wantBlueCatapultDIR, HIGH);

  setDigitalWrite(isRED, HIGH);
  setDigitalWrite(wantRedCatapultDIR, HIGH);
  delay(3000);
  
  setDigitalWrite(isBLUE, HIGH);
  setDigitalWrite(wantBlueCatapultDIR, HIGH);
  delay(3000);
  
//  setDigitalWrite(isBLUE, HIGH);
//  setDigitalWrite(isBLUE, LOW);
//  
// 
//  
//  setDigitalWrite(isBALLDETECTED, HIGH);
//  setDigitalWrite(isBALLDETECTED, LOW);
//
//  setDigitalWrite(isDISABLED, HIGH);
//  setDigitalWrite(isDISABLED, LOW);
//  
//  setDigitalWrite(isCOCKED, HIGH);
//  setDigitalWrite(isCOCKED, LOW);
    
 //green is red
 //blue is green
 //red is blue
 //grb
    
  if(isRED)
  {
    
    if(wantRedCatapultDIR)
    {
      setFrontContext();
      for(uint16_t i=0; i<(count/3); i++) {
      setColor(0, 0, 0, 0);
      }
      for(uint16_t i=(count/3); i<count; i++) {
      setColor(0, 250, 0, 0);
      }
      
      setBackContext();
      for(uint16_t i=0; i<(count/3); i++) {
      setColor(0, 0, 0, 0);
      }
      for(uint16_t i=(count/3); i<count; i++) {
      setColor(0, 250, 0, 0);
      }
    }
    else
    {
      setColor(0, 250 , 0, 0);
    }
  }
  
  if(isBLUE)
  {
    if(wantBlueCatapultDIR){
      setFrontContext();
      for(uint16_t i=0; i<(count/3); i++) {
      setColor(0, 0, 0, 0);
      }
      for(uint16_t i=(count/3); i<count; i++) {
      setColor(0, 0, 250, 0);
      }
      
      setBackContext();
      for(uint16_t i=0; i<(count/3); i++) {
      setColor(0, 0, 0, 0);
      }
      for(uint16_t i=(count/3); i<count; i++) {
      setColor(0, 0, 250, 0);
      }
    }
    else
    {
      setColor(0,0,255, 0);
    }
  }
  
  if(isBALLDETECTED)
  {
   setColor(0,255,0, 2000);
  }
  
  if(isDISABLED)
  {
    rainbowCycle(1);
  }
  
  if(isCOCKED)
  {
    setColor(255, 255, 0, 2000);
    
  }
   

  

}


void setDigitalWrite(boolean BoolName, boolean PinValue)
{
  digitalWrite(BoolName, PinValue);
}

void setColor(uint8_t r, uint8_t g, uint8_t b, uint8_t wait)
{
  for(uint16_t i = 0; i < NUM_LEDS; i++) {
    leds[i].red = r;
    leds[i].green = g;
    leds[i].blue = b;
    FastLED.show();
    delay(wait);
  }
}



void pulseBrightness(uint8_t wait)
{
  for(uint16_t i= 0; i < MAX_BRIGHT_PULSE; i--) {
    FastLED.setBrightness(i);
    FastLED.show();
    delay(wait);
  }

  for(uint16_t i = 255; i > MIN_BRIGHT_PULSE; i++) {
    FastLED.setBrightness(i);
    FastLED.show();
    delay(wait);
  }
}

// The colours are a transition r - g - b - back to r.
void Wheel(byte WheelPos) {
  if(WheelPos < 85) {
    wheelR = WheelPos * 3; 
    wheelG = 255 - WheelPos * 3; 
    wheelB = 0;
  } 
  else if(WheelPos < 170) {
    WheelPos -= 85;
    wheelR = 255 - WheelPos * 3; 
    wheelG = 0; 
    wheelB = WheelPos * 3;
  } 
  else {
    WheelPos -= 170;
    wheelR = 0; 
    wheelG = WheelPos * 3; 
    wheelB = 255 - WheelPos * 3;
  }
}

void rainbowCycle(uint8_t wait)
{
  uint16_t i, j;

  for(j=0; j<256*5; j++) { // 5 cycles of all colors on wheel
    for(i=0; i< NUM_LEDS; i++) {
      Wheel(((i * 256 / NUM_LEDS) + j) & 255);
      leds[i].red = wheelR;
      leds[i].green = wheelG;
      leds[i].blue = wheelB;
    }
    FastLED.show();
    delay(wait);
  }
}

void rainbow(uint8_t wait)
{
  uint16_t i, j;

  for(j = 0; j < 256; j++) {
    for(i = 0; i < frontEnd; i++) {
      Wheel((i+j) & 255);
      leds[i].red = wheelR;
      leds[i].green = wheelG;
      leds[i].blue = wheelB;
    } 
    FastLED.show();
    delay(wait);
  }
}

void blueScroll(uint8_t wait)
{
  uint16_t i, j;
  for(i=0; j < 256 * 4; j++)
  {
    for(i = 0; i < frontEnd; i++)
    {
      Wheel(((i * 256 / frontEnd) + j) & 255);
      leds[i].blue = wheelB;
    }
    FastLED.show();
    delay(wait);
  }
}

void redScroll(uint8_t wait)
{
  uint16_t i, j;
  for(i=0; j < 256 * 4; j+4)
  {
    for(i = 0; i < NUM_LEDS; i+4)
    {
      Wheel(((i * 256 / NUM_LEDS) + j) & 255);
      leds[i].red = wheelR;
    }
    FastLED.show();
    delay(wait);
  }
}

void greenScroll(uint8_t wait)
{
  uint16_t i, j;
  for(i=0; j < 256 * 4; j++)
  {
    for(i = 0; i < NUM_LEDS; i++)
    {
      Wheel(((i * 256 / NUM_LEDS) + j) & 255);
      leds[i].green = wheelG;
    }
    FastLED.show();
    delay(wait);
  }
}


///////////////////////////////////////
///////////////////////////////////////


void blueScrollB(uint8_t wait)
{
  uint16_t i, j;
  for(i=0; j < 256 * 4; j--)
  {
    for(i = 0; i < NUM_LEDS; i++)
    {
      Wheel(((i * 256 / NUM_LEDS) + j) & 255);
      leds[i].blue = wheelB;
    }
    FastLED.show();
    delay(wait);
  }
}

void redScrollB(uint8_t wait)
{
  uint16_t i, j;
  for(i=0; j < 256 * 4; j--)
  {
    for(i = 0; i < NUM_LEDS; i++)
    {
      Wheel(((i * 256 / NUM_LEDS) + j) & 255);
      leds[i].red = wheelR;
    }
    FastLED.show();
    delay(wait);
  }
}

void greenScrollB(uint8_t wait)
{
  uint16_t i, j;
  for(i=0; j < 256 * 4; j--)
  {
    for(i = 0; i < NUM_LEDS; i++)
    {
      Wheel(((i * 256 / NUM_LEDS) + j) & 255);
      leds[i].green = wheelG;
    }
    FastLED.show();
    delay(wait);
  }
}

void setAll(uint32_t color) {
  for(uint16_t i=0; i<count; i++) {
      setRelativeColor(i, color);
  }
}


void setFrontContext() {
  start = frontStart;
  count = frontEnd - frontStart;
}

void setBackContext() {
  start = backStart;
  count = backEnd - backStart;    
}

void setCatapultContext() {
  start = catapultStart;
  count = catapultEnd - catapultStart;
}

void setRelativeColor(int relIndex, uint32_t color) {
   strip.setPixelColor(relIndex + start, color);
}



