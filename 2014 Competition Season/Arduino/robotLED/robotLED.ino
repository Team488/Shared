#include <FastLED.h>
#include <Adafruit_NeoPixel.h>


#define DISABLED 2
#define BLUE_ALLIANCE 3
#define RED_ALLIANCE 4
#define ARM_DISABLED 5
#define DATA_PIN 6
#define GIVE_BALL 7

#define NUM_LEDS 48
#define MIN_BRIGHT_PULSE 10
#define MAX_BRIGHT_PULSE 230

CRGB leds[NUM_LEDS];

int wheelR = 0;
int wheelG = 0;
int wheelB = 0;

Adafruit_NeoPixel strip = Adafruit_NeoPixel(48, DATA_PIN, NEO_GRB + NEO_KHZ400);

int backStart = 0;
int backEnd = 19;

int frontStart = 20;
int frontEnd = 39;

int catapultStart = 40;
int catapultEnd = 48;

// curent strips values
int start;
int count;
int thirdCount = count/3;

void setup()
{
  FastLED.addLeds<WS2811, DATA_PIN, RGB>(leds, NUM_LEDS);
  pinMode(RED_ALLIANCE, INPUT);
  pinMode(BLUE_ALLIANCE, INPUT);
  pinMode(ARM_DISABLED, INPUT);
  pinMode(DISABLED, INPUT);
  pinMode(GIVE_BALL, INPUT);
  
 
  strip.begin();
  strip.show();
}

void loop()
{
  setRobotLED();
}

void setRobotLED()
{
  boolean isRED = digitalRead(RED_ALLIANCE);
  boolean isBLUE = digitalRead(BLUE_ALLIANCE);
  boolean isDISABLED = digitalRead(DISABLED);
  boolean isARM_DISABLED = digitalRead(ARM_DISABLED);
  boolean isGIVE_BALL = digitalRead(GIVE_BALL);

  digitalWrite(DISABLED, LOW);
  digitalWrite(isBLUE, HIGH);
  digitalWrite(isRED, LOW);
  digitalWrite(isARM_DISABLED, LOW);
  digitalWrite(isGIVE_BALL, LOW);

  if(isRED)
  {
      setFrontContext();
      for(uint16_t i = 0; i < thirdCount; i++)
      {   
        setColor(250, 250 , 250, 0);
      }
      for(uint16_t i = thirdCount; i < count; i++)
      {   
        setColor(0,250,0,0);
      }
      
      setBackContext();
      for(uint16_t i = 0; i < thirdCount; i++)
      {
        setColor(250, 250 , 250,0);
      }
      for(uint16_t i = thirdCount; i < count; i++)
      {   
        setColor(0, 250 , 0, 0);
      }
  }
  
  else if(isBLUE)
  {
      setFrontContext();
      for(uint16_t i = 0; i < (count/3); i++)
      {   
        setAll(strip.Color(250, 250 , 250));
      }
      for(uint16_t i = (count/3); i < count; i++)
      {   
        setAll(strip.Color(0,0,255));
      }
      
      setBackContext();
      for(uint16_t i = 0; i < (count/3); i++)
      {
        setAll(strip.Color(250, 250 , 250));
      }
      for(uint16_t i = (count/3); i < count; i++)
      {   
        setAll(strip.Color(0,0,255));
      }
  }
  
 else if(isDISABLED)
  {
    setColor(250, 250, 250, 0);
  }
else  if(isARM_DISABLED)
  {
     rainbowCycle(2);
  }
   
 else if(isGIVE_BALL)
  {
    setFrontContext();
    clearAll();
    pulse();
    
    setBackContext();
    clearAll();
    pulse();
  }
  else{
    setColor(0,0,0,0);
  }
  
  strip.show();
}

void setColor(uint8_t r, uint8_t g, uint8_t b, uint8_t wait)
{
  for(uint16_t i = 0; i < NUM_LEDS; i++)
  {
    leds[i].red = r;
    leds[i].green = g;
    leds[i].blue = b;
    FastLED.show();
    delay(wait);
  }
}

void pulseBrightness(uint8_t wait)
{
  for(uint16_t i= 0; i < MAX_BRIGHT_PULSE; i++)
  {
    FastLED.setBrightness(i);
    FastLED.show();
    delay(wait);
  }

  for(uint16_t i = 255; i > MIN_BRIGHT_PULSE; i--)
  {
    FastLED.setBrightness(i);
    FastLED.show();
    delay(wait);
  }
}

// The colours are a transition r - g - b - back to r.
void Wheel(byte WheelPos) 
  {
  if(WheelPos < 85) {
    wheelR = WheelPos * 3; 
    wheelG = 255 - WheelPos * 3; 
    wheelB = 0;
  } 
  else if(WheelPos < 170)
  {
    WheelPos -= 85;
    wheelR = 255 - WheelPos * 3; 
    wheelG = 0; 
    wheelB = WheelPos * 3;
  } 
  else
  {
    WheelPos -= 170;
    wheelR = 0; 
    wheelG = WheelPos * 3; 
    wheelB = 255 - WheelPos * 3;
  }
}

void rainbowCycle(uint8_t wait)
{
  uint16_t i, j;

  for(j=0; j<256*5; j++)
  { // 5 cycles of all colors on wheel
    for(i=0; i< NUM_LEDS; i++)
    {
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

  for(j = 0; j < 256; j++)
  {
    for(i = 0; i < NUM_LEDS; i++)
    {
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
    for(i = 0; i < NUM_LEDS; i++)
    {
      Wheel(((i * 256 / NUM_LEDS) + j) & 255);
      leds[i].blue = wheelB;
    }
    FastLED.show();
    delay(wait);
  }
}

void redScroll(uint8_t wait)
{
  uint16_t i, j;
  for(i=0; j < 256 * 4; j++)
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

void setAll(uint32_t color)
{
  for(uint16_t i=0; i<count; i++)
  {
      setRelativeColor(i, color);
  }
}

void clearAll()
{
   for(uint16_t j=0; j<strip.numPixels(); j++)
   {
      strip.setPixelColor((j), strip.Color(0, 0, 0));
   }
}

void setFrontContext()
{
  start = frontStart;
  count = frontEnd - start + 1;  
}

void setBackContext()
{
  start = backStart;
  count = backEnd - backStart + 1;    
}

void setCatapultContext()
{
  start = catapultStart;
  count = catapultEnd - catapultStart + 1;
}

void setRelativeColor(int relIndex, uint32_t color)
{
  if(relIndex >= 0 && relIndex < count)
  {
    strip.setPixelColor(relIndex + start, color);
  }
}


void pulse()
{
  int mid = count / 2;
   for(uint16_t i=0; i < mid+1; i++)
   {
      setAll(strip.Color(0,0,0));
      uint32_t c1 = strip.Color(250, 0, 127);
      uint32_t c2 = strip.Color(250, 0, 50);
     
      setRelativeColor((mid + i), c1);
      setRelativeColor((mid + i+1), c2);
      setRelativeColor((mid - i), c1);
      setRelativeColor((mid - (i+1)),c2);
      strip.show();
      delay(30);
  } 
}


