/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import com.shtrih.fiscalprinter.command.PrinterDate;
import java.util.Scanner;
import com.shtrih.util.MathUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
/**
 *
 * @author V.Kravtsov
 */
public class CashCoreVersion {

    private final int majorVersion;
    private final int minorVersion;
    private final int build;

    public CashCoreVersion(String version) {
        Scanner scanner = new Scanner(version);
        scanner.useDelimiter("\\.");
        majorVersion = scanner.nextInt();
        minorVersion = scanner.nextInt();
        build = scanner.nextInt();
    }

    public int compare(CashCoreVersion v) {
        int rc = MathUtils.compare(majorVersion, v.majorVersion);
        if (rc == 0) {
            rc = MathUtils.compare(minorVersion, v.minorVersion);
            if (rc == 0) {
                rc = MathUtils.compare(build, v.build);
            }
        }
        return rc;
    }
}
