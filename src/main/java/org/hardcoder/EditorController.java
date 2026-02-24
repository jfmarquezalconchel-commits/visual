package org.hardcoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditorController {
    private static final Logger LOGGER = Logger.getLogger(EditorController.class.getName());
    private final DocumentModel model;

    public EditorController(DocumentModel model) {
        this.model = model;
    }

    public void loadText(Path path) throws IOException {
        LOGGER.info(() -> "Loading text from: " + path);
        String content = Files.readString(path, StandardCharsets.UTF_8);
        model.setText(content);
        model.setPath(path);
        LOGGER.fine(() -> "Loaded text length=" + (content == null ? 0 : content.length()));
    }

    public void saveText(Path path) throws IOException {
        LOGGER.info(() -> "Saving current model text to: " + path);
        Files.writeString(path, model.getText(), StandardCharsets.UTF_8);
        model.setPath(path);
    }

    // convenience overload to save provided text without mutating model
    public void saveText(Path path, String text) throws IOException {
        LOGGER.info(() -> "Saving provided text to: " + path);
        if (text == null) text = "";
        Files.writeString(path, text, StandardCharsets.UTF_8);
        model.setPath(path);
        model.setText(text);
    }

    public int find(String term, int fromIndex) {
        if (term == null || term.isEmpty()) return -1;
        String text = model.getText();
        int idx = text.indexOf(term, Math.max(0, fromIndex));
        LOGGER.fine(() -> "Find term='" + term + "' fromIndex=" + fromIndex + " -> " + idx);
        return idx;
    }

    public boolean replace(String term, String replacement, boolean replaceAll) {
        if (term == null || term.isEmpty()) return false;
        String text = model.getText();
        if (replaceAll) {
            String replaced = text.replace(term, replacement);
            model.setText(replaced);
            LOGGER.info(() -> "Replaced all occurrences of '" + term + "'");
            return true;
        }
        int idx = text.indexOf(term);
        if (idx >= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(text, 0, idx).append(replacement).append(text.substring(idx + term.length()));
            model.setText(sb.toString());
            LOGGER.info(() -> "Replaced one occurrence of '" + term + "' at index " + idx);
            return true;
        }
        LOGGER.fine(() -> "No occurrence of '" + term + "' found to replace");
        return false;
    }
}
