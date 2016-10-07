/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Fake;

import Helper.Helper;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author turox
 */
public class FileUtils {

    public static void copyFileToDirectory(FakeFile orig, FakeFile dest) throws Exception {
        if (!dest.isDirectory()) {
            throw new Exception("Destination is not a directory");
        }
        try {
            FakeFile orig_clone = (FakeFile) orig.clone();
            orig_clone.setPathField(Helper.normalizePath(dest.getPathField() + "/" + orig_clone.getName()));
            dest.addChild(orig_clone);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void copyFile(FakeFile orig, FakeFile dest) {
        FakeFile dest_parent = dest.getParentFile();
        if (dest_parent.exists() && dest_parent.isDirectory()) {
            try {
                FakeFile orig_clone = (FakeFile) orig.clone();
                orig_clone.setPathField(Helper.normalizePath(dest_parent.getPathField() + "/" + dest.getName()));
                dest_parent.addChild(orig_clone);
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void copyDirectoryToDirectory(FakeFile orig, FakeFile dest) throws Exception {
        if (!dest.isDirectory()) {
            throw new Exception("Destination is not a directory");
        }
        try {
            FakeFile orig_clone = (FakeFile) orig.clone();
            orig_clone.setPathField(Helper.normalizePath(dest.getPathField() + "/" + orig_clone.getName()));

            // FIX CHILDREN PATH FIELD
            // AND FIX CHILDREN'S CHILDREN'S CHILNDREN'S CHILNDREN PATH FIELD            
            Helper.massReplacePathField(orig_clone, orig.getPathField(), orig_clone.getPathField());

            dest.addChild(orig_clone);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void moveFileToDirectory(FakeFile orig, FakeFile dest, boolean createDestDir) throws Exception {
        if (!dest.isDirectory()) {
            throw new Exception("Destination is not a directory");
        }
        try {
            FakeFile orig_clone = (FakeFile) orig.clone();
            orig_clone.setPathField(Helper.normalizePath(dest.getPathField() + "/" + orig_clone.getName()));
            dest.addChild(orig_clone);
            orig.delete();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void moveFile(FakeFile orig, FakeFile dest) {
        FakeFile dest_parent = dest.getParentFile();
        if (dest_parent.exists() && dest_parent.isDirectory()) {
            try {
                FakeFile orig_clone = (FakeFile) orig.clone();
                orig_clone.setPathField(Helper.normalizePath(dest_parent.getPathField() + "/" + dest.getName()));
                dest_parent.addChild(orig_clone);
                orig.delete();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void moveDirectoryToDirectory(FakeFile orig, FakeFile dest, boolean createDestDir) throws Exception {
        if (!dest.isDirectory()) {
            throw new Exception("Destination is not a directory");
        }
        try {
            FakeFile orig_clone = (FakeFile) orig.clone();
            orig_clone.setPathField(Helper.normalizePath(dest.getPathField() + "/" + orig_clone.getName()));

            // FIX CHILDREN PATH FIELD
            // AND FIX CHILDREN'S CHILDREN'S CHILNDREN'S CHILNDREN PATH FIELD            
            Helper.massReplacePathField(orig_clone, orig.getPathField(), orig_clone.getPathField());

            dest.addChild(orig_clone);
            orig.delete();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
