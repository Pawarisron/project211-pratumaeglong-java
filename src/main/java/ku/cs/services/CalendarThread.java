package ku.cs.services;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarThread implements Runnable{

    private boolean stop;

    private Label timeLabel;
    private String name;

    private int priority;
    private String time;
    private Thread thread;

    public CalendarThread(String threadName,Label timelabel) {
        this.timeLabel = timelabel;
        name = threadName;
        thread = new Thread(this,name);
        thread.setPriority(2);
        this.priority = 2;
        System.out.println("New CalendarThread: "+thread);
        stop = false;
        thread.start();
    }
    public CalendarThread(String threadName,Label timelabel,int priority) {
        this.timeLabel = timelabel;
        name = threadName;
        thread = new Thread(this,name);
        thread.setPriority(priority);
        this.priority = priority;
        System.out.println("New CalendarThread: "+thread);
        stop = false;
        thread.start();
    }

    @Override
    public void run() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        while (!stop){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //เอาเวลามาเก็บใน String time
//            final String time = timeFormat.format(Calendar.getInstance().getTime());
            time = timeFormat.format(Calendar.getInstance().getTime());
            Platform.runLater(()-> {
                //แสดงเวลาออกมา ให้ user รู้ ผ่าน label ที่รับเข้ามากับ Constructor
                timeLabel.setText(time);
            });
        }
    }
    public void stop(){
        stop = true;
    }

    public boolean isStop() {
        return stop;
    }

    public Label getTimeLabel() {
        return timeLabel;
    }

    public String getName() {
        return name;
    }

    public Thread getThread() {
        return thread;
    }
    public void setPriority(int priority){
        thread.setPriority(priority);
    }
    public String getTime(){
        return time;
    }

    public int getPriority() {
        return priority;
    }
}
