/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleencypter;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;

/**
 *
 * @author salman
 */
public class EncryptionUtil {
    
 
    public static final String SECRET_KEY_FILE = "C:/keys/secret.key";
    
    public static CipherInputStream getCipherInputStreamOfFile( String fileWithPathName, Cipher cipher ){
     
        FileInputStream fis; 
        
        if( fileWithPathName == null ){
            return null;
        }
        try {
            fis = new FileInputStream( new File( fileWithPathName )  );
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
        
        if( fis == null || cipher == null ){
            return null;
        }
        CipherInputStream cis = new CipherInputStream( fis, cipher );
        
        return cis;
        
    }
    
    public static CipherOutputStream getCipherOutputStreamOfFile( String file, Cipher cipher ) {
        
        FileOutputStream fos;
        
        try {
            fos = new FileOutputStream( new File( file ) );
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
        
        if( fos == null || cipher == null ){
            return null;
        }
        
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        return cos;
    }
    
    public static void saveKey( SecretKey key ) {
    
        File secretKeyFile = new File(SECRET_KEY_FILE);
        if (secretKeyFile.getParentFile() != null) {
                secretKeyFile.getParentFile().mkdirs();
            }
        try {
            secretKeyFile.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("error creating parent directorey");
        }
        
        ObjectOutputStream secretKeyOS = null;
        try {
            secretKeyOS = new ObjectOutputStream(new FileOutputStream(secretKeyFile));
            secretKeyOS.writeObject(key);
            secretKeyOS.close();
        } catch ( IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
 
    public static SecretKey generateSecretKey(){
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecretKey skey = kgen.generateKey();
            return skey;
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        }
        
        
    }
    
    public static boolean doesSecretKeyExits(){
        File keyFile = new File( SECRET_KEY_FILE );
        if( keyFile.exists() ){
            return true;
        }else{
            return false;
        }
    }
    
    public static SecretKey readSecretKey(){
        
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream( SECRET_KEY_FILE));
            final SecretKey secretKey = (SecretKey) inputStream.readObject();
            return secretKey;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch ( IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    
    public static SecretKey getSecretKey(){
    
    
        SecretKey secrteKey = null;
        if( doesSecretKeyExits()){
            secrteKey = readSecretKey();
        }else{
            secrteKey = generateSecretKey();
            saveKey(secrteKey);
        }
        return secrteKey;
    }
    
    public static void writeFromCipherInputStream( CipherInputStream cis, String file ){
    
        int read;
        
        try {
            FileOutputStream fos = new FileOutputStream( new File( file ) );
            while((read = cis.read())!=-1)
            {
                fos.write((char)read);
                fos.flush();
            }
            //cis.close();
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static CipherInputStream encrypt( String file , SecretKey secretKey ){
     
        Cipher encipher;
        
        try {
            encipher = Cipher.getInstance("AES");
            encipher.init(Cipher.ENCRYPT_MODE, secretKey );
            CipherInputStream cis = getCipherInputStreamOfFile(file, encipher);
            return cis;
        } catch (InvalidKeyException ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
       
    }
    
    public static CipherOutputStream decrypt( String file , SecretKey secretKey ){
     
        Cipher encipher;
        
        try {
            encipher = Cipher.getInstance("AES");
            encipher.init(Cipher.DECRYPT_MODE, secretKey );
            CipherOutputStream cos = getCipherOutputStreamOfFile(file, encipher);
            return cos;
        } catch (InvalidKeyException ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
       
    }
    
    public static void writeToCipherOutputStream( CipherOutputStream cos, String file ){
        FileInputStream encfis = null;
        try {
            int read;
            encfis = new FileInputStream( new File( file) );
            while((read=encfis.read())!=-1)
            {
                cos.write(read);
                cos.flush();
            }
            cos.close();
            //encfis.close();
        } catch (IOException ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
