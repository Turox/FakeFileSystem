package Helper;

import Fake.FakeFile;
import Fake.FakeFileSystem;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author turox
 */
public class Helper {

    public static ArrayList<String> cmds = new ArrayList();

    public static boolean validIntPosixPerm(int chmod) {
        int other = chmod % 10, group = (chmod / 10) % 10, owner = (chmod / 100) % 10;

        if (owner >= 4) {
            owner -= 4;
        }
        if (owner >= 2) {
            owner -= 2;
        }
        if (owner >= 1) {
            owner -= 1;
        }

        if (group >= 4) {
            group -= 4;
        }
        if (group >= 2) {
            group -= 2;
        }
        if (group >= 1) {
            group -= 1;
        }

        if (other >= 4) {
            other -= 4;
        }
        if (other >= 2) {
            other -= 2;
        }
        if (other >= 1) {
            other -= 1;
        }

        return (owner == 0 && group == 0 && other == 0);
    }

    public static Set<PosixFilePermission> intToSetPosix(int chmod) {
        Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
        int other = chmod % 10, group = (chmod / 10) % 10, owner = (chmod / 100) % 10;

        if (owner >= 4) {
            perms.add(PosixFilePermission.OWNER_READ);
            owner -= 4;
        }
        if (owner >= 2) {
            perms.add(PosixFilePermission.OWNER_WRITE);
            owner -= 2;
        }
        if (owner >= 1) {
            perms.add(PosixFilePermission.OWNER_EXECUTE);
            owner -= 1;
        }

        if (group >= 4) {
            perms.add(PosixFilePermission.GROUP_READ);
            group -= 4;
        }
        if (group >= 2) {
            perms.add(PosixFilePermission.GROUP_WRITE);
            group -= 2;
        }
        if (group >= 1) {
            perms.add(PosixFilePermission.GROUP_EXECUTE);
            group -= 1;
        }

        if (other >= 4) {
            perms.add(PosixFilePermission.OTHERS_READ);
            other -= 4;
        }
        if (other >= 2) {
            perms.add(PosixFilePermission.OTHERS_WRITE);
            other -= 2;
        }
        if (other >= 1) {
            perms.add(PosixFilePermission.OTHERS_EXECUTE);
            other -= 1;
        }

        return perms;
    }

    public static String normalizePath(String s) {
        String out = s;
        out = out.replace("//", "/");
        return out;
    }

    public static String getOperatingPath(String s) {
        String old, new_path;
        try {
            if (!s.startsWith("/")) {
                if (operatingSystem.fileSystem.FileSytemSimulator.currentDir.equals("/")) {
                    old = operatingSystem.fileSystem.FileSytemSimulator.currentDir + s;
                } else {
                    old = operatingSystem.fileSystem.FileSytemSimulator.currentDir + "/" + s;
                }
            } else {
                old = s;
            }
            new_path = new URI(old).normalize().getPath();
            return new_path;
        } catch (URISyntaxException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "/";
    }

    public static ArrayList<String> getCommandsDump(FakeFile cur, String path) {
        FakeFile[] files = cur.listFiles();
        if (cur.isDirectory()) {
            if (!cur.isHidden()) {
                Helper.cmds.add("mkdir " + path);
            }
        }
        for (FakeFile f : files) {
            if (f.isDirectory()) {
                Helper.getCommandsDump(f, path + f.getName() + "/");
            } else {
                Helper.cmds.add("createfile " + path + f.getName());
            }
        }
        return Helper.cmds;
    }
    
    public static void massReplacePathField(FakeFile all_mighty_father, String find, String replace) {
        FakeFile[] files = all_mighty_father.listFiles();      
        for (FakeFile f : files) {
            all_mighty_father.child.remove(f.getPathField());
            if (f.isDirectory()) {                
                f.setPathField(f.getPathField().replace(find, replace));                
                massReplacePathField(f, find, replace);
            } else {
                f.setPathField(f.getPathField().replace(find, replace));
            }
            all_mighty_father.child.put(f.getPathField(), f);
        }
    }
    

    public static Fake.FakeFileSystem fs = new FakeFileSystem();
}
