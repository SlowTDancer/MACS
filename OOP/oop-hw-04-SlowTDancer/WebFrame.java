import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WebFrame extends JFrame {
    public static final String FILENAME = "links2.txt";
    private static final String[] COLS = new String[]{"URL", "Status"};
    private DefaultTableModel model;
    private List<String> urls;
    private JProgressBar progressBar;
    private JPanel panel;
    private JButton single, concurrent, stop;
    private JTextField field;
    private JLabel running, completed, elapsed;
    private final Lock lock;
    private Launcher launcher;

    public WebFrame(String filename) {
        super("WebLoader");
        readFile(filename);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        initTable();

        prepareSouthRegion();
        addSouthRegion();
        addListeners();
        add(panel);

        lock = new ReentrantLock();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void readFile(String filename) {
        try {
            BufferedReader rd = new BufferedReader(new FileReader(filename));
            urls = new ArrayList<>();
            while (true) {
                String url = rd.readLine();
                if (url == null) break;
                urls.add(url);
            }
            rd.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initTable() {
        model = new DefaultTableModel(COLS, 0);
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize(new Dimension(600, 300));
        for (String url : urls) {
            model.addRow(new String[]{url, ""});
        }
        panel.add(scrollpane);
    }

    private void prepareSouthRegion(){
        single = new JButton("Single Thread Fetch");
        concurrent = new JButton("Concurrent Fetch");
        stop = new JButton("Stop");
        setButtons(false);

        field = new JTextField(15);
        field.setMaximumSize(new Dimension(50, field.getHeight()));

        running = new JLabel("Running: 0");
        completed = new JLabel("Completed: 0");
        elapsed = new JLabel("Elapsed:");

        progressBar = new JProgressBar();
        progressBar.setMaximum(urls.size());
    }
    private void addSouthRegion() {
        addTop();
        addMid();
        addBot();
    }

    private void addTop(){
        panel.add(single);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(concurrent);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void addMid(){
        panel.add(running);
        panel.add(completed);
        panel.add(elapsed);
    }

    private void addBot(){
        panel.add(progressBar);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(stop);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        stop.setEnabled(false);
    }
    private void addListeners() {
        single.addActionListener(e -> startFetching(true));
        concurrent.addActionListener(e -> startFetching(false));
        stop.addActionListener(e -> stopFetching());
    }

    private void startFetching(boolean isSingleThread) {
        if(!isSingleThread && field.getText().equals((""))) return;
        try {
            int num= 1;
            if(!isSingleThread) num = Integer.parseInt(field.getText());
            SwingUtilities.invokeLater(this::resetFrame);
            launcher = new Launcher(num);
            launcher.start();
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    private void stopFetching() {
        lock.lock();
        launcher.interrupt();
        lock.unlock();
        running.setText("Running: 0");
        setButtons(false);
    }

    private void setButtons(boolean running){
        single.setEnabled(!running);
        concurrent.setEnabled(!running);
        stop.setEnabled(running);
    }

    private void resetFrame() {
        setButtons(true);

        for(int i = 0; i < urls.size(); i++){
            model.setValueAt("", i, 1);
        }

        running.setText("Running: 0");
        completed.setText("Completed: 0");
        elapsed.setText("Elapsed: 0.0");
        progressBar.setMaximum(urls.size());
        progressBar.setValue(0);
    }


    public class Launcher extends Thread {
        public Semaphore limiter;
        private int runningCount, completedCount;
        private double timeElapsed;
        private final List<Thread> workers;


        public Launcher(int limit) {
            timeElapsed = (double) System.currentTimeMillis();
            workers = new ArrayList<>();
            limiter = new Semaphore(limit);
            runningCount = 0;
            completedCount = 0;
            incrementRunningThreadCount();
        }

        @Override
        public void run() {
            for(int i = 0; i < urls.size(); i++){
                String url = urls.get(i);
                try {
                    limiter.acquire();
                    lock.lock();
                    Thread worker = new WebWorker(this, url, i);
                    workers.add(worker);
                    worker.start();
                    lock.unlock();
                } catch (InterruptedException e) {
                    interruptWorkers();
                    decrementThreadCount(true);
                    return;
                }
            }
            joinThreads();
            decrementThreadCount(true);
        }

        private void joinThreads() {
            for(Thread thread : workers){
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    interruptWorkers();
                }
            }
        }

        public void incrementRunningThreadCount(){
            synchronized(running){
                runningCount++;
                SwingUtilities.invokeLater(() -> running.setText("Running: " + (runningCount)));
            }
        }

        public void decrementThreadCount(boolean isLauncher){
            synchronized (running){
                runningCount--;
                SwingUtilities.invokeLater(() -> running.setText("Running: " + runningCount));
            }
            synchronized(completed){
                completedCount++;
                completed.setText("Completed: " + completedCount);
                progressBar.setValue(progressBar.getValue() + 1);
                if(isLauncher){
                    timeElapsed = (System.currentTimeMillis() - timeElapsed) / 1000;
                    elapsed.setText("Elapsed: "+ timeElapsed);
                    stopFetching();
                }
            }
        }

        public void updateStatus(int index, String status) {
            SwingUtilities.invokeLater(() -> model.setValueAt(status, index, 1));
        }

        private void interruptWorkers() {
            for(Thread worker : workers) worker.interrupt();
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WebFrame(FILENAME));
    }
}