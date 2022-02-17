/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kktnetdtest;

import gnu.io.LibManager;

/**
 *
 * @author V.Kravtsov
 */
public class Kktnetdtest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try{
            LibManager.getInstance();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
