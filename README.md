"# tooth_app22-01" 
//Arduino code.ino

#include <SoftwareSerial.h>
SoftwareSerial btSerial(4, 3);

#include <DHT.h>

#define DHTPIN 5 //온습도센서
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

#define LED_R 8

int coolfan = 6;

int temperature, humidity;

char message;

void setup() {
  Serial.begin(9600);
  btSerial.begin(9600);
  dht.begin();
  delay(1000);

  pinMode(LED_R, OUTPUT);
  pinMode(coolfan, OUTPUT);
}

void loop() {
  Serial.println();
  humidity = dht.readHumidity(); //습도
  temperature = dht.readTemperature(); //온도
  
  
  
  
  

  if(btSerial.available()) {
    message = char(btSerial.read());
    if(message == '1') {
      digitalWrite(coolfan, HIGH);
    }
    else if (message == '2') {
      digitalWrite(coolfan, LOW);
    }
    else if (message == '3') {
      if(humidity > 59) {
    digitalWrite(LED_R, HIGH);
    digitalWrite(coolfan, HIGH);
    }
  else {
    digitalWrite(LED_R, LOW);
    digitalWrite(coolfan, LOW);
    }
    }
  } //if문 끝

  DHT_readBT();
  DHT_content();
  LED_content();
}

void AUTO_mode() {
  }

void DHT_readBT() {
  btSerial.print(temperature); //temperature
  btSerial.print(",");
  btSerial.print(humidity);
  btSerial.print(",");
  btSerial.println();
  delay(1000);
}

void DHT_content() {
  Serial.print(temperature);
  Serial.print(" C, ");
  Serial.print(humidity);
  Serial.print(",");
  Serial.println();
  delay(1000);
}

void LED_content() {
  if(humidity > 59) digitalWrite(LED_R, HIGH);
  else digitalWrite(LED_R, LOW);
}
