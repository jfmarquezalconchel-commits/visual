package org.hardcoder;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class EditorControllerTest {

    @Test
    void loadAndSaveText() throws Exception {
        DocumentModel model = new DocumentModel();
        EditorController ctrl = new EditorController(model);

        Path tmp = Files.createTempFile("testdoc", ".txt");
        String sample = "hola mundo";
        Files.writeString(tmp, sample);

        ctrl.loadText(tmp);
        assertEquals(sample, model.getText());

        // modify and save
        model.setText("changed");
        ctrl.saveText(tmp);
        assertEquals("changed", Files.readString(tmp));
    }

    @Test
    void findAndReplace() {
        DocumentModel model = new DocumentModel();
        model.setText("abc abc abc");
        EditorController ctrl = new EditorController(model);

        int idx = ctrl.find("abc", 0);
        assertEquals(0, idx);

        boolean ok = ctrl.replace("abc", "X", false);
        assertTrue(ok);
        assertEquals("X abc abc", model.getText());

        ctrl.replace("abc", "Y", true);
        assertEquals("X Y Y", model.getText());
    }
}

