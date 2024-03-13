package org.firstinspires.ftc.teamcode.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

class cfgParser {

    private static final File cfgFile = new File("Config.txt");

    private static Map<Hardware, String> hardwareCFGMap;

    /**
     * If config map if map is empty or null, it will automatically update the map
     * @return Hardware configuration map
     */
    public static Map<Hardware, String> getHardwareCFGMap() {
        // Get configuration if empty
        if(hardwareCFGMap == null || hardwareCFGMap.isEmpty())
            updateMaps();

        return hardwareCFGMap;
    }

    /**
     * Updates the configuration maps
     */
    public static void updateMaps() {
        Scanner scan = null;
        try {
            scan = new Scanner(cfgFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Config file not found!");
        }

        hardwareCFGMap.clear();
        while(scan.hasNextLine()) {
            String[] cfgKeyValue = scan.nextLine().split(" : ");
            hardwareCFGMap.put(Hardware.valueOf(cfgKeyValue[0]), cfgKeyValue[1]);
        }

        scan.close();
    }
}
