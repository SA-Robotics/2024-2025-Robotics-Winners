package org.firstinspires.ftc.teamcode.config;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.Map;

public enum Hardware {
    DT_FRONT_RIGHT_MOTOR(DcMotor.class),
    DT_BACK_RIGHT_MOTOR(DcMotor.class),
    DT_FRONT_LEFT_MOTOR(DcMotor.class),
    DT_BACK_LEFT_MOTOR(DcMotor.class);

    private String name;
    private Class clazz;
    private static HardwareMap hwMap;

    private Hardware(Class<?> clazz) {
        Map<Hardware, String> hwCFG = cfgParser.getHardwareCFGMap();

        this.name = hwCFG.get(this);
        this.clazz = clazz;
    }

    /**
     * @return Name of hardware in configuration file
     */
    public String getConfigName() {
        return name;
    }

    /**
     * @return Type of hardware a type is
     */
    public Class getType() {
        return clazz;
    }

    /**
     * Initializes the hardware component with the configured name and type
     * @return Reference to class
     */
    public <T> T get() {
        return (T)hwMap.get(clazz.getClass(), name);
    }

    public static void init(HardwareMap hwMap) {
        Hardware.hwMap = hwMap;
    }
}
