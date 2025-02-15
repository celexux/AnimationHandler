// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.animations.AnimationHandler;

import static frc.robot.Constants.LEDConstants.*;

public class AddressableLEDSubsystem extends SubsystemBase {
  /** Creates a new AddressableLEDSubsystem. */

  public enum ColorType {
    SHIFT,
    SNAKE,
    SOLID,
    ANIMATION;
  }

  private final AddressableLED LED;
  private final AddressableLEDBuffer LEDBuffer;
  private static AddressableLEDSubsystem instance;

  private int driver_iterations;
  private int human_iterations;
  private int driver_SHIFT_H;
  private int human_SHIFT_H;
  private int animationFrame;
  private double brightnessModifier;
  private Integer[][][][] animationArray;
  public AddressableLEDSubsystem() {
    LED = new AddressableLED(PWM_PORT);
    LEDBuffer = new AddressableLEDBuffer(LENGTH);
    LED.setLength(LEDBuffer.getLength());
    LED.setData(LEDBuffer);
    LED.start();
    driver_iterations = 0;
    driver_SHIFT_H = 0;
    human_iterations = 0;
    human_SHIFT_H = 0;
    animationFrame=0;
    animationArray = AnimationHandler.getAnimation("hiburnie.gif");
  }

  public static AddressableLEDSubsystem getInstance(){
    if(instance == null){
      instance = new AddressableLEDSubsystem();
    }
    return instance;
  }

  /**
   * This method sets all the LED groups (Human Player & Driver) to off
   */
  public void allOff() {
    // Sets each LED to off
    for(int i = 0; i < LEDBuffer.getLength(); i++) {
      LEDBuffer.setHSV(i, 0, 0, 0);
    }
    LED.setData(LEDBuffer);
  }

  public void driverOff() {
    // driver panel is indices 0 to DRIVER_START_RANGE
    for (int i = 0; i < DRIVER_START_RANGE; i++) {
      LEDBuffer.setHSV(i, 0, 0, 0);
    }

    LED.setData(LEDBuffer);
  }

  public void humanPlayerOff() {
    // human player panel is indices DRIVER_START_RANGE to the end of the buffer
    for (int i = DRIVER_START_RANGE; i < LEDBuffer.getLength(); i++) {
      LEDBuffer.setHSV(i, 0, 0, 0);
    }

    LED.setData(LEDBuffer);
  }

  /**
   * This method sets the Human Player LED group to the Yellow Color or Purple Color
   */
  public void driverColorMethod(ColorType colorType) {
    //If the colorType requested is yellow, then it will set the Human Player group color to yellow
    //If the colorType requested is purple, then it will set the Human Player group color to purple
    if (ColorType.SHIFT == colorType){
      if(driver_iterations == 0){
        for(int i = 0; i < DRIVER_START_RANGE; i++){
          LEDBuffer.setHSV(i, driver_SHIFT_H, SHIFT_S, SHIFT_V);
        }
        driver_SHIFT_H++;
        driver_SHIFT_H%=180;
        LED.setData(LEDBuffer);
      }
        driver_iterations++;
        driver_iterations%=4;
      } else if (ColorType.SNAKE == colorType){
        driver_iterations%=DRIVER_START_RANGE;

        LEDBuffer.setHSV(driver_iterations, 0, 0, 255);
        if(driver_iterations>=10){
          LEDBuffer.setHSV(driver_iterations-10, 0, 0, 0);
        } else {
          LEDBuffer.setHSV(DRIVER_START_RANGE-(10-driver_iterations), 0, 0, 0);
        }
        LED.setData(LEDBuffer);
        driver_iterations++;
      } 
    }

  /**
   * This method sets the Driver LED group to a specifed color
   */
  public void humanColorMethod(ColorType colorType) {
    if (ColorType.SHIFT == colorType){
      if(human_iterations == 0){
        for(int i = DRIVER_START_RANGE; i < LEDBuffer.getLength(); i++){
          LEDBuffer.setHSV(i, human_SHIFT_H, SHIFT_S, SHIFT_V);
        }
        human_SHIFT_H++;
        human_SHIFT_H%=180;
        LED.setData(LEDBuffer);
      }
        human_iterations++;
        human_iterations%=5;
      } else if (ColorType.SNAKE == colorType){
        int index = human_iterations+DRIVER_START_RANGE-1;
        if(human_iterations>LEDBuffer.getLength()-DRIVER_START_RANGE-1){
          human_iterations=0;
        }

        LEDBuffer.setHSV(index-1, 0, 0, 255);
        if(index>=DRIVER_START_RANGE+10 && human_iterations>=10){
          LEDBuffer.setHSV(index-11, 0, 0, 0);
        } else if(human_iterations<=10) {
          LEDBuffer.setHSV(LEDBuffer.getLength()-(10-human_iterations)-1, 0, 0, 0);
        }
        LED.setData(LEDBuffer);
        human_iterations++;
      } else if(ColorType.ANIMATION == colorType){
          if(driver_iterations == 0){
            for (int row = 0; row < 16; row++) {
              for (int col = 0; col < 16; col++) {
                // Map image (row, col) to physical coordinates (x, y)
                // Image: row 0 = top, col 0 = left.
                // Physical: (0,0) = bottom right.
                int physicalX = 15 - col;   // Rightmost image column (col=15) becomes x=0.
                int physicalY = 15 - row;   // Top image row (row=0) becomes y=15.
                
                // Determine the LED's position within its column based on the wiring direction.
                int indexInColumn;
                if (physicalX % 2 == 0) {
                    // Even column (x = 0, 2, 4, ...): wired bottom-to-top.
                    indexInColumn = physicalY; 
                } else {
                    // Odd column (x = 1, 3, 5, ...): wired top-to-bottom.
                    indexInColumn = 15 - physicalY;
                }
                
                // Compute the overall LED index.
                int ledIndex = physicalX * 16 + indexInColumn;
                
                // Set the LED color to match the corresponding image pixel.
                //edit this values for brightness. 0.05 is recommended for viewing within proximity.
                brightnessModifier = .05;
                
                LEDBuffer.setRGB(
                  ledIndex+DRIVER_START_RANGE,
                  (int) (animationArray[animationFrame][row][col][0]*brightnessModifier),
                  (int) (animationArray[animationFrame][row][col][1]*brightnessModifier),
                  (int) (animationArray[animationFrame][row][col][2]*brightnessModifier)
                );
              }
            }
            LED.setData(LEDBuffer);
            animationFrame++;
            animationFrame%=(animationArray.length);
          }
          driver_iterations++;
          driver_iterations%=5;
      }
  }

  /**
   * 
   * show a color on the led panel on the front of the robot
   * @param colorType purple or yellow
   * @return
   */
  public Command HumanColor(ColorType colorType) {
    // subsystem requirement is PURPOSEFULLY OMITTED
    return new FunctionalCommand(
      // start
      () -> {},
      // execute
      () -> {
        this.humanColorMethod(colorType);
      },
      // end
      (Boolean interrupted) -> {
        this.humanPlayerOff();
      },
      // is finished
      () -> { return false; }
    ).ignoringDisable(true);
  }

  /**
   * show a color on the led panel on the back of the robot
   * @param colorType purple, yellow, or red
   * @return 
   */
  public Command DriverColor(ColorType colorType) {
    // subsystem requirement is PURPOSEFULLY OMITTED
    return new FunctionalCommand(
      // start
      () -> {},
      // execute
      () -> {
        this.driverColorMethod(colorType);
      },
      // end
      (Boolean interrupted) -> {
        this.driverOff();
      },
      // is finished
      () -> { return false; }
    ).ignoringDisable(true);
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}