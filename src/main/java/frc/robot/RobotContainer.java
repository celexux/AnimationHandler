// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import frc.robot.commands.*;
import frc.robot.subsystems.*;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final AddressableLEDSubsystem aLEDSub;
  private SendableChooser<Command> autoChooser;
  private SendableChooser<String> humanAnimationChooser;

  public RobotContainer() {

    aLEDSub = AddressableLEDSubsystem.getInstance();
  
    autoChooser = new SendableChooser<>();
    humanAnimationChooser = new SendableChooser<>();
    
    autoChooser.addOption("hue shift (rainbow)", Autos.shiftLED(aLEDSub)); 
    autoChooser.addOption("snake (white)", Autos.snakeLED(aLEDSub)); 
    autoChooser.addOption("Do nothing", new InstantCommand());

    humanAnimationChooser.addOption("Jumping Burnie", "burnie.gif");
    humanAnimationChooser.addOption("Assembled Burnie", "hiburnie.gif");

    SmartDashboard.putData(autoChooser);
    SmartDashboard.putData(humanAnimationChooser);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
  public String getHumanAnimation() {
    return humanAnimationChooser.getSelected();
  }
}