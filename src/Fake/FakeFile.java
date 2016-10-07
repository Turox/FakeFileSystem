package Fake;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
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
 * A testing version of java.io.File. Each method is overridden to call back to
 * a FakeFileSystem interface, allowing fine grained control of what files are
 * allowed to do. The original java.io.File methods are still available to be
 * called with the same method name with an underscore prepended. For example, a
 * call of .exists() on FakeFile will call back to a provided FakeFileSystem
 * implementation, where ._exists() will call the original File implementation.
 */
public class FakeFile extends File implements Cloneable {

    private static final long serialVersionUID = 6833759609396625529L;

    private static final Field pathField;
    private static final Field prefixLengthField;

    static {
        try {
            pathField = File.class.getDeclaredField("path");
            pathField.setAccessible(true);
            prefixLengthField = File.class.getDeclaredField("prefixLength");
            prefixLengthField.setAccessible(true);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    final FakeFileSystem fs;
    String key = null;
    public byte[] data;
    public boolean isDirectory = false;
    public int chmod;
    public long lastModified;
    public String owner = "aluno";
    public Map<String, FakeFile> child = new HashMap<String, FakeFile>();

    /**
     * http://www.docjar.com/html/api/java/io/File.java.html
     */
    public FakeFile(FakeFileSystem fs, String pathname) {
        super(pathname);
        this.fs = fs;
        setPathField(fixSlashes(pathname));
    }

    /**
     * http://www.docjar.com/html/api/java/io/File.java.html
     */
    public FakeFile(FakeFileSystem fs, File parent, String child) {
        this(fs, "");
        if (child == null) {
            throw new NullPointerException();
        }
        if (parent == null) {
            setPathField(fixSlashes(child));
        } else {
            setPathField(calculatePath(getPathField(parent), child));
        }
    }

    /**
     * http://www.docjar.com/html/api/java/io/File.java.html
     */
    public FakeFile(FakeFileSystem fs, String parent, String child) {
        this(fs, "");
        if (child == null) {
            throw new NullPointerException();
        }
        if (parent == null) {
            setPathField(fixSlashes(child));
        } else {
            setPathField(calculatePath(parent, child));
        }
    }

    /**
     * http://www.docjar.com/html/api/java/io/File.java.html
     */
    public FakeFile(FakeFileSystem fs, URI uri) {
        this(fs, "");
        checkURI(uri);
        setPathField(fixSlashes(uri.getPath()));
    }

    /**
     * http://www.docjar.com/html/api/java/io/File.java.html
     */
    public FakeFile(FakeFileSystem fs, File file) {
        this(fs, "");
        setPathField(getPathField(file));
        setPrefixLengthField(getPrefixLength(file));
    }

    public String getPathField() {
        return getPathField(this);
    }

    public static String getPathField(File file) {
        try {
            return (String) pathField.get(file);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getPrefixLength(File file) {
        try {
            return prefixLengthField.getInt(file);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPrefixLengthField() {
        return getPrefixLength(this);
    }

    public FakeFile setPathField(String path) {
        try {
            pathField.set(this, path);
            return this;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public FakeFile setPrefixLengthField(int prefixLength) {
        try {
            prefixLengthField.setInt(this, prefixLength);
            return this;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public FakeFile setKey(String key) {
        this.key = key;
        return this;
    }

    public String getKey() {
        return this.key;
    }

    @Override
    public boolean canExecute() {
        return this.fs.canExecute(this);
    }

    @Override
    public boolean canRead() {
        return this.fs.canRead(this);
    }

    @Override
    public boolean canWrite() {
        return this.fs.canWrite(this);
    }

    @Override
    public boolean createNewFile() throws IOException {
        return this.fs.createNewFile(this);
    }

    @Override
    public boolean delete() {
        return this.fs.delete(this);
    }

    @Override
    public void deleteOnExit() {
        this.fs.deleteOnExit(this);
    }

    @Override
    public boolean exists() {
        return this.fs.exists(this);
    }

    @Override
    public FakeFile getAbsoluteFile() {
        return this.fs.getAbsoluteFile(this);
    }

    @Override
    public String getAbsolutePath() {
        return this.fs.getAbsolutePath(this);
    }

    @Override
    public FakeFile getCanonicalFile() throws IOException {
        return this.fs.getCanonicalFile(this);
    }

    @Override
    public String getCanonicalPath() throws IOException {
        return this.fs.getCanonicalPath(this);
    }

    @Override
    public long getFreeSpace() {
        return this.fs.getFreeSpace(this);
    }

    @Override
    public FakeFile getParentFile() {
        return this.fs.getParentFile(this);
    }

    @Override
    public long getTotalSpace() {
        return this.fs.getTotalSpace(this);
    }

    @Override
    public long getUsableSpace() {
        return this.fs.getUsableSpace(this);
    }

    @Override
    public boolean isAbsolute() {
        return this.fs.isAbsolute(this);
    }

    @Override
    public boolean isDirectory() {
        return this.fs.isDirectory(this);
    }

    @Override
    public boolean isFile() {
        return this.fs.isFile(this);
    }

    @Override
    public boolean isHidden() {
        return this.fs.isHidden(this);
    }

    @Override
    public long lastModified() {
        return this.fs.lastModified(this);
    }

    @Override
    public long length() {
        return this.fs.length(this);
    }

    @Override
    public String[] list() {
        return this.fs.list(this);
    }

    @Override
    public String[] list(FilenameFilter filter) {
        return this.fs.list(this, filter);
    }

    @Override
    public FakeFile[] listFiles() {
        return this.fs.listFiles(this);
    }

    @Override
    public FakeFile[] listFiles(FileFilter filter) {
        return this.fs.listFiles(this, filter);
    }

    @Override
    public FakeFile[] listFiles(FilenameFilter filter) {
        return this.fs.listFiles(this, filter);
    }

    @Override
    public boolean mkdir() {
        return this.fs.mkdir(this);
    }

    @Override
    public boolean mkdirs() {
        return this.fs.mkdirs(this);
    }

    @Override
    public boolean renameTo(File dest) {
        return this.fs.renameTo(this, (FakeFile) dest);
    }

    @Override
    public boolean setExecutable(boolean executable, boolean ownerOnly) {
        return this.fs.setExecutable(this, executable, ownerOnly);
    }

    @Override
    public boolean setExecutable(boolean executable) {
        return this.fs.setExecutable(this, executable);
    }

    @Override
    public boolean setLastModified(long time) {
        return this.fs.setLastModified(this, time);
    }

    @Override
    public boolean setReadable(boolean readable, boolean ownerOnly) {
        return this.fs.setReadable(this, readable, ownerOnly);
    }

    @Override
    public boolean setReadable(boolean readable) {
        return this.fs.setReadable(this, readable);
    }

    @Override
    public boolean setReadOnly() {
        return this.fs.setReadOnly(this);
    }

    @Override
    public boolean setWritable(boolean writable, boolean ownerOnly) {
        return this.fs.setWritable(this, writable, ownerOnly);
    }

    @Override
    public boolean setWritable(boolean writable) {
        return this.fs.setWritable(this, writable);
    }

    @Override
    public String toString() {
        return this.fs.toString(this);
    }

    @Override
    public URI toURI() {
        return this.fs.toURI(this);
    }

    /**
     * @deprecated because File.toURL() is @deprecated
     */
    @Deprecated
    @Override
    public URL toURL() throws MalformedURLException {
        return this.fs.toURL(this);
    }

    @Override
    public boolean equals(Object obj) {
        return this.fs.equals(this, obj);
    }

    @Override
    public int hashCode() {
        return this.fs.hashCode(this);
    }

    @Override
    public int compareTo(File pathname) {
        return this.fs.compareTo(this, pathname);
    }

    @Override
    public String getName() {
        return this.fs.getName(this);
    }

    @Override
    public String getParent() {
        return this.fs.getParent(this);
    }

    @Override
    public String getPath() {
        return this.fs.getPath(this);
    }

    public void setHidden(boolean hidden) {
        this.fs.setHidden(this, hidden);
    }

    public void touch() {
        this.fs.touch(this);
    }

    public boolean _canExecute() {
        return super.canExecute();
    }

    public boolean _canRead() {
        return super.canRead();
    }

    public boolean _canWrite() {
        return super.canWrite();
    }

    public int _compareTo(File pathname) {
        return super.compareTo(pathname);
    }

    public boolean _createNewFile() throws IOException {
        return super.createNewFile();
    }

    public boolean _delete() {
        return super.delete();
    }

    public void _deleteOnExit() {
        super.deleteOnExit();
    }

    public boolean _equals(Object obj) {
        return super.equals(obj);
    }

    public boolean _exists() {
        return super.exists();
    }

    public File _getAbsoluteFile() {
        return super.getAbsoluteFile();
    }

    public String _getAbsolutePath() {
        return super.getAbsolutePath();
    }

    public File _getCanonicalFile() throws IOException {
        return super.getCanonicalFile();
    }

    public String _getCanonicalPath() throws IOException {
        return super.getCanonicalPath();
    }

    public long _getFreeSpace() {
        return super.getFreeSpace();
    }

    public String _getName() {
        return super.getName();
    }

    public String _getParent() {
        return super.getParent();
    }

    public File _getParentFile() {
        return super.getParentFile();
    }

    public String _getPath() {
        return super.getPath();
    }

    public long _getTotalSpace() {
        return super.getTotalSpace();
    }

    public long _getUsableSpace() {
        return super.getUsableSpace();
    }

    public int _hashCode() {
        return super.hashCode();
    }

    public boolean _isAbsolute() {
        return super.isAbsolute();
    }

    public boolean _isDirectory() {
        return super.isDirectory();
    }

    public boolean _isFile() {
        return super.isFile();
    }

    public boolean _isHidden() {
        return super.isHidden();
    }

    public long _lastModified() {
        return super.lastModified();
    }

    public long _length() {
        return super.length();
    }

    public String[] _list() {
        return super.list();
    }

    public String[] _list(FilenameFilter filter) {
        return super.list(filter);
    }

    public File[] _listFiles() {
        return super.listFiles();
    }

    public File[] _listFiles(FileFilter filter) {
        return super.listFiles(filter);
    }

    public File[] _listFiles(FilenameFilter filter) {
        return super.listFiles(filter);
    }

    public boolean _mkdir() {
        return super.mkdir();
    }

    public boolean _mkdirs() {
        return super.mkdirs();
    }

    public boolean _renameTo(File dest) {
        return super.renameTo(dest);
    }

    public boolean _setExecutable(boolean executable, boolean ownerOnly) {
        return super.setExecutable(executable, ownerOnly);
    }

    public boolean _setExecutable(boolean executable) {
        return super.setExecutable(executable);
    }

    public boolean _setLastModified(long time) {
        return super.setLastModified(time);
    }

    public boolean _setReadable(boolean readable, boolean ownerOnly) {
        return super.setReadable(readable, ownerOnly);
    }

    public boolean _setReadable(boolean readable) {
        return super.setReadable(readable);
    }

    public boolean _setReadOnly() {
        return super.setReadOnly();
    }

    public boolean _setWritable(boolean writable, boolean ownerOnly) {
        return super.setWritable(writable, ownerOnly);
    }

    public boolean _setWritable(boolean writable) {
        return super.setWritable(writable);
    }

    public String _toString() {
        return super.toString();
    }

    public URI _toURI() {
        return super.toURI();
    }

    /**
     * @deprecated because File.toURL() is @deprecated
     */
    @Deprecated
    public URL _toURL() throws MalformedURLException {
        return super.toURL();
    }

    /**
     * http://www.docjar.com/html/api/java/io/File.java.html
     */
    void checkURI(URI uri) {
        if (!uri.isAbsolute()) {
            throw new IllegalArgumentException(uri.toString());
        } else if (!uri.getRawSchemeSpecificPart().startsWith("/")) {
            throw new IllegalArgumentException(uri.toString());
        }

        String temp = uri.getScheme();
        if (temp == null || !temp.equals("file")) {
            throw new IllegalArgumentException(uri.toString());
        }

        temp = uri.getRawPath();
        if (temp == null || temp.length() == 0) {
            throw new IllegalArgumentException(uri.toString());
        }

        if (uri.getRawAuthority() != null) {
            throw new IllegalArgumentException("authority " + uri.toString());
        }

        if (uri.getRawQuery() != null) {
            throw new IllegalArgumentException("query " + uri.toString());
        }

        if (uri.getRawFragment() != null) {
            throw new IllegalArgumentException("fragment " + uri.toString());
        }
    }

    /**
     * http://www.docjar.com/html/api/java/io/File.java.html
     */
    String calculatePath(String dirPath, String name) {
        dirPath = fixSlashes(dirPath);
        if (!name.equals("") || dirPath.equals("")) {
            // Remove all the proceeding separator chars from name
            name = fixSlashes(name);

            int separatorIndex = 0;
            while ((separatorIndex < name.length())
                    && (name.charAt(separatorIndex) == fs.getSeparatorChar())) {
                separatorIndex++;
            }
            if (separatorIndex > 0) {
                name = name.substring(separatorIndex, name.length());
            }

            // Ensure there is a separator char between dirPath and name
            if (dirPath.length() > 0
                    && (dirPath.charAt(dirPath.length() - 1) == fs.getSeparatorChar())) {
                return dirPath + name;
            }
            return dirPath + fs.getSeparatorChar() + name;
        }

        return dirPath;
    }

    /**
     * http://www.docjar.com/html/api/java/io/File.java.html
     *
     * The purpose of this method is to take a path and fix the slashes up. This
     * includes changing them all to the current platforms fileSeparator and
     * removing duplicates.
     */
    String fixSlashes(String origPath) {
        int uncIndex = 1;
        int length = origPath.length(), newLength = 0;
        if (fs.getSeparatorChar() == '/') {
            uncIndex = 0;
        } else if (length > 2 && origPath.charAt(1) == ':') {
            uncIndex = 2;
        }

        boolean foundSlash = false;
        char newPath[] = origPath.toCharArray();
        for (int i = 0; i < length; i++) {
            char pathChar = newPath[i];
            if ((fs.getSeparatorChar() == '\\' && pathChar == '\\')
                    || pathChar == '/') {
                /* UNC Name requires 2 leading slashes */
                if ((foundSlash && i == uncIndex) || !foundSlash) {
                    newPath[newLength++] = fs.getSeparatorChar();
                    foundSlash = true;
                }
            } else {
                // check for leading slashes before a drive
                if (pathChar == ':'
                        && uncIndex > 0
                        && (newLength == 2 || (newLength == 3 && newPath[1] == fs.getSeparatorChar()))
                        && newPath[0] == fs.getSeparatorChar()) {
                    newPath[0] = newPath[newLength - 1];
                    newLength = 1;
                    // allow trailing slash after drive letter
                    uncIndex = 2;
                }
                newPath[newLength++] = pathChar;
                foundSlash = false;
            }
        }
        // remove trailing slash
        if (foundSlash
                && (newLength > (uncIndex + 1) || (newLength == 2
                && newPath[0] != fs.getSeparatorChar()))) {
            newLength--;
        }

        return new String(newPath, 0, newLength);
    }

    public void addChild(FakeFile f) {
        child.put(f.getPathField(), f);
    }

    public boolean childExists(FakeFile f) {
        return child.get(f.getPathField()) != null;
    }

    public void removeChild(FakeFile f) {
        child.remove(f.getPathField());
    }

    protected FakeFile clone() throws CloneNotSupportedException {
        FakeFile clone = (FakeFile) super.clone();
        clone.child = new HashMap<String, FakeFile>();
        for (Map.Entry<String, FakeFile> entrySet : this.child.entrySet()) {
            String key1 = entrySet.getKey();
            FakeFile value = entrySet.getValue().clone();
            clone.child.put(key1, value);
        }
        return clone;
    }

}
