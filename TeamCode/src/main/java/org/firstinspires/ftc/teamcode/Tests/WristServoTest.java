package org.firstinspires.ftc.teamcode.Tests;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Usefuls.Motor.ServoMotorBetter;
@TeleOp
@Config
// down: 0.15, entering submersible: 0.5, scoring: 0.585
public class WristServoTest extends LinearOpMode {
    public static double angle;
    public void runOpMode(){
        Servo wrist = hardwareMap.get(Servo.class, "wrist");
        waitForStart();
        while(opModeIsActive()&&!isStopRequested()){
            wrist.setPosition(angle);
        }

    }
}
