package com.todo.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.todo.dao.TodoAppDAO;
import com.todo.model.Todo;

public class TodoAppGUI extends JFrame {
    private TodoAppDAO todoDAO;
    private JTable todoTable;
    private DefaultTableModel tableModel;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JCheckBox completedCheckBox;
    private JButton addButton, updateButton, deleteButton, refreshButton;
    private JComboBox<String> filterComboBox;

    public TodoAppGUI() {
        this.todoDAO = new TodoAppDAO();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadTodos();
    }

    private void initializeComponents() {
        setTitle("Todo Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Table setup
        String[] columnNames = {"ID", "Title", "Description", "Completed", "Created At", "Updated At"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) return String.class;
                return super.getColumnClass(columnIndex);
            }
        };
        todoTable = new JTable(tableModel);
        todoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        todoTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedTodo();
        });

        // Input fields
        titleField = new JTextField(20);
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        completedCheckBox = new JCheckBox("Completed");

        // Buttons
        addButton = new JButton("Add Todo");
        updateButton = new JButton("Update Todo");
        deleteButton = new JButton("Delete Todo");
        refreshButton = new JButton("Refresh Todos");

        // Filter
        String[] filterOptions = {"All", "Completed", "Pending"};
        filterComboBox = new JComboBox<>(filterOptions);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(new JLabel("Title"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Description"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(new JScrollPane(descriptionArea), gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Completed"), gbc);
        gbc.gridx = 1; 
        inputPanel.add(completedCheckBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(filterComboBox);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(filterPanel, BorderLayout.NORTH);
        northPanel.add(inputPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);
        add(new JScrollPane(todoTable), BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void setupEventListeners() {
        addButton.addActionListener(e -> addTodo());
        updateButton.addActionListener(e -> updateTodo());
        deleteButton.addActionListener(e -> deleteTodo());
        refreshButton.addActionListener(e -> loadTodos());
        filterComboBox.addActionListener(e -> filterTodos());
    }

    private void addTodo() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        boolean completed = completedCheckBox.isSelected();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a title", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Todo todo = new Todo(title, description);
            todo.setCompleted(completed);
            todoDAO.createtodo(todo);
            JOptionPane.showMessageDialog(this, "Todo added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadTodos();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding todo: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTodo() {
        int row = todoTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to update", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title cannot be empty", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        String description = descriptionArea.getText().trim();
        boolean completed = completedCheckBox.isSelected();

        try {
            Todo todo = todoDAO.getTodoById(id);
            if (todo != null) {
                todo.setTitle(title);
                todo.setDescription(description);
                todo.setCompleted(completed);

                if (todoDAO.updateTodo(todo)) {
                    JOptionPane.showMessageDialog(this, "Todo updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadTodos();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update todo", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTodo() {
        int row = todoTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this todo?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            if (todoDAO.deleteTodo(id)) {
                JOptionPane.showMessageDialog(this, "Todo deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearInputFields();
                loadTodos();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete todo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterTodos() {
        String selected = (String) filterComboBox.getSelectedItem();
        try {
            List<Todo> todos;
            if (selected.equals("All")) todos = todoDAO.getAllTodos();
            else if (selected.equals("Completed")) todos = todoDAO.filterTodos(true);
            else todos = todoDAO.filterTodos(false);

            updateTable(todos);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error filtering todos: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTodos() {
        try {
            List<Todo> todos = todoDAO.getAllTodos();
            updateTable(todos);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading todos: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedTodo() {
        int row = todoTable.getSelectedRow();
        if (row != -1) {
            String title = (String) tableModel.getValueAt(row, 1);
            String description = (String) tableModel.getValueAt(row, 2);
            String completedStr = (String) tableModel.getValueAt(row, 3);
            boolean completed = completedStr.equals("Yes");

            titleField.setText(title);
            descriptionArea.setText(description);
            completedCheckBox.setSelected(completed);
        }
    }

    private void refreshTodos() {
        loadTodos();
        filterComboBox.setSelectedIndex(0);
        clearInputFields();
    }

    private void updateTable(List<Todo> todos) {
        tableModel.setRowCount(0);
        for (Todo t : todos) {
            Object[] rowData = {
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.isCompleted() ? "Yes" : "No",
                t.getCreated_at(),
                t.getUpdated_at()
            };
            tableModel.addRow(rowData);
        }
    }

    private void clearInputFields() {
        titleField.setText("");
        descriptionArea.setText("");
        completedCheckBox.setSelected(false);
    }
}
