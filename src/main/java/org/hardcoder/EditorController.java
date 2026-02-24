package org.hardcoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class EditorController {
    private final DocumentModel model;

    public EditorController(DocumentModel model) {
        this.model = model;
    }

    public void loadText(Path path) throws IOException {
        String content = Files.readString(path, StandardCharsets.UTF_8);
        model.setText(content);
        model.setPath(path);
    }

    public void saveText(Path path) throws IOException {
        Files.writeString(path, model.getText(), StandardCharsets.UTF_8);
        model.setPath(path);
    }

    // convenience overload to save provided text without mutating model
    public void saveText(Path path, String text) throws IOException {
        if (text == null) text = "";
        Files.writeString(path, text, StandardCharsets.UTF_8);
        model.setPath(path);
        model.setText(text);
    }

    public int find(String term, int fromIndex) {
        if (term == null || term.isEmpty()) return -1;
        String text = model.getText();
        return text.indexOf(term, Math.max(0, fromIndex));
    }

    public boolean replace(String term, String replacement, boolean replaceAll) {
        if (term == null || term.isEmpty()) return false;
        String text = model.getText();
        if (replaceAll) {
            model.setText(text.replace(term, replacement));
            return true;
        }
        int idx = text.indexOf(term);
        if (idx >= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(text, 0, idx).append(replacement).append(text.substring(idx + term.length()));
            model.setText(sb.toString());
            return true;
        }
        return false;
    }
}
