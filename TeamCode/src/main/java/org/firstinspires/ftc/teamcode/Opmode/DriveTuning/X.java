package org.firstinspires.ftc.teamcode.Opmode.DriveTuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.Drivetrain;

@Config
public class X extends LinearOpMode {
    Drivetrain drive = new Drivetrain(hardwareMap, new ElapsedTime(), new Pose2d(0, 0, 0));
    public static double xTarget = 0, yTarget = 0, rTarget = 0;
    public void runOpMode() throws InterruptedException {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        waitForStart();
        while(opModeIsActive()) {
            drive.autonomous();
            drive.setTarget(new Pose2d(xTarget, yTarget, rTarget));
            drive.update();
            telemetry.addData("x", drive.getPose().getX());
            telemetry.addData("y", drive.getPose().getY());
            telemetry.addData("heading", drive.getPose().getHeading());

            telemetry.addData("powerX", drive.Powers().getX());
            telemetry.addData("powerY", drive.Powers().getY());
            telemetry.addData("powerHeading", drive.Powers().getHeading());

            telemetry.addData("xOut", drive.PIDOutputs().getX());
            telemetry.addData("yOut", drive.PIDOutputs().getY());
            telemetry.addData("headingOut", drive.PIDOutputs().getHeading());

            telemetry.update();
        }
    }
}