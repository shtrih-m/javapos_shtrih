
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author V.Kravtsov
 */
public class EscCommand {

    private ByteArrayOutputStream stream = new ByteArrayOutputStream();

    public EscCommand() {
    }

    public byte[] getBytes() {
        return stream.toByteArray();
    }

    // Select Printing Position for HRI Characters
    // 0 =Not printed
    // 1 = Above the bar code
    // 2 = Belowthe bar code
    // 3 = Both above and below the bar code
    public void selectHRIPosition(int value) throws IOException {
        stream.write(0x1D);
        stream.write(0x48);
        stream.write((byte)value);
    }

    // Select Pitch for HRI Characters
    // 0 = Standard Pitch at 15.2 CPI on receipt
    // 1 = Compressed Pitch at 19 CPI on receipt
    public void selectHRIPitch(int value) throws IOException {
        stream.write(0x1D);
        stream.write(0x66);
        stream.write((byte)value);
    }

    // Select Bar Code Width
    public void selectBarcodeWidth(int value) throws IOException {
        stream.write(0x1D);
        stream.write(0x77);
        stream.write((byte)value);
    }

    // Select Bar Code Height
    public void selectBarcodeHeight(int value) throws IOException {
        stream.write(0x1D);
        stream.write(0x68);
        stream.write((byte)value);
    }

    // Print Barcode
    public void printBarcode(byte type, String barcode) throws IOException {
        stream.write(0x1D);
        stream.write(0x6B);
        stream.write(type);
        stream.write((byte) barcode.length());
        stream.write(barcode.getBytes());
    }

    public void EOL() {
        stream.write(0x0A);
    }
}
