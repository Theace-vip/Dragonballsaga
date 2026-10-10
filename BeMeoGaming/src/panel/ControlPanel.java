package panel;

import boss.Boss;
import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ControlPanel extends JFrame {
    private CardLayout cards;
    private JPanel contentCards;
    private JLabel lbClock, lbOnlineTop;
    private JLabel lbServerStatus, lbPlayers, lbNextMaint;
    private JLabel lbCpu, lbRam, lbThreads, lbSessions;
    private JLabel lbIps, lbDelay;
    private JLabel lbGift, lbBoss, lbConsign, lbUptime;
    private JButton btnAutoMaint, btnAutoClean;
    private JProgressBar barCpu, barRam, barThread, barSession;

    private static final Color BG = new Color(242, 244, 248);
    private static final Color CARD = Color.WHITE;
    private static final Color SIDEBAR = new Color(22, 27, 55);

    public ControlPanel() {
        setTitle("Ngoc Rong - Server Control Panel");
        setSize(1380, 830);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        add(buildSidebar(), BorderLayout.WEST);
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.add(buildHeader(), BorderLayout.NORTH);
        cards = new CardLayout();
        contentCards = new JPanel(cards);
        contentCards.setBackground(BG);
        contentCards.add(new JScrollPane(buildDashboard()), "DASH");
        contentCards.add(buildHelp(), "HELP");
        contentCards.add(buildAccounts(), "ACC");
        contentCards.add(buildPlayers(), "PLAYERS");
        contentCards.add(buildPlayerMng(), "PLYMNG");
        contentCards.add(buildInventory(), "INV");
        contentCards.add(buildTransactions(), "TX");
        contentCards.add(buildNapThePage(), "NAPTHE");
        contentCards.add(buildNapRatePage(), "NAPRATE");
        contentCards.add(buildAudit(), "AUDIT");
        contentCards.add(buildMailPage(), "MAIL");
        contentCards.add(buildShop(), "SHOP");
        contentCards.add(buildItems(), "ITEMS");
        contentCards.add(buildOptionDict(), "OPT");
        contentCards.add(buildNpc(), "NPC");
        contentCards.add(buildBoss(), "BOSS");
        contentCards.add(buildMapMob(), "MAPMOB");
        contentCards.add(buildEvents(), "EVENTS");
        contentCards.add(buildFormulas(), "FORM");
        contentCards.add(buildChiSo(), "CHISO");
        contentCards.add(buildTambao(), "TAMBAO");
        contentCards.add(buildConsign(), "CONSIGN");
        contentCards.add(buildGiftcode(), "GIFT");
        contentCards.add(buildPhucLoi(), "PHUCLOI");
        contentCards.add(buildChests(), "RUONG");
        contentCards.add(new JScrollPane(buildCungMenh()), "CUNGMENH");
        contentCards.add(buildSet5Mon(), "SET5");
        contentCards.add(buildHeThong(), "HETHONG");
        contentCards.add(buildDonate(), "DONATE");
        main.add(contentCards, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
        refreshAll();
        new javax.swing.Timer(1000, e -> refreshAll()).start();
    }

    private void go(String key) { cards.show(contentCards, key); }

    // ================= SIDEBAR =================
    private JPanel buildSidebar() {
        // Sidebar co scroll: header + footer fix, giua la danh muc keo truot len/xuong
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(SIDEBAR);
        outer.setPreferredSize(new Dimension(240, 800));
        outer.setBorder(new EmptyBorder(14, 14, 8, 14));
        JPanel head = new JPanel();
        head.setOpaque(false);
        head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS));
        JLabel logo = new JLabel("NR  Ngoc Rong");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel sub = new JLabel("Server Control Panel");
        sub.setForeground(new Color(150, 160, 200));
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        head.add(logo); head.add(sub); head.add(Box.createVerticalStrut(14));
        outer.add(head, BorderLayout.NORTH);
        SidebarMenu p = new SidebarMenu();
        p.setBackground(SIDEBAR);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(sideLabel("TONG QUAN"));
        p.add(sideBtn("Bang Dieu Khien", "DASH"));
        p.add(sideBtn("HUONG DAN SU DUNG", "HELP"));
        p.add(sideBtn("Nhat Ky Admin", "AUDIT"));
        p.add(Box.createVerticalStrut(6));
        p.add(sideLabel("QUAN LY"));
        p.add(sideBtn("Quan Ly Tai Khoan", "ACC"));
        p.add(sideBtn("Danh Sach Nguoi Choi", "PLAYERS"));
        p.add(sideBtn("Quan Ly Nhan Vat", "PLYMNG"));
        p.add(sideBtn("Hanh Trang / Ruong (all account)", "INV"));
        p.add(sideBtn("Cua Hang (Shop)", "SHOP"));
        p.add(sideBtn("Thu Vien Vat Pham", "ITEMS"));
        p.add(sideBtn("Tao Ruong (Box)", "RUONG"));
        p.add(sideBtn("Tu Dien Option", "OPT"));
        p.add(sideBtn("NPC + Shop", "NPC"));
        p.add(sideBtn("Cau Hinh Boss", "BOSS"));
        p.add(sideBtn("Map & Mob", "MAPMOB"));
        p.add(sideBtn("Su Kien (Events)", "EVENTS"));
        p.add(sideBtn("Cong Thuc (Formulas)", "FORM"));
        p.add(sideBtn("Chi So (Dame/HP/KI)", "CHISO"));
        p.add(sideBtn("Vong Quay (Tam Bao)", "TAMBAO"));
        p.add(sideBtn("Don Rac", "CONSIGN"));
        p.add(Box.createVerticalStrut(6));
        p.add(sideLabel("GIAO DICH"));
        p.add(sideBtn("Quan Ly Giftcode", "GIFT"));
        p.add(sideBtn("Phuc Loi (Qua Online)", "PHUCLOI"));
        p.add(sideBtn("Gui Qua (Mail)", "MAIL"));
        p.add(sideBtn("Lich Su Giao Dich", "TX"));
        p.add(sideBtn("Lich Su Nap The", "NAPTHE"));
        p.add(sideBtn("Ty Le Nap (x2/x3...)", "NAPRATE"));
        p.add(Box.createVerticalStrut(6));
        p.add(sideLabel("TINH NANG GAME"));
        p.add(sideBtn("Cung Menh (Bo Mong)", "CUNGMENH"));
        p.add(sideBtn("Set Do 5 Mon (option)", "SET5"));
        p.add(sideBtn("He Thong (theo nhom: Thien/Dia Dao...)", "HETHONG"));
        p.add(sideBtn("Nap Tien / Buff VND", "DONATE"));
        p.add(Box.createVerticalGlue());
        JScrollPane sp = new JScrollPane(p);
        sp.setBorder(null);
        sp.setBackground(SIDEBAR);
        sp.getViewport().setBackground(SIDEBAR);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.getVerticalScrollBar().setBackground(SIDEBAR);
        outer.add(sp, BorderLayout.CENTER);
        JLabel dev = new JLabel("Developed by Ha Huy Hoang");
        dev.setForeground(new Color(120, 130, 170));
        dev.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        outer.add(dev, BorderLayout.SOUTH);
        return outer;
    }

    // Panel danh muc sidebar hien thi Scrollable de JScrollPane truot len/xuong khi qua nhieu muc
    private static class SidebarMenu extends JPanel implements Scrollable {
        @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(Rectangle vis, int ori, int dir) { return 16; }
        @Override public int getScrollableBlockIncrement(Rectangle vis, int ori, int dir) {
            return ori == SwingConstants.VERTICAL ? Math.max(vis.height - 16, 16) : Math.max(vis.width - 16, 16);
        }
        @Override public boolean getScrollableTracksViewportWidth() { return true; }
        @Override public boolean getScrollableTracksViewportHeight() {
            return getParent() instanceof JViewport && getParent().getHeight() >= getPreferredSize().height;
        }
    }

    private JLabel sideLabel(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(new Color(130, 140, 180));
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        return l;
    }

    private JButton sideBtn(String t, String key) {
        JButton b = new JButton(t);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setMaximumSize(new Dimension(200, 30));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setBackground(SIDEBAR);
        b.setForeground(new Color(200, 208, 230));
        b.addActionListener(e -> go(key));
        return b;
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(BG);
        h.setBorder(new EmptyBorder(10, 16, 6, 16));
        JPanel left = new JPanel(new GridLayout(2, 1));
        left.setBackground(BG);
        JLabel small = new JLabel("Server Control / Bang Dieu Khien");
        small.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        small.setForeground(Color.GRAY);
        JLabel big = new JLabel("Bang Dieu Khien");
        big.setFont(new Font("Segoe UI", Font.BOLD, 20));
        left.add(small); left.add(big);
        h.add(left, BorderLayout.WEST);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(BG);
        lbClock = new JLabel("--:--:-- - --/--/----");
        lbOnlineTop = new JLabel("0 online");
        JLabel on = new JLabel("ONLINE");
        on.setOpaque(true); on.setBackground(new Color(220, 255, 230));
        on.setForeground(new Color(20, 140, 60));
        on.setBorder(new EmptyBorder(4, 10, 4, 10));
        on.setFont(new Font("Segoe UI", Font.BOLD, 11));
        right.add(lbClock); right.add(lbOnlineTop); right.add(on);
        h.add(right, BorderLayout.EAST);
        return h;
    }

    // ================= helpers =================
    private JPanel wrapPage(JComponent inner, String title, String sub) {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BG);
        page.setBorder(new EmptyBorder(6, 16, 16, 16));
        JPanel head = new JPanel(new GridLayout(2, 1));
        head.setBackground(BG);
        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JLabel s = new JLabel(sub);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        s.setForeground(Color.GRAY);
        head.add(t); head.add(s);
        page.add(head, BorderLayout.NORTH);
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(BG);
        body.setBorder(new EmptyBorder(8, 0, 0, 0));
        body.add(inner, BorderLayout.CENTER);
        page.add(body, BorderLayout.CENTER);
        return page;
    }

    private JPanel card(String title, JLabel value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 10));
        t.setForeground(new Color(140, 150, 170));
        value.setFont(new Font("Segoe UI", Font.BOLD, 16));
        p.add(t, BorderLayout.NORTH);
        p.add(value, BorderLayout.CENTER);
        return p;
    }

    private JPanel cardBar(String title, JLabel value, JProgressBar bar) {
        JPanel p = card(title, value);
        bar.setStringPainted(false);
        bar.setPreferredSize(new Dimension(100, 6));
        p.add(bar, BorderLayout.SOUTH);
        return p;
    }

    private JButton btn(String t) {
        JButton b = new JButton(t);
        b.setFocusPainted(false);
        return b;
    }

    private JTextField tf(String t, int c) { JTextField f = new JTextField(t, c); return f; }
    private JTextField tf(int c) { return new JTextField(c); }

    private static final java.util.concurrent.ExecutorService BG_POOL = java.util.concurrent.Executors.newCachedThreadPool(r -> { Thread t = new Thread(r); try { t.setDaemon(true); } catch (Exception e) {} return t; });
    private void bg(Runnable r) { try { BG_POOL.submit(r); } catch (Exception e) { try { new Thread(r).start(); } catch (Exception ex) {} } }
    private void info(String s) { JOptionPane.showMessageDialog(this, s, "Thong bao", JOptionPane.INFORMATION_MESSAGE); }
    private void err(String s) { JOptionPane.showMessageDialog(this, s, "Loi", JOptionPane.ERROR_MESSAGE); }
    private JTextArea roArea(int rows) {
        JTextArea a = new JTextArea(rows, 20);
        a.setLineWrap(true); a.setWrapStyleWord(true);
        a.setEditable(false); a.setOpaque(false);
        a.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return a;
    }
    private void addRow(JPanel grid, GridBagConstraints base, int y, String label, JComponent field) {
        GridBagConstraints l = (GridBagConstraints) base.clone();
        l.gridx = 0; l.gridy = y; l.weightx = 0; l.fill = GridBagConstraints.NONE;
        l.anchor = GridBagConstraints.NORTHEAST;
        grid.add(new JLabel(label), l);
        GridBagConstraints f = (GridBagConstraints) base.clone();
        f.gridx = 1; f.gridy = y; f.weightx = 1; f.fill = GridBagConstraints.HORIZONTAL;
        grid.add(field, f);
    }

    // ================= DASHBOARD =================
    private JPanel buildDashboard() {
        JPanel c = new JPanel();
        c.setBackground(BG);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBorder(new EmptyBorder(6, 16, 16, 16));
        JPanel row1 = new JPanel(new GridLayout(1, 3, 12, 12));
        row1.setBackground(BG);
        row1.setMaximumSize(new Dimension(2000, 110));
        lbServerStatus = new JLabel(); lbPlayers = new JLabel(); lbNextMaint = new JLabel();
        row1.add(card("SERVER STATUS", lbServerStatus));
        row1.add(card("PLAYERS ONLINE", lbPlayers));
        row1.add(card("NEXT MAINTENANCE", lbNextMaint));
        c.add(row1); c.add(Box.createVerticalStrut(12));
        JPanel row2 = new JPanel(new GridLayout(1, 4, 12, 12));
        row2.setBackground(BG);
        row2.setMaximumSize(new Dimension(2000, 125));
        lbCpu = new JLabel(); lbRam = new JLabel(); lbThreads = new JLabel(); lbSessions = new JLabel();
        barCpu = new JProgressBar(); barRam = new JProgressBar(); barThread = new JProgressBar(); barSession = new JProgressBar();
        row2.add(cardBar("SERVER CPU", lbCpu, barCpu));
        row2.add(cardBar("JVM RAM (HEAP)", lbRam, barRam));
        row2.add(cardBar("THREADS", lbThreads, barThread));
        row2.add(cardBar("SESSIONS", lbSessions, barSession));
        c.add(row2); c.add(Box.createVerticalStrut(12));
        JPanel row3 = new JPanel(new GridLayout(1, 2, 12, 12));
        row3.setBackground(BG);
        row3.setMaximumSize(new Dimension(2000, 100));
        lbIps = new JLabel(); lbDelay = new JLabel();
        row3.add(card("IPS / MAX PER-IP", lbIps));
        row3.add(card("SERVER DELAY", lbDelay));
        c.add(row3); c.add(Box.createVerticalStrut(12));
        c.add(quickPanel()); c.add(Box.createVerticalStrut(12));
        c.add(statsPanel()); c.add(Box.createVerticalStrut(12));
        c.add(serverCfgPanel());
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG);
        outer.add(c, BorderLayout.CENTER);
        return outer;
    }

    private JPanel quickPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        p.setMaximumSize(new Dimension(2000, 130));
        JLabel t = new JLabel("Quick Actions");
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));
        p.add(t, BorderLayout.NORTH);
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btns.setBackground(CARD);
        JButton b1 = btn("Bao Tri (Tuy Chinh)");
        JButton b2 = btn("Bao Tri Ngay");
        b2.setBackground(new Color(220, 60, 70)); b2.setForeground(Color.WHITE);
        JButton b3 = btn("Tai lai DB (Shop)");
        b3.setBackground(new Color(110, 90, 220)); b3.setForeground(Color.WHITE);
        JButton b4 = btn("Don Session");
        btnAutoMaint = btn(""); btnAutoClean = btn("");
        b1.addActionListener(e -> doMaintCustom());
        b2.addActionListener(e -> doMaintNow());
        b3.addActionListener(e -> doReloadDb());
        b4.addActionListener(e -> doCleanSession());
        btnAutoMaint.addActionListener(e -> { server.AutoMaintenance.AutoMaintenance = !server.AutoMaintenance.AutoMaintenance; refreshAll(); });
        btnAutoClean.addActionListener(e -> { PanelData.AUTO_CLEAN_SS = !PanelData.AUTO_CLEAN_SS; refreshAll(); });
        btns.add(b1); btns.add(b2); btns.add(b3); btns.add(b4); btns.add(btnAutoMaint); btns.add(btnAutoClean);
        p.add(btns, BorderLayout.CENTER);
        return p;
    }

    private JPanel statsPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JLabel t = new JLabel("Game Statistics");
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));
        p.add(t, BorderLayout.NORTH);
        JPanel g = new JPanel(new GridLayout(3, 2, 10, 10));
        g.setBackground(CARD);
        lbGift = new JLabel(); lbBoss = new JLabel(); lbConsign = new JLabel(); lbUptime = new JLabel();
        lbGift.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbBoss.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbConsign.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbUptime.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g.add(new JLabel("GIFTCODES")); g.add(new JLabel("CONSIGN ITEMS"));
        g.add(lbGift); g.add(lbConsign);
        g.add(new JLabel("BOSS STATUS")); g.add(new JLabel("UPTIME"));
        g.add(lbBoss); g.add(lbUptime);
        p.add(g, BorderLayout.CENTER);
        return p;
    }

    private JPanel serverCfgPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        p.setMaximumSize(new Dimension(2000, 120));
        JLabel t = new JLabel("Cau Hinh Server (runtime)");
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));
        p.add(t, BorderLayout.NORTH);
        JPanel f = new JPanel(new FlowLayout(FlowLayout.LEFT));
        f.setBackground(CARD);
        JTextField fMax = tf(String.valueOf(server.Manager.MAX_PLAYER), 6);
        JTextField fIp = tf(String.valueOf(server.Manager.MAX_PER_IP), 6);
        JButton bSave = btn("Luu");
        bSave.addActionListener(e -> bg(() -> {
            try {
                int a = Integer.parseInt(fMax.getText().trim());
                int b = Integer.parseInt(fIp.getText().trim());
                PanelService.setMaxPlayer(a); PanelService.setMaxPerIp(b);
                SwingUtilities.invokeLater(() -> info("Da luu MAX_PLAYER=" + a + ", MAX_PER_IP=" + b));
            } catch (Exception ex) { SwingUtilities.invokeLater(() -> err("Loi: " + ex.getMessage())); }
        }));
        f.add(new JLabel("MAX_PLAYER:")); f.add(fMax);
        f.add(new JLabel("MAX_PER_IP:")); f.add(fIp);
        f.add(bSave);
        p.add(f, BorderLayout.CENTER);
        return p;
    }

    // ================= ACCOUNTS =================
    private DefaultTableModel accModel;
    private JTable accTable;
    private JTextField accSearch;

    private JPanel buildAccounts() {
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setBackground(CARD);
        body.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        accSearch = tf(18);
        JButton bFind = btn("Tim kiem");
        JButton bReload = btn("Tai lai");
        top.add(new JLabel("Username/ID:")); top.add(accSearch); top.add(bFind); top.add(bReload);
        body.add(top, BorderLayout.NORTH);
        accModel = new DefaultTableModel(new String[]{"ID", "Username", "Pass", "Ban", "Admin", "VND", "TongNap", "VIP", "Active", "IP"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        accTable = new JTable(accModel);
        body.add(new JScrollPane(accTable), BorderLayout.CENTER);
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bot.setBackground(CARD);
        JTextField fPass = tf(10); JTextField fVnd = tf("0", 8); JTextField fTong = tf("0", 8); JTextField fVip = tf("-1", 4);
        JTextField fNewUser = tf(14);
        JButton bBan = btn("Ban"); JButton bUnban = btn("Unban");
        JButton bAdmin = btn("Set Admin"); JButton bUnadmin = btn("Bo Admin");
        JButton bPass = btn("Doi Pass"); JButton bBuff = btn("Buff VND"); JButton bDel = btn("Xoa TK");
        JButton bTop = btn("Top Nap");
        JButton bCreate = btn("Tao TK moi"); bCreate.setBackground(new Color(40, 150, 80)); bCreate.setForeground(Color.WHITE);
        JButton bRename = btn("Doi ten TK da chon");
        bCreate.addActionListener(e -> {
            String u = fNewUser.getText().trim();
            String p = fPass.getText().trim();
            if (u.isEmpty() || p.isEmpty()) { err("Nhap username (o Ten TK) va pass (o Pass) de tao tai khoan"); return; }
            bg(() -> { String r = PanelService.createAccount(u, p); SwingUtilities.invokeLater(() -> { info(r); loadAccounts(); }); });
        });
        bRename.addActionListener(e -> {
            String id = selAccId(); if (id == null) return;
            String u = fNewUser.getText().trim();
            if (u.isEmpty()) { err("Nhap username moi vao o Ten TK"); return; }
            bg(() -> { String r = PanelService.renameAccount(Integer.parseInt(id), u); SwingUtilities.invokeLater(() -> { info(r); loadAccounts(); }); });
        });
        bBan.addActionListener(e -> accAction("ban"));
        bUnban.addActionListener(e -> accAction("unban"));
        bAdmin.addActionListener(e -> accAction("admin"));
        bUnadmin.addActionListener(e -> accAction("unadmin"));
        bPass.addActionListener(e -> { String id = selAccId(); if (id == null) return; String p = fPass.getText().trim(); if (p.isEmpty()) { err("Nhap pass moi"); return; } bg(() -> { String r = PanelService.resetPass(Integer.parseInt(id), p); SwingUtilities.invokeLater(() -> { info(r); loadAccounts(); }); }); });
        bBuff.addActionListener(e -> { String id = selAccId(); if (id == null) return; bg(() -> { String r; try { r = PanelService.buffVnd(Integer.parseInt(id), Long.parseLong(fVnd.getText().trim()), Long.parseLong(fTong.getText().trim()), Integer.parseInt(fVip.getText().trim())); } catch (Exception ex) { r = "Loi: " + ex.getMessage(); } final String rr = r; SwingUtilities.invokeLater(() -> { info(rr); loadAccounts(); }); }); });
        bDel.addActionListener(e -> accAction("del"));
        bTop.addActionListener(e -> bg(() -> {
            List<Map<String, Object>> list = PanelService.topNap(20);
            StringBuilder sb = new StringBuilder("TOP NAP:\n");
            int i = 1;
            for (Map<String, Object> m : list) sb.append(i++).append(". ").append(m.get("username")).append(" - tongnap=").append(m.get("tongnap")).append(" vnd=").append(m.get("vnd")).append("\n");
            SwingUtilities.invokeLater(() -> info(sb.toString()));
        }));
        bFind.addActionListener(e -> loadAccounts());
        bReload.addActionListener(e -> loadAccounts());
        bot.add(new JLabel("Ten TK:")); bot.add(fNewUser); bot.add(bCreate); bot.add(bRename);
        bot.add(new JLabel("Pass:")); bot.add(fPass); bot.add(bPass);
        bot.add(bBan); bot.add(bUnban); bot.add(bAdmin); bot.add(bUnadmin);
        bot.add(new JLabel("VND+:")); bot.add(fVnd); bot.add(new JLabel("TongNap+:")); bot.add(fTong);
        bot.add(new JLabel("VIP(-1=giu):")); bot.add(fVip); bot.add(bBuff); bot.add(bTop); bot.add(bDel);
        body.add(bot, BorderLayout.SOUTH);
        bg(this::loadAccounts);
        return wrapPage(body, "Quan Ly Tai Khoan", "Tim kiem, TAO/DOI TEN/DOI PASS/XOA TK, ban/unban, admin, buff VND, top nap");
    }

    private String selAccId() {
        int r = accTable.getSelectedRow();
        if (r < 0) { err("Chon 1 dong trong bang"); return null; }
        return String.valueOf(accModel.getValueAt(r, 0));
    }

    private void accAction(String act) {
        String id = selAccId();
        if (id == null) return;
        if (act.equals("del")) {
            int c = JOptionPane.showConfirmDialog(this, "Xoa account " + id + "?", "Xac nhan", JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION) return;
        }
        bg(() -> {
            String r = "OK";
            int i = Integer.parseInt(id);
            switch (act) {
                case "ban": r = PanelService.banAccount(i, true); break;
                case "unban": r = PanelService.banAccount(i, false); break;
                case "admin": r = PanelService.setAdmin(i, true); break;
                case "unadmin": r = PanelService.setAdmin(i, false); break;
                case "del": r = PanelService.deleteAccount(i); break;
            }
            final String rr = r;
            SwingUtilities.invokeLater(() -> { info(rr); loadAccounts(); });
        });
    }

    private void loadAccounts() {
        List<Map<String, Object>> list = PanelService.listAccounts(accSearch.getText(), 500);
        SwingUtilities.invokeLater(() -> {
            accModel.setRowCount(0);
            for (Map<String, Object> m : list) {
                accModel.addRow(new Object[]{m.get("id"), m.get("username"), m.get("password"), m.get("ban"), m.get("is_admin"), m.get("vnd"), m.get("tongnap"), m.get("vip"), m.get("active"), m.get("ip")});
            }
        });
    }

    // ================= PLAYERS ONLINE =================
    private DefaultTableModel plModel;
    private JTable plTable;

    private JPanel buildPlayers() {
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setBackground(CARD);
        body.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        JButton bReload = btn("Tai lai online");
        JButton bKickAll = btn("Kick ALL");
        bKickAll.setBackground(new Color(220, 60, 70)); bKickAll.setForeground(Color.WHITE);
        JTextField fBc = tf(24);
        JButton bBc = btn("Thong bao ALL");
        bReload.addActionListener(e -> bg(this::loadPlayers));
        bKickAll.addActionListener(e -> { int c = JOptionPane.showConfirmDialog(this, "Kick tat ca?", "Xac nhan", JOptionPane.YES_NO_OPTION); if (c != JOptionPane.YES_OPTION) return; bg(() -> { String r = PanelService.kickAll(); SwingUtilities.invokeLater(() -> { info(r); loadPlayers(); }); }); });
        bBc.addActionListener(e -> { String t = fBc.getText().trim(); if (t.isEmpty()) return; bg(() -> { String r = PanelService.broadcast(t); SwingUtilities.invokeLater(() -> info(r)); }); });
        top.add(bReload); top.add(bKickAll); top.add(new JLabel("TB All:")); top.add(fBc); top.add(bBc);
        body.add(top, BorderLayout.NORTH);
        plModel = new DefaultTableModel(new String[]{"Name", "Power", "TiemNang", "Gold", "Gem", "Map", "Zone"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        plTable = new JTable(plModel);
        body.add(new JScrollPane(plTable), BorderLayout.CENTER);
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bot.setBackground(CARD);
        JTextField fPower = tf("0", 8); JTextField fTn = tf("0", 8);
        JTextField fGold = tf("0", 8); JTextField fGem = tf("0", 6); JTextField fRuby = tf("0", 6);
        JTextField fMsg = tf(16);
        JButton bKick = btn("Kick"); JButton bMsg = btn("Nhan tin"); JButton bBuff = btn("Buff chi so");
        bKick.addActionListener(e -> { String n = selPlName(); if (n == null) return; bg(() -> { String r = PanelService.kickPlayer(n); SwingUtilities.invokeLater(() -> { info(r); loadPlayers(); }); }); });
        bMsg.addActionListener(e -> { String n = selPlName(); if (n == null) return; String t = fMsg.getText().trim(); bg(() -> { String r = PanelService.notifyPlayer(n, t); SwingUtilities.invokeLater(() -> info(r)); }); });
        bBuff.addActionListener(e -> { String n = selPlName(); if (n == null) return; bg(() -> { String r; try { r = PanelService.buffPlayer(n, Double.parseDouble(fPower.getText().trim()), Double.parseDouble(fTn.getText().trim()), Long.parseLong(fGold.getText().trim()), Integer.parseInt(fGem.getText().trim()), Integer.parseInt(fRuby.getText().trim())); } catch (Exception ex) { r = "Loi: " + ex.getMessage(); } final String rr = r; SwingUtilities.invokeLater(() -> { info(rr); loadPlayers(); }); }); });
        bot.add(bKick); bot.add(new JLabel("Tin nhan:")); bot.add(fMsg); bot.add(bMsg);
        bot.add(new JLabel("Power+:")); bot.add(fPower); bot.add(new JLabel("TN+:")); bot.add(fTn);
        bot.add(new JLabel("Gold+:")); bot.add(fGold); bot.add(new JLabel("Gem+:")); bot.add(fGem);
        bot.add(new JLabel("Ruby+:")); bot.add(fRuby); bot.add(bBuff);
        body.add(bot, BorderLayout.SOUTH);
        bg(this::loadPlayers);
        return wrapPage(body, "Danh Sach Nguoi Choi", "Kick, nhan tin, buff chi so player online");
    }

    private String selPlName() {
        int r = plTable.getSelectedRow();
        if (r < 0) { err("Chon 1 player"); return null; }
        return String.valueOf(plModel.getValueAt(r, 0));
    }

    private void loadPlayers() {
        List<player.Player> list = PanelService.onlinePlayers();
        SwingUtilities.invokeLater(() -> {
            plModel.setRowCount(0);
            for (player.Player p : list) {
                try {
                    String map = p.zone != null && p.zone.map != null ? p.zone.map.mapName : "?";
                    int zone = p.zone != null ? p.zone.zoneId : -1;
                    plModel.addRow(new Object[]{p.name, (long) p.nPoint.power, (long) p.nPoint.tiemNang, p.inventory.gold, p.inventory.gem, map, zone});
                } catch (Exception e) {}
            }
        });
    }

    // ================= SHOP (editor truc quan, khong go JSON) =================
    private DefaultTableModel npcShopModel;
    private JTable npcShopTable;
    private java.util.List<Map<String, Object>> npcShopCache = new java.util.ArrayList<>();
    private java.util.List<Map<String, Object>> shopTabCache = new java.util.ArrayList<>();
    private int curShopId = -1;
    private String curShopTag = "";
    private int curShopType = -1;
    private JLabel lbShopStats;
    private JLabel lbShopOpener;
    private DefaultTableModel shopModel;
    private JTable shopTable;
    private DefaultTableModel shopItemTableModel;
    private JTable shopItemTable;
    private java.util.List<ShopItemModel> shopDraft = new java.util.ArrayList<>();
    private int shopDraftTabId = -1;
    private JLabel lbShopStatus;
    private java.util.List<Map<String, Object>> optDictCache = new java.util.ArrayList<>();
    private boolean shopInlineGuard = false;
    private boolean optInlineGuard = false;

    private JPanel buildShop() {
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setBackground(CARD);
        body.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        JButton bReload = btn("Tai lai Shop/NPC");
        JButton bReloadShop = btn("Reload Shop vao server");
        JButton bOpen = btn("Mo tab de sua");
        bOpen.setBackground(new Color(70, 120, 220)); bOpen.setForeground(Color.WHITE);
        JButton bCloneTab = btn("Nhan ban tab");
        JButton bUndo = btn("Hoan tac");
        JButton bHist = btn("Lich su");
        JButton bExp = btn("Xuat CSV");
        JButton bImp = btn("Nhap CSV");
        bReload.addActionListener(e -> bg(this::loadNpcShops));
        bReloadShop.addActionListener(e -> bg(() -> { String r = PanelService.reloadShop(); SwingUtilities.invokeLater(() -> info(r)); }));
        bOpen.addActionListener(e -> openShopTabDraft());
        bCloneTab.addActionListener(e -> cloneShopTab());
        bUndo.addActionListener(e -> undoShopTab());
        bHist.addActionListener(e -> showShopHistory());
        bExp.addActionListener(e -> exportShopCsv());
        bImp.addActionListener(e -> importShopCsv());
        top.add(bReload); top.add(bReloadShop); top.add(bOpen); top.add(bCloneTab); top.add(bUndo); top.add(bHist); top.add(bExp); top.add(bImp);
        JButton bNewShop = btn("Tao shop moi");
        bNewShop.setBackground(new Color(40, 140, 70)); bNewShop.setForeground(Color.WHITE);
        bNewShop.addActionListener(e -> showNewShopDialog());
        top.add(bNewShop);
        lbShopStats = new JLabel("...");
        lbShopStats.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbShopStats.setForeground(new Color(90, 100, 130));
        top.add(lbShopStats);
        body.add(top, BorderLayout.NORTH);
        // --- Tang 1: NPC + Shop (26 shop, 3 tat) ---
        npcShopModel = new DefaultTableModel(new String[]{"ShopID", "NPC", "Tag Shop", "Loai", "Tab", "Trang thai"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        npcShopTable = new JTable(npcShopModel);
        npcShopTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        npcShopTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int r = npcShopTable.getSelectedRow();
            if (r < 0 || r >= npcShopCache.size()) return;
            Map<String, Object> m = npcShopCache.get(r);
            curShopId = ((Number) m.get("shop_id")).intValue();
            curShopTag = String.valueOf(m.get("shop"));
            curShopType = ((Number) m.get("type_shop")).intValue();
            String op = String.valueOf(m.get("opener"));
            lbShopOpener.setText("NPC mo: " + op + " | Loai: " + m.get("type_vn"));
            filterTabsByShop();
        });
        JScrollPane spNpc = new JScrollPane(npcShopTable);
        spNpc.setPreferredSize(new Dimension(430, 400));
        spNpc.setBorder(BorderFactory.createTitledBorder("1. Chon NPC lam Shop (26 shop - 3 tat do 0 tab)"));
        // --- Tang 2: Tab cua shop dang chon ---
        shopModel = new DefaultTableModel(new String[]{"TabID", "Tab", "Idx", "So mon"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        shopTable = new JTable(shopModel);
        shopTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane spTabs = new JScrollPane(shopTable);
        spTabs.setPreferredSize(new Dimension(330, 400));
        spTabs.setBorder(BorderFactory.createTitledBorder("2. Tab cua Shop (chon 1 tab -> Mo tab de sua)"));
        shopItemTableModel = new DefaultTableModel(new String[]{"TempID", "Ten vat pham (nhap doi de chon)", "SL/Gia (go so)", "Tien (Thuong)", "Doi bang (Dac biet - nhap doi)", "Moi", "Ban", "Option (nhap doi)"}, 0) {
            @Override public boolean isCellEditable(int r, int c) {
                if (shopDraft == null || r < 0 || r >= shopDraft.size()) return false;
                return c == 2 || c == 3 || c == 5 || c == 6;
            }
            @Override public Class<?> getColumnClass(int c) {
                if (c == 5 || c == 6) return Boolean.class;
                return String.class;
            }
            @Override public String getColumnName(int c) {
                if (c == 2) return curShopType == 3 ? "SL doi (go so)" : "Gia (go so)";
                if (c == 3) return curShopType == 3 ? "Tien (khoa: theo Doi bang)" : "Tien (chon)";
                return super.getColumnName(c);
            }
            @Override public void setValueAt(Object v, int r, int c) {
                if (shopInlineGuard) { super.setValueAt(v, r, c); return; }
                try {
                    if (shopDraft == null || r < 0 || r >= shopDraft.size()) return;
                    ShopItemModel m = shopDraft.get(r);
                    if (c == 2) {
                        int gv = Integer.parseInt(String.valueOf(v).trim().replaceAll("[^0-9-]", ""));
                        if (gv < 0) gv = 0;
                        m.cost = gv;
                    } else if (c == 3) {
                        if (curShopType == 3) { super.setValueAt(specDisplay(m.itemSpec), r, 4); return; }
                        String s = String.valueOf(v);
                        if (s.contains("(1)")) m.typeSell = 1;
                        else if (s.contains("(3)")) m.typeSell = 3;
                        else if (s.contains("(4)")) m.typeSell = 4;
                        else m.typeSell = 0;
                    } else if (c == 5) {
                        m.isNew = (v instanceof Boolean) ? (Boolean) v : Boolean.parseBoolean(String.valueOf(v));
                    } else if (c == 6) {
                        m.isSell = (v instanceof Boolean) ? (Boolean) v : Boolean.parseBoolean(String.valueOf(v));
                    } else { super.setValueAt(v, r, c); return; }
                    shopInlineGuard = true;
                    try {
                        if (c == 2) super.setValueAt(String.valueOf(m.cost), r, c);
                        else if (c == 3) super.setValueAt(m.typeSell == 1 ? "Ngoc (1)" : m.typeSell == 3 ? "Ruby (3)" : m.typeSell == 4 ? "Coupon/DiemSK (4)" : "Vang (0)", r, c);
                        else super.setValueAt(v, r, c);
                        super.setValueAt(specDisplay(m.itemSpec), r, 4);
                        super.setValueAt(PanelService.itemSummaryVN(m), r, 7);
                    } finally { shopInlineGuard = false; }
                    lbShopStatus.setText("Nhap tab " + shopDraftTabId + ": " + shopDraft.size() + " mon (da sua dong " + (r + 1) + ", chua XUAT BAN). Nho Xem truoc -> XUAT BAN.");
                } catch (Exception ex) { shopInlineGuard = false; err("Loi: " + ex.getMessage()); }
            }
        };
        shopItemTable = new JTable(shopItemTableModel);
        shopItemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        shopItemTable.setRowHeight(24);
        JComboBox<String> cbTypeSellInline = new JComboBox<>(new String[]{"Vang (0)", "Ngoc (1)", "Ruby (3)", "Coupon/DiemSK (4)"});
        shopItemTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(cbTypeSellInline));
        shopItemTable.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        shopItemTable.getColumnModel().getColumn(5).setCellRenderer(shopItemTable.getDefaultRenderer(Boolean.class));
        shopItemTable.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        shopItemTable.getColumnModel().getColumn(6).setCellRenderer(shopItemTable.getDefaultRenderer(Boolean.class));
        shopItemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int r = shopItemTable.getSelectedRow();
                    int c = shopItemTable.getSelectedColumn();
                    if (r < 0 || r >= shopDraft.size()) return;
                    if (c == 1) {
                        Integer pick = openTemplatePicker();
                        if (pick != null) {
                            ShopItemModel m = shopDraft.get(r);
                            m.tempId = pick;
                            models.Template.ItemTemplate t = PanelService.getTemplateById(pick);
                            if (t != null) m.itemName = t.name;
                            refreshShopDraftTable();
                            shopItemTable.setRowSelectionInterval(r, r);
                        }
                    } else if (c == 4) {
                        if (curShopType != 3) { info("Shop Thuong dung cot Tien (Vang/Ngoc/Ruby). Cot Doi bang chi dung cho Shop Dac biet (Cua Hang dac biet)."); return; }
                        Integer pick = openSpecPicker();
                        if (pick != null) {
                            ShopItemModel m = shopDraft.get(r);
                            m.itemSpec = pick;
                            refreshShopDraftTable();
                            shopItemTable.setRowSelectionInterval(r, r);
                            lbShopStatus.setText("Da doi tien doi thanh " + specDisplay(pick) + " (dong " + (r+1) + ", chua XUAT BAN).");
                        }
                    } else if (c == 7) {
                        openShopOptionInlineEditor(shopDraft.get(r), r);
                    }
                }
            }
        });
        JScrollPane spItems = new JScrollPane(shopItemTable);
        spItems.setBorder(BorderFactory.createTitledBorder("3. Mon trong Tab (Thuong: Gia+Loai tien; Dac biet: SL + Doi bang) - click o Gia/Loai tien de sua, nhap doi Ten/Option/Tien doi)"));
        lbShopOpener = new JLabel("Chon 1 NPC ben trai de thay Tab + cach tinh tien.");
        lbShopOpener.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbShopOpener.setForeground(new Color(60, 90, 160));
        lbShopOpener.setBorder(new EmptyBorder(2, 6, 2, 6));
        JSplitPane splitRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, spTabs, spItems);
        splitRight.setResizeWeight(0.30);
        JSplitPane splitAll = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, spNpc, splitRight);
        splitAll.setResizeWeight(0.36);
        JPanel centerWrap = new JPanel(new BorderLayout(4, 4));
        centerWrap.setBackground(CARD);
        centerWrap.add(lbShopOpener, BorderLayout.NORTH);
        centerWrap.add(splitAll, BorderLayout.CENTER);
        body.add(centerWrap, BorderLayout.CENTER);
        JPanel bot = new JPanel(new BorderLayout());
        bot.setBackground(CARD);
        JPanel rowBtn = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rowBtn.setBackground(CARD);
        JButton bAdd = btn("Them item"); JButton bEdit = btn("Sua item");
        JButton bCopy = btn("Nhan ban item"); JButton bDel = btn("Xoa");
        JButton bUp = btn("Len"); JButton bDown = btn("Xuong");
        JButton bPreview = btn("Xem truoc"); JButton bPublish = btn("XUAT BAN + Reload");
        bPublish.setBackground(new Color(40, 150, 80)); bPublish.setForeground(Color.WHITE);
        bAdd.addActionListener(e -> openShopItemDialog(null));
        bEdit.addActionListener(e -> { int r = shopItemTable.getSelectedRow(); if (r < 0 || r >= shopDraft.size()) { err("Chon 1 item"); return; } openShopItemDialog(shopDraft.get(r)); });
        bCopy.addActionListener(e -> copyShopItem());
        bDel.addActionListener(e -> delShopItem());
        bUp.addActionListener(e -> moveShopItem(-1));
        bDown.addActionListener(e -> moveShopItem(1));
        bPreview.addActionListener(e -> previewShopDraft());
        bPublish.addActionListener(e -> publishShopDraft());
        rowBtn.add(bAdd); rowBtn.add(bEdit); rowBtn.add(bCopy); rowBtn.add(bDel); rowBtn.add(bUp); rowBtn.add(bDown); rowBtn.add(bPreview); rowBtn.add(bPublish);
        lbShopStatus = new JLabel("Chua mo tab nao. Bam 'Mo tab de sua' sau khi chon tab trai.");
        lbShopStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbShopStatus.setForeground(new Color(90, 100, 130));
        bot.add(rowBtn, BorderLayout.NORTH);
        bot.add(lbShopStatus, BorderLayout.SOUTH);
        body.add(bot, BorderLayout.SOUTH);
        bg(this::loadNpcShops);
        bg(() -> { optDictCache = PanelService.listOptionDict(); });
        return wrapPage(body, "Cua Hang (Shop) - NPC -> Tab -> Item", "B1 chon NPC/Shop trai -> B2 chon Tab giua -> Mo tab -> sua Item phai. Shop Thuong: Gia + Loai tien. Shop Dac biet (Cua Hang dac biet): So luong + Doi bang item (Thoi Vang/Ngoc/Ruby...). Tu sinh JSON + Reload.");
    }

    // Tang 1: nap 26 shop + stats (3 tat do 0 tab)
    private void loadNpcShops() {
        java.util.List<Map<String, Object>> shops = PanelService.listNpcShops();
        java.util.List<Map<String, Object>> tabs = PanelService.listShopTabs();
        String stats = PanelService.shopStatsLine();
        SwingUtilities.invokeLater(() -> {
            npcShopCache = shops;
            shopTabCache = tabs;
            npcShopModel.setRowCount(0);
            for (Map<String, Object> m : shops) {
                npcShopModel.addRow(new Object[]{m.get("shop_id"), m.get("npc"), m.get("shop"), m.get("type_vn"), m.get("tabs"), m.get("status")});
            }
            lbShopStats.setText(stats);
            if (!shops.isEmpty()) {
                int sel = npcShopTable.getSelectedRow();
                if (sel < 0) { npcShopTable.setRowSelectionInterval(0, 0); }
                else filterTabsByShop();
            }
        });
    }

    private void filterTabsByShop() {
        if (curShopId < 0) return;
        shopModel.setRowCount(0);
        for (Map<String, Object> m : shopTabCache) {
            int sid = ((Number) m.get("shop_id")).intValue();
            if (sid != curShopId) continue;
            int tabId = ((Number) m.get("tab_id")).intValue();
            int cnt = 0;
            try {
                String j = PanelService.getTabItems(tabId);
                Object o = org.json.simple.JSONValue.parse(j);
                if (o instanceof org.json.simple.JSONArray) cnt = ((org.json.simple.JSONArray) o).size();
            } catch (Exception e) {}
            shopModel.addRow(new Object[]{m.get("tab_id"), m.get("tab"), m.get("idx"), cnt});
        }
        shopDraft = new java.util.ArrayList<>(); shopDraftTabId = -1;
        shopItemTableModel.setRowCount(0);
        lbShopStatus.setText("Shop " + curShopTag + " (" + shopModel.getRowCount() + " tab). Chon 1 tab giua -> Bam 'Mo tab de sua'.");
    }

    private void reloadTabsKeepNpc() {
        int keepShop = curShopId;
        bg(() -> {
            java.util.List<Map<String, Object>> shops = PanelService.listNpcShops();
            java.util.List<Map<String, Object>> tabs = PanelService.listShopTabs();
            String stats = PanelService.shopStatsLine();
            SwingUtilities.invokeLater(() -> {
                npcShopCache = shops; shopTabCache = tabs;
                npcShopModel.setRowCount(0);
                int selIdx = 0;
                for (int i = 0; i < shops.size(); i++) {
                    Map<String, Object> m = shops.get(i);
                    npcShopModel.addRow(new Object[]{m.get("shop_id"), m.get("npc"), m.get("shop"), m.get("type_vn"), m.get("tabs"), m.get("status")});
                    if (((Number) m.get("shop_id")).intValue() == keepShop) selIdx = i;
                }
                lbShopStats.setText(stats);
                if (!shops.isEmpty()) { npcShopTable.setRowSelectionInterval(selIdx, selIdx); }
                filterTabsByShop();
            });
        });
    }

    private String selShopTab() {
        int r = shopTable.getSelectedRow();
        if (r < 0) { err("Chon 1 tab o cot giua (sau khi chon NPC ben trai)"); return null; }
        return String.valueOf(shopModel.getValueAt(r, 0));
    }

    private String shopItemCol3Title() { return curShopType == 3 ? "Doi bang (nhap doi de chon)" : "Loai tien (chon)"; }

    private String specDisplay(int specId) {
        if (specId <= 0) return "Mac dinh";
        try {
            models.Template.ItemTemplate t = PanelService.getTemplateById(specId);
            if (t != null && t.name != null) return t.name + " (#" + specId + ")";
        } catch (Exception e) {}
        return PanelService.specItemName(specId);
    }

    private String shopCostTitle() { return curShopType == 3 ? "SL doi (go so)" : "Gia (go so)"; }

    private void openShopTabDraft() {
        String id = selShopTab();
        if (id == null) return;
        int tabId = Integer.parseInt(id);
        bg(() -> {
            String bk = PanelService.backupTab(tabId);
            java.util.List<ShopItemModel> list = PanelService.parseShopItems(tabId);
            SwingUtilities.invokeLater(() -> {
                shopDraft = list; shopDraftTabId = tabId;
                refreshShopDraftTable();
                lbShopStatus.setText("Dang sua tab_id=" + tabId + " (" + list.size() + " mon). Da backup: " + bk + ". Sua xong bam Xem truoc -> XUAT BAN.");
            });
        });
    }

    private void refreshShopDraftTable() {
        try { shopItemTable.getColumnModel().getColumn(2).setHeaderValue(curShopType == 3 ? "SL doi (go so)" : "Gia (go so)"); } catch (Exception e) {}
        try { shopItemTable.getTableHeader().repaint(); } catch (Exception e) {}
        shopItemTableModel.setRowCount(0);
        for (ShopItemModel m : shopDraft) {
            String typeStr = PanelService.typeSellName(m.typeSell);
            if (m.typeSell == 0) typeStr = "Vang (0)";
            else if (m.typeSell == 1) typeStr = "Ngoc (1)";
            else if (m.typeSell == 3) typeStr = "Ruby (3)";
            else if (m.typeSell == 4) typeStr = "Coupon/DiemSK (4)";
            shopItemTableModel.addRow(new Object[]{m.tempId, m.itemName, String.valueOf(m.cost), typeStr, specDisplay(m.itemSpec), m.isNew, m.isSell, PanelService.itemSummaryVN(m)});
        }
    }

    private void refreshShopDraftRow(int r) {
        if (r < 0 || r >= shopDraft.size()) return;
        ShopItemModel m = shopDraft.get(r);
        String typeStr = PanelService.typeSellName(m.typeSell);
        if (m.typeSell == 0) typeStr = "Vang (0)";
        else if (m.typeSell == 1) typeStr = "Ngoc (1)";
        else if (m.typeSell == 3) typeStr = "Ruby (3)";
        else if (m.typeSell == 4) typeStr = "Coupon/DiemSK (4)";
        shopInlineGuard = true;
        try {
            shopItemTableModel.setValueAt(m.tempId, r, 0);
            shopItemTableModel.setValueAt(m.itemName, r, 1);
            shopItemTableModel.setValueAt(String.valueOf(m.cost), r, 2);
            shopItemTableModel.setValueAt(typeStr, r, 3);
            shopItemTableModel.setValueAt(specDisplay(m.itemSpec), r, 4);
            shopItemTableModel.setValueAt(m.isNew, r, 5);
            shopItemTableModel.setValueAt(m.isSell, r, 6);
            shopItemTableModel.setValueAt(PanelService.itemSummaryVN(m), r, 7);
        } finally { shopInlineGuard = false; }
    }

    // Chon tien doi cho shop Dac biet: Thoi Vang #457, Luong Vang #1270, Hong ngoc #861...
    private Integer openSpecPicker() {
        final Integer[] result = new Integer[1];
        JDialog d = new JDialog(this, "Chon TIEN DOI (shop Dac biet)", true);
        d.setSize(560, 400);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] presets = new String[]{"Luong Vang #1270 (Cua Hang dac biet)", "Thoi Vang #457", "Hong ngoc #861", "Xu Tan Thu #1027", "Qua Hong Dao #542", "Vang Kim Co #543", "Hat giong #462", "Luong Bac #1271"};
        int[] ids = new int[]{1270, 457, 861, 1027, 542, 543, 462, 1271};
        JComboBox<String> cb = new JComboBox<>(presets);
        JTextField fId = new JTextField("1270", 8);
        cb.addActionListener(e -> { int i = cb.getSelectedIndex(); if (i >= 0 && i < ids.length) fId.setText(String.valueOf(ids[i])); });
        top.add(new JLabel("Chon nhanh:")); top.add(cb);
        top.add(new JLabel("Hoac TempID:")); top.add(fId);
        JLabel lb = new JLabel(" ");
        fId.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { upd(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { upd(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { upd(); }
            void upd() { try { int id = Integer.parseInt(fId.getText().trim()); lb.setText(specDisplay(id)); } catch (Exception ex) { lb.setText(" "); } }
        });
        try { lb.setText(specDisplay(1270)); } catch (Exception e) {}
        d.add(top, BorderLayout.NORTH);
        d.add(lb, BorderLayout.CENTER);
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bOk = btn("Chon"); bOk.setBackground(new Color(40, 150, 80)); bOk.setForeground(Color.WHITE);
        JButton bCancel = btn("Huy");
        bOk.addActionListener(e -> { try { result[0] = Integer.parseInt(fId.getText().trim()); } catch (Exception ex) { result[0] = null; } d.dispose(); });
        bCancel.addActionListener(e -> d.dispose());
        bot.add(bOk); bot.add(bCancel);
        d.add(bot, BorderLayout.SOUTH);
        d.setVisible(true);
        return result[0];
    }

    // Nhap doi o Option: mo bang sua option truc tiep tai cho (chon dropdown + go param + Them/Xoa)
    private void openShopOptionInlineEditor(ShopItemModel m, int row) {
        JDialog d = new JDialog(this, "Option: " + (m.itemName == null ? ("#" + m.tempId) : m.itemName), true);
        d.setSize(640, 460);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());
        DefaultTableModel om = new DefaultTableModel(new String[]{"Option (chon dropdown)", "Param (go so)", "Mo ta"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 0 || c == 1; }
        };
        JTable ot = new JTable(om);
        ot.setRowHeight(24);
        final boolean[] og = new boolean[]{false};
        Runnable reload = () -> {
            og[0] = true;
            try {
                om.setRowCount(0);
                for (ShopItemModel.Opt o : m.options) om.addRow(new Object[]{o.id, String.valueOf(o.param), PanelService.optionDisplay(o.id)});
            } finally { og[0] = false; }
        };
        reload.run();
        // dropdown option tieng Viet (gioi han rong de o Param khong bi day mat)
        JComboBox<String> cbOpt = new JComboBox<>();
        cbOpt.setPrototypeDisplayValue("#000 Sao pha le x99 | Ten dai........");
        cbOpt.setPreferredSize(new java.awt.Dimension(380, 25));
        cbOpt.setMaximumRowCount(20);
        java.util.List<Integer> ids = new java.util.ArrayList<>();
        if (optDictCache == null || optDictCache.isEmpty()) optDictCache = PanelService.listOptionDict();
        for (Map<String, Object> o : optDictCache) {
            int id = ((Number) o.get("id")).intValue();
            ids.add(id);
            String tv = o.get("ten_viet") == null ? "" : String.valueOf(o.get("ten_viet"));
            String nm = o.get("name") == null ? "" : String.valueOf(o.get("name"));
            cbOpt.addItem("#" + id + " " + (tv.isEmpty() ? nm : tv));
        }
        ot.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(cbOpt) {
            @Override public Object getCellEditorValue() {
                int i = cbOpt.getSelectedIndex();
                return (i >= 0 && i < ids.size()) ? ids.get(i) : super.getCellEditorValue();
            }
        });
        om.addTableModelListener(e -> {
            if (og[0]) return;
            if (e.getType() != javax.swing.event.TableModelEvent.UPDATE) return;
            if (e.getColumn() == 2) return;
            int r = e.getFirstRow();
            if (r < 0 || r >= m.options.size()) return;
            try {
                og[0] = true;
                Object ov = om.getValueAt(r, 0);
                int oid = ov instanceof Number ? ((Number) ov).intValue() : Integer.parseInt(String.valueOf(ov).replaceAll("[^0-9-]", ""));
                long pv = Long.parseLong(String.valueOf(om.getValueAt(r, 1)).trim().replaceAll("[^0-9-]", ""));
                m.options.get(r).id = oid;
                m.options.get(r).param = pv;
                om.setValueAt(PanelService.optionDisplay(oid), r, 2);
            } catch (Exception ex) {} finally { og[0] = false; }
        });
        JPanel pick = new JPanel(new BorderLayout(4, 4));
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Option:")); row1.add(cbOpt);
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField fP = new JTextField("0", 12);
        JButton bAdd = btn("Them dong");
        bAdd.setBackground(new Color(70, 120, 220)); bAdd.setForeground(Color.WHITE);
        JButton bDel = btn("Xoa dong");
        row2.add(new JLabel("Param:")); row2.add(fP);
        row2.add(bAdd); row2.add(bDel);
        JLabel hint = new JLabel("Chon Option -> nhap Param -> bam 'Them dong' (bang duoi co dong moi luu duoc).");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        hint.setForeground(new Color(90, 100, 130));
        pick.add(row1, BorderLayout.NORTH);
        pick.add(row2, BorderLayout.CENTER);
        pick.add(hint, BorderLayout.SOUTH);
        bAdd.addActionListener(e -> {
            int idx = cbOpt.getSelectedIndex();
            int oid = (idx >= 0 && idx < ids.size()) ? ids.get(idx) : 47;
            long pv = 0;
            try { pv = Long.parseLong(fP.getText().trim().replaceAll("[^0-9-]", "")); } catch (Exception ex) {}
            String er = optParamErrById(oid, pv);
            if (er != null) { err(er); return; }
            m.options.add(new ShopItemModel.Opt(oid, pv));
            reload.run();
        });
        bDel.addActionListener(e -> { int r = ot.getSelectedRow(); if (r >= 0 && r < m.options.size()) { m.options.remove(r); reload.run(); } });
        d.add(pick, BorderLayout.NORTH);
        d.add(new JScrollPane(ot), BorderLayout.CENTER);
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bOk = btn("Luu option");
        bOk.setBackground(new Color(40, 150, 80)); bOk.setForeground(Color.WHITE);
        bOk.addActionListener(e -> {
            try { if (ot.isEditing()) ot.getCellEditor().stopCellEditing(); } catch (Exception ex) {}
            if (m.options.isEmpty() && !ids.isEmpty()) {
                try {
                    int idx = cbOpt.getSelectedIndex();
                    int oid = (idx >= 0 && idx < ids.size()) ? ids.get(idx) : ids.get(0);
                    long pv = 0;
                    try { pv = Long.parseLong(fP.getText().trim().replaceAll("[^0-9-]", "")); } catch (Exception ex) {}
                    m.options.add(new ShopItemModel.Opt(oid, pv));
                } catch (Exception ex) {}
            }
            d.dispose(); refreshShopDraftTable(); shopItemTable.setRowSelectionInterval(row, row);
        });
        bot.add(bOk);
        d.add(bot, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void needDraft() { if (shopDraftTabId < 0) { err("Mo 1 tab truoc (nut 'Mo tab de sua')"); throw new RuntimeException("no draft"); } }

    private void copyShopItem() {
        try { needDraft(); } catch (Exception e) { return; }
        int r = shopItemTable.getSelectedRow();
        if (r < 0 || r >= shopDraft.size()) { err("Chon 1 item"); return; }
        shopDraft.add(r + 1, shopDraft.get(r).copy());
        refreshShopDraftTable();
    }

    private void delShopItem() {
        try { needDraft(); } catch (Exception e) { return; }
        int r = shopItemTable.getSelectedRow();
        if (r < 0 || r >= shopDraft.size()) { err("Chon 1 item"); return; }
        int c = JOptionPane.showConfirmDialog(this, "Xoa item dong " + (r + 1) + "?", "Xac nhan", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        shopDraft.remove(r);
        refreshShopDraftTable();
    }

    private void moveShopItem(int d) {
        try { needDraft(); } catch (Exception e) { return; }
        int r = shopItemTable.getSelectedRow();
        int n = r + d;
        if (r < 0 || n < 0 || n >= shopDraft.size()) return;
        ShopItemModel t = shopDraft.get(r); shopDraft.set(r, shopDraft.get(n)); shopDraft.set(n, t);
        refreshShopDraftTable();
        shopItemTable.setRowSelectionInterval(n, n);
    }

    private void previewShopDraft() {
        try { needDraft(); } catch (Exception e) { return; }
        String s = PanelService.previewShopItems(shopDraft);
        JTextArea a = new JTextArea(s, 20, 70);
        a.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(a), "Xem truoc tab " + shopDraftTabId, JOptionPane.INFORMATION_MESSAGE);
    }

    private void publishShopDraft() {
        try { needDraft(); } catch (Exception e) { return; }
        int c = JOptionPane.showConfirmDialog(this, "Xuat ban " + shopDraft.size() + " mon vao tab " + shopDraftTabId + "? (tu backup + reload shop)", "Xac nhan", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        bg(() -> {
            String r = PanelService.publishTabModels(shopDraftTabId, shopDraft, "admin-panel");
            SwingUtilities.invokeLater(() -> { info(r); reloadTabsKeepNpc(); });
        });
    }

    private void undoShopTab() {
        String id = selShopTab();
        if (id == null) return;
        int tabId = Integer.parseInt(id);
        bg(() -> {
            String r = PanelService.undoTab(tabId);
            SwingUtilities.invokeLater(() -> { info(r); if (tabId == shopDraftTabId) openShopTabDraft(); reloadTabsKeepNpc(); });
        });
    }

    private void showShopHistory() {
        String id = selShopTab();
        if (id == null) return;
        int tabId = Integer.parseInt(id);
        bg(() -> {
            java.util.List<Map<String, Object>> h = PanelService.tabHistory(tabId, 50);
            StringBuilder sb = new StringBuilder("Lich su tab " + tabId + ":\n");
            for (Map<String, Object> m : h) sb.append("#").append(m.get("id")).append(" [").append(m.get("action")).append("] ").append(m.get("actor")).append(" ").append(m.get("time")).append(" - ").append(m.get("summary")).append("\n");
            SwingUtilities.invokeLater(() -> { JTextArea a = new JTextArea(sb.toString(), 16, 80); a.setEditable(false); JOptionPane.showMessageDialog(this, new JScrollPane(a), "Lich su", JOptionPane.INFORMATION_MESSAGE); });
        });
    }

    private void cloneShopTab() {
        String id = selShopTab();
        if (id == null) return;
        String name = JOptionPane.showInputDialog(this, "Ten tab copy:", "Copy");
        if (name == null) return;
        int tabId = Integer.parseInt(id);
        bg(() -> { String r = PanelService.cloneTab(tabId, name.trim()); SwingUtilities.invokeLater(() -> { info(r); reloadTabsKeepNpc(); }); });
    }

    private void exportShopCsv() {
        String id = selShopTab();
        if (id == null) return;
        int tabId = Integer.parseInt(id);
        bg(() -> {
            String csv = PanelService.exportTabCsv(tabId);
            SwingUtilities.invokeLater(() -> { JTextArea a = new JTextArea(csv, 18, 80); a.setEditable(false); JOptionPane.showMessageDialog(this, new JScrollPane(a), "CSV tab " + tabId + " (copy de sua Excel)", JOptionPane.INFORMATION_MESSAGE); });
        });
    }

    private void importShopCsv() {
        try { needDraft(); } catch (Exception e) { return; }
        JTextArea a = new JTextArea(16, 80);
        int c = JOptionPane.showConfirmDialog(this, new JScrollPane(a), "Dan CSV vao (Ten,TempID,Gia,LoaiTien,IsNew,IsSell,Options,ItemSpec) - Chi xem truoc, khong ghi ngay", JOptionPane.OK_CANCEL_OPTION);
        if (c != JOptionPane.OK_OPTION) return;
        String csv = a.getText();
        bg(() -> {
            String r = PanelService.importTabCsv(shopDraftTabId, csv, false);
            SwingUtilities.invokeLater(() -> { JTextArea b = new JTextArea(r, 18, 80); b.setEditable(false); JOptionPane.showMessageDialog(this, new JScrollPane(b), "Ket qua doc CSV", JOptionPane.INFORMATION_MESSAGE); });
        });
    }

    // ---- Dialog sua 1 item: search VN + dropdown tien + builder option ----
    private void openShopItemDialog(ShopItemModel editTarget) {
        try { needDraft(); } catch (Exception e) { return; }
        boolean isNew = (editTarget == null);
        ShopItemModel m = isNew ? new ShopItemModel() : editTarget;
        if (isNew) { m.isSell = true; m.typeSell = 0; }
        boolean isSpec = (curShopType == 3);
        JDialog d = new JDialog(this, (isNew ? "Them item" : "Sua item") + " - shop " + curShopTag + (isSpec ? " [Dac biet: SL + Doi bang]" : " [Thuong: Gia + Loai tien]"), true);
        d.setSize(700, 640);
        d.setMinimumSize(new java.awt.Dimension(640, 560));
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());
        JPanel f = new JPanel(new GridLayout(0, 2, 8, 6));
        f.setBorder(new EmptyBorder(12, 12, 8, 12));
        JTextField fTemp = new JTextField(String.valueOf(m.tempId), 8);
        fTemp.setEditable(false);
        JLabel lbName = new JLabel(m.itemName == null ? "" : m.itemName);
        JButton bPick = new JButton("Chon vat pham (go tieng Viet: ao, kiem...)");
        bPick.addActionListener(e -> {
            Integer pick = openTemplatePicker();
            if (pick != null) {
                fTemp.setText(String.valueOf(pick));
                models.Template.ItemTemplate t = PanelService.getTemplateById(pick);
                if (t != null) lbName.setText(t.name + " | icon=" + t.iconID + " | " + PanelService.itemTypeNameVN(t.type));
            }
        });
        JTextField fCost = new JTextField(String.valueOf(m.cost), 10);
        JComboBox<String> cbSell = new JComboBox<>(new String[]{"Vang (0)", "Ngoc (1)", "Ruby (3)", "Coupon/DiemSK (4)"});
        if (m.typeSell == 1) cbSell.setSelectedIndex(1);
        else if (m.typeSell == 3) cbSell.setSelectedIndex(2);
        else if (m.typeSell == 4) cbSell.setSelectedIndex(3);
        else cbSell.setSelectedIndex(0);
        JCheckBox ckNew = new JCheckBox("Moi (is_new)", m.isNew);
        JCheckBox ckSell = new JCheckBox("Dang ban (is_sell)", m.isSell);
        JTextField fSpec = new JTextField(String.valueOf(m.itemSpec), 8);
        fSpec.setEditable(false);
        JLabel lbSpecName = new JLabel(specDisplay(m.itemSpec));
        lbSpecName.setForeground(new Color(60, 90, 160));
        JButton bSpecPick = new JButton("Chon tien doi (Thoi Vang/Luong Vang/Ngoc...)");
        bSpecPick.addActionListener(ev -> {
            Integer pick = openSpecPicker();
            if (pick != null) { fSpec.setText(String.valueOf(pick)); lbSpecName.setText(specDisplay(pick)); }
        });
        JLabel lbCost = new JLabel(isSpec ? "SL doi (vd: 10 Luong Vang):" : "Gia:");
        JLabel lbSell = new JLabel(isSpec ? "Loai tien (tu Doi bang):" : "Loai tien (dropdown):");
        cbSell.setEnabled(!isSpec);
        if (isSpec && m.typeSell != 3) m.typeSell = 3;
        f.add(new JLabel("TempID (khong go tay):")); f.add(fTemp);
        f.add(new JLabel("Ten:")); f.add(lbName);
        f.add(new JLabel("")); f.add(bPick);
        f.add(lbCost); f.add(fCost);
        f.add(lbSell); f.add(cbSell);
        f.add(ckNew); f.add(ckSell);
        if (isSpec) {
            f.add(new JLabel("Doi bang (item_spec - tien dac biet):")); f.add(fSpec);
            f.add(new JLabel("Dang la:")); f.add(lbSpecName);
            f.add(new JLabel("")); f.add(bSpecPick);
        } else {
            JLabel lbSpecHint = new JLabel("Shop Thuong: khong dung Doi bang (de 0).");
            lbSpecHint.setForeground(new Color(120, 120, 120));
            f.add(new JLabel("ItemSpec:")); f.add(lbSpecHint);
        }
        d.add(f, BorderLayout.NORTH);
        DefaultTableModel optModel = new DefaultTableModel(new String[]{"Option", "Param", "Mo ta"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable optTable = new JTable(optModel);
        Runnable reloadOpt = () -> {
            optModel.setRowCount(0);
            for (ShopItemModel.Opt o : m.options) optModel.addRow(new Object[]{o.id, o.param, PanelService.optionDisplay(o.id)});
        };
        reloadOpt.run();
        JPanel optBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> cbOpt = new JComboBox<>();
        java.util.List<Integer> optIds = new java.util.ArrayList<>();
        final JTextField fOptQ = tf(10);
        final Runnable fillOptCombo = () -> {
            String q = fOptQ.getText().trim().toLowerCase();
            String qNo = q;
            try { qNo = boss.BossManager.convertString(q); } catch (Exception ex) {}
            cbOpt.removeAllItems(); optIds.clear();
            if (optDictCache == null || optDictCache.isEmpty()) optDictCache = PanelService.listOptionDict();
            for (Map<String, Object> o : optDictCache) {
                int id = ((Number) o.get("id")).intValue();
                String tv = o.get("ten_viet") == null ? "" : String.valueOf(o.get("ten_viet"));
                String nm = o.get("name") == null ? "" : String.valueOf(o.get("name"));
                String lbl = "#" + id + " " + (tv.isEmpty() ? nm : tv);
                if (!q.isEmpty()) {
                    boolean match = lbl.toLowerCase().contains(q) || String.valueOf(id).contains(q);
                    if (!match) { try { match = boss.BossManager.convertString(lbl.toLowerCase()).contains(qNo); } catch (Exception ex) {} }
                    if (!match) continue;
                }
                optIds.add(id);
                cbOpt.addItem(lbl);
            }
        };
        fOptQ.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) { fillOptCombo.run(); }
        });
        bg(() -> SwingUtilities.invokeLater(fillOptCombo::run));
        JTextField fParam = new JTextField("0", 10);
        JButton bOptAdd = new JButton("Them option");
        JButton bOptDel = new JButton("Xoa option");
        bOptAdd.addActionListener(e -> {
            int idx = cbOpt.getSelectedIndex();
            if (idx < 0 || idx >= optIds.size()) { err("Chon 1 option"); return; }
            int oid = optIds.get(idx);
            long pv;
            try { pv = Long.parseLong(fParam.getText().trim()); } catch (Exception ex) { err("Param phai la so"); return; }
            String er = optParamErrById(oid, pv);
            if (er != null) { err(er); return; }
            m.options.add(new ShopItemModel.Opt(oid, pv));
            reloadOpt.run();
        });
        bOptDel.addActionListener(e -> { int r = optTable.getSelectedRow(); if (r < 0 || r >= m.options.size()) { err("Chon 1 option"); return; } m.options.remove(r); reloadOpt.run(); });
        optBar.add(new JLabel("Option (dropdown tu dien):")); optBar.add(cbOpt);
        optBar.add(new JLabel("Loc:")); optBar.add(fOptQ);
        optBar.add(new JLabel("Param (so):")); optBar.add(fParam);
        optBar.add(bOptAdd); optBar.add(bOptDel);
        JPanel optPanel = new JPanel(new BorderLayout());
        optPanel.setBorder(BorderFactory.createTitledBorder("Builder option (chon dropdown + nhap so, co nut Them/Xoa. Tom tat hien o bang item)"));
        optPanel.add(optBar, BorderLayout.NORTH);
        JScrollPane spOpt = new JScrollPane(optTable);
        spOpt.setPreferredSize(new java.awt.Dimension(600, 180));
        optPanel.add(spOpt, BorderLayout.CENTER);
        JPanel mid = new JPanel(new BorderLayout());
        mid.add(f, BorderLayout.NORTH);
        mid.add(optPanel, BorderLayout.CENTER);
        d.add(new JScrollPane(mid), BorderLayout.CENTER);
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bOk = new JButton(isNew ? "Them vao nhap" : "Luu vao nhap");
        bOk.setBackground(new Color(40, 150, 80)); bOk.setForeground(Color.WHITE);
        JButton bCancel = new JButton("Huy");
        bOk.addActionListener(e -> {
            try {
                m.tempId = Integer.parseInt(fTemp.getText().trim());
                if (PanelService.getTemplateById(m.tempId) == null) { err("temp_id khong ton tai: " + m.tempId); return; }
                m.cost = Integer.parseInt(fCost.getText().trim());
                if (m.cost < 0) { err("Gia khong am"); return; }
                int si = cbSell.getSelectedIndex();
                m.typeSell = (byte) (si == 1 ? 1 : si == 2 ? 3 : si == 3 ? 4 : 0);
                m.isNew = ckNew.isSelected();
                m.isSell = ckSell.isSelected();
                try { m.itemSpec = Integer.parseInt(fSpec.getText().trim()); } catch (Exception ex) { m.itemSpec = 0; }
                models.Template.ItemTemplate t = PanelService.getTemplateById(m.tempId);
                if (t != null) m.itemName = t.name;
                if (isNew) shopDraft.add(m);
                refreshShopDraftTable();
                lbShopStatus.setText("Nhap tab " + shopDraftTabId + ": " + shopDraft.size() + " mon (chua Xuat ban). Nho Xem truoc -> XUAT BAN.");
                d.dispose();
            } catch (Exception ex) { err("Loi: " + ex.getMessage()); }
        });
        bCancel.addActionListener(e -> d.dispose());
        bot.add(bOk); bot.add(bCancel);
        d.add(bot, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private Integer openTemplatePicker() {
        JDialog d = new JDialog(this, "Chon vat pham (tim tieng Viet, khong go ID tay)", true);
        d.setSize(700, 480);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField fKey = new JTextField(22);
        JComboBox<String> cbType = new JComboBox<>(new String[]{"Tat ca loai", "Ao(0)", "Quan(1)", "Gang(2)", "Giay(3)", "Rada(4)", "Cai trang/PK(5)", "SK(27)"});
        JButton bFind = new JButton("Tim (vd: ao)");
        top.add(new JLabel("Go ten:")); top.add(fKey); top.add(cbType); top.add(bFind);
        d.add(top, BorderLayout.NORTH);
        DefaultTableModel tm = new DefaultTableModel(new String[]{"ID", "Ten", "Loai", "Icon", "Part"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tb = new JTable(tm);
        d.add(new JScrollPane(tb), BorderLayout.CENTER);
        final Integer[] result = new Integer[1];
        Runnable doFind = () -> bg(() -> {
            int typeF = -1;
            int si = cbType.getSelectedIndex();
            if (si == 1) typeF = 0; else if (si == 2) typeF = 1; else if (si == 3) typeF = 2;
            else if (si == 4) typeF = 3; else if (si == 5) typeF = 4; else if (si == 6) typeF = 5; else if (si == 7) typeF = 27;
            java.util.List<Map<String, Object>> list = PanelService.listItemTemplatesFull(fKey.getText(), typeF, -1, 200);
            SwingUtilities.invokeLater(() -> {
                tm.setRowCount(0);
                for (Map<String, Object> x : list) tm.addRow(new Object[]{x.get("id"), x.get("name"), x.get("type_vn"), x.get("icon"), x.get("part")});
            });
        });
        bFind.addActionListener(e -> doFind.run());
        fKey.addActionListener(e -> doFind.run());
        doFind.run();
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bOk = new JButton("Chon dong nay");
        bOk.setBackground(new Color(70, 120, 220)); bOk.setForeground(Color.WHITE);
        bOk.addActionListener(e -> { int r = tb.getSelectedRow(); if (r < 0) { err("Chon 1 dong"); return; } result[0] = ((Number) tm.getValueAt(r, 0)).intValue(); d.dispose(); });
        JButton bCancel = new JButton("Huy");
        bCancel.addActionListener(e -> d.dispose());
        bot.add(bOk); bot.add(bCancel);
        d.add(bot, BorderLayout.SOUTH);
        d.setVisible(true);
        return result[0];
    }


    // ================= ITEMS: thu vien full + anh + lien ket =================
    private DefaultTableModel libModel;
    private JTable libTable;
    private java.util.List<Map<String, Object>> libCache = new java.util.ArrayList<>();

    private JPanel buildItems() {
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setBackground(CARD);
        body.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        JTextField fKey = tf(18);
        JComboBox<String> cbType = new JComboBox<>(new String[]{"Tat ca loai", "Ao(0)", "Quan(1)", "Gang(2)", "Giay(3)", "Rada(4)", "Cai trang/PK(5)", "SK(27)", "Khac"});
        JComboBox<String> cbGender = new JComboBox<>(new String[]{"Tat ca phai", "Trai Dat(0)", "Namek(1)", "Xayda(2)", "Chung(3)"});
        JButton bFind = btn("Tim (vd: ao)");
        JButton bUsed = btn("Dang ban o dau?");
        JButton bGive = btn("Tang cho player");
        JButton bCopyNew = btn("Tao do moi tu dong nay (tai dung anh cu)");
        JButton bSpec = btn("Xuat spec anh moi (muc 2)");
        final JLabel lbCount = new JLabel("Hien 0 do");
        Runnable doFind = () -> bg(() -> {
            int typeF = -1;
            int si = cbType.getSelectedIndex();
            if (si == 1) typeF = 0; else if (si == 2) typeF = 1; else if (si == 3) typeF = 2;
            else if (si == 4) typeF = 3; else if (si == 5) typeF = 4; else if (si == 6) typeF = 5; else if (si == 7) typeF = 27;
            int genF = cbGender.getSelectedIndex() - 1;
            if (si == 8) {
                java.util.List<Map<String, Object>> all = PanelService.listItemTemplatesFull(fKey.getText(), -1, genF < 0 ? -1 : genF, 100000);
                java.util.List<Map<String, Object>> fl = new java.util.ArrayList<>();
                for (Map<String, Object> x : all) { int t = ((Number) x.get("type")).intValue(); if (t != 0 && t != 1 && t != 2 && t != 3 && t != 4 && t != 5 && t != 27) fl.add(x); }
                libCache = fl;
            } else libCache = PanelService.listItemTemplatesFull(fKey.getText(), typeF, genF < 0 ? -1 : genF, 100000);
            SwingUtilities.invokeLater(() -> {
                libModel.setRowCount(0);
                for (Map<String, Object> x : libCache) libModel.addRow(new Object[]{x.get("id"), x.get("name"), x.get("type_vn"), x.get("gender_vn"), x.get("icon"), x.get("part"), x.get("gold")});
                lbCount.setText("Hien " + libCache.size() + " / " + server.Manager.ITEM_TEMPLATES.size() + " do (khong con gioi han 500)");
            });
        });
        bFind.addActionListener(e -> doFind.run());
        fKey.addActionListener(e -> doFind.run());
        bUsed.addActionListener(e -> {
            int r = libTable.getSelectedRow();
            if (r < 0) { err("Chon 1 dong"); return; }
            int temp = ((Number) libModel.getValueAt(r, 0)).intValue();
            bg(() -> {
                java.util.List<String> w = PanelService.whereUsed(temp);
                StringBuilder sb = new StringBuilder("Temp " + temp + " dang dung o:\n");
                for (String s : w) sb.append("- ").append(s).append("\n");
                SwingUtilities.invokeLater(() -> info(sb.toString()));
            });
        });
        bGive.addActionListener(e -> {
            int r = libTable.getSelectedRow();
            if (r < 0) { err("Chon 1 dong"); return; }
            int temp = ((Number) libModel.getValueAt(r, 0)).intValue();
            showGiveItemDialog("", temp, null);
        });
        bCopyNew.addActionListener(e -> {
            int r = libTable.getSelectedRow();
            if (r < 0) { err("Chon 1 dong lam mau"); return; }
            int temp = ((Number) libModel.getValueAt(r, 0)).intValue();
            String nm = JOptionPane.showInputDialog(this, "Ten do moi (gi nguyen chi so mau, doi ten):", libModel.getValueAt(r, 1) + " +");
            if (nm == null) return;
            bg(() -> { String s = PanelService.createItemTemplateReuse(temp, nm.trim(), -1, -1, -1, -9999); SwingUtilities.invokeLater(() -> { info(s); doFind.run(); }); });
        });
        bSpec.addActionListener(e -> {
            String ten = JOptionPane.showInputDialog(this, "Ten do muon ve anh moi:", "Do moi");
            if (ten == null) return;
            bg(() -> { String s = PanelService.exportNewItemSpec(ten, 0, -1, "Admin ghi them mo ta o day"); SwingUtilities.invokeLater(() -> info(s)); });
        });
        top.add(new JLabel("Tim:")); top.add(fKey); top.add(cbType); top.add(cbGender); top.add(bFind);
        top.add(bUsed); top.add(bGive); top.add(bCopyNew); top.add(bSpec); top.add(lbCount);
        body.add(top, BorderLayout.NORTH);
        libModel = new DefaultTableModel(new String[]{"ID", "Ten", "Loai", "Phai", "IconID", "Part", "Gia goc"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        libTable = new JTable(libModel);
        body.add(new JScrollPane(libTable), BorderLayout.CENTER);
        JLabel note = new JLabel("Preview anh: icon_id cat tu pack res x1-x4, part neu co. Neu thieu pack hien so ID, khong crash. Cot 'Dang ban o dau' tra shop/tab/giftcode. Tao do muc 1 tai dung anh cu dung ngay; muc 2 chi xuat spec cho dev.");
        note.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        note.setForeground(new Color(90, 100, 130));
        body.add(note, BorderLayout.SOUTH);
        bg(doFind);
        return wrapPage(body, "Thu Vien Vat Pham + Anh + Lien ket", "Toan bo item_template: tim tieng Viet, loc loai/phai, xem icon/part, biet dang ban o dau, tao do 2 muc.");
    }


    // ================= TU DIEN OPTION (dung chung Shop + Giftcode) =================
    private DefaultTableModel optDictModel;
    private JTable optDictTable;

    private JPanel buildOptionDict() {
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setBackground(CARD);
        body.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);        JButton bReload = btn("Tai lai tu dien");
        JButton bSave = btn("Luu dong dang chon");
        bSave.setBackground(new Color(40, 150, 80));
        bSave.setForeground(Color.WHITE);
        JButton bNew = btn("Them option moi");
        bNew.setBackground(new Color(70, 120, 220));
        bNew.setForeground(Color.WHITE);
        bNew.addActionListener(e -> showNewOptionDialog());
        optDictFKey = tf(12);
        optDictFKey.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) { applyOptFilter(); }
        });
        optDictCount = new JLabel("Hien 0 option");
        JTextField fTen = tf(16); JTextField fGoiY = tf(20);
        JTextField fCach = tf(40);
        JTextField fMin = tf("0", 8); JTextField fMax = tf("999999", 8);
        bReload.addActionListener(e -> bg(this::loadOptDict));
        bSave.addActionListener(e -> {
            int r = optDictTable.getSelectedRow();
            if (r < 0) { err("Chon 1 option"); return; }
            int oid = ((Number) optDictModel.getValueAt(r, 0)).intValue();
            bg(() -> {
                String s;
                try { s = PanelService.saveOptionDict(oid, fTen.getText().trim(), fGoiY.getText().trim(), Long.parseLong(fMin.getText().trim()), Long.parseLong(fMax.getText().trim()), fCach.getText().trim()); }
                catch (Exception ex) { s = "Loi: " + ex.getMessage(); }
                final String rr = s;
                SwingUtilities.invokeLater(() -> { info(rr); loadOptDict(); optDictCache = PanelService.listOptionDict(); });
            });
        });
        optDictModel = new DefaultTableModel(new String[]{"ID", "Ten goc", "Ten Viet", "Goi y param", "Min", "Max", "Cach dung"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        optDictTable = new JTable(optDictModel) {
            // tooltip hien noi day khi chuot treo len o (dac biet useful neu text bi cat)
            @Override public String getToolTipText(java.awt.event.MouseEvent e) {
                int r = rowAtPoint(e.getPoint()), c = columnAtPoint(e.getPoint());
                if (r < 0 || c < 0) return null;
                Object v = getValueAt(r, c);
                if (v == null || String.valueOf(v).isEmpty()) return null;
                String s = String.valueOf(v).replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
                return "<html><div style='width:380px'>" + s + "</div></html>";
            }
        };
        // cot co dinh rong + cuon ngang, khong keo lai bang (de doc 'Cach dung' cho de nhin)
        optDictTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        optDictTable.setRowHeight(24);
        optDictTable.setFillsViewportHeight(true);
        int[] optW = {46, 155, 150, 175, 75, 85, 430};
        for (int i = 0; i < optW.length && i < optDictTable.getColumnModel().getColumnCount(); i++) {
            optDictTable.getColumnModel().getColumn(i).setPreferredWidth(optW[i]);
        }
        // cot 'Cach dung': text den 252 ky tu -> wrap nhieu dong boi JTextArea renderer
        final JTextArea cachRender = new JTextArea();
        cachRender.setLineWrap(true);
        cachRender.setWrapStyleWord(true);
        cachRender.setOpaque(true);
        cachRender.setEditable(false);
        cachRender.setFocusable(false);
        cachRender.setFont(optDictTable.getFont());
        cachRender.setBorder(new EmptyBorder(3, 5, 3, 5));
        optDictTable.getColumnModel().getColumn(6).setCellRenderer(new javax.swing.table.TableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                cachRender.setText(v == null ? "" : String.valueOf(v));
                if (sel) { cachRender.setBackground(t.getSelectionBackground()); cachRender.setForeground(t.getSelectionForeground()); }
                else { cachRender.setBackground(t.getBackground()); cachRender.setForeground(t.getForeground()); }
                return cachRender;
            }
        });
        optDictTable.getSelectionModel().addListSelectionListener(e -> {
            int r = optDictTable.getSelectedRow();
            if (r < 0) return;
            fTen.setText(String.valueOf(optDictModel.getValueAt(r, 2) == null ? "" : optDictModel.getValueAt(r, 2)));
            fGoiY.setText(String.valueOf(optDictModel.getValueAt(r, 3) == null ? "" : optDictModel.getValueAt(r, 3)));
            fMin.setText(String.valueOf(optDictModel.getValueAt(r, 4) == null ? "0" : optDictModel.getValueAt(r, 4)));
            fMax.setText(String.valueOf(optDictModel.getValueAt(r, 5) == null ? "" : optDictModel.getValueAt(r, 5)));
            fCach.setText(String.valueOf(optDictModel.getValueAt(r, 6) == null ? "" : optDictModel.getValueAt(r, 6)));
        });
        top.add(bReload); top.add(new JLabel("Loc:")); top.add(optDictFKey); top.add(bNew);
        top.add(new JLabel("Ten Viet:")); top.add(fTen);
        top.add(new JLabel("Goi y:")); top.add(fGoiY);
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.setBackground(CARD);
        row2.add(new JLabel("Cach dung:")); row2.add(fCach);
        row2.add(new JLabel("Min:")); row2.add(fMin); row2.add(new JLabel("Max:")); row2.add(fMax);
        row2.add(bSave); row2.add(optDictCount);
        JPanel north = new JPanel(new GridLayout(2, 1));
        north.setBackground(CARD);
        north.add(top); north.add(row2);
        body.add(north, BorderLayout.NORTH);
        body.add(new JScrollPane(optDictTable), BorderLayout.CENTER);
        JLabel note = new JLabel("Vi du 47: Giap | 50: Suc danh. Min/Max de panel canh bao khi admin nhap param sai. Cot 'Cach dung' ghi cach option phat huy tac dung (vd: 'Cong don % vao dame', 'Chi tinh khi crit'). Khong sua bang goc item_option_template.");
        note.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        body.add(note, BorderLayout.SOUTH);
        bg(this::loadOptDict);
        return wrapPage(body, "Tu Dien Option", "Bang dich option: id + ten Viet + goi y param + min/max + cach dung. Dung chung Shop va Giftcode.");
    }

    private void loadOptDict() {
        java.util.List<Map<String, Object>> list = PanelService.listOptionDict();
        optDictCache = list;
        SwingUtilities.invokeLater(this::applyOptFilter);
    }

    private JTextField optDictFKey;
    private JLabel optDictCount;

    // loc tu dien option theo id/ten/khong dau (go trong o 'Loc' cua Tab Tu Dien Option)
    private void applyOptFilter() {
        if (optDictModel == null) return;
        String key = optDictFKey == null ? "" : optDictFKey.getText().trim().toLowerCase();
        String keyNo = key;
        try { keyNo = boss.BossManager.convertString(key); } catch (Exception e) {}
        java.util.List<Map<String, Object>> list = optDictCache;
        optDictModel.setRowCount(0);
        int shown = 0;
        if (list != null) {
            for (Map<String, Object> m : list) {
                if (!key.isEmpty()) {
                    String hay = (m.get("id") + " " + m.get("name") + " " + m.get("ten_viet") + " " + m.get("cach_dung")).toLowerCase();
                    boolean match = hay.contains(key);
                    if (!match) { try { match = boss.BossManager.convertString(hay).contains(keyNo); } catch (Exception e) {} }
                    if (!match) continue;
                }
                optDictModel.addRow(new Object[]{m.get("id"), m.get("name"), m.get("ten_viet"), m.get("goi_y"), m.get("min"), m.get("max"), m.get("cach_dung")});
                shown++;
            }
        }
        if (optDictCount != null) optDictCount.setText("Hien " + shown + "/" + (list == null ? 0 : list.size()) + " option");
        reflowOptDictRows();
    }

    // tinh chieu cao tung dong theo do dai 'Cach dung' de text wrap het, khong bi cat
    private void reflowOptDictRows() {
        if (optDictTable == null || optDictModel == null) return;
        try {
            int cw = optDictTable.getColumnModel().getColumn(6).getWidth() - 14;
            if (cw < 80) cw = 80;
            java.awt.FontMetrics fm = optDictTable.getFontMetrics(optDictTable.getFont());
            int lh = fm.getHeight() + 4;
            for (int r = 0; r < optDictTable.getRowCount(); r++) {
                Object v = optDictModel.getValueAt(r, 6);
                String s = v == null ? "" : String.valueOf(v);
                int lines = 1, cur = 0;
                for (String w : s.split(" +")) {
                    int ww = fm.stringWidth(w);
                    if (cur == 0) cur = ww;
                    else if (cur + 1 + ww <= cw) cur += 1 + ww;
                    else { lines++; cur = ww; }
                }
                optDictTable.setRowHeight(r, Math.max(24, lines * lh + 8));
            }
        } catch (Exception e) {}
    }

    // kiem tra param co nam trong min/max cua Tu Dien Option (null = hop le)
    private String optParamErrById(int oid, long pv) {
        try {
            if (optDictCache == null || optDictCache.isEmpty()) return null;
            for (Map<String, Object> m : optDictCache) {
                if (((Number) m.get("id")).intValue() != oid) continue;
                long lo = ((Number) m.get("min")).longValue();
                long hi = ((Number) m.get("max")).longValue();
                if (hi > lo && (pv < lo || pv > hi)) {
                    return "Param " + pv + " ngoai khoang [" + lo + " .. " + hi + "] cua option #" + oid + " (sua Min/Max o Tab Tu Dien Option neu muon cho phep)";
                }
                break;
            }
        } catch (Exception e) {}
        return null;
    }

    // dialog tao option MOI (id ke tiep tu dong, chen truc tiep vao item_option_template)
    private void showNewOptionDialog() {
        final JDialog d = new JDialog(this, "Them option moi", true);
        d.setSize(480, 384);
        d.setLocationRelativeTo(this);
        JPanel p = new JPanel(new java.awt.GridLayout(0, 2, 8, 6));
        p.setBorder(new EmptyBorder(12, 12, 12, 12));
        final JTextField fId = tf("", 8);
        final JTextField fName = tf(24);
        final JTextField fType = tf("0", 4);
        final JTextField fTv = tf(24);
        final JTextField fGoiY = tf(24);
        final JTextField fCach = tf(24);
        final JTextField fMin = tf("0", 10);
        final JTextField fMax = tf("999999", 10);
        bg(() -> {
            final int next = server.Manager.ITEM_OPTION_TEMPLATES.size();
            SwingUtilities.invokeLater(() -> fId.setText(String.valueOf(next)));
        });
        p.add(new JLabel("ID (ke tiep, tu dong):")); p.add(fId);
        p.add(new JLabel("Ten goc (dung # cho param):")); p.add(fName);
        p.add(new JLabel("Type (0 = mac dinh):")); p.add(fType);
        p.add(new JLabel("Ten Viet:")); p.add(fTv);
        p.add(new JLabel("Goi y param:")); p.add(fGoiY);
        p.add(new JLabel("Cach dung:")); p.add(fCach);
        p.add(new JLabel("Min:")); p.add(fMin);
        p.add(new JLabel("Max:")); p.add(fMax);
        JLabel tip = new JLabel("<html>• ID phai ke tiep 0..N - client gan id = vi tri mang, chen giua se lam SAI ten option.<br>"
                + "• Toi da 255 option (dem 1 byte). Option moi: client dang online can DANG NHAP LAI moi thay.<br>"
                + "• Ten goc nen co '#' (vi du '+# suc danh') de hien thi param.</html>");
        tip.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tip.setForeground(new Color(90, 100, 130));
        JPanel south = new JPanel(new BorderLayout());
        south.setBorder(new EmptyBorder(0, 12, 8, 12));
        south.add(tip, BorderLayout.CENTER);
        JPanel bb = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bOk = btn("TAO OPTION");
        bOk.setBackground(new Color(40, 150, 80));
        bOk.setForeground(Color.WHITE);
        JButton bCancel = btn("Huy");
        bb.add(bOk); bb.add(bCancel);
        south.add(bb, BorderLayout.EAST);
        d.add(p, BorderLayout.CENTER);
        d.add(south, BorderLayout.SOUTH);
        bCancel.addActionListener(e -> d.dispose());
        bOk.addActionListener(e -> bg(() -> {
            String s;
            try {
                s = PanelService.createOptionTemplate(Integer.parseInt(fId.getText().trim()), fName.getText().trim(),
                        Integer.parseInt(fType.getText().trim()), fTv.getText().trim(), fGoiY.getText().trim(),
                        Long.parseLong(fMin.getText().trim()), Long.parseLong(fMax.getText().trim()), fCach.getText().trim());
            } catch (Exception ex) { s = "Loi nhap: " + ex.getMessage(); }
            final String rr = s;
            SwingUtilities.invokeLater(() -> {
                if (rr.startsWith("OK")) { info(rr); d.dispose(); } else { err(rr); }
                loadOptDict();
            });
        }));
        d.setVisible(true);
    }

    // ================= NPC + SHOP (wizard 3 buoc, buoc 3 cho research map) =================
    private DefaultTableModel npcModel;
    private JTable npcTable;

    private JPanel buildNpc() {
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setBackground(CARD);
        body.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        JButton bReload = btn("Tai lai NPC");
        JTextField fName = tf(14); JTextField fHead = tf("0", 5); JTextField fBody = tf("0", 5);
        JTextField fLeg = tf("0", 5); JTextField fAv = tf("0", 5);
        JButton bCreate = btn("Buoc 1: Tao npc_template");
        bCreate.setBackground(new Color(70, 120, 220)); bCreate.setForeground(Color.WHITE);
        JTextField fShopTag = tf(12); JTextField fNpcId = tf(6);
        JButton bShop = btn("Buoc 2: Tao shop + tab rong");
        bShop.setBackground(new Color(40, 150, 80)); bShop.setForeground(Color.WHITE);
        bReload.addActionListener(e -> bg(this::loadNpc));
        bCreate.addActionListener(e -> bg(() -> {
            String s;
            try { s = PanelService.createNpcDraft(fName.getText().trim(), Integer.parseInt(fHead.getText().trim()), Integer.parseInt(fBody.getText().trim()), Integer.parseInt(fLeg.getText().trim()), Integer.parseInt(fAv.getText().trim())); }
            catch (Exception ex) { s = "Loi: " + ex.getMessage(); }
            final String rr = s;
            SwingUtilities.invokeLater(() -> { info(rr); loadNpc(); });
        }));
        bShop.addActionListener(e -> bg(() -> {
            String s;
            try { s = PanelService.createShopForNpc(Integer.parseInt(fNpcId.getText().trim()), fShopTag.getText().trim(), 0); }
            catch (Exception ex) { s = "Loi: " + ex.getMessage(); }
            final String rr = s;
            SwingUtilities.invokeLater(() -> info(rr));
        }));
        top.add(bReload);
        top.add(new JLabel("Ten NPC:")); top.add(fName);
        top.add(new JLabel("Head:")); top.add(fHead); top.add(new JLabel("Body:")); top.add(fBody);
        top.add(new JLabel("Leg:")); top.add(fLeg); top.add(new JLabel("Avatar:")); top.add(fAv); top.add(bCreate);
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.setBackground(CARD);
        row2.add(new JLabel("NPC_ID:")); row2.add(fNpcId);
        row2.add(new JLabel("Tag shop (vd SHOP_SUKIEN):")); row2.add(fShopTag); row2.add(bShop);
        JPanel north = new JPanel(new GridLayout(2, 1));
        north.setBackground(CARD);
        north.add(top); north.add(row2);
        body.add(north, BorderLayout.NORTH);
        npcModel = new DefaultTableModel(new String[]{"ID", "Ten", "Head", "Body", "Leg", "Avatar"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        npcTable = new JTable(npcModel);
        body.add(new JScrollPane(npcTable), BorderLayout.CENTER);
        JLabel note = new JLabel("Buoc 3 dat NPC vao map: hien NPC load theo map/npcId/npcX/npcY o Map.initNpc (cho research tiep). Tam tao template + shop rong truoc, game khong crash.");
        note.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        body.add(note, BorderLayout.SOUTH);
        bg(this::loadNpc);
        return wrapPage(body, "NPC + Shop (wizard)", "Buoc 1 tao npc_template -> Buoc 2 tao shop + tab rong -> Buoc 3 dat vao map (cho research).");
    }

    private void loadNpc() {
        java.util.List<Map<String, Object>> list = PanelService.listNpc(500);
        SwingUtilities.invokeLater(() -> {
            npcModel.setRowCount(0);
            for (Map<String, Object> m : list) npcModel.addRow(new Object[]{m.get("id"), m.get("name"), m.get("head"), m.get("body"), m.get("leg"), m.get("avatar")});
        });
    }

    // ================= BOSS (phan tang cho admin moi: Chon nhom -> Chon boss -> Sua) =================
    private DefaultTableModel bossModel;
    private JTable bossTable;
    private java.util.List<Boss> bossCache = new java.util.ArrayList<>();
    private JComboBox<String> cbBossGroup;
    private JTextField fBossSearch;
    private JList<String> lsBossField;
    private DefaultListModel<String> lsBossFieldModel;
    private java.util.List<Map<String, Object>> bossCatCache = new java.util.ArrayList<>();
    private JLabel lbBossTitle;
    private JTextArea lbBossGoc, lbBossGoiY, lbBossReward;
    private JTextField fBossDame, fBossHp, fBossRest, fBossMaps, fBossDrop, fBossQty, fBossRate;
    private JTextArea fBossNote;
    private String curBossField = null;
    // DOT 4: tim spawn
    private DefaultTableModel spawnModel;
    private JTable spawnTable;
    private JTextField fSpawnSearch;
    private JLabel lbSpawnCount;
    private java.util.List<Map<String, Object>> spawnCache = new java.util.ArrayList<>();
    // DOT 4: world boss (V2: lich goi + xoay khu + qua JSON)
    private JCheckBox cbWBOn;
    private JTextField fWBMap, fWBDur, fWBHp, fWBDame, fWBTop1, fWBTop23, fWBTop410, fWBConsol, fWBMinDame;
    private JTextField fWBSpawn, fWBLife, fWBZoneSec, fWBStartZone;
    private JTextField fWBTop1J, fWBTop23J, fWBTop410J, fWBConsolJ;
    private JComboBox<String> cbWBMapList;
    private java.util.List<Integer> wbMapIds = new java.util.ArrayList<>();
    private JLabel lbWBLive, lbWBLeft, lbWBLuot, lbWBMapName, lbWBSeason;
    private JTextArea taWBTop;

    private JPanel buildBoss() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("1. Chon & Sua Boss (admin moi dung tab nay)", bossEditTab());
        // Tab live giu lai
        JPanel live = new JPanel(new BorderLayout(8, 8));
        live.setBackground(CARD);
        live.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        JButton bReload = btn("Tai lai boss");
        JTextField fId = tf(8);
        JButton bSummon = btn("Goi Boss theo ID");
        JButton bKill = btn("Kill boss chon");
        JButton bLeave = btn("Duoi khoi map");
        JTextField fZone = tf(6);
        bSummon.setBackground(new Color(40, 140, 70)); bSummon.setForeground(Color.WHITE);
        bKill.setBackground(new Color(220, 60, 70)); bKill.setForeground(Color.WHITE);
        bReload.addActionListener(e -> bg(this::loadBosses));
        bSummon.addActionListener(e -> bg(() -> { String r; try { int z = Integer.parseInt(fZone.getText().trim()); r = PanelService.spawnBossByAdmin(Integer.parseInt(fId.getText().trim()), z); } catch (Exception ex) { r = "Loi: " + ex.getMessage(); } final String rr = r; SwingUtilities.invokeLater(() -> { info(rr); loadBosses(); }); }));
        bKill.addActionListener(e -> { Boss b = selBoss(); if (b == null) return; bg(() -> { String r = PanelService.killBoss(b); SwingUtilities.invokeLater(() -> { info(r); loadBosses(); }); }); });
        bLeave.addActionListener(e -> { Boss b = selBoss(); if (b == null) return; bg(() -> { String r = PanelService.removeBossFromMap(b); SwingUtilities.invokeLater(() -> { info(r); loadBosses(); }); }); });
        top.add(bReload); top.add(new JLabel("BossID (xem ben tab 1):")); top.add(fId); top.add(new JLabel("Zone(-1=def):")); top.add(fZone); top.add(bSummon); top.add(bKill); top.add(bLeave);
        live.add(top, BorderLayout.NORTH);
        bossModel = new DefaultTableModel(new String[]{"#", "Thong tin"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bossTable = new JTable(bossModel);
        live.add(new JScrollPane(bossTable), BorderLayout.CENTER);
        bg(this::loadBosses);
        tabs.addTab("2. Boss Live (kill/goi)", live);
        tabs.addTab("3. Tim Boss Spawn O Dau", bossSpawnTab());
        tabs.addTab("4. Boss The Gioi (bat tu, Top dame)", worldBossTab());
        JPanel body = new JPanel(new BorderLayout());
        body.add(tabs, BorderLayout.CENTER);
        return wrapPage(body, "Cau Hinh Boss", "Tab 1: sua chi so | Tab 2 (live): kill/goi | Tab 3: tim boss spawn o map/khu nao | Tab 4: Boss The Gioi bat tu + Top dame");
    }

    // ================= MAP & MOB (panel_map_mob) =================
    private JComboBox<String> cbMapId;
    private JTable tblMapMob;
    private DefaultTableModel mapMobModel;
    private List<Map<String, Object>> mapMobRowsCache = new java.util.ArrayList<>();
    private final java.util.Map<Integer, String> mapNameCache = new java.util.HashMap<>();
    private JLabel lbMapStat;
    private JTextField fMapHp, fMapSd, fMapX, fMapY, fMapLevel, fMapMobTemp, fMapMobIndex;
    private JTextArea fMapDropJson, fMapNote;
    private JCheckBox chkMapIsNew;
    private JLabel lbMapTitle;

    private JPanel buildMapMob() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(CARD);
        root.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JLabel guide = new JLabel("<html><b>Cach dung:</b> 1) Chon <b>Map</b> -> bang ben trai hien <b>quai THAT</b> cua map (goc tu map_template) -> 2) Click 1 quai de sua, hoac 'Form moi' de them quai moi -> 3) Bam <b>Luu</b>. Cot <b>Ov</b> = dong da co override. Drop ap dung <b>ngay</b>; HP/SD ap dung khi restart zone. Luu y: sua quai goc nen giu nguyen <b>Temp + Index</b> (game khop map+temp+index moi cap nhat).</html>");
        root.add(guide, BorderLayout.NORTH);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.45);
        split.setDividerLocation(560);

        // ===== Trai: chon map + bang quai =====
        JPanel left = new JPanel(new BorderLayout(8, 8));
        left.setBackground(CARD);
        JPanel ltop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ltop.setBackground(CARD);
        ltop.add(new JLabel("Chon map:"));
        cbMapId = new JComboBox<>();
        cbMapId.setEditable(true);
        cbMapId.setPreferredSize(new Dimension(320, 26));
        List<Map<String, Object>> maps = PanelService.listAllMaps();
        for (Map<String, Object> mm : maps) {
            try {
                int id = ((Number) mm.get("id")).intValue();
                String nm = mm.get("name") == null ? "" : String.valueOf(mm.get("name"));
                mapNameCache.put(id, nm);
                cbMapId.addItem(id + (nm.isEmpty() ? "" : " - " + nm));
            } catch (Exception ex) {}
        }
        if (cbMapId.getItemCount() == 0) for (int i = 0; i < 250; i++) cbMapId.addItem(String.valueOf(i));
        JButton bLoadMap = btn("Tai quai map");
        ltop.add(cbMapId); ltop.add(bLoadMap);
        left.add(ltop, BorderLayout.NORTH);
        mapMobModel = new DefaultTableModel(new String[]{"ID", "Temp", "Ten quai", "Index", "Lv", "HP", "SD%", "Moi?", "Ov", "Ghi chu"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblMapMob = new JTable(mapMobModel);
        tblMapMob.setRowHeight(22);
        left.add(new JScrollPane(tblMapMob), BorderLayout.CENTER);
        JPanel lbtm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lbtm.setBackground(CARD);
        JButton bDel = btn("Xoa override dong chon");
        bDel.setBackground(new Color(220, 60, 70)); bDel.setForeground(Color.WHITE);
        JButton bClear = btn("Form moi (them quai)");
        lbtm.add(bDel); lbtm.add(bClear);
        lbMapStat = new JLabel("Chon map de xem quai");
        lbMapStat.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbMapStat.setForeground(new Color(60, 90, 160));
        lbtm.add(lbMapStat);
        left.add(lbtm, BorderLayout.SOUTH);

        // ===== Phai: form sua =====
        JPanel right = new JPanel();
        right.setBackground(CARD);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        lbMapTitle = new JLabel("Them / sua quai (map 0)");
        lbMapTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        right.add(lbMapTitle);
        right.add(Box.createVerticalStrut(6));
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(CARD);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        fMapMobTemp = tf(15); fMapMobIndex = tf(15); fMapLevel = tf(15);
        fMapHp = tf(15); fMapSd = tf(15); fMapX = tf(15); fMapY = tf(15);
        fMapDropJson = new JTextArea(6, 15); fMapDropJson.setLineWrap(true); fMapDropJson.setWrapStyleWord(true);
        fMapNote = new JTextArea(2, 15); fMapNote.setLineWrap(true); fMapNote.setWrapStyleWord(true);
        chkMapIsNew = new JCheckBox("La quai moi (them vao map, khong phai override)");
        chkMapIsNew.setBackground(CARD);
        addRow(grid, gc, 0, "Mob temp (id template):", fMapMobTemp);
        addRow(grid, gc, 1, "Mob index (-1 = them moi):", fMapMobIndex);
        addRow(grid, gc, 2, "Level (1-50):", fMapLevel);
        addRow(grid, gc, 3, "HP (0 = giu goc; nen 1 ty+):", fMapHp);
        addRow(grid, gc, 4, "SD % (-1 = giu goc):", fMapSd);
        addRow(grid, gc, 5, "X (vi tri):", fMapX);
        addRow(grid, gc, 6, "Y (vi tri):", fMapY);
        addRow(grid, gc, 7, "La quai moi (is_new):", chkMapIsNew);
        addRow(grid, gc, 8, "Drop JSON [{temp_id,qty,rate,min,max,options}]:", new JScrollPane(fMapDropJson));
        addRow(grid, gc, 9, "Ghi chu:", new JScrollPane(fMapNote));
        right.add(grid);
        right.add(Box.createVerticalStrut(8));
        JPanel acts = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acts.setBackground(CARD);
        JButton bSave = btn("Luu");
        bSave.setBackground(new Color(40, 140, 70)); bSave.setForeground(Color.WHITE);
        JButton bLoadBossCat = btn("Ap dung + reload DB");
        acts.add(bSave); acts.add(bLoadBossCat);
        right.add(acts);
        JLabel hint = new JLabel("<html><small>Vi du drop JSON: <code>[{\"temp_id\":14,\"qty\":1,\"rate\":50,\"min\":25000,\"max\":30000,\"options\":[{\"id\":50,\"param\":30000}]}]</code> (cai trang 25k-30k SD)</small></html>");
        right.add(hint);

        // ===== Wire events =====
        Runnable doLoad = () -> {
            final int mapId;
            try { mapId = parseMapId(); }
            catch (Exception ex) { info("Map ID phai la so"); return; }
            String nm = mapNameCache.get(mapId);
            lbMapTitle.setText("Map " + mapId + (nm == null || nm.isEmpty() ? "" : " - " + nm) + " - chon 1 quai de sua / them quai moi");
            bg(() -> {
                List<Map<String, Object>> rows = PanelService.listMapMobs(mapId);
                SwingUtilities.invokeLater(() -> {
                    mapMobRowsCache = rows;
                    mapMobModel.setRowCount(0);
                    int ov = 0;
                    for (Map<String, Object> r : rows) {
                        if (((Number) r.getOrDefault("has_ov", 0)).intValue() == 1) ov++;
                        int sd = ((Number) r.getOrDefault("sd", -1)).intValue();
                        mapMobModel.addRow(new Object[]{
                            r.get("id"), r.get("mob_temp"), r.get("mob_name"), r.get("mob_index"),
                            r.get("level"), r.get("hp"), sd < 0 ? "goc" : String.valueOf(sd),
                            ((Number) r.getOrDefault("is_new", 0)).intValue() == 1 ? "Y" : "",
                            ((Number) r.getOrDefault("has_ov", 0)).intValue() == 1 ? "Y" : "",
                            r.get("note")
                        });
                    }
                    if (rows.isEmpty()) lbMapStat.setText("Map " + mapId + ": khong co quai goc trong map_template (chi them quai moi duoc)");
                    else lbMapStat.setText("Map " + mapId + ": " + rows.size() + " quai - " + ov + " co override");
                });
            });
        };
        bLoadMap.addActionListener(e -> doLoad.run());
        cbMapId.addActionListener(e -> doLoad.run());
        if (cbMapId.getItemCount() > 0) doLoad.run();

        tblMapMob.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int r = tblMapMob.getSelectedRow();
            if (r < 0 || r >= mapMobRowsCache.size()) return;
            Map<String, Object> fr = mapMobRowsCache.get(r);
            fMapMobTemp.setText(String.valueOf(fr.get("mob_temp")));
            fMapMobIndex.setText(String.valueOf(fr.get("mob_index")));
            fMapLevel.setText(String.valueOf(fr.get("level")));
            fMapHp.setText(String.valueOf(fr.get("hp")));
            fMapSd.setText(String.valueOf(fr.getOrDefault("sd", -1)));
            fMapX.setText(String.valueOf(fr.get("x")));
            fMapY.setText(String.valueOf(fr.get("y")));
            fMapDropJson.setText(fr.get("drop_json") == null ? "" : String.valueOf(fr.get("drop_json")));
            fMapNote.setText(fr.get("note") == null ? "" : String.valueOf(fr.get("note")));
            chkMapIsNew.setSelected(((Number) fr.getOrDefault("is_new", 0)).intValue() == 1);
            int id = ((Number) fr.getOrDefault("id", 0)).intValue();
            int idx = ((Number) fr.getOrDefault("mob_index", -1)).intValue();
            lbMapTitle.setText(id > 0
                    ? ("Sua quai override #" + id + " (index " + idx + ")")
                    : ("Tao override cho quai goc index " + idx + " (chua sua)"));
        });

        bSave.addActionListener(e -> {
            int mapId; try { mapId = parseMapId(); } catch (Exception ex) { info("Map ID sai"); return; }
            int r = tblMapMob.getSelectedRow();
            int id = (r < 0) ? 0 : (Integer) mapMobModel.getValueAt(r, 0);
            int mobTemp, mobIndex, level, sd, x, y, isNew;
            double hp;
            try { mobTemp = Integer.parseInt(fMapMobTemp.getText().trim()); mobIndex = Integer.parseInt(fMapMobIndex.getText().trim()); level = Integer.parseInt(fMapLevel.getText().trim()); sd = Integer.parseInt(fMapSd.getText().trim()); x = Integer.parseInt(fMapX.getText().trim()); y = Integer.parseInt(fMapY.getText().trim()); hp = Double.parseDouble(fMapHp.getText().trim()); } catch (Exception ex) { info("Phai nhap so hop le (HP/SD/X/Y/Lv/Index/Temp)"); return; }
            isNew = chkMapIsNew.isSelected() ? 1 : 0;
            String drop = fMapDropJson.getText() == null ? "" : fMapDropJson.getText().trim();
            String note = fMapNote.getText() == null ? "" : fMapNote.getText().trim();
            bg(() -> {
                String rs = PanelService.saveMapMobOverride(id, mapId, mobIndex, mobTemp, level, hp, sd, x, y, drop, isNew, note);
                SwingUtilities.invokeLater(() -> { info(rs); doLoad.run(); });
            });
        });

        bDel.addActionListener(e -> {
            int r = tblMapMob.getSelectedRow(); if (r < 0) { info("Chon dong can xoa"); return; }
            int id = (Integer) mapMobModel.getValueAt(r, 0);
            if (id <= 0) { info("Day la quai GOC cua map - chua co override de xoa."); return; }
            bg(() -> {
                String rs = PanelService.deleteMapMobOverride(id);
                SwingUtilities.invokeLater(() -> { info(rs); doLoad.run(); });
            });
        });

        bClear.addActionListener(e -> {
            fMapMobTemp.setText(""); fMapMobIndex.setText("-1"); fMapLevel.setText("1");
            fMapHp.setText("0"); fMapSd.setText("-1"); fMapX.setText("0"); fMapY.setText("0");
            fMapDropJson.setText(""); fMapNote.setText(""); chkMapIsNew.setSelected(false);
            tblMapMob.clearSelection();
            lbMapTitle.setText("Them quai moi");
        });

        bLoadBossCat.addActionListener(e -> bg(() -> { mob.MobOverride.gI().reload(); SwingUtilities.invokeLater(() -> info("Da reload MobOverride cache tu DB")); }));

        JScrollPane scR = new JScrollPane(right);
        scR.getVerticalScrollBar().setUnitIncrement(16);
        split.setLeftComponent(left);
        split.setRightComponent(scR);
        root.add(split, BorderLayout.CENTER);
        return wrapPage(root, "Map & Mob (panel_map_mob)", "Xem quai THAT cua map, override HP/SD/drop, them quai moi");
    }

    private int parseMapId() {
        String s = String.valueOf(cbMapId.getSelectedItem());
        int i = s.indexOf(" - ");
        if (i >= 0) s = s.substring(0, i);
        s = s.trim();
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < s.length(); k++) {
            char c = s.charAt(k);
            if (c >= '0' && c <= '9') sb.append(c);
            else break;
        }
        if (sb.length() == 0) throw new NumberFormatException(s);
        return Integer.parseInt(sb.toString());
    }

    private JPanel bossEditTab() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(CARD);
        root.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JLabel guide = new JLabel("<html><b>Cach dung (30 giay):</b> 1) Chon <b>Nhom boss</b> -> 2) Go <b>tim</b> hoac click ten boss ben trai -> 3) Sua o ben phai -> 4) Bam <b>Luu</b>. De trong/nhap -1 = giu nguyen goc. Boss goi moi se nhan chi so moi.</html>");
        root.add(guide, BorderLayout.NORTH);
        // Trai: nhom + tim + list
        JPanel left = new JPanel(new BorderLayout(6, 6));
        left.setBackground(CARD);
        left.setPreferredSize(new Dimension(320, 500));
        cbBossGroup = new JComboBox<>(new String[]{"Tat ca", "Boss Cot Truyen / Pho Ban", "Ngu Hanh Son (Trung Thu)", "Halloween", "Nhan Gioi", "Than Thu", "Noel", "Hung Vuong", "Tet", "Than / Training", "Doraemon / Cau Than Thu", "Dac Biet / Test", "Khac"});
        fBossSearch = tf(18);
        fBossSearch.setToolTipText("Go ten boss, field, bossID roi Enter");
        lsBossFieldModel = new DefaultListModel<>();
        lsBossField = new JList<>(lsBossFieldModel);
        lsBossField.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel ltop = new JPanel(new GridLayout(3, 1, 4, 4));
        ltop.setBackground(CARD);
        ltop.add(new JLabel("Buoc 1 - Nhom boss:"));
        ltop.add(cbBossGroup);
        JPanel srow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        srow.setBackground(CARD);
        srow.add(new JLabel("Buoc 2 - Tim:"));
        srow.add(fBossSearch);
        ltop.add(srow);
        left.add(ltop, BorderLayout.NORTH);
        left.add(new JScrollPane(lsBossField), BorderLayout.CENTER);
        JButton bReloadCat = btn("Tai danh sach boss");
        left.add(bReloadCat, BorderLayout.SOUTH);
        // Phai: chi tiet - dung doc + boc chu de khong che nhau
        JPanel right = new JPanel(new BorderLayout(6, 6));
        right.setBackground(CARD);
        lbBossTitle = new JLabel("Chua chon boss");
        lbBossTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        right.add(lbBossTitle, BorderLayout.NORTH);
        JPanel formWrap = new JPanel();
        formWrap.setBackground(CARD);
        formWrap.setLayout(new BoxLayout(formWrap, BoxLayout.Y_AXIS));
        lbBossGoc = roArea(3); lbBossGoc.setBorder(BorderFactory.createTitledBorder("Thong tin goc (chi doc)"));
        lbBossGoiY = roArea(2); lbBossGoiY.setBorder(BorderFactory.createTitledBorder("Goi y dame ao"));
        lbBossReward = roArea(3); lbBossReward.setBorder(BorderFactory.createTitledBorder("Do roi goc (trong code)"));
        formWrap.add(lbBossGoc); formWrap.add(Box.createVerticalStrut(6));
        formWrap.add(lbBossGoiY); formWrap.add(Box.createVerticalStrut(6));
        formWrap.add(lbBossReward); formWrap.add(Box.createVerticalStrut(10));
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(CARD);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        fBossDame = tf(15); fBossHp = tf(15); fBossRest = tf(15); fBossMaps = tf(15);
        fBossDrop = tf(15); fBossQty = tf(15); fBossRate = tf(15);
        fBossNote = new JTextArea(2, 15); fBossNote.setLineWrap(true); fBossNote.setWrapStyleWord(true);
        addRow(grid, gc, 0, "Dame moi (-1 = giu goc):", fBossDame);
        addRow(grid, gc, 1, "HP moi (vd 1 ty,2 ty; trong = giu goc):", fBossHp);
        addRow(grid, gc, 2, "Nghi Respawn giay (-1 = giu goc):", fBossRest);
        addRow(grid, gc, 3, "Map join moi (vd 0,1,2; trong = giu goc):", fBossMaps);
        addRow(grid, gc, 4, "Rot them item temp (-1 = khong rot):", fBossDrop);
        addRow(grid, gc, 5, "So luong rot them:", fBossQty);
        addRow(grid, gc, 6, "Ti le rot them % (0-100):", fBossRate);
        addRow(grid, gc, 7, "Ghi chu:", new JScrollPane(fBossNote));
        formWrap.add(grid);
        JScrollPane scRight = new JScrollPane(formWrap);
        scRight.setBorder(null);
        scRight.getVerticalScrollBar().setUnitIncrement(16);
        right.add(scRight, BorderLayout.CENTER);
        JPanel acts = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acts.setBackground(CARD);
        JButton bSave = btn("Luu (Buoc 4)");
        bSave.setBackground(new Color(40, 140, 70)); bSave.setForeground(Color.WHITE);
        JButton bReset = btn("Reset ve goc");
        JButton bSummonF = btn("Goi boss nay (theo BossID)");
        acts.add(bSave); acts.add(bReset); acts.add(bSummonF);
        acts.add(new JLabel("Luu xong: goi boss moi de thay doi co tac dung."));
        right.add(acts, BorderLayout.SOUTH);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setResizeWeight(0.0);
        split.setDividerLocation(360);
        left.setMinimumSize(new Dimension(300, 400));
        left.setPreferredSize(new Dimension(360, 500));
        root.add(split, BorderLayout.CENTER);
        // events
        Runnable reload = () -> {
            List<Map<String, Object>> list = PanelService.listBossCatalog();
            bossCatCache = list;
            SwingUtilities.invokeLater(this::filterBossList);
        };
        bReloadCat.addActionListener(e -> bg(reload));
        cbBossGroup.addActionListener(e -> filterBossList());
        fBossSearch.addActionListener(e -> filterBossList());
        lsBossField.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            String sel = lsBossField.getSelectedValue();
            if (sel == null) return;
            String field = sel.split(" \\| ")[0].trim();
            loadBossEdit(field);
        });
        bSave.addActionListener(e -> {
            if (curBossField == null) { err("Chon 1 boss ben trai truoc"); return; }
            bg(() -> {
                double dame; try { dame = Double.parseDouble(fBossDame.getText().trim()); } catch (Exception ex) { dame = -1; }
                int rest; try { rest = Integer.parseInt(fBossRest.getText().trim()); } catch (Exception ex) { rest = -1; }
                int drop; try { drop = Integer.parseInt(fBossDrop.getText().trim()); } catch (Exception ex) { drop = -1; }
                int qty; try { qty = Integer.parseInt(fBossQty.getText().trim()); } catch (Exception ex) { qty = 1; }
                int rate; try { rate = Integer.parseInt(fBossRate.getText().trim()); } catch (Exception ex) { rate = 100; }
                String r = PanelService.saveBossEdit(curBossField, dame, fBossHp.getText().trim(), rest, fBossMaps.getText().trim(), drop, qty, rate, fBossNote.getText());
                SwingUtilities.invokeLater(() -> info(r + "\nGoi boss moi de ap dung."));
            });
        });
        bReset.addActionListener(e -> {
            if (curBossField == null) return;
            bg(() -> { String r = PanelService.resetBossEdit(curBossField); SwingUtilities.invokeLater(() -> { info(r); loadBossEdit(curBossField); }); });
        });
        bSummonF.addActionListener(e -> {
            if (curBossField == null) return;
            bg(() -> {
                Map<String, Object> d = PanelService.bossEditData(curBossField);
                String r;
                try { r = PanelService.summonBoss(Integer.parseInt(String.valueOf(d.get("bossId")))); }
                catch (Exception ex) { r = "Boss nay khong co ID co dinh (random/clone). Dung tab Boss Live de goi theo ID."; }
                final String rr = r;
                SwingUtilities.invokeLater(() -> { info(rr); loadBosses(); });
            });
        });
        bg(reload);
        return root;
    }

    private void filterBossList() {
        if (lsBossFieldModel == null) return;
        String g = cbBossGroup == null ? "Tat ca" : String.valueOf(cbBossGroup.getSelectedItem());
        String q = fBossSearch == null ? "" : fBossSearch.getText().trim().toLowerCase();
        lsBossFieldModel.clear();
        for (Map<String, Object> m : bossCatCache) {
            String field = String.valueOf(m.get("field"));
            String grp = panel.tuning.BossResolve.groupOf(field);
            if (!"Tat ca".equals(g) && !g.equals(grp)) continue;
            String row = field + " | " + m.get("name");
            String full = row + " | ID:" + m.get("bossId") + " | dame:" + m.get("dame");
            if (!q.isEmpty() && !full.toLowerCase().contains(q)) continue;
            lsBossFieldModel.addElement(row);
        }
    }

    private void loadBossEdit(String field) {
        bg(() -> {
            Map<String, Object> d = PanelService.bossEditData(field);
            curBossField = field;
            SwingUtilities.invokeLater(() -> {
                lbBossTitle.setText(field + " - " + d.get("name") + " (Nhom: " + d.get("nhom") + " | BossID: " + d.get("bossId") + ")");
                lbBossGoc.setText("Dame " + d.get("dameGoc") + " | HP " + d.get("hpGoc") + "\nMap " + d.get("mapsName") + " | Rest " + d.get("restGoc") + "s");
                lbBossGoc.setCaretPosition(0);
                lbBossGoiY.setText(String.valueOf(d.get("goiY")));
                lbBossGoiY.setCaretPosition(0);
                lbBossReward.setText(String.valueOf(d.get("reward")));
                lbBossReward.setCaretPosition(0);
                fBossDame.setText(String.valueOf(d.get("dameOv")));
                fBossHp.setText(String.valueOf(d.get("hpOv") == null ? "" : d.get("hpOv")));
                fBossRest.setText(String.valueOf(d.get("restOv")));
                fBossMaps.setText(String.valueOf(d.get("mapsOv") == null ? "" : d.get("mapsOv")));
                fBossDrop.setText(String.valueOf(d.get("drop")));
                fBossQty.setText(String.valueOf(d.get("dropQty")));
                fBossRate.setText(String.valueOf(d.get("dropRate")));
                fBossNote.setText(String.valueOf(d.get("note") == null ? "" : d.get("note")));
            });
        });
    }

    private Boss selBoss() {
        int r = bossTable.getSelectedRow();
        if (r < 0 || r >= bossCache.size()) { err("Chon 1 boss"); return null; }
        return bossCache.get(r);
    }

    private void loadBosses() {
        List<Boss> list = PanelService.allBosses();
        bossCache = list;
        SwingUtilities.invokeLater(() -> {
            bossModel.setRowCount(0);
            for (int i = 0; i < list.size(); i++) bossModel.addRow(new Object[]{i, PanelService.bossInfo(list.get(i))});
        });
    }

    // ================= DOT 4: TAB 3 - TIM BOSS SPAWN O DAU =================
    private JPanel bossSpawnTab() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBackground(CARD);
        root.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JLabel guide = new JLabel("<html><b>Cach dung:</b> Go ten boss / ten map / BossID / mapID roi Enter. De trong + Bam <b>Tim</b> = xem TAT CA boss dang quan ly (online + dang nghi). Cot <b>Map/Khu</b> cho biet boss dang o dau.</html>");
        root.add(guide, BorderLayout.NORTH);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        fSpawnSearch = tf(28);
        fSpawnSearch.setToolTipText("vd: broly / fide / 5 / map 5 / -372");
        JButton bFind = btn("Tim");
        bFind.setBackground(new Color(40, 120, 200)); bFind.setForeground(Color.WHITE);
        JButton bAll = btn("Xem tat ca");
        lbSpawnCount = new JLabel("...");
        top.add(new JLabel("Tim boss/map:"));
        top.add(fSpawnSearch); top.add(bFind); top.add(bAll); top.add(lbSpawnCount);
        root.add(top, BorderLayout.CENTER);
        // de BorderLayout khong che nhau: gom top+guide vao north
        JPanel north = new JPanel(new BorderLayout(4, 4));
        north.setBackground(CARD);
        north.add(guide, BorderLayout.NORTH);
        north.add(top, BorderLayout.CENTER);
        root.add(north, BorderLayout.NORTH);
        spawnModel = new DefaultTableModel(new String[]{"Ten boss", "Nhom", "BossID", "Map / Khu", "Nguoi", "Trang thai", "HP"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        spawnTable = new JTable(spawnModel);
        spawnTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        spawnTable.getColumnModel().getColumn(3).setPreferredWidth(260);
        spawnTable.setRowHeight(22);
        root.add(new JScrollPane(spawnTable), BorderLayout.CENTER);
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bot.setBackground(CARD);
        JButton bGo = btn("Duoi boss dang chon khoi map");
        bGo.addActionListener(e -> {
            int r = spawnTable.getSelectedRow();
            if (r < 0 || r >= spawnCache.size()) { err("Chon 1 dong trong bang"); return; }
            final int bid = Integer.parseInt(String.valueOf(spawnCache.get(r).get("bossId")));
            bg(() -> {
                String rs = "Khong thay boss online";
                for (Boss b : PanelService.allBosses()) {
                    if (b != null && b.id == bid && b.zone != null) { rs = PanelService.removeBossFromMap(b); break; }
                }
                final String rr = rs;
                SwingUtilities.invokeLater(() -> { info(rr); loadSpawn(); });
            });
        });
        bot.add(bGo);
        bot.add(new JLabel("(Muon goi boss: sang tab 2 nhap BossID)"));
        root.add(bot, BorderLayout.SOUTH);
        Runnable doFind = this::loadSpawn;
        bFind.addActionListener(e -> bg(doFind));
        bAll.addActionListener(e -> { fSpawnSearch.setText(""); bg(doFind); });
        fSpawnSearch.addActionListener(e -> bg(doFind));
        bg(doFind);
        return root;
    }

    private void loadSpawn() {
        String q = fSpawnSearch == null ? "" : fSpawnSearch.getText();
        List<Map<String, Object>> list = PanelService.searchBossSpawn(q);
        spawnCache = list;
        SwingUtilities.invokeLater(() -> {
            spawnModel.setRowCount(0);
            for (Map<String, Object> m : list) {
                long hp = 0, hpMax = 0;
                try { hp = Long.parseLong(String.valueOf(m.get("hp"))); } catch (Exception ex) {}
                try { hpMax = Long.parseLong(String.valueOf(m.get("hpMax"))); } catch (Exception ex) {}
                String hpStr = hpMax > 0 ? (hp + "/" + hpMax) : "-";
                spawnModel.addRow(new Object[]{m.get("name"), m.get("nhom"), m.get("bossId"), m.get("map"), m.get("nguoiTrongKhu"), m.get("status"), hpStr});
            }
            lbSpawnCount.setText("Tim thay " + list.size() + " boss");
        });
    }

    // ================= DOT 4: TAB 4 - BOSS THE GIOI (bat tu, top dame) =================
    private JPanel worldBossTab() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(CARD);
        root.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JLabel guide = new JLabel("<html><b>Boss The Gioi V2:</b> goi theo gio hang ngay, song LIFE phut, xoay khu moi ZONE_SEC giay. Qua JSON co option (uu tien JSON, legacy giu lai tuong thich). <b>Phat thuong</b> = trao qua top dame roi Giet boss (khong hoi sinh); <b>Kill Boss</b> = giet ngay khong trao thuong. Top dame tich luy toi 1.00E305.</html>");
        root.add(guide, BorderLayout.NORTH);
        // Trai: cau hinh V2
        JPanel left = new JPanel(new GridBagLayout());
        left.setBackground(CARD);
        left.setBorder(BorderFactory.createTitledBorder("Cau hinh V2 (luu file worldboss.properties)"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        cbWBOn = new JCheckBox("Bat Boss The Gioi (ON)");
        fWBMap = tf(8); fWBDur = tf(8); fWBHp = tf(15); fWBDame = tf(15);
        fWBTop1 = tf(16); fWBTop23 = tf(16); fWBTop410 = tf(16); fWBConsol = tf(16); fWBMinDame = tf(12);
        fWBSpawn = tf(20); fWBLife = tf(8); fWBZoneSec = tf(8); fWBStartZone = tf(8);
        fWBTop1J = tf(22); fWBTop23J = tf(22); fWBTop410J = tf(22); fWBConsolJ = tf(22);
        cbWBMapList = new JComboBox<>();
        lbWBMapName = new JLabel("...");
        fWBMap.setToolTipText("ID map boss dung (dong bo 2 chieu voi combo ben trai).");
        fWBSpawn.setToolTipText("vd 12:00,19:00 (cach nhau dau phay)");
        fWBLife.setToolTipText("phut, vd 60");
        fWBZoneSec.setToolTipText("giay, vd 15 (toi thieu 5)");
        fWBStartZone.setToolTipText("-1 = tu dong, >=0 = khu bat dau");
        fWBTop1.setToolTipText("legacy vd 457:1"); fWBTop23.setToolTipText("legacy vd 457:1"); fWBTop410.setToolTipText("legacy vd 457:1"); fWBConsol.setToolTipText("legacy vd 456:5");
        fWBTop1J.setToolTipText("JSON array [{temp_id,quantity,options:[{id,param}]}]");
        fWBTop23J.setToolTipText("JSON array [{temp_id,quantity,options:[{id,param}]}]");
        fWBTop410J.setToolTipText("JSON array [{temp_id,quantity,options:[{id,param}]}]");
        fWBConsolJ.setToolTipText("JSON array [{temp_id,quantity,options:[{id,param}]}]");
        addRow(left, gc, 0, "Trang thai:", cbWBOn);
        JPanel pMap = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pMap.setBackground(CARD);
        pMap.add(cbWBMapList); pMap.add(fWBMap); pMap.add(lbWBMapName);
        addRow(left, gc, 1, "Map (combo + id):", pMap);
        addRow(left, gc, 2, "Gio goi hang ngay:", fWBSpawn);
        JPanel pLife = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pLife.setBackground(CARD);
        pLife.add(new JLabel("Song:")); pLife.add(fWBLife); pLife.add(new JLabel("phut"));
        pLife.add(new JLabel("Doi khu:")); pLife.add(fWBZoneSec); pLife.add(new JLabel("giay"));
        pLife.add(new JLabel("Khu bd:")); pLife.add(fWBStartZone);
        addRow(left, gc, 3, "Vong doi:", pLife);
        addRow(left, gc, 4, "Moi dot cu (phut):", fWBDur);
        JPanel pHp = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pHp.setBackground(CARD);
        pHp.add(fWBHp);
        JButton bHpCur = btn("Hien tai");
        JButton bHp900k = btn("900k ty");
        JButton bHpMax = btn("Max 9E15");
        bHp900k.setToolTipText("9.0E14 ~ 900.000 ty - mac dinh");
        bHpMax.setToolTipText("9.0E15 ~ 9.000.000 ty - cao nhat khuyen nghi, client cu van hien 2.1 ty");
        pHp.add(bHpCur); pHp.add(bHp900k); pHp.add(bHpMax);
        left.putClientProperty("hpCur", bHpCur);
        left.putClientProperty("hp900k", bHp900k);
        left.putClientProperty("hpMax", bHpMax);
        addRow(left, gc, 5, "HP boss:", pHp);
        addRow(left, gc, 6, "Dame boss:", fWBDame);
        JButton bPick1 = btn("Chon qua Top1");
        JButton bPick23 = btn("Chon Top23");
        JButton bPick410 = btn("Chon Top410");
        JButton bPickC = btn("Chon An ui");
        JPanel pT1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pT1.setBackground(CARD); pT1.add(fWBTop1); pT1.add(bPick1);
        JPanel pT23 = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pT23.setBackground(CARD); pT23.add(fWBTop23); pT23.add(bPick23);
        JPanel pT410 = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pT410.setBackground(CARD); pT410.add(fWBTop410); pT410.add(bPick410);
        JPanel pC = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pC.setBackground(CARD); pC.add(fWBConsol); pC.add(bPickC);
        addRow(left, gc, 7, "Qua Top 1 (cu):", pT1);
        addRow(left, gc, 8, "Qua Top 2-3 (cu):", pT23);
        addRow(left, gc, 9, "Qua Top 4-10 (cu):", pT410);
        addRow(left, gc, 10, "Qua an ui (cu):", pC);
        addRow(left, gc, 11, "JSON Top1:", fWBTop1J);
        addRow(left, gc, 12, "JSON Top23:", fWBTop23J);
        addRow(left, gc, 13, "JSON Top410:", fWBTop410J);
        addRow(left, gc, 14, "JSON An ui:", fWBConsolJ);
        addRow(left, gc, 15, "Dame toi thieu:", fWBMinDame);
        // luu tam nut pick de gan event ben duoi
        left.putClientProperty("pick1", bPick1);
        left.putClientProperty("pick23", bPick23);
        left.putClientProperty("pick410", bPick410);
        left.putClientProperty("pickC", bPickC);
        JPanel acts = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acts.setBackground(CARD);
        JButton bSaveWB = btn("Luu cau hinh");
        bSaveWB.setBackground(new Color(40, 140, 70)); bSaveWB.setForeground(Color.WHITE);
        JButton bSummonWB = btn("Goi Boss The Gioi");
        bSummonWB.setBackground(new Color(40, 120, 200)); bSummonWB.setForeground(Color.WHITE);
        JButton bRewardWB = btn("Phat thuong + Giet boss");
        bRewardWB.setBackground(new Color(190, 120, 20)); bRewardWB.setForeground(Color.WHITE);
        JButton bKillWB = btn("Kill Boss");
        bKillWB.setBackground(new Color(170, 40, 40)); bKillWB.setForeground(Color.WHITE);
        bKillWB.setToolTipText("Giet Boss The Gioi NGAY, khong trao thuong, boss khong tu hoi sinh");
        JButton bResetWB = btn("Reset Top");
        JButton bReloadWB = btn("Tai lai");
        acts.add(bSaveWB); acts.add(bSummonWB); acts.add(bRewardWB); acts.add(bKillWB); acts.add(bResetWB); acts.add(bReloadWB);
        // Phai: live status V2
        JPanel right = new JPanel();
        right.setBackground(CARD);
        right.setBorder(BorderFactory.createTitledBorder("Live: vi tri + Top dame"));
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        lbWBLive = new JLabel("..."); lbWBLeft = new JLabel("..."); lbWBLuot = new JLabel("...");
        lbWBSeason = new JLabel("...");
        lbWBLive.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbWBLeft.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbWBLeft.setForeground(new Color(180, 60, 60));
        right.add(new JLabel("Mua giai:")); right.add(lbWBSeason); right.add(Box.createVerticalStrut(4));
        right.add(new JLabel("Dang o:")); right.add(lbWBLive); right.add(Box.createVerticalStrut(4));
        right.add(new JLabel("Con lai:")); right.add(lbWBLeft); right.add(Box.createVerticalStrut(4));
        right.add(new JLabel("Luot danh:")); right.add(lbWBLuot); right.add(Box.createVerticalStrut(6));
        right.add(new JLabel("Top dame (Top 10):"));
        taWBTop = roArea(14);
        taWBTop.setBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)));
        JScrollPane scTop = new JScrollPane(taWBTop);
        scTop.setPreferredSize(new Dimension(340, 300));
        right.add(scTop);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(left), right);
        split.setResizeWeight(0.55);
        split.setDividerLocation(560);
        JPanel center = new JPanel(new BorderLayout(6, 6));
        center.setBackground(CARD);
        center.add(split, BorderLayout.CENTER);
        center.add(acts, BorderLayout.SOUTH);
        root.add(center, BorderLayout.CENTER);
        // events
        Runnable reload = this::loadWB;
        bReloadWB.addActionListener(e -> bg(reload));
        bSaveWB.setText("Luu cau hinh V2");
        bSaveWB.addActionListener(e -> bg(() -> {
            boolean on = cbWBOn.isSelected();
            int map = 5, dur = 60, life = 60, zsec = 15, zstart = -1;
            double hp, dame, minD;
            try {
                int sel = cbWBMapList.getSelectedIndex();
                if (sel >= 0 && sel < wbMapIds.size()) map = wbMapIds.get(sel);
                else map = Integer.parseInt(fWBMap.getText().trim());
            } catch (Exception ex) { try { map = Integer.parseInt(fWBMap.getText().trim()); } catch (Exception ex2) { err("Map ID phai la so"); return; } }
            try { dur = Integer.parseInt(fWBDur.getText().trim()); } catch (Exception ex) { dur = 60; }
            try { hp = Double.parseDouble(fWBHp.getText().trim().replace(",", "")); } catch (Exception ex) { hp = -1; }
            try { dame = Double.parseDouble(fWBDame.getText().trim().replace(",", "")); } catch (Exception ex) { dame = -1; }
            try { minD = Double.parseDouble(fWBMinDame.getText().trim().replace(",", "")); } catch (Exception ex) { minD = -1; }
            try { life = Integer.parseInt(fWBLife.getText().trim()); } catch (Exception ex) { life = 60; }
            try { zsec = Integer.parseInt(fWBZoneSec.getText().trim()); } catch (Exception ex) { zsec = 15; }
            try { zstart = Integer.parseInt(fWBStartZone.getText().trim()); } catch (Exception ex) { zstart = -1; }
            if (zsec < 5) zsec = 5;
            if (life < 1) life = 60;
            String r = PanelService.saveWorldBossV2(on, map, dur, hp, dame, fWBTop1.getText(), fWBTop23.getText(), fWBTop410.getText(), fWBConsol.getText(), minD, fWBSpawn.getText(), life, zsec, zstart, fWBTop1J.getText(), fWBTop23J.getText(), fWBTop410J.getText(), fWBConsolJ.getText());
            SwingUtilities.invokeLater(() -> { info(r); loadWB(); });
        }));
        cbWBMapList.addActionListener(e -> {
            try {
                int sel = cbWBMapList.getSelectedIndex();
                if (sel >= 0 && sel < wbMapIds.size()) {
                    fWBMap.setText(String.valueOf(wbMapIds.get(sel)));
                    lbWBMapName.setText(String.valueOf(cbWBMapList.getSelectedItem()));
                }
            } catch (Exception ex) {}
        });
        fWBMap.addActionListener(e -> {
            try {
                int id = Integer.parseInt(fWBMap.getText().trim());
                for (int i = 0; i < wbMapIds.size(); i++) {
                    if (wbMapIds.get(i) == id) { cbWBMapList.setSelectedIndex(i); break; }
                }
                lbWBMapName.setText(PanelService.mapNamePublic(id));
            } catch (Exception ex) {}
        });
        JButton bP1 = (JButton) left.getClientProperty("pick1");
        JButton bP23 = (JButton) left.getClientProperty("pick23");
        JButton bP410 = (JButton) left.getClientProperty("pick410");
        JButton bPC = (JButton) left.getClientProperty("pickC");
        if (bP1 != null) bP1.addActionListener(ev -> { String s = openWBRewardPicker(fWBTop1J.getText()); if (s != null) fWBTop1J.setText(s); });
        if (bP23 != null) bP23.addActionListener(ev -> { String s = openWBRewardPicker(fWBTop23J.getText()); if (s != null) fWBTop23J.setText(s); });
        if (bP410 != null) bP410.addActionListener(ev -> { String s = openWBRewardPicker(fWBTop410J.getText()); if (s != null) fWBTop410J.setText(s); });
        if (bPC != null) bPC.addActionListener(ev -> { String s = openWBRewardPicker(fWBConsolJ.getText()); if (s != null) fWBConsolJ.setText(s); });
        bSummonWB.addActionListener(e -> bg(() -> {
            String r = PanelService.summonWorldBoss();
            SwingUtilities.invokeLater(() -> { info(r); loadWB(); loadSpawn(); });
        }));
        bRewardWB.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this, "Phat thuong dot nay NGAY va Giet Boss The Gioi?\nBoss se bi giet, khong xuat hien tren ban do va KHONG tu hoi sinh (chi goi lai bang 'Goi Boss' hoac lich SPAWN_TIMES).", "Xac nhan", JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION) return;
            bg(() -> { String r = PanelService.rewardWorldBossNow(); SwingUtilities.invokeLater(() -> { info(r); loadWB(); loadSpawn(); }); });
        });
        bKillWB.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this, "Giet Boss The Gioi NGAY (khong trao thuong)?\nBoss se khong tu hoi sinh. Neu muon trao qua top dame, bam 'Phat thuong + Giet boss' thay vi vao day.", "Xac nhan", JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION) return;
            bg(() -> { String r = PanelService.killWorldBossNow(); SwingUtilities.invokeLater(() -> { info(r); loadWB(); loadSpawn(); }); });
        });
        bResetWB.addActionListener(e -> bg(() -> {
            String r = PanelService.resetWorldBossTop();
            SwingUtilities.invokeLater(() -> { info(r); loadWB(); });
        }));
        try {
            JButton bHpCur2 = (JButton) left.getClientProperty("hpCur");
            JButton bHp900k2 = (JButton) left.getClientProperty("hp900k");
            JButton bHpMax2 = (JButton) left.getClientProperty("hpMax");
            if (bHp900k2 != null) bHp900k2.addActionListener(ev -> { try { fWBHp.setText("9.0E14"); } catch (Exception ex) {} });
            if (bHpMax2 != null) bHpMax2.addActionListener(ev -> { try { fWBHp.setText("9.0E15"); } catch (Exception ex) {} });
            if (bHpCur2 != null) bHpCur2.addActionListener(ev -> bg(() -> {
                try {
                    Map<String, Object> st = PanelService.worldBossState();
                    Object hp = st.get("hp");
                    Object live = st.get("live");
                    final String hps = String.valueOf(hp);
                    final String lvs = String.valueOf(live);
                    SwingUtilities.invokeLater(() -> { try { fWBHp.setText(hps); } catch (Exception ex) {} info("HP cau hinh: " + hps + "\nLive: " + lvs); });
                } catch (Exception ex) { SwingUtilities.invokeLater(() -> err("Loi: " + ex.getMessage())); }
            }));
        } catch (Exception ex) {}
        bg(reload);
        return root;
    }

    private void loadWB() {
        bg(() -> {
            Map<String, Object> st = PanelService.worldBossState();
            java.util.List<Map<String, Object>> maps = null;
            try { maps = PanelService.listAllMaps(); } catch (Exception e) {}
            final java.util.List<Map<String, Object>> fMaps = maps;
            SwingUtilities.invokeLater(() -> {
                try { cbWBOn.setSelected(Boolean.parseBoolean(String.valueOf(st.get("on")))); } catch (Exception e) {}
                try { fWBMap.setText(String.valueOf(st.get("mapId"))); } catch (Exception e) {}
                try { fWBDur.setText(String.valueOf(st.get("durationMin"))); } catch (Exception e) {}
                try { fWBHp.setText(String.valueOf(st.get("hp"))); } catch (Exception e) {}
                try { fWBDame.setText(String.valueOf(st.get("dame"))); } catch (Exception e) {}
                try { fWBTop1.setText(String.valueOf(st.get("top1"))); } catch (Exception e) {}
                try { fWBTop23.setText(String.valueOf(st.get("top23"))); } catch (Exception e) {}
                try { fWBTop410.setText(String.valueOf(st.get("top410"))); } catch (Exception e) {}
                try { fWBConsol.setText(String.valueOf(st.get("consol"))); } catch (Exception e) {}
                try { fWBMinDame.setText(String.valueOf(st.get("minDame"))); } catch (Exception e) {}
                try { fWBSpawn.setText(String.valueOf(st.get("spawnTimes"))); } catch (Exception e) {}
                try { fWBLife.setText(String.valueOf(st.get("lifeMin"))); } catch (Exception e) {}
                try { fWBZoneSec.setText(String.valueOf(st.get("zoneSec"))); } catch (Exception e) {}
                try { fWBStartZone.setText(String.valueOf(st.get("startZone"))); } catch (Exception e) {}
                try { fWBTop1J.setText(String.valueOf(st.get("top1Json"))); } catch (Exception e) {}
                try { fWBTop23J.setText(String.valueOf(st.get("top23Json"))); } catch (Exception e) {}
                try { fWBTop410J.setText(String.valueOf(st.get("top410Json"))); } catch (Exception e) {}
                try { fWBConsolJ.setText(String.valueOf(st.get("consolJson"))); } catch (Exception e) {}
                try { lbWBMapName.setText(String.valueOf(st.get("mapName"))); } catch (Exception e) {}
                try { lbWBSeason.setText(String.valueOf(st.get("season"))); } catch (Exception e) {}
                try {
                    if (fMaps != null && !fMaps.isEmpty() && cbWBMapList.getItemCount() == 0) {
                        wbMapIds.clear();
                        for (Map<String, Object> mm : fMaps) {
                            try {
                                int id = Integer.parseInt(String.valueOf(mm.get("id")));
                                String nm = String.valueOf(mm.get("name"));
                                wbMapIds.add(id);
                                cbWBMapList.addItem(id + " - " + nm);
                            } catch (Exception ex) {}
                        }
                    }
                } catch (Exception e) {}
                try {
                    int cur = Integer.parseInt(fWBMap.getText().trim());
                    for (int i = 0; i < wbMapIds.size(); i++) {
                        if (wbMapIds.get(i) == cur) { cbWBMapList.setSelectedIndex(i); break; }
                    }
                } catch (Exception e) {}
                try { lbWBLive.setText(String.valueOf(st.get("live")) + " (" + st.get("mapName") + ")"); } catch (Exception e) {}
                try { lbWBLeft.setText(String.valueOf(st.get("conLai"))); } catch (Exception e) {}
                try { lbWBLuot.setText(String.valueOf(st.get("luotDanh")) + " nguoi da danh"); } catch (Exception e) {}
                try { taWBTop.setText(String.valueOf(st.get("top"))); taWBTop.setCaretPosition(0); } catch (Exception e) {}
            });
        });
    }

    private String openWBRewardPicker(String curJson) {
        try {
            java.util.List<GiftItemModel> draft = new java.util.ArrayList<>();
            try {
                java.util.List<GiftItemModel> p = boss.WorldBossTracker.parseGiftJson(curJson);
                if (p != null) for (GiftItemModel m : p) if (m != null) draft.add(m.copy());
            } catch (Exception e) {}
            try { PanelService.enrichGiftNames(draft); } catch (Exception e) {}
            JDialog d = new JDialog(this, "Chon qua Boss The Gioi (SL go truc tiep, Ten/Option nhap doi)", true);
            d.setSize(760, 500);
            d.setLocationRelativeTo(this);
            d.setLayout(new BorderLayout());
            final boolean[] guard = new boolean[]{false};
            DefaultTableModel om = new DefaultTableModel(new String[]{"TempID", "Ten qua (nhap doi de chon)", "SL (go so)", "Option (nhap doi de sua)"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return c == 2; }
                @Override public void setValueAt(Object v, int r, int c) {
                    if (guard[0]) { super.setValueAt(v, r, c); return; }
                    try {
                        if (r < 0 || r >= draft.size()) return;
                        if (c != 2) { super.setValueAt(v, r, c); return; }
                        int q = Integer.parseInt(String.valueOf(v).trim().replaceAll("[^0-9-]", ""));
                        if (q <= 0) { err("So luong > 0"); return; }
                        draft.get(r).quantity = q;
                        guard[0] = true;
                        try {
                            super.setValueAt(String.valueOf(q), r, c);
                            super.setValueAt(PanelService.giftItemSummaryVN(draft.get(r)), r, 3);
                        } finally { guard[0] = false; }
                    } catch (Exception ex) { guard[0] = false; err("Loi: " + ex.getMessage()); }
                }
            };
            JTable tb = new JTable(om);
            tb.setRowHeight(24);
            tb.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            Runnable refresh = new Runnable() {
                @Override public void run() {
                    try {
                        guard[0] = true;
                        try {
                            om.setRowCount(0);
                            for (GiftItemModel m : draft) {
                                String nm = m.itemName;
                                try {
                                    String sp = GiftItemModel.specialName(m.tempId);
                                    if (sp != null) nm = sp;
                                    else if (nm == null || nm.isEmpty()) {
                                        models.Template.ItemTemplate t = PanelService.getTemplateById(m.tempId);
                                        if (t != null) { m.itemName = t.name; nm = t.name; }
                                    }
                                } catch (Exception ex) {}
                                if (nm == null || nm.isEmpty()) nm = "#" + m.tempId;
                                om.addRow(new Object[]{m.tempId, nm, String.valueOf(m.quantity), PanelService.giftItemSummaryVN(m)});
                            }
                        } finally { guard[0] = false; }
                    } catch (Exception ex) { guard[0] = false; }
                }
            };
            refresh.run();
            tb.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (e.getClickCount() != 2) return;
                    int r = tb.getSelectedRow();
                    int c = tb.getSelectedColumn();
                    if (r < 0 || r >= draft.size()) return;
                    if (c == 1) {
                        Integer pick = openTemplatePicker();
                        if (pick == null) {
                            String[] opts = {"Vang (-1)", "Ngoc (-2)", "Ngoc khoa (-3)"};
                            int ch = JOptionPane.showOptionDialog(ControlPanel.this, "Khong chon vat pham? Chon tien te nhanh:", "Tien te", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opts, opts[0]);
                            if (ch == 0) pick = -1; else if (ch == 1) pick = -2; else if (ch == 2) pick = -3;
                            else return;
                        }
                        GiftItemModel m = draft.get(r);
                        m.tempId = pick;
                        String sp = GiftItemModel.specialName(pick);
                        if (sp != null) m.itemName = sp;
                        else { models.Template.ItemTemplate t = PanelService.getTemplateById(pick); if (t != null) m.itemName = t.name; else m.itemName = "#" + pick; }
                        refresh.run();
                    } else if (c == 3) {
                        openWBOptionEditor(draft.get(r), refresh);
                        refresh.run();
                    }
                }
            });
            d.add(new JScrollPane(tb), BorderLayout.CENTER);
            JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton bAdd = new JButton("Them mon");
            JButton bDel = new JButton("Xoa dong");
            JButton bCoin = new JButton("Them Vang/Ngoc");
            top.add(new JLabel("SL go truc tiep o bang; Ten + Option nhap doi de chon/sua."));
            top.add(bAdd); top.add(bCoin); top.add(bDel);
            d.add(top, BorderLayout.NORTH);
            final String[] out = new String[1];
            final boolean[] ok = new boolean[]{false};
            JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton bOk = new JButton("Luu JSON");
            bOk.setBackground(new Color(40, 140, 70)); bOk.setForeground(Color.WHITE);
            JButton bCancel = new JButton("Huy");
            bot.add(bOk); bot.add(bCancel);
            d.add(bot, BorderLayout.SOUTH);
            bAdd.addActionListener(new java.awt.event.ActionListener() {
                @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        Integer pick = openTemplatePicker();
                        int tid = 14;
                        String nm = "";
                        if (pick != null) {
                            tid = pick;
                            models.Template.ItemTemplate t = PanelService.getTemplateById(pick);
                            if (t != null) nm = t.name;
                        } else {
                            String s = JOptionPane.showInputDialog(ControlPanel.this, "Nhap temp_id (vd 14=Ngoc rong, -1=Vang, -2=Ngoc, -3=Ngoc khoa):", "14");
                            if (s == null) return;
                            try { tid = Integer.parseInt(s.trim()); } catch (Exception ex) { err("temp_id so"); return; }
                            String sp = GiftItemModel.specialName(tid);
                            if (sp != null) nm = sp;
                            else { models.Template.ItemTemplate t = PanelService.getTemplateById(tid); if (t != null) nm = t.name; }
                        }
                        GiftItemModel m = new GiftItemModel();
                        m.tempId = tid; m.itemName = nm; m.quantity = 1;
                        draft.add(m);
                        refresh.run();
                    } catch (Exception ex) {}
                }
            });
            bCoin.addActionListener(new java.awt.event.ActionListener() {
                @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        String[] opts = {"Vang (-1)", "Ngoc (-2)", "Ngoc khoa (-3)"};
                        int ch = JOptionPane.showOptionDialog(ControlPanel.this, "Chon tien te:", "Tien te", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opts, opts[0]);
                        int tid = -1;
                        if (ch == 0) tid = -1; else if (ch == 1) tid = -2; else if (ch == 2) tid = -3; else return;
                        String s = JOptionPane.showInputDialog(ControlPanel.this, "Nhap so luong:", "1000000");
                        if (s == null) return;
                        int q = 1;
                        try { q = Integer.parseInt(s.trim().replaceAll("[^0-9-]", "")); } catch (Exception ex) { err("So luong so"); return; }
                        if (q <= 0) { err("So luong > 0"); return; }
                        GiftItemModel m = new GiftItemModel();
                        m.tempId = tid; m.itemName = GiftItemModel.specialName(tid); m.quantity = q;
                        draft.add(m);
                        refresh.run();
                    } catch (Exception ex) {}
                }
            });
            bDel.addActionListener(new java.awt.event.ActionListener() {
                @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        int r = tb.getSelectedRow();
                        if (r >= 0 && r < draft.size()) { draft.remove(r); refresh.run(); }
                        else err("Chon 1 dong");
                    } catch (Exception ex) {}
                }
            });
            bOk.addActionListener(new java.awt.event.ActionListener() {
                @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        try { if (tb.isEditing()) tb.getCellEditor().stopCellEditing(); } catch (Exception ex) {}
                        java.util.List<String> errs = PanelService.validateGiftItems(draft);
                        if (!errs.isEmpty()) {
                            StringBuilder sb = new StringBuilder("Chua luu, con loi:\n");
                            for (String s : errs) sb.append("- ").append(s).append("\n");
                            err(sb.toString());
                            return;
                        }
                        org.json.simple.JSONArray arr = new org.json.simple.JSONArray();
                        for (GiftItemModel m : draft) arr.add(m.toJson());
                        out[0] = arr.toJSONString();
                        ok[0] = true;
                    } catch (Exception ex) { err("Loi: " + ex.getMessage()); return; }
                    d.dispose();
                }
            });
            bCancel.addActionListener(new java.awt.event.ActionListener() {
                @Override public void actionPerformed(java.awt.event.ActionEvent e) { ok[0] = false; out[0] = null; d.dispose(); }
            });
            d.setVisible(true);
            if (ok[0]) return out[0];
            return null;
        } catch (Exception e) { return null; }
    }

    private void openWBOptionEditor(GiftItemModel m, Runnable refreshOuter) {
        try {
            if (m == null) return;
            JDialog d = new JDialog(this, "Option: " + (m.itemName == null || m.itemName.isEmpty() ? ("#" + m.tempId) : m.itemName), true);
            d.setSize(640, 460);
            d.setLocationRelativeTo(this);
            d.setLayout(new BorderLayout());
            DefaultTableModel om = new DefaultTableModel(new String[]{"Option (chon dropdown)", "Param (go so)", "Mo ta"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return c == 0 || c == 1; }
            };
            JTable ot = new JTable(om);
            ot.setRowHeight(24);
            final boolean[] og = new boolean[]{false};
            Runnable reload = () -> {
                og[0] = true;
                try {
                    om.setRowCount(0);
                    for (GiftItemModel.Opt o : m.options) om.addRow(new Object[]{o.id, String.valueOf(o.param), PanelService.optionDisplay(o.id)});
                } finally { og[0] = false; }
            };
            reload.run();
            JComboBox<String> cbOpt = new JComboBox<>();
            cbOpt.setPrototypeDisplayValue("#000 Sao pha le x99 | Ten dai........");
            cbOpt.setPreferredSize(new java.awt.Dimension(380, 25));
            cbOpt.setMaximumRowCount(20);
            java.util.List<Integer> ids = new java.util.ArrayList<>();
            try {
                if (optDictCache == null || optDictCache.isEmpty()) optDictCache = PanelService.listOptionDict();
                for (Map<String, Object> o : optDictCache) {
                    int id = ((Number) o.get("id")).intValue();
                    ids.add(id);
                    String tv = o.get("ten_viet") == null ? "" : String.valueOf(o.get("ten_viet"));
                    String nm = o.get("name") == null ? "" : String.valueOf(o.get("name"));
                    cbOpt.addItem("#" + id + " " + (tv.isEmpty() ? nm : tv));
                }
            } catch (Exception ex) {}
            ot.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(cbOpt) {
                @Override public Object getCellEditorValue() {
                    int i = cbOpt.getSelectedIndex();
                    return (i >= 0 && i < ids.size()) ? ids.get(i) : super.getCellEditorValue();
                }
            });
            om.addTableModelListener(e -> {
                if (og[0]) return;
                if (e.getType() != javax.swing.event.TableModelEvent.UPDATE) return;
                if (e.getColumn() == 2) return;
                int r = e.getFirstRow();
                if (r < 0 || r >= m.options.size()) return;
                try {
                    og[0] = true;
                    Object ov = om.getValueAt(r, 0);
                    int oid = ov instanceof Number ? ((Number) ov).intValue() : Integer.parseInt(String.valueOf(ov).replaceAll("[^0-9-]", ""));
                    long pv = Long.parseLong(String.valueOf(om.getValueAt(r, 1)).trim().replaceAll("[^0-9-]", ""));
                    m.options.get(r).id = oid;
                    m.options.get(r).param = pv;
                    om.setValueAt(PanelService.optionDisplay(oid), r, 2);
                } catch (Exception ex) {} finally { og[0] = false; }
            });
            JPanel pick = new JPanel(new BorderLayout(4, 4));
            JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            row1.add(new JLabel("Option:")); row1.add(cbOpt);
            JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JTextField fP = new JTextField("0", 12);
            JButton bAddO = btn("Them dong");
            bAddO.setBackground(new Color(70, 120, 220)); bAddO.setForeground(Color.WHITE);
            JButton bDelO = btn("Xoa dong");
            row2.add(new JLabel("Param:")); row2.add(fP);
            row2.add(bAddO); row2.add(bDelO);
            pick.add(row1, BorderLayout.NORTH);
            pick.add(row2, BorderLayout.CENTER);
            bAddO.addActionListener(e -> {
                int idx = cbOpt.getSelectedIndex();
                int oid = (idx >= 0 && idx < ids.size()) ? ids.get(idx) : 50;
                long pv = 0;
                try { pv = Long.parseLong(fP.getText().trim().replaceAll("[^0-9-]", "")); } catch (Exception ex) {}
                String er = optParamErrById(oid, pv);
                if (er != null) { err(er); return; }
                m.options.add(new GiftItemModel.Opt(oid, pv));
                reload.run();
            });
            bDelO.addActionListener(e -> { int r = ot.getSelectedRow(); if (r >= 0 && r < m.options.size()) { m.options.remove(r); reload.run(); } });
            d.add(pick, BorderLayout.NORTH);
            d.add(new JScrollPane(ot), BorderLayout.CENTER);
            JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton bOk = btn("Luu option");
            bOk.setBackground(new Color(40, 150, 80)); bOk.setForeground(Color.WHITE);
            bOk.addActionListener(e -> {
                try { if (ot.isEditing()) ot.getCellEditor().stopCellEditing(); } catch (Exception ex) {}
                d.dispose();
                try { if (refreshOuter != null) refreshOuter.run(); } catch (Exception ex) {}
            });
            bot.add(bOk);
            d.add(bot, BorderLayout.SOUTH);
            d.setVisible(true);
        } catch (Exception e) {}
    }

    // ================= EVENTS (phan tang: Chon event -> Bat/tat + ti le + farm -> Luu) =================
    private JList<String> lsEv;
    private DefaultListModel<String> lsEvModel;
    private java.util.List<Map<String, Object>> evCache = new java.util.ArrayList<>();
    private JLabel lbEvTitle, lbEvInfo;
    private JCheckBox cbEvOn;
    private JTextField fEvDrop, fEvPoint;
    private JTextArea fEvFarm, fEvNote;
    private String curEv = null;

    private JPanel buildEvents() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("1. Chon & Sua Event (admin moi dung tab nay)", eventEditTab());
        // catalog cu giu lam tab 2
        JPanel cat = new JPanel(new BorderLayout(8, 8));
        cat.setBackground(CARD);
        cat.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        DefaultTableModel evModel = new DefaultTableModel(new String[]{"Event", "Bat", "Boss spawn", "NPC", "Farm nguyen lieu (map/quai)", "Tinh diem", "Do roi (itemID)"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable evTable = new JTable(evModel);
        evTable.setAutoCreateRowSorter(true);
        JButton bEv = btn("Tai catalog Events");
        Runnable loadEv = () -> {
            List<Map<String, Object>> list = PanelService.listEventCatalog();
            SwingUtilities.invokeLater(() -> {
                evModel.setRowCount(0);
                for (Map<String, Object> m : list) evModel.addRow(new Object[]{m.get("key"), m.get("on"), m.get("boss"), m.get("npc"), m.get("farm"), m.get("diem"), m.get("items")});
            });
        };
        bEv.addActionListener(e -> bg(loadEv));
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.setBackground(CARD);
        bar.add(bEv);
        bar.add(new JLabel(" | Farm: mob thuong roi NL -> NPC nau banh (99 combo + 2 ty vang) -> diemfam -> top/doi qua"));
        cat.add(bar, BorderLayout.NORTH);
        cat.add(new JScrollPane(evTable), BorderLayout.CENTER);
        bg(loadEv);
        tabs.addTab("2. Catalog chi tiet (doc)", cat);
        tabs.addTab("3. Trung Thu (phan tang: ty le -> qua ruong -> chi so)", trungThuTab());
        JPanel body = new JPanel(new BorderLayout());
        body.add(tabs, BorderLayout.CENTER);
        return wrapPage(body, "Su Kien (Events)", "Buoc 1: chon Event -> bat/tat + ti le farm/diem -> Luu (co tac dung ngay, boss moi can restart init) | Buoc 2: xem catalog");
    }

    private JPanel eventEditTab() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(CARD);
        root.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        root.add(new JLabel("<html><b>Cach dung:</b> 1) Click event ben trai -> 2) Tich <b>Bat</b>, sua ti le farm/diem -> 3) Bam <b>Luu</b>. Tat = boss event do khong spawn o lan init sau.</html>"), BorderLayout.NORTH);
        JPanel left = new JPanel(new BorderLayout(6, 6));
        left.setBackground(CARD);
        left.setPreferredSize(new Dimension(280, 480));
        lsEvModel = new DefaultListModel<>();
        lsEv = new JList<>(lsEvModel);
        lsEv.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        left.add(new JLabel("Buoc 1 - Chon event:"), BorderLayout.NORTH);
        left.add(new JScrollPane(lsEv), BorderLayout.CENTER);
        JButton bReload = btn("Tai danh sach");
        left.add(bReload, BorderLayout.SOUTH);
        JPanel right = new JPanel(new BorderLayout(6, 6));
        right.setBackground(CARD);
        lbEvTitle = new JLabel("Chua chon event");
        lbEvTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        right.add(lbEvTitle, BorderLayout.NORTH);
        JPanel formWrap = new JPanel();
        formWrap.setBackground(CARD);
        formWrap.setLayout(new BoxLayout(formWrap, BoxLayout.Y_AXIS));
        lbEvInfo = new JLabel("-");
        evInfoBox = roArea(4);
        evInfoBox.setBorder(BorderFactory.createTitledBorder("Thong tin goc (boss/NPC/diem/roi)"));
        formWrap.add(evInfoBox); formWrap.add(Box.createVerticalStrut(10));
        // giu lbEvInfo dong bo voi box (an label goc)
        lbEvInfo.setVisible(false);
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(CARD);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        cbEvOn = new JCheckBox("Bat event nay");
        fEvDrop = tf(12); fEvPoint = tf(12);
        fEvFarm = new JTextArea(3, 15); fEvFarm.setLineWrap(true); fEvFarm.setWrapStyleWord(true);
        fEvNote = new JTextArea(2, 15); fEvNote.setLineWrap(true); fEvNote.setWrapStyleWord(true);
        addRow(grid, gc, 0, "Buoc 2 - Bat/Tat:", cbEvOn);
        addRow(grid, gc, 1, "Ti le roi farm (1.0 = goc):", fEvDrop);
        addRow(grid, gc, 2, "Ti le diem (1.0 = goc):", fEvPoint);
        addRow(grid, gc, 3, "Farm o map/quai nao:", new JScrollPane(fEvFarm));
        addRow(grid, gc, 4, "Ghi chu:", new JScrollPane(fEvNote));
        formWrap.add(grid);
        formWrap.putClientProperty("evInfoBox", evInfoBox);
        JScrollPane scR = new JScrollPane(formWrap);
        scR.setBorder(null);
        scR.getVerticalScrollBar().setUnitIncrement(16);
        right.add(scR, BorderLayout.CENTER);
        JPanel acts = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acts.setBackground(CARD);
        JButton bSave = btn("Luu (Buoc 3)");
        bSave.setBackground(new Color(40, 140, 70)); bSave.setForeground(Color.WHITE);
        acts.add(bSave);
        acts.add(new JLabel("Luu xong co tac dung ngay cho flag; boss moi can restart de init."));
        right.add(acts, BorderLayout.SOUTH);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setResizeWeight(0.0);
        split.setDividerLocation(300);
        left.setMinimumSize(new Dimension(240, 400));
        left.setPreferredSize(new Dimension(300, 480));
        root.add(split, BorderLayout.CENTER);
        Runnable reload = () -> {
            evCache = PanelService.listEventTuning();
            SwingUtilities.invokeLater(() -> {
                lsEvModel.clear();
                for (Map<String, Object> m : evCache) lsEvModel.addElement(String.valueOf(m.get("key")) + ((Boolean.TRUE.equals(m.get("on")) || Boolean.TRUE.equals(m.get("onFlag"))) ? " [BAT]" : " [TAT]"));
            });
        };
        bReload.addActionListener(e -> bg(reload));
        lsEv.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int i = lsEv.getSelectedIndex();
            if (i < 0 || i >= evCache.size()) return;
            loadEventEdit(i);
        });
        bSave.addActionListener(e -> {
            if (curEv == null) { err("Chon 1 event ben trai truoc"); return; }
            bg(() -> {
                double drop; try { drop = Double.parseDouble(fEvDrop.getText().trim()); } catch (Exception ex) { drop = 1.0; }
                double point; try { point = Double.parseDouble(fEvPoint.getText().trim()); } catch (Exception ex) { point = 1.0; }
                String r = PanelService.saveEventTuning(curEv, cbEvOn.isSelected(), drop, point, fEvFarm.getText(), fEvNote.getText());
                SwingUtilities.invokeLater(() -> { info(r); reload.run(); });
            });
        });
        bg(reload);
        return root;
    }

    private JTextArea evInfoBox;

    private void loadEventEdit(int idx) {
        Map<String, Object> m = evCache.get(idx);
        curEv = String.valueOf(m.get("key"));
        // thong tin goc tu catalog
        String info = "";
        try {
            for (Map<String, Object> c : PanelService.listEventCatalog()) {
                if (String.valueOf(c.get("key")).contains(curEv) || curEv.contains(String.valueOf(c.get("key")).split(" ")[0])) { info = "Boss: " + c.get("boss") + "\nNPC: " + c.get("npc") + "\nDiem: " + c.get("diem"); break; }
            }
        } catch (Exception e) {}
        final String fi = info;
        SwingUtilities.invokeLater(() -> {
            lbEvTitle.setText(curEv + (Boolean.TRUE.equals(m.get("on")) ? " [BAT]" : " [TAT]"));
            lbEvInfo.setText(fi);
            if (evInfoBox != null) { evInfoBox.setText(fi); evInfoBox.setCaretPosition(0); }
            cbEvOn.setSelected(Boolean.TRUE.equals(m.get("on")) || Boolean.TRUE.equals(m.get("onFlag")));
            fEvDrop.setText(String.valueOf(m.get("drop")));
            fEvPoint.setText(String.valueOf(m.get("point")));
            fEvFarm.setText(String.valueOf(m.get("farm") == null ? "" : m.get("farm")));
            fEvNote.setText(String.valueOf(m.get("note") == null ? "" : m.get("note")));
        });
    }

    // ================= TRUNG THU (PHAN TANG: bat/tat -> ty le roi -> nau banh -> moc doi qua -> ruong -> boss) =================
    private DefaultTableModel ttModel;
    private JTable ttTable;
    private JCheckBox cbTtOn;
    // --- moc doi diem (CRUD) ---
    private DefaultTableModel ttExModel, ttRwModel;
    private JTable ttExTable, ttRwTable;
    private java.util.List<java.util.List<int[]>> ttExRewards = new java.util.ArrayList<>();
    private int ttExSel = -1;
    private boolean ttBusy;
    // --- ruong 1914 (CRUD) ---
    private DefaultTableModel ttBoxModel;
    private JTable ttBoxTable;
    private JTextField fTtCostumeRate, fTtCostumeRange;

    private JPanel trungThuTab() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("1. Tong / Ty le roi / Nau banh / Boss", ttGeneralTab());
        tabs.addTab("2. Moc doi diem (them / sua / xoa + qua)", ttExchangeTab());
        tabs.addTab("3. Ruong Trung Thu 1914 (qua + option)", ttBoxTab());
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(CARD);
        body.add(tabs, BorderLayout.CENTER);
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.setBackground(CARD);
        cbTtOn = new JCheckBox("BAT su kien Trung Thu (bat = hien NPC noi banh, tat = an NPC ngay lap tuc)");
        JButton bOn = btn("Luu bat/tat");
        bOn.setBackground(new Color(40, 140, 70)); bOn.setForeground(Color.WHITE);
        bOn.addActionListener(e -> bg(() -> {
            String r = PanelService.saveTrungThuTuning("on", cbTtOn.isSelected() ? "1" : "0");
            SwingUtilities.invokeLater(() -> { info(r); ttSyncToggle.run(); });
        }));
        bar.add(cbTtOn);
        bar.add(bOn);
        bar.add(new JLabel(" | k = x1000 (7k = 7000%, 5k = 5000%) | NPC 66 chi o map lang, thay NPC 92"));
        body.add(bar, BorderLayout.SOUTH);
        bg(ttSyncToggle);
        return wrapPage(body, "Trung Thu (Phan tang)", "Bat/Tat su kien -> Ty le roi -> Nau banh -> Moc doi qua -> Ruong 1914 -> Boss");
    }

    private final Runnable ttSyncToggle = () -> {
        boolean on = PanelService.isTrungThuOn();
        SwingUtilities.invokeLater(() -> { if (cbTtOn != null) cbTtOn.setSelected(on); });
    };

    private static int ttCellInt(Object o, int def) {
        try { return (int) panel.tuning.TrungThuTuning.parseRate(String.valueOf(o)); }
        catch (Exception e) { return def; }
    }

    private static String ttItemName(int id) {
        try {
            models.Template.ItemTemplate t = services.ItemService.gI().getTemplate(id);
            return t == null ? "?" : t.name.trim();
        } catch (Exception e) { return "?"; }
    }

    // ---------- TAB 1: bang phan tang (ty le / nau banh / boss) ----------
    private JPanel ttGeneralTab() {
        JPanel edit = new JPanel(new BorderLayout(8, 8));
        edit.setBackground(CARD);
        edit.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        edit.add(new JLabel("<html><b>Cach dung:</b> sua cot <b>Gia tri</b> -&gt; bam <b>Luu Trung Thu</b>. "
                + "Moc doi qua va ruong sua rieng o tab 2 / tab 3.<br>"
                + "<b>Don vi: k = x1000</b> -&gt; <b>7k = 7000 (7000%)</b>, <b>5k = 5000 (5000%)</b>. <u>Khong nhap 77</u>.</html>"), BorderLayout.NORTH);
        ttModel = new DefaultTableModel(new String[]{"Nhom", "Khoa", "Gia tri", "Ghi chu"}, 0) {
            @Override public boolean isCellEditable(int r, int c) {
                if (c != 2) return false;
                try { Object k = getValueAt(r, 1); return k != null && !String.valueOf(k).trim().isEmpty(); }
                catch (Exception e) { return false; }
            }
        };
        ttTable = new JTable(ttModel);
        ttTable.setAutoCreateRowSorter(true);
        ttTable.getColumnModel().getColumn(0).setPreferredWidth(110);
        ttTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        ttTable.getColumnModel().getColumn(2).setPreferredWidth(260);
        ttTable.getColumnModel().getColumn(3).setPreferredWidth(560);
        edit.add(new JScrollPane(ttTable), BorderLayout.CENTER);
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.setBackground(CARD);
        JButton bLoad = btn("Tai lai");
        JButton bSave = btn("Luu Trung Thu");
        bSave.setBackground(new Color(40, 140, 70)); bSave.setForeground(Color.WHITE);
        bar.add(bLoad);
        bar.add(bSave);
        edit.add(bar, BorderLayout.SOUTH);
        Runnable loadTT = () -> {
            List<Map<String, Object>> list = PanelService.listTrungThuTuning();
            SwingUtilities.invokeLater(() -> {
                ttModel.setRowCount(0);
                String last = null;
                for (Map<String, Object> m : list) {
                    String k = String.valueOf(m.get("key"));
                    // moc doi diem va ruong co tab rieng
                    if (k.startsWith("exchange.") || k.startsWith("box.reward.")) continue;
                    String layer = String.valueOf(m.get("layer"));
                    if (!layer.equals(last)) {
                        ttModel.addRow(new Object[]{"== " + layer + " ==", "", "", ""});
                        last = layer;
                    }
                    ttModel.addRow(new Object[]{layer, k, m.get("value"), m.get("note")});
                }
            });
        };
        bLoad.addActionListener(e -> bg(loadTT));
        bSave.addActionListener(e -> bg(() -> {
            try { if (ttTable.isEditing()) ttTable.getCellEditor().stopCellEditing(); } catch (Exception ex) {}
            java.util.LinkedHashMap<String, String> vals = new java.util.LinkedHashMap<>();
            for (int i = 0; i < ttModel.getRowCount(); i++) {
                String k = String.valueOf(ttModel.getValueAt(i, 1));
                if (k == null || k.trim().isEmpty()) continue;
                vals.put(k, String.valueOf(ttModel.getValueAt(i, 2)));
            }
            String r = PanelService.saveTrungThuTuningAll(vals);
            SwingUtilities.invokeLater(() -> { info(r); loadTT.run(); });
        }));
        bg(loadTT);
        return edit;
    }

    // ---------- TAB 2: CRUD MOC DOI DIEM ----------
    private JPanel ttExchangeTab() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBackground(CARD);
        root.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        root.add(new JLabel("<html><b>Them / sua / xoa moc:</b> sua truc tiep cot <b>Diem can</b> / <b>Cong diem</b>; chon 1 moc de sua phan <b>Qua</b> duoi. "
                + "Them qua: nhap <b>ItemID</b> -&gt; bam Them qua. Sua <b>So luong / OptionID / Tham so option</b> truc tiep. "
                + "Bam <b>Luu moc doi diem</b> de ap dung.<br>"
                + "Luu y: trong game chi hien toi da <b>8 moc</b> (nut chon), moc thu 9 tro di chi luu trong DB.</html>"), BorderLayout.NORTH);

        ttExModel = new DefaultTableModel(new String[]{"#", "Diem can", "Cong diem sukien", "So qua"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 1 || c == 2; }
        };
        ttExTable = new JTable(ttExModel);
        ttExTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        ttExTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        ttExTable.getColumnModel().getColumn(2).setPreferredWidth(140);
        ttExTable.getColumnModel().getColumn(3).setPreferredWidth(70);
        ttExTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting() || ttBusy) return;
            ttRewardsPull();
            ttExSel = ttExTable.getSelectedRow();
            ttRewardsRefresh();
        });

        ttRwModel = new DefaultTableModel(new String[]{"#", "ItemID", "Ten vat pham", "So luong", "OptionID", "Tham so option"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 1 || c == 3 || c == 4 || c == 5; }
        };
        ttRwTable = new JTable(ttRwModel);
        ttRwTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        ttRwTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        ttRwTable.getColumnModel().getColumn(2).setPreferredWidth(220);
        ttRwTable.getColumnModel().getColumn(3).setPreferredWidth(90);
        ttRwTable.getColumnModel().getColumn(4).setPreferredWidth(85);
        ttRwTable.getColumnModel().getColumn(5).setPreferredWidth(120);

        JPanel top = new JPanel(new BorderLayout(4, 4));
        top.setBackground(CARD);
        top.add(new JLabel("Moc doi diem:"), BorderLayout.NORTH);
        top.add(new JScrollPane(ttExTable), BorderLayout.CENTER);
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setBackground(CARD);
        JButton bAddEx = btn("+ Them moc");
        JButton bDelEx = btn("- Xoa moc duoc chon");
        topBar.add(bAddEx);
        topBar.add(bDelEx);
        top.add(topBar, BorderLayout.SOUTH);

        JPanel bot = new JPanel(new BorderLayout(4, 4));
        bot.setBackground(CARD);
        bot.add(new JLabel("Qua trong moc duoc chon:"), BorderLayout.NORTH);
        bot.add(new JScrollPane(ttRwTable), BorderLayout.CENTER);
        JPanel botBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botBar.setBackground(CARD);
        JButton bAddRw = btn("+ Them qua");
        JButton bDelRw = btn("- Xoa qua");
        JButton bSaveEx = btn("Luu moc doi diem");
        bSaveEx.setBackground(new Color(40, 140, 70)); bSaveEx.setForeground(Color.WHITE);
        botBar.add(bAddRw);
        botBar.add(bDelRw);
        botBar.add(bSaveEx);
        botBar.add(new JLabel(" | Option mac dinh 30 = Da Khoa; option 50/77/103 = Dame/Sinh Luc/MaNa (%)"));
        bot.add(botBar, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bot);
        split.setResizeWeight(0.45);
        split.setBorder(null);
        root.add(split, BorderLayout.CENTER);

        bAddEx.addActionListener(e -> {
            ttRewardsPull();
            java.util.List<int[]> rw = new java.util.ArrayList<>();
            rw.add(new int[]{1554, 10, 30, 1});
            ttExRewards.add(rw);
            ttExModel.addRow(new Object[]{ttExModel.getRowCount() + 1, 5000, 5, rw.size()});
            ttExSel = ttExModel.getRowCount() - 1;
            ttBusy = true; ttExTable.setRowSelectionInterval(ttExSel, ttExSel); ttBusy = false;
            ttRewardsRefresh();
        });
        bDelEx.addActionListener(e -> {
            int i = ttExTable.getSelectedRow();
            if (i < 0) { err("Chon moc can xoa truoc"); return; }
            ttExModel.removeRow(i);
            ttExRewards.remove(i);
            ttExSel = -1;
            ttRewardsRefresh();
            ttRenumberEx();
        });
        bAddRw.addActionListener(e -> {
            if (ttExSel < 0) { err("Chon 1 moc truoc"); return; }
            ttRewardsPull();
            ttExRewards.get(ttExSel).add(new int[]{0, 1, 0, 0});
            ttRewardsRefresh();
        });
        bDelRw.addActionListener(e -> {
            if (ttExSel < 0) return;
            int i = ttRwTable.getSelectedRow();
            if (i < 0) { err("Chon mon qua can xoa"); return; }
            ttRewardsPull();
            ttExRewards.get(ttExSel).remove(i);
            ttRewardsRefresh();
        });
        bSaveEx.addActionListener(e -> bg(() -> {
            try { if (ttExTable.isEditing()) ttExTable.getCellEditor().stopCellEditing(); } catch (Exception ex) {}
            try { if (ttRwTable.isEditing()) ttRwTable.getCellEditor().stopCellEditing(); } catch (Exception ex) {}
            try { SwingUtilities.invokeAndWait(() -> ttRewardsPull()); }
            catch (Exception ex) { ttRewardsPull(); }
            java.util.List<Map<String, Object>> rows = new java.util.ArrayList<>();
            for (int i = 0; i < ttExModel.getRowCount(); i++) {
                java.util.LinkedHashMap<String, Object> m = new java.util.LinkedHashMap<>();
                m.put("need", ttCellInt(ttExModel.getValueAt(i, 1), 0));
                m.put("sukien", ttCellInt(ttExModel.getValueAt(i, 2), 0));
                m.put("rewards", i < ttExRewards.size() ? ttExRewards.get(i) : new java.util.ArrayList<int[]>());
                rows.add(m);
            }
            String r = PanelService.saveTrungThuExchange(rows);
            SwingUtilities.invokeLater(() -> { info(r); ttLoadExchange.run(); });
        }));

        Runnable loadEx = () -> {
            List<Map<String, Object>> list = PanelService.listTrungThuExchange();
            SwingUtilities.invokeLater(() -> {
                ttBusy = true;
                ttExModel.setRowCount(0);
                ttExRewards.clear();
                for (Map<String, Object> m : list) {
                    @SuppressWarnings("unchecked")
                    java.util.List<int[]> rw = (java.util.List<int[]>) m.get("rewards");
                    if (rw == null) rw = new java.util.ArrayList<>();
                    ttExRewards.add(rw);
                    ttExModel.addRow(new Object[]{ttExModel.getRowCount() + 1, m.get("need"), m.get("sukien"), rw.size()});
                }
                ttExSel = ttExModel.getRowCount() > 0 ? 0 : -1;
                if (ttExSel >= 0) ttExTable.setRowSelectionInterval(ttExSel, ttExSel);
                ttBusy = false;
                ttRewardsRefresh();
            });
        };
        ttLoadExchange = loadEx;
        bg(loadEx);
        return root;
    }

    private Runnable ttLoadExchange = () -> {};

    private void ttRenumberEx() {
        for (int i = 0; i < ttExModel.getRowCount(); i++) {
            ttExModel.setValueAt(i + 1, i, 0);
            ttExModel.setValueAt(ttExRewards.get(i).size(), i, 3);
        }
    }

    private void ttRewardsRefresh() {
        ttBusy = true;
        ttRwModel.setRowCount(0);
        if (ttExSel >= 0 && ttExSel < ttExRewards.size()) {
            java.util.List<int[]> rw = ttExRewards.get(ttExSel);
            for (int i = 0; i < rw.size(); i++) {
                int[] r = rw.get(i);
                ttRwModel.addRow(new Object[]{i + 1, r[0], ttItemName(r[0]), r[1], r[2], r[3]});
            }
        }
        ttBusy = false;
    }

    private void ttRewardsPull() {
        if (ttExSel < 0 || ttExSel >= ttExRewards.size()) return;
        try { if (ttRwTable.isEditing()) ttRwTable.getCellEditor().stopCellEditing(); } catch (Exception ex) {}
        java.util.List<int[]> rw = ttExRewards.get(ttExSel);
        rw.clear();
        for (int i = 0; i < ttRwModel.getRowCount(); i++) {
            int id = ttCellInt(ttRwModel.getValueAt(i, 1), 0);
            if (id <= 0) continue;
            rw.add(new int[]{id, Math.max(1, ttCellInt(ttRwModel.getValueAt(i, 3), 1)),
                    ttCellInt(ttRwModel.getValueAt(i, 4), 0), ttCellInt(ttRwModel.getValueAt(i, 5), 0)});
        }
        if (ttExSel < ttExModel.getRowCount()) {
            ttExModel.setValueAt(rw.size(), ttExSel, 3);
        }
    }

    // ---------- TAB 3: CRUD RUONG 1914 ----------
    private JPanel ttBoxTab() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBackground(CARD);
        root.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        root.add(new JLabel("<html><b>Noi dung Ruong Trung Thu (1914):</b> sua truc tiep <b>So luong / OptionID / Tham so option</b>, bam <b>+ Them qua</b> / <b>- Xoa qua</b>, roi <b>Luu ruong</b>.<br>"
                + "Option mau: <b>50</b> = Dame +N%, <b>77</b> = Sinh Luc +N%, <b>103</b> = MaNa +N% (vd 7000 = 7k = 7000%). <b>0</b> = khong gan option.</html>"), BorderLayout.NORTH);

        JPanel head = new JPanel(new FlowLayout(FlowLayout.LEFT));
        head.setBackground(CARD);
        head.add(new JLabel("Ty le re cai trang (%):"));
        fTtCostumeRate = tf("5", 5);
        head.add(fTtCostumeRate);
        head.add(new JLabel("Khoai ID cai trang (min-max):"));
        fTtCostumeRange = tf("282-292", 8);
        head.add(fTtCostumeRange);
        root.add(head, BorderLayout.NORTH);

        ttBoxModel = new DefaultTableModel(new String[]{"#", "ItemID", "Ten vat pham", "So luong", "OptionID", "Tham so option"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 1 || c == 3 || c == 4 || c == 5; }
        };
        ttBoxTable = new JTable(ttBoxModel);
        JScrollPane sc = new JScrollPane(ttBoxTable);
        sc.setBorder(null);
        root.add(sc, BorderLayout.CENTER);

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.setBackground(CARD);
        JButton bAdd = btn("+ Them qua");
        JButton bDel = btn("- Xoa qua");
        JButton bSave = btn("Luu ruong");
        JButton bLoad = btn("Tai lai");
        bSave.setBackground(new Color(40, 140, 70)); bSave.setForeground(Color.WHITE);
        bar.add(bAdd); bar.add(bDel); bar.add(bLoad); bar.add(bSave);
        root.add(bar, BorderLayout.SOUTH);

        bAdd.addActionListener(e -> {
            ttBoxModel.addRow(new Object[]{ttBoxModel.getRowCount() + 1, 0, "?", 1, 0, 0});
        });
        bDel.addActionListener(e -> {
            int i = ttBoxTable.getSelectedRow();
            if (i < 0) { err("Chon mon qua can xoa"); return; }
            ttBoxModel.removeRow(i);
            for (int k = 0; k < ttBoxModel.getRowCount(); k++) ttBoxModel.setValueAt(k + 1, k, 0);
        });
        Runnable loadBox = () -> bg(() -> {
            Map<String, Object> m = PanelService.listTrungThuBox();
            @SuppressWarnings("unchecked")
            java.util.List<int[]> rw = (java.util.List<int[]>) m.get("rewards");
            SwingUtilities.invokeLater(() -> {
                ttBoxModel.setRowCount(0);
                if (rw != null) {
                    for (int[] r : rw) {
                        ttBoxModel.addRow(new Object[]{ttBoxModel.getRowCount() + 1, r[0], ttItemName(r[0]), r[1], r[2], r[3]});
                    }
                }
                fTtCostumeRate.setText(String.valueOf(m.get("costumeRate")));
                fTtCostumeRange.setText(String.valueOf(m.get("costumeRange")));
            });
        });
        bLoad.addActionListener(e -> loadBox.run());
        bSave.addActionListener(e -> bg(() -> {
            try { if (ttBoxTable.isEditing()) ttBoxTable.getCellEditor().stopCellEditing(); } catch (Exception ex) {}
            java.util.List<int[]> rw = new java.util.ArrayList<>();
            Runnable pullBox = () -> {
                for (int i = 0; i < ttBoxModel.getRowCount(); i++) {
                    int id = ttCellInt(ttBoxModel.getValueAt(i, 1), 0);
                    if (id <= 0) continue;
                    rw.add(new int[]{id, Math.max(1, ttCellInt(ttBoxModel.getValueAt(i, 3), 1)),
                            ttCellInt(ttBoxModel.getValueAt(i, 4), 0), ttCellInt(ttBoxModel.getValueAt(i, 5), 0)});
                }
            };
            try { SwingUtilities.invokeAndWait(pullBox); } catch (Exception ex) { pullBox.run(); }
            String r = PanelService.saveTrungThuBox(ttCellInt(fTtCostumeRate.getText(), 5),
                    fTtCostumeRange.getText(), rw);
            SwingUtilities.invokeLater(() -> { info(r); loadBox.run(); });
        }));
        bg(loadBox);
        return root;
    }

    // ================= FORMULAS (sua truc tiep tren panel, khong vao code) =================
    private DefaultTableModel tuneModel;
    private JTable tuneTable;

    private JPanel buildFormulas() {
        JTabbedPane tabs = new JTabbedPane();
        // Tab 1: sua truc tiep
        JPanel edit = new JPanel(new BorderLayout(8, 8));
        edit.setBackground(CARD);
        edit.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        edit.add(new JLabel("<html><b>Cach dung:</b> Sua cot <b>Gia tri</b> -> Bam <b>Luu cong thuc</b>. Co tac dung ngay cho lan tinh chi so sau, khong can build lai. Dame ao: giu 1.0 luc dau, tang dan (1.2/1.5/2.0), max 10-30 ty nho BOSS_HP_SCALE + DAME_SOFT_CAP.</html>"), BorderLayout.NORTH);
        tuneModel = new DefaultTableModel(new String[]{"Ma", "Gia tri", "Giai thich (admin moi doc cot nay)"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 1; }
        };
        tuneTable = new JTable(tuneModel);
        tuneTable.setAutoCreateRowSorter(true);
        tuneTable.getColumnModel().getColumn(0).setPreferredWidth(140);
        tuneTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        tuneTable.getColumnModel().getColumn(2).setPreferredWidth(700);
        edit.add(new JScrollPane(tuneTable), BorderLayout.CENTER);
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.setBackground(CARD);
        JButton bLoad = btn("Tai cong thuc");
        JButton bSave = btn("Luu cong thuc");
        bSave.setBackground(new Color(40, 140, 70)); bSave.setForeground(Color.WHITE);
        bar.add(bLoad); bar.add(bSave);
        bar.add(new JLabel(" | Tan thu <100k -> so cap 1tr -> trung 10tr -> cao 100tr -> sieu 1 ty -> max 10-30 ty"));
        edit.add(bar, BorderLayout.SOUTH);
        Runnable loadT = () -> {
            List<Map<String, Object>> list = PanelService.listGameTuning();
            SwingUtilities.invokeLater(() -> {
                tuneModel.setRowCount(0);
                for (Map<String, Object> m : list) tuneModel.addRow(new Object[]{m.get("key"), m.get("value"), m.get("note")});
            });
        };
        bLoad.addActionListener(e -> bg(loadT));
        bSave.addActionListener(e -> bg(() -> {
            try { if (tuneTable.isEditing()) tuneTable.getCellEditor().stopCellEditing(); } catch (Exception ex) {}
            Map<String, Double> vals = new java.util.LinkedHashMap<>();
            for (int i = 0; i < tuneModel.getRowCount(); i++) {
                try { vals.put(String.valueOf(tuneModel.getValueAt(i, 0)), Double.parseDouble(String.valueOf(tuneModel.getValueAt(i, 1)))); }
                catch (Exception ex) {}
            }
            String r = PanelService.saveGameTuning(vals);
            SwingUtilities.invokeLater(() -> { info(r); loadT.run(); });
        }));
        bg(loadT);
        tabs.addTab("1. Sua cong thuc (admin moi dung tab nay)", edit);
        // Tab 2: giai thich chi tiet (doc)
        JPanel doc = new JPanel(new BorderLayout(8, 8));
        doc.setBackground(CARD);
        doc.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        DefaultTableModel fModel = new DefaultTableModel(new String[]{"Cong thuc", "Ham", "Chi tiet", "File"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable fTable = new JTable(fModel);
        fTable.setAutoCreateRowSorter(true);
        JButton bF = btn("Tai giai thich");
        Runnable loadF = () -> {
            List<Map<String, Object>> list = PanelService.listFormulaCatalog();
            SwingUtilities.invokeLater(() -> {
                fModel.setRowCount(0);
                for (Map<String, Object> m : list) fModel.addRow(new Object[]{m.get("ten"), m.get("ham"), m.get("congThuc"), m.get("file")});
            });
        };
        bF.addActionListener(e -> bg(loadF));
        JPanel bar2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar2.setBackground(CARD);
        bar2.add(bF);
        bar2.add(new JLabel(" | Bang giai thich ham tinh (chi doc, muon doi so thi sua o tab 1)"));
        doc.add(bar2, BorderLayout.NORTH);
        doc.add(new JScrollPane(fTable), BorderLayout.CENTER);
        bg(loadF);
        tabs.addTab("2. Giai thich ham (doc)", doc);
        JPanel body = new JPanel(new BorderLayout());
        body.add(tabs, BorderLayout.CENTER);
        return wrapPage(body, "Cong Thuc Game", "Tab 1: sua so truc tiep + Luu (co tac dung ngay) | Tab 2: giai thich ham tinh");
    }

    // ================= CHI SO PHAN TANG (Dame/HP/KI/Crit/Option/Output/Show-Real/X-Ty) =================
    private JPanel buildChiSo() {
        try {
            JTabbedPane tabs = new JTabbedPane();
            try { tabs.addTab("Dame", makeStatRateTab("Dame: sua cot Hien thi / Thuc te -> Luu (ap dung ngay).", (String k) -> { try { return k != null && k.toLowerCase().contains("dame"); } catch (Exception e) { return false; } })); } catch (Exception e) { e.printStackTrace(); }
            try { tabs.addTab("HP", makeStatRateTab("HP: sua cot Hien thi / Thuc te -> Luu (ap dung ngay).", (String k) -> { try { return k != null && (k.toLowerCase().contains("hp") || k.toLowerCase().contains("huyet")); } catch (Exception e) { return false; } })); } catch (Exception e) { e.printStackTrace(); }
            try { tabs.addTab("KI", makeStatRateTab("KI: sua cot Hien thi / Thuc te -> Luu (ap dung ngay).", (String k) -> { try { String s = k == null ? "" : k.toLowerCase(); return s.contains("_ki") || s.endsWith("ki") || s.contains("bokhi"); } catch (Exception e) { return false; } })); } catch (Exception e) { e.printStackTrace(); }
            try { tabs.addTab("Crit/Giap/Ne", makeCritTab()); } catch (Exception e) { e.printStackTrace(); }
            try { tabs.addTab("Option", makeStatRateTab("Option factor: display=so hien, real=so thuc, real()=REAL*GLOBAL_FACTOR.", (String k) -> { try { return k != null && k.toLowerCase().startsWith("opt"); } catch (Exception e) { return false; } })); } catch (Exception e) { e.printStackTrace(); }
            try { tabs.addTab("Output", makeOutputTab()); } catch (Exception e) { e.printStackTrace(); }
            try { tabs.addTab("Cong thuc Show/Real", makeStatRateTab("Toan bo Show/Real: Hien thi=DISPLAY, Thuc te=REAL, real()=REAL*GLOBAL_FACTOR.", (String k) -> true)); } catch (Exception e) { e.printStackTrace(); }
            try { tabs.addTab("X Ty test", makeTierTestTab()); } catch (Exception e) { e.printStackTrace(); }
            JPanel body = new JPanel(new BorderLayout());
            try { body.setBackground(BG); } catch (Exception e) {}
            try { body.add(tabs, BorderLayout.CENTER); } catch (Exception e) { e.printStackTrace(); }
            return wrapPage(body, "Chi So (Dame/HP/KI)", "Dame/HP/KI/Crit/Option/Output/Show-Real/X-Ty | Luu=ap dung nong, khong can restart");
        } catch (Exception e) { e.printStackTrace(); JPanel f = new JPanel(); try { f.add(new JLabel("Loi tao tab Chi So: " + e.getMessage())); } catch (Exception ex) {} return wrapPage(f, "Chi So", "Loi"); }
    }

    // Dien giai ten tham so tab Chi So sang tieng Viet de hieu truoc khi sua
    private static String statMeaning(String key) {
        try {
            String k = key == null ? "" : key.trim();
            if (k.startsWith("d_") || k.startsWith("r_")) k = k.substring(2);
            // cuoi ten = chi so dung cho he nao
            String suf = "";
            String base = k;
            if (base.endsWith("_dame")) { suf = "SD (sát thương)"; base = base.substring(0, base.length() - 5); }
            else if (base.endsWith("_hp")) { suf = "HP (máu)"; base = base.substring(0, base.length() - 3); }
            else if (base.endsWith("_ki")) { suf = "KI (năng lượng)"; base = base.substring(0, base.length() - 3); }
            if (!suf.isEmpty()) {
                String he = base;
                switch (base) {
                    case "chuyensinh" -> he = "Chuyển sinh";
                    case "thiendo" -> he = "Thiên Đạo";
                    case "capPb" -> he = "Cấp Pb (Boss)";
                    case "lyruou" -> he = "Lý rượu";
                    case "dakethon" -> he = "Đá Kết Hôn";
                    case "duockethon" -> he = "Kết hôn";
                    default -> { if (base.startsWith("pet")) he = "Pet bậc " + base.substring(3); }
                }
                return "Hệ " + he + ": chỉ số " + suf + " - cột Thực tế là giá trị % server cộng thật";
            }
            switch (base) {
                case "globalFactor": return "TOÀN CỤC: nhân MỌI cột 'Thực tế' (1.0 = giữ nguyên, 1.5 = x1.5 toàn server)";
                case "opt49_factor": return "Option 49 - Tấn công %: % thật = param trên đồ × Thực/100";
                case "opt50_factor": return "Option 50 - Sức đánh %: % thật = param trên đồ × Thực/100";
                case "opt77_factor": return "Option 77 - HP %: % thật = param trên đồ × Thực/100";
                case "opt103_factor": return "Option 103 - KI %: % thật = param trên đồ × Thực/100";
                case "banhgaquay": return "Bánh gà quay: chỉ số nhận khi ăn";
                case "cuongno_sc": return "Cường Nổ (sc): chỉ số khi dùng";
                case "cuongno2": return "Cường Nổ 2: chỉ số khi dùng";
                case "bohuyet_sc": return "Bộ Huyết (sc): chỉ số khi dùng";
                case "bokhi_sc": return "Bộ Khí (sc): chỉ số khi dùng";
                case "gogeta": return "Gogeta: chỉ số theo mốc";
                case "worldcup": return "Sự kiện World Cup: chỉ số theo mốc";
                case "nhatAn": return "Nhật Án: chỉ số theo mốc";
                case "nguyetAn": return "Nguyệt Án: chỉ số theo mốc";
                case "tinhAn": return "Tinh Án: chỉ số theo mốc";
                case "clan_per_lv": return "Bang hội: chỉ số cộng theo CẤP BANG";
                case "bachho_per_lv": return "Bạch Hổ: chỉ số cộng mỗi cấp";
                case "thanhlong_per_lv": return "Thanh Long: chỉ số cộng mỗi cấp";
                default: break;
            }
            if (base.startsWith("opt")) return "Option % trên đồ: % thật = param × Thực/100";
            if (base.startsWith("star")) return "Sao Pha Lê: chỉ số thưởng khi đạt " + base.substring(4) + " sao trên trang bị";
            if (base.startsWith("saoDen")) return "Sao Đen " + base.substring(6) + ": chỉ số thưởng";
            if (base.startsWith("vip")) return "VIP bậc " + base.substring(3) + ": chỉ số theo bậc VIP đang có";
            if (base.startsWith("set")) return "Set đồ " + base.substring(3) + ": chỉ số khi mang đủ bộ";
            if (base.startsWith("pet")) return "Pet bậc " + base.substring(3) + ": chỉ số pet buff cho chủ";
            return "Chưa có mô tả - 'Hiển thị' = số hiện trong game, 'Thực tế' = số server cộng thật";
        } catch (Exception e) { return key; }
    }

    private JPanel makeStatRateTab(String hint, java.util.function.Predicate<String> filter) {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        try { p.setBackground(CARD); } catch (Exception e) {}
        try { p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14))); } catch (Exception e) {}
        try { p.add(new JLabel("<html><b>Cách dùng:</b> cột <b>Ý nghĩa</b> giải thích tham số là gì - đọc nó trước khi sửa. "
                + "Cột <b>Hiển thị</b> = số player nhìn thấy trên đồ, cột <b>Thực tế</b> = số server thật sự cộng vào chỉ số. "
                + "Sửa 2 cột đó rồi bấm <b>Lưu</b> = áp dụng ngay, không cần restart. " + hint + "</html>"), BorderLayout.NORTH); } catch (Exception e) {}
        DefaultTableModel m = new DefaultTableModel(new String[]{"Tham số", "Ý nghĩa / cách dùng", "Hiển thị", "Thực tế"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { try { return c == 2 || c == 3; } catch (Exception e) { return false; } }
        };
        JTable t = new JTable(m);
        try { t.setAutoCreateRowSorter(true); } catch (Exception e) {}
        try {
            javax.swing.table.TableColumnModel ccm = t.getColumnModel();
            ccm.getColumn(0).setPreferredWidth(150);
            ccm.getColumn(1).setPreferredWidth(470);
            ccm.getColumn(2).setPreferredWidth(90);
            ccm.getColumn(3).setPreferredWidth(90);
        } catch (Exception e) {}
        try { p.add(new JScrollPane(t), BorderLayout.CENTER); } catch (Exception e) {}
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        try { bar.setBackground(CARD); } catch (Exception e) {}
        JButton bLoad = btn("Tai lai");
        JButton bSave = btn("Luu");
        try { bSave.setBackground(new Color(40, 140, 70)); bSave.setForeground(Color.WHITE); } catch (Exception e) {}
        JButton bReset = btn("Khoi phuc");
        try { bar.add(bLoad); } catch (Exception e) {}
        try { bar.add(bSave); } catch (Exception e) {}
        try { bar.add(bReset); } catch (Exception e) {}
        try { bar.add(new JLabel(" | Hiển thị = số game hiện · Thực tế = số server cộng thật · Khoi phuc = đặt Thực tế về bằng Hiển thị")); } catch (Exception e) {}
        try { p.add(bar, BorderLayout.SOUTH); } catch (Exception e) {}
        Runnable load = () -> {
            try {
                List<Map<String, Object>> list = PanelService.listStatRate();
                SwingUtilities.invokeLater(() -> {
                    try {
                        m.setRowCount(0);
                        if (list != null) for (Map<String, Object> row : list) {
                            try {
                                String k = String.valueOf(row.get("key"));
                                if (filter != null && !filter.test(k)) continue;
                                m.addRow(new Object[]{k, statMeaning(k), row.get("display"), row.get("real")});
                            } catch (Exception ex) {}
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                });
            } catch (Exception e) { e.printStackTrace(); }
        };
        try { bLoad.addActionListener(e -> bg(load)); } catch (Exception e) {}
        try {
            bSave.addActionListener(e -> bg(() -> {
                try { try { if (t.isEditing()) t.getCellEditor().stopCellEditing(); } catch (Exception ex) {} } catch (Exception ex) {}
                try {
                    Map<String, double[]> vals = new java.util.LinkedHashMap<>();
                    for (int i = 0; i < m.getRowCount(); i++) {
                        try {
                            String k = String.valueOf(m.getValueAt(i, 0));
                            double d = Double.parseDouble(String.valueOf(m.getValueAt(i, 2)));
                            double r = Double.parseDouble(String.valueOf(m.getValueAt(i, 3)));
                            vals.put(k, new double[]{d, r});
                        } catch (Exception ex) {}
                    }
                    String r = PanelService.saveStatRate(vals);
                    SwingUtilities.invokeLater(() -> { try { info(r); } catch (Exception ex) {} try { load.run(); } catch (Exception ex) {} });
                } catch (Exception ex) { ex.printStackTrace(); }
            }));
        } catch (Exception e) {}
        try {
            bReset.addActionListener(e -> bg(() -> {
                try {
                    for (int i = 0; i < m.getRowCount(); i++) {
                        try { m.setValueAt(m.getValueAt(i, 2), i, 3); } catch (Exception ex) {}
                    }
                    Map<String, double[]> vals = new java.util.LinkedHashMap<>();
                    for (int i = 0; i < m.getRowCount(); i++) {
                        try {
                            String k = String.valueOf(m.getValueAt(i, 0));
                            double d = Double.parseDouble(String.valueOf(m.getValueAt(i, 2)));
                            double r = Double.parseDouble(String.valueOf(m.getValueAt(i, 3)));
                            vals.put(k, new double[]{d, r});
                        } catch (Exception ex) {}
                    }
                    String r = PanelService.saveStatRate(vals);
                    SwingUtilities.invokeLater(() -> { try { info(r + " (Khoi phuc: Thuc te = Hien thi)"); } catch (Exception ex) {} try { load.run(); } catch (Exception ex) {} });
                } catch (Exception ex) { ex.printStackTrace(); }
            }));
        } catch (Exception e) {}
        try { bg(load); } catch (Exception e) {}
        return p;
    }

    private JPanel makeCritTab() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        try { p.setBackground(CARD); } catch (Exception e) {}
        try { p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14))); } catch (Exception e) {}
        try { p.add(new JLabel("<html><b>Cach dung:</b> Sua cot Hien thi -> Luu (ap dung ngay). 1=bat, 0=tat cho 2 dong THIENDAO.</html>"), BorderLayout.NORTH); } catch (Exception e) {}
        DefaultTableModel m = new DefaultTableModel(new String[]{"Ten", "Hien thi", "Thuc te"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { try { return c == 1 || c == 2; } catch (Exception e) { return false; } }
        };
        JTable t = new JTable(m);
        try { t.setAutoCreateRowSorter(true); } catch (Exception e) {}
        try { p.add(new JScrollPane(t), BorderLayout.CENTER); } catch (Exception e) {}
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        try { bar.setBackground(CARD); } catch (Exception e) {}
        JButton bLoad = btn("Tai lai");
        JButton bSave = btn("Luu");
        try { bSave.setBackground(new Color(40, 140, 70)); bSave.setForeground(Color.WHITE); } catch (Exception e) {}
        JButton bReset = btn("Khoi phuc");
        try { bar.add(bLoad); bar.add(bSave); bar.add(bReset); } catch (Exception e) {}
        try { p.add(bar, BorderLayout.SOUTH); } catch (Exception e) {}
        Runnable load = () -> {
            try {
                List<Map<String, Object>> list = PanelService.listCrit();
                SwingUtilities.invokeLater(() -> {
                    try {
                        m.setRowCount(0);
                        if (list != null) for (Map<String, Object> row : list) {
                            try { m.addRow(new Object[]{row.get("key"), row.get("value"), row.get("value")}); } catch (Exception ex) {}
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                });
            } catch (Exception e) { e.printStackTrace(); }
        };
        try { bLoad.addActionListener(e -> bg(load)); } catch (Exception e) {}
        try {
            bSave.addActionListener(e -> bg(() -> {
                try { try { if (t.isEditing()) t.getCellEditor().stopCellEditing(); } catch (Exception ex) {} } catch (Exception ex) {}
                try {
                    Map<String, Double> vals = new java.util.LinkedHashMap<>();
                    for (int i = 0; i < m.getRowCount(); i++) {
                        try { vals.put(String.valueOf(m.getValueAt(i, 0)), Double.parseDouble(String.valueOf(m.getValueAt(i, 1)))); } catch (Exception ex) {}
                    }
                    String r = PanelService.saveCrit(vals);
                    SwingUtilities.invokeLater(() -> { try { info(r); } catch (Exception ex) {} try { load.run(); } catch (Exception ex) {} });
                } catch (Exception ex) { ex.printStackTrace(); }
            }));
        } catch (Exception e) {}
        try {
            bReset.addActionListener(e -> bg(() -> {
                try { String r = PanelService.saveCrit(new java.util.LinkedHashMap<>()); SwingUtilities.invokeLater(() -> { try { info(r); } catch (Exception ex) {} try { load.run(); } catch (Exception ex) {} }); } catch (Exception ex) { ex.printStackTrace(); }
            }));
        } catch (Exception e) {}
        try { bg(load); } catch (Exception e) {}
        return p;
    }

    private JPanel makeOutputTab() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        try { p.setBackground(CARD); } catch (Exception e) {}
        try { p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14))); } catch (Exception e) {}
        DefaultTableModel m = new DefaultTableModel(new String[]{"Ten", "Hien thi", "Thuc te"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { try { return c == 1 || c == 2; } catch (Exception e) { return false; } }
        };
        JTable t = new JTable(m);
        try { t.setAutoCreateRowSorter(true); } catch (Exception e) {}
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        try { top.setBackground(CARD); } catch (Exception e) {}
        JSlider slider = new JSlider(10, 500, 100);
        JTextField txt = new JTextField("1.0", 6);
        JButton b05 = btn("0.5");
        JButton b10 = btn("1.0");
        JButton b15 = btn("1.5");
        JButton bApply = btn("Ap dung nong");
        try { bApply.setBackground(new Color(40, 140, 70)); bApply.setForeground(Color.WHITE); } catch (Exception e) {}
        try { top.add(new JLabel("PLAYER_OUT_RATE (0.1-5.0):")); } catch (Exception e) {}
        try { top.add(slider); } catch (Exception e) {}
        try { top.add(txt); } catch (Exception e) {}
        try { top.add(b05); top.add(b10); top.add(b15); top.add(bApply); } catch (Exception e) {}
        try { top.add(new JLabel(" | preset 0.5/1.0/1.5")); } catch (Exception e) {}
        try { p.add(top, BorderLayout.NORTH); } catch (Exception e) {}
        try { p.add(new JScrollPane(t), BorderLayout.CENTER); } catch (Exception e) {}
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        try { bar.setBackground(CARD); } catch (Exception e) {}
        JButton bLoad = btn("Tai lai");
        JButton bSave = btn("Luu");
        try { bSave.setBackground(new Color(40, 140, 70)); bSave.setForeground(Color.WHITE); } catch (Exception e) {}
        JButton bReset = btn("Khoi phuc");
        try { bar.add(bLoad); bar.add(bSave); bar.add(bReset); } catch (Exception e) {}
        try { p.add(bar, BorderLayout.SOUTH); } catch (Exception e) {}
        Runnable load = () -> {
            try {
                List<Map<String, Object>> list = PanelService.listOutput();
                SwingUtilities.invokeLater(() -> {
                    try {
                        m.setRowCount(0);
                        if (list != null) for (Map<String, Object> row : list) {
                            try { m.addRow(new Object[]{row.get("key"), row.get("value"), row.get("value")}); } catch (Exception ex) {}
                        }
                        try {
                            for (int i = 0; i < m.getRowCount(); i++) {
                                try {
                                    if ("PLAYER_OUT_RATE".equals(String.valueOf(m.getValueAt(i, 0)))) {
                                        double v = Double.parseDouble(String.valueOf(m.getValueAt(i, 1)));
                                        if (v < 0.1) v = 0.1; if (v > 5.0) v = 5.0;
                                        int sv = (int) Math.round(v * 100);
                                        if (sv < 10) sv = 10; if (sv > 500) sv = 500;
                                        slider.setValue(sv);
                                        txt.setText(String.valueOf(v));
                                    }
                                } catch (Exception ex) {}
                            }
                        } catch (Exception e) { e.printStackTrace(); }
                    } catch (Exception e) { e.printStackTrace(); }
                });
            } catch (Exception e) { e.printStackTrace(); }
        };
        try {
            slider.addChangeListener(e -> {
                try { txt.setText(String.valueOf(slider.getValue() / 100.0)); } catch (Exception ex) {}
            });
        } catch (Exception e) {}
        try { b05.addActionListener(e -> { try { slider.setValue(50); txt.setText("0.5"); } catch (Exception ex) {} }); } catch (Exception e) {}
        try { b10.addActionListener(e -> { try { slider.setValue(100); txt.setText("1.0"); } catch (Exception e2) {} }); } catch (Exception e) {}
        try { b15.addActionListener(e -> { try { slider.setValue(150); txt.setText("1.5"); } catch (Exception e2) {} }); } catch (Exception e) {}
        try {
            bApply.addActionListener(e -> bg(() -> {
                try {
                    double v = 1.0;
                    try { v = Double.parseDouble(txt.getText().trim()); } catch (Exception ex) { v = slider.getValue() / 100.0; }
                    if (v < 0.1) v = 0.1; if (v > 5.0) v = 5.0;
                    Map<String, Double> vals = new java.util.LinkedHashMap<>();
                    try { vals.put("PLAYER_OUT_RATE", v); } catch (Exception ex) {}
                    try {
                        for (int i = 0; i < m.getRowCount(); i++) {
                            try {
                                String k = String.valueOf(m.getValueAt(i, 0));
                                if ("PLAYER_OUT_RATE".equals(k)) continue;
                                vals.put(k, Double.parseDouble(String.valueOf(m.getValueAt(i, 1))));
                            } catch (Exception ex) {}
                        }
                    } catch (Exception ex) {}
                    String r = PanelService.saveOutput(vals);
                    SwingUtilities.invokeLater(() -> { try { info(r); } catch (Exception ex) {} try { load.run(); } catch (Exception ex) {} });
                } catch (Exception ex) { ex.printStackTrace(); }
            }));
        } catch (Exception e) {}
        try { bLoad.addActionListener(e -> bg(load)); } catch (Exception e) {}
        try {
            bSave.addActionListener(e -> bg(() -> {
                try { try { if (t.isEditing()) t.getCellEditor().stopCellEditing(); } catch (Exception ex) {} } catch (Exception ex) {}
                try {
                    Map<String, Double> vals = new java.util.LinkedHashMap<>();
                    for (int i = 0; i < m.getRowCount(); i++) {
                        try { vals.put(String.valueOf(m.getValueAt(i, 0)), Double.parseDouble(String.valueOf(m.getValueAt(i, 1)))); } catch (Exception ex) {}
                    }
                    String r = PanelService.saveOutput(vals);
                    SwingUtilities.invokeLater(() -> { try { info(r); } catch (Exception ex) {} try { load.run(); } catch (Exception ex) {} });
                } catch (Exception ex) { ex.printStackTrace(); }
            }));
        } catch (Exception e) {}
        try {
            bReset.addActionListener(e -> bg(() -> {
                try {
                    Map<String, Double> vals = new java.util.LinkedHashMap<>();
                    try { vals.put("PLAYER_OUT_RATE", 1.0); vals.put("HP_OUT_RATE", 1.0); vals.put("KI_OUT_RATE", 1.0); vals.put("GLOBAL_FACTOR", 1.0); } catch (Exception ex) {}
                    String r = PanelService.saveOutput(vals);
                    SwingUtilities.invokeLater(() -> { try { info(r + " (Khoi phuc ve 1.0)"); } catch (Exception ex) {} try { load.run(); } catch (Exception ex) {} });
                } catch (Exception ex) { ex.printStackTrace(); }
            }));
        } catch (Exception e) {}
        try { bg(load); } catch (Exception e) {}
        return p;
    }

    private JPanel makeTierTestTab() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        try { p.setBackground(CARD); } catch (Exception e) {}
        try { p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14))); } catch (Exception e) {}
        JPanel f = new JPanel(new FlowLayout(FlowLayout.LEFT));
        try { f.setBackground(CARD); } catch (Exception e) {}
        JTextField fM = new JTextField("65", 10);
        JTextField fT = new JTextField("3", 10);
        JButton bTest = btn("Test");
        try { bTest.setBackground(new Color(40, 140, 70)); bTest.setForeground(Color.WHITE); } catch (Exception e) {}
        JLabel lb = new JLabel("Nhap mantissa + tier -> Bam Test");
        try { f.add(new JLabel("mantissa:")); } catch (Exception e) {}
        try { f.add(fM); } catch (Exception e) {}
        try { f.add(new JLabel("tier:")); } catch (Exception e) {}
        try { f.add(fT); } catch (Exception e) {}
        try { f.add(bTest); } catch (Exception e) {}
        try { f.add(lb); } catch (Exception e) {}
        try { p.add(f, BorderLayout.NORTH); } catch (Exception e) {}
        JTextArea note = new JTextArea("X Ty: mantissa * (1 ty ^ tier). Vd 65 X3Ti. Bam Test de xem chuoi hien thi qua TieredNumber.toDisplayString().");
        try { note.setEditable(false); } catch (Exception e) {}
        try { p.add(new JScrollPane(note), BorderLayout.CENTER); } catch (Exception e) {}
        try {
            bTest.addActionListener(e -> bg(() -> {
                try {
                    long mm = 0; int tt = 0;
                    try { mm = Long.parseLong(fM.getText().trim()); } catch (Exception ex) { final String msg = "mantissa phai la so"; SwingUtilities.invokeLater(() -> { try { lb.setText(msg); } catch (Exception e2) {} }); return; }
                    try { tt = Integer.parseInt(fT.getText().trim()); } catch (Exception ex) { final String msg = "tier phai la so nguyen"; SwingUtilities.invokeLater(() -> { try { lb.setText(msg); } catch (Exception e2) {} }); return; }
                    utils.TieredNumber tn = utils.TieredNumber.of(mm, tt);
                    String s = "0";
                    double cap = 0;
                    try { s = tn.toDisplayString(); } catch (Exception ex) {}
                    try { cap = tn.toCappedDouble(); } catch (Exception ex) {}
                    final String out = s + " (cap=" + cap + ", raw=" + tn.toString() + ")";
                    SwingUtilities.invokeLater(() -> { try { lb.setText(out); } catch (Exception ex) {} try { info(out); } catch (Exception ex) {} });
                } catch (Exception ex) { ex.printStackTrace(); }
            }));
        } catch (Exception e) {}
        return p;
    }

    // ================= TAMBAO (VONG QUAY) =================
    private DefaultTableModel mdTbItem, mdTbMoc, mdTbHist;
    private JTable tbTbItem, tbTbMoc, tbTbHist;
    private java.util.List<Map<String, Object>> tbItemCache = new java.util.ArrayList<>();
    private java.util.List<Map<String, Object>> tbMocCache = new java.util.ArrayList<>();
    private JLabel lbTbInfo;
    private JComboBox<String> cbTbItem;
    private java.util.List<Integer> cbTbItemIds = new java.util.ArrayList<>();

    private JPanel buildTambao() {
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setBackground(CARD);
        body.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        JButton bReload = btn("Reload Tam Bao");
        bReload.setBackground(new Color(40, 140, 70)); bReload.setForeground(Color.WHITE);
        lbTbInfo = new JLabel("Hien tai: " + PanelService.tamBaoInfo());
        bReload.addActionListener(e -> bg(() -> { PanelService.reloadTamBao(); SwingUtilities.invokeLater(this::refreshTbAll); }));
        top.add(bReload); top.add(lbTbInfo);
        body.add(top, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Vat pham quay (tambao_items)", buildTbItemTab());
        tabs.addTab("Moc thuong (moc_vong_quay)", buildTbMocTab());
        tabs.addTab("Lich su quay (history_tambao)", buildTbHistTab());
        body.add(tabs, BorderLayout.CENTER);

        JLabel hint = new JLabel("<html>Sua o day -> tu luu vao SQL va <b>reload Tam Bao ngay</b>, khong can restart server. "
                + "Server nap 14 o moi lan mo vong quay; thieu dong thi bu vat pham du phong (ty le 0%). "
                + "Chia mac dinh 1874 (Key vang) - neu pool 1874 rong thi server lay key nho nhat. "
                + "Client: cmd 106 = mo vong quay / nhan moc, cmd 107 = quay x1-x10 / nhan moc.</html>");
        hint.setBorder(new EmptyBorder(6, 4, 0, 4));
        body.add(hint, BorderLayout.SOUTH);

        bg(() -> SwingUtilities.invokeLater(() -> { loadTbItems(); loadTbMocs(); loadTbHist(); loadTbItemCombo(""); }));
        return wrapPage(body, "Vong Quay (Tam Bao)", "Quan ly vat pham quay, moc thuong va lich su - luu truc tiep vao SQL");
    }

    private void refreshTbAll() {
        loadTbItems(); loadTbMocs(); loadTbHist();
        if (lbTbInfo != null) lbTbInfo.setText("Hien tai: " + PanelService.tamBaoInfo());
    }

    private static String tbStr(Object o) { return o == null ? "" : String.valueOf(o); }

    // ---------------- TAB 1: VAT PHAM QUAY ----------------
    private JPanel buildTbItemTab() {
        JPanel p = new JPanel(new BorderLayout(4, 4));
        p.setBackground(CARD);
        mdTbItem = new DefaultTableModel(new String[]{"ID", "Chia (key)", "TempID", "Ten vat pham", "SL", "Ty le %", "Option", "Ghi chu", "Tu", "Den", "Trang thai"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tbTbItem = new JTable(mdTbItem);
        tbTbItem.setSelectionMode(0);
        p.add(new JScrollPane(tbTbItem), BorderLayout.CENTER);

        JPanel edit = new JPanel(new GridBagLayout());
        edit.setBackground(CARD);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(3, 3, 3, 3); gc.anchor = GridBagConstraints.WEST; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1;

        final JTextField fFilter = tf(12);
        JButton bFilter = btn("Tim vat pham");
        cbTbItem = new JComboBox<>();
        cbTbItem.setPreferredSize(new Dimension(320, 26));
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row1.setBackground(CARD);
        row1.add(new JLabel("Tim:")); row1.add(fFilter); row1.add(bFilter); row1.add(cbTbItem);

        final JTextField fKey = tf("1874", 6);
        final JTextField fTemp = tf(6);
        final JTextField fQty = tf("1", 4);
        final JTextField fRate = tf("10", 5);
        final JTextField fOpt = tf(10);
        final JTextField fDes = tf(14);
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row2.setBackground(CARD);
        row2.add(new JLabel("Chia:")); row2.add(fKey);
        row2.add(new JLabel("TempID:")); row2.add(fTemp);
        row2.add(new JLabel("SL:")); row2.add(fQty);
        row2.add(new JLabel("Ty le %:")); row2.add(fRate);
        row2.add(new JLabel("Option:")); row2.add(fOpt);
        row2.add(new JLabel("Ghi chu:")); row2.add(fDes);

        final JTextField fStart = tf(13);
        final JTextField fEnd = tf(13);
        final JComboBox<String> cbOn = new JComboBox<>(new String[]{"Bat", "Tat"});
        JButton bAdd = btn("Them dong");
        bAdd.setBackground(new Color(40, 140, 70)); bAdd.setForeground(Color.WHITE);
        JButton bUpd = btn("Sua dong chon");
        JButton bDel = btn("Xoa dong chon");
        bDel.setBackground(new Color(190, 60, 60)); bDel.setForeground(Color.WHITE);
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row3.setBackground(CARD);
        row3.add(new JLabel("Tu (yyyy-MM-dd HH:mm:ss):")); row3.add(fStart);
        row3.add(new JLabel("Den:")); row3.add(fEnd);
        row3.add(new JLabel("TT:")); row3.add(cbOn);
        row3.add(bAdd); row3.add(bUpd); row3.add(bDel);

        gc.gridx = 0; gc.gridy = 0; edit.add(row1, gc);
        gc.gridy = 1; edit.add(row2, gc);
        gc.gridy = 2; edit.add(row3, gc);
        p.add(edit, BorderLayout.SOUTH);

        tbTbItem.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int r = tbTbItem.getSelectedRow();
            if (r < 0 || r >= tbItemCache.size()) return;
            Map<String, Object> m = tbItemCache.get(r);
            fKey.setText(String.valueOf(m.get("key_item_id")));
            fTemp.setText(String.valueOf(m.get("item_id")));
            fQty.setText(String.valueOf(m.get("quantity")));
            fRate.setText(String.valueOf(m.get("rate")));
            fOpt.setText(tbStr(m.get("item_options")));
            fDes.setText(tbStr(m.get("des")));
            fStart.setText(tbStr(m.get("start_at")));
            fEnd.setText(tbStr(m.get("end_at")));
            cbOn.setSelectedIndex(((Number) m.get("enabled")).intValue() == 1 ? 0 : 1);
        });
        bFilter.addActionListener(e -> loadTbItemCombo(fFilter.getText()));
        cbTbItem.addActionListener(e -> {
            int i = cbTbItem.getSelectedIndex();
            if (i >= 0 && i < cbTbItemIds.size()) fTemp.setText(String.valueOf(cbTbItemIds.get(i)));
        });
        bAdd.addActionListener(e -> {
            try {
                final int key = Integer.parseInt(fKey.getText().trim());
                final int tid = Integer.parseInt(fTemp.getText().trim());
                final int qty = Integer.parseInt(fQty.getText().trim());
                final double rate = Double.parseDouble(fRate.getText().trim().replace(",", "."));
                final String opt = fOpt.getText().trim(), des = fDes.getText().trim();
                final String st = fStart.getText().trim(), en = fEnd.getText().trim();
                final boolean on = cbOn.getSelectedIndex() == 0;
                bg(() -> { String r = PanelService.addTamBaoItem(key, tid, qty, opt, rate, des, st, en, on);
                    SwingUtilities.invokeLater(() -> { info(r); refreshTbAll(); }); });
            } catch (Exception ex) { err("Chia/TempID/SL/Ty le phai la so nguyen"); }
        });
        bUpd.addActionListener(e -> {
            int r = tbTbItem.getSelectedRow();
            if (r < 0) { err("Chon 1 dong trong bang truoc"); return; }
            try {
                final int id = ((Number) mdTbItem.getValueAt(r, 0)).intValue();
                final int key = Integer.parseInt(fKey.getText().trim());
                final int tid = Integer.parseInt(fTemp.getText().trim());
                final int qty = Integer.parseInt(fQty.getText().trim());
                final double rate = Double.parseDouble(fRate.getText().trim().replace(",", "."));
                final String opt = fOpt.getText().trim(), des = fDes.getText().trim();
                final String st = fStart.getText().trim(), en = fEnd.getText().trim();
                final boolean on = cbOn.getSelectedIndex() == 0;
                bg(() -> { String rs = PanelService.updateTamBaoItem(id, key, tid, qty, opt, rate, des, st, en, on);
                    SwingUtilities.invokeLater(() -> { info(rs); refreshTbAll(); }); });
            } catch (Exception ex) { err("Chia/TempID/SL/Ty le phai la so nguyen"); }
        });
        bDel.addActionListener(e -> {
            int r = tbTbItem.getSelectedRow();
            if (r < 0) { err("Chon 1 dong trong bang truoc"); return; }
            final int id = ((Number) mdTbItem.getValueAt(r, 0)).intValue();
            int c = JOptionPane.showConfirmDialog(this, "Xoa dong vat pham vong quay id=" + id + "?", "Xac nhan", JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION) return;
            bg(() -> { String s = PanelService.deleteTamBaoItem(id); SwingUtilities.invokeLater(() -> { info(s); refreshTbAll(); }); });
        });
        return p;
    }

    private void loadTbItems() {
        bg(() -> {
            java.util.List<Map<String, Object>> list = PanelService.listTamBaoItems();
            SwingUtilities.invokeLater(() -> {
                tbItemCache = list;
                mdTbItem.setRowCount(0);
                for (Map<String, Object> m : list) {
                    mdTbItem.addRow(new Object[]{m.get("id"),
                            m.get("key_item_id") + (tbStr(m.get("key_name")).isEmpty() ? "" : " - " + m.get("key_name")),
                            m.get("item_id"), tbStr(m.get("item_name")), m.get("quantity"), m.get("rate"),
                            tbStr(m.get("item_options")), tbStr(m.get("des")),
                            tbStr(m.get("start_at")), tbStr(m.get("end_at")),
                            ((Number) m.get("enabled")).intValue() == 1 ? "Bat" : "Tat"});
                }
            });
        });
    }

    private void loadTbItemCombo(String filter) {
        bg(() -> {
            java.util.List<Map<String, Object>> list = PanelService.listItemTemplatesFull(filter, -1, -1, 500);
            SwingUtilities.invokeLater(() -> {
                cbTbItem.removeAllItems();
                cbTbItemIds.clear();
                for (Map<String, Object> m : list) {
                    int id = ((Number) m.get("id")).intValue();
                    cbTbItemIds.add(id);
                    cbTbItem.addItem(id + " - " + m.get("name"));
                }
            });
        });
    }

    // ---------------- TAB 2: MOC THUONG ----------------
    private JPanel buildTbMocTab() {
        JPanel p = new JPanel(new BorderLayout(4, 4));
        p.setBackground(CARD);
        mdTbMoc = new DefaultTableModel(new String[]{"ID", "TempID", "Ten vat pham", "SL", "Diem quay can", "Option JSON"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tbTbMoc = new JTable(mdTbMoc);
        tbTbMoc.setSelectionMode(0);
        p.add(new JScrollPane(tbTbMoc), BorderLayout.CENTER);

        JPanel edit = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        edit.setBackground(CARD);
        final JTextField fTemp = tf(6);
        final JTextField fQty = tf("1", 4);
        final JTextField fMax = tf("10", 6);
        final JTextField fOpt = tf("[]", 18);
        JButton bAdd = btn("Them moc");
        bAdd.setBackground(new Color(40, 140, 70)); bAdd.setForeground(Color.WHITE);
        JButton bUpd = btn("Sua dong chon");
        JButton bDel = btn("Xoa dong chon");
        bDel.setBackground(new Color(190, 60, 60)); bDel.setForeground(Color.WHITE);
        edit.add(new JLabel("TempID:")); edit.add(fTemp);
        edit.add(new JLabel("SL:")); edit.add(fQty);
        edit.add(new JLabel("Diem quay can:")); edit.add(fMax);
        edit.add(new JLabel("Option:")); edit.add(fOpt);
        edit.add(bAdd); edit.add(bUpd); edit.add(bDel);
        p.add(edit, BorderLayout.SOUTH);

        JLabel note = new JLabel("<html>Option JSON vd <b>[{\"id\":30,\"param\":1},{\"id\":77,\"param\":50}]</html>");
        note.setBorder(new EmptyBorder(2, 4, 2, 4));
        p.add(note, BorderLayout.NORTH);

        tbTbMoc.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int r = tbTbMoc.getSelectedRow();
            if (r < 0 || r >= tbMocCache.size()) return;
            Map<String, Object> m = tbMocCache.get(r);
            fTemp.setText(String.valueOf(m.get("item_id")));
            fQty.setText(String.valueOf(m.get("quantity")));
            fMax.setText(String.valueOf(m.get("max_value")));
            fOpt.setText(tbStr(m.get("item_options")));
        });
        bAdd.addActionListener(e -> {
            try {
                final int tid = Integer.parseInt(fTemp.getText().trim());
                final int qty = Integer.parseInt(fQty.getText().trim());
                final int mx = Integer.parseInt(fMax.getText().trim());
                final String opt = fOpt.getText().trim();
                bg(() -> { String r = PanelService.addTamBaoMoc(tid, qty, mx, opt);
                    SwingUtilities.invokeLater(() -> { info(r); refreshTbAll(); }); });
            } catch (Exception ex) { err("TempID/SL/Diem phai la so nguyen"); }
        });
        bUpd.addActionListener(e -> {
            int r = tbTbMoc.getSelectedRow();
            if (r < 0) { err("Chon 1 dong trong bang truoc"); return; }
            try {
                final int id = ((Number) mdTbMoc.getValueAt(r, 0)).intValue();
                final int tid = Integer.parseInt(fTemp.getText().trim());
                final int qty = Integer.parseInt(fQty.getText().trim());
                final int mx = Integer.parseInt(fMax.getText().trim());
                final String opt = fOpt.getText().trim();
                bg(() -> { String rs = PanelService.updateTamBaoMoc(id, tid, qty, mx, opt);
                    SwingUtilities.invokeLater(() -> { info(rs); refreshTbAll(); }); });
            } catch (Exception ex) { err("TempID/SL/Diem phai la so nguyen"); }
        });
        bDel.addActionListener(e -> {
            int r = tbTbMoc.getSelectedRow();
            if (r < 0) { err("Chon 1 dong trong bang truoc"); return; }
            final int id = ((Number) mdTbMoc.getValueAt(r, 0)).intValue();
            int c = JOptionPane.showConfirmDialog(this, "Xoa moc thuong id=" + id + "?", "Xac nhan", JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION) return;
            bg(() -> { String s = PanelService.deleteTamBaoMoc(id); SwingUtilities.invokeLater(() -> { info(s); refreshTbAll(); }); });
        });
        return p;
    }

    private void loadTbMocs() {
        bg(() -> {
            java.util.List<Map<String, Object>> list = PanelService.listTamBaoMocs();
            SwingUtilities.invokeLater(() -> {
                tbMocCache = list;
                mdTbMoc.setRowCount(0);
                for (Map<String, Object> m : list) {
                    mdTbMoc.addRow(new Object[]{m.get("id"), m.get("item_id"), tbStr(m.get("item_name")),
                            m.get("quantity"), m.get("max_value"), tbStr(m.get("item_options"))});
                }
            });
        });
    }

    // ---------------- TAB 3: LICH SU QUAY ----------------
    private JPanel buildTbHistTab() {
        JPanel p = new JPanel(new BorderLayout(4, 4));
        p.setBackground(CARD);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        JButton bHist = btn("Tai 100 dong moi nhat");
        bHist.addActionListener(e -> loadTbHist());
        top.add(bHist);
        top.add(new JLabel("Moi dong = 1 luot quay trung (id nhan vat, ten, vat pham JSON, thoi diem)."));
        p.add(top, BorderLayout.NORTH);
        mdTbHist = new DefaultTableModel(new String[]{"ID", "ID nhan vat", "Ten nhan vat", "Vat pham trung (JSON)", "Luc"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tbTbHist = new JTable(mdTbHist);
        tbTbHist.setSelectionMode(0);
        p.add(new JScrollPane(tbTbHist), BorderLayout.CENTER);
        return p;
    }

    private void loadTbHist() {
        bg(() -> {
            java.util.List<Map<String, Object>> list = PanelService.listTamBaoHistory(100);
            SwingUtilities.invokeLater(() -> {
                mdTbHist.setRowCount(0);
                for (Map<String, Object> m : list) {
                    mdTbHist.addRow(new Object[]{m.get("id"), m.get("id_player"), tbStr(m.get("name")),
                            tbStr(m.get("item")), tbStr(m.get("created_at"))});
                }
            });
        });
    }

    // ================= CONSIGN =================
    private DefaultTableModel csModel;
    private JTable csTable;

    private JPanel buildConsign() {
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setBackground(CARD);
        body.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        JButton bReload = btn("Tai lai");
        JButton bDel = btn("Xoa dong chon");
        JButton bClear = btn("Xoa SACH ky gui");
        bClear.setBackground(new Color(220, 60, 70)); bClear.setForeground(Color.WHITE);
        bReload.addActionListener(e -> bg(this::loadConsign));
        bDel.addActionListener(e -> { int r = csTable.getSelectedRow(); if (r < 0) { err("Chon 1 dong"); return; } int id = ((Number) csModel.getValueAt(r, 0)).intValue(); bg(() -> { String s = PanelService.deleteConsign(id); SwingUtilities.invokeLater(() -> { info(s); loadConsign(); }); }); });
        bClear.addActionListener(e -> { int c = JOptionPane.showConfirmDialog(this, "Xoa SACH shop ky gui?", "Xac nhan", JOptionPane.YES_NO_OPTION); if (c != JOptionPane.YES_OPTION) return; bg(() -> { String s = PanelService.clearConsign(); SwingUtilities.invokeLater(() -> { info(s); loadConsign(); }); }); });
        top.add(bReload); top.add(bDel); top.add(bClear);
        body.add(top, BorderLayout.NORTH);
        csModel = new DefaultTableModel(new String[]{"ID", "ItemID", "Nguoi ban", "Tab", "Gold", "Gem", "SL"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        csTable = new JTable(csModel);
        body.add(new JScrollPane(csTable), BorderLayout.CENTER);
        bg(this::loadConsign);
        return wrapPage(body, "Don Rac (Ky Gui)", "Xoa item rac trong shop ky gui");
    }

    private void loadConsign() {
        SwingUtilities.invokeLater(() -> {
            csModel.setRowCount(0);
            try {
                for (models.Consign.ConsignItem it : models.Consign.ConsignShopManager.gI().listItem) {
                    if (it == null) continue;
                    csModel.addRow(new Object[]{it.id, it.itemId, it.player_sell, it.tab, it.goldSell, it.gemSell, it.quantity});
                }
            } catch (Exception e) {}
        });
    }

    // ================= GIFTCODE (sua truc tiep tai o, khong go JSON) =================
    private DefaultTableModel giftModel;
    private JTable giftTable;
    private DefaultTableModel giftItemModel;
    private JTable giftItemTable;
    private java.util.List<Map<String, Object>> giftCache = new java.util.ArrayList<>();
    private java.util.List<GiftItemModel> giftDraft = new java.util.ArrayList<>();
    private int giftDraftId = -1;
    private JLabel lbGiftStatus;
    private boolean giftInlineGuard = false;
    private boolean giftItemGuard = false;

    // ================= TAO RUONG (BOX) =================
    private DefaultTableModel mdChest, mdEntry;
    private JTable tbChest, tbEntry;
    private int curChestId = -1;
    private java.util.List<Map<String, Object>> chestCache = new java.util.ArrayList<>();
    private JComboBox<String> cbChestItem;
    private JComboBox<String> cbChestMode;
    private java.util.List<Integer> cbChestItemIds = new java.util.ArrayList<>();
    private JLabel lbChestInfo, lbRateSum;

    private JPanel buildChests() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(CARD);
        root.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JLabel guide = new JLabel("<html><b>Tao Ruong (Box):</b> moi ruong = 1 item MOI luu vao DB (copy hinh/loai tu item mau). "
                + "<b>Mode 0</b> = chon dung 1 mon theo trong so ty le (tong khong can 100, he thong tu chuan hoa). "
                + "<b>Mode 1</b> = moi mon roll doc lap theo %, co the nhan nhieu mon. Thay doi ap dung NGAY, khong can restart.</html>");
        root.add(guide, BorderLayout.NORTH);

        // TRAI: danh sach ruong hien co
        JPanel left = new JPanel(new BorderLayout(4, 4));
        left.setBackground(CARD);
        left.setBorder(BorderFactory.createTitledBorder("Cac ruong hien co"));
        mdChest = new DefaultTableModel(new String[]{"ID item", "Ten ruong", "Ten item trong game", "So mon", "Mode"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tbChest = new JTable(mdChest);
        tbChest.setSelectionMode(0);
        left.add(new JScrollPane(tbChest), BorderLayout.CENTER);
        JPanel lp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lp.setBackground(CARD);
        JButton bNew = btn("Tao ruong moi");
        bNew.setBackground(new Color(40, 140, 70)); bNew.setForeground(Color.WHITE);
        JButton bCopy = btn("Copy ruong da chon");
        JButton bDel = btn("Xoa ruong");
        JButton bReload = btn("Tai lai");
        lp.add(bNew); lp.add(bCopy); lp.add(bDel); lp.add(bReload);
        left.add(lp, BorderLayout.SOUTH);

        // PHAI: noi dung ruong + them/sua mon
        JPanel right = new JPanel(new BorderLayout(4, 4));
        right.setBackground(CARD);
        lbChestInfo = new JLabel("Chon 1 ruong ben trai de xem noi dung");
        lbChestInfo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        right.add(lbChestInfo, BorderLayout.NORTH);
        mdEntry = new DefaultTableModel(new String[]{"ID dong", "TempID", "Ten vat pham", "SL", "Ty le"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tbEntry = new JTable(mdEntry);
        tbEntry.setSelectionMode(0);
        right.add(new JScrollPane(tbEntry), BorderLayout.CENTER);

        JPanel edit = new JPanel(new GridBagLayout());
        edit.setBackground(CARD);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(3, 3, 3, 3); gc.anchor = GridBagConstraints.WEST; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1;

        final JTextField fFilter = tf(14);
        JButton bFilter = btn("Tim vat pham");
        cbChestItem = new JComboBox<>();
        cbChestItem.setPreferredSize(new Dimension(340, 26));
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row1.setBackground(CARD);
        row1.add(new JLabel("Tim:")); row1.add(fFilter); row1.add(bFilter); row1.add(cbChestItem);

        final JTextField fTemp = tf(6);
        final JTextField fQty = tf("1", 4);
        final JTextField fRate = tf("10", 5);
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row2.setBackground(CARD);
        row2.add(new JLabel("TempID:")); row2.add(fTemp);
        row2.add(new JLabel("SL:")); row2.add(fQty);
        row2.add(new JLabel("Ty le:")); row2.add(fRate);
        JButton bAdd = btn("Them vao ruong");
        bAdd.setBackground(new Color(40, 140, 70)); bAdd.setForeground(Color.WHITE);
        JButton bUpd = btn("Sua dong chon");
        JButton bDelEntry = btn("Xoa dong chon");
        row2.add(bAdd); row2.add(bUpd); row2.add(bDelEntry);

        cbChestMode = new JComboBox<>(new String[]{"0 - Random chon 1 mon (trong so)", "1 - Moi mon roll doc lap (%)"});
        JButton bSaveMode = btn("Luu mode");
        lbRateSum = new JLabel("Tong ty le: -");
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row3.setBackground(CARD);
        row3.add(new JLabel("Mode:")); row3.add(cbChestMode); row3.add(bSaveMode); row3.add(lbRateSum);

        gc.gridx = 0; gc.gridy = 0; edit.add(row1, gc);
        gc.gridy = 1; edit.add(row2, gc);
        gc.gridy = 2; edit.add(row3, gc);
        right.add(edit, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setResizeWeight(0.42);
        split.setDividerLocation(520);
        root.add(split, BorderLayout.CENTER);

        // ===== events =====
        tbChest.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int r = tbChest.getSelectedRow();
            if (r < 0 || r >= chestCache.size()) {
                curChestId = -1;
                lbChestInfo.setText("Chon 1 ruong ben trai de xem noi dung");
                lbRateSum.setText("Tong ty le: -");
                mdEntry.setRowCount(0);
                return;
            }
            Map<String, Object> m = chestCache.get(r);
            curChestId = ((Number) m.get("item_id")).intValue();
            try { cbChestMode.setSelectedIndex(((Number) m.get("mode")).intValue() == 1 ? 1 : 0); } catch (Exception ex) {}
            loadEntries();
        });
        tbEntry.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int r = tbEntry.getSelectedRow();
            if (r < 0) return;
            try {
                fTemp.setText(String.valueOf(mdEntry.getValueAt(r, 1)));
                fQty.setText(String.valueOf(mdEntry.getValueAt(r, 3)));
                fRate.setText(String.valueOf(mdEntry.getValueAt(r, 4)));
            } catch (Exception ex) {}
        });
        bReload.addActionListener(e -> loadChests());
        bFilter.addActionListener(e -> loadItemCombo(fFilter.getText()));
        bNew.addActionListener(e -> showNewChestDialog());
        bCopy.addActionListener(e -> {
            if (curChestId < 0) { err("Chon 1 ruong ben trai"); return; }
            String n = JOptionPane.showInputDialog(this, "Ten ruong MOI (copy tu ruong id=" + curChestId + "):", "Ruong copy");
            if (n == null || n.trim().isEmpty()) return;
            final String nm = n.trim();
            final int src = curChestId;
            bg(() -> { String r = PanelService.copyChest(src, nm); SwingUtilities.invokeLater(() -> { info(r); loadChests(); }); });
        });
        bDel.addActionListener(e -> {
            if (curChestId < 0) { err("Chon 1 ruong"); return; }
            int c = JOptionPane.showConfirmDialog(this, "Xoa ruong id=" + curChestId + "? (giu lai item_template)", "Xac nhan", JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION) return;
            final int id = curChestId;
            bg(() -> { String r = PanelService.deleteChest(id); SwingUtilities.invokeLater(() -> { info(r); curChestId = -1; loadChests(); }); });
        });
        cbChestItem.addActionListener(e -> {
            int i = cbChestItem.getSelectedIndex();
            if (i >= 0 && i < cbChestItemIds.size()) fTemp.setText(String.valueOf(cbChestItemIds.get(i)));
        });
        bAdd.addActionListener(e -> {
            if (curChestId < 0) { err("Chon ruong ben trai truoc"); return; }
            try {
                final int tid = Integer.parseInt(fTemp.getText().trim());
                final int qty = Integer.parseInt(fQty.getText().trim());
                final double rate = Double.parseDouble(fRate.getText().trim().replace(",", "."));
                final int cid = curChestId;
                bg(() -> { String r = PanelService.addChestEntry(cid, tid, qty, rate); SwingUtilities.invokeLater(() -> { info(r); loadChests(); }); });
            } catch (Exception ex) { err("TempID/SL/Ty le phai la so"); }
        });
        bUpd.addActionListener(e -> {
            int r = tbEntry.getSelectedRow();
            if (r < 0 || curChestId < 0) { err("Chon 1 dong trong bang noi dung"); return; }
            try {
                final int id = ((Number) mdEntry.getValueAt(r, 0)).intValue();
                final int qty = Integer.parseInt(fQty.getText().trim());
                final double rate = Double.parseDouble(fRate.getText().trim().replace(",", "."));
                bg(() -> { String s = PanelService.updateChestEntry(id, qty, rate); SwingUtilities.invokeLater(() -> { info(s); loadChests(); }); });
            } catch (Exception ex) { err("SL/Ty le phai la so"); }
        });
        bDelEntry.addActionListener(e -> {
            int r = tbEntry.getSelectedRow();
            if (r < 0) { err("Chon 1 dong trong bang noi dung"); return; }
            final int id = ((Number) mdEntry.getValueAt(r, 0)).intValue();
            int c = JOptionPane.showConfirmDialog(this, "Xoa mon id=" + id + " khoi ruong?", "Xac nhan", JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION) return;
            bg(() -> { String s = PanelService.deleteChestEntry(id); SwingUtilities.invokeLater(() -> { info(s); loadChests(); }); });
        });
        bSaveMode.addActionListener(e -> {
            if (curChestId < 0) { err("Chon ruong ben trai truoc"); return; }
            final int cid = curChestId;
            final int mode = cbChestMode.getSelectedIndex();
            bg(() -> { String s = PanelService.setChestMode(cid, mode); SwingUtilities.invokeLater(() -> { info(s); loadChests(); }); });
        });

        loadChests();
        loadItemCombo("");
        return root;
    }

    private void loadChests() {
        bg(() -> {
            java.util.List<Map<String, Object>> list = PanelService.listChests();
            SwingUtilities.invokeLater(() -> {
                chestCache = list;
                mdChest.setRowCount(0);
                for (Map<String, Object> m : list) {
                    int mode = ((Number) m.get("mode")).intValue();
                    mdChest.addRow(new Object[]{m.get("item_id"), m.get("name"), m.get("item_name"),
                            m.get("n"), mode == 1 ? "1 - roll doc lap" : "0 - chon 1 mon"});
                }
                if (curChestId >= 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (((Number) list.get(i).get("item_id")).intValue() == curChestId) {
                            tbChest.setRowSelectionInterval(i, i);
                            break;
                        }
                    }
                }
            });
        });
    }

    private void loadEntries() {
        if (curChestId < 0) return;
        final int id = curChestId;
        bg(() -> {
            java.util.List<Map<String, Object>> list = PanelService.listChestEntries(id);
            SwingUtilities.invokeLater(() -> {
                if (curChestId != id) return;
                mdEntry.setRowCount(0);
                double sum = 0;
                for (Map<String, Object> m : list) {
                    mdEntry.addRow(new Object[]{m.get("id"), m.get("temp_id"), m.get("item_name"),
                            m.get("qty"), m.get("rate")});
                    sum += ((Number) m.get("rate")).doubleValue();
                }
                lbChestInfo.setText("Ruong id=" + id + " - " + mdEntry.getRowCount()
                        + " mon (chon 1 dong de sua SL/ty le, hoac nhap moi roi bam Them)");
                lbRateSum.setText("Tong ty le: " + sum + (cbChestMode.getSelectedIndex() == 0 ? " (tu chuan hoa)" : ""));
            });
        });
    }

    private void loadItemCombo(String filter) {
        bg(() -> {
            java.util.List<Map<String, Object>> list = PanelService.listItemTemplatesFull(filter, -1, -1, 500);
            SwingUtilities.invokeLater(() -> {
                cbChestItem.removeAllItems();
                cbChestItemIds.clear();
                for (Map<String, Object> m : list) {
                    int id = ((Number) m.get("id")).intValue();
                    cbChestItemIds.add(id);
                    cbChestItem.addItem(id + " - " + m.get("name"));
                }
            });
        });
    }

    private void showNewChestDialog() {
        JDialog d = new JDialog(this, "Tao ruong moi", true);
        d.setSize(500, 240);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout(8, 8));
        JPanel f = new JPanel(new GridBagLayout());
        f.setBackground(CARD);
        f.setBorder(new EmptyBorder(12, 12, 12, 12));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4); gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1;
        JTextField fName = tf(24);
        JTextField fSrc = tf("1413", 8);
        addRow(f, gc, 0, "Ten ruong:", fName);
        addRow(f, gc, 1, "Item mau (id):", fSrc);
        addRow(f, gc, 2, "Ghi chu:", new JLabel("1413 = Ruong Than Bi 1Sao, 1735 = Hop Nguyen Thuy... (copy hinh + loai)"));
        addRow(f, gc, 3, "Ket qua:", new JLabel("Tao ra 1 item MOI id = max+1, luu vao DB, dung ngay khong restart."));
        d.add(f, BorderLayout.CENTER);
        JPanel acts = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acts.setBackground(CARD);
        JButton bOk = btn("Tao ruong");
        bOk.setBackground(new Color(40, 140, 70)); bOk.setForeground(Color.WHITE);
        JButton bCancel = btn("Huy");
        bOk.addActionListener(e -> {
            String nm = fName.getText().trim();
            if (nm.isEmpty()) { err("Nhap ten ruong"); return; }
            int src = 1413;
            try { src = Integer.parseInt(fSrc.getText().trim()); } catch (Exception ex) {}
            final String fnm = nm; final int fsrc = src;
            bg(() -> { String r = PanelService.createChest(fnm, fsrc); SwingUtilities.invokeLater(() -> { info(r); d.dispose(); loadChests(); }); });
        });
        bCancel.addActionListener(e -> d.dispose());
        acts.add(bOk); acts.add(bCancel);
        d.add(acts, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private JPanel buildGiftcode() {
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setBackground(CARD);
        body.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        JTextField fCode = tf(12); JTextField fCount = tf("100", 6); JTextField fDays = tf("30", 5);
        JButton bCreate = btn("Tao code");
        bCreate.setBackground(new Color(40, 150, 80)); bCreate.setForeground(Color.WHITE);
        JButton bReload = btn("Tai lai");
        JButton bDel = btn("Xoa code");
        JTextField fAdd = tf("100", 6);
        JButton bAdd = btn("Cong luot");
        bCreate.addActionListener(e -> bg(() -> {
            String code = fCode.getText().trim();
            int cnt = 100, days = 30;
            try { cnt = Integer.parseInt(fCount.getText().trim()); } catch (Exception ex) {}
            try { days = Integer.parseInt(fDays.getText().trim()); } catch (Exception ex) {}
            if (code.isEmpty()) { SwingUtilities.invokeLater(() -> err("Nhap ten code")); return; }
            String r = PanelService.createGiftcode(code, cnt, days, "[]");
            SwingUtilities.invokeLater(() -> { info(r); loadGift(); });
        }));
        bReload.addActionListener(e -> bg(() -> { String r = PanelService.reloadGiftcode(); SwingUtilities.invokeLater(() -> { loadGift(); }); }));
        bDel.addActionListener(e -> {
            int r = giftTable.getSelectedRow();
            if (r < 0) { err("Chon 1 code"); return; }
            int id = ((Number) giftModel.getValueAt(r, 0)).intValue();
            int c = JOptionPane.showConfirmDialog(this, "Xoa code id=" + id + "?", "Xac nhan", JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION) return;
            bg(() -> { String s = PanelService.deleteGiftcode(id); SwingUtilities.invokeLater(() -> { info(s); loadGift(); }); });
        });
        bAdd.addActionListener(e -> {
            int r = giftTable.getSelectedRow();
            if (r < 0) { err("Chon 1 code"); return; }
            int id = ((Number) giftModel.getValueAt(r, 0)).intValue();
            bg(() -> {
                String s;
                try { s = PanelService.addGiftCount(id, Integer.parseInt(fAdd.getText().trim())); }
                catch (Exception ex) { s = "Loi: " + ex.getMessage(); }
                final String ss = s;
                SwingUtilities.invokeLater(() -> { info(ss); loadGift(); });
            });
        });
        top.add(new JLabel("Code:")); top.add(fCode);
        top.add(new JLabel("Luot:")); top.add(fCount);
        top.add(new JLabel("Het han (ngay):")); top.add(fDays);
        top.add(bCreate); top.add(bReload); top.add(bDel);
        top.add(new JLabel("Cong:")); top.add(fAdd); top.add(bAdd);
        body.add(top, BorderLayout.NORTH);
        // Bang code: click vao o Code / Con lai / Het han la go sua luon, Enter la tu luu
        giftModel = new DefaultTableModel(new String[]{"ID", "Code (go sua)", "Con lai (go so)", "Het han (yyyy-MM-dd HH:mm:ss)", "So mon", "Tom tat qua"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 1 || c == 2 || c == 3; }
            @Override public void setValueAt(Object v, int r, int c) {
                if (giftInlineGuard) { super.setValueAt(v, r, c); return; }
                try {
                    if (r < 0 || r >= giftCache.size()) return;
                    Map<String, Object> row = giftCache.get(r);
                    int id = ((Number) row.get("id")).intValue();
                    String code = String.valueOf(row.get("code"));
                    int cnt = ((Number) row.get("count")).intValue();
                    String exp = String.valueOf(row.get("expired_str"));
                    if (c == 1) {
                        code = String.valueOf(v).trim();
                        if (code.isEmpty()) { err("Code khong rong"); return; }
                    } else if (c == 2) {
                        cnt = Integer.parseInt(String.valueOf(v).trim().replaceAll("[^0-9-]", ""));
                        if (cnt < -1) cnt = -1;
                    } else if (c == 3) {
                        exp = String.valueOf(v).trim();
                    } else { super.setValueAt(v, r, c); return; }
                    String detail = String.valueOf(row.get("detail"));
                    java.util.List<GiftItemModel> items = PanelService.parseGiftDetail(detail == null ? "[]" : detail);
                    final String fCode2 = code; final int fCnt = cnt; final String fExp = exp;
                    final java.util.List<GiftItemModel> fItems = items;
                    giftInlineGuard = true;
                    try { super.setValueAt(v, r, c); } finally { giftInlineGuard = false; }
                    bg(() -> {
                        String rs = PanelService.updateGiftcodeFull(id, fCode2, fCnt, fExp, fItems);
                        SwingUtilities.invokeLater(() -> {
                            if (!rs.startsWith("OK")) err(rs);
                            else { row.put("code", fCode2); row.put("count", fCnt); if (giftDraftId == id) openGiftDraft(id, false); }
                        });
                    });
                } catch (Exception ex) { giftInlineGuard = false; err("Loi: " + ex.getMessage()); }
            }
        };
        giftTable = new JTable(giftModel);
        giftTable.setRowHeight(24);
        giftTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        giftTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int r = giftTable.getSelectedRow();
            if (r < 0 || r >= giftCache.size()) return;
            int id = ((Number) giftCache.get(r).get("id")).intValue();
            openGiftDraft(id, true);
        });
        JScrollPane spGift = new JScrollPane(giftTable);
        spGift.setBorder(BorderFactory.createTitledBorder("1. Danh sach code (click vao o Code / Con lai / Het han de sua truc tiep, Enter la tu luu)"));
        spGift.setPreferredSize(new Dimension(900, 260));
        // Bang item cua code dang chon: SL go truc tiep, Ten + Option nhap doi de chon
        giftItemModel = new DefaultTableModel(new String[]{"TempID", "Ten qua (nhap doi de chon: ao, kiem...)", "SL (go so)", "Option (nhap doi de sua)"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 2; }
            @Override public void setValueAt(Object v, int r, int c) {
                if (giftItemGuard) { super.setValueAt(v, r, c); return; }
                try {
                    if (r < 0 || r >= giftDraft.size() || giftDraftId < 0) return;
                    if (c != 2) { super.setValueAt(v, r, c); return; }
                    int q = Integer.parseInt(String.valueOf(v).trim().replaceAll("[^0-9-]", ""));
                    if (q <= 0) { err("So luong > 0"); return; }
                    giftDraft.get(r).quantity = q;
                    giftItemGuard = true;
                    try {
                        super.setValueAt(String.valueOf(q), r, c);
                        super.setValueAt(PanelService.giftItemSummaryVN(giftDraft.get(r)), r, 3);
                    } finally { giftItemGuard = false; }
                    saveGiftDraftSilent();
                } catch (Exception ex) { giftItemGuard = false; err("Loi: " + ex.getMessage()); }
            }
        };
        giftItemTable = new JTable(giftItemModel);
        giftItemTable.setRowHeight(24);
        giftItemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        giftItemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() != 2) return;
                int r = giftItemTable.getSelectedRow();
                int c = giftItemTable.getSelectedColumn();
                if (r < 0 || r >= giftDraft.size()) return;
                if (c == 1) {
                    Integer pick = openTemplatePicker();
                    if (pick == null) {
                        // cho chon tien te nhanh
                        String[] opts = {"Vang (-1)", "Ngoc (-2)", "Ngoc khoa (-3)"};
                        int ch = JOptionPane.showOptionDialog(ControlPanel.this, "Khong chon vat pham? Chon tien te nhanh:", "Tien te", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opts, opts[0]);
                        if (ch == 0) pick = -1; else if (ch == 1) pick = -2; else if (ch == 2) pick = -3;
                        else return;
                    }
                    GiftItemModel m = giftDraft.get(r);
                    m.tempId = pick;
                    String sp = GiftItemModel.specialName(pick);
                    if (sp != null) m.itemName = sp;
                    else { models.Template.ItemTemplate t = PanelService.getTemplateById(pick); if (t != null) m.itemName = t.name; }
                    refreshGiftDraftTable();
                    saveGiftDraftSilent();
                } else if (c == 3) {
                    openGiftOptionInlineEditor(giftDraft.get(r), r);
                }
            }
        });
        JScrollPane spGiftItem = new JScrollPane(giftItemTable);
        spGiftItem.setBorder(BorderFactory.createTitledBorder("2. Qua trong code (SL go truc tiep; Ten + Option nhap doi de chon/sua, tu luu)"));
        JSplitPane splitG = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spGift, spGiftItem);
        splitG.setResizeWeight(0.55);
        body.add(splitG, BorderLayout.CENTER);
        JPanel bot = new JPanel(new BorderLayout());
        bot.setBackground(CARD);
        JPanel rowB = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rowB.setBackground(CARD);
        JButton bAddItem = btn("Them mon");
        JButton bDelItem = btn("Xoa mon");
        JButton bSave = btn("Luu ngay");
        bSave.setBackground(new Color(40, 150, 80)); bSave.setForeground(Color.WHITE);
        bAddItem.addActionListener(e -> {
            if (giftDraftId < 0) { err("Chon 1 code o tren truoc"); return; }
            Integer pick = openTemplatePicker();
            int tid = 14;
            String nm = "";
            if (pick != null) {
                tid = pick;
                models.Template.ItemTemplate t = PanelService.getTemplateById(pick);
                if (t != null) nm = t.name;
            } else {
                String s = JOptionPane.showInputDialog(this, "Nhap temp_id (vd 14=Ngoc rong, -1=Vang, -2=Ngoc, -3=Ngoc khoa):", "14");
                if (s == null) return;
                try { tid = Integer.parseInt(s.trim()); } catch (Exception ex) { err("temp_id so"); return; }
                String sp = GiftItemModel.specialName(tid);
                if (sp != null) nm = sp;
                else { models.Template.ItemTemplate t = PanelService.getTemplateById(tid); if (t != null) nm = t.name; }
            }
            GiftItemModel m = new GiftItemModel();
            m.tempId = tid; m.itemName = nm; m.quantity = 1;
            giftDraft.add(m);
            refreshGiftDraftTable();
            saveGiftDraftSilent();
        });
        bDelItem.addActionListener(e -> {
            int r = giftItemTable.getSelectedRow();
            if (r < 0 || r >= giftDraft.size()) { err("Chon 1 mon"); return; }
            giftDraft.remove(r);
            refreshGiftDraftTable();
            saveGiftDraftSilent();
        });
        bSave.addActionListener(e -> saveGiftDraftLoud());
        rowB.add(bAddItem); rowB.add(bDelItem); rowB.add(bSave);
        lbGiftStatus = new JLabel("Chon 1 code o tren de xem/sua qua. Sua o nao luu o do, khong go JSON.");
        lbGiftStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbGiftStatus.setForeground(new Color(90, 100, 130));
        bot.add(rowB, BorderLayout.NORTH);
        bot.add(lbGiftStatus, BorderLayout.SOUTH);
        body.add(bot, BorderLayout.SOUTH);
        bg(this::loadGift);
        return wrapPage(body, "Quan Ly Giftcode", "Click o de sua Code/SL/Het han truc tiep - Ten/Option nhap doi de chon - Tu sinh JSON + goi API cu");
    }

    private void loadGift() {
        java.util.List<Map<String, Object>> list = PanelService.listGiftcodeFull();
        giftCache = list;
        SwingUtilities.invokeLater(() -> {
            giftModel.setRowCount(0);
            for (Map<String, Object> m : list) {
                giftModel.addRow(new Object[]{m.get("id"), m.get("code"), m.get("count"), m.get("expired_str"), m.get("n_item"), m.get("summary")});
            }
            if (!list.isEmpty()) {
                int sel = giftTable.getSelectedRow();
                if (sel < 0 || sel >= list.size()) { giftTable.setRowSelectionInterval(0, 0); sel = 0; }
                int id = ((Number) list.get(sel).get("id")).intValue();
                openGiftDraft(id, true);
            } else { giftDraft = new java.util.ArrayList<>(); giftDraftId = -1; giftItemModel.setRowCount(0); }
        });
    }

    private void openGiftDraft(int giftId, boolean refreshTable) {
        bg(() -> {
            String detail = "[]";
            for (Map<String, Object> m : giftCache) {
                if (((Number) m.get("id")).intValue() == giftId) { Object d = m.get("detail"); if (d != null) detail = String.valueOf(d); break; }
            }
            java.util.List<GiftItemModel> items = PanelService.parseGiftDetail(detail);
            SwingUtilities.invokeLater(() -> {
                giftDraft = items; giftDraftId = giftId;
                giftItemModel.setRowCount(0);
                for (GiftItemModel m : items) {
                    giftItemModel.addRow(new Object[]{m.tempId, m.itemName, String.valueOf(m.quantity), PanelService.giftItemSummaryVN(m)});
                }
                if (lbGiftStatus != null) lbGiftStatus.setText("Dang sua code id=" + giftId + " (" + items.size() + " mon). Sua o nao tu luu o do.");
            });
        });
    }

    private void refreshGiftDraftTable() {
        giftItemModel.setRowCount(0);
        for (GiftItemModel m : giftDraft) {
            giftItemModel.addRow(new Object[]{m.tempId, m.itemName, String.valueOf(m.quantity), PanelService.giftItemSummaryVN(m)});
        }
    }

    private void saveGiftDraftSilent() {
        if (giftDraftId < 0) return;
        Map<String, Object> row = null;
        for (Map<String, Object> m : giftCache) if (((Number) m.get("id")).intValue() == giftDraftId) { row = m; break; }
        if (row == null) return;
        String code = String.valueOf(row.get("code"));
        int cnt = ((Number) row.get("count")).intValue();
        String exp = String.valueOf(row.get("expired_str"));
        final Map<String, Object> fRow = row;
        java.util.List<GiftItemModel> copy = new java.util.ArrayList<>();
        for (GiftItemModel m : giftDraft) copy.add(m.copy());
        bg(() -> {
            String r = PanelService.updateGiftcodeFull(giftDraftId, code, cnt, exp, copy);
            SwingUtilities.invokeLater(() -> {
                if (!r.startsWith("OK")) err(r);
                else {
                    fRow.put("detail", PanelService.buildGiftDetailJson(copy));
                    fRow.put("n_item", copy.size());
                    fRow.put("summary", PanelService.giftSummaryVN(copy));
                    int sel = giftTable.getSelectedRow();
                    if (sel >= 0 && sel < giftCache.size() && ((Number) giftCache.get(sel).get("id")).intValue() == giftDraftId) {
                        giftInlineGuard = true;
                        try {
                            giftModel.setValueAt(copy.size(), sel, 4);
                            giftModel.setValueAt(fRow.get("summary"), sel, 5);
                        } finally { giftInlineGuard = false; }
                    }
                    if (lbGiftStatus != null) lbGiftStatus.setText("Da tu luu code id=" + giftDraftId + " (" + copy.size() + " mon).");
                }
            });
        });
    }

    private void saveGiftDraftLoud() {
        if (giftDraftId < 0) { err("Chon 1 code"); return; }
        saveGiftDraftSilent();
        info("Da gui lenh luu code id=" + giftDraftId);
    }

    private void openGiftOptionInlineEditor(GiftItemModel m, int row) {
        JDialog d = new JDialog(this, "Option: " + (m.itemName == null ? ("#" + m.tempId) : m.itemName), true);
        d.setSize(640, 460);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());
        DefaultTableModel om = new DefaultTableModel(new String[]{"Option (chon dropdown)", "Param (go so)", "Mo ta"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 0 || c == 1; }
        };
        JTable ot = new JTable(om);
        ot.setRowHeight(24);
        final boolean[] og = new boolean[]{false};
        Runnable reload = () -> {
            og[0] = true;
            try {
                om.setRowCount(0);
                for (GiftItemModel.Opt o : m.options) om.addRow(new Object[]{o.id, String.valueOf(o.param), PanelService.optionDisplay(o.id)});
            } finally { og[0] = false; }
        };
        reload.run();
        JComboBox<String> cbOpt = new JComboBox<>();
        cbOpt.setPrototypeDisplayValue("#000 Sao pha le x99 | Ten dai........");
        cbOpt.setPreferredSize(new java.awt.Dimension(380, 25));
        cbOpt.setMaximumRowCount(20);
        java.util.List<Integer> ids = new java.util.ArrayList<>();
        if (optDictCache == null || optDictCache.isEmpty()) optDictCache = PanelService.listOptionDict();
        for (Map<String, Object> o : optDictCache) {
            int id = ((Number) o.get("id")).intValue();
            ids.add(id);
            String tv = o.get("ten_viet") == null ? "" : String.valueOf(o.get("ten_viet"));
            String nm = o.get("name") == null ? "" : String.valueOf(o.get("name"));
            cbOpt.addItem("#" + id + " " + (tv.isEmpty() ? nm : tv));
        }
        ot.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(cbOpt) {
            @Override public Object getCellEditorValue() {
                int i = cbOpt.getSelectedIndex();
                return (i >= 0 && i < ids.size()) ? ids.get(i) : super.getCellEditorValue();
            }
        });
        om.addTableModelListener(e -> {
            if (og[0]) return;
            if (e.getType() != javax.swing.event.TableModelEvent.UPDATE) return;
            if (e.getColumn() == 2) return;
            int r = e.getFirstRow();
            if (r < 0 || r >= m.options.size()) return;
            try {
                og[0] = true;
                Object ov = om.getValueAt(r, 0);
                int oid = ov instanceof Number ? ((Number) ov).intValue() : Integer.parseInt(String.valueOf(ov).replaceAll("[^0-9-]", ""));
                long pv = Long.parseLong(String.valueOf(om.getValueAt(r, 1)).trim().replaceAll("[^0-9-]", ""));
                m.options.get(r).id = oid;
                m.options.get(r).param = pv;
                om.setValueAt(PanelService.optionDisplay(oid), r, 2);
            } catch (Exception ex) {} finally { og[0] = false; }
        });
        JPanel pick = new JPanel(new BorderLayout(4, 4));
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Option:")); row1.add(cbOpt);
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField fP = new JTextField("0", 12);
        JButton bAddO = btn("Them dong");
        bAddO.setBackground(new Color(70, 120, 220)); bAddO.setForeground(Color.WHITE);
        JButton bDelO = btn("Xoa dong");
        row2.add(new JLabel("Param:")); row2.add(fP);
        row2.add(bAddO); row2.add(bDelO);
        JLabel hint = new JLabel("Chon Option -> nhap Param -> bam 'Them dong' (bang duoi co dong moi luu duoc).");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        hint.setForeground(new Color(90, 100, 130));
        pick.add(row1, BorderLayout.NORTH);
        pick.add(row2, BorderLayout.CENTER);
        pick.add(hint, BorderLayout.SOUTH);
        bAddO.addActionListener(e -> {
            int idx = cbOpt.getSelectedIndex();
            int oid = (idx >= 0 && idx < ids.size()) ? ids.get(idx) : 50;
            long pv = 0;
            try { pv = Long.parseLong(fP.getText().trim().replaceAll("[^0-9-]", "")); } catch (Exception ex) {}
            String er = optParamErrById(oid, pv);
            if (er != null) { err(er); return; }
            m.options.add(new GiftItemModel.Opt(oid, pv));
            reload.run();
        });
        bDelO.addActionListener(e -> { int r = ot.getSelectedRow(); if (r >= 0 && r < m.options.size()) { m.options.remove(r); reload.run(); } });
        d.add(pick, BorderLayout.NORTH);
        d.add(new JScrollPane(ot), BorderLayout.CENTER);
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bOk = btn("Luu option (tu luu code)");
        bOk.setBackground(new Color(40, 150, 80)); bOk.setForeground(Color.WHITE);
        bOk.addActionListener(e -> {
            try { if (ot.isEditing()) ot.getCellEditor().stopCellEditing(); } catch (Exception ex) {}
            if (m.options.isEmpty() && !ids.isEmpty()) {
                try {
                    int idx = cbOpt.getSelectedIndex();
                    int oid = (idx >= 0 && idx < ids.size()) ? ids.get(idx) : ids.get(0);
                    long pv = 0;
                    try { pv = Long.parseLong(fP.getText().trim().replaceAll("[^0-9-]", "")); } catch (Exception ex) {}
                    m.options.add(new GiftItemModel.Opt(oid, pv));
                } catch (Exception ex) {}
            }
            d.dispose(); refreshGiftDraftTable(); saveGiftDraftSilent();
        });
        bot.add(bOk);
        d.add(bot, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    // ================= PHUC LOI (phan tang 3 buoc: TAB -> MOC -> QUA) =================
    private JPanel plCards;
    private CardLayout plCardLayout;
    // Tang 1: danh sach tab
    private DefaultTableModel plTabModel;
    private JTable plTabTable;
    private java.util.List<Map<String, Object>> plTabCache = new java.util.ArrayList<>();
    // Tang 2: moc trong tab da chon
    private DefaultTableModel plMocModel;
    private JTable plMocTable;
    private java.util.List<Map<String, Object>> plMocCache = new java.util.ArrayList<>();
    private int plSelectedTabId = -1;
    private String plSelectedTabName = "";
    private String plSelectedTabTien = "Coin";
    private JLabel plMocTitle;
    // Tang 3: sua qua trong 1 moc
    private DefaultTableModel plItemModel;
    private JTable plItemTable;
    private java.util.List<GiftItemModel> plItemDraft = new java.util.ArrayList<>();
    private java.util.List<GiftItemModel> plItemOriginal = new java.util.ArrayList<>();
    private int plSelectedMocId = -1;
    private String plSelectedMocName = "";
    private JLabel plItemTitle;
    private JLabel lbPlStatus;
    private boolean plGuard = false;

    // ================= SET 5 MON (bang set_config) =================
    private DefaultTableModel setCfgModel;
    private JTable setCfgTable;
    private java.util.List<Map<String, Object>> setCfgCache = new java.util.ArrayList<>();

    private long parseLong0(String s) {
        try {
            String t = String.valueOf(s).trim().replaceAll("[^0-9-]", "");
            return t.isEmpty() ? 0L : Long.parseLong(t);
        } catch (Exception e) { return 0L; }
    }

    private int parseInt0(String s) { return (int) parseLong0(s); }

    private String numStr(Map<String, Object> old, String key) {
        return old == null ? "0" : String.valueOf(old.get(key));
    }

    private JPanel setRow(String label, JComponent... comps) {
        JPanel r = new JPanel(new FlowLayout(FlowLayout.LEFT));
        r.setBackground(CARD);
        r.add(new JLabel(label));
        for (JComponent c : comps) { r.add(c); }
        return r;
    }

    private String fmtBonus(Object base, Object pct) {
        long b = ((Number) base).longValue();
        int p = ((Number) pct).intValue();
        StringBuilder sb = new StringBuilder();
        if (b != 0) { sb.append("+").append(b).append(" "); }
        if (p != 0) { sb.append("+").append(p).append("%"); }
        String s = sb.toString().trim();
        return s.isEmpty() ? "-" : s;
    }

    private void loadSetCfgs() {
        java.util.List<Map<String, Object>> list = PanelService.listSetConfigs();
        setCfgCache = list;
        SwingUtilities.invokeLater(() -> {
            setCfgModel.setRowCount(0);
            for (Map<String, Object> m : list) {
                setCfgModel.addRow(new Object[]{
                    m.get("id"), m.get("name"), m.get("option_id"),
                    fmtBonus(m.get("hp_base"), m.get("hp_pct")),
                    fmtBonus(m.get("ki_base"), m.get("ki_pct")),
                    fmtBonus(m.get("dame_base"), m.get("dame_pct")),
                    fmtBonus(m.get("def_base"), m.get("def_pct")),
                    m.get("crit_add"), m.get("crit_dmg_pct"),
                    String.valueOf(m.get("skill_dmg")),
                    ((Number) m.get("active")).intValue() != 0 ? "Bat" : "Tat"
                });
            }
        });
    }

    private JPanel buildSet5Mon() {
        JPanel page = new JPanel(new BorderLayout(8, 8));
        page.setBackground(CARD);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        JButton bReload = btn("Tai lai");
        JButton bAdd = btn("Them set");
        JButton bEdit = btn("Sua set da chon");
        JButton bDel = btn("Xoa set da chon");
        bReload.addActionListener(e -> bg(this::loadSetCfgs));
        bAdd.addActionListener(e -> editSetCfgDialog(0));
        bEdit.addActionListener(e -> {
            int r = setCfgTable.getSelectedRow();
            if (r < 0 || r >= setCfgCache.size()) { err("Chon 1 set truoc (bam vao 1 dong)"); return; }
            editSetCfgDialog(((Number) setCfgCache.get(r).get("id")).intValue());
        });
        bDel.addActionListener(e -> {
            int r = setCfgTable.getSelectedRow();
            if (r < 0 || r >= setCfgCache.size()) { err("Chon 1 set truoc"); return; }
            final int id = ((Number) setCfgCache.get(r).get("id")).intValue();
            final String nm = String.valueOf(setCfgCache.get(r).get("name"));
            int c = JOptionPane.showConfirmDialog(this, "Xoa set [" + id + "] " + nm + "?", "Xac nhan", JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION) { return; }
            bg(() -> { String s = PanelService.deleteSetConfig(id); SwingUtilities.invokeLater(() -> { info(s); loadSetCfgs(); }); });
        });
        top.add(bReload); top.add(bAdd); top.add(bEdit); top.add(bDel);
        page.add(top, BorderLayout.NORTH);
        setCfgModel = new DefaultTableModel(new String[]{"ID", "Ten set", "Option id", "HP", "KI", "Dame", "DEF", "+Chi mang", "STCM %", "Skill dmg (id:%,...)", "Trang thai"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        setCfgTable = new JTable(setCfgModel);
        setCfgTable.setRowHeight(26);
        setCfgTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCfgTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int r = setCfgTable.getSelectedRow();
                    if (r >= 0 && r < setCfgCache.size()) { editSetCfgDialog(((Number) setCfgCache.get(r).get("id")).intValue()); }
                }
            }
        });
        JScrollPane sp = new JScrollPane(setCfgTable);
        sp.setBorder(BorderFactory.createTitledBorder("Set chi kich hoat khi CA 5 MON dau (ao, quan, gang, giay, nhan) deu mang option id - nhan dup de sua"));
        page.add(sp, BorderLayout.CENTER);
        bg(this::loadSetCfgs);
        return wrapPage(page, "Set Do 5 Mon (option)", "Moi set = 1 option id tren do. Chi tinh 5 mon dau tien; du 5/5 mon mang option moi cong bonus. Them/sua xong server tu nap lai, khong can restart.");
    }

    private void editSetCfgDialog(final int id) {
        Map<String, Object> old = null;
        if (id > 0) {
            for (Map<String, Object> m : setCfgCache) {
                if (((Number) m.get("id")).intValue() == id) { old = m; break; }
            }
            if (old == null) { err("Khong tim thay set id=" + id); return; }
        }
        JTextField fTen = new JTextField(old == null ? "Set Moi" : String.valueOf(old.get("name")), 18);
        JTextField fOpt = new JTextField(old == null ? "0" : String.valueOf(old.get("option_id")), 8);
        JCheckBox fActive = new JCheckBox("Bat (active)", old == null || ((Number) old.get("active")).intValue() != 0);
        JTextField fHpB = new JTextField(numStr(old, "hp_base"), 9);
        JTextField fHpP = new JTextField(numStr(old, "hp_pct"), 6);
        JTextField fKiB = new JTextField(numStr(old, "ki_base"), 9);
        JTextField fKiP = new JTextField(numStr(old, "ki_pct"), 6);
        JTextField fDmB = new JTextField(numStr(old, "dame_base"), 9);
        JTextField fDmP = new JTextField(numStr(old, "dame_pct"), 6);
        JTextField fDfB = new JTextField(numStr(old, "def_base"), 9);
        JTextField fDfP = new JTextField(numStr(old, "def_pct"), 6);
        JTextField fCrit = new JTextField(numStr(old, "crit_add"), 6);
        JTextField fSdcm = new JTextField(numStr(old, "crit_dmg_pct"), 6);
        JTextField fSkill = new JTextField(old == null ? "" : String.valueOf(old.get("skill_dmg")), 18);
        JPanel form = new JPanel(new java.awt.GridLayout(0, 1, 3, 3));
        form.add(setRow("Ten set:", fTen));
        form.add(setRow("Option id (tren do):", fOpt));
        form.add(setRow("Bonus:", fActive));
        form.add(setRow("HP base / %:", fHpB, fHpP));
        form.add(setRow("KI base / %:", fKiB, fKiP));
        form.add(setRow("Dame base / %:", fDmB, fDmP));
        form.add(setRow("DEF base / %:", fDfB, fDfP));
        form.add(setRow("Them chi mang:", fCrit));
        form.add(setRow("STCM % (sat thuong chi mang):", fSdcm));
        form.add(setRow("Skill dmg:", fSkill));
        JLabel hint = new JLabel("<html>Skill dmg: <b>&lt;id skill&gt;:&lt;%&gt;</b> nhieu thi phay dau, vd <b>1:100,4:80</b><br>"
                + "Id skill: 0 Dragon, 1 Kame, 2 Demon, 3 Masenko, 4 Galick, 5 Antomic, 9 Kaioken, 10 QCKK, 11 Makan, 12 De Trung, 17 Lien Hoan, 24 Super Kame<br>"
                + "Base = so nguyen cong them; % = cong theo % hien tai cua chi so. De 0 = khong dung. Kich hoat khi du 5/5 mon dau.</html>");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        form.add(hint);
        int c = JOptionPane.showConfirmDialog(this, form, id > 0 ? ("Sua set [" + id + "]") : "Them set moi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (c != JOptionPane.OK_OPTION) { return; }
        final String ten = fTen.getText().trim();
        if (ten.isEmpty()) { err("Ten set khong duoc rong"); return; }
        final int optV = parseInt0(fOpt.getText());
        if (optV <= 0) { err("Option id phai la so > 0 (id option tren 5 mon)"); return; }
        final int activeV = fActive.isSelected() ? 1 : 0;
        final long hpBv = parseLong0(fHpB.getText()); final int hpPv = parseInt0(fHpP.getText());
        final long kiBv = parseLong0(fKiB.getText()); final int kiPv = parseInt0(fKiP.getText());
        final long dmBv = parseLong0(fDmB.getText()); final int dmPv = parseInt0(fDmP.getText());
        final long dfBv = parseLong0(fDfB.getText()); final int dfPv = parseInt0(fDfP.getText());
        final int critV = parseInt0(fCrit.getText());
        final int sdcmV = parseInt0(fSdcm.getText());
        final String skillV = fSkill.getText().trim();
        bg(() -> {
            String rs = PanelService.saveSetConfig(id, ten, optV, activeV, hpBv, hpPv, kiBv, kiPv, dmBv, dmPv, dfBv, dfPv, critV, sdcmV, skillV);
            SwingUtilities.invokeLater(() -> { info(rs); loadSetCfgs(); });
        });
    }

    private JPanel buildPhucLoi() {
        plCardLayout = new CardLayout();
        plCards = new JPanel(plCardLayout);
        plCards.setBackground(CARD);
        plCards.add(buildPlTabListPage(), "TABS");
        plCards.add(buildPlMocListPage(), "MOCS");
        plCards.add(buildPlItemPage(), "ITEMS");
        // Mo man hinh dau tien
        plCardLayout.show(plCards, "TABS");
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(CARD);
        body.add(plCards, BorderLayout.CENTER);
        lbPlStatus = new JLabel("Buoc 1/3: chon 1 tab Phuc Loi de vao danh sach moc (nhan dup hoac nut 'Vao tab').");
        lbPlStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbPlStatus.setForeground(new Color(90, 100, 130));
        lbPlStatus.setBorder(new EmptyBorder(4, 10, 4, 10));
        body.add(lbPlStatus, BorderLayout.SOUTH);
        return wrapPage(body, "Phuc Loi (Qua Online / Diem Danh / Nap / Coin / Shop)", "3 buoc: chon tab -> chon moc -> sua qua. Tab id>=3 = Shop (tien te = Coin hoac item han trang, mua khong gioi han). Sua xong tu luu va tu reload server");
    }

    // ---------------------------------------------------------------- TANG 1: TAB
    private JPanel buildPlTabListPage() {
        JPanel page = new JPanel(new BorderLayout(8, 8));
        page.setBackground(CARD);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        JButton bReload = btn("Tai lai");
        JButton bEnter = btn("Vao tab (sua moc)");
        bEnter.setBackground(new Color(70, 120, 220)); bEnter.setForeground(Color.WHITE);
        JButton bEdit = btn("Sua thong tin tab");
        JButton bAdd = btn("Them tab");
        JButton bDel = btn("Xoa tab");
        bReload.addActionListener(e -> bg(this::loadPlTabs));
        bEnter.addActionListener(e -> enterPlSelectedTab());
        bEdit.addActionListener(e -> editPlTabInfo());
        bAdd.addActionListener(e -> addPlTabDialog());
        bDel.addActionListener(e -> deletePlTabSelected());
        top.add(bReload); top.add(bEnter); top.add(bEdit); top.add(bAdd); top.add(bDel);
        page.add(top, BorderLayout.NORTH);
        plTabModel = new DefaultTableModel(new String[]{"ID", "Ten tab", "So moc", "Thong bao hien trong game", "Chu tich luy hien sau so"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        plTabTable = new JTable(plTabModel);
        plTabTable.setRowHeight(26);
        plTabTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        plTabTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) enterPlSelectedTab();
            }
        });
        JScrollPane sp = new JScrollPane(plTabTable);
        sp.setBorder(BorderFactory.createTitledBorder("Buoc 1/3 - Danh sach tab Phuc Loi (nhan dup 1 tab hoac bam 'Vao tab' de sang buoc 2)"));
        page.add(sp, BorderLayout.CENTER);
        bg(this::loadPlTabs);
        return page;
    }

    private void loadPlTabs() {
        java.util.List<Map<String, Object>> list = PanelService.listPhucLoiTabs();
        plTabCache = list;
        SwingUtilities.invokeLater(() -> {
            plTabModel.setRowCount(0);
            for (Map<String, Object> m : list) {
                // moc lien ket voi tab qua id_tab (PhucLoiManager: t.tab_id == tab.id_tab), khong phai id
                int tabId = ((Number) m.get("id_tab")).intValue();
                int n = PanelService.listPhucLoiMocs(tabId).size();
                String tich = String.valueOf(m.get("tich_luy"));
                String info = String.valueOf(m.get("info_phucloi"));
                plTabModel.addRow(new Object[]{m.get("id"), m.get("name"), n, info, tich});
            }
        });
    }

    private void enterPlSelectedTab() {
        int r = plTabTable.getSelectedRow();
        if (r < 0 || r >= plTabCache.size()) { err("Chon 1 tab truoc (bam vao 1 dong)"); return; }
        Map<String, Object> row = plTabCache.get(r);
        // dung id_tab lam khoa lien ket den phuc_loi_tab.tab_id
        plSelectedTabId = ((Number) row.get("id_tab")).intValue();
        plSelectedTabName = String.valueOf(row.get("name"));
        int curTab = 0;
        try { curTab = ((Number) row.get("currency_item")).intValue(); } catch (Exception ex) { curTab = 0; }
        if (curTab == 0) {
            plSelectedTabTien = "Coin";
        } else if (curTab == -1) {
            plSelectedTabTien = "Ngọc Xanh";
        } else if (curTab == -2) {
            plSelectedTabTien = "Hồng Ngọc";
        } else {
            models.Template.ItemTemplate tTien = PanelService.getTemplateById(curTab);
            plSelectedTabTien = tTien != null ? tTien.name : ("#" + curTab);
        }
        plMocTitle.setText("Tab dang mo: [" + plSelectedTabId + "] " + plSelectedTabName + " (tien te: " + plSelectedTabTien + ")");
        plMocTable.getColumnModel().getColumn(2).setHeaderValue(plSelectedTabId >= 3 ? ("Gia (" + plSelectedTabTien + ")") : "Muc can");
        plCardLayout.show(plCards, "MOCS");
        lbPlStatus.setText("Buoc 2/3: danh sach moc cua tab '" + plSelectedTabName + "'. Nhan dup 1 moc de sang buoc 3 sua qua.");
        bg(() -> loadPlMocs(plSelectedTabId));
    }

    private void editPlTabInfo() {
        int r = plTabTable.getSelectedRow();
        if (r < 0 || r >= plTabCache.size()) { err("Chon 1 tab truoc"); return; }
        Map<String, Object> row = plTabCache.get(r);
        final int id = ((Number) row.get("id")).intValue();
        final String oldName = String.valueOf(row.get("name"));
        final int maxTab = ((Number) row.get("max_tab")).intValue();
        final int idTab = ((Number) row.get("id_tab")).intValue();
        final String oldInfo = String.valueOf(row.get("info_phucloi"));
        final int action = ((Number) row.get("action")).intValue();
        final String oldTich = String.valueOf(row.get("tich_luy"));
        int oldCurrency = 0;
        try { oldCurrency = ((Number) row.get("currency_item")).intValue(); } catch (Exception ex) { oldCurrency = 0; }

        JTextField fTen = new JTextField(oldName, 18);
        JTextField fInfo = new JTextField(oldInfo, 18);
        JTextField fTich = new JTextField(oldTich, 12);
        JTextField fCur = new JTextField(String.valueOf(oldCurrency), 8);
        JPanel form = new JPanel(new java.awt.GridLayout(0, 1, 4, 4));
        JPanel r1 = new JPanel(new FlowLayout(FlowLayout.LEFT)); r1.add(new JLabel("Ten tab:")); r1.add(fTen);
        JPanel r2 = new JPanel(new FlowLayout(FlowLayout.LEFT)); r2.add(new JLabel("Thong bao hien:")); r2.add(fInfo);
        JPanel r3 = new JPanel(new FlowLayout(FlowLayout.LEFT)); r3.add(new JLabel("Chu tich luy:")); r3.add(fTich);
        JPanel r4 = new JPanel(new FlowLayout(FlowLayout.LEFT)); r4.add(new JLabel("Tien te tra (0=Coin, id item):")); r4.add(fCur);
        JLabel hint = new JLabel("<html>Chu tich luy = ten tien te hien SAU con so (vd 'phut', 'ngay', 'Lượng Vàng').<br>Tien te tra: 0 = Coin, -1 = Vi Ngoc Xanh, -2 = Vi Hong Ngoc, hoac id item han trang (1271 Luong Bac, 1270 Luong Vuang, 457 Thoi Vuang, 1150 Co 4 La).</html>");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        form.add(r1); form.add(r2); form.add(r3); form.add(r4); form.add(hint);
        int c = JOptionPane.showConfirmDialog(this, form, "Sua tab [" + id + "] " + oldName, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (c != JOptionPane.OK_OPTION) return;
        final String fTenV = fTen.getText().trim();
        final String fInfoV = fInfo.getText().trim();
        final String fTichV = fTich.getText().trim();
        if (fTenV.isEmpty()) { err("Ten khong rong"); return; }
        int fCurTmp;
        try { fCurTmp = Integer.parseInt(fCur.getText().trim().replaceAll("[^0-9-]", "")); } catch (Exception ex) { err("Tien te tra phai la so"); return; }
        final int fCurV = fCurTmp;
        bg(() -> { String rs = PanelService.savePhucLoiTab(id, fTenV, maxTab, idTab, fInfoV, action, fTichV, fCurV);
            SwingUtilities.invokeLater(() -> { info(rs); loadPlTabs(); if (lbPlStatus != null) lbPlStatus.setText(rs); }); });
    }

    private void addPlTabDialog() {
        String s = JOptionPane.showInputDialog(this, "ID tab moi (so khong trung, vi du 4):\nID nay cung la ma lien ket moc (id_tab).", "4");
        if (s == null) return;
        int id;
        try { id = Integer.parseInt(s.trim()); } catch (Exception ex) { err("ID phai la so"); return; }
        JTextField fTen = new JTextField("Tab Moi", 18);
        JTextField fInfo = new JTextField("Mo ta tab", 18);
        JTextField fTich = new JTextField("", 12);
        JTextField fCur = new JTextField("0", 8);
        JPanel form = new JPanel(new java.awt.GridLayout(0, 1, 4, 4));
        JPanel r1 = new JPanel(new FlowLayout(FlowLayout.LEFT)); r1.add(new JLabel("Ten tab:")); r1.add(fTen);
        JPanel r2 = new JPanel(new FlowLayout(FlowLayout.LEFT)); r2.add(new JLabel("Thong bao hien:")); r2.add(fInfo);
        JPanel r3 = new JPanel(new FlowLayout(FlowLayout.LEFT)); r3.add(new JLabel("Chu tich luy:")); r3.add(fTich);
        JPanel r4 = new JPanel(new FlowLayout(FlowLayout.LEFT)); r4.add(new JLabel("Tien te tra (0=Coin, id item):")); r4.add(fCur);
        form.add(r1); form.add(r2); form.add(r3); form.add(r4);
        JLabel hintId = new JLabel("<html>ID &gt;= 3 = tab Shop: 'Gia' cua moc = so tien te tru khi mua, mua khong gioi han.<br>Tien te: 0 = Coin, -1 = Vi Ngoc Xanh, -2 = Vi Hong Ngoc, hoac id item han trang (1271 Luong Bac, 1270 Luong Vuang, 457 Thoi Vuang, 1150 Co 4 La).</html>");
        hintId.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        form.add(hintId);
        int c = JOptionPane.showConfirmDialog(this, form, "Them tab moi id=" + id, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (c != JOptionPane.OK_OPTION) return;
        final String fTenV = fTen.getText().trim();
        final String fInfoV = fInfo.getText().trim();
        final String fTichV = fTich.getText().trim();
        if (fTenV.isEmpty()) { err("Ten khong rong"); return; }
        int fCurTmp2;
        try { fCurTmp2 = Integer.parseInt(fCur.getText().trim().replaceAll("[^0-9-]", "")); } catch (Exception ex) { err("Tien te tra phai la so"); return; }
        final int fCurV = fCurTmp2;
        bg(() -> { String rs = PanelService.addPhucLoiTab(id, fTenV, 0, id, fInfoV, 1, fTichV, fCurV);
            SwingUtilities.invokeLater(() -> { info(rs + ". Bam 'Vao tab' de them moc."); loadPlTabs(); }); });
    }

    private void deletePlTabSelected() {
        int r = plTabTable.getSelectedRow();
        if (r < 0 || r >= plTabCache.size()) { err("Chon 1 tab truoc"); return; }
        final int id = ((Number) plTabCache.get(r).get("id")).intValue();
        final String nm = String.valueOf(plTabCache.get(r).get("name"));
        int c = JOptionPane.showConfirmDialog(this, "Xoa tab [" + id + "] " + nm + " va TOAN BO moc trong tab nay?", "Xac nhan", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        bg(() -> { String s = PanelService.deletePhucLoiTab(id); SwingUtilities.invokeLater(() -> { info(s); loadPlTabs(); }); });
    }

    // ---------------------------------------------------------------- TANG 2: MOC
    private JPanel buildPlMocListPage() {
        JPanel page = new JPanel(new BorderLayout(8, 8));
        page.setBackground(CARD);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        JButton bBack = btn("<Quay lai danh sach tab");
        bBack.setBackground(new Color(120, 120, 140)); bBack.setForeground(Color.WHITE);
        JButton bEnter = btn("Sua qua trong moc");
        bEnter.setBackground(new Color(70, 120, 220)); bEnter.setForeground(Color.WHITE);
        JButton bEdit = btn("Sua ten / muc can / trang thai");
        JButton bAdd = btn("Them moc");
        JButton bDel = btn("Xoa moc");
        bBack.addActionListener(e -> { plCardLayout.show(plCards, "TABS"); lbPlStatus.setText("Buoc 1/3: chon 1 tab Phuc Loi."); });
        bEnter.addActionListener(e -> enterPlSelectedMoc());
        bEdit.addActionListener(e -> editPlMocInfo());
        bAdd.addActionListener(e -> addPlMocDialog());
        bDel.addActionListener(e -> deletePlMocSelected());
        top.add(bBack); top.add(bEnter); top.add(bEdit); top.add(bAdd); top.add(bDel);
        page.add(top, BorderLayout.NORTH);
        plMocTitle = new JLabel("Tab dang mo: ...");
        plMocTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        plMocTitle.setBorder(new EmptyBorder(4, 10, 4, 10));
        plMocModel = new DefaultTableModel(new String[]{"ID", "Ten moc", "Muc can", "Trang thai", "So mon qua", "Tom tat qua"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        plMocTable = new JTable(plMocModel);
        plMocTable.setRowHeight(26);
        plMocTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        plMocTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) enterPlSelectedMoc();
            }
        });
        JScrollPane sp = new JScrollPane(plMocTable);
        sp.setBorder(BorderFactory.createTitledBorder("Buoc 2/3 - Danh sach moc thuong (nhan dup 1 moc hoac bam 'Sua qua trong moc' de sang buoc 3)"));
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(CARD);
        center.add(plMocTitle, BorderLayout.NORTH);
        center.add(sp, BorderLayout.CENTER);
        page.add(center, BorderLayout.CENTER);
        return page;
    }

    private void loadPlMocs(int tabId) {
        java.util.List<Map<String, Object>> list = PanelService.listPhucLoiMocs(tabId);
        plMocCache = list;
        SwingUtilities.invokeLater(() -> {
            plMocModel.setRowCount(0);
            for (Map<String, Object> m : list) {
                java.util.List<GiftItemModel> items = (java.util.List<GiftItemModel>) m.get("items");
                plMocModel.addRow(new Object[]{m.get("id"), m.get("name"), m.get("max_count"),
                        (((Number) m.get("active")).intValue() == 1 ? "bat" : "tat"),
                        items.size(), PanelService.giftSummaryVN(items)});
            }
        });
    }

    private void enterPlSelectedMoc() {
        int r = plMocTable.getSelectedRow();
        if (r < 0 || r >= plMocCache.size()) { err("Chon 1 moc truoc (bam vao 1 dong)"); return; }
        Map<String, Object> row = plMocCache.get(r);
        plSelectedMocId = ((Number) row.get("id")).intValue();
        plSelectedMocName = String.valueOf(row.get("name"));
        java.util.List<GiftItemModel> items = (java.util.List<GiftItemModel>) row.get("items");
        plItemDraft = new java.util.ArrayList<>();
        plItemOriginal = new java.util.ArrayList<>();
        for (GiftItemModel x : items) { plItemDraft.add(x.copy()); plItemOriginal.add(x.copy()); }
        plItemTitle.setText("Moc dang sua: [" + plSelectedMocId + "] " + plSelectedMocName + " (tab: " + plSelectedTabName + ")");
        refreshPlItemTable();
        plCardLayout.show(plCards, "ITEMS");
        lbPlStatus.setText("Buoc 3/3: sua qua cua moc '" + plSelectedMocName + "'. Nhan dup Ten vat pham / Option de mo menu chon. Bam 'Luu moc' de ghi DB + reload server.");
    }

    private void editPlMocInfo() {
        int r = plMocTable.getSelectedRow();
        if (r < 0 || r >= plMocCache.size()) { err("Chon 1 moc truoc"); return; }
        Map<String, Object> row = plMocCache.get(r);
        final int id = ((Number) row.get("id")).intValue();
        final int tabId = ((Number) row.get("tab_id")).intValue();
        final String oldName = String.valueOf(row.get("name"));
        final int oldMax = ((Number) row.get("max_count")).intValue();
        final int oldActive = ((Number) row.get("active")).intValue();
        JTextField fTen = new JTextField(oldName, 18);
        JTextField fMax = new JTextField(String.valueOf(oldMax), 10);
        String[] stOpts = {"bat (mo - cho phep nhan)", "tat (an - khong nhan duoc)"};
        JComboBox<String> cbActive = new JComboBox<>(stOpts);
        cbActive.setSelectedIndex(oldActive == 1 ? 0 : 1);
        JPanel form = new JPanel(new java.awt.GridLayout(0, 1, 4, 4));
        JPanel r1 = new JPanel(new FlowLayout(FlowLayout.LEFT)); r1.add(new JLabel("Ten moc:")); r1.add(fTen);
        JPanel r2 = new JPanel(new FlowLayout(FlowLayout.LEFT)); r2.add(new JLabel(tabId >= 3 ? ("Gia (" + plSelectedTabTien + "):") : "Muc can:")); r2.add(fMax);
        JPanel r3 = new JPanel(new FlowLayout(FlowLayout.LEFT)); r3.add(new JLabel("Trang thai:")); r3.add(cbActive);
        form.add(r1); form.add(r2); form.add(r3);
        if (tabId >= 3) {
            JLabel hintCoin = new JLabel("<html>Shop: 'Gia' = so " + plSelectedTabTien + " tru moi lan mua. Trang thai <b>bat</b> = dang ban, mua lai khong gioi han.<br>So luong ban va option cua qua chinh o buoc 3 (sua qua trong moc).</html>");
            hintCoin.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            form.add(hintCoin);
        }
        int c = JOptionPane.showConfirmDialog(this, form, "Sua moc [" + id + "] " + oldName, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (c != JOptionPane.OK_OPTION) return;
        final String fTenV = fTen.getText().trim();
        int fMaxV;
        try { fMaxV = Integer.parseInt(fMax.getText().trim().replaceAll("[^0-9-]", "")); } catch (Exception ex) { err("Muc can phai la so"); return; }
        final int fActiveV = cbActive.getSelectedIndex() == 0 ? 1 : 0;
        if (fTenV.isEmpty()) { err("Ten khong rong"); return; }
        java.util.List<GiftItemModel> items = (java.util.List<GiftItemModel>) row.get("items");
        java.util.List<GiftItemModel> copy = new java.util.ArrayList<>();
        for (GiftItemModel m : items) copy.add(m.copy());
        bg(() -> { String rs = PanelService.savePhucLoiMoc(id, tabId, fTenV, fMaxV, fActiveV, copy);
            SwingUtilities.invokeLater(() -> { info(rs); loadPlMocs(plSelectedTabId); if (lbPlStatus != null) lbPlStatus.setText(rs); }); });
    }

    private void addPlMocDialog() {
        if (plSelectedTabId < 0) { err("Chua vao tab nao (buoc 1)"); return; }
        // tinh ID ke tiep tren TOAN BO moc (moi tab chi lay 1 phan), tranh trung id cua tab khac
        final int nextId = PanelService.nextPhucLoiMocId();
        JTextField fTen = new JTextField("Moc moi", 18);
        JTextField fMax = new JTextField("1", 10);
        JPanel form = new JPanel(new java.awt.GridLayout(0, 1, 4, 4));
        JPanel r1 = new JPanel(new FlowLayout(FlowLayout.LEFT)); r1.add(new JLabel("Ten moc:")); r1.add(fTen);
        JPanel r2 = new JPanel(new FlowLayout(FlowLayout.LEFT)); r2.add(new JLabel(plSelectedTabId >= 3 ? ("Gia (" + plSelectedTabTien + "):") : "Muc can:")); r2.add(fMax);
        form.add(r1); form.add(r2);
        if (plSelectedTabId >= 3) {
            JLabel hintCoin = new JLabel("<html>Shop: 'Gia' = so " + plSelectedTabTien + " tru moi lan mua, mua khong gioi han. Moc se duoc tao o trang thai <b>bat</b>.<br>Sau khi tao xong bam dup moc de them qua: so luong ban + option tuy ban chon.</html>");
            hintCoin.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            form.add(hintCoin);
        }
        int c = JOptionPane.showConfirmDialog(this, form, "Them moc moi vao tab '" + plSelectedTabName + "'", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (c != JOptionPane.OK_OPTION) return;
        final String fTenV = fTen.getText().trim();
        int fMaxV;
        try { fMaxV = Integer.parseInt(fMax.getText().trim().replaceAll("[^0-9-]", "")); } catch (Exception ex) { err("Muc can phai la so"); return; }
        if (fTenV.isEmpty()) { err("Ten khong rong"); return; }
        bg(() -> { String rs = PanelService.savePhucLoiMoc(nextId, plSelectedTabId, fTenV, fMaxV, plSelectedTabId >= 3 ? 1 : 0, new java.util.ArrayList<>());
            SwingUtilities.invokeLater(() -> { info(rs + ". Nhan dup moc de them qua."); loadPlMocs(plSelectedTabId); }); });
    }

    private void deletePlMocSelected() {
        int r = plMocTable.getSelectedRow();
        if (r < 0 || r >= plMocCache.size()) { err("Chon 1 moc truoc"); return; }
        final int id = ((Number) plMocCache.get(r).get("id")).intValue();
        final String nm = String.valueOf(plMocCache.get(r).get("name"));
        int c = JOptionPane.showConfirmDialog(this, "Xoa moc [" + id + "] " + nm + "?", "Xac nhan", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        bg(() -> { String s = PanelService.deletePhucLoiMoc(id); SwingUtilities.invokeLater(() -> { info(s); loadPlMocs(plSelectedTabId); }); });
    }

    // ---------------------------------------------------------------- TANG 3: QUA TRONG MOC
    private JPanel buildPlItemPage() {
        JPanel page = new JPanel(new BorderLayout(8, 8));
        page.setBackground(CARD);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        JButton bBack = btn("<Quay lai danh sach moc");
        bBack.setBackground(new Color(120, 120, 140)); bBack.setForeground(Color.WHITE);
        JButton bAdd = btn("Them qua");
        bAdd.setBackground(new Color(70, 120, 220)); bAdd.setForeground(Color.WHITE);
        JButton bDel = btn("Xoa qua");
        JButton bUndo = btn("Hoan tac chua luu");
        JButton bSave = btn("Luu moc (ghi DB + reload)");
        bSave.setBackground(new Color(40, 150, 80)); bSave.setForeground(Color.WHITE);
        bBack.addActionListener(e -> {
            bg(() -> loadPlMocs(plSelectedTabId));
            plCardLayout.show(plCards, "MOCS");
            lbPlStatus.setText("Buoc 2/3: danh sach moc cua tab '" + plSelectedTabName + "'.");
        });
        bAdd.addActionListener(e -> {
            Integer pick = openTemplatePicker();
            if (pick == null) return;
            GiftItemModel m = new GiftItemModel();
            m.tempId = pick;
            models.Template.ItemTemplate t = PanelService.getTemplateById(pick);
            m.itemName = t != null ? t.name : String.valueOf(pick);
            m.quantity = 1;
            plItemDraft.add(m);
            refreshPlItemTable();
        });
        bDel.addActionListener(e -> {
            int r = plItemTable.getSelectedRow();
            if (r < 0 || r >= plItemDraft.size()) { err("Chon 1 mon qua truoc"); return; }
            plItemDraft.remove(r);
            refreshPlItemTable();
        });
        bUndo.addActionListener(e -> {
            plItemDraft = new java.util.ArrayList<>();
            for (GiftItemModel x : plItemOriginal) plItemDraft.add(x.copy());
            refreshPlItemTable();
            info("Da khoi phuc ve lan luu gan nhat");
        });
        bSave.addActionListener(e -> savePlItemsLoud());
        top.add(bBack); top.add(bAdd); top.add(bDel); top.add(bUndo); top.add(bSave);
        page.add(top, BorderLayout.NORTH);
        plItemTitle = new JLabel("Moc dang sua: ...");
        plItemTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        plItemTitle.setBorder(new EmptyBorder(4, 10, 4, 10));
        plItemModel = new DefaultTableModel(new String[]{"TempID", "Ten vat pham (NHAP DEP de mo menu chon)", "So luong (go so, Enter tu nho)", "Option (NHAP DEP de mo menu chon)"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 2; }
            @Override public void setValueAt(Object v, int r, int c) {
                if (plGuard) { super.setValueAt(v, r, c); return; }
                try {
                    if (r < 0 || r >= plItemDraft.size()) return;
                    if (c != 2) { super.setValueAt(v, r, c); return; }
                    int q = Integer.parseInt(String.valueOf(v).trim().replaceAll("[^0-9-]", ""));
                    if (q <= 0) { err("So luong phai > 0"); return; }
                    plItemDraft.get(r).quantity = q;
                    plGuard = true;
                    try {
                        super.setValueAt(String.valueOf(q), r, c);
                        super.setValueAt(PanelService.giftItemSummaryVN(plItemDraft.get(r)), r, 3);
                    } finally { plGuard = false; }
                } catch (Exception ex) { plGuard = false; err("So luong phai la so"); }
            }
        };
        plItemTable = new JTable(plItemModel);
        plItemTable.setRowHeight(26);
        plItemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        plItemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() != 2) return;
                int r = plItemTable.getSelectedRow();
                int c = plItemTable.getSelectedColumn();
                if (r < 0 || r >= plItemDraft.size()) return;
                if (c == 1) {
                    Integer pick = openTemplatePicker();
                    if (pick == null) return;
                    GiftItemModel m = plItemDraft.get(r);
                    m.tempId = pick;
                    models.Template.ItemTemplate t = PanelService.getTemplateById(pick);
                    m.itemName = t != null ? t.name : String.valueOf(pick);
                    refreshPlItemTable();
                } else if (c == 3) {
                    openPlOptionEditor(plItemDraft.get(r));
                }
            }
        });
        JScrollPane sp = new JScrollPane(plItemTable);
        sp.setBorder(BorderFactory.createTitledBorder("Buoc 3/3 - Qua trong moc (nhan dup 'Ten vat pham' de doi mon, nhan dup 'Option' de them/sua option bang menu)"));
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(CARD);
        center.add(plItemTitle, BorderLayout.NORTH);
        center.add(sp, BorderLayout.CENTER);
        page.add(center, BorderLayout.CENTER);
        return page;
    }

    private void refreshPlItemTable() {
        plGuard = true;
        try {
            plItemModel.setRowCount(0);
            for (GiftItemModel m : plItemDraft) {
                plItemModel.addRow(new Object[]{m.tempId, m.itemName, String.valueOf(m.quantity), PanelService.giftItemSummaryVN(m)});
            }
        } finally { plGuard = false; }
    }

    private boolean plItemsChanged() {
        if (plItemDraft.size() != plItemOriginal.size()) return true;
        for (int i = 0; i < plItemDraft.size(); i++) {
            GiftItemModel a = plItemDraft.get(i), b = plItemOriginal.get(i);
            if (a.tempId != b.tempId || a.quantity != b.quantity || a.options.size() != b.options.size()) return true;
            for (int j = 0; j < a.options.size(); j++) {
                if (a.options.get(j).id != b.options.get(j).id || a.options.get(j).param != b.options.get(j).param) return true;
            }
        }
        return false;
    }

    private void savePlItemsLoud() {
        if (plSelectedMocId < 0) { err("Chua chon moc (buoc 2)"); return; }
        Map<String, Object> row = null;
        for (Map<String, Object> m : plMocCache) if (((Number) m.get("id")).intValue() == plSelectedMocId) { row = m; break; }
        if (row == null) { err("Khong tim thay moc trong bo dem, bam 'Quay lai' roi vao lai"); return; }
        final Map<String, Object> fRow = row;
        final int tabId = ((Number) row.get("tab_id")).intValue();
        String name = String.valueOf(row.get("name"));
        int maxCount = ((Number) row.get("max_count")).intValue();
        int active = ((Number) row.get("active")).intValue();
        if (!plItemsChanged()) { info("Khong co thay doi nao de luu"); return; }
        java.util.List<GiftItemModel> copy = new java.util.ArrayList<>();
        for (GiftItemModel m : plItemDraft) copy.add(m.copy());
        final String fName = name;
        bg(() -> {
            String r = PanelService.savePhucLoiMoc(plSelectedMocId, tabId, fName, maxCount, active, copy);
            SwingUtilities.invokeLater(() -> {
                if (!r.startsWith("OK")) { err(r); return; }
                plItemOriginal = new java.util.ArrayList<>();
                for (GiftItemModel m : copy) plItemOriginal.add(m.copy());
                fRow.put("items", copy);
                fRow.put("list_item", PanelService.buildPhucLoiItemsJson(copy));
                int sel = plMocTable.getSelectedRow();
                if (sel >= 0 && sel < plMocCache.size() && ((Number) plMocCache.get(sel).get("id")).intValue() == plSelectedMocId) {
                    plMocModel.setValueAt(copy.size(), sel, 4);
                    plMocModel.setValueAt(PanelService.giftSummaryVN(copy), sel, 5);
                }
                info(r);
                if (lbPlStatus != null) lbPlStatus.setText("Da luu moc [" + plSelectedMocId + "] " + fName + ". Server da reload Phuc Loi.");
            });
        });
    }

    /** Menu chon option cho qua trong Phuc Loi: dropdown chon option, chi go so param. */
    private void openPlOptionEditor(GiftItemModel m) {
        JDialog d = new JDialog(this, "Option cua: " + (m.itemName == null ? ("#" + m.tempId) : m.itemName), true);
        d.setSize(640, 480);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());
        DefaultTableModel om = new DefaultTableModel(new String[]{"Option (chon dropdown)", "Param (go so)", "Mo ta"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 0 || c == 1; }
        };
        JTable ot = new JTable(om);
        ot.setRowHeight(24);
        final boolean[] og = new boolean[]{false};
        Runnable reload = () -> {
            og[0] = true;
            try {
                om.setRowCount(0);
                for (GiftItemModel.Opt o : m.options) om.addRow(new Object[]{o.id, String.valueOf(o.param), PanelService.optionDisplay(o.id)});
            } finally { og[0] = false; }
        };
        reload.run();
        JComboBox<String> cbOpt = new JComboBox<>();
        cbOpt.setPrototypeDisplayValue("#000 Sao pha le x99 | Ten dai........");
        cbOpt.setPreferredSize(new java.awt.Dimension(380, 25));
        cbOpt.setMaximumRowCount(20);
        java.util.List<Integer> ids = new java.util.ArrayList<>();
        if (optDictCache == null || optDictCache.isEmpty()) optDictCache = PanelService.listOptionDict();
        for (Map<String, Object> o : optDictCache) {
            int id = ((Number) o.get("id")).intValue();
            ids.add(id);
            String tv = o.get("ten_viet") == null ? "" : String.valueOf(o.get("ten_viet"));
            String nm = o.get("name") == null ? "" : String.valueOf(o.get("name"));
            cbOpt.addItem("#" + id + " " + (tv.isEmpty() ? nm : tv));
        }
        ot.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(cbOpt) {
            @Override public Object getCellEditorValue() {
                int i = cbOpt.getSelectedIndex();
                return (i >= 0 && i < ids.size()) ? ids.get(i) : super.getCellEditorValue();
            }
        });
        om.addTableModelListener(e -> {
            if (og[0]) return;
            if (e.getType() != javax.swing.event.TableModelEvent.UPDATE) return;
            if (e.getColumn() == 2) return;
            int r = e.getFirstRow();
            if (r < 0 || r >= m.options.size()) return;
            try {
                og[0] = true;
                Object ov = om.getValueAt(r, 0);
                int oid = ov instanceof Number ? ((Number) ov).intValue() : Integer.parseInt(String.valueOf(ov).replaceAll("[^0-9-]", ""));
                long pv = Long.parseLong(String.valueOf(om.getValueAt(r, 1)).trim().replaceAll("[^0-9-]", ""));
                m.options.get(r).id = oid;
                m.options.get(r).param = pv;
                om.setValueAt(PanelService.optionDisplay(oid), r, 2);
            } catch (Exception ex) {} finally { og[0] = false; }
        });
        JPanel pick = new JPanel(new BorderLayout(4, 4));
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(new JLabel("Option:")); row1.add(cbOpt);
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField fP = new JTextField("0", 12);
        JButton bAddO = btn("Them dong");
        bAddO.setBackground(new Color(70, 120, 220)); bAddO.setForeground(Color.WHITE);
        JButton bDelO = btn("Xoa dong");
        row2.add(new JLabel("Param:")); row2.add(fP);
        row2.add(bAddO); row2.add(bDelO);
        JLabel hint = new JLabel("Chon Option -> nhap Param -> bam 'Them dong'. Hoac go sua truc tiep trong bang duoi.");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        hint.setForeground(new Color(90, 100, 130));
        pick.add(row1, BorderLayout.NORTH);
        pick.add(row2, BorderLayout.CENTER);
        pick.add(hint, BorderLayout.SOUTH);
        bAddO.addActionListener(e -> {
            int idx = cbOpt.getSelectedIndex();
            int oid = (idx >= 0 && idx < ids.size()) ? ids.get(idx) : 50;
            long pv = 0;
            try { pv = Long.parseLong(fP.getText().trim().replaceAll("[^0-9-]", "")); } catch (Exception ex) {}
            String er = optParamErrById(oid, pv);
            if (er != null) { err(er); return; }
            m.options.add(new GiftItemModel.Opt(oid, pv));
            reload.run();
        });
        bDelO.addActionListener(e -> { int r = ot.getSelectedRow(); if (r >= 0 && r < m.options.size()) { m.options.remove(r); reload.run(); } });
        d.add(pick, BorderLayout.NORTH);
        d.add(new JScrollPane(ot), BorderLayout.CENTER);
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bOk = btn("Dong (ghi nhan thay doi)");
        bOk.setBackground(new Color(40, 150, 80)); bOk.setForeground(Color.WHITE);
        bOk.addActionListener(e -> {
            try { if (ot.isEditing()) ot.getCellEditor().stopCellEditing(); } catch (Exception ex) {}
            d.dispose(); refreshPlItemTable();
        });
        bot.add(bOk);
        d.add(bot, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    // ================= HE THONG (phan tang: NHOM -> PHAN -> TUNG THONG SO) =================
    // Tang 1: danh sach nhom he thong (Thien Dao, Dia Dao, Chuyen Sinh Su Phu, ...).
    // Tang 2: mo 1 nhom -> bang thong so cua rieng nhom do, loc tiep theo "phan" ben trong.
    // Nho vay khong con 1 bang dai 40+ dong kho nhin nhu truoc; them nhom moi chi can khai bao trong
    // SystemTuning.defaults() la tu dong hien ra, khong phai sua giao dien.
    private JPanel htCards;
    private CardLayout htCardLayout;
    private JLabel lbHeThong;
    private String htView = "GROUPS";
    private String htOpenGroup = "";
    private DefaultTableModel htGroupModel;
    private JTable htGroupTable;
    private java.util.List<String> htGroupOrder = new java.util.ArrayList<>();
    private DefaultTableModel htParamModel;
    private JTable htParamTable;
    private JComboBox<String> htSection;
    private boolean htFilling = false;
    private java.util.List<Map<String, Object>> heThongCache = new java.util.ArrayList<>();
    /** Gia tri da sua nhung chua bam Luu (giu lai khi doi "phan" de khong bi mat). */
    private final Map<String, Double> htPending = new java.util.LinkedHashMap<>();

    private JPanel buildHeThong() {
        htCardLayout = new CardLayout();
        htCards = new JPanel(htCardLayout);
        htCards.setBackground(CARD);
        htCards.add(buildHtGroupPage(), "GROUPS");
        htCards.add(buildHtParamPage(), "PARAMS");
        htCardLayout.show(htCards, "GROUPS");
        htView = "GROUPS";
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(CARD);
        body.add(htCards, BorderLayout.CENTER);
        lbHeThong = new JLabel("Buoc 1/2: chon 1 nhom he thong (nhan dup hoac nut 'Mo nhom de sua').");
        lbHeThong.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbHeThong.setForeground(new Color(90, 100, 130));
        lbHeThong.setBorder(new EmptyBorder(4, 10, 4, 10));
        body.add(lbHeThong, BorderLayout.SOUTH);
        bg(this::loadHeThong);
        return wrapPage(body, "He Thong (Thien/Dia Dao, Chuyen Sinh, Tu Tien, Dao Lu/Em Be, Ket Hon, Cay Phep...)",
                "2 buoc: chon nhom -> sua tung phan ben trong. Sua cot 'Gia tri' roi bam Luu la ap dung ngay, khong can build lai");
    }

    // ------------------------------------------------ Tang 1/2: danh sach nhom
    private JPanel buildHtGroupPage() {
        JPanel page = new JPanel(new BorderLayout(8, 8));
        page.setBackground(CARD);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        JButton bReload = btn("Tai lai");
        JButton bEnter = btn("Mo nhom de sua");
        bEnter.setBackground(new Color(70, 120, 220)); bEnter.setForeground(Color.WHITE);
        bReload.addActionListener(e -> bg(this::loadHeThong));
        bEnter.addActionListener(e -> enterHtSelectedGroup());
        top.add(bReload); top.add(bEnter);
        page.add(top, BorderLayout.NORTH);
        htGroupModel = new DefaultTableModel(new String[]{"Nhom he thong", "So thong so", "Da sua", "Cac phan ben trong"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        htGroupTable = new JTable(htGroupModel);
        htGroupTable.setRowHeight(26);
        htGroupTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        htGroupTable.getColumnModel().getColumn(0).setPreferredWidth(220);
        htGroupTable.getColumnModel().getColumn(1).setPreferredWidth(90);
        htGroupTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        htGroupTable.getColumnModel().getColumn(3).setPreferredWidth(520);
        htGroupTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) enterHtSelectedGroup();
            }
        });
        JScrollPane sp = new JScrollPane(htGroupTable);
        sp.setBorder(BorderFactory.createTitledBorder("Buoc 1/2 - Danh sach nhom he thong (nhan dup 1 nhom de vao sua thong so)"));
        page.add(sp, BorderLayout.CENTER);
        return page;
    }

    // ------------------------------------------------ Tang 2/2: thong so cua 1 nhom
    private JPanel buildHtParamPage() {
        JPanel page = new JPanel(new BorderLayout(8, 8));
        page.setBackground(CARD);
        JPanel top = new JPanel(new BorderLayout(8, 4));
        top.setBackground(CARD);
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.setBackground(CARD);
        JButton bBack = btn("< Quay lai danh sach nhom");
        JButton bReload = btn("Tai lai");
        JButton bSave = btn("Luu nhom nay (ap dung ngay)");
        bSave.setBackground(new Color(40, 150, 80)); bSave.setForeground(Color.WHITE);
        JButton bDef = btn("Khoi phuc mac dinh ca nhom");
        bBack.addActionListener(e -> showHtGroups(false));
        bReload.addActionListener(e -> bg(this::loadHeThong));
        bSave.addActionListener(e -> saveHtGroup());
        bDef.addActionListener(e -> resetHtGroup());
        row1.add(bBack); row1.add(bReload); row1.add(bSave); row1.add(bDef);
        top.add(row1, BorderLayout.NORTH);
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.setBackground(CARD);
        htSection = new JComboBox<>(new String[]{"Tat ca"});
        htSection.setPreferredSize(new Dimension(280, 24));
        htSection.addActionListener(e -> { if (htFilling) return; harvestHtParams(); renderHtParams(); });
        row2.add(new JLabel("Phan trong nhom:")); row2.add(htSection);
        top.add(row2, BorderLayout.SOUTH);
        page.add(top, BorderLayout.NORTH);
        htParamModel = new DefaultTableModel(new String[]{"Phan", "Ma", "\u00dd nghia (so nay anh huong gi)", "Gia tri", "Mac dinh", "Trang thai"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 3; }
        };
        htParamTable = new JTable(htParamModel);
        htParamTable.setRowHeight(24);
        htParamTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        htParamTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        htParamTable.getColumnModel().getColumn(2).setPreferredWidth(520);
        htParamTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        htParamTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        htParamTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        JScrollPane sp = new JScrollPane(htParamTable);
        sp.setBorder(BorderFactory.createTitledBorder("Buoc 2/2 - Sua cot 'Gia tri' (chi sua so, khong sua ma thong so)"));
        page.add(sp, BorderLayout.CENTER);
        return page;
    }

    private void htStatus(String s) {
        if (lbHeThong != null) SwingUtilities.invokeLater(() -> lbHeThong.setText(s));
    }

    private String htSectionName(Map<String, Object> m) {
        String s = String.valueOf(m.get("section"));
        return (s == null || s.isEmpty() || "null".equals(s)) ? "(chung)" : s;
    }

    private double htDefVal(Map<String, Object> m) {
        try { return ((Number) m.get("def")).doubleValue(); } catch (Exception e) { return 0; }
    }

    private double htCurVal(Map<String, Object> m) {
        try {
            Double p = htPending.get(String.valueOf(m.get("key")));
            if (p != null) return p;
            return ((Number) m.get("value")).doubleValue();
        } catch (Exception e) { return 0; }
    }

    /** In so de doc: 150000000000000 -> 150000000000000, khong hien 1.5E14. */
    private String htFmt(double v) {
        if (v == Math.rint(v) && Math.abs(v) < 1e15) return String.valueOf((long) v);
        return String.valueOf(v);
    }

    private void loadHeThong() {
        java.util.List<Map<String, Object>> list = PanelService.listSystemTuning();
        heThongCache = list;
        SwingUtilities.invokeLater(() -> {
            renderHtGroups();
            if ("PARAMS".equals(htView) && !htOpenGroup.isEmpty()) {
                htCardLayout.show(htCards, "PARAMS");
                fillHtSections();
            } else {
                htCardLayout.show(htCards, "GROUPS");
                htStatus("Buoc 1/2: " + list.size() + " thong so trong " + htGroupOrder.size()
                        + " nhom. Chon 1 nhom roi bam 'Mo nhom de sua' (hoac nhan dup).");
            }
        });
    }

    private void renderHtGroups() {
        if (htGroupModel == null) return;
        htGroupModel.setRowCount(0);
        htGroupOrder.clear();
        java.util.Map<String, Integer> cnt = new java.util.LinkedHashMap<>();
        java.util.Map<String, Integer> changed = new java.util.LinkedHashMap<>();
        java.util.Map<String, java.util.LinkedHashSet<String>> secs = new java.util.LinkedHashMap<>();
        for (Map<String, Object> m : heThongCache) {
            String g = String.valueOf(m.get("group"));
            Integer c = cnt.get(g);
            cnt.put(g, c == null ? 1 : c + 1);
            java.util.LinkedHashSet<String> s = secs.get(g);
            if (s == null) { s = new java.util.LinkedHashSet<>(); secs.put(g, s); }
            s.add(htSectionName(m));
            if (Math.abs(htCurVal(m) - htDefVal(m)) > 1e-7) {
                Integer d = changed.get(g);
                changed.put(g, d == null ? 1 : d + 1);
            }
        }
        for (String g : cnt.keySet()) {
            htGroupOrder.add(g);
            Integer d = changed.get(g);
            htGroupModel.addRow(new Object[]{g, cnt.get(g), d == null ? "-" : (d + " dong"),
                    String.join("   |   ", secs.get(g))});
        }
    }

    private void enterHtSelectedGroup() {
        int r = htGroupTable == null ? -1 : htGroupTable.getSelectedRow();
        if (r < 0 || r >= htGroupOrder.size()) { err("Chon 1 nhom truoc (bam vao 1 dong trong bang)"); return; }
        htOpenGroup = htGroupOrder.get(r);
        htPending.clear();
        showHtGroups(true);
    }

    private void showHtGroups(boolean intoGroup) {
        if (intoGroup) {
            htView = "PARAMS";
            htCardLayout.show(htCards, "PARAMS");
            fillHtSections();
        } else {
            htView = "GROUPS";
            htOpenGroup = "";
            htPending.clear();
            htCardLayout.show(htCards, "GROUPS");
            renderHtGroups();
            htStatus("Buoc 1/2: " + heThongCache.size() + " thong so trong " + htGroupOrder.size()
                    + " nhom. Chon 1 nhom roi bam 'Mo nhom de sua' (hoac nhan dup).");
        }
    }

    /** Nap lai danh sach "phan" co trong nhom dang mo roi ve bang thong so. */
    private void fillHtSections() {
        if (htSection == null) return;
        htFilling = true;
        try {
            java.util.LinkedHashSet<String> secs = new java.util.LinkedHashSet<>();
            for (Map<String, Object> m : heThongCache) {
                if (htOpenGroup.equals(String.valueOf(m.get("group")))) secs.add(htSectionName(m));
            }
            htSection.removeAllItems();
            htSection.addItem("Tat ca");
            for (String s : secs) htSection.addItem(s);
            htSection.setSelectedIndex(0);
        } finally {
            htFilling = false;
        }
        renderHtParams();
    }

    private void renderHtParams() {
        if (htParamModel == null) return;
        String sec = htSection == null || htSection.getSelectedItem() == null ? "Tat ca" : String.valueOf(htSection.getSelectedItem());
        htParamModel.setRowCount(0);
        int n = 0, chg = 0;
        for (Map<String, Object> m : heThongCache) {
            if (!htOpenGroup.equals(String.valueOf(m.get("group")))) continue;
            String s = htSectionName(m);
            if (!"Tat ca".equals(sec) && !sec.equals(s)) continue;
            double val = htCurVal(m), def = htDefVal(m);
            boolean diff = Math.abs(val - def) > 1e-7;
            htParamModel.addRow(new Object[]{s, m.get("key"), m.get("note"), htFmt(val), htFmt(def), diff ? "da doi" : "mac dinh"});
            n++;
            if (diff) chg++;
        }
        htStatus("Buoc 2/2 - Nhom '" + htOpenGroup + "': " + n + " thong so dang hien, " + chg + " khac mac dinh"
                + (htPending.isEmpty() ? "" : " (" + htPending.size() + " gia tri chua bam Luu)")
                + ". Sua cot 'Gia tri' roi bam 'Luu nhom nay'.");
    }

    /** Gom gia tri dang nhap tren bang vao htPending (chua ghi DB) - de doi "phan" khong mat sua do. */
    private void harvestHtParams() {
        if (htParamModel == null) return;
        try { if (htParamTable != null && htParamTable.isEditing()) htParamTable.getCellEditor().stopCellEditing(); } catch (Exception ex) {}
        for (int i = 0; i < htParamModel.getRowCount(); i++) {
            String k = String.valueOf(htParamModel.getValueAt(i, 1));
            try {
                htPending.put(k, Double.parseDouble(String.valueOf(htParamModel.getValueAt(i, 3)).trim().replaceAll("[^0-9.\\-]", "")));
            } catch (Exception ex) {}
        }
    }

    private void saveHtGroup() {
        if (htOpenGroup.isEmpty()) return;
        harvestHtParams();
        final String g = htOpenGroup;
        Map<String, Double> vals = new java.util.LinkedHashMap<>();
        for (Map<String, Object> m : heThongCache) {
            if (g.equals(String.valueOf(m.get("group")))) vals.put(String.valueOf(m.get("key")), htCurVal(m));
        }
        if (vals.isEmpty()) { info("Nhom '" + g + "' khong co thong so nao."); return; }
        bg(() -> {
            String r = PanelService.saveSystemTuning(vals);
            SwingUtilities.invokeLater(() -> {
                info(r + "\nNhom: " + g);
                htPending.clear();
                loadHeThongThenOpen(g);
            });
        });
    }

    private void resetHtGroup() {
        if (htOpenGroup.isEmpty()) return;
        final String g = htOpenGroup;
        if (JOptionPane.showConfirmDialog(this, "Khoi phuc mac dinh cho toan bo nhom '" + g + "'?",
                "Xac nhan", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        Map<String, Double> vals = new java.util.LinkedHashMap<>();
        for (Map<String, Object> m : heThongCache) {
            if (g.equals(String.valueOf(m.get("group")))) vals.put(String.valueOf(m.get("key")), htDefVal(m));
        }
        bg(() -> {
            String r = PanelService.saveSystemTuning(vals);
            SwingUtilities.invokeLater(() -> { info(r); htPending.clear(); loadHeThongThenOpen(g); });
        });
    }

    /** Sau khi Luu/Khoi phuc: nap lai so lieu roi mo lai dung nhom dang sua. */
    private void loadHeThongThenOpen(String g) {
        java.util.List<Map<String, Object>> list = PanelService.listSystemTuning();
        heThongCache = list;
        htOpenGroup = g;
        htView = "PARAMS";
        SwingUtilities.invokeLater(() -> {
            renderHtGroups();
            htCardLayout.show(htCards, "PARAMS");
            fillHtSections();
        });
    }

    // ================= CUNG MENH (NPC Bo Mong - Rung Karin) =================
    private JTextField cmMax, cmHpP, cmKiP, cmDameP, cmHpF, cmKiF, cmDameF;
    private JTextField cmManhBase, cmManhStep, cmManhExtra, cmDpEvery;
    private JTextField cmDpManhBase, cmDpManhStep, cmDpNgocBase, cmDpNgocStep;
    private DefaultTableModel cmBacModel;
    private JTable cmBacTable;
    private java.util.List<Map<String, Object>> cmBacCache = new java.util.ArrayList<>();
    private JTextArea cmPreview;
    private JLabel lbCmStatus;

    private JPanel buildCungMenh() {
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setBackground(CARD);
        body.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));

        // --- 1. Cau hinh chung ---
        JPanel cfgBox = new JPanel(new BorderLayout(6, 6));
        cfgBox.setBackground(CARD);
        cfgBox.setBorder(BorderFactory.createTitledBorder("1. Cau hinh Cung Menh (so sai o day la vao game ngay, khong can build lai)"));
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(CARD);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(2, 4, 2, 4);
        gc.anchor = GridBagConstraints.WEST;
        cmMax = tf("120", 6);
        cmHpP = tf("0", 6); cmKiP = tf("0", 6); cmDameP = tf("0", 6);
        cmHpF = tf("2000", 8); cmKiF = tf("2000", 8); cmDameF = tf("200", 8);
        cmManhBase = tf("2", 5); cmManhStep = tf("2", 5); cmManhExtra = tf("1", 5);
        cmDpEvery = tf("10", 5);
        cmDpManhBase = tf("15", 5); cmDpManhStep = tf("5", 5);
        cmDpNgocBase = tf("1", 5); cmDpNgocStep = tf("1", 5);
        int y = 0;
        addRow(grid, gc, y++, "Bao cap (cap toi da):", cmMax);
        JPanel pct = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pct.setBackground(CARD);
        pct.add(new JLabel("HP:")); pct.add(cmHpP); pct.add(new JLabel("KI:")); pct.add(cmKiP); pct.add(new JLabel("SD:")); pct.add(cmDameP);
        pct.add(new JLabel("  (% moi cap - de 0 neu khong dung)"));
        addRow(grid, gc, y++, "Moi cap tang %:", pct);
        JPanel flat = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        flat.setBackground(CARD);
        flat.add(new JLabel("HP:")); flat.add(cmHpF); flat.add(new JLabel("KI:")); flat.add(cmKiF); flat.add(new JLabel("SD:")); flat.add(cmDameF);
        flat.add(new JLabel("  (cong thang moi cap)"));
        addRow(grid, gc, y++, "Moi cap tang thang:", flat);
        JPanel manh = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        manh.setBackground(CARD);
        manh.add(new JLabel("Manh o cap 1:")); manh.add(cmManhBase);
        manh.add(new JLabel("cu moi")); manh.add(cmManhStep);
        manh.add(new JLabel("cap cong them")); manh.add(cmManhExtra); manh.add(new JLabel("manh"));
        addRow(grid, gc, y++, "Gia nang cap:", manh);
        JPanel dp = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        dp.setBackground(CARD);
        dp.add(new JLabel("cu")); dp.add(cmDpEvery); dp.add(new JLabel("cap phai dot pha 1 lan"));
        addRow(grid, gc, y++, "Dot pha:", dp);
        JPanel dpm = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        dpm.setBackground(CARD);
        dpm.add(new JLabel("Manh:")); dpm.add(cmDpManhBase); dpm.add(new JLabel("+ moi lan:")); dpm.add(cmDpManhStep);
        dpm.add(new JLabel("  Ngoc:")); dpm.add(cmDpNgocBase); dpm.add(new JLabel("+ moi lan:")); dpm.add(cmDpNgocStep);
        addRow(grid, gc, y++, "Gia dot pha:", dpm);
        JButton bSave = btn("Luu cau hinh");
        bSave.setBackground(new Color(40, 150, 80)); bSave.setForeground(Color.WHITE);
        JButton bReload = btn("Tai lai tu DB");
        JButton bPreview = btn("Xem truoc bang gia + chi so");
        bSave.addActionListener(e -> saveCmConfig());
        bReload.addActionListener(e -> bg(this::loadCmConfig));
        bPreview.addActionListener(e -> cmPreview.setText(buildCmPreview()));
        JPanel acts = new JPanel(new GridLayout(0, 1, 0, 6));
        acts.setBackground(CARD);
        acts.add(bSave); acts.add(bPreview); acts.add(bReload);
        GridBagConstraints ga = (GridBagConstraints) gc.clone();
        ga.gridx = 2; ga.gridy = 0; ga.gridheight = 6; ga.anchor = GridBagConstraints.NORTH;
        grid.add(acts, ga);
        cfgBox.add(grid, BorderLayout.NORTH);
        cmPreview = roArea(7);
        JScrollPane spPv = new JScrollPane(cmPreview);
        spPv.setBorder(BorderFactory.createTitledBorder("Xem truoc (bam nut 'Xem truoc bang gia + chi so')"));
        spPv.setPreferredSize(new Dimension(300, 150));
        cfgBox.add(spPv, BorderLayout.CENTER);

        // --- 2. Bang bac: moi moc cap duoc cong option nao ---
        JPanel bacBox = new JPanel(new BorderLayout(6, 6));
        bacBox.setBackground(CARD);
        bacBox.setBorder(BorderFactory.createTitledBorder("2. Moi bac tang option nao (dat cap >= moc la duoc cong option do)"));
        JPanel bacTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bacTop.setBackground(CARD);
        JButton bAdd = btn("Them bac (chon option bang menu)");
        bAdd.setBackground(new Color(70, 120, 220)); bAdd.setForeground(Color.WHITE);
        JButton bEdit = btn("Sua bac");
        JButton bDel = btn("Xoa bac");
        JButton bRel = btn("Tai lai");
        bAdd.addActionListener(e -> cmBacDialog(null));
        bEdit.addActionListener(e -> {
            int r = cmBacTable.getSelectedRow();
            if (r < 0 || r >= cmBacCache.size()) { err("Chon 1 bac truoc"); return; }
            cmBacDialog(cmBacCache.get(r));
        });
        bDel.addActionListener(e -> {
            int r = cmBacTable.getSelectedRow();
            if (r < 0 || r >= cmBacCache.size()) { err("Chon 1 bac truoc"); return; }
            final int id = ((Number) cmBacCache.get(r).get("id")).intValue();
            if (JOptionPane.showConfirmDialog(this, "Xoa bac id=" + id + "?", "Xac nhan", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            bg(() -> { String s = PanelService.deleteCungMenhBac(id); SwingUtilities.invokeLater(() -> { info(s); loadCmBacs(); }); });
        });
        bRel.addActionListener(e -> bg(this::loadCmBacs));
        bacTop.add(bAdd); bacTop.add(bEdit); bacTop.add(bDel); bacTop.add(bRel);
        bacBox.add(bacTop, BorderLayout.NORTH);
        cmBacModel = new DefaultTableModel(new String[]{"ID", "Moc cap (bac)", "Option", "Param", "Mo ta option", "Ghi chu"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        cmBacTable = new JTable(cmBacModel);
        cmBacTable.setRowHeight(24);
        cmBacTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cmBacTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int r = cmBacTable.getSelectedRow();
                    if (r >= 0 && r < cmBacCache.size()) cmBacDialog(cmBacCache.get(r));
                }
            }
        });
        bacBox.add(new JScrollPane(cmBacTable), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, cfgBox, bacBox);
        split.setResizeWeight(0.55);
        split.setBorder(null);
        body.add(split, BorderLayout.CENTER);
        lbCmStatus = new JLabel("...");
        lbCmStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbCmStatus.setForeground(new Color(90, 100, 130));
        lbCmStatus.setBorder(new EmptyBorder(4, 6, 0, 0));
        body.add(lbCmStatus, BorderLayout.SOUTH);
        bg(this::loadCmConfig);
        bg(this::loadCmBacs);
        return wrapPage(body, "Cung Menh (NPC Bo Mong - Rung Karin)", "Sua bao cap / gia moi cap / % HP-KI-SD moi cap / option tang theo tung bac. Luu xong server tu nap lai, khong can build lai.");
    }

    private void loadCmConfig() {
        Map<String, Object> c = PanelService.cungMenhConfig();
        SwingUtilities.invokeLater(() -> {
            cmMax.setText(String.valueOf(c.get("max_level")));
            cmHpP.setText(String.valueOf(c.get("hp_percent")));
            cmKiP.setText(String.valueOf(c.get("ki_percent")));
            cmDameP.setText(String.valueOf(c.get("dame_percent")));
            cmHpF.setText(String.valueOf(c.get("hp_flat")));
            cmKiF.setText(String.valueOf(c.get("ki_flat")));
            cmDameF.setText(String.valueOf(c.get("dame_flat")));
            cmManhBase.setText(String.valueOf(c.get("manh_base")));
            cmManhStep.setText(String.valueOf(c.get("manh_step")));
            cmManhExtra.setText(String.valueOf(c.get("manh_extra")));
            cmDpEvery.setText(String.valueOf(c.get("dot_pha_every")));
            cmDpManhBase.setText(String.valueOf(c.get("dot_pha_manh_base")));
            cmDpManhStep.setText(String.valueOf(c.get("dot_pha_manh_step")));
            cmDpNgocBase.setText(String.valueOf(c.get("dot_pha_ngoc_base")));
            cmDpNgocStep.setText(String.valueOf(c.get("dot_pha_ngoc_step")));
            if (lbCmStatus != null) lbCmStatus.setText("Da tai cau hinh Cung Menh tu DB");
        });
    }

    private void saveCmConfig() {
        try {
            int maxLevel = Integer.parseInt(cmMax.getText().trim().replaceAll("[^0-9]", ""));
            if (maxLevel < 1) { err("Bao cap phai >= 1"); return; }
            double hpP = Double.parseDouble(cmHpP.getText().trim().replaceAll("[^0-9.\\-]", ""));
            double kiP = Double.parseDouble(cmKiP.getText().trim().replaceAll("[^0-9.\\-]", ""));
            double dameP = Double.parseDouble(cmDameP.getText().trim().replaceAll("[^0-9.\\-]", ""));
            long hpF = Long.parseLong(cmHpF.getText().trim().replaceAll("[^0-9\\-]", ""));
            long kiF = Long.parseLong(cmKiF.getText().trim().replaceAll("[^0-9\\-]", ""));
            long dameF = Long.parseLong(cmDameF.getText().trim().replaceAll("[^0-9\\-]", ""));
            int manhBase = Integer.parseInt(cmManhBase.getText().trim().replaceAll("[^0-9]", ""));
            int manhStep = Integer.parseInt(cmManhStep.getText().trim().replaceAll("[^0-9]", ""));
            int manhExtra = Integer.parseInt(cmManhExtra.getText().trim().replaceAll("[^0-9]", ""));
            int dpEvery = Integer.parseInt(cmDpEvery.getText().trim().replaceAll("[^0-9]", ""));
            int dpManhBase = Integer.parseInt(cmDpManhBase.getText().trim().replaceAll("[^0-9]", ""));
            int dpManhStep = Integer.parseInt(cmDpManhStep.getText().trim().replaceAll("[^0-9\\-]", ""));
            int dpNgocBase = Integer.parseInt(cmDpNgocBase.getText().trim().replaceAll("[^0-9]", ""));
            int dpNgocStep = Integer.parseInt(cmDpNgocStep.getText().trim().replaceAll("[^0-9\\-]", ""));
            bg(() -> {
                String r = PanelService.saveCungMenhConfig(maxLevel, hpP, kiP, dameP, hpF, kiF, dameF,
                        manhBase, manhStep, manhExtra, dpEvery, dpManhBase, dpManhStep, dpNgocBase, dpNgocStep);
                SwingUtilities.invokeLater(() -> {
                    info(r);
                    if (lbCmStatus != null) lbCmStatus.setText(r);
                    cmPreview.setText(buildCmPreview());
                });
            });
        } catch (Exception ex) {
            err("Chi duoc dien so. Loi: " + ex.getMessage());
        }
    }

    private void loadCmBacs() {
        java.util.List<Map<String, Object>> list = PanelService.listCungMenhBac();
        cmBacCache = list;
        SwingUtilities.invokeLater(() -> {
            cmBacModel.setRowCount(0);
            for (Map<String, Object> m : list) {
                int oid = ((Number) m.get("option_id")).intValue();
                cmBacModel.addRow(new Object[]{m.get("id"), m.get("level"), "#" + oid, m.get("param"),
                        PanelService.optionDisplay(oid), m.get("note")});
            }
            if (lbCmStatus != null) lbCmStatus.setText("Tong " + list.size() + " bac option dang bat");
        });
    }

    /** Hop thoai them/sua bac: chi dien moc cap + so param, option chon bang menu. */
    private void cmBacDialog(Map<String, Object> rowOrNull) {
        final int id = rowOrNull == null ? -1 : ((Number) rowOrNull.get("id")).intValue();
        JTextField fLevel = tf(rowOrNull == null ? "10" : String.valueOf(rowOrNull.get("level")), 6);
        JTextField fParam = tf(rowOrNull == null ? "0" : String.valueOf(rowOrNull.get("param")), 10);
        JTextField fNote = tf(rowOrNull == null ? "" : String.valueOf(rowOrNull.get("note")), 20);
        JComboBox<String> cbOpt = new JComboBox<>();
        cbOpt.setPrototypeDisplayValue("#000 Ten option | ten viet (goi y param)........");
        cbOpt.setPreferredSize(new java.awt.Dimension(420, 25));
        cbOpt.setMaximumRowCount(20);
        java.util.List<Integer> ids = new java.util.ArrayList<>();
        if (optDictCache == null || optDictCache.isEmpty()) optDictCache = PanelService.listOptionDict();
        for (Map<String, Object> o : optDictCache) {
            int oid = ((Number) o.get("id")).intValue();
            ids.add(oid);
            String tv = o.get("ten_viet") == null ? "" : String.valueOf(o.get("ten_viet"));
            String nm = o.get("name") == null ? "" : String.valueOf(o.get("name"));
            String hint = o.get("goi_y") == null ? "" : String.valueOf(o.get("goi_y"));
            cbOpt.addItem("#" + oid + " " + nm + (tv.isEmpty() ? "" : " | " + tv) + (hint.isEmpty() ? "" : " (" + hint + ")"));
        }
        if (rowOrNull != null) {
            int cur = ((Number) rowOrNull.get("option_id")).intValue();
            int idx = ids.indexOf(cur);
            if (idx >= 0) cbOpt.setSelectedIndex(idx);
        }
        JPanel form = new JPanel(new java.awt.GridLayout(0, 1, 4, 4));
        JPanel r1 = new JPanel(new FlowLayout(FlowLayout.LEFT)); r1.add(new JLabel("Moc cap:")); r1.add(fLevel);
        r1.add(new JLabel("  (vi du 10 = dat cap 10 tro len la duoc cong)"));
        JPanel r2 = new JPanel(new FlowLayout(FlowLayout.LEFT)); r2.add(new JLabel("Option:")); r2.add(cbOpt);
        JPanel r3 = new JPanel(new FlowLayout(FlowLayout.LEFT)); r3.add(new JLabel("Param:")); r3.add(fParam);
        r3.add(new JLabel("  (chi so cua option, xem goi y trong menu)"));
        JPanel r4 = new JPanel(new FlowLayout(FlowLayout.LEFT)); r4.add(new JLabel("Ghi chu:")); r4.add(fNote);
        form.add(r1); form.add(r2); form.add(r3); form.add(r4);
        String title = (rowOrNull == null ? "Them bac moi" : "Sua bac id=" + id);
        int c = JOptionPane.showConfirmDialog(this, form, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (c != JOptionPane.OK_OPTION) return;
        try {
            int level = Integer.parseInt(fLevel.getText().trim().replaceAll("[^0-9]", ""));
            int idx = cbOpt.getSelectedIndex();
            if (idx < 0 || idx >= ids.size()) { err("Chon 1 option"); return; }
            int oid = ids.get(idx);
            long param = Long.parseLong(fParam.getText().trim().replaceAll("[^0-9\\-]", ""));
            String note = fNote.getText().trim();
            bg(() -> {
                String r = PanelService.saveCungMenhBac(id, level, oid, param, note);
                SwingUtilities.invokeLater(() -> { info(r); loadCmBacs(); });
            });
        } catch (Exception ex) {
            err("Moc cap va param phai la so");
        }
    }

    /** Xem truoc bang gia + chi so tu cac o dang dien (chua luu). */
    private String buildCmPreview() {
        StringBuilder sb = new StringBuilder();
        try {
            int maxLevel = Integer.parseInt(cmMax.getText().trim().replaceAll("[^0-9]", ""));
            double hpP = Double.parseDouble(cmHpP.getText().trim().replaceAll("[^0-9.\\-]", ""));
            double kiP = Double.parseDouble(cmKiP.getText().trim().replaceAll("[^0-9.\\-]", ""));
            double dameP = Double.parseDouble(cmDameP.getText().trim().replaceAll("[^0-9.\\-]", ""));
            long hpF = Long.parseLong(cmHpF.getText().trim().replaceAll("[^0-9\\-]", ""));
            long kiF = Long.parseLong(cmKiF.getText().trim().replaceAll("[^0-9\\-]", ""));
            long dameF = Long.parseLong(cmDameF.getText().trim().replaceAll("[^0-9\\-]", ""));
            int manhBase = Integer.parseInt(cmManhBase.getText().trim().replaceAll("[^0-9]", ""));
            int manhStep = Math.max(1, Integer.parseInt(cmManhStep.getText().trim().replaceAll("[^0-9]", "")));
            int manhExtra = Integer.parseInt(cmManhExtra.getText().trim().replaceAll("[^0-9]", ""));
            int dpEvery = Math.max(1, Integer.parseInt(cmDpEvery.getText().trim().replaceAll("[^0-9]", "")));
            int dpManhBase = Integer.parseInt(cmDpManhBase.getText().trim().replaceAll("[^0-9]", ""));
            int dpManhStep = Integer.parseInt(cmDpManhStep.getText().trim().replaceAll("[^0-9\\-]", ""));
            int dpNgocBase = Integer.parseInt(cmDpNgocBase.getText().trim().replaceAll("[^0-9]", ""));
            int dpNgocStep = Integer.parseInt(cmDpNgocStep.getText().trim().replaceAll("[^0-9\\-]", ""));
            sb.append("Bao cap: ").append(maxLevel).append(" - moi cap: +")
              .append(fmt(hpF)).append(" HP (" ).append(hpP).append("%), +")
              .append(fmt(kiF)).append(" KI (").append(kiP).append("%), +")
              .append(fmt(dameF)).append(" SD (").append(dameP).append("%)\n");
            sb.append("Gia nang cap: ").append(manhBase).append(" manh, cu ")
              .append(manhStep).append(" cap + ").append(manhExtra).append(" manh\n--- Bang 10 cap dau ---\n");
            for (int lv = 1; lv <= Math.min(10, maxLevel); lv++) {
                int need = manhBase + ((lv - 1) / manhStep) * manhExtra;
                sb.append("Len cap ").append(lv).append(": ").append(need).append(" manh")
                  .append(lv % dpEvery == 0 ? "  [phai dot pha " + (lv / dpEvery) + " truoc]" : "").append("\n");
            }
            sb.append("--- Dot pha (cu ").append(dpEvery).append(" cap) ---\n");
            for (int n = 1; n * dpEvery <= maxLevel && n <= 12; n++) {
                int needM = dpManhBase + (n - 1) * dpManhStep;
                int needN = dpNgocBase + (n - 1) * dpNgocStep;
                sb.append("Dot pha lan ").append(n).append(" (moc cap ").append(n * dpEvery).append("): ")
                  .append(needM).append(" manh + ").append(needN).append(" ngoc\n");
            }
            sb.append("--- Bac option dang bat ---\n");
            if (cmBacCache.isEmpty()) {
                sb.append("(chua co bac nao - them o bang 2)\n");
            } else {
                for (Map<String, Object> m : cmBacCache) {
                    sb.append("cap ").append(m.get("level")).append(" -> option #")
                      .append(m.get("option_id")).append(" param ").append(m.get("param")).append("\n");
                }
            }
        } catch (Exception e) {
            sb.append("Dien day du cac o so roi bam lai.");
        }
        return sb.toString();
    }

    private String fmt(long n) {
        return String.format("%,d", n).replace(',', '.');
    }

    // ================= DONATE =================
    private DefaultTableModel topModel;
    private JTable topTable;
    private JTextField napIdField;
    private JLabel napState = new JLabel(" ");
    private final Map<String, JLabel> napCur = new java.util.LinkedHashMap<>();
    private final Map<String, JTextField> napIn = new java.util.LinkedHashMap<>();

    private JPanel buildDonate() {
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setBackground(CARD);
        body.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));

        // --- dong 1: chon account + tai hien trang
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        napIdField = tf(8);
        JButton bLoad = btn("Tải hiện trạng");
        bLoad.setBackground(new Color(40, 120, 200)); bLoad.setForeground(Color.WHITE);
        JButton bReload = btn("Tai lai Top Nap");
        bLoad.addActionListener(e -> refreshNap());
        bReload.addActionListener(e -> bg(this::loadTop));
        top.add(new JLabel("Account ID:")); top.add(napIdField);
        top.add(bLoad); top.add(bReload); top.add(napState);

        // --- dong 2: nap day du (danh nhieu phan cung luc, giong nap that)
        JPanel full = new JPanel(new FlowLayout(FlowLayout.LEFT));
        full.setBackground(CARD);
        JTextField fAmount = tf("20000", 10);
        JComboBox<String> cbTarget = new JComboBox<>(new String[]{"vnd (vao so du ngay)", "temp_vnd (so du cho)"});
        JCheckBox cTong = new JCheckBox("Tong nap", true);
        JCheckBox cDanap = new JCheckBox("Da nap (top web)", true);
        JCheckBox cActive = new JCheckBox("Mo thanh vien", true);
        JCheckBox cReset = new JCheckBox("Dat lai qua nap dau", false);
        JButton bFull = btn("NẠP ĐẦY ĐỦ");
        bFull.setBackground(new Color(40, 150, 80)); bFull.setForeground(Color.WHITE);
        bFull.addActionListener(e -> bg(() -> {
            String r;
            try {
                int id = Integer.parseInt(napIdField.getText().trim());
                long amount = Long.parseLong(fAmount.getText().trim().replace(".", ""));
                r = PanelService.napFull(id, amount, cbTarget.getSelectedIndex() == 0,
                        cTong.isSelected(), cDanap.isSelected(), cActive.isSelected(), cReset.isSelected());
            } catch (Exception ex) {
                r = "Loi: " + ex.getMessage();
            }
            final String rr = r;
            SwingUtilities.invokeLater(() -> { info(rr); refreshNap(); loadTop(); });
        }));
        full.add(new JLabel("So tien:")); full.add(fAmount);
        full.add(new JLabel("Vao:")); full.add(cbTarget);
        full.add(cTong); full.add(cDanap); full.add(cActive); full.add(cReset);
        full.add(bFull);

        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        north.setBackground(CARD);
        top.setAlignmentX(Component.LEFT_ALIGNMENT);
        full.setAlignmentX(Component.LEFT_ALIGNMENT);
        north.add(top);
        north.add(full);
        body.add(north, BorderLayout.NORTH);

        // --- tung phan tien: sua rieng le tung cai
        JPanel parts = new JPanel();
        parts.setLayout(new BoxLayout(parts, BoxLayout.Y_AXIS));
        parts.setBackground(CARD);
        JLabel h = new JLabel("Tung phan - sua rieng le   (Cong = + o nhap, Dat = ghi de gia tri; moc nap / qua nap dau / phuc loi doc tu TONG NAP)");
        h.setFont(new Font("Segoe UI", Font.BOLD, 12));
        h.setBorder(new EmptyBorder(4, 4, 4, 4));
        h.setAlignmentX(Component.LEFT_ALIGNMENT);
        parts.add(h);
        for (String[] p : PanelService.napParts()) {
            parts.add(partRow(p[0], p[1]));
        }
        JScrollPane partScroll = new JScrollPane(parts);
        partScroll.setBorder(BorderFactory.createEmptyBorder());
        body.add(partScroll, BorderLayout.CENTER);

        // --- bang top nap
        topModel = new DefaultTableModel(new String[]{"ID", "Username", "VND", "TongNap", "VIP"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        topTable = new JTable(topModel);
        JScrollPane topScroll = new JScrollPane(topTable);
        topScroll.setPreferredSize(new Dimension(200, 170));
        body.add(topScroll, BorderLayout.SOUTH);

        bg(this::loadTop);
        return wrapPage(body, "Nap Tien / Buff VND", "Tung phan rieng le + nap day du (so du, cho nap, tong nap, top web, mo TV, VIP, qua nap dau)");
    }

    private JPanel partRow(String key, String desc) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        row.setBackground(CARD);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel name = new JLabel(desc);
        name.setPreferredSize(new Dimension(280, 22));
        JLabel cur = new JLabel("-");
        cur.setForeground(new Color(90, 100, 120));
        cur.setPreferredSize(new Dimension(150, 22));
        JTextField in = tf("0", 10);
        JButton bAdd = btn("+ Cong");
        bAdd.setBackground(new Color(40, 150, 80)); bAdd.setForeground(Color.WHITE);
        JButton bSet = btn("Dat");
        bSet.setBackground(new Color(210, 130, 30)); bSet.setForeground(Color.WHITE);
        napCur.put(key, cur);
        napIn.put(key, in);
        bAdd.addActionListener(e -> applyNapPart(key, in, true));
        bSet.addActionListener(e -> applyNapPart(key, in, false));
        row.add(name); row.add(cur); row.add(new JLabel("O nhap:")); row.add(in); row.add(bAdd); row.add(bSet);
        return row;
    }

    private void applyNapPart(String key, JTextField in, boolean add) {
        final int id;
        final long v;
        try {
            id = Integer.parseInt(napIdField.getText().trim());
            v = Long.parseLong(in.getText().trim().replace(".", ""));
        } catch (Exception ex) {
            err("Loi: can nhap Account ID va o nhap la so. " + ex.getMessage());
            return;
        }
        bg(() -> {
            String r = PanelService.napApply(id, key, v, add);
            final String rr = r;
            SwingUtilities.invokeLater(() -> { info(rr); refreshNap(); loadTop(); });
        });
    }

    /** Tai hien trang cac phan tien cua account dang nhap o o Account ID. */
    private void refreshNap() {
        final int id;
        try {
            id = Integer.parseInt(napIdField.getText().trim());
        } catch (Exception ex) {
            err("Nhap Account ID (so) truoc");
            return;
        }
        bg(() -> {
            Map<String, Object> st = PanelService.napStatus(id);
            SwingUtilities.invokeLater(() -> {
                Object er = st.get("error");
                if (er != null) { err("Loi: " + er); return; }
                if (!Boolean.TRUE.equals(st.get("found"))) { err("Khong tim thay account id " + id); return; }
                for (Map.Entry<String, JLabel> e : napCur.entrySet()) {
                    Object v = st.get(e.getKey());
                    e.getValue().setText(v == null ? "-" : fmt(((Number) v).longValue()));
                }
                napState.setText(Boolean.TRUE.equals(st.get("online")) ? "   [player ONLINE]" : "   [player offline]");
            });
        });
    }

    private void loadTop() {
        List<Map<String, Object>> list = PanelService.topNap(100);
        SwingUtilities.invokeLater(() -> {
            topModel.setRowCount(0);
            for (Map<String, Object> m : list) topModel.addRow(new Object[]{m.get("id"), m.get("username"), m.get("vnd"), m.get("tongnap"), m.get("vip")});
        });
    }

    // ================= refresh dashboard =================
    private void refreshAll() {
        try {
            int online = PanelData.playersOnline();
            int sess = PanelData.sessions();
            lbClock.setText(PanelData.clockText());
            lbOnlineTop.setText(online + " online");
            boolean maint = PanelData.maintenanceRunning();
            lbServerStatus.setText(maint ? "Maintenance ON" : "Maintenance OFF");
            lbPlayers.setText(String.valueOf(online));
            lbNextMaint.setText(PanelData.nextMaintText());
            double cpu = PanelData.cpuPct();
            int used = PanelData.heapUsedMb();
            int max = PanelData.heapMaxMb();
            int th = PanelData.threads();
            lbCpu.setText(String.format("%.1f%%", cpu));
            lbRam.setText(used + " / " + max + " MB");
            lbThreads.setText(String.valueOf(th));
            lbSessions.setText(String.valueOf(sess));
            barCpu.setValue((int) Math.min(100, cpu));
            barRam.setValue(max > 0 ? Math.min(100, used * 100 / Math.max(1, max)) : 0);
            barThread.setValue(Math.min(100, th * 100 / Math.max(1, 300)));
            barSession.setValue(Math.min(100, sess * 100 / Math.max(1, 2000)));
            lbIps.setText(PanelData.ipCount() + " | Max/IP: " + PanelData.maxPerIp());
            long delay = PanelData.dbDelayMs();
            lbDelay.setText(delay < 0 ? "DB loi" : delay + " ms");
            int[] bs = PanelData.bossStats();
            lbGift.setText("Giftcodes: " + PanelData.giftcodes());
            lbBoss.setText("Boss: " + bs[0] + " Alive | " + bs[1] + " Respawn | " + bs[2] + " Wait");
            lbConsign.setText("Consign Items: " + PanelData.consign());
            lbUptime.setText("Uptime: " + PanelData.uptimeText());
            btnAutoMaint.setText("AutoSave: " + (server.AutoMaintenance.AutoMaintenance ? "ON" : "OFF"));
            btnAutoClean.setText("AutoClean SS: " + (PanelData.AUTO_CLEAN_SS ? "ON" : "OFF"));
        } catch (Exception e) {}
    }

    private void doMaintCustom() {
        try {
            String s = JOptionPane.showInputDialog(this, "Nhap so phut bao tri (vi du 5):", "5");
            if (s == null) return;
            int min = Integer.parseInt(s.trim());
            new Thread(() -> server.Maintenance.gI().start(min)).start();
            info("Da hen bao tri sau " + min + " phut.");
        } catch (Exception ex) {
            err("Loi: " + ex.getMessage());
        }
    }

    private void doMaintNow() {
        try {
            int c = JOptionPane.showConfirmDialog(this, "Bao tri NGAY, kick all player?", "Xac nhan", JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION) return;
            new Thread(() -> server.Maintenance.gI().startImmediately()).start();
        } catch (Exception ex) {
            err("Loi: " + ex.getMessage());
        }
    }

    private void doReloadDb() {
        new Thread(() -> {
            String r = PanelService.reloadShop();
            SwingUtilities.invokeLater(() -> info(r));
        }).start();
    }

    private void doCleanSession() {
        new Thread(() -> {
            try {
                network.SessionManager.gI().cleanupSessions();
                SwingUtilities.invokeLater(() -> info("Da don session chet."));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> err("Loi: " + ex.getMessage()));
            }
        }).start();
    }

    private void showNewShopDialog() {
        JDialog d = new JDialog(this, "Tao shop moi (tu NPC co san)", true);
        d.setSize(560, 460);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout(8, 8));
        JPanel f = new JPanel(new GridBagLayout());
        f.setBorder(new EmptyBorder(12, 12, 12, 12));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4); gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1;
        JTextField fNpc = tf(10), fTag = tf(16), fName = tf(16), fType = tf(4);
        JTextArea fItems = new JTextArea(6, 30); fItems.setLineWrap(true); fItems.setWrapStyleWord(true);
        fItems.setText("[{\"temp_id\":14,\"cost\":100,\"item_spec\":0,\"type_sell\":0,\"is_sell\":true,\"options\":[]}]");
        addRow(f, gc, 0, "NPC ID:", fNpc);
        addRow(f, gc, 1, "Tag (ShopService.opendShop):", fTag);
        addRow(f, gc, 2, "Ten shop:", fName);
        addRow(f, gc, 3, "Type shop:", fType);
        addRow(f, gc, 4, "Items JSON [{temp_id,cost,item_spec,type_sell,is_sell,options}]:", new JScrollPane(fItems));
        addRow(f, gc, 5, "Tien te (type_sell): 0=thoi vang,1=ngoc xanh,3=rubi,4=luong vang,5=luong bac,6=co 4 la", new JLabel(""));
        d.add(new JScrollPane(f), BorderLayout.CENTER);
        JPanel acts = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bOk = btn("Tao shop");
        bOk.setBackground(new Color(40, 140, 70)); bOk.setForeground(Color.WHITE);
        JButton bCancel = btn("Huy");
        bOk.addActionListener(e -> {
            try {
                int npc = Integer.parseInt(fNpc.getText().trim());
                String tag = fTag.getText().trim();
                String name = fName.getText().trim();
                int type = Integer.parseInt(fType.getText().trim());
                String items = fItems.getText().trim();
                if (tag.isEmpty()) { info("Tag khong duoc trong"); return; }
                final String itemsJson = items;
                bg(() -> {
                    String r = PanelService.createShop(npc, tag, name, type, itemsJson);
                    SwingUtilities.invokeLater(() -> {
                        info(r);
                        d.dispose();
                        bg(this::loadNpcShops);
                    });
                });
            } catch (Exception ex) { info("Loi: " + ex.getMessage()); }
        });
        bCancel.addActionListener(e -> d.dispose());
        acts.add(bOk); acts.add(bCancel);
        d.add(acts, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private static ControlPanel instance;

    // ================= QUAN LY NHAN VAT (online + offline) =================
    private JTextField pmSearch;
    private DefaultTableModel pmModel;
    private JTable pmTable;

    // ================= HUONG DAN SU DUNG (cho nguoi moi) =================
    private JPanel buildHelp() {
        String guide = String.join("\n",
            "=================================================================",
            " HUONG DAN SU DUNG PANEL - Server BeMeoGaming",
            "=================================================================",
            "",
            "QUY UOC CHUNG",
            " - Moi man co dong tieu de + dong mo ta duoi cung man (doc truoc khi bam).",
            " - Buoc 1: nhap tu khoa / chon dong trong bang. Buoc 2: bam nut hanh dong.",
            " - Cac thao tac nguy hiem (Xoa, Kick, Reset) deu hoi Xac nhan truoc.",
            " - Cong viec chay ngam (tai bang, truy van DB) -> doi 1-3 giay se co ket qua.",
            " - Moi thao tac quan tri deu duoc ghi vao man 'Nhat Ky Admin' (AUDIT).",
            "",
            "-------------------- TONG QUAN --------------------",
            " - Bang Dieu Khien: trang thai server, CPU/RAM, nut tu dong bao tri / don rac.",
            " - Nhat Ky Admin: lich su admin da lam gi (loc theo hanh dong).",
            " - Huong Dan Su Dung: man hinh nay.",
            "",
            "-------------------- QUAN LY --------------------",
            " - Quan Ly Tai Khoan: tim theo username/ID. Tao TK moi (o Ten TK + Pass),",
            "   doi ten, doi pass, xoa TK, ban/unban, set admin, buff VND/TongNap/VIP.",
            " - Danh Sach Nguoi Choi: xem ai dang ONLINE, kick ca server, gui thong bao ALL.",
            " - Quan Ly Nhan Vat: sua chi so/tien/skill, tang item, xoa nhan vat (online va offline).",
            " - Hanh Trang / Ruong: xem + xoa + them do o Tui do / Do mac / RUONG / Da ban.",
            "   Nhap ten TAI KHOAN de xem toan bo nhan vat cua account do.",
            " - Cua Hang (Shop): sua item shop, gia, option.",
            " - Thu Vien Vat Pham / Tu Dien Option: tra cuu ID vat pham / ID option (nhap tu khoa).",
            " - Tao Ruong (Box): tao ruong qua tang cho server.",
            " - NPC + Shop, Cau Hinh Boss, Map & Mob, Su Kien: cau hinh noi dung game.",
            " - Cong Thuc (Formulas): sua cong tinh sat thuong / chi so.",
            " - Chi So (Dame/HP/KI): sua tile hien thi / thuc te -> Luu la ap dung ngay.",
            " - Vong Quay (Tam Bao), Don Rac (xoa item rac).",
            "",
            "-------------------- GIAO DICH --------------------",
            " - Quan Ly Giftcode: tao/sua code, so luong luot, qua kem option.",
            " - Phuc Loi (Qua Online), Gui Qua (Mail): gui qua cho player/100 nguoi online.",
            " - Lich Su Giao Dich: chon loai (Trade / Nap the / Bank / MoMo / Order / Web shop),",
            "   nhap ten de loc, chon so dong roi bam 'Tai lich su'.",
            "",
            "-------------------- TINH NANG GAME --------------------",
            " - Cung Menh: xem/sua bac cung menh, config % HP/KI/SD moi cap, dot pha.",
            " - Set Do 5 Mon: cau hinh option bo 5 mon.",
            " - He Thong: BO NHOM thong so (Thien/Đia Dao, Chuyen Sinh, Tu Tien,",
            "   Dao Lu/Em Be, Ket Hon, Cay Phep, VIP, Diem Farm...).",
            "   Chon nhom -> sua cot 'Gia tri' -> Luu = ap dung NGAY, khong can build lai.",
            " - Nap Tien / Buff VND: nap tien, kiem tra lich su nap.",
            "",
            "-------------------- CANH BAO --------------------",
            " - Khong dua mat khau/ma OTP cho ai qua chat in-game.",
            " - 'Xoa TK' chi xoa bang account, nhan vat duoc giu lai de an toan.",
            " - Doi ten/doi pass co the nguoi choi dang dang nhap -> cho vao lai se bi dua ra man hinh login.",
            " - Moi sua so lieu he thong (tab He Thong) nen ghi nho gia tri mac dinh truoc khi doi",
            "   (cot 'Mac dinh' ben canh de tra ve bat cu luc).",
            "================================================================="
        );
        JTextArea ta = new JTextArea(guide);
        ta.setEditable(false);
        ta.setFont(new Font("Consolas", Font.PLAIN, 13));
        ta.setBackground(CARD);
        ta.setForeground(new Color(30, 34, 45));
        ta.setCaretPosition(0);
        JScrollPane sp = new JScrollPane(ta);
        sp.setBorder(new EmptyBorder(4, 4, 4, 4));
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(CARD);
        body.add(sp, BorderLayout.CENTER);
        return wrapPage(body, "Huong Dan Su Dung", "Doc truoc khi dung cac man khac - tong quan tung chuc nang va canh bao quan trong");
    }

    // ================= HANH TRANG / RUONG (toan bo account) =================
    private DefaultTableModel invCharModel, invItemModel;
    private JTable invCharTable, invItemTable;
    private JTextField invSearch;
    private JComboBox<String> invLoc;
    private JLabel invInfo;
    private String invCurName;

    private static final String[] INV_KEYS = {"bag", "body", "box", "lucky", "daban"};
    private static final String[] INV_NAMES = {
        "Tui do (items_bag)", "Do mac (items_body)", "RUONG (items_box)",
        "Ruong lucky (crack)", "Do da ban (items_daban)"};

    private JPanel buildInventory() {
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setBackground(CARD);
        body.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        invSearch = tf(20);
        JButton bFind = btn("Tim nhan vat");
        invLoc = new JComboBox<>(INV_NAMES);
        JButton bView = btn("Xem noi dung");
        top.add(new JLabel("Ten NV / Ten tai khoan:")); top.add(invSearch); top.add(bFind);
        top.add(new JLabel("  Vi tri:")); top.add(invLoc); top.add(bView);
        body.add(top, BorderLayout.NORTH);

        invCharModel = new DefaultTableModel(new String[]{"Ten NV", "Tai khoan", "Power", "Trang thai"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        invCharTable = new JTable(invCharModel);
        invItemModel = new DefaultTableModel(new String[]{"Slot", "ID", "Ten vat pham", "SL", "Options", "Tao luc"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        invItemTable = new JTable(invItemModel);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(invCharTable), new JScrollPane(invItemTable));
        split.setResizeWeight(0.32);
        split.setBorder(BorderFactory.createTitledBorder("Chon nhan vat (tra theo TEN NV hoac TEN TAI KHOAN) | noi dung cua vi tri da chon"));
        body.add(split, BorderLayout.CENTER);

        invCharTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) loadInvItems();
            }
        });

        JPanel bot = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bot.setBackground(CARD);
        JButton bReload = btn("Tai lai danh sach NV");
        JButton bDel = btn("Xoa vat pham da chon");
        JButton bGive = btn("Them vat pham vao vi tri nay");
        JButton bKick = btn("Kick neu online");
        bDel.setBackground(new Color(220, 60, 70)); bDel.setForeground(Color.WHITE);
        bFind.addActionListener(e -> loadInvChars());
        bReload.addActionListener(e -> loadInvChars());
        bView.addActionListener(e -> loadInvItems());
        bDel.addActionListener(e -> invDeleteItem());
        bGive.addActionListener(e -> invGiveItem());
        bKick.addActionListener(e -> {
            String n = invCurName != null ? invCurName : selInvCharName();
            if (n == null) return;
            bg(() -> { String r = PanelService.kickPlayer(n); SwingUtilities.invokeLater(() -> info(r)); });
        });
        invInfo = new JLabel("Chon 1 nhan vat ben trai -> chon vi tri -> bam 'Xem noi dung'. Nhap ten TAI KHOAN de xay toan bo nhan vat cua account.");
        invInfo.setForeground(new Color(90, 100, 130));
        bot.add(bReload); bot.add(bView); bot.add(bDel); bot.add(bGive); bot.add(bKick); bot.add(invInfo);
        body.add(bot, BorderLayout.SOUTH);
        bg(this::loadInvChars);
        return wrapPage(body, "Hanh Trang / Ruong (toan bo account)",
                "Xem + xoa + them vat pham o Tui do / Do mac / RUONG / Ruong lucky / Do da ban - tim theo ten NV hoac ten tai khoan");
    }

    private void loadInvChars() {
        final String q = invSearch == null ? "" : invSearch.getText();
        bg(() -> {
            List<Map<String, Object>> list = PanelService.searchPlayers(q, 1000);
            SwingUtilities.invokeLater(() -> {
                invCharModel.setRowCount(0);
                for (Map<String, Object> m : list) {
                    invCharModel.addRow(new Object[]{m.get("name"), m.get("username"), m.get("power"),
                        Boolean.TRUE.equals(m.get("online")) ? "ONLINE" : "Offline"});
                }
                invItemModel.setRowCount(0);
                invCurName = null;
                invInfo.setText("Tim thay " + list.size() + " nhan vat. Chon 1 dong -> 'Xem noi dung'.");
            });
        });
    }

    private String selInvCharName() {
        int r = invCharTable.getSelectedRow();
        if (r < 0) { err("Chon 1 nhan vat trong bang ben trai"); return null; }
        return String.valueOf(invCharModel.getValueAt(r, 0));
    }

    private void loadInvItems() {
        final String n = selInvCharName();
        if (n == null) return;
        invCurName = n;
        final String key = INV_KEYS[invLoc.getSelectedIndex()];
        final String locName = INV_NAMES[invLoc.getSelectedIndex()];
        bg(() -> {
            final Map<String, Object> v = PanelService.viewPlayer(n);
            SwingUtilities.invokeLater(() -> {
                invItemModel.setRowCount(0);
                if (v == null) { err("Khong tim thay player: " + n); return; }
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> items = (List<Map<String, Object>>) v.get(key);
                int count = 0;
                if (items != null) {
                    for (Map<String, Object> im : items) {
                        invItemModel.addRow(new Object[]{im.get("slot"), im.get("id"), im.get("name"), im.get("qty"), im.get("opts"), im.get("time")});
                        count++;
                    }
                }
                invInfo.setText("NV " + n + " [" + v.get("online") + "] - " + locName + ": " + count + " vat pham"
                        + (key.equals("lucky") || key.equals("daban") ? " (vi tri chi xem - khong gui realtime)" : ""));
            });
        });
    }

    private void invDeleteItem() {
        final String n = invCurName != null ? invCurName : selInvCharName();
        if (n == null) return;
        int r = invItemTable.getSelectedRow();
        if (r < 0) { err("Chon 1 vat pham can xoa"); return; }
        final int slot = ((Number) invItemModel.getValueAt(r, 0)).intValue();
        final String key = INV_KEYS[invLoc.getSelectedIndex()];
        final String ten = String.valueOf(invItemModel.getValueAt(r, 2));
        int c = JOptionPane.showConfirmDialog(this, "Xoa '" + ten + "' (slot " + slot + ") khoi " + INV_NAMES[invLoc.getSelectedIndex()] + " cua " + n + "?",
                "Xac nhan", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        bg(() -> { String rr = PanelService.removePlayerItem(n, key, slot); SwingUtilities.invokeLater(() -> { info(rr); loadInvItems(); }); });
    }

    private void invGiveItem() {
        final String n = invCurName != null ? invCurName : selInvCharName();
        if (n == null) return;
        final String key = INV_KEYS[invLoc.getSelectedIndex()];
        if (!key.equals("bag") && !key.equals("box")) {
            err("Chi them vao Tui do hoac Ruong. Doi vi tri roi thu lai.");
            return;
        }
        JTextField fId = tf(8);
        JTextField fQty = tf("1", 6);
        JTextField fOpt = tf(18);
        JPanel pn = new JPanel(new GridLayout(0, 2, 6, 6));
        pn.add(new JLabel("Temp ID vat pham:")); pn.add(fId);
        pn.add(new JLabel("So luong:")); pn.add(fQty);
        pn.add(new JLabel("Options (id:param,id:param):")); pn.add(fOpt);
        int c = JOptionPane.showConfirmDialog(this, pn, "Them vat pham vao " + INV_NAMES[invLoc.getSelectedIndex()] + " cua " + n,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (c != JOptionPane.OK_OPTION) return;
        final String idTxt = fId.getText().trim(), qtyTxt = fQty.getText().trim(), optTxt = fOpt.getText().trim();
        bg(() -> {
            String rr;
            try {
                rr = PanelService.giveItem(n, Integer.parseInt(idTxt), Integer.parseInt(qtyTxt), optTxt, key);
            } catch (Exception ex) { rr = "Loi nhap so: " + ex.getMessage(); }
            final String r2 = rr;
            SwingUtilities.invokeLater(() -> { info(r2); loadInvItems(); });
        });
    }

    // ================= LICH SU GIAO DICH =================
    private DefaultTableModel txModel;
    private JTable txTable;
    private JComboBox<String> txType, txLimit;
    private JTextField txFilter;

    private static final String[] TX_TYPES = {
        "Trade 2 player (history_transaction)", "Nap the (napthe)", "Nap Bank (history_bank)",
        "Don nap MoMo/ZaloPay (order)", "MoMo trans (momo_trans)", "Web shop (web_shop_history)"};
    private static final String[][] TX_HEADERS = {
        {"Thoi gian", "Player 1", "Player 2", "Do cua P1", "Do cua P2"},
        {"Thoi gian", "Tai khoan nap", "Ten ingame", "Nha mang", "So tien", "Status", "Serial", "Code"},
        {"Thoi gian", "Username", "VND", "Cash", "Code", "Mo ta"},
        {"Thoi gian", "AccountID", "OrderId", "Loai", "So tien", "Status", "TransId"},
        {"Thoi gian", "Username", "Transaction", "So tien", "Noi dung"},
        {"Thoi gian", "Username", "Vat pham", "SL", "Don gia", "Tong tien"}};
    private static final String[][] TX_KEYS = {
        {"time", "p1", "p2", "i1", "i2"},
        {"time", "user", "ingame", "telco", "amount", "status", "serial", "code"},
        {"time", "user", "vnd", "cash", "code", "desc"},
        {"time", "acc", "orderid", "type", "amount", "status", "transid"},
        {"time", "user", "txid", "amount", "content"},
        {"time", "user", "item", "amount", "price", "total"}};

    private JPanel buildTransactions() {
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setBackground(CARD);
        body.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        txType = new JComboBox<>(TX_TYPES);
        txFilter = tf(16);
        txLimit = new JComboBox<>(new String[]{"100", "500", "1000", "5000"});
        JButton bLoad = btn("Tai lich su");
        bLoad.setBackground(new Color(70, 120, 220)); bLoad.setForeground(Color.WHITE);
        top.add(new JLabel("Loai giao dich:")); top.add(txType);
        top.add(new JLabel("  Loc (ten / ma):")); top.add(txFilter);
        top.add(new JLabel("  So dong:")); top.add(txLimit); top.add(bLoad);
        body.add(top, BorderLayout.NORTH);
        txModel = new DefaultTableModel(TX_HEADERS[0], 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        txTable = new JTable(txModel);
        body.add(new JScrollPane(txTable), BorderLayout.CENTER);
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bot.setBackground(CARD);
        JLabel hint = new JLabel("Loc trong: ten player, username, so the / ma giao dich. Trade player thi nhap ten 1 trong 2 ben.");
        hint.setForeground(new Color(90, 100, 130));
        bLoad.addActionListener(e -> loadTx());
        txType.addActionListener(e -> loadTx());
        bot.add(bLoad); bot.add(hint);
        body.add(bot, BorderLayout.SOUTH);
        bg(this::loadTx);
        return wrapPage(body, "Lich Su Giao Dich",
                "Xem cac loai: Trade 2 player, Nap the, Nap Bank, Don nap MoMo/Zalo, MoMo trans, Web shop - co bo loc theo ten/ma");
    }

    private void loadTx() {
        final int i = txType.getSelectedIndex();
        final String f = txFilter == null ? "" : txFilter.getText();
        int lim = 100;
        try { lim = Integer.parseInt((String) txLimit.getSelectedItem()); } catch (Exception e) { }
        final int limit = lim;
        bg(() -> {
            final List<Map<String, Object>> rows;
            switch (i) {
                case 0: rows = PanelService.listTrades(f, limit); break;
                case 1: rows = PanelService.listNapThe(f, limit); break;
                case 2: rows = PanelService.listBankTx(f, limit); break;
                case 3: rows = PanelService.listOrderTx(f, limit); break;
                case 4: rows = PanelService.listMomoTx(f, limit); break;
                default: rows = PanelService.listWebShopTx(f, limit); break;
            }
            final String[] heads = TX_HEADERS[i];
            final String[] keys = TX_KEYS[i];
            SwingUtilities.invokeLater(() -> {
                txModel.setColumnIdentifiers(heads);
                txModel.setRowCount(0);
                for (Map<String, Object> m : rows) {
                    Object[] row = new Object[keys.length];
                    for (int k = 0; k < keys.length; k++) row[k] = m.get(keys[k]);
                    txModel.addRow(row);
                }
                if (rows.isEmpty()) info("Khong co du lieu nao phu hop.");
            });
        });
    }

    // ================= LICH SU NAP THE (the cao) =================
    private DefaultTableModel ntModel;
    private JTable ntTable;
    private JTextField ntFilter;
    private JComboBox<String> ntLimit;

    private JPanel buildNapThePage() {
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setBackground(CARD);
        body.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        ntFilter = tf(18);
        ntLimit = new JComboBox<>(new String[]{"100", "500", "1000", "5000"});
        JButton bLoad = btn("Tai lich su the");
        bLoad.setBackground(new Color(70, 120, 220)); bLoad.setForeground(Color.WHITE);
        top.add(new JLabel("Loc (tai khoan / ten ingame / so the):")); top.add(ntFilter);
        top.add(new JLabel("  So dong:")); top.add(ntLimit); top.add(bLoad);
        body.add(top, BorderLayout.NORTH);
        ntModel = new DefaultTableModel(new String[]{"Tai khoan", "Ten ingame", "So tien nap", "Thoi gian nap", "Nha mang", "Status", "Serial", "Code"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        ntTable = new JTable(ntModel);
        body.add(new JScrollPane(ntTable), BorderLayout.CENTER);
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bot.setBackground(CARD);
        JLabel hint = new JLabel("Luong nap the da ngung o web (code giu dang comment) - lich su o day de doi soat va cong tay khi can.");
        hint.setForeground(new Color(90, 100, 130));
        bot.add(hint);
        bLoad.addActionListener(e -> loadNapThe());
        body.add(bot, BorderLayout.SOUTH);
        bg(this::loadNapThe);
        return wrapPage(body, "Lich Su Nap The",
                "Tai khoan - Ten ingame - So tien nap - Thoi gian nap (loc theo tai khoan / ten nhan vat / so the)");
    }

    private void loadNapThe() {
        final String f = ntFilter == null ? "" : ntFilter.getText();
        int lim = 100;
        try { lim = Integer.parseInt((String) ntLimit.getSelectedItem()); } catch (Exception e) { }
        final int limit = lim;
        bg(() -> {
            final List<Map<String, Object>> rows = PanelService.listNapThe(f, limit);
            SwingUtilities.invokeLater(() -> {
                ntModel.setRowCount(0);
                for (Map<String, Object> m : rows) {
                    ntModel.addRow(new Object[]{m.get("user"), m.get("ingame"), m.get("amount"), m.get("time"),
                        m.get("telco"), m.get("status"), m.get("serial"), m.get("code")});
                }
                if (rows.isEmpty()) info("Khong co du lieu nap the nao phu hop.");
            });
        });
    }

    // ================= TY LE NAP (x2 / x3 / ... / x50) =================
    private JTextField nrRate, nrUntil, nrNote;
    private JCheckBox nrEnabled;
    private JLabel nrNow;

    private JPanel buildNapRatePage() {
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setBackground(CARD);
        body.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBackground(CARD);
        form.setBorder(BorderFactory.createTitledBorder("Cau hinh ty le nap (tran toi da x" + PanelService.NAP_RATE_MAX + ")"));
        nrRate = tf(8);
        nrUntil = tf(16);
        nrNote = tf(24);
        nrEnabled = new JCheckBox("Bat ty le nay");
        nrEnabled.setBackground(CARD);
        form.add(new JLabel("Ty le nap (1 - " + PanelService.NAP_RATE_MAX + ", vi du 2 = x2):")); form.add(nrRate);
        form.add(new JLabel("Trang thai:")); form.add(nrEnabled);
        form.add(new JLabel("Ket thuc luc (yyyy-MM-dd HH:mm, trong = khong han):")); form.add(nrUntil);
        form.add(new JLabel("Ghi chu (dip le...):")); form.add(nrNote);

        JButton bSave = btn("Luu ty le");
        bSave.setBackground(new Color(70, 120, 220)); bSave.setForeground(Color.WHITE);
        bSave.addActionListener(e -> saveNapRate());
        JButton bReload = btn("Tai lai");
        bReload.addActionListener(e -> loadNapRate());
        JPanel act = new JPanel(new FlowLayout(FlowLayout.LEFT));
        act.setBackground(CARD); act.add(bSave); act.add(bReload);

        nrNow = new JLabel(" ");
        nrNow.setForeground(new Color(60, 70, 110));
        nrNow.setBorder(new EmptyBorder(6, 2, 6, 2));
        JTextArea guide = new JTextArea(
                "Cach dung:\n"
                + "- Ty le nay ap dung cho NAP CHUYEN KHOAN tren web nap (nrokura.site) va cho nap the khi bat lai.\n"
                + "- Vi du: ty le = 2 -> nap 20.000d duoc 40.000d (x2); ty le = 10 -> x10. Tran toi da x" + PanelService.NAP_RATE_MAX + ".\n"
                + "- Dat 'Ket thuc luc' de het le ty le tu tat (bo trong = khong gioi han thoi gian).\n"
                + "- Web nap doc truc tiep bang panel_nap_rate trong DB game -> ap dung ngay, khong can restart server.");
        guide.setEditable(false); guide.setLineWrap(true); guide.setWrapStyleWord(true);
        guide.setBackground(new Color(246, 248, 252));
        guide.setBorder(new EmptyBorder(8, 8, 8, 8));
        JPanel mid = new JPanel(new BorderLayout(8, 8));
        mid.setBackground(CARD);
        mid.add(new JScrollPane(guide), BorderLayout.CENTER);
        mid.add(nrNow, BorderLayout.SOUTH);

        JPanel right = new JPanel(new BorderLayout(8, 8));
        right.setBackground(CARD);
        right.add(act, BorderLayout.NORTH);
        right.add(mid, BorderLayout.CENTER);

        body.add(form, BorderLayout.NORTH);
        body.add(right, BorderLayout.CENTER);
        bg(this::loadNapRate);
        return wrapPage(body, "Ty Le Nap (Su Kien)",
                "Dat x2 / x3 / x10 cho dip le - tran x" + PanelService.NAP_RATE_MAX + " - co the gioi han thoi gian, het han tu tat");
    }

    private void loadNapRate() {
        bg(() -> {
            final Map<String, Object> m = PanelService.getNapRate();
            final int rate = ((Number) m.get("rate")).intValue();
            final int en = ((Number) m.get("enabled")).intValue();
            SwingUtilities.invokeLater(() -> {
                nrRate.setText(String.valueOf(rate));
                nrEnabled.setSelected(en == 1);
                nrUntil.setText(m.get("until_at") == null ? "" : String.valueOf(m.get("until_at")));
                nrNote.setText(m.get("note") == null ? "" : String.valueOf(m.get("note")));
                nrNow.setText("Hien tai: x" + rate + (en == 1 ? " (DANG BAT)" : " (TAT)")
                        + (m.get("until_at") == null ? "" : " | ket thuc: " + m.get("until_at"))
                        + (m.get("note") == null || String.valueOf(m.get("note")).isEmpty() ? "" : " | ghi chu: " + m.get("note"))
                        + " | cap nhat: " + m.get("updated_by") + " luc " + m.get("updated_at"));
            });
        });
    }

    private void saveNapRate() {
        int rate;
        try { rate = Integer.parseInt(nrRate.getText().trim()); } catch (Exception e) { err("Ty le phai la so tu 1 den " + PanelService.NAP_RATE_MAX); return; }
        if (rate < 1 || rate > PanelService.NAP_RATE_MAX) { err("Ty le chi trong khoang 1 - " + PanelService.NAP_RATE_MAX + " (tran x" + PanelService.NAP_RATE_MAX + ")"); return; }
        final int r = rate;
        final boolean en = nrEnabled.isSelected();
        final String until = nrUntil.getText().trim();
        final String note = nrNote.getText().trim();
        bg(() -> {
            final String msg = PanelService.setNapRate(r, en, until, note, PanelService.AUDIT_ACTOR);
            SwingUtilities.invokeLater(() -> { info(msg); loadNapRate(); });
        });
    }

    private JPanel buildPlayerMng() {
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setBackground(CARD);
        body.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(CARD);
        pmSearch = tf(20);
        JButton bFind = btn("Tim kiem");
        JButton bReload = btn("Tai lai");
        top.add(new JLabel("Ten player / tai khoan:")); top.add(pmSearch); top.add(bFind); top.add(bReload);
        bFind.addActionListener(e -> loadPlayerMng());
        bReload.addActionListener(e -> loadPlayerMng());
        body.add(top, BorderLayout.NORTH);
        pmModel = new DefaultTableModel(new String[]{"Ten NV", "Tai khoan", "Hanh tinh", "Power", "Map", "Trang thai"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        pmTable = new JTable(pmModel);
        body.add(new JScrollPane(pmTable), BorderLayout.CENTER);
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bot.setBackground(CARD);
        JButton bEdit = btn("Mo form sua (chi so / do / skill)");
        JButton bGive = btn("Tang item");
        JButton bKick = btn("Kick neu online");
        JButton bDel = btn("Xoa nhan vat");
        bEdit.addActionListener(e -> { String n = selPlayerName(); if (n != null) showPlayerEditDialog(n); });
        bGive.addActionListener(e -> { String n = selPlayerName(); if (n != null) showGiveItemDialog(n, -1, null); });
        bKick.addActionListener(e -> {
            String n = selPlayerName(); if (n == null) return;
            bg(() -> { String r = PanelService.kickPlayer(n); SwingUtilities.invokeLater(() -> info(r)); });
        });
        bDel.addActionListener(e -> {
            String n = selPlayerName(); if (n == null) return;
            int c = JOptionPane.showConfirmDialog(this, "Xoa TRON nhan vat " + n + "? Khong the hoan tac!", "Xac nhan", JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION) return;
            bg(() -> { String r = PanelService.deletePlayerRow(n); SwingUtilities.invokeLater(() -> { info(r); loadPlayerMng(); }); });
        });
        bot.add(bEdit); bot.add(bGive); bot.add(bKick); bot.add(bDel);
        body.add(bot, BorderLayout.SOUTH);
        bg(this::loadPlayerMng);
        return wrapPage(body, "Quan Ly Nhan Vat", "Sua chi so, tien, tui do, skill cho player ONLINE va OFFLINE - khong can SQL/JSON");
    }

    private void loadPlayerMng() {
        List<Map<String, Object>> list = PanelService.searchPlayers(pmSearch == null ? "" : pmSearch.getText(), 500);
        SwingUtilities.invokeLater(() -> {
            pmModel.setRowCount(0);
            for (Map<String, Object> m : list) {
                pmModel.addRow(new Object[]{m.get("name"), m.get("username"), m.get("gender"), m.get("power"),
                    String.valueOf(m.get("map")) + " " + m.get("map_name"),
                    Boolean.TRUE.equals(m.get("online")) ? "ONLINE" : "Offline"});
            }
        });
    }

    private String selPlayerName() {
        int r = pmTable.getSelectedRow();
        if (r < 0) { err("Chon 1 dong trong bang"); return null; }
        return String.valueOf(pmModel.getValueAt(r, 0));
    }

    private void showPlayerEditDialog(final String name) {
        final JDialog d = new JDialog(this, "Sua nhan vat: " + name, false);
        d.setSize(960, 620);
        d.setLocationRelativeTo(this);
        JTabbedPane tabs = new JTabbedPane();

        // --- Tab 1: chi so & tien ---
        JPanel p1 = new JPanel(new GridLayout(0, 2, 8, 8));
        p1.setBorder(new EmptyBorder(14, 14, 14, 14));
        final JTextField fPower = tf("", 14); final JTextField fTn = tf("", 14);
        final JTextField fGold = tf("", 14); final JTextField fGem = tf("", 14);
        final JTextField fRuby = tf("", 14); final JTextField fCoupon = tf("", 14);
        p1.add(new JLabel("Power (suc manh):")); p1.add(fPower);
        p1.add(new JLabel("Tiem nang:")); p1.add(fTn);
        p1.add(new JLabel("Vang (gold):")); p1.add(fGold);
        p1.add(new JLabel("Ngoc xanh (gem):")); p1.add(fGem);
        p1.add(new JLabel("Hong ngoc (ruby):")); p1.add(fRuby);
        p1.add(new JLabel("Diem (coupon):")); p1.add(fCoupon);
        final JButton bSave1 = btn("Luu chi so / tien");
        p1.add(new JLabel("")); p1.add(bSave1);
        tabs.addTab("Chi so & Tien", p1);

        // --- Tab 2: tui do & do mac ---
        JPanel p2 = new JPanel(new BorderLayout(8, 8));
        p2.setBorder(new EmptyBorder(8, 8, 8, 8));
        final DefaultTableModel bodyM = new DefaultTableModel(new String[]{"Slot", "ID", "Ten do", "SL", "Options"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        final DefaultTableModel bagM = new DefaultTableModel(new String[]{"Slot", "ID", "Ten do", "SL", "Options"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        final JTable bodyT = new JTable(bodyM);
        final JTable bagT = new JTable(bagM);
        JScrollPane spBody = new JScrollPane(bodyT);
        JScrollPane spBag = new JScrollPane(bagT);
        spBody.setBorder(BorderFactory.createTitledBorder("Dang mac (items_body)"));
        spBag.setBorder(BorderFactory.createTitledBorder("Tui do (items_bag)"));
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spBody, spBag);
        split.setResizeWeight(0.5);
        p2.add(split, BorderLayout.CENTER);
        JPanel p2b = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JButton bGive2 = btn("Tang item vao tui");
        final JButton bDelBag = btn("Xoa chon khoi TUI");
        final JButton bDelBody = btn("Xoa chon khoi NGOAI");
        final JButton bRefresh = btn("Tai lai");
        p2b.add(bGive2); p2b.add(bDelBag); p2b.add(bDelBody); p2b.add(bRefresh);
        p2.add(p2b, BorderLayout.SOUTH);
        tabs.addTab("Tui do & Do mac", p2);

        // --- Tab 3: skill ---
        JPanel p3 = new JPanel(new BorderLayout(8, 8));
        p3.setBorder(new EmptyBorder(8, 8, 8, 8));
        final DefaultTableModel skM = new DefaultTableModel(new String[]{"ID", "Ten skill", "Cap", "Cap max"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 2; }
        };
        final JTable skT = new JTable(skM);
        p3.add(new JScrollPane(skT), BorderLayout.CENTER);
        JPanel p3b = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JButton bSaveSkill = btn("Luu cap skill da sua");
        p3b.add(bSaveSkill);
        p3b.add(new JLabel("- nhap cap moi vao cot 'Cap' roi bam Luu (tu dong gioi han cap toi da)"));
        p3.add(p3b, BorderLayout.SOUTH);
        tabs.addTab("Skill", p3);

        d.add(tabs);

        final Runnable refresh = () -> bg(() -> {
            final Map<String, Object> v = PanelService.viewPlayer(name);
            SwingUtilities.invokeLater(() -> {
                if (v == null) { err("Khong tim thay player (co the da bi xoa)"); d.dispose(); return; }
                fPower.setText(String.valueOf(v.get("power")));
                fTn.setText(String.valueOf(v.get("tiemNang")));
                fGold.setText(String.valueOf(v.get("gold")));
                fGem.setText(String.valueOf(v.get("gem")));
                fRuby.setText(String.valueOf(v.get("ruby")));
                fCoupon.setText(String.valueOf(v.get("coupon")));
                bodyM.setRowCount(0);
                bagM.setRowCount(0);
                skM.setRowCount(0);
                try {
                    for (Map<String, Object> im : (List<Map<String, Object>>) v.get("body")) {
                        bodyM.addRow(new Object[]{im.get("slot"), im.get("id"), im.get("name"), im.get("qty"), im.get("opts")});
                    }
                    for (Map<String, Object> im : (List<Map<String, Object>>) v.get("bag")) {
                        bagM.addRow(new Object[]{im.get("slot"), im.get("id"), im.get("name"), im.get("qty"), im.get("opts")});
                    }
                    for (Map<String, Object> sm : (List<Map<String, Object>>) v.get("skills")) {
                        skM.addRow(new Object[]{sm.get("id"), sm.get("name"), sm.get("point"), sm.get("max")});
                    }
                } catch (Exception ex) { }
            });
        });

        bSave1.addActionListener(e -> {
            final String pw = fPower.getText().trim(), tn = fTn.getText().trim(), gd = fGold.getText().trim(),
                    gm = fGem.getText().trim(), rb = fRuby.getText().trim(), cp = fCoupon.getText().trim();
            bg(() -> {
                String r;
                try {
                    r = PanelService.saveBasic(name, Double.parseDouble(pw), Double.parseDouble(tn),
                            Long.parseLong(gd), Integer.parseInt(gm), Integer.parseInt(rb), Integer.parseInt(cp));
                } catch (Exception ex) { r = "Loi nhap so: " + ex.getMessage(); }
                final String rr = r;
                SwingUtilities.invokeLater(() -> { info(rr); refresh.run(); });
            });
        });
        bGive2.addActionListener(e -> showGiveItemDialog(name, -1, refresh));
        bRefresh.addActionListener(e -> refresh.run());
        bDelBag.addActionListener(e -> {
            int r = bagT.getSelectedRow();
            if (r < 0) { err("Chon 1 dong trong TUI"); return; }
            final int slot = ((Number) bagM.getValueAt(r, 0)).intValue();
            bg(() -> { String r2 = PanelService.removePlayerItem(name, "bag", slot); SwingUtilities.invokeLater(() -> { info(r2); refresh.run(); }); });
        });
        bDelBody.addActionListener(e -> {
            int r = bodyT.getSelectedRow();
            if (r < 0) { err("Chon 1 dong trong DO MAC"); return; }
            final int slot = ((Number) bodyM.getValueAt(r, 0)).intValue();
            bg(() -> { String r2 = PanelService.removePlayerItem(name, "body", slot); SwingUtilities.invokeLater(() -> { info(r2); refresh.run(); }); });
        });
        bSaveSkill.addActionListener(e -> bg(() -> {
            StringBuilder sb = new StringBuilder();
            int okCount = 0;
            for (int r = 0; r < skM.getRowCount(); r++) {
                int id, cap;
                try {
                    id = Integer.parseInt(String.valueOf(skM.getValueAt(r, 0)).trim());
                    cap = Integer.parseInt(String.valueOf(skM.getValueAt(r, 2)).trim());
                } catch (Exception ex) { continue; }
                String rr = PanelService.setPlayerSkillPoint(name, id, cap);
                sb.append(rr).append("\n");
                if (rr.startsWith("OK")) okCount++;
            }
            final String msg = okCount + " skill da luu:\n" + sb;
            SwingUtilities.invokeLater(() -> { info(msg); refresh.run(); });
        }));

        d.setVisible(true);
        refresh.run();
    }

    // --- Dialog tang item + option builder (dung chung cho Library va Quan Ly Nhan Vat) ---
    private void showGiveItemDialog(final String playerName, final int tempId) {
        showGiveItemDialog(playerName, tempId, null);
    }

    private void showGiveItemDialog(final String playerName, final int tempId, final Runnable afterSave) {
        final JDialog d = new JDialog(this, "Tang vat pham", false);
        d.setSize(780, 580);
        d.setLocationRelativeTo(this);
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JTextField fName = tf(playerName == null ? "" : playerName, 14);
        top.add(new JLabel("Ten player:")); top.add(fName);
        top.add(new JLabel("Tim do:"));
        final JTextField fKey = tf(16);
        final JButton bSearch = btn("Tim");
        top.add(fKey); top.add(bSearch);
        root.add(top, BorderLayout.NORTH);

        final DefaultTableModel resM = new DefaultTableModel(new String[]{"ID", "Ten do", "Loai"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        final JTable resT = new JTable(resM);
        JScrollPane resScroll = new JScrollPane(resT);
        resScroll.setPreferredSize(new Dimension(740, 170));

        final DefaultTableModel optM = new DefaultTableModel(new String[]{"ID Option", "Ten Option", "Param"}, 0);
        final JTable optT = new JTable(optM);
        JPanel optPanel = new JPanel(new BorderLayout(4, 4));
        optPanel.setBorder(BorderFactory.createTitledBorder("Options cua vat pham (khong can thi de trong)"));
        optPanel.add(new JScrollPane(optT), BorderLayout.CENTER);
        JPanel optBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JComboBox<String> cbOpt = new JComboBox<>();
        final JTextField fParam = tf("0", 8);
        final JTextField fOptQ = tf(10);
        JButton bAddOpt = btn("Them option");
        JButton bDelOpt = btn("Xoa option chon");
        optBtns.add(new JLabel("Option:")); optBtns.add(cbOpt);
        optBtns.add(new JLabel("Loc:")); optBtns.add(fOptQ);
        optBtns.add(new JLabel("Param:")); optBtns.add(fParam);
        optBtns.add(bAddOpt); optBtns.add(bDelOpt);
        optPanel.add(optBtns, BorderLayout.SOUTH);

        JPanel center = new JPanel(new BorderLayout(6, 6));
        center.add(resScroll, BorderLayout.NORTH);
        center.add(optPanel, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        JPanel bot = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bot.add(new JLabel("So luong:"));
        final JTextField fQty = tf("1", 6);
        bot.add(fQty);
        final JLabel lbChoosen = new JLabel(tempId >= 0 ? ("Da chon do temp " + tempId) : "Chua chon do - tim roi click 2 lan vao dong");
        bot.add(lbChoosen);
        final JButton bOk = btn("TANG CHO PLAYER");
        final JButton bCancel = btn("Huy");
        bot.add(bOk); bot.add(bCancel);
        root.add(bot, BorderLayout.SOUTH);

        d.setContentPane(root);

        final int[] chosen = {tempId};
        bSearch.addActionListener(e -> {
            final String key = fKey.getText().trim();
            bg(() -> {
                final List<Map<String, Object>> list = PanelService.listItemTemplatesFull(key, -1, -1, 5000);
                SwingUtilities.invokeLater(() -> {
                    resM.setRowCount(0);
                    for (Map<String, Object> m : list) {
                        resM.addRow(new Object[]{m.get("id"), m.get("name"), m.get("type_vn")});
                    }
                    if (list.isEmpty()) info("Khong tim thay do nao voi tu khoa: " + key);
                });
            });
        });
        resT.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() >= 2 && resT.getSelectedRow() >= 0) {
                    chosen[0] = ((Number) resM.getValueAt(resT.getSelectedRow(), 0)).intValue();
                    lbChoosen.setText("Da chon do temp " + chosen[0] + " - " + resM.getValueAt(resT.getSelectedRow(), 1));
                }
            }
        });
        final java.util.List<Map<String, Object>> optsAll = new java.util.ArrayList<>();
        final Runnable fillOpt = () -> {
            String q = fOptQ.getText().trim().toLowerCase();
            String qNo = q;
            try { qNo = boss.BossManager.convertString(q); } catch (Exception ex) {}
            cbOpt.removeAllItems();
            for (Map<String, Object> m : optsAll) {
                String lbl = m.get("id") + " - " + m.get("name");
                if (!q.isEmpty()) {
                    boolean match = lbl.toLowerCase().contains(q) || String.valueOf(m.get("id")).contains(q);
                    if (!match) { try { match = boss.BossManager.convertString(lbl.toLowerCase()).contains(qNo); } catch (Exception ex) {} }
                    if (!match) continue;
                }
                cbOpt.addItem(lbl);
            }
        };
        fOptQ.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) { fillOpt.run(); }
        });
        bg(() -> {
            final List<Map<String, Object>> opts = PanelService.listOptionDict();
            SwingUtilities.invokeLater(() -> { optsAll.addAll(opts); fillOpt.run(); });
        });
        bAddOpt.addActionListener(e -> {
            String sel = (String) cbOpt.getSelectedItem();
            if (sel == null) { err("Chon 1 option"); return; }
            try {
                int oid = Integer.parseInt(sel.split(" - ")[0].trim());
                long param = Long.parseLong(fParam.getText().trim());
                String er = optParamErrById(oid, param);
                if (er != null) { err(er); return; }
                optM.addRow(new Object[]{oid, sel.substring(sel.indexOf(" - ") + 3), param});
            } catch (Exception ex) { err("Param phai la so: " + ex.getMessage()); }
        });
        bDelOpt.addActionListener(e -> {
            int r = optT.getSelectedRow();
            if (r >= 0) optM.removeRow(r);
        });
        bOk.addActionListener(e -> {
            final String plName = fName.getText().trim();
            if (plName.isEmpty()) { err("Nhap ten player"); return; }
            if (chosen[0] < 0) { err("Chon do can tang (tim roi click 2 lan)"); return; }
            int qty;
            try { qty = Integer.parseInt(fQty.getText().trim()); } catch (Exception ex) { err("So luong phai la so"); return; }
            StringBuilder ob = new StringBuilder();
            for (int r = 0; r < optM.getRowCount(); r++) {
                try {
                    if (ob.length() > 0) ob.append(",");
                    ob.append(Integer.parseInt(String.valueOf(optM.getValueAt(r, 0)).trim()))
                      .append(":").append(Long.parseLong(String.valueOf(optM.getValueAt(r, 2)).trim()));
                } catch (Exception ex) { }
            }
            final String optStr = ob.toString();
            final int fId = chosen[0];
            bg(() -> {
                final String rr = PanelService.addPlayerItem(plName, fId, qty, optStr);
                SwingUtilities.invokeLater(() -> { info(rr); if (rr.startsWith("OK") && afterSave != null) afterSave.run(); d.dispose(); });
            });
        });
        bCancel.addActionListener(e -> d.dispose());
        d.setVisible(true);
    }

    // ================= NHAT KY ADMIN =================
    private DefaultTableModel auditModel;
    private JTable auditTable;
    private JTextField auditSearch;
    private JTextField auditActor;
    private DefaultTableModel loginModel;

    private JPanel buildAudit() {
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setBackground(CARD);
        body.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JTabbedPane tabs = new JTabbedPane();

        JPanel p1 = new JPanel(new BorderLayout(8, 8));
        p1.setBackground(CARD);
        JPanel top1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top1.setBackground(CARD);
        auditActor = tf(PanelService.AUDIT_ACTOR, 12);
        auditSearch = tf(16);
        JButton bSetActor = btn("Dat ten admin");
        JButton bFind = btn("Tim");
        JButton bReload = btn("Tai lai");
        top1.add(new JLabel("Dang thao tac (admin):")); top1.add(auditActor); top1.add(bSetActor);
        top1.add(new JLabel("Tim:")); top1.add(auditSearch); top1.add(bFind); top1.add(bReload);
        p1.add(top1, BorderLayout.NORTH);
        auditModel = new DefaultTableModel(new String[]{"ID", "Thoi gian", "Admin", "Thao tac", "Chi tiet"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        auditTable = new JTable(auditModel);
        p1.add(new JScrollPane(auditTable), BorderLayout.CENTER);
        tabs.addTab("Nhat ky thao tac admin", p1);
        bSetActor.addActionListener(e -> { PanelService.AUDIT_ACTOR = auditActor.getText().trim(); info("Da dat ten admin ghi nhat ky: " + PanelService.AUDIT_ACTOR); });
        bFind.addActionListener(e -> loadAudit());
        bReload.addActionListener(e -> loadAudit());

        JPanel p2 = new JPanel(new BorderLayout(8, 8));
        p2.setBackground(CARD);
        JPanel top2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top2.setBackground(CARD);
        JButton bReload2 = btn("Tai lai");
        top2.add(new JLabel("Lich su dang nhap gan nhat cua toan bo tai khoan")); top2.add(bReload2);
        p2.add(top2, BorderLayout.NORTH);
        loginModel = new DefaultTableModel(new String[]{"Tai khoan", "Dang nhap cuoi", "Dang xuat", "IP", "Server"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable loginT = new JTable(loginModel);
        p2.add(new JScrollPane(loginT), BorderLayout.CENTER);
        tabs.addTab("Lich su dang nhap (IP)", p2);
        bReload2.addActionListener(e -> loadLogin());

        body.add(tabs, BorderLayout.CENTER);
        bg(this::loadAudit);
        bg(this::loadLogin);
        return wrapPage(body, "Nhat Ky Admin", "Ghi lai moi thao tac buff/tang do/ban/tang VND... + lich su dang nhap IP - khong can SQL");
    }

    private void loadAudit() {
        final String key = auditSearch == null ? "" : auditSearch.getText();
        List<Map<String, Object>> list = PanelService.listAudit(key, 1000);
        SwingUtilities.invokeLater(() -> {
            auditModel.setRowCount(0);
            for (Map<String, Object> m : list) {
                auditModel.addRow(new Object[]{m.get("id"), m.get("time"), m.get("actor"), m.get("action"), m.get("detail")});
            }
        });
    }

    private void loadLogin() {
        List<Map<String, Object>> list = PanelService.listLoginHistory(500);
        SwingUtilities.invokeLater(() -> {
            loginModel.setRowCount(0);
            for (Map<String, Object> m : list) {
                loginModel.addRow(new Object[]{m.get("username"), m.get("login"), m.get("logout"), m.get("ip"), m.get("server")});
            }
        });
    }

    // ================= GUI QUA (MAIL) =================
    private JPanel buildMailPage() {
        JPanel body = new JPanel(new BorderLayout(8, 8));
        body.setBackground(CARD);
        body.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 234, 242)), new EmptyBorder(12, 14, 12, 14)));
        JLabel desc = new JLabel("<html><h2>Gui qua / Thu cho player</h2>"
                + "<p>Chon nhan vat, them vat pham + option bang bang chon, gui ngay cho player <b>online</b>"
                + " (thong bao thu moi) hoac <b>offline</b> (luu truc tiep vao hop thu trong DB).</p>"
                + "<p>Nhap ten thu, tieu de, noi dung roi bam Gui Thu.</p></html>");
        desc.setBorder(new EmptyBorder(20, 10, 10, 10));
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(CARD);
        JButton bOpen = btn("Mo cua so Gui Thu");
        bOpen.addActionListener(e -> Mail.sendMail.showForm());
        p.add(bOpen);
        body.add(desc, BorderLayout.CENTER);
        body.add(p, BorderLayout.SOUTH);
        return wrapPage(body, "Gui Qua (Mail)", "Gui vat pham + option qua thu cho 1 player online/offline - khong can SQL/JSON");
    }

    public static void showPanel() {
        SwingUtilities.invokeLater(() -> {
            try {
                // neu panel van con dang mo thi chi bring to front, khong mo ban trung
                if (instance != null && instance.isDisplayable()) {
                    if (instance.getExtendedState() == JFrame.ICONIFIED) {
                        instance.setExtendedState(JFrame.NORMAL);
                    }
                    instance.toFront();
                    instance.requestFocus();
                    return;
                }
                instance = new ControlPanel();
                instance.setVisible(true);
            } catch (Throwable e) { e.printStackTrace(); }
        });
    }
}
