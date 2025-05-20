import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebWorker extends Thread {
    private final String urlAddress;
    private final WebFrame.Launcher launcher;
    private final int index;

    public WebWorker(WebFrame.Launcher launcher, String urlAddress, int index) {
        super();
        this.launcher = launcher;
        this.urlAddress = urlAddress;
        this.index = index;
    }

    @Override
    public void run() {
        launcher.incrementRunningThreadCount();
        downloadURL();
        launcher.decrementThreadCount(false);
        launcher.limiter.release();
    }

    //  This is the core web/download i/o code...
    private void downloadURL() {
        long timeElapsed = System.currentTimeMillis();
        InputStream input = null;
        StringBuilder contents;
        try {
            URL url = new URL(urlAddress);
            URLConnection connection = url.openConnection();

            // Set connect() to throw an IOException
            // if connection does not succeed in this many msecs.
            connection.setConnectTimeout(5000);

            connection.connect();
            input = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            char[] array = new char[1000];
            int len;
            contents = new StringBuilder(1000);
            while ((len = reader.read(array, 0, array.length)) > 0) {
                if(isInterrupted()) {
                    updateFrameCatch(true);
                    return;
                }
                contents.append(array, 0, len);
                Thread.sleep(100);
            }

            // Successful download if we get here
            updateFrameSuccess(contents, timeElapsed);
        }
        // Otherwise, control jumps to a catch...
        catch (IOException ignored) {
            updateFrameCatch(false);
        } catch (InterruptedException exception) {
            updateFrameCatch(true);
        }
        // "finally" clause, to close the input stream
        // in any case
        finally {
            try {
                if (input != null) input.close();
            } catch (IOException ignored) {}
        }
    }

    private void updateFrameSuccess(StringBuilder contents, long timeElapsed){
        timeElapsed = (System.currentTimeMillis() - timeElapsed);
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()));
        String status = String.format("%s %sms %d bytes", time, timeElapsed, contents.length());
        launcher.updateStatus(index, status);
    }
    private void updateFrameCatch(boolean interrupted){
        String status = "err";
        if (interrupted) status = "interrupted";
        launcher.updateStatus(index, status);
    }
}