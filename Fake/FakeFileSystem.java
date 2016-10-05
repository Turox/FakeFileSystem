package Fake;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/*
 * Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
/**
 * A stub of FakeFileOperations for use in testing.
 */
public class FakeFileSystem {

    public FakeFile root;

    public FakeFileSystem() {
        root = new FakeFile(this, "/");
        root.isDirectory = true;
    }

    public boolean canExecute(FakeFile fake) {
        return true;
    }

    public boolean canRead(FakeFile fake) {
        return true;
    }

    public boolean canWrite(FakeFile fake) {
        return true;
    }

    public int compareTo(FakeFile fake, File pathname) {
        throw new UnsupportedOperationException();
    }

    public boolean createNewFile(FakeFile fake) throws IOException {
        fake.isDirectory = false;
        fake.chmod = 644;
        fake.lastModified = System.currentTimeMillis();
        FakeFile parent = fake.getParentFile();
        if (parent != null) {
            parent.addChild(fake);
            return true;
        } else {
            return false;
        }
    }

    public boolean delete(FakeFile fake) {
        FakeFile parent = fake.getParentFile();
        if (parent != null && parent.exists()) {
            parent.removeChild(fake);
            return true;
        } else {
            return false;
        }
    }

    public void deleteOnExit(FakeFile fake) {
        throw new UnsupportedOperationException();
    }

    public boolean equals(FakeFile fake, Object obj) {
        throw new UnsupportedOperationException();
    }

    public boolean exists(FakeFile fake) {
        String path = fake.getPathField();
        if (path.equals("/")) {
            return true;
        } else {
            FakeFile parent = fake.getParentFile();
            if (parent != null) {
                return parent.childExists(fake);
            } else {
                return false;
            }
        }
    }

    public FakeFile getAbsoluteFile(FakeFile fake) {
        throw new UnsupportedOperationException();
    }

    public String getAbsolutePath(FakeFile fake) {
        throw new UnsupportedOperationException();
    }

    public FakeFile getCanonicalFile(FakeFile fake) throws IOException {
        throw new UnsupportedOperationException();
    }

    public String getCanonicalPath(FakeFile fake) throws IOException {
        throw new UnsupportedOperationException();
    }

    public long getFreeSpace(FakeFile fake) {
        throw new UnsupportedOperationException();
    }

    public String getName(FakeFile fake) {
        String[] splited = fake.getPathField().split("/");
        return splited[splited.length - 1];
    }

    public String getParent(FakeFile fake) {
        String s = fake.getPathField();
        if (!s.equals("/")) {
            String[] split = s.split("/");
            String[] parent = Arrays.copyOf(split, split.length - 1);
            if (parent.length > 1) {
                return String.join("/", parent);
            } else {
                return "/";
            }
        }
        return "/";
    }

    public FakeFile getParentFile(FakeFile fake) {
        String parent_field = fake.getParent();
        FakeFile parent;

        if (parent_field == "/") {
            parent = root;
        } else {
            parent = this.pathToFakeFile(parent_field);
        }

        if (parent != null && parent.exists()) {
            return parent;
        } else {
            return null;
        }
    }

    public String getPath(FakeFile fake) {
        return fake.getPathField();
    }

    public long getTotalSpace(FakeFile fake) {
        throw new UnsupportedOperationException();
    }

    public long getUsableSpace(FakeFile fake) {
        throw new UnsupportedOperationException();
    }

    public int hashCode(FakeFile fake) {
        throw new UnsupportedOperationException();
    }

    public boolean isAbsolute(FakeFile fake) {
        throw new UnsupportedOperationException();
    }

    public boolean isDirectory(FakeFile fake) {
        return fake.isDirectory;
    }

    public boolean isFile(FakeFile fake) {
        return !fake.isDirectory;
    }

    public boolean isHidden(FakeFile fake) {
        return fake.getPathField().charAt(0) == '.';
    }

    public void setHidden(FakeFile fake, boolean hidden) {
        throw new UnsupportedOperationException();
    }

    public void touch(FakeFile fake) {
        throw new UnsupportedOperationException();
    }

    public long lastModified(FakeFile fake) {
        return fake.lastModified;
    }

    public long length(FakeFile fake) {
        throw new UnsupportedOperationException();
    }

    public String[] list(FakeFile fake, FilenameFilter filter) {
        throw new UnsupportedOperationException();
    }

