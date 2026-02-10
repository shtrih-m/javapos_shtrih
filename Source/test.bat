@echo off

set classpath=jpos113.jar
set classpath=%classpath%;jpos113-controls.jar
set classpath=%classpath%;shtrihjavapos.jar
set classpath=%classpath%;AbsoluteLayout.jar
set classpath=%classpath%;FiscalPrinterTest.jar
set classpath=%classpath%;log4j-1.2.12.jar
set classpath=%classpath%;xerces.jar
set classpath=%classpath%;nrjavaserial-3.12.0.jar
set classpath=%classpath%;zxing-2.2.jar
set classpath=%classpath%;%cd%\

set lp=c:\windows\system32
set lp=%lp%;%cd%\


java.exe -cp %classpath% -Djava.library.path=%lp% MainDialog