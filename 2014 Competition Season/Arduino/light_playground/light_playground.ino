#include <Adafruit_NeoPixel.h>

#define PIN 6

// Parameter 1 = number of pixels in strip
// Parameter 2 = Arduino pin number (most are valid)
// Parameter 3 = pixel type flags, add together as needed:
//   NEO_KHZ800  800 KHz bitstream (most NeoPixel products w/WS2812 LEDs)
//   NEO_KHZ400  400 KHz (classic 'v1' (not v2) FLORA pixels, WS2811 drivers)
//   NEO_GRB     Pixels are wired for GRB bitstream (most NeoPixel products)
//   NEO_RGB     Pixels are wired for RGB bitstream (v1 FLORA pixels, not v2)
Adafruit_NeoPixel strip = Adafruit_NeoPixel(20, PIN, NEO_GRB + NEO_KHZ400);

uint32_t currentColor;

void setup() {
  strip.begin();
  Serial.begin(9600);
  strip.show(); // Initialize all pixels to 'off'
  
  currentColor = strip.Color(200,0,0);
}

//rgb
// red is blue
// green is red
// blue is green

//grb
// green is blue
// red is red
// blue is green

void loop() {
   pulse(); 
}

void catapultSideSplit() {
  int cap = strip.numPixels();
  
  int whiteLimit = cap / 2.5;
  
  int whiteValue = 200;
  
  for(uint16_t i=0; i<whiteLimit; i++) {
      strip.setPixelColor(i, strip.Color(whiteValue, whiteValue, whiteValue));
  }
  for(uint16_t i=whiteLimit; i<cap; i++) {
      strip.setPixelColor(i, currentColor);
  }
  strip.show();
}

void catapultSideGradient() {
  int cap = strip.numPixels();
  
  for(uint16_t i=0; i<cap; i++) {
    int startC = 200;
    int endC = 255;
    
    int startC2 = 200;
    int endC2 = 0;
    
    float ratio = (float)i / (float)cap;
    int value = (endC - startC) * ratio + startC;
    int value2 = (endC2 - startC2) * ratio + startC2;
     strip.setPixelColor(i, strip.Color(value, value2, value2));
  }
  strip.show();
  delay(10000);
}

void pulse() {
  int cap = strip.numPixels();
  int mid = cap / 2;
   for(uint16_t i=0; i < mid+1; i++) {
     for(uint16_t j=0; j<strip.numPixels(); j++) {
        strip.setPixelColor((j), strip.Color(0, 0, 0));
     }
     
     uint32_t c1 = strip.Color(250, 0, 127);
     uint32_t c2 = strip.Color(250, 0, 50);
     
      strip.setPixelColor((mid + i), c1);
      strip.setPixelColor((mid + i+1), c2);
      strip.setPixelColor((mid - i), c1);
      strip.setPixelColor((mid - (i+1)),c2);
      strip.show();
      delay(40);
  } 
}




void fiftyfifty(uint32_t c1, uint32_t c2) {
  int mid = strip.numPixels() / 2;
  for(uint16_t i=0; i < mid; i++) {
      strip.setPixelColor(i, c1);
  }
  for(uint16_t i= mid; i < strip.numPixels(); i++) {
      strip.setPixelColor(i, c2);
  }
  strip.show();
}

// Fill the dots one after the other with a color
void colorWipe(uint32_t c, uint8_t wait) {
  for(uint16_t i=0; i<strip.numPixels(); i++) {
      strip.setPixelColor(i, c);
      strip.show();
      delay(wait);
  }
}

// Fill the dots one after the other with a color
void colorWipeSplit(uint32_t c, uint8_t wait) {
  for(uint16_t i=0; i<strip.numPixels()/2; i++) {
      strip.setPixelColor(strip.numPixels()/2 + i, c);
      strip.setPixelColor(strip.numPixels()/2 - i, c);
      strip.show();
      delay(wait);
  }
}

//Theatre-style crawling lights.
void theaterChase(uint32_t c, uint8_t wait) {
    for (int q=0; q < 3; q++) {
      for (int i=0; i < strip.numPixels(); i=i+3) {
        strip.setPixelColor(i+q, c);    //turn every third pixel on
      }
      strip.show();
     
      delay(wait);
     
      for (int i=0; i < strip.numPixels(); i=i+3) {
        strip.setPixelColor(i+q, 0);        //turn every third pixel off
      }
    }
}

