import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ToDoListApp extends JFrame {
    private DefaultListModel<String> taskModel;
    private JList<String> taskList;
    private JTextField taskInput;

    private final String FILE_NAME = "tasks.txt";

    public ToDoListApp() {
        setTitle("📝 To-Do List");
        setSize(450, 500);  // Slightly reduced height
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        // Panel to hold everything
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Task input field
        taskInput = new JTextField();
        taskInput.setFont(new Font("SansSerif", Font.PLAIN, 14));
        taskInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // Smaller height
        taskInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                new EmptyBorder(4, 8, 4, 8)
        ));

        // Add Task button
        JButton addButton = createStyledButton("➕ Add Task");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Task List
        taskModel = new DefaultListModel<>();
        taskList = new JList<>(taskModel);
        taskList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setBackground(new Color(245, 250, 255)); // Light blue-ish background
        taskList.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setPreferredSize(new Dimension(400, 150)); // Smaller height
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Action buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setMaximumSize(new Dimension(400, 35));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton deleteButton = createStyledButton("🗑️ Delete");
        JButton doneButton = createStyledButton("✅ Done");
        JButton saveButton = createStyledButton("💾 Save");
        JButton loadButton = createStyledButton("📂 Load");

        buttonPanel.add(deleteButton);
        buttonPanel.add(doneButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        // Add components to the main panel
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(taskInput);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        mainPanel.add(addButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);

        // Functionality
        addButton.addActionListener(e -> {
            String task = taskInput.getText().trim();
            if (!task.isEmpty()) {
                taskModel.addElement(task);
                taskInput.setText("");
            }
        });

        deleteButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index != -1) taskModel.remove(index);
        });

        doneButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index != -1) {
                String task = taskModel.get(index);
                if (!task.startsWith("[Done] ")) {
                    taskModel.set(index, "[Done] " + task);
                }
            }
        });

        saveButton.addActionListener(e -> saveTasksToFile());
        loadButton.addActionListener(e -> loadTasksFromFile());

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(new Color(230, 240, 255));
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return btn;
    }

    private void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < taskModel.size(); i++) {
                writer.write(taskModel.getElementAt(i));
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "✅ Tasks saved!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "❌ Error saving tasks.");
        }
    }

    private void loadTasksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            taskModel.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                taskModel.addElement(line);
            }
            JOptionPane.showMessageDialog(this, "📂 Tasks loaded!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "❌ Error loading tasks.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoListApp::new);
    }
}
