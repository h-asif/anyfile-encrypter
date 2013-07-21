/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleencypter;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;
 
/*
 * FileChooserDemo.java uses these files:
 *   images/Open16.gif
 *   images/Save16.gif
 */
public class GUI extends JPanel implements ActionListener {
    static private final String newline = "\n";
    JButton selectFile, encryptButton, decryptButton;
    JTextArea log;
    JFileChooser fc;
    String selectedFile;
    Cipher encipher, decipher;
            
 
    public GUI() {
        super(new BorderLayout());
 
        //Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
 
        //Create a file chooser
        fc = new JFileChooser();
 
      
        selectFile = new JButton("Open a File...");
        selectFile.addActionListener(this);

        
        encryptButton = new JButton("encrypt a File...");
        encryptButton.addActionListener(this);
        
        decryptButton = new JButton("decrypt a File...");
        decryptButton.addActionListener(this);
        
        
        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(selectFile);
   //     buttonPanel.add(saveButton);
        buttonPanel.add( encryptButton );
        buttonPanel.add( decryptButton );
     //   buttonPanel.add( generateKey );
        
        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }
 
    public void actionPerformed(ActionEvent e) {
 
        //Handle open button action.
        if ( e.getSource() == selectFile) {
            int returnVal = fc.showOpenDialog(GUI.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                log.append("Opening: " + file.getName() + "." + newline);
                selectedFile = file.getPath();//file.getName();
                
            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
 
        //Handle save button action.
        }else if( e.getSource().equals( encryptButton ) ){

            log.append("Encrypting file: "+selectedFile+" ...." + newline);
            System.out.println(" encryption ....");
            SecretKey sKey = EncryptionUtil.getSecretKey();
            CipherInputStream cis =EncryptionUtil.encrypt( selectedFile, sKey);
            EncryptionUtil.writeFromCipherInputStream(cis, selectedFile+"_enc");

        }else if( e.getSource().equals( decryptButton ) ){
             System.out.println(" decryption ....");
            log.append("decrypting file: "+selectedFile+" ...." + newline);
            SecretKey sKey = EncryptionUtil.getSecretKey();
            //String decFile = selectedFile.substring(0,  selectedFile.indexOf("_"));
            CipherOutputStream cos = EncryptionUtil.decrypt(selectedFile+"_dec", sKey);
            EncryptionUtil.writeToCipherOutputStream(cos, selectedFile);
        }
    }
 
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = GUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Our Enrypter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Add content to the window.
        frame.add(new GUI());
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE); 
                createAndShowGUI();
            }
        });
    }
}
