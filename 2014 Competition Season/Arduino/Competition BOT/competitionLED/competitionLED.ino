#include <SoftwareSerial.h>
#include <Adafruit_NeoPixel.h>
#include <FastLED.h>



//3 - 4 - 5

//3 -> 13 - 3
//4 -> 12 - 2
//5 -> 11 - 1

#define stateTwoPIN 3
#define stateOnePIN 4
#define RED_ALLIANCE 5
#define PIN 6

boolean isRED = false;
int state;

#define NUM_LEDS 48
#define MIN_BRIGHT_PULSE 10
#define MAX_BRIGHT_PULSE 230

CRGB leds[NUM_LEDS];

boolean stateOne;
int wheelR = 0;
int wheelG = 0;
int wheelB = 0;

Adafruit_NeoPixel strip = Adafruit_NeoPixel(NUM_LEDS, PIN, NEO_GRB + NEO_KHZ400);

int backStart = 0;
int backEnd = 19;

int frontStart = 20;
int frontEnd = 39;

int catapultStart = 40;
int catapultEnd = 48;

// curent strips values
int start;
int count;
int numPixels;

void setup()
{
  FastLED.addLeds<WS2811, PIN, RGB>(leds, NUM_LEDS);
  pinMode(RED_ALLIANCE, INPUT);
  pinMode(stateOnePIN, INPUT);
  pinMode(stateTwoPIN, INPUT);
   
  strip.begin();
  Serial.begin(9600);
  strip.show();
}

void loop()
{
     readInputs();
     setLEDState();
     strip.show();
}

////////////////////////////////////////////////////////////////////////////////
//////////////////| FUNCTIONS |////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////


void setLEDState()
{   
    stateOne = digitalRead(stateOnePIN);
    switch(state){     
    case 0:
    if(isRED){
      setCatapultRed();
    }else{
      setCatapultBlue();
    }
    break;
    
    case 1:
    if(stateOne != false){
    setFrontContext();
    rainbowCycle(2);
    setBackContext();
    rainbowCycle(2);
    }else{
      return;
    }
    break;
      
    case 2:  
    setFrontContext();
    pulse();
    setBackContext();
    pulse();
    break;
      
    case 3:
     setFrontContext();
     for(uint16_t i = 0; i < (count/3); i++){
     setRelativeColor(i, strip.Color(250, 250, 250));
     }
     for(uint16_t i = (count/3); i < count; i++){
     setRelativeColor(i, strip.Color(255, 0, 0));
     }
     setBackContext();
     for(uint16_t i = 0; i < 2 * (count/3); i++){
          setRelativeColor(i, strip.Color(0, 250, 0));

     }
     for(uint16_t i = 2 * (count/3); i < count; i++){
          setRelativeColor(i, strip.Color(250, 0, 250));
     }
       
    break;
    }
}

void readInputs()
{
  isRED = digitalRead(RED_ALLIANCE);
  boolean stateOne = digitalRead(stateOnePIN);
  boolean stateTwo = digitalRead(stateTwoPIN);
  state = 0;
  
  if(stateOne)
  {
    state += 1;
  }

  if(stateTwo)
  {
    state += 2;
  }
}

void setCatapultRed()
{
     setFrontContext();
     for(uint16_t i = 0; i < (count/3); i++)
     {
     setRelativeColor(i, strip.Color(255, 255, 255)); //white
     }
     for(uint16_t i = (count/3); i < 2*(count/3); i++)
     {
       numPixels = (2 * (count/3)) - (count/3);
       setRelativeColor(i, strip.Color(255, 0, 0)); //want this to be knight rider chase
      // knightRider(3, 32, 5, 0xFF0000);
     }
     for(uint16_t i = 2 * (count/3); i < count; i++)
     {
     setRelativeColor(i, strip.Color(255, 0, 0)); //red
     }
     
     setBackContext();
     for(uint16_t i = 0; i < (count/3); i++)
     {
          setRelativeColor(i, strip.Color(255, 0, 0)); //red
     }
     for(uint16_t i = (count/3); i < 2 * (count/3); i++)
     {
          numPixels = (2 * (count/3)) - (count/3);
          setRelativeColor(i, strip.Color(255, 0, 0));
        //  knightRider(3, 32, 5, 0xFF0000);
     }
     for(uint16_t i = 2 * (count/3); i < count; i++)
     {
          setRelativeColor(i, strip.Color(255, 255, 255)); //white
     }
}

void setCatapultBlue()
{
  setFrontContext();
     for(uint16_t i = 0; i < (count/3); i++){
     setRelativeColor(i, strip.Color(250, 0, 255)); // blue green
     }
     for(uint16_t i = (count/3); i < 2* (count/3); i++){
     numPixels= (2 * (count/3)) - (count/3);
     setRelativeColor(i, strip.Color(0, 255, 0)); // KR BLUE CHASE
      // knightRider(3, 32, 2, 0x0000FF); // Cycles, Speed, Width, RGB Color (blue)
     }
     for(uint16_t i = 2 * (count/3); i < count; i++){
     setRelativeColor(i, strip.Color(0, 255, 0)); //blue
     }
     
     setBackContext();
     for(uint16_t i = 0; i < (count/3); i++){
        setRelativeColor(i, strip.Color(0, 255, 0));
     }
     for(uint16_t i = (count/3); i < 2 * (count/3); i++){
        numPixels = (2 * (count/3)) - (count/3); 
        setRelativeColor(i, strip.Color(0, 255, 0)); // KRBLUECHASE
       // knightRider(3, 32, 2, 0x0000FF); // Cycles, Speed, Width, RGB Color (blue)
     }
     for(uint16_t i = 2 * (count/3); i < count; i++){
        setRelativeColor(i, strip.Color(250, 0, 250)); //blue green
     }
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

void WheelBrightness(byte WheelPos) 
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
      leds[+i].green = wheelG;
      leds[i].blue = wheelB;
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
      setRelativeColor((mid + i+2), c2);
      
      setRelativeColor((mid - i), c1);
      setRelativeColor((mid - (i+1)), c2);
      setRelativeColor((mid - (i+2)), c2);
      
      strip.show();
      delay(30);
  } 
}

void knightRider(uint16_t cycles, uint16_t freq, uint8_t width, uint32_t color) {
  uint32_t old_val[numPixels]; // up to 256 lights!
  // Larson time baby!
  for(int i = 0; i < cycles; i++){
    for (int count = 1; count<numPixels; count++) {
      strip.setPixelColor(count, color); strip.show();
      delay(freq);
      old_val[count] = color;
      for(int x = count; x>0; x--) {
        old_val[x-1] = dimColor(old_val[x-1], width);
        strip.setPixelColor(x-1, old_val[x-1]); strip.show();
      }
    }
    for (int count = numPixels-1; count>=0; count--) {
      strip.setPixelColor(count, color); strip.show();
      delay(freq);
      old_val[count] = color;
      for(int x = count; x<=numPixels;x++) {
        old_val[x-1] = dimColor(old_val[x-1], width);
        strip.setPixelColor(x+1, old_val[x+1]); strip.show();
      }
    }
  }
}

uint32_t dimColor(uint32_t color, uint8_t width) {
   return (((color&0xFF0000)/width)&0xFF0000) + (((color&0x00FF00)/width)&0x00FF00) + (((color&0x0000FF)/width)&0x0000FF);
}
