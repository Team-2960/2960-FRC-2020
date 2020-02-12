void setup() {
  pinMode(0, INPUT_PULLDOWN);
  pinMode(1, INPUT_PULLDOWN);
  pinMode(2, INPUT_PULLDOWN);
  pinMode(3, INPUT_PULLDOWN);
  pinMode(4, INPUT_PULLDOWN);
  pinMode(5, INPUT_PULLDOWN);
  pinMode(6, INPUT_PULLDOWN);
  pinMode(7, INPUT_PULLDOWN);
  pinMode(8, INPUT_PULLDOWN);
  pinMode(9, INPUT_PULLDOWN);
  pinMode(10, INPUT_PULLDOWN);
  pinMode(11, INPUT_PULLDOWN);
  pinMode(13, INPUT);
  pinMode(14, INPUT);
}

void loop() {
  //manual button 
    Joystick.button(1, digitalRead(0));

  //retract and extend button 
    Joystick.button(2, digitalRead(1));
    Joystick.button(3, digitalRead(2));

  //winch button 
    Joystick.button(4, digitalRead(3));

  //shoot direction button 
    Joystick.button(5, digitalRead(4));

  //camera button 
    Joystick.button(6, digitalRead(5));

  //short button 
    Joystick.button(7, digitalRead(6));

  //long button 
    Joystick.button(8, digitalRead(7));

  //intake button 
    Joystick.button(9, digitalRead(8));
    Joystick.button(10, digitalRead(9));

  //shoot button
    Joystick.button(11, digitalRead(10));

  //index out button
    Joystick.button(12, digitalRead(11));

  //speed 13 14
    Joystick.X(analogRead(13));
    Joystick.Y(analogRead(14));

  //angle 15 16
}
