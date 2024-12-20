package org.firstinspires.ftc.teamcode.Opmode.Auto.Red;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.Arm;
import org.firstinspires.ftc.teamcode.Hardware.Claw;
import org.firstinspires.ftc.teamcode.Hardware.Drivetrain;
import org.firstinspires.ftc.teamcode.Hardware.Slides;
import org.firstinspires.ftc.teamcode.Hardware.Wrist;
import org.firstinspires.ftc.teamcode.RoboActions;
import org.firstinspires.ftc.teamcode.Usefuls.Gamepad.stickyGamepad;

import java.util.List;

@Autonomous(name = "4 cycle - red")
@Config
public class RedAutoWithoutPartner extends LinearOpMode {
    public static double xTarget = 10, yTarget = 0, rTarget = 0;
    State currentState = State.DRIVETODEPOSIT;
    Actions currentAction = Actions.PICKUP;
    boolean timeToggle = true;
    boolean actionToggle = true;
    boolean actionToggleCycle=true;
    double TimeStamp = 0;
    double ActionStamp = 0;
    double ActionStampCycle=0;
    int currentCycle = 0;
    ElapsedTime timer = new ElapsedTime();
    double oldTime = 0;
    double timeStamp = 0;
    PARKING parking = PARKING.NEAR;
    boolean isThirdCycle = false;
    boolean slideNonsense = true;
    //angle for turret on third cycle
    public static double angle = 0.4;
    RoboActions actions;
    public void runOpMode() throws InterruptedException {
        //bulk reading
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        Drivetrain drive = new Drivetrain(hardwareMap, new Pose2d(0, 0, 0));
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Slides slides = new Slides(hardwareMap, drive.getSlidesMotor());
        Arm arm = new Arm(hardwareMap, drive.getArmMotor());
        Claw claw = new Claw(hardwareMap);
        Wrist wrist = new Wrist(hardwareMap);
        stickyGamepad gp = new stickyGamepad(gamepad1);
        actions = new RoboActions(hardwareMap, new Pose2d(0, 0, 0));
        while (opModeInInit()) {
            claw.close();
            wrist.intake();


            parking = PARKING.NEAR;

            gp.update();
            telemetry.addData("Parking: ", parking.name());
            telemetry.update();


        }
        waitForStart();
        double frequency = 0;
        double loopTime = 0;
        while (opModeIsActive()) {
            drive.autonomous();

            switch (currentAction) {
                case REST:
                    break;
                case PICKUP:
                    actions.preScore();
                    currentAction = Actions.SLIDESEXTEND;
                    break;
                case THIRDCYCLEPICKUP:
                    if(actionToggle){
                        ActionStamp=timer.milliseconds();
                        actionToggle=false;
                    }
                    actions.preScore();
                    if(timer.milliseconds()>ActionStamp+500){
                        actionToggle=true;
                        currentAction= Actions.SLIDESEXTEND;
                    }
                    break;
                case SLIDESEXTEND:
                    if (actionToggle) {//to be filled in...
                        ActionStamp = timer.milliseconds();
                        actionToggle = false;
                    }
                    if (timer.milliseconds() > ActionStamp + 1250) {
                        slides.score();

                        actionToggle = true;
                        currentAction = Actions.WRISTDEPOSIT;
                    }
                    break;
                case WRISTDEPOSIT:
//                    wrist.deposit();
                    if (actionToggle) {//to be filled in...
                        ActionStamp = timer.milliseconds();
                        actionToggle = false;
                    }
                    if (timer.milliseconds() > ActionStamp + 600) {
                        wrist.score();
                        actionToggle = true;
                        currentAction = Actions.DEPOSIT;
                    }
                    break;
                case DEPOSIT:
//
                    if (actionToggle) {//to be filled in...
                        ActionStamp = timer.milliseconds();
                        actionToggle = false;
                    }
                    if (timer.milliseconds() > ActionStamp + 500) {
                        claw.open();
                        if(currentCycle == 2){
                            actions.adjustTurret(angle);
                        }else{
                            actions.resetTurret();
                        }
                        actionToggle = true;
                        currentAction = Actions.RESET;
                    }
                    break;
                case RESET:
                    if (actionToggle) {
                        ActionStamp = timer.milliseconds();
                        actionToggle = false;
                    }
                    if (timer.milliseconds() > ActionStamp + 500) {
                        wrist.intake();
                        slides.preScore();
                    }

                    if (timer.milliseconds() > ActionStamp + 1000) {
                        arm.preTake();
                        actionToggle = true;
                        currentAction = Actions.REST;
                    }
                    break;
                case INTAKE:
                    if (actionToggle) {
                        ActionStamp = timer.milliseconds();
                        actionToggle = false;

                    }

                    if (timer.milliseconds() > ActionStamp + 500) {
                        if (isThirdCycle) {
                            slides.setTargetSlidesPosition(7.5);

                        } else {
                            slides.setTargetSlidesPosition(7);
                        }
                    }

                    if (timer.milliseconds() > ActionStamp + 650) {
                        arm.intake();

                    }
                    if (timer.milliseconds() > ActionStamp + 750) {
                        claw.close();

                    }


                    if (timer.milliseconds() > ActionStamp + 1000) {

                        slides.setTargetSlidesPosition(2);
                        actions.resetTurret();
                        actionToggle = true;

                        currentAction = Actions.REST;
                    }


                    break;

            }
            switch (currentState) {
                case DRIVETODEPOSIT:

                    drive.setTarget(new Pose2d(-10, 11, 0));

                    if (timeToggle) {//to be filled in...
                        TimeStamp = timer.milliseconds();
                        timeToggle = false;

                    }
                    if (timer.milliseconds() > TimeStamp + 500) {
                        timeToggle = true;
                        currentAction = Actions.PICKUP;
                        currentState = State.DEPOSIT;
                    }

                    break;
                case DEPOSIT:

                    drive.setTarget(new Pose2d(-13, 13, -45)); // deposit

                    if (timeToggle) {//to be filled in...
                        TimeStamp = timer.milliseconds();
                        timeToggle = false;
                        if(!(currentCycle==3)){
                            currentAction = Actions.PICKUP;
                        } else if (currentCycle==3){
                            currentAction=Actions.THIRDCYCLEPICKUP;
                        }
                    }

                    if (timer.milliseconds() > TimeStamp + 3500) {

                        timeToggle = true;
                        if (currentCycle == 0) {
                            currentState = State.CYCLE1;
                        } else if (currentCycle == 1) {
                            currentState = State.CYCLE2;
                        } else if (currentCycle == 2) {
                            currentState = State.CYCLE3;
                        } else if (currentCycle == 3) {
                            if (parking == PARKING.NEAR) {
                                currentState = State.PARKNEAR;
                            } else if (parking == PARKING.FAR) {
                                currentState = State.PARKFAR;
                            }
                        }
                    }

                    break;

                case CYCLE1:
                    drive.setTarget(new Pose2d(-8, 22, 0)); //first block
                    if (timeToggle) {//to be filled in...
                        TimeStamp = timer.milliseconds();
                        timeToggle = false;
                    }
                    if (timer.milliseconds() > TimeStamp + 1000) {
                        currentAction = Actions.INTAKE;
                    }
                    if (timer.milliseconds() > TimeStamp + 2500) {
                        timeToggle = true;
                        currentState = State.DEPOSIT;
                        currentCycle++;
                    }
                    break;
                case CYCLE2:
                    drive.setTarget(new Pose2d(-18, 22, 0)); //second block
                    if (timeToggle) {//to be filled in...
                        TimeStamp = timer.milliseconds();
                        timeToggle = false;
                    }
                    if (timer.milliseconds() > TimeStamp + 1000) {
                        currentAction = Actions.INTAKE;
                    }
                    if (timer.milliseconds() > TimeStamp + 2500) {
                        timeToggle = true;
                        currentState = State.DEPOSIT;
                        currentCycle++;
                    }
                    break;
                case CYCLE3:
                    isThirdCycle = true;
                    drive.setTarget(new Pose2d(-18, 25.7, 39)); //third block
                    if (timeToggle) {
                        TimeStamp = timer.milliseconds();
                        timeToggle = false;
                    }
                    if (timer.milliseconds() > TimeStamp + 1000) {
                        currentAction = Actions.INTAKE;
                    }
                    if (timer.milliseconds() > TimeStamp + 2500) {
                        timeToggle = true;
                        currentState = State.DEPOSIT;
                        currentCycle++;
                    }
                    break;
                case PARKNEAR:
                    drive.setTarget(new Pose2d(5, 49, 0));
                    if(timeToggle){
                        TimeStamp = timer.milliseconds();
                        timeToggle = false;
                    }
                    if (timer.milliseconds() > TimeStamp + 1500) {
                        timeToggle = true;
                        currentState=State.REST;
                    }
                    break;
                case HANGINTERMEDIATE:
                    drive.setTarget(new Pose2d(10, 49, 90));
                    if(timeToggle){
                        TimeStamp = timer.milliseconds();
                        timeToggle = false;
                    }
                    if (timer.milliseconds() > TimeStamp + 1500) {
                        timeToggle = true;
                        currentState=State.HANG;
                    }
                    break;
                case HANG:
                    if(timeToggle){
                        TimeStamp = timer.milliseconds();
                        timeToggle = false;
                        actions.hangPower(-1);
                    }
                    //time can be changed
                    if (timer.milliseconds() > TimeStamp + 2000) {
                        timeToggle = true;
                        currentState=State.REST;
                    }
                    break;
                    
                case PARKFAR:
                    drive.setTarget(new Pose2d(6, 27, -90));
                    if (timeToggle) {//to be filled in...
                        TimeStamp = timer.milliseconds();
                        timeToggle = false;
                    }
                    if (timer.milliseconds() > TimeStamp + 1000) {
                        drive.setTarget(new Pose2d(76, 27, -90));
                    }
                    if (timer.milliseconds() > TimeStamp + 3000) {
                        drive.setTarget(new Pose2d(76, 49, -90));
                    }
                    if (timer.milliseconds() > TimeStamp + 4000) {
                        drive.setTarget(new Pose2d(76, 49, -33));
                        timeToggle = true;
                        currentState=State.REST;
                    }

                    break;
                case REST:
                    break;


            }
            double newTime = getRuntime();
            loopTime = newTime - oldTime;
            frequency = 1 / loopTime;
            oldTime = newTime;

            drive.update();
            slides.update();
            arm.update();
            telemetry.addData("x", drive.getPose().getX());
            telemetry.addData("y", drive.getPose().getY());
            telemetry.addData("heading", drive.getPose().getHeading());
            telemetry.addData("Hub loop Time: ", frequency);
            telemetry.addData("powerX", drive.Powers().getX());
            telemetry.addData("powerY", drive.Powers().getY());
            telemetry.addData("powerHeading", drive.Powers().getHeading());

            telemetry.addData("xOut", drive.PIDOutputs().getX());
            telemetry.addData("yOut", drive.PIDOutputs().getY());
            telemetry.addData("headingOut", drive.PIDOutputs().getHeading());
            telemetry.addData("current state:", currentState.name());
            telemetry.addData("current action:", currentAction.name());
            telemetry.addData("current cycle:", currentCycle);
            telemetry.addData("isThirdCycle:",isThirdCycle);
            telemetry.addData("a: ", isThirdCycle && timer.milliseconds() > ActionStampCycle + 500);


            telemetry.update();
        }
    }

    enum State {
        DRIVETODEPOSIT, DEPOSIT, CYCLE1, CYCLE2, CYCLE3, PARKNEAR, HANG, PARKFAR, HANGINTERMEDIATE, REST
    }

    //(-18, 23.5, 30)
    enum Actions {
        PRELOADINTAKE, PICKUP, SLIDESEXTEND, WRISTDEPOSIT, DEPOSIT, RESET, INTAKE, THIRDCYCLEPICKUP, REST, THIRDSLIDE

    }

    enum PARKING {
        NEAR, FAR
    }
}
