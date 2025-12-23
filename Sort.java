import java.awt.*;
import java.util.*;
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

    private volatile boolean isPaused = false;
    private volatile boolean stopRequested = false;
    private volatile boolean isRunning = false;

    // ---------------- ARRAY ----------------
    public void genArray() {
        array = new int[arraySize];
        Random r = new Random();
        for (int i = 0; i < arraySize; i++) {
            array[i] = r.nextInt(500) + 50;
        }
        currIndex = -1;
    }

    // ---------------- PSEUDOCODE ----------------
    public void PseudoCode() {
        switch (CurrAlgo) {
            case "Bubble Sort":
                PseudoCode = "Bubble Sort:\nO(n²) Avg/Worst, O(n) Best\n";
                break;
            case "Insertion Sort":
                PseudoCode = "Insertion Sort:\nO(n²) Avg/Worst, O(n) Best\n";
                break;
            case "Selection Sort":
                PseudoCode = "Selection Sort:\nO(n²) All cases\n";
                break;
            case "Merge Sort":
                PseudoCode = "Merge Sort:\nO(n log n)\n";
                break;
            case "Quick Sort":
                PseudoCode = "Quick Sort:\nO(n log n) Avg, O(n²) Worst\n";
                break;
        }
    }

    // ---------------- DRAW ----------------
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int bar = getWidth() / arraySize;

        for (int i = 0; i < array.length; i++) {
            int h = array[i];

            // 🔵 Active bar blue, others black
            g.setColor(i == currIndex ? new Color(30, 144, 255) : Color.BLACK);
            g.fillRect(i * bar, getHeight() - h, bar, h);

            // Grid lines (aesthetic)
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(i * bar, 0, i * bar, getHeight());
        }
    }

    // ---------------- CONTROLLED DELAY ----------------
    private void controlledDelay() {
        try {
            while (isPaused && !stopRequested) Thread.sleep(50);
            if (stopRequested) return;
            Thread.sleep(delay);
        } catch (InterruptedException ignored) {}
    }

    private void swap(int i, int j) {
        int t = array[i];
        array[i] = array[j];
        array[j] = t;
    }

    // Bubble Sort
    private void bubbleSort() {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (stopRequested) return;
                currIndex = j;
                comparisons++;
                if (array[j] > array[j + 1]) swap(j, j + 1);
                repaint(); controlledDelay();
            }
        }
    }

    // Selection Sort
    private void selectionSort() {
        for (int i = 0; i < array.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < array.length; j++) {
                if (stopRequested) return;
                currIndex = j;
                comparisons++;
                if (array[j] < array[min]) min = j;
                repaint(); controlledDelay();
            }
            swap(i, min);
        }
    }

    // Insertion Sort
    private void insertionSort() {
        for (int i = 1; i < array.length; i++) {
            int key = array[i], j = i - 1;
            while (j >= 0 && array[j] > key) {
                if (stopRequested) return;
                comparisons++;
                array[j + 1] = array[j];
                currIndex = j--;
                repaint(); controlledDelay();
            }
            array[j + 1] = key;
        }
    }

    // Merge Sort
    private void mergeSort(int l, int r) {
        if (l < r && !stopRequested) {
            int m = (l + r) / 2;
            mergeSort(l, m);
            mergeSort(m + 1, r);
            merge(l, m, r);
        }
    }

    private void merge(int l, int m, int r) {
        int[] L = Arrays.copyOfRange(array, l, m + 1);
        int[] R = Arrays.copyOfRange(array, m + 1, r + 1);
        int i = 0, j = 0, k = l;

        while (i < L.length && j < R.length) {
            if (stopRequested) return;
            currIndex = k;
            comparisons++;
            array[k++] = (L[i] <= R[j]) ? L[i++] : R[j++];
            repaint(); controlledDelay();
        }
        while (i < L.length) array[k++] = L[i++];
        while (j < R.length) array[k++] = R[j++];
    }

    // Quick Sort
    private void quickSort(int low, int high) {
        if (low < high && !stopRequested) {
            int pi = partition(low, high);
            quickSort(low, pi - 1);
            quickSort(pi + 1, high);
        }
    }

    private int partition(int low, int high) {
        int pivot = array[high], i = low - 1;
        for (int j = low; j < high; j++) {
            if (stopRequested) return i + 1;
            currIndex = j;
            comparisons++;
            if (array[j] < pivot) swap(++i, j);
            repaint(); controlledDelay();
        }
        swap(i + 1, high);
        return i + 1;
    }

    // ---------------- UI ----------------
    public Sort() {
        genArray();

        JFrame frame = new JFrame("Sorting Visualizer");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY);

        JButton start = new JButton("Start");
        JButton pauseResume = new JButton("Pause");
        JButton reset = new JButton("Reset");

        String[] algos = {
                "Bubble Sort", "Insertion Sort",
                "Selection Sort", "Merge Sort", "Quick Sort"
        };
        JComboBox<String> algoBox = new JComboBox<>(algos);

        panel.add(start);
        panel.add(pauseResume);
        panel.add(reset);
        panel.add(algoBox);

        slider = new JSlider(10, 200, arraySize);
        slider.setMajorTickSpacing(50);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        label = new JLabel("Array Size: " + arraySize);
        label.setForeground(Color.WHITE);

        panel.add(slider);
        panel.add(label);

        frame.add(panel, BorderLayout.SOUTH);

        info = new JTextArea(10, 30);
        info.setBackground(Color.DARK_GRAY);
        info.setForeground(Color.WHITE);
        frame.add(new JScrollPane(info), BorderLayout.EAST);

        // START
        start.addActionListener(e -> {
            if (isRunning) return;
            stopRequested = false;
            isPaused = false;
            isRunning = true;
            comparisons = 0;

            PseudoCode();
            info.setText("Sorting using: " + CurrAlgo + "\n" + PseudoCode);

            new Thread(() -> {
                switch (CurrAlgo) {
                    case "Bubble Sort": bubbleSort(); break;
                    case "Insertion Sort": insertionSort(); break;
                    case "Selection Sort": selectionSort(); break;
                    case "Merge Sort": mergeSort(0, array.length - 1); break;
                    case "Quick Sort": quickSort(0, array.length - 1); break;
                }
                if (!stopRequested)
                    info.append("\nSorting completed!\nComparisons: " + comparisons);
                isRunning = false;
            }).start();
        });

        // PAUSE / RESUME
        pauseResume.addActionListener(e -> {
            isPaused = !isPaused;
            pauseResume.setText(isPaused ? "Resume" : "Pause");
        });

        // RESET
        reset.addActionListener(e -> {
            stopRequested = true;
            isPaused = false;
            genArray();
            repaint();
            info.setText("Array reset.\n");
        });

        // ALGORITHM CHANGE
        algoBox.addActionListener(e -> {
            CurrAlgo = (String) algoBox.getSelectedItem();
            PseudoCode();
            info.setText("Algorithm: " + CurrAlgo + "\n" + PseudoCode);
        });

        // SLIDER (LIVE NUMBER UPDATE)
        slider.addChangeListener(e -> {
            int value = slider.getValue();

            // 🔢 Live update while dragging
            label.setText("Array Size: " + value);

            // Stop sorting immediately
            stopRequested = true;
            isPaused = false;

            // Regenerate only after release
            if (!slider.getValueIsAdjusting()) {
                arraySize = value;
                genArray();
                repaint();
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Sort::new);
    }
}
