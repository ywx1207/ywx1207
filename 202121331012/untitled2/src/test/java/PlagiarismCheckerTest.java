import org.junit.Test;

import static org.junit.Assert.*;

public class PlagiarismCheckerTest {

    @Test
    public void main() {
        PlagiarismChecker plagiarismChecker = new PlagiarismChecker();
        long luj = plagiarismChecker.calculateSimHash("今天心");
        System.out.println(luj);
    }
}