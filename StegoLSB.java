import java.awt.image.BufferedImage;

public class StegoLSB {

    public static BufferedImage encodeText(BufferedImage image, String message) {
        byte[] msgBytes = message.getBytes();
        int msgLen = msgBytes.length;

        // Convert message length and message into 1 array
        byte[] lenBytes = new byte[] {
            (byte)(msgLen >> 24),
            (byte)(msgLen >> 16),
            (byte)(msgLen >> 8),
            (byte)(msgLen)
        };

        byte[] fullData = new byte[lenBytes.length + msgBytes.length];
        System.arraycopy(lenBytes, 0, fullData, 0, 4);
        System.arraycopy(msgBytes, 0, fullData, 4, msgBytes.length);

        int width = image.getWidth();
        int height = image.getHeight();
        int dataIndex = 0, bitIndex = 0;

        outer:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (dataIndex >= fullData.length) break outer;

                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;

                // Modify each channel's LSB to store bits
                for (int i = 0; i < 3; i++) {
                    if (dataIndex >= fullData.length) break;

                    int bit = (fullData[dataIndex] >> (7 - bitIndex)) & 1;

                    if (i == 0) r = (r & 0xFE) | bit;
                    else if (i == 1) g = (g & 0xFE) | bit;
                    else b = (b & 0xFE) | bit;

                    bitIndex++;
                    if (bitIndex == 8) {
                        bitIndex = 0;
                        dataIndex++;
                    }
                }

                int newRGB = (0xFF << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(x, y, newRGB);
            }
        }

        return image;
    }

    public static String decodeText(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        byte[] data = new byte[width * height * 3 / 8]; // max possible
        int dataIndex = 0, bitIndex = 0;
        int currentByte = 0;

        int totalBitsToRead = -1; // Unknown until we read the first 32 bits
        int bitsRead = 0;

        outer:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int[] channels = {
                    (rgb >> 16) & 0xff, // R
                    (rgb >> 8) & 0xff,  // G
                    rgb & 0xff          // B
                };

                for (int i = 0; i < 3; i++) {
                    int bit = channels[i] & 1;
                    currentByte = (currentByte << 1) | bit;
                    bitIndex++;
                    bitsRead++;

                    if (bitIndex == 8) {
                        data[dataIndex++] = (byte) currentByte;
                        currentByte = 0;
                        bitIndex = 0;

                        if (dataIndex == 4 && totalBitsToRead == -1) {
                            int len = ((data[0] & 0xFF) << 24)
                                    | ((data[1] & 0xFF) << 16)
                                    | ((data[2] & 0xFF) << 8)
                                    | (data[3] & 0xFF);
                            totalBitsToRead = (len + 4) * 8;
                        }

                        if (totalBitsToRead != -1 && bitsRead >= totalBitsToRead)
                            break outer;
                    }
                }
            }
        }

        int msgLen = ((data[0] & 0xFF) << 24)
                   | ((data[1] & 0xFF) << 16)
                   | ((data[2] & 0xFF) << 8)
                   | (data[3] & 0xFF);

        byte[] msgBytes = new byte[msgLen];
        System.arraycopy(data, 4, msgBytes, 0, msgLen);
        return new String(msgBytes);
    }
}
