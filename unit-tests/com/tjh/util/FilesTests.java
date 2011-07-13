package com.tjh.util;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileFilter;

import static com.tjh.test.ArgumentMatchers.notEqualTo;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

public class FilesTests {
    private File mockDir;
    private FileFilter mockFileFilter;

    @Test(expected = IllegalArgumentException.class)
    public void findRequiresDirectory() {
        File mockFile = createMock(File.class);
        expect(mockFile.isDirectory()).andReturn(false);
        replay(mockFile);

        Files.find(mockFile, null);
    }

    @Test
    public void findRecursesSubdirectories(){
        File mockSubDir = createMock(File.class);
        expect(mockDir.listFiles(notEqualTo(mockFileFilter))).andReturn(new File[]{mockSubDir});
        expect(mockDir.listFiles(mockFileFilter)).andStubReturn(new File[]{});
        expect(mockSubDir.isDirectory()).andReturn(true).atLeastOnce();
        expect(mockSubDir.listFiles(notEqualTo(mockFileFilter))).andReturn(new File[]{});
        expect(mockSubDir.listFiles(mockFileFilter)).andStubReturn(new File[]{});
        replay(mockDir, mockSubDir, mockFileFilter);
        
        Files.find(mockDir, mockFileFilter);
        verify(mockDir, mockSubDir, mockFileFilter);
    }

    @Test
    public void findCallsListFilesWithFilter(){
        expect(mockDir.listFiles(notEqualTo(mockFileFilter))).andStubReturn(new File[]{});
        expect(mockDir.listFiles(mockFileFilter)).andReturn(new File[]{});
        replay(mockDir, mockFileFilter);

        Files.find(mockDir, mockFileFilter);
        verify(mockDir, mockFileFilter);
    }

    @Before
    public void before() {
        mockDir = createMock(File.class);
        expect(mockDir.isDirectory()).andReturn(true);
        mockFileFilter = createMock(FileFilter.class);
    }
}
