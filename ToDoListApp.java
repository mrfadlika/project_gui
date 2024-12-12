import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class WelcomeScreen extends JFrame {
    public WelcomeScreen() {
        setTitle("Selamat Datang");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        ImageIcon icon = new ImageIcon("path/to/your/image.png");
        JLabel logoLabel = new JLabel(icon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcomeLabel = new JLabel("Selamat Datang");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(200, 30));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Masukkan Nama Anda:");
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton enterButton = new JButton("Masuk");
        enterButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(20));
        panel.add(welcomeLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(usernameLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(enterButton);

        enterButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            if(!username.isEmpty()) {
                new ToDoListApp(username);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Nama tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });

        add(panel);
        setVisible(true);
    }
}

class Task {
    private String description;
    private boolean completed;

    public Task(String description) {
        this.description = description;
        this.completed = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return description + (completed ? " [Selesai]" : " [Belum Selesai]");
    }
}

public class ToDoListApp extends JFrame implements ActionListener {
    private JList<Task> taskList;
    private DefaultListModel<Task> listModel;
    private JTextField taskField;
    private JButton addButton, removeButton, clearButton;
    private List<Task> tasks = new ArrayList<>();
    private String username;

    public ToDoListApp(String username) {
        this.username = username;
        taskList = new JList<>();
        listModel = new DefaultListModel<>();
        taskList.setModel(listModel);
        taskField = new JTextField(20);
        addButton = new JButton("Tambah Tugas");
        removeButton = new JButton("Hapus Tugas");
        clearButton = new JButton("Hapus Semua");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Selamat Datang, " + username + "!");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel taskLabel = new JLabel("Nama Tugas: ");
        inputPanel.add(taskLabel);
        inputPanel.add(taskField);

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(clearButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        taskList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Task task = (Task) value;
                JCheckBox checkBox = new JCheckBox(task.getDescription());
                checkBox.setSelected(task.isCompleted());
                checkBox.setEnabled(!task.isCompleted());
                checkBox.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
                checkBox.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
                return checkBox;
            }
        });

        taskList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = taskList.locationToIndex(e.getPoint());
                if (index != -1) {
                    Task task = listModel.getElementAt(index);
                    if(!task.isCompleted()) {
                        task.setCompleted(true);
                        taskList.repaint();
                        JOptionPane.showMessageDialog(ToDoListApp.this, "Tugas '" + task.getDescription() + "' telah selesai!", "Tugas Selesai", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        addButton.addActionListener(this);
        removeButton.addActionListener(this);
        clearButton.addActionListener(this);

        taskField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addTask();
                }
            }
        });

        add(mainPanel);
        setTitle("To-Do List - " + username);
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addTask() {
        String newTaskDesc = taskField.getText().trim();
        if (!newTaskDesc.isEmpty()) {
            Task newTask = new Task(newTaskDesc);
            tasks.add(newTask);
            updateListModel();
            taskField.setText("");
            JOptionPane.showMessageDialog(this,
                "Tugas berhasil ditambahkan!",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Nama tugas tidak boleh kosong!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removeTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task removedTask = tasks.remove(selectedIndex);
            updateListModel();
            JOptionPane.showMessageDialog(this,
                "Tugas '" + removedTask.getDescription() + "' berhasil dihapus!",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Pilih tugas yang akan dihapus!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearTasks() {
        if (!tasks.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus semua tugas?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                tasks.clear();
                updateListModel();
                JOptionPane.showMessageDialog(this,
                    "Semua tugas berhasil dihapus!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Daftar tugas sudah kosong!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateListModel() {
        listModel.clear();
        for (Task task : tasks) {
            listModel.addElement(task);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addTask();
        } else if (e.getSource() == removeButton) {
            removeTask();
        } else if (e.getSource() == clearButton) {
            clearTasks();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WelcomeScreen();
        });
    }
}