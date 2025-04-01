package com;

import com.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;

@Component
public class EmailSenderGUI implements CommandLineRunner {

    private final EmailService emailService;

    @Autowired
    public EmailSenderGUI(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void run(String... args) {
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    private void createAndShowGUI() {
        // Create the main window
        JFrame frame = new JFrame("Email Sender Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);

        // Create main panel with nice padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form panel using GridBagLayout for better control
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Email recipient field
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        formPanel.add(new JLabel("To:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 1;
        JTextField toField = new JTextField(25);
        toField.setToolTipText("For multiple recipients, separate email addresses with commas");
        formPanel.add(toField, gbc);

        // Multiple recipients checkbox
        gbc.gridx = 2; gbc.gridy = 0; gbc.gridwidth = 1;
        JCheckBox multipleRecipientsBox = new JCheckBox("Multiple");
        formPanel.add(multipleRecipientsBox, gbc);

        // Subject field
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Subject:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        JTextField subjectField = new JTextField(30);
        formPanel.add(subjectField, gbc);

        // Body field with scrolling
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Body:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        JTextArea bodyArea = new JTextArea(10, 30);
        bodyArea.setLineWrap(true);
        JScrollPane bodyScrollPane = new JScrollPane(bodyArea);
        formPanel.add(bodyScrollPane, gbc);

        // Email type selection panel
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3;
        JPanel typePanel = new JPanel();
        typePanel.setBorder(BorderFactory.createTitledBorder("Email Type"));

        ButtonGroup group = new ButtonGroup();
        JRadioButton simpleEmailRadio = new JRadioButton("Simple Email");
        JRadioButton htmlEmailRadio = new JRadioButton("HTML Email");
        JRadioButton fileAttachmentRadio = new JRadioButton("Email with Attachment");

        group.add(simpleEmailRadio);
        group.add(htmlEmailRadio);
        group.add(fileAttachmentRadio);
        simpleEmailRadio.setSelected(true);

        typePanel.add(simpleEmailRadio);
        typePanel.add(htmlEmailRadio);
        typePanel.add(fileAttachmentRadio);
        formPanel.add(typePanel, gbc);

        // File attachment components
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel fileLabel = new JLabel("Attachment:");
        fileLabel.setVisible(false);
        formPanel.add(fileLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        JTextField filePathField = new JTextField(20);
        filePathField.setEditable(false);
        filePathField.setVisible(false);
        formPanel.add(filePathField, gbc);

        gbc.gridx = 2; gbc.gridy = 4; gbc.gridwidth = 1;
        JButton browseButton = new JButton("Browse");
        browseButton.setVisible(false);
        formPanel.add(browseButton, gbc);

        // Show/hide file components based on selection
        fileAttachmentRadio.addActionListener(e -> {
            fileLabel.setVisible(true);
            filePathField.setVisible(true);
            browseButton.setVisible(true);
        });

        simpleEmailRadio.addActionListener(e -> {
            fileLabel.setVisible(false);
            filePathField.setVisible(false);
            browseButton.setVisible(false);
        });

        htmlEmailRadio.addActionListener(e -> {
            fileLabel.setVisible(false);
            filePathField.setVisible(false);
            browseButton.setVisible(false);
        });

        // File chooser setup
        final JFileChooser fileChooser = new JFileChooser();
        final File[] selectedFile = {null};

        browseButton.addActionListener(e -> {
            int returnVal = fileChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                selectedFile[0] = fileChooser.getSelectedFile();
                filePathField.setText(selectedFile[0].getAbsolutePath());
            }
        });

        // Button panel with styled buttons
        JPanel buttonPanel = new JPanel();
        JButton sendButton = new JButton("Send Email");
        sendButton.setBackground(new Color(92, 184, 92));
        sendButton.setForeground(Color.WHITE);

        JButton clearButton = new JButton("Clear");
        clearButton.setBackground(new Color(91, 192, 222));
        clearButton.setForeground(Color.WHITE);

        buttonPanel.add(sendButton);
        buttonPanel.add(clearButton);

        // Add panels to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Send button action
        sendButton.addActionListener(e -> {
            try {
                String toText = toField.getText().trim();
                String subject = subjectField.getText().trim();
                String body = bodyArea.getText().trim();

                if (toText.isEmpty() || subject.isEmpty() || body.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (simpleEmailRadio.isSelected()) {
                    if (multipleRecipientsBox.isSelected()) {
                        // Parse multiple email addresses
                        String[] recipients = toText.split(",");
                        // Trim whitespace from each address
                        for (int i = 0; i < recipients.length; i++) {
                            recipients[i] = recipients[i].trim();
                        }
                        emailService.sendEmail(recipients, subject, body);
                        JOptionPane.showMessageDialog(frame, "Email sent to " + recipients.length + " recipients!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        emailService.sendEmail(toText, subject, body);
                        JOptionPane.showMessageDialog(frame, "Email sent successfully!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if (htmlEmailRadio.isSelected()) {
                    emailService.sendEmailWithHtml(toText, subject, body, "true");
                    JOptionPane.showMessageDialog(frame, "HTML email sent successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else if (fileAttachmentRadio.isSelected()) {
                    if (selectedFile[0] == null) {
                        JOptionPane.showMessageDialog(frame, "Please select a file", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    emailService.sendEmailWithFile(toText, subject, body, selectedFile[0]);
                    JOptionPane.showMessageDialog(frame, "Email with attachment sent successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error sending email: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Clear button action
        clearButton.addActionListener(e -> {
            toField.setText("");
            subjectField.setText("");
            bodyArea.setText("");
            filePathField.setText("");
            selectedFile[0] = null;
            simpleEmailRadio.setSelected(true);
            multipleRecipientsBox.setSelected(false);
            fileLabel.setVisible(false);
            filePathField.setVisible(false);
            browseButton.setVisible(false);
        });

        // Display the window
        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }
}