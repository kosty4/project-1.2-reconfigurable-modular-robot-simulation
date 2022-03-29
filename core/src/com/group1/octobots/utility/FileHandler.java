package com.group1.octobots.utility;

import com.badlogic.gdx.utils.Array;
import com.group1.octobots.Module;
import com.group1.octobots.Obstacle;
import com.group1.octobots.Target;
import com.group1.octobots.World;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Utility class for file input / output, courtesy of Jackie and Jun.
 */
public class FileHandler {

    private static final String MODULE_FILE = "module";
    private static final String OBSTACLE_FILE = "obstacle";
    private static final String TARGET_FILE = "target";

    private static final String COMMENT = "//";

    public static void readInputFiles() {
        readCfgFile("input/Agents", MODULE_FILE);
        readCfgFile("input/Obstacles", OBSTACLE_FILE);
        readCfgFile("input/Targets", TARGET_FILE);
    }

    private static void readCfgFile(String path, String fileType) {
        try(Scanner sc = new Scanner(new File(path))) {
            switch (fileType) {
                case MODULE_FILE:
                    makeModules(sc);
                    break;
                case OBSTACLE_FILE:
                    makeObstacles(sc);
                    break;
                case TARGET_FILE:
                    makeTargets(sc);
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Incorrect file type specified "
                                    + fileType);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void makeModules(Scanner sc) {
        Array<String> lineList = new Array<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.startsWith(COMMENT))
                lineList.add(line);
        }
        World.modCount = lineList.size;

        int tag = 1;
        for (String line : lineList) {
            String[] words = line.split(" ");

            float x = Float.parseFloat(words[1]);
            float y = Float.parseFloat(words[2]);
            float z = Float.parseFloat(words[3]);

            World.modules.add(new Module(words[0], x, y, z, tag++));
        }
    }

    private static void makeObstacles(Scanner sc) {
        while (sc.hasNextLine()) {
            String id = sc.next();
            if (id.startsWith(COMMENT)) continue;

            int x = sc.nextInt();
            int y = sc.nextInt();
            int z = sc.nextInt();

            World.obstacles.add(new Obstacle(id, x, y, z));
        }
    }

    private static void makeTargets(Scanner sc) {
        while (sc.hasNextLine()) {
            String id = sc.next();
            if (id.startsWith(COMMENT)) continue;

            int x = sc.nextInt();
            int y = sc.nextInt();
            int z = sc.nextInt();

            World.targets.add(new Target(id, x, y, z));
        }
    }

    private FileHandler(){}
}
