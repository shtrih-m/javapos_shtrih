package com.shtrih.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author V.Kravtsov
 */
import java.io.File;
import java.io.FileInputStream;

public class FileUtils {

    private FileUtils() {
    }

    public static String getExtention(String fileName) {
        String result = "";
        int nameIndex = fileName.lastIndexOf(".");
        int pathIndex = fileName.lastIndexOf(File.separator);
        if ((nameIndex > pathIndex) && (nameIndex >= 0)) {
            result = fileName.substring(nameIndex);
        }
        return result;
    }

    public static String removeExtention(String fileName) {
        String result = fileName;
        int nameIndex = fileName.lastIndexOf(".");
        int pathIndex = fileName.lastIndexOf(File.separator);
        if ((nameIndex > pathIndex) && (nameIndex >= 0)) {
            result = fileName.substring(0, nameIndex);
        }
        return result;
    }

    public static String read(String fileName) throws Exception {
        File file = new File(fileName);
        FileInputStream stream = new FileInputStream(file);
        try {
            int fileLen = (int) file.length();
            byte[] fileData = new byte[fileLen];
            stream.read(fileData);
            return new String(fileData);
        } finally {
            stream.close();
        }
    }

}
