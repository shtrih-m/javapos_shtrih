#!/bin/sh
CLASSPATH=$CLASSPATH:jpos113.jar
CLASSPATH=$CLASSPATH:jpos113-controls.jar
CLASSPATH=$CLASSPATH:shtrihjavapos.jar
CLASSPATH=$CLASSPATH:AbsoluteLayout.jar
CLASSPATH=$CLASSPATH:FiscalPrinterTest.jar
CLASSPATH=$CLASSPATH:log4j-1.2.12.jar
CLASSPATH=$CLASSPATH:xerces.jar
CLASSPATH=$CLASSPATH:nrjavaserial-3.12.0.jar
CLASSPATH=$CLASSPATH:jbarcode-0.2.8.jar
CLASSPATH=$CLASSPATH:zxing-2.2.jar
CLASSPATH=$CLASSPATH:%cd%\


LIB_PATH=$LD_LIBRARY_PATH
########################################
#                                      #
#  Add Device Specific jar's here...   #
#                                      #
########################################

#CLASSPATH=$CLASSPATH:/path_to_service.jar


java -cp $CLASSPATH -Djava.library.path=$LIB_PATH MainDialog
