/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author User
 */
public class NotAEncryptionFile {
    private static int key;
    
    protected static void setKey(int Ikey){key = Ikey;}
    
    public static String Encryption(String message) { return e(key,message); }
    
    public static String Decryption (String message){ return d(key,message); }

    private static String e(int key, String message) {
        char[] chars = message.toCharArray();
        String returnMessageE = "";
        
        for(char c : chars) {
            c += key;
            returnMessageE = returnMessageE + c;
        }
        
        return returnMessageE;
    }

    private static String d(int key, String message) {
        char[] chars = message.toCharArray();
        String returnMessageD = "";
        
        for(char c : chars) {
            c -= key;
            returnMessageD = returnMessageD + c;
        }
        
        return returnMessageD;
    }
}
