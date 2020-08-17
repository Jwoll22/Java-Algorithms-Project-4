import java.util.Random;
import java.math.BigInteger;
import java.util.Arrays;

public class Add128 implements SymCipher {
    private byte[] key;

    public Add128() {
        BigInteger bigKey = new BigInteger(1024, 80, new Random());
        key = bigKey.toByteArray();
    }

    public Add128(byte[] key) {
        this.key = key;
    }

    public byte[] getKey() {
        return key;
    }

    public byte[] encode(String s) {
        int i = 0;
        byte[] inp = s.getBytes();
        byte[] out = new byte[inp.length];

        System.out.println("Original string message: " + s);
        System.out.print("Original byte array message:");
        for (int j = 0; j < inp.length; j++)
            System.out.print(" " + inp[j]);
        System.out.println();

        while (i < inp.length) {
            out[i] = (byte)(inp[i] + key[i%key.length]);
            i++;
        }
        System.out.print("Encrypted byte array message:");
        for (int j = 0; j < out.length; j++)
            System.out.print(" " + out[j]);
        System.out.println();
        return out;
    }

    public String decode(byte[] inp) {
        int i = 0;
        byte[] out = new byte[inp.length];
        System.out.print("Original byte array message:");
        for (int j = 0; j < inp.length; j++)
            System.out.print(" " + inp[j]);
        System.out.println();

        while (i < inp.length) {
            out[i] = (byte)(inp[i] - key[i%key.length]);
            i++;
        }
        System.out.print("Decrypted byte array message:");
        for (int j = 0; j < out.length; j++)
            System.out.print(" " + out[j]);
        System.out.println();
        System.out.println("Decrypted string message: " + new String(out));
        return new String(out);
    }
}
