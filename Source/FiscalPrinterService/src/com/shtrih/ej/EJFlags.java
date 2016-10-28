/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.ej;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.util.BitUtils;

public class EJFlags {
    private final int flags;

    public EJFlags(int flags) {
        this.flags = flags;
    }

    public int getFlags() {
        return flags;
    }

    public int getDocType() {
        return flags & 3;
    }

    public boolean isArchiveOpened() {
        return BitUtils.testBit(flags, 2);
    }

    public boolean isActivated() {
        return BitUtils.testBit(flags, 3);
    }

    public boolean isReportMode() {
        return BitUtils.testBit(flags, 4);
    }

    public boolean isDocOpened() {
        return BitUtils.testBit(flags, 5);
    }

    public boolean isDayOpened() {
        return BitUtils.testBit(flags, 6);
    }

    public boolean isFatalError() {
        return BitUtils.testBit(flags, 7);
    }

}
