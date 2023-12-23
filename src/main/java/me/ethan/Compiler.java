package me.ethan;

import me.ethan.exceptions.InvalidCommandException;
import me.ethan.exceptions.InvalidLabelException;

import java.util.*;

public class Compiler {

    private static final Map<String,Byte> conditionals = getConditionalCommands();
    private static final Map<String,Byte> actionCommands = getActionCommands();


    public static void main(String[] args) {
            String script = ":START  attack\n" +
                    "     moveForward\n" +
                    ":EAT  eat \n" +
                    "     GOTO EAT\n";
            List<String> tokens = convertToTokens(script); // convert script into individual tokens
            List<Byte> bytecode = convertToBytecode(tokens); // convert tokens into the bytecode
            System.out.println("Script into tokens " + tokens);
            System.out.println("Tokens into bytecode " + bytecode);
    }

    private static List<String> convertToTokens(String script) {
        String[] eachLine = getLines(script);
        List<String> tokens = new ArrayList<>();
        for (String string : eachLine) {
            String line = string.trim();
            if (line.startsWith(":")) {
                int commandI = (string.trim().indexOf(" "));
                if (commandI == -1) {
                    tokens.add(line);
                } else {
                    String label = line.substring(0, commandI).trim();
                    String command = string.substring(commandI).trim();
                    tokens.add(label);
                    tokens.add(command);
                }
            } else if (line.startsWith("GOTO")) {
                String label = line.substring(5);
                tokens.add("GOTO");
                tokens.add(label);
            } else if (line.startsWith("if")) {
                int commandI = (string.trim().indexOf(" "));
                String ifStatement = line.substring(0, commandI).trim();
                String command = string.trim().substring(commandI);
                tokens.add(ifStatement.trim());
                tokens.add(command.trim());
            } else {
                tokens.add(line);
            }
        }
        return tokens;
    }

    private static List<Byte> convertToBytecode(List<String> tokens) {
        List<Byte> bytecode = new ArrayList<>();
        Map<String,Byte> labels = getLabels(tokens); // this is prob slow, but when i found a faster way,
        // jerry said to do it this way. so blame him
        for(int i = 0; i < tokens.size(); i++){
            String token = tokens.get(i);
            if(labels.containsKey(token.substring(1))) {
                System.out.println(token);// nothin
            }else if (isActionCommand(token)){
                bytecode.add(actionCommands.get(token));
            } else if (isConditionalCommand(token)){
                bytecode.add(conditionals.get(token));
                String label = tokens.get(i+1);
                if(!labels.containsKey(label)){
                    try {
                        throw new InvalidLabelException("Label \"" + label + "\" does not exist");
                    } catch (InvalidLabelException e) {
                        throw new RuntimeException(e);
                    }
                }
                bytecode.add(labels.get(label));
                i++;
            } else {
                try {
                    throw new InvalidCommandException("Invalid Command: " + token);
                } catch (InvalidCommandException e) {
                    throw new RuntimeException(e);
                }
                // this is a target, you can ignore
            }
        }
        return bytecode;
    }

    private static Map<String, Byte> getLabels(List<String> tokens) {
        Map<String,Byte> labels = new HashMap<>();
        byte instructionPointer = 0;
        for (String token : tokens) {
            if (token.startsWith(":")) {
                String label = token.substring(1);
                if(labels.containsKey(label)){
                    try {
                        throw new InvalidLabelException("Label at line " + instructionPointer + " appears twice. ERROR");
                    } catch (InvalidLabelException e) {
                        throw new RuntimeException(e);
                    }
                }
                labels.put(label, instructionPointer);
            } else if (isConditionalCommand(token)) {
                instructionPointer += 2;
            } else {
                instructionPointer++;
            }
        }
        System.out.println("ALL LABELS" + labels);
        return labels;
    }

    private static boolean isConditionalCommand(String token) {
        return conditionals.containsKey(token);
    }

    private static boolean isActionCommand(String token) {
        return actionCommands.containsKey(token);
    }

    private static int countLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return lines.length;
    }

    private static String[] getLines(String script) {
        Scanner scanner = new Scanner(script);
        String[] arr = new String[countLines(script)];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = scanner.nextLine();
        }
        return arr;
    }

    private static Map<String, Byte> getConditionalCommands(){
        Map<String,Byte> conditionals = new HashMap<>();
        conditionals.put("ifEnemy", (byte) 30);
        conditionals.put("ifAlly", (byte) 31);
        conditionals.put("ifFood", (byte) 32);
        conditionals.put("ifEmpty", (byte) 33);
        conditionals.put("ifWall", (byte) 34);
        conditionals.put("GOTO", (byte) 35);
        return conditionals;
    }

    private static Map<String, Byte> getActionCommands() {
        Map<String, Byte> commands = new HashMap<>();
        commands.put("noop", (byte) 0);
        commands.put("moveForward", (byte) 10);
        commands.put("rotateRight", (byte) 11);
        commands.put("rotateLeft", (byte) 12);
        commands.put("attack", (byte) 13);
        commands.put("eat", (byte) 14);
        return commands;
    }
}
