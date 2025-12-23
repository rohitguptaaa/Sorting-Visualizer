import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
public class Sort extends JPanel {
    int[] array;
    int arraySize = 60;
    private final int delay = 20;
    String CurrAlgo = "Bubble Sort";
    int currIndex = -1;
    int comparisons = 0;

    private JSlider slider;
    private JLabel label;
    private JTextArea info;
    private String PseudoCode;

    public void genArray() {
        array = new int[arraySize];
        Random r = new Random();
        for (int i = 0; i < arraySize; i++) {
            array[i] = r.nextInt(500) + 50;
        }
        currIndex = -1;
    }

    public void PseudoCode() {
        switch (CurrAlgo) {
            case "Bubble Sort":
                PseudoCode = "Bubble Sort:\nO(n²) Avg/Worst, O(n) Best\n" +
                        "1. Compare adjacent elements.\n2. Swap if needed.\n3. Repeat.\n";
                break;
            case "Insertion Sort":
                PseudoCode = "Insertion Sort:\nO(n²) Avg/Worst, O(n) Best\n" +
                        "1. Pick next element.\n2. Compare & insert.\n";
                break;
            case "Selection Sort":
                PseudoCode = "Selection Sort:\nO(n²) Avg/Worst/Best\n" +
                        "1. Find min in unsorted part.\n2. Swap with first unsorted.\n";
                break;
            case "Merge Sort":
                PseudoCode = "Merge Sort:\nO(n log n) Avg/Worst/Best\n" +
                        "1. Divide & conquer.\n2. Sort halves.\n3. Merge.\n";
                break;
            case "Quick Sort":
                PseudoCode = "Quick Sort:\nO(n log n) Avg/Best, O(n²) Worst\n" +
                        "1. Choose pivot.\n2. Partition.\n3. Recur.\n";
                break;
            case "Counting Sort":
                PseudoCode = "Counting Sort:\nO(n + k)\nStable if done carefully\n" +
                        "1. Count frequency.\n2. Modify count.\n3. Build output.\n";
                break;
            case "Radix Sort":
                PseudoCode = "Radix Sort:\nO(nk) where k = #digits\n" +
                        "1. Sort by each digit (LSD first).\n2. Use stable sort like Counting Sort.\n";
                break;
            case "Bucket Sort":
                PseudoCode = "Bucket Sort:\nO(n + n²) worst, O(n) best\n" +
                        "1. Create buckets.\n2. Distribute.\n3. Sort each bucket.\n4. Merge.\n";
                break;
            default:
                PseudoCode = "Select a sorting algorithm.";
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int bar = getWidth() / arraySize;
        for (int i = 0; i < array.length; i++) {
            int h = array[i];
            g.setColor(i == currIndex ? Color.RED : Color.BLACK);
            g.fillRect(i * bar, getHeight() - h, bar, h);

            // Draw partition line between bars
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(i * bar, 0, i * bar, getHeight());
        }
    }


    public void eroor() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void swap(int i, int j) {
        int t = array[i];
        array[i] = array[j];
        array[j] = t;
    }

    private void bubbleSort() {
        for (int i = 0; i < array.length - 1; i++)
            for (int j = 0; j < array.length - i - 1; j++) {
                currIndex = j;
                comparisons++;
                if (array[j] > array[j + 1]) swap(j, j + 1);
                repaint(); eroor();
            }
        currIndex = -1;
    }

    private void selectionSort() {
        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                currIndex = j;
                comparisons++;
                if (array[j] < array[minIndex]) minIndex = j;
                repaint(); eroor();
            }
            if (minIndex != i) swap(i, minIndex);
        }
        currIndex = -1;
    }

    private void insertionSort() {
        for (int i = 1; i < array.length; i++) {
            int key = array[i], j = i - 1;
            while (j >= 0 && array[j] > key) {
                comparisons++;
                array[j + 1] = array[j];
                currIndex = j; j--;
                repaint(); eroor();
            }
            array[j + 1] = key;
            repaint(); eroor();
        }
        currIndex = -1;
    }

    private void mergeSort(int l, int r) {
        if (l < r) {
            int m = l + (r - l) / 2;
            mergeSort(l, m); mergeSort(m + 1, r);
            merge(l, m, r);
        }
    }

    private void merge(int l, int m, int r) {
        int[] L = Arrays.copyOfRange(array, l, m + 1);
        int[] R = Arrays.copyOfRange(array, m + 1, r + 1);
        int i = 0, j = 0, k = l;
        while (i < L.length && j < R.length) {
            currIndex = k;
            comparisons++;
            array[k++] = (L[i] <= R[j]) ? L[i++] : R[j++];
            repaint(); eroor();
        }
        while (i < L.length) array[k++] = L[i++];
        while (j < R.length) array[k++] = R[j++];
    }

    private void quickSort(int low, int high) {
        if (low < high) {
            int pi = partition(low, high);
            quickSort(low, pi - 1); quickSort(pi + 1, high);
        }
    }

    private int partition(int low, int high) {
        int pivot = array[high], i = low - 1;
        for (int j = low; j < high; j++) {
            currIndex = j;
            comparisons++;
            if (array[j] < pivot) swap(++i, j);
            repaint(); eroor();
        }
        swap(i + 1, high); return i + 1;
    }

    private void countingSort() {
        int max = Arrays.stream(array).max().getAsInt();
        int[] count = new int[max + 1];
        for (int val : array) count[val]++;
        int index = 0;
        for (int i = 0; i <= max; i++)
            while (count[i]-- > 0) {
                array[index++] = i; currIndex = index - 1;
                repaint(); eroor();
            }
    }

    private void radixSort() {
        int max = Arrays.stream(array).max().getAsInt();
        for (int exp = 1; max / exp > 0; exp *= 10) countingSortForRadix(exp);
    }

    private void countingSortForRadix(int exp) {
        int[] output = new int[array.length];
        int[] count = new int[10];
        for (int val : array) count[(val / exp) % 10]++;
        for (int i = 1; i < 10; i++) count[i] += count[i - 1];
        for (int i = array.length - 1; i >= 0; i--)
            output[--count[(array[i] / exp) % 10]] = array[i];
        for (int i = 0; i < array.length; i++) {
            array[i] = output[i]; currIndex = i;
            repaint(); eroor();
        }
    }

    private void bucketSort() {
        int max = Arrays.stream(array).max().getAsInt();
        int bucketCount = 10;
        List<Integer>[] buckets = new List[bucketCount];
        for (int i = 0; i < bucketCount; i++) buckets[i] = new ArrayList<>();
        for (int val : array) {
            int idx = val * bucketCount / (max + 1);
            buckets[idx].add(val);
        }
        int index = 0;
        for (List<Integer> bucket : buckets) {
            Collections.sort(bucket);
            for (int val : bucket) {
                array[index++] = val; currIndex = index - 1;
                repaint(); eroor();
            }
        }
    }

    public Sort() {
        genArray();
        JFrame frame = new JFrame("Sorting Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        this.setBackground(Color.WHITE);
        frame.add(this, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.DARK_GRAY);
        JButton start = new JButton("Start");
        JButton reset = new JButton("Reset");
        String[] algorithms = {
                "Bubble Sort", "Insertion Sort", "Selection Sort",
                "Merge Sort", "Quick Sort", "Counting Sort",
                "Radix Sort", "Bucket Sort"
        };
        JComboBox<String> algoSlec = new JComboBox<>(algorithms);
        btnPanel.add(start); btnPanel.add(reset); btnPanel.add(algoSlec);

        slider = new JSlider(10, 200, arraySize);
        label = new JLabel("Array Size: " + arraySize);
        slider.setMajorTickSpacing(50);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setBackground(Color.DARK_GRAY);
        slider.setForeground(Color.WHITE);
        btnPanel.add(slider); btnPanel.add(label);

        info = new JTextArea(10, 30);
        info.setBackground(Color.DARK_GRAY);
        info.setForeground(Color.WHITE);
        info.setFont(new Font("Monospace", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(info);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        start.addActionListener(e -> new Thread(() -> {
            comparisons = 0;
            info.setText("Sorting using: " + CurrAlgo + "\n" + PseudoCode + "\n");
            switch (CurrAlgo) {
                case "Bubble Sort": bubbleSort(); break;
                case "Selection Sort": selectionSort(); break;
                case "Insertion Sort": insertionSort(); break;
                case "Merge Sort": mergeSort(0, array.length - 1); break;
                case "Quick Sort": quickSort(0, array.length - 1); break;
                case "Counting Sort": countingSort(); break;
                case "Radix Sort": radixSort(); break;
                case "Bucket Sort": bucketSort(); break;
            }
            info.append("Sorting completed!\nComparisons: " + comparisons + "\n");
        }).start());

        reset.addActionListener(e -> {
            genArray(); repaint();
            info.setText("Array reset. Choose a sorting algorithm and start.\n");
        });

        algoSlec.addActionListener(e -> {
            CurrAlgo = (String) algoSlec.getSelectedItem();
            PseudoCode();
            info.setText("Current Algorithm: " + CurrAlgo + "\n" + PseudoCode + "\n");
        });

        slider.addChangeListener(e -> {
            arraySize = slider.getValue();
            label.setText("Array Size: " + arraySize);
            genArray(); repaint();
            info.setText("Array size updated to: " + arraySize + "\n");
        });

        frame.add(btnPanel, BorderLayout.SOUTH);
        frame.add(scroll, BorderLayout.EAST);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Sort::new);
    }
}