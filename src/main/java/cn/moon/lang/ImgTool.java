package cn.moon.lang;

import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImgTool {


    public static String toBase64DataUri(BufferedImage image) throws IOException {
        String mimeType = "image/jpg";

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);

        byte[] data = os.toByteArray();
        os.close();

        return getDataUri(mimeType, "base64", Base64Utils.encodeToString(data));
    }


    public static String getDataUri(String mimeType, String encoding, String data) {
        final StringBuilder builder = new StringBuilder("data:");
        if (StrTool.isNotBlank(mimeType)) {
            builder.append(mimeType);
        }

        if (StrTool.isNotBlank(encoding)) {
            builder.append(';').append(encoding);
        }
        builder.append(',').append(data);

        return builder.toString();
    }


}
