package org.firstinspires.ftc.teamcode.Tests;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Wrist;

@TeleOp
@Config
// closed: 0.2,opened: 0.8,
public class WristTest extends LinearOpMode {
    public static double angle=0.15;
    public void runOpMode(){
        Wrist wrist = new Wrist(hardwareMap);
        waitForStart();
        wrist.setPosition(0.15);
        while(opModeIsActive()&&!isStopRequested()){
            wrist.setPosition(angle);
        }

    }
}