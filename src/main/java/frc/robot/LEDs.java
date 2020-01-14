package frc.robot;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
public class LEDs{
    private AddressableLED LEDStrip;
    private AddressableLEDBuffer LEDBuffer;
    public LEDs(){
        LEDStrip = new AddressableLED(1);
        LEDBuffer = new AddressableLEDBuffer(25);
        LEDStrip.setLength(LEDBuffer.getLength());
        for(int i = 0; i < LEDBuffer.getLength(); i++){
            LEDBuffer.setRGB(i, 0, 100, 0);
        }
        LEDStrip.setData(LEDBuffer);
        LEDStrip.start();
    }
}