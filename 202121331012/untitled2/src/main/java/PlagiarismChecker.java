import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

public class PlagiarismChecker {

    private static final int HASH_SIZE = 64; // SimHash的哈希位数
    private static final int SIMILARITY_THRESHOLD = 10; // 相似度阈值

    // 计算SimHash值
    private static long calculateSimHash(String text) {
        int[] hashBits = new int[HASH_SIZE];

        // 分词处理
        String[] words = text.split("\\s+");

        // 生成哈希值
        for (String word : words) {
            long wordHash = hash(word);
            for (int i = 0; i < HASH_SIZE; i++) {
                long bitmask = 1L << i;
                if ((wordHash & bitmask) != 0) {
                    hashBits[i] += 1;
                } else {
                    hashBits[i] -= 1;
                }
            }
        }

        // 构造SimHash值
        long simHash = 0;
        for (int i = 0; i < HASH_SIZE; i++) {
            if (hashBits[i] >= 0) {
                simHash |= 1L << i;
            }
        }

        return simHash;
    }

    // 计算海明距离
    private static int calculateHammingDistance(long hash1, long hash2) {
        long xor = hash1 ^ hash2;
        int distance = 0;
        while (xor != 0) {
            distance += 1;
            xor &= xor - 1;
        }
        return distance;
    }

    // 哈希函数
    private static long hash(String word) {
        // 在实际应用中，可以使用更复杂的哈希函数
        return word.hashCode();
    }

    // 从txt文件中读取内容
    private static String readTextFromFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String file1 = "E:\\JavaProject\\untitled2\\orig.txt";
        String file2 = "E:\\\\JavaProject\\\\untitled2\\\\orig_add.txt";
        String answerFile = "E:\\\\JavaProject\\\\untitled2\\\\answer.txt";

        try {
            // 从文件中读取论文内容
            String paper1 = readTextFromFile(file1);
            String paper2 = readTextFromFile(file2);

            // 计算SimHash值
            long hash1 = calculateSimHash(paper1);
            long hash2 = calculateSimHash(paper2);

            // 计算海明距离
            int distance = calculateHammingDistance(hash1, hash2);

            // 计算重复率
            double similarity = 1.0 - (double) distance / HASH_SIZE;

            // 折算成百分制并保留小数点后两位
            double similarityPercentage = similarity * 100;
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String formattedSimilarity = decimalFormat.format(similarityPercentage);

            // 输出结果到答案文件
            try (PrintWriter writer = new PrintWriter(new FileWriter(answerFile))) {
                writer.println("Paper 1: " + paper1);
                writer.println("Paper 2: " + paper2);
                writer.println("相似度: " + formattedSimilarity + "%");

            }

            System.out.println("Plagiarism check completed. Results written to " + answerFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}