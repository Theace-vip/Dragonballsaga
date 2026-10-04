package server;



import java.io.IOException;
import java.time.LocalTime;
import utils.Logger;

public class AutoMaintenance extends Thread {

    public static boolean AutoMaintenance = true;
    public static final int hours = 23;
    public static final int mins = 59;
    public static final int second = 50;
    private static AutoMaintenance instance;
    public static boolean isRunning;

    public static AutoMaintenance gI() {
        if (instance == null) {
            instance = new AutoMaintenance();
        }
        return instance;
    }

    @Override
    public void run() {
        while (!Maintenance.isRunning && !isRunning) {
            try {
                if (AutoMaintenance) {
                    LocalTime currentTime = LocalTime.now();
                    if (currentTime.getHour() == hours && currentTime.getMinute() == mins && currentTime.getSecond() == second) {
                        Logger.log(Logger.PURPLE, "Đang Trong Quá Trình Tiến Hành Bảo Trì Tự Động\n");
                        Maintenance.gI().start(15);//như này là 23h59p 50 giây báo bảo trì trong vòng 15 giây
                        isRunning = true;
                        AutoMaintenance = false;

                    }
                }
                Thread.sleep(1000);

            } catch (Exception e) {
            }
        }
    }

    public static void runBatchFile(String batchFilePath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "start", batchFilePath);
        Process process = processBuilder.start();
        try {
            process.waitFor();
        } catch (Exception e) {
        }
    }
}
