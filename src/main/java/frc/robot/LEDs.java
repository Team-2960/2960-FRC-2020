package frc.robot;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
public class LEDs{
    private AddressableLED LEDStrip;
    private AddressableLEDBuffer LEDBuffer;
    /**
     * LEDS
     */
    public LEDs(){
        LEDStrip = new AddressableLED(1);
        LEDBuffer = new AddressableLEDBuffer(25);
        LEDStrip.setLength(LEDBuffer.getLength());
        for(int i = 0; i < LEDBuffer.getLength(); i++){
            LEDBuffer.setRGB(i, 0, 255, 0);
        }
        LEDStrip.setData(LEDBuffer);
        LEDStrip.start();
    }
}