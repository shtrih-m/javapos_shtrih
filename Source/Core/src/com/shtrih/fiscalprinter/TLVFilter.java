/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import com.shtrih.fiscalprinter.command.PrinterConst;

/**
 *
 * @author Виталий
 */
public class TLVFilter {
    
    
    public static void filter(TLVItems src, TLVItems dst, int ffd) throws Exception {
        for (int i = 0; i < src.size(); i++) {
            TLVItem item = src.get(i);
            switch (item.getId()) {
                case 1227:
                case 1228:
                case 1243:
                case 1244:
                case 1245:
                case 1246:
                case 1254:
                    if (ffd == PrinterConst.FS_FORMAT_FFD_1_2) {
                        TLVItem customerSTLV = dst.find(1256);
                        if (customerSTLV == null) {
                            customerSTLV = dst.add(1256);
                        }
                        customerSTLV.addItem(item);

                    } else{
                        dst.add(item);
                    }
                    break;
                    
                default: dst.add(item);
            }
        }
    }
    
}
