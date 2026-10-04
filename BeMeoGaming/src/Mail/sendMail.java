package Mail;

import server.Client;
import services.ItemService;
import services.Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import item.Item;
import item.Item.ItemOption;
import jdbc.DBConnecter;
import player.Player;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import jdbc.daos.NDVSqlFetcher;
import jdbc.daos.PlayerDAO;

public class sendMail extends JFrame {

    private JTextField txtRecipient, txtTitle, txtTitle2, txtContent, txtQuantity, txtParam;
    private JComboBox<String> comboItems, comboOptions;
    private JTable tableItems, tableOptions;
    private DefaultTableModel itemTableModel, optionTableModel;
    private List<Item> itemList = new ArrayList<>();
    private List<ItemOption> currentOptions = new ArrayList<>();

    public sendMail() {
        setTitle("Gửi Thư");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Tên người nhận:"));
        txtRecipient = new JTextField();
        inputPanel.add(txtRecipient);

        inputPanel.add(new JLabel("Tên thư:"));
        txtTitle = new JTextField();
        inputPanel.add(txtTitle);

        inputPanel.add(new JLabel("Tiêu đề thư:"));
        txtTitle2 = new JTextField();
        inputPanel.add(txtTitle2);

        inputPanel.add(new JLabel("Nội dung:"));
        txtContent = new JTextField();
        inputPanel.add(txtContent);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Option and Item Panel
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        // Options Table
        optionTableModel = new DefaultTableModel(new String[]{"ID Option", "Tên Option", "Param"}, 0);
        tableOptions = new JTable(optionTableModel);
        JScrollPane optionScrollPane = new JScrollPane(tableOptions);

        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.add(new JLabel("Danh sách Options"), BorderLayout.NORTH);
        optionPanel.add(optionScrollPane, BorderLayout.CENTER);

        JPanel optionButtonPanel = new JPanel(new FlowLayout());
        comboOptions = new JComboBox<>(getOptionNames());
        txtParam = new JTextField(5);
        JButton btnAddOption = new JButton("Thêm Option");
        JButton btnRemoveOption = new JButton("Xóa Option");
        optionButtonPanel.add(new JLabel("Chọn Option:"));
        optionButtonPanel.add(comboOptions);
        optionButtonPanel.add(new JLabel("Param:"));
        optionButtonPanel.add(txtParam);
        optionButtonPanel.add(btnAddOption);
        optionButtonPanel.add(btnRemoveOption);

        optionPanel.add(optionButtonPanel, BorderLayout.SOUTH);
        centerPanel.add(optionPanel);

        // Items Table
        itemTableModel = new DefaultTableModel(new String[]{"ID Vật phẩm", "Tên Vật phẩm", "Số lượng", "Options"}, 0);
        tableItems = new JTable(itemTableModel);
        JScrollPane itemScrollPane = new JScrollPane(tableItems);

        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.add(new JLabel("Danh sách Vật phẩm"), BorderLayout.NORTH);
        itemPanel.add(itemScrollPane, BorderLayout.CENTER);

        JPanel itemButtonPanel = new JPanel(new FlowLayout());
        comboItems = new JComboBox<>(getItemNames());
        txtQuantity = new JTextField(5);
        JButton btnAddItem = new JButton("Thêm Vật phẩm");
        JButton btnRemoveItem = new JButton("Xóa Vật phẩm");
        itemButtonPanel.add(new JLabel("Chọn Vật phẩm:"));
        itemButtonPanel.add(comboItems);
        itemButtonPanel.add(new JLabel("Số lượng:"));
        itemButtonPanel.add(txtQuantity);
        itemButtonPanel.add(btnAddItem);
        itemButtonPanel.add(btnRemoveItem);

        itemPanel.add(itemButtonPanel, BorderLayout.SOUTH);
        centerPanel.add(itemPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Send Button
        JButton btnSend = new JButton("Gửi Thư");
        mainPanel.add(btnSend, BorderLayout.SOUTH);

        // Action Listeners
        btnAddOption.addActionListener(e -> addOption());
        btnRemoveOption.addActionListener(e -> removeOption());
        btnAddItem.addActionListener(e -> addItem());
        btnRemoveItem.addActionListener(e -> removeItem());
        btnSend.addActionListener(e -> sendMail());
    }

    private String[] getItemNames() {
        return getItemList().toArray(new String[0]);
    }

    private String[] getOptionNames() {
        return getOptionList().toArray(new String[0]);
    }

    public static List<String> getItemList() {
        List<String> itemList = new ArrayList<>();
        try (Connection con = DBConnecter.getConnectionServer(); PreparedStatement ps = con.prepareStatement("SELECT id, name FROM item_template"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                itemList.add(id + " - " + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }

    public static List<String> getOptionList() {
        List<String> optionList = new ArrayList<>();
        try (Connection con = DBConnecter.getConnectionServer(); PreparedStatement ps = con.prepareStatement("SELECT id, name FROM item_option_template"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                optionList.add(id + " - " + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return optionList;
    }

    private void addOption() {
        try {
            String selectedOption = (String) comboOptions.getSelectedItem();
            int optionId = Integer.parseInt(selectedOption.split(" - ")[0]);
            int param = Integer.parseInt(txtParam.getText().trim());

            currentOptions.add(new ItemOption(optionId, param));
            optionTableModel.addRow(new Object[]{optionId, selectedOption.split(" - ")[1], param});

            // Adjust table size dynamically
            tableOptions.setPreferredScrollableViewportSize(new Dimension(
                    tableOptions.getPreferredScrollableViewportSize().width,
                    tableOptions.getRowHeight() * (optionTableModel.getRowCount() + 1)
            ));
            tableOptions.revalidate();
            tableOptions.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm option: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addItem() {
        try {
            String selectedItem = (String) comboItems.getSelectedItem();
            int itemId = Integer.parseInt(selectedItem.split(" - ")[0]);
            int quantity = Integer.parseInt(txtQuantity.getText().trim());

            // Tạo vật phẩm mới và thêm vào danh sách
            Item newItem = ItemService.gI().createNewItem((short) itemId);
            newItem.quantity = quantity;
            newItem.itemOptions.addAll(currentOptions);

            itemList.add(newItem);

            // Lấy thông tin các options của vật phẩm
            StringBuilder optionsInfo = new StringBuilder();
            for (ItemOption option : currentOptions) {
                optionsInfo.append("[").append(option.getOptionString()).append("] ");
            }

            // Thêm vật phẩm vào bảng
            itemTableModel.addRow(new Object[]{
                itemId,
                selectedItem.split(" - ")[1],
                quantity,
                optionsInfo.toString().trim()
            });

            // Xóa danh sách option tạm thời sau khi thêm vật phẩm
            currentOptions.clear();
            optionTableModel.setRowCount(0); // Xóa toàn bộ option trong bảng Options

            // Điều chỉnh bảng hiển thị
            tableItems.setPreferredScrollableViewportSize(new Dimension(
                    tableItems.getPreferredScrollableViewportSize().width,
                    tableItems.getRowHeight() * (itemTableModel.getRowCount() + 1)
            ));
            tableItems.revalidate();
            tableItems.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm vật phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeOption() {
        int selectedRow = tableOptions.getSelectedRow();
        if (selectedRow != -1) {
            currentOptions.remove(selectedRow);
            optionTableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một option để xóa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void removeItem() {
        int selectedRow = tableItems.getSelectedRow();
        if (selectedRow != -1) {
            itemList.remove(selectedRow);
            itemTableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một vật phẩm để xóa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void sendMail() {
        try {
            String recipient = txtRecipient.getText().trim();
            String title = txtTitle.getText().trim();
            String title2 = txtTitle2.getText().trim();
            String content = txtContent.getText().trim();

            if (recipient.isEmpty() || title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Player rais = Client.gI().getPlayer(recipient);
            if (rais == null) {
                Player plOff = NDVSqlFetcher.loadPlayerByName(recipient);
                if(plOff == null){
                    JOptionPane.showMessageDialog(this, "Không tìm thấy người chơi: " + recipient, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Thu thu = new Thu(title, title2, "Được gửi bởi hệ thống", content, System.currentTimeMillis(), false, false);
                thu.listItem.addAll(itemList);
                plOff.homThu.add(thu);
                
                PlayerDAO.updatePlayer(plOff);
                JOptionPane.showMessageDialog(this, "Thư đã được gửi thành công! Người chơi đang Offline", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            Thu thu = new Thu(title, title2, "Được gửi bởi hệ thống", content, System.currentTimeMillis(), false, false);
            thu.listItem.addAll(itemList);

            rais.homThu.add(thu);
            HomThuService.gI().sendListMail(rais);
            Service.gI().sendThongBaoFromAdmin(rais, "Bạn nhận được thư mới");

            JOptionPane.showMessageDialog(this, "Thư đã được gửi thành công! Người chơi đang online", "Thành công", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi gửi thư: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void showForm() {
        SwingUtilities.invokeLater(() -> {
            sendMail form = new sendMail();
            form.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            sendMail form = new sendMail();
            form.setVisible(true);
        });
    }
}
