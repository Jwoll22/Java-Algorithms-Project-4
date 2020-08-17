import java.util.Random;
import java.math.*;
import java.util.Arrays;
import java.util.ArrayList;

public class Substitute implements SymCipher {
    private byte[] key;

    public Substitute() {
        ArrayList<Integer> cache = new ArrayList<Integer>();
        Random rnd = new Random();
        key = new byte[256];
        for (int i = 0; i < 256; i++) {
            cache.add(i);
        }
        while (cache.size() > 0) {
            int ind1 = rnd.nextInt(cache.size());
            int num1 = cache.get(ind1);
            cache.remove(ind1);
            int ind2 = rnd.nextInt(cache.size());
            int num2 = cache.get(ind2);
            cache.remove(ind2);
            key[num1] = (byte)num2;
            key[num2] = (byte)num1;
        }
    }

    public Substitute(byte[] key) {
        this.key = key;
    }

    public byte[] getKey() {
        return key;
    }

    public byte[] encode(String s) {
        byte[] inp = s.getBytes();

        System.out.println("Original string message: " + s);
        System.out.print("Original byte array message:");
        for (int j = 0; j < inp.length; j++)
            System.out.print(" " + inp[j]);
        System.out.println();

        for (int i = 0; i < inp.length; i++) {
            inp[i] = key[inp[i] & 0xFF];
        }

        System.out.print("Encrypted byte array message:");
        for (int j = 0; j < inp.length; j++)
            System.out.print(" " + inp[j]);
        System.out.println();
        return inp;
    }

    public String decode(byte[] inp) {
        System.out.print("Original byte array message:");
        for (int j = 0; j < inp.length; j++)
            System.out.print(" " + inp[j]);
        System.out.println();

        for (int i = 0; i < inp.length; i++) {
            inp[i] = key[inp[i] & 0xFF];
        }

        System.out.print("Decrypted byte array message:");
        for (int j = 0; j < inp.length; j++)
            System.out.print(" " + inp[j]);
        System.out.println();
        System.out.println("Decrypted string message: " + new String(inp));
        return new String(inp);
    }
}