    public String[] list(FakeFile fake) {
        throw new UnsupportedOperationException();
    }

    public FakeFile[] listFiles(FakeFile fake, FileFilter filter) {
        throw new UnsupportedOperationException();
    }

    public FakeFile[] listFiles(FakeFile fake, FilenameFilter filter) {
        throw new UnsupportedOperationException();
    }

    public FakeFile[] listFiles(FakeFile fake) {
        ArrayList<FakeFile> output = new ArrayList();
        for (Map.Entry<String, FakeFile> entrySet : fake.child.entrySet()) {
            FakeFile value = entrySet.getValue();
            output.add(value);
        }
        FakeFile[] a = new FakeFile[output.size()];
        output.toArray(a);
        return a;
    }

    public boolean mkdir(FakeFile fake) {
        fake.isDirectory = true;
        fake.chmod = 755;
        fake.lastModified = System.currentTimeMillis();
        FakeFile parent = fake.getParentFile();
        if (parent != null) {
            parent.addChild(fake);
            return true;
        } else {
            return false;
        }
    }

    public boolean mkdirs(FakeFile fake) {
        FakeFile tmp = null;
        FakeFile parent = fake.getParentFile();
        if (parent != null) {
            mkdir(fake);
            return true;
        } else {
            ArrayList<FakeFile> to_create = new ArrayList();
            String[] files = fake.getPathField().split("/");
            for (int i = 1; i < files.length; i++) {
                String out = String.join("/", Arrays.copyOf(files, i + 1));
                FakeFile nFakeFile = pathToFakeFile(out);
                if (!nFakeFile.exists()) {
                    tmp = new FakeFile(this, out);
                }
                to_create.add(tmp);
            }

            for (FakeFile to_create1 : to_create) {
                to_create1.mkdir();
            }
        }
        return true;
    }

    public boolean renameTo(FakeFile fake, FakeFile dest) {
        try {
            if (fake.exists()) {
                fake.setPathField(dest.getPathField());
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean setExecutable(FakeFile fake, boolean executable,
            boolean ownerOnly) {
        throw new UnsupportedOperationException();
    }

    public boolean setExecutable(FakeFile fake, boolean executable) {
        throw new UnsupportedOperationException();
    }

    public boolean setLastModified(FakeFile fake, long time) {
        fake.lastModified = time;
        return true;
    }

    public boolean setReadOnly(FakeFile fake) {
        throw new UnsupportedOperationException();
    }

    public boolean setReadable(FakeFile fake, boolean readable,
            boolean ownerOnly) {
        throw new UnsupportedOperationException();
    }

    public boolean setReadable(FakeFile fake, boolean readable) {
        throw new UnsupportedOperationException();
    }

    public boolean setWritable(FakeFile fake, boolean writable,
            boolean ownerOnly) {
        throw new UnsupportedOperationException();
    }

    public boolean setWritable(FakeFile fake, boolean writable) {
        throw new UnsupportedOperationException();
    }

    public String toString(FakeFile fake) {
        throw new UnsupportedOperationException();
    }

    public URI toURI(FakeFile fake) {
        throw new UnsupportedOperationException();
    }

    public URL toURL(FakeFile fake) throws MalformedURLException {
        throw new UnsupportedOperationException();
    }

    public InputStream getInputStream(FakeFile fake) throws IOException {
        throw new UnsupportedOperationException();
    }

    public OutputStream getOutputStream(FakeFile fake, boolean append) throws IOException {
        throw new UnsupportedOperationException();
    }

    public char getSeparatorChar() {
        return File.separatorChar;
    }

    public String getSeparator() {
        return File.separator;
    }

    public String getPathSeparator() {
        return File.pathSeparator;
    }

    public char getPathSeparatorChar() {
        return File.pathSeparatorChar;
    }

    public boolean isCaseSensitive() {
        return (getSeparatorChar() == '/');
    }

    public FakeFile pathToFakeFile(String s) {
        FakeFile f;
        if (s.equals("/")) {
            return root;
        } else {
            String[] files = s.split("/");
            f = root;
            for (int i = 1; i < files.length; i++) {
                if (f == null) {
                    f = null;
                } else {
                    String filepath = String.join("/", Arrays.copyOf(files, i + 1));
                    f = f.child.get(filepath);
                }
            }
        }
        if (f == null) {
            return new FakeFile(this, s);
        }
        return f;
    }

}
