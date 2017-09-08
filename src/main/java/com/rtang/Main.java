package com.rtang;

import java.util.List;
import java.util.Scanner;

/**
 * Created by rtang on 9/7/17.
 */
public class Main {
    final static String validCommands = "Valid commands are: \n" +
                                       "    set <key> <value>\n" +
                                       "    get <key>\n" +
                                       "    delete <key>\n" +
                                       "    load\n" +
                                       "    print\n";
    private static KPCBHashMap map;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please input integer value to specify size of hash map: ");
        int size = scanner.nextInt();
        map = new KPCBHashMap(size);

        while (true) {
            String input = scanner.nextLine();
            String[] cmd = input.split("\\s+");
            int len = cmd.length;
            switch (cmd[0].toUpperCase()) {
                case "SET":
                    if (len < 3) {
                        System.out.println(validCommands);
                        break;
                    }
                    String setValue = "";
                    for (int i = 2; i < len; i++) {
                        setValue += cmd[i] + " ";
                    }
                    if (map.set(cmd[1], setValue)) System.out.println("Successfully set on specified key\n");
                    else System.out.println("Failed to set on specified key\n");
                    break;
                case "GET":
                    if (len != 2) {
                        System.out.println(validCommands);
                        break;
                    }
                    Object getValue = map.get(cmd[1]);
                    if (getValue != null) {
                        System.out.println("Successfully retrieved value at specified key:");
                        System.out.println(getValue + "\n");
                    } else System.out.println("Failed to get value at specified key\n");
                    break;
                case "DELETE":
                    if (len != 2) {
                        System.out.println(validCommands);
                        break;
                    }
                    Object deletedValue = map.delete(cmd[1]);
                    if (deletedValue != null) {
                        System.out.println("Successfully deleted value at specified key:");
                        System.out.println(deletedValue + "\n");
                    } else System.out.println("Failed to delete value at specified key\n");
                    break;
                case "LOAD":
                    if (len != 1) {
                        System.out.println(validCommands);
                        break;
                    }
                    System.out.println("Load factor: " + map.load() + "\n");
                    break;
                case "PRINT":
                    if (len != 1) {
                        System.out.println(validCommands);
                        break;
                    }
                    System.out.println(map.toString());
                    break;
                default:
                    System.out.println(validCommands);
                    break;
            }
        }
    }
}
