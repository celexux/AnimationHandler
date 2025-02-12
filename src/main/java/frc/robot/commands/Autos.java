// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;

import frc.robot.Constants.LiftConstants;
import frc.robot.subsystems.AddressableLEDSubsystem;
import frc.robot.subsystems.AddressableLEDSubsystem.ColorType;

/** Example static factory for an autonomous command. */
public final class Autos {

  public static Command shiftLED(AddressableLEDSubsystem led){
    return Commands.sequence(
      led.DriverColor(ColorType.SHIFT).withTimeout(10)
    ).ignoringDisable(true);
  }
  public static Command snakeLED(AddressableLEDSubsystem led){
    return Commands.sequence(
      new ParallelCommandGroup(
        led.DriverColor(ColorType.SNAKE).withTimeout(3),
        led.HumanColor(ColorType.SNAKE).withTimeout(3)
      )
    );
  }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
