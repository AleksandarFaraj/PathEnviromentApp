package it.enow;

import it.enow.dialogs.AddToPathDialog;
import it.enow.windows.RegistryHandler;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VerifyWindowsTest {
    private AddToPathDialog addToPath;
    private Verify verify;

    @Before
    public void setUp() throws Exception {
        addToPath = new AddToPathDialog(null,verify,null);
        verify = new Verify(new RegistryHandler());
    }

    @Test
    public void verifyAndCleanDir_normalUse_returnsWhatWeExpect() throws IOException {
        assertEquals("C:\\", verify.verifyAndCleanDir("C:\\"));
        assertEquals(System.getenv("SystemRoot"), verify.verifyAndCleanDir("%SystemRoot%"));
    }

    @Test
    public void verifyAndCleanDir_normalUse_cleansTheDirectory() throws IOException {
        assertEquals("C:\\", verify.verifyAndCleanDir("C:\\\\"));
    }

    @Test(expected = IOException.class)
    public void verifyAndClean_invalidPath_throwsError() throws IOException {
        verify.verifyAndCleanDir("somerandompath");
    }

    @Test
    public void environmentifyDirectory_normalUse_replacedAllVariablesWithValuesFromTheEnvironment() {
        assertEquals(System.getenv("SystemRoot"), verify.environmentifyDirectory("%SystemRoot%"));
    }

    @Test(expected = NullPointerException.class)
    public void environmentifyDirectory_usingASystemVariableThatDoesNotExist_shouldThrowError() {
        assertEquals(System.getenv("SystemRoot"), verify.environmentifyDirectory("%Sy23efwwe423%"));
    }

    @Test
    public void verifyAndCleanPath_callingWithOneCorrectPath_shouldReturnOneCorrectPath() {
        assertEquals("C:\\", verify.verifyAndCleanPath("C:\\"));
    }

    @Test
    public void verifyAndCleanPath_callingWithTwoCorrectPaths_shouldReturnTwoCorrectPaths() {
        assertEquals("C:\\;C:\\Windows", verify.verifyAndCleanPath("C:\\;C:\\Windows"));
    }

    @Test
    public void verifyAndCleanPath_callingWithTwoCorrectAndOneWrongPathAndWePressYes_shouldReturnTwoCorrectPathAndRemoveInvalidOne() {
        JOptionProxy jOptionProxy = new JOptionProxy() {
            @Override
            public int showConfirmDialog(Component c, String message, String s, int i) {
                return JOptionPane.YES_OPTION;
            }
        };
        verify.inject(jOptionProxy);
        assertEquals("C:\\;C:\\Windows", verify.verifyAndCleanPath("C:\\;C:\\Windows;WKQLJDASLKDJQWE123:::DD"));
    }
    @Test
    public void verifyAndCleanPath_callingWithTwoCorrectAndOneWrongPathAndWePressNo_shouldReturnAll() {
        JOptionProxy jOptionProxy = new JOptionProxy() {
            @Override
            public int showConfirmDialog(Component c, String message, String s, int i) {
                return JOptionPane.NO_OPTION;
            }
        };
        verify.inject(jOptionProxy);
        assertEquals("C:\\;C:\\Windows;WKQLJDASLKDJQWE123:::DD", verify.verifyAndCleanPath("C:\\;C:\\Windows;WKQLJDASLKDJQWE123:::DD"));
    }
    @Test
    public void verifyAndCleanPath_callingWithTwoCorrectAndOneBrokenEnvironmentVariablePathAndWePressNo_shouldReturnTwoCorrectPathAndPromptForDeletionOnInvalid() {
        JOptionProxy jOptionProxy = new JOptionProxy() {
            @Override
            public int showConfirmDialog(Component c, String message, String s, int i) {
                assertTrue("Does not contains our message", message.contains("%dSECRET%"));
                return JOptionPane.NO_OPTION;
            }
        };
        verify.inject(jOptionProxy);
        assertEquals("C:\\;C:\\Windows;%dSECRET%", verify.verifyAndCleanPath("C:\\;C:\\Windows;%dSECRET%"));
    }
    @Test
    public void verifyAndCleanPath_callingWithSomeTrailingCommasAndSlashes_shouldReturnTwoCorrectWithoutTrailingCommaOrSlashes() {
        assertEquals("C:\\;C:\\Windows", verify.verifyAndCleanPath("C:\\;C:\\Windows\\;;;;;;;;;;;"));
    }

}