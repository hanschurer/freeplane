package org.freeplane.core.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileUtilsTest {
    
    private File testFile;
    private String testContent;
    
    @Before
    public void setUp() throws IOException {
        testFile = File.createTempFile("test", ".txt");
        testContent = "Sample Content\nTest Content";
        FileUtils.dumpStringToFile(testContent, testFile, StandardCharsets.UTF_8.name());
    }

    @Test
    public void testReadFile() {
        String content = FileUtils.readFile(testFile);
        assertNotNull("File content should not be null", content);
        assertTrue("File content should contain test text", content.contains(testContent));
    }

    @Test 
    public void testGetExtension() {
        assertEquals("txt", FileUtils.getExtension(testFile));
        assertEquals("doc", FileUtils.getExtension("test.doc"));
        assertEquals("", FileUtils.getExtension("test"));
        assertNull(FileUtils.getExtension((String)null));
    }

    @Test
    public void testRemoveExtension() {
        assertEquals("test", FileUtils.removeExtension("test.txt"));
        assertEquals("path/to/file", FileUtils.removeExtension("path/to/file.doc"));
        assertEquals("noext", FileUtils.removeExtension("noext"));
    }

    @Test
    public void testCreateDirectory() {
        String tempDir = System.getProperty("java.io.tmpdir") + "/testDir" + System.currentTimeMillis();
        assertTrue(FileUtils.createDirectory(tempDir));
        File dir = new File(tempDir);
        assertTrue(dir.exists() && dir.isDirectory());
        dir.delete();
    }

    @Test
    public void testSlurpFile() throws IOException {
        String content = FileUtils.slurpFile(testFile);
        assertEquals(testContent, content);
    }

    @Test
    public void testGetAbsoluteFile() {
        String baseDir = "/base/dir";
        String relativePath = "test/file.txt";
        String absolutePath = "/absolute/path/file.txt";
        
        File relativeFile = FileUtils.getAbsoluteFile(baseDir, relativePath);
        assertEquals(new File(baseDir, relativePath), relativeFile);
        
        File absoluteFile = FileUtils.getAbsoluteFile(baseDir, absolutePath);
        assertEquals(new File(absolutePath), absoluteFile);
    }

    @Test
    public void testDefaultCharset() {
        assertNotNull(FileUtils.defaultCharset());
    }

    @Test
    public void testCopyFile() throws IOException {
        File sourceFile = File.createTempFile("source", ".txt");
        File destFile = File.createTempFile("dest", ".txt");
        String testData = "Test data for copy file\nMultiple lines\nMulti-language test";
        
        FileUtils.dumpStringToFile(testData, sourceFile, StandardCharsets.UTF_8.name());
        FileUtils.copyFile(sourceFile, destFile);
        
        String copiedContent = FileUtils.slurpFile(destFile);
        assertEquals(testData, copiedContent);
        
        sourceFile.delete();
        destFile.delete();
    }

    @Test(expected = IOException.class)
    public void testCopyFileWithNonExistentSource() throws IOException {
        File nonExistentFile = new File("nonexistent.txt");
        File destFile = File.createTempFile("dest", ".txt");
        FileUtils.copyFile(nonExistentFile, destFile);
    }

    @Test
    public void testSetHidden() throws InterruptedException {
        if(System.getProperty("os.name").startsWith("Win")) {
            FileUtils.setHidden(testFile, true, true);
            assertTrue(testFile.isHidden());
            
            FileUtils.setHidden(testFile, false, true);
            assertFalse(testFile.isHidden());
        }
    }

    @Test
    public void testIsAbsolutePath() {
        String osName = System.getProperty("os.name");
        if(osName.startsWith("Win")) {
            assertTrue(FileUtils.isAbsolutePath("C:\\test\\file.txt"));
            assertFalse(FileUtils.isAbsolutePath("test\\file.txt"));
        } else {
            assertTrue(FileUtils.isAbsolutePath("/test/file.txt"));
            assertFalse(FileUtils.isAbsolutePath("test/file.txt"));
        }
    }
}