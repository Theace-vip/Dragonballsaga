package network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import network.inetwork.ISession;

public class SessionManager {

    private static SessionManager instance;
    private final List<ISession> sessions = new ArrayList<>();

    public static SessionManager gI() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void putSession(ISession session) {
        this.sessions.add(session);
    }

    public void removeSession(ISession session) {
        this.sessions.remove(session);
    }

    public List<ISession> getSessions() {
        return this.sessions;
    }

    public void cleanupSessions() {
        // Duyệt qua các session và xóa những session không còn kết nối
        Iterator<ISession> iterator = sessions.iterator();
        while (iterator.hasNext()) {
            ISession session = iterator.next();
            if (!session.isConnected()) {
                iterator.remove(); // Xóa session khỏi tập hợp
                removeSession(session); // Gọi removeSession để thực hiện logic bổ sung
                session.dispose(); // Dọn dẹp tài nguyên của session
            }
        }
    }

    public void startCleanupThread() {
        Thread cleanupThread = new Thread(() -> {
            while (true) {
                cleanupSessions(); // Gọi hàm dọn dẹp session
                try {
                    TimeUnit.SECONDS.sleep(5); // Dọn dẹp mỗi phút (60 giây)
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Đặt lại trạng thái ngắt
                    break; // Thoát khỏi vòng lặp khi bị ngắt
                }
            }
        });
        cleanupThread.setDaemon(true); // Thiết lập thread là daemon nếu không cần giữ chương trình hoạt động
        cleanupThread.start(); // Bắt đầu thread dọn dẹp
    }

    public ISession findByID(long id) throws Exception {
        if (this.sessions.isEmpty()) {
            throw new Exception("Session " + id + " does not exist");
        }
        for (ISession session : this.sessions) {
            if (session.getID() > id) {
                throw new Exception("Session " + id + " does not exist");
            }
            if (session.getID() != id) {
                continue;
            }
            return session;
        }
        throw new Exception("Session " + id + " does not exist");
    }

    public int getNumSession() {
        return this.sessions.size();
    }
}
