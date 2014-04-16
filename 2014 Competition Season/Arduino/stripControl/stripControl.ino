#include <Adafruit_NeoPixel.h>

#define PIN 6

Adafruit_NeoPixel strip = Adafruit_NeoPixel(49, PIN, NEO_GRB + NEO_KHZ400);

int backStart = 0;
int backEnd = 19;

int frontStart = 20;
int frontEnd = 39;

int catapultStart = 40;
int catapultEnd = 48;

// curent strips values
int start;
int count;

//grb
// green is blue
// red is red
// blue is green

void setup() {
  strip.begin();
  Serial.begin(9600);
  strip.show(); // Initialize all pixels to 'off'
}

void loop() {
  // reset all to off
  setFrontContext();

  clearAll();
  pulse();
  setBackContext();
  clearAll();
  pulse();
//  setCatapultContext();
//  setAll(strip.Color(0, 0, 250));
//
//  setAll(strip.Color(0, 250, 0));
//  setBackContext();
//  setAll(strip.Color(0, 250, 0));
//  //setCatapultContext();
  //setAll(strip.Color(0, 0, 250));

  strip.show();
}

void pulse() {
  int mid = count / 2;
   for(uint16_t i=0; i < mid+1; i++) {
     setAll(strip.Color(0,0,0));
     //uint32_t c1 = strip.Color(250, 0, 127);
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

void setAll(uint32_t color) {
  for(uint16_t i=0; i<count; i++) {
      setRelativeColor(i, color);
  }
}

void clearAll() {
   for(uint16_t j=0; j<strip.numPixels(); j++) {
      strip.setPixelColor((j), strip.Color(0, 0, 0));
   }
}

void setFrontContext() {
  start = frontStart;
  count = frontEnd - start + 1;  
}

void setBackContext() {
  start = backStart;
  count = backEnd - backStart + 1;    
}

void setCatapultContext() {
  start = catapultStart;
  count = catapultEnd - catapultStart + 1;
}

void setRelativeColor(int relIndex, uint32_t color) {
  if(relIndex >= 0 && relIndex < count) {
    strip.setPixelColor(relIndex + start, color);
  }
}


