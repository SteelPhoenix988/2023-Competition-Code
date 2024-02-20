//Everybot code stapled to several example codes with the TrashBot naming conventions.
//Uses PWM for arcade drive.
//Uses CAN for arm joints and intake.
  //They SparkMaxes may need to be configured.
//Uses a 3 axis joystick.
  //Originally used mechanum and it made sense. 
  //For arcade we could use whatever.
  //Buttons for arms are super wrong. Check mapping or just do it by testing.
//Intake currently has no code.
  //Everybot got fancy with it and has voltages applied to keep the part sucked in.
  //We should totally do that to make sure things don't just like...pop out.

  package frc.robot;

  import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

//Imports a bunch of libraries. Make sure you have the Pheonx JSON library installed.
  import edu.wpi.first.wpilibj.Joystick;
   import edu.wpi.first.wpilibj.TimedRobot;
   import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj2.command.Command;
  
  //Stuff below this is the actual code that does things with the robot.
  public class Robot extends TimedRobot
  {
    private Command m_autonomousCommand;
    //Turns motor control channels into variables so we can use them later.
      //I don't think actually have to do this but it's the choice Everybot made.
      //I'm also not sure why they started with k. I think it was so it would autofill better?
    private static final int kRearLeftChannel = 0;
    private static final int kFrontLeftChannel = 1;
    private static final int kFrontRightChannel = 3;
    private static final int kRearRightChannel = 2;
    private static final int kJoystickChannel = 0;
    private Timer mytimer;
    
    //private static final RobotContainer m_robotContainer;
    //No idea what this does but it needs to exist or the joystick breaks. 
    private Joystick m_stick;
    
    //This chunk defines the arm and intake motors min/max values.
    //They are controlled via boolean functions, so the roboRIO needs to know what to set things to when buttons are pushed.
      //Currently both the intake joint and arm joint use the same parameters.
        //They are not gear boxed the same and should totally be changed after some testing.
  
     //Sets current limit for ArmJointOne
       static final int ARM_CURRENT_LIMIT_A = 20;
      // Percent output to run the arm up/down at.
        //Adjust this to modify drop speed.
        //Everybot had it sync to the smart dashboard and be adjustable.
  
     //Defines Drive Motor Controllers
      PWMVictorSPX driveLeftFront = new PWMVictorSPX(kFrontLeftChannel);
      PWMVictorSPX driveLeftRear = new PWMVictorSPX(kRearLeftChannel);
      PWMVictorSPX driveRightFront = new PWMVictorSPX(kFrontRightChannel);
      PWMVictorSPX driveRightRear = new PWMVictorSPX(kRearRightChannel);
   
     //Defines Arm Motor Controllers
      //PWMSparkMax armJointOne = new PWMSparkMax(kArmOneChannel);
      //PWMSparkMax armJointTwo = new PWMSparkMax(kArmTwoChannel);
      private CANSparkMax armJointOne;
      private CANSparkMax armJointTwo;
    
  //Activates when robot turns on.
    @Override
    public void robotInit() 
     {
      armJointOne = new CANSparkMax(2, MotorType.kBrushed);
      armJointTwo = new CANSparkMax(1, MotorType.kBrushless);

      // Inverts the right side motors to jive with drive code. 
      //Change when appropriate or else it will just spin or go in reverse.
       driveLeftFront.setInverted(false);
       driveLeftRear.setInverted(false);
       driveRightFront.setInverted(true);
       driveRightRear.setInverted(true);
       armJointOne.setInverted(false);
       armJointTwo.setInverted(false);
       m_stick = new Joystick(kJoystickChannel);
     }
     
      
   @Override
   public void robotPeriodic()
   {



//PUT CODE WHICH YOU NEED 5TO RUN AT ALL TIMES



   }
  //This sets things up for when autonomous begins.
    @Override
    public void autonomousInit() 
     {
      mytimer = new Timer();
      mytimer.reset();
      mytimer.start();
     }
    
  //This is what actually happens during autonmous.
    @Override
    public void autonomousPeriodic() 
     {
      if(mytimer.get() < 6.0)
      {
      double driveLeftPower = 0.3;
      double driveRightPower = 0.3;
  
      driveLeftRear.set(driveLeftPower);
      driveLeftFront.set(driveLeftPower);
      driveRightFront.set(driveRightPower);
      driveRightRear.set(driveRightPower);
      }
      else
      {
      double driveLeftPower = 0.0;
      double driveRightPower = 0.0;
  
      driveLeftRear.set(driveLeftPower);
      driveLeftFront.set(driveLeftPower);
      driveRightFront.set(driveRightPower);
      driveRightRear.set(driveRightPower);

      mytimer.stop();
         }
     }
    
  //This sets things up for when teleop begins.
    @Override
    public void teleopInit() 
     {
      // This makes sure that the autonomous stops running when
      // teleop starts running. If you want the autonomous to
      // continue until interrupted by another command, remove
      // this line or comment it out.
      if (m_autonomousCommand != null) {
        m_autonomousCommand.cancel();
      }
     }
  
  //This is what actually happens during teleop. Let's us control things.
    @Override
    public void teleopPeriodic()
     {
      //This chunk is for arm control
      //Nested if statements because buttons are boolean.
      //As stated above, you may need to add more parameters to the motors.
      //Intake angle and arm angle are controlled separately.
      //Partially in case we need weird angles and partially because I'm not doing all the work for you.
      //They can be synced to move in unison if you want to figure it out.

      //Make first arm joint go up and down based on button push.
     //double armJointOnePower = 0.0;
     if (m_stick.getRawButton(3))
     {
       //Lower the intake
       armJointOne.set(-0.2);
     } 
     else if (m_stick.getRawButton(5)) 
     {
       //Raise the intake
       armJointOne.set(0.5);
     } 
     else 
     {
       //Do nothing and let it sit where it is.
       //We might need to apply voltage to keep it from slipping.
       armJointOne.set(0.0);
     }
      //armJointOne.set(armJointOnePower);

      //Make intake joint go up and down based on button push.
      //double armJointTwoPower = 0.0;
      if (m_stick.getRawButton(4))
      {
        //Lower the intake
        armJointTwo.set(-0.2);
      } 
      else if (m_stick.getRawButton(6)) 
      {
        //Raise the intake
        armJointTwo.set(0.2);
      } 
      else 
      {
        //Do nothing and let it sit where it is.
        //We might need to apply voltage to keep it from slipping.
        armJointTwo.set(0.0);
      }
      //armJointTwo.set(armJointTwoPower);
        
      
                    //This chunk is for driving.
        //Couldn't use build in differential function since it would only do one motor set.
        //You could make a drive train, but that would involve me coding.
        //Could be done way more efficiently but whatever. 
        {
          double forward = -m_stick.getY();
          double turn = -m_stick.getZ();
          double driveLeftPower = forward - turn;
          double driveRightPower = forward + turn;
    
          driveLeftRear.set(driveLeftPower);
          driveLeftFront.set(driveLeftPower);
          driveRightFront.set(driveRightPower);
          driveRightRear.set(driveRightPower);
        }
     }
  }
