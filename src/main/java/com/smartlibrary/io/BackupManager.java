package com.smartlibrary.io;

import com.smartlibrary.annotation.CourseConcept;

import java.io.*;

/**
 * Backup Manager demonstrating Byte-Oriented Streams.
 * Demonstrates Unit 4: Byte Streams (FileInputStream, FileOutputStream, Buffered Streams).
 */
@CourseConcept(unit = 4, concept = "Byte-Oriented I/O Streams & Binary File Backup")
public class BackupManager {

    public static boolean createDatabaseBackup(String sourcePath, String destPath) {
        File sourceFile = new File(sourcePath);
        File destFile = new File(destPath);

        if (!sourceFile.exists()) {
            System.err.println("Source database file does not exist: " + sourcePath);
            return false;
        }

        if (destFile.getParentFile() != null && !destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }

        // Demonstrates Byte Stream FileInputStream and FileOutputStream with Buffered streams
        try (InputStream in = new BufferedInputStream(new FileInputStream(sourceFile));
             OutputStream out = new BufferedOutputStream(new FileOutputStream(destFile))) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            return true;
        } catch (IOException e) {
            System.err.println("Byte stream copy error during backup: " + e.getMessage());
            return false;
        }
    }
}
