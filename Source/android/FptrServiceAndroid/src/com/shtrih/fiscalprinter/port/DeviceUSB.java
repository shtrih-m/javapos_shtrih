package com.shtrih.fiscalprinter.port;

import android.content.Context;
import android.hardware.usb.UsbManager;

import com.shtrih.hoho.android.usbserial.driver.UsbSerialDriver;
import com.shtrih.hoho.android.usbserial.driver.UsbSerialPort;
import com.shtrih.hoho.android.usbserial.driver.UsbSerialProber;
import com.shtrih.util.CircularByteBuffer;
import com.shtrih.util.CompositeLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DeviceUSB {

    private class UsbSerialDriverWithName {
        private UsbSerialDriver usbSerialDriver;
        private String name;

        public UsbSerialDriverWithName(UsbSerialDriver usbSerialDriver, String name) {
            this.usbSerialDriver = usbSerialDriver;
            this.name = name;
        }

        public UsbSerialDriver getUsbSerialDriver() {
            return usbSerialDriver;
        }

        public String getName() {
            return name;
        }
    }

    //private static final String ACTION_USB_PERMISSION = "com.shtrih.tinyjavapostester.USB_PERMISSION";

    private static final CompositeLogger L = CompositeLogger.getLogger(DeviceUSB.class);
    private UsbManager usbManager;
    //private PendingIntent permissionIntent;
    private UsbSerialDriver currentDevice = null;

    private int dataBits = UsbSerialPort.DATABITS_8;
    private int parity = UsbSerialPort.PARITY_NONE;
    private int stopBits = UsbSerialPort.STOPBITS_1;
    //private int flowControl = UsbSerialPort.FLOWCONTROL_NONE;

//    private byte[] bufferRead;
//    private int bufferReadOffset;
//    private int bufferReadLength;

    //private final int МИНИМАЛЬНОЕ_ВРЕМЯ_ОЖИДАНИЯ_ОТВЕТА = 50;

    public DeviceUSB(Context context) {

        ClearBuffers();

        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

//        permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
//        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
//        context.registerReceiver(usbReceiver, filter);
    }

//    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
//
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (ACTION_USB_PERMISSION.equals(action)) {
//                synchronized (this) {
//                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
//                        if (device != null) {
//                        }
//                    } else {
//                        L.error("permission denied for device " + device);
//                    }
//                }
//            }
//        }
//    };

//    public String GetSettings() {
//        int i;
//        //
//        String StrName = "";
//        List<UsbSerialDriver> allDriver = GetAllDevice();
//        List<UsbSerialDriverWithName> allDriverWithName = GetAllDeviceName(allDriver);
//        boolean isFindDevice = false;
//        for (i = 0; i < allDriverWithName.size(); i++) {
//            if (allDriverWithName.get(i).getName() == usbDeviceName) {
//                StrName += "\t<ListItem Value=\"" + allDriverWithName.get(i).getName() + "\" Selected=\"true\"/>\n";
//                isFindDevice = true;
//            } else
//                StrName += "\t<ListItem Value=\"" + allDriverWithName.get(i).getName() + "\"/>\n";
//        }
//        if (!isFindDevice) {
//            usbDeviceName = "";
//            StrName += "\t<ListItem Value=\"\" Selected=\"true\"/>\n";
//        }
//        //
//        String StrBaudRate = "";
//        for (i = 0; i < UsbSerialPort.BAUDRATE.length; i++) {
//            if (UsbSerialPort.BAUDRATE[i] == baudRate)
//                StrBaudRate += "\t<ListItem Value=\"" + UsbSerialPort.BAUDRATE[i] + "\" Selected=\"true\"/>\n";
//            else
//                StrBaudRate += "\t<ListItem Value=\"" + UsbSerialPort.BAUDRATE[i] + "\"/>\n";
//        }
//        //
//        String StrDataBits = "";
//        for (i = 0; i < UsbSerialPort.DATABITS.length; i++) {
//            if (UsbSerialPort.DATABITS[i] == dataBits)
//                StrDataBits += "\t<ListItem Value=\"" + UsbSerialPort.DATABITS[i] + "\" Selected=\"true\"/>\n";
//            else
//                StrDataBits += "\t<ListItem Value=\"" + UsbSerialPort.DATABITS[i] + "\"/>\n";
//        }
//        //
//        String StrParity = "";
//        for (i = 0; i < UsbSerialPort.PARITY.length; i++) {
//            if (UsbSerialPort.PARITY[i] == parity)
//                StrParity += "\t<ListItem Value=\"" + UsbSerialPort.PARITY[i] + "\" Selected=\"true\"/>\n";
//            else
//                StrParity += "\t<ListItem Value=\"" + UsbSerialPort.PARITY[i] + "\"/>\n";
//        }
//        //
//        String StrStopBits = "";
//        for (i = 0; i < UsbSerialPort.STOPBITS.length; i++) {
//            if (UsbSerialPort.STOPBITS[i] == stopBits)
//                StrStopBits += "\t<ListItem Value=\"" + UsbSerialPort.STOPBITS[i] + "\" Selected=\"true\"/>\n";
//            else
//                StrStopBits += "\t<ListItem Value=\"" + UsbSerialPort.STOPBITS[i] + "\"/>\n";
//        }
//        //
//        String StrFlowControl = "";
//        for (i = 0; i < UsbSerialPort.FLOWCONTROL.length; i++) {
//            if (UsbSerialPort.FLOWCONTROL[i] == flowControl)
//                StrFlowControl += "\t<ListItem Value=\"" + UsbSerialPort.FLOWCONTROL[i] + "\" Selected=\"true\"/>\n";
//            else
//                StrFlowControl += "\t<ListItem Value=\"" + UsbSerialPort.FLOWCONTROL[i] + "\"/>\n";
//        }
//        //
//        String Ret = String.format("\t<List Id=\"Name\" Text=\"Имя устройства\" >\n" +
//                        "%s" +
//                        "</List>" +
//                        "\t<List Id=\"BaudRate\" Text=\"Скорость\" >\n" +
//                        "%s" +
//                        "</List>" +
//                        "\t<List Id=\"DataBits\" Text=\"Биты данных\" >\n" +
//                        "%s" +
//                        "</List>" +
//                        "\t<List Id=\"Parity\" Text=\"Четность\" >\n" +
//                        "%s" +
//                        "</List>" +
//                        "\t<List Id=\"StopBits\" Text=\"Стоповые биты\" >\n" +
//                        "%s" +
//                        "</List>" +
//                        "\t<List Id=\"FlowControl\" Text=\"Управление потоком\" >\n" +
//                        "%s" +
//                        "</List>",
//                StrName,
//                StrBaudRate,
//                StrDataBits,
//                StrParity,
//                StrStopBits,
//                StrFlowControl);
//        return Ret;
//    }
//
//    public boolean SetSettings(String Set) {
//        return super.SetSettings(Set);
//    }

//    public boolean SetSettings(Map<String, String> values) {
//        //String Dop=values.get("Device.BaudRate");
//        usbDeviceName = values.get("Device.Name");
//        try {
//            baudRate = Integer.valueOf(values.get("Device.BaudRate"));
//        } catch (NumberFormatException e) {
//            baudRate = 115200;
//        }
//        //
//        try {
//            dataBits = Integer.valueOf(values.get("Device.DataBits"));
//        } catch (NumberFormatException e) {
//            dataBits = UsbSerialPort.DATABITS_8;
//        }
//        //
//        try {
//            parity = Integer.valueOf(values.get("Device.Parity"));
//        } catch (NumberFormatException e) {
//            parity = UsbSerialPort.PARITY_NONE;
//        }
//        //
//        try {
//            stopBits = Integer.valueOf(values.get("Device.StopBits"));
//        } catch (NumberFormatException e) {
//            stopBits = UsbSerialPort.STOPBITS_1;
//        }
//        //
//        try {
//            flowControl = Integer.valueOf(values.get("Device.FlowControl"));
//        } catch (NumberFormatException e) {
//            flowControl = UsbSerialPort.FLOWCONTROL_NONE;
//        }
//        return true;
//    }

//    public Map<String, AppSettings.ChoiceList.ListFiller> getPropertyFillers() {
//        Map<String, AppSettings.ChoiceList.ListFiller> f = new HashMap<>();
//        f.put("Device.Name", new AppSettings.ChoiceList.ListFiller() {
//            @Override
//            protected void executeFilling(List<AppSettings.ChoiceList.ChoiceListItem> list) {
//                List<UsbSerialDriver> allDriver = GetAllDevice();
//                List<UsbSerialDriverWithName> allDriverWithName = GetAllDeviceName(allDriver);
//                boolean isFindDevice = false;
//                for (int i = 0; i < allDriverWithName.size(); i++) {
//                    list.add(new AppSettings.ChoiceList.ChoiceListItem(allDriverWithName.get(i).getName(), allDriverWithName.get(i).getName() == usbDeviceName));
//                    isFindDevice = (allDriverWithName.get(i).getName() == usbDeviceName);
//                }
//                if (!isFindDevice) {
//                    list.add(new AppSettings.ChoiceList.ChoiceListItem("", true));
//                    usbDeviceName = "";
//                }
//            }
//        });
//        return f;
//    }

    public boolean connect(String usbDeviceName, int baudRate) {
        disconnect();
        currentDevice = GetDeviceForName(usbDeviceName, baudRate);
        if (currentDevice == null) {
            L.error("Устройство с именем \"" + usbDeviceName + "\" не найдено");
            return false;
        }
        try {
            currentDevice.getPorts().get(0).open(usbManager.openDevice(currentDevice.getDevice()));
        } catch (IOException e) {
            L.error("Ошибка подключения к устройству(" + e.getMessage() + ")");
            return false;
        }
        try {
            currentDevice.getPorts().get(0).setParameters(baudRate, dataBits, stopBits, parity);
        } catch (IOException e) {
            L.error("Ошибка инициализации порта");
            return false;
        }

        return true;
    }

    public boolean disconnect() {
        if (currentDevice == null)
            return false;

        try {
            currentDevice.getPorts().get(0).close();
            currentDevice = null;
        } catch (IOException e) {
            L.error("Ошибка инициализации порта");
        }

        return true;
    }

    public boolean Write(byte[] buffer, int offset, int count) {
        if (currentDevice == null) {
            L.error("Устройство не подключено");
            return false;
        }
        byte[] buff = new byte[count];
        System.arraycopy(buffer, offset, buff, 0, count);
        try {
            currentDevice.getPorts().get(0).write(buff, 1000);
        } catch (IOException e) {
            L.error("Ошибка записи на устройство(" + e.getMessage() + ")");
            return false;
        }
        return true;
    }

    public boolean Write(byte[] buffer) {
        return Write(buffer, 0, buffer.length);
    }

    private CircularByteBuffer readBffer = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);

    public boolean Read(byte[] buffer, int offset, int count, int timeout) throws Exception {
        if (currentDevice == null) {
            L.error("Устройство не подключено");
            return false;
        }

        byte[] buff = new byte[1024];
        while (readBffer.getAvailable() < count) {
//            if (currentDevice instanceof FtdiSerialDriver) {
//                int CountWaitTic = timeout / МИНИМАЛЬНОЕ_ВРЕМЯ_ОЖИДАНИЯ_ОТВЕТА;
//                for (int i = 0; i < CountWaitTic; i++) {
//                    try {
//                        countNow = currentDevice.getPorts().get(0).read(buff, МИНИМАЛЬНОЕ_ВРЕМЯ_ОЖИДАНИЯ_ОТВЕТА);
//                    } catch (IOException e) {
//                        L.error("Ошибка чтения с устройства(" + e.getMessage() + ")");
//                    }
//                    if (countNow > 0)
//                        break;
//                    Thread.sleep(МИНИМАЛЬНОЕ_ВРЕМЯ_ОЖИДАНИЯ_ОТВЕТА);
//                }
//            } else {
            try {

                int countNow = currentDevice.getPorts().get(0).read(buff, timeout);

                if (countNow <= 0) {
                    L.error("Ошибка чтения с устройства");
                    return false;
                } else {
                    readBffer.getOutputStream().write(buff, 0, countNow);
                }

            } catch (IOException e) {
                L.error("Ошибка чтения с устройства(" + e.getMessage() + ")");
                return false;
            }
        }

        int readCount = readBffer.getInputStream().read(buffer, offset, count);

        if (readCount != count) {
            L.error("readCount != count");
            return false;
        }

        //}
        /*while(countReceive<count) {
            try {
				countNow=currentDevice.getPorts().get(0).read(buff,timeout);
			} catch (IOException e) {
				super.SetError(ОШИБКА_ЧТЕНИЯ_С_УСТРОЙСВА,"Ошибка чтения с устройства("+e.getMessage()+")");
			}
			if(countNow==0) {
				super.SetError(ОШИБКА_ЧТЕНИЯ_С_УСТРОЙСВА,"Ошибка чтения с устройства");
				return false;
			}
			System.arraycopy(buff,0,buffer,offset+countReceive,countNow);
			countReceive+=countNow;
		}*/
        /*try {
            countNow=currentDevice.getPorts().get(0).read(buff,timeout);
		} catch (IOException e) {
			super.SetError(ОШИБКА_ЧТЕНИЯ_С_УСТРОЙСВА,"Ошибка чтения с устройства("+e.getMessage()+")");
		}
		System.arraycopy(buff,0,buffer,offset,count);*/
        return true;
    }

    private List<UsbSerialDriver> GetAllDevice() {
        return UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);
    }

    private List<UsbSerialDriverWithName> GetAllDeviceName(List<UsbSerialDriver> usbSerialDrivers, int baudRate) {
        disconnect();
        List<UsbSerialDriverWithName> ret = new ArrayList<>();
        for (int i = 0; i < usbSerialDrivers.size(); i++) {
            currentDevice = usbSerialDrivers.get(i);
            try {
                currentDevice.getPorts().get(0).open(usbManager.openDevice(currentDevice.getDevice()));
            } catch (IOException e) {
                L.error("Неправильное USB-устройство(" + e.getMessage() + ")");
                continue;
            } catch (NullPointerException e) {
                L.error("USB-устройство недоступно(" + e.getMessage() + ")");
                continue;
            }
            try {
                currentDevice.getPorts().get(0).setParameters(baudRate, dataBits, stopBits, parity);
            } catch (IOException e) {
                L.error("Ошибка установки параметров(" + e.getMessage() + ")");
                continue;
            }
            String deviceName = String.format(Locale.ENGLISH, "%d", currentDevice.getDevice().getDeviceId());
            try {
                currentDevice.getPorts().get(0).close();
            } catch (IOException e) {
                L.error("Ошибка установки параметров(" + e.getMessage() + ")");
            }

            UsbSerialDriverWithName curDriver = new UsbSerialDriverWithName(usbSerialDrivers.get(i), deviceName);
            ret.add(curDriver);
        }
        currentDevice = null;
        return ret;
    }

    private UsbSerialDriver GetDeviceForName(String deviceName, int baudRate) {
        List<UsbSerialDriver> allDriver = GetAllDevice();
        List<UsbSerialDriverWithName> allDriverWithName = GetAllDeviceName(allDriver, baudRate);
        // UsbSerialDriver ret = null;
        for (int i = 0; i < allDriverWithName.size(); i++) {
            if (allDriverWithName.get(i).getName().equals(deviceName))
                return allDriverWithName.get(i).getUsbSerialDriver();
        }

        return null;
    }

    public void ClearBuffers() {
        readBffer.clear();
//        bufferRead = new byte[260];
//        bufferReadOffset = 0;
//        bufferReadLength = 0;
    }

//    private boolean CheckReadBuffer(byte[] buffer, int offset, int count) {
//        if (count <= (bufferReadLength - bufferReadOffset)) {
//            System.arraycopy(bufferRead, bufferReadOffset, buffer, offset, count);
//            bufferReadOffset += count;
//            return false;
//        }
//        return true;
//    }
//
//    private boolean AddToReadBuffer(byte[] buffer, int count) {
//        if (bufferReadOffset != 0) {
//            if (bufferReadLength > bufferReadOffset) {
//                byte[] dopBuffer = new byte[260];
//                System.arraycopy(bufferRead, bufferReadOffset, dopBuffer, 0, bufferReadLength - bufferReadOffset);
//                bufferReadLength = bufferReadLength - bufferReadOffset;
//                bufferReadOffset = 0;
//                System.arraycopy(dopBuffer, 0, bufferRead, bufferReadOffset, bufferReadLength);
//            } else {
//                bufferReadOffset = 0;
//                bufferReadLength = 0;
//            }
//        }
//        if (count != 0) {
//            System.arraycopy(buffer, 0, bufferRead, bufferReadOffset, count);
//            bufferReadLength += count;
//        }
//        return true;
//    }
}
