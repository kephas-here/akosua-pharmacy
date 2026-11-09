package pharmacy;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PharmacyTrackerTest {

    private List<String> runProgram() {
        PrintStream original = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
        try {
            PharmacyTracker.main(new String[0]);
        } finally {
            System.setOut(original);
        }
        return captured.toString().lines().toList();
    }

    @Test
    void outputMatchesTheSavedRebuildOutput() throws IOException {
        List<String> expected = Files.readAllLines(Path.of("actual-output-after.txt"));

        assertEquals(expected, runProgram());
    }

    @Test
    void onlyTheRefusedSaleDiffersFromTheOriginal() throws IOException {
        List<String> original = Files.readAllLines(Path.of("expected-output-before.txt"));
        List<String> rebuilt = runProgram();

        assertEquals(original.size(), rebuilt.size());
        List<Integer> changedLines = new ArrayList<>();
        for (int i = 0; i < original.size(); i++) {
            if (!original.get(i).equals(rebuilt.get(i))) {
                changedLines.add(i + 1);
            }
        }
        assertEquals(List.of(4, 9, 10), changedLines);
    }
}
