package com.mypoc.grapgql.filecompare;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileComparator {

    public static void main(String[] args) throws IOException {
        String folder1Path = "folder1_path";
        String folder2Path = "folder2_path";

        List<File> filesInFolder1 = getFilesRecursive(folder1Path);
        List<File> filesInFolder2 = getFilesRecursive(folder2Path);

        compareFiles(filesInFolder1, filesInFolder2);
    }

    private static List<File> getFilesRecursive(String folderPath) {
        List<File> files = new ArrayList<>();

        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Invalid folder path: " + folderPath);
            return files;
        }

        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                files.addAll(getFilesRecursive(file.getAbsolutePath()));
            } else if (file.isFile()) {
                files.add(file);
            }
        }

        return files;
    }

    private static void compareFiles(List<File> filesInFolder1, List<File> filesInFolder2) throws IOException {
        for (File file1 : filesInFolder1) {
            boolean foundMatch = false;
            for (File file2 : filesInFolder2) {
                if (file1.getName().equals(file2.getName())) {
                    if (!isFileContentEqual(file1, file2)) {
                        System.out.println("Files differ: " + file1.getAbsolutePath() + " and " + file2.getAbsolutePath());
                    }
                    foundMatch = true;
                    break;
                }
            }

            if (!foundMatch) {
                System.out.println("File not found in folder 2: " + file1.getAbsolutePath());
            }
        }

        for (File file2 : filesInFolder2) {
            boolean foundMatch = false;
            for (File file1 : filesInFolder1) {
                if (file2.getName().equals(file1.getName())) {
                    foundMatch = true;
                    break;
                }
            }

            if (!foundMatch) {
                System.out.println("File not found in folder 1: " + file2.getAbsolutePath());
            }
        }
    }

    private static boolean isFileContentEqual(File file1, File file2) throws IOException {
        if (file1.length() != file2.length()) {
            return false;
        }

        try (java.io.InputStream in1 = new java.io.FileInputStream(file1);
             java.io.InputStream in2 = new java.io.FileInputStream(file2)) {
            int byte1, byte2;
            while ((byte1 = in1.read()) != -1) {
                byte2 = in2.read();
                if (byte1 != byte2) {
                    return false;
                }
            }
            return true;
        }
    }
}
