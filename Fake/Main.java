/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Fake;

/**
 *
 * @author turox
 */
public class Main {
    public static void main(String[] args) {
        FakeFileSystem fs = new FakeFileSystem();
        FakeFile f = new FakeFile(fs, "/home");
        f.mkdir();
        f = new FakeFile(fs, "/home2");
        f.mkdir();
        f = new FakeFile(fs, "/home3");
        f.mkdir();
        f = new FakeFile(fs, "/home4");
        f.mkdir();
        
        FakeFile sf = new FakeFile(fs, "/home/turox/abc/m8");
        System.out.println(sf.mkdirs());
        FakeFile[] fakefiles = fs.root.listFiles();
        for (FakeFile fakefile : fakefiles) {
            System.out.println(fakefile.getName());
        }
    }
    
}
