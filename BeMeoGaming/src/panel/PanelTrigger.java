package panel;

import java.io.File;
import java.nio.file.Files;

/**
 * Mo lai ControlPanel tu file trigger.
 * - open-panel.bat ghi file "panel.trigger" trong thu muc lam viec cua server
 *   -> thread nay thay va goi ControlPanel.showPanel() trong chinh JVM server.
 * Dung khi server da chay nhung bi dong cua so panel.
 */
public class PanelTrigger {

    public static final File TRIGGER = new File("panel.trigger");

    public static void start() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000L);
                    if (TRIGGER.exists()) {
                        Files.deleteIfExists(TRIGGER.toPath());
                        ControlPanel.showPanel();
                    }
                } catch (InterruptedException e) {
                    return;
                } catch (Exception e) {
                    try {
                        Files.deleteIfExists(TRIGGER.toPath());
                    } catch (Exception ignored) {
                    }
                }
            }
        }, "Panel trigger");
        t.setDaemon(true);
        t.start();
    }
}
