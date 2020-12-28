package cn.adolf.adolf.file;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.stream.IntStream;

/**
 * @program: Adolf
 * @description:
 * @author: yjq
 * @create: 2020-12-28 15:12
 **/
public class FileUtils {

    public static boolean copyByChannel(String srcFile, String outFile) {
        return copyByChannel(new File(srcFile), new File(outFile));
    }

    public static boolean copyByChannel(File srcFile, File outFile) {
        if (!srcFile.exists()) return false;
        File tempFile = new File(outFile.getAbsolutePath() + ".tmp");
        tempFile.mkdirs();
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        boolean isCopySuccess = false;

        try {
            if (tempFile.exists()) {
                tempFile.delete();
            }

            inStream = new FileInputStream(srcFile);
            outStream = new FileOutputStream(tempFile);
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);

            if (tempFile.exists()) {
                isCopySuccess = tempFile.length() == srcFile.length();
                tempFile.renameTo(outFile);
            }
        } catch (IOException e) {
            isCopySuccess = false;
            e.printStackTrace();
        } finally {
            close(inStream);
            close(in);
            close(outStream);
            close(out);
        }

        return isCopySuccess;
    }


    public static boolean copyByBytes(String srcFile, String outFile) {
        return copyByBytes(new File(srcFile), new File(outFile));
    }

    public static boolean copyByBytes(File srcFile, File outFile) {
        if (!srcFile.exists()) return false;
        File tempFile = new File(outFile.getAbsolutePath() + ".tmp");
        tempFile.mkdirs();

        InputStream in = null;
        OutputStream out = null;

        boolean isCopySuccess = false;

        try {
            if (tempFile.exists()) {
                tempFile.delete();
            }
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(tempFile);

            byte[] buf = new byte[4096];
            int i;
            while ((i = in.read(buf)) != -1) {
                out.write(buf, 0, i);
            }

            if (tempFile.exists()) {
                isCopySuccess = tempFile.length() == srcFile.length();
                tempFile.renameTo(outFile);
            }
        } catch (IOException e) {
            isCopySuccess = false;
            e.printStackTrace();
        } finally {
            close(in);
            close(out);
        }

        return isCopySuccess;
    }

    public static boolean copyAsset(Context context, String srcAssetName, String outAbPath) {
        String tmpFile = outAbPath + ".tmp";
        InputStream in = null;
        OutputStream out = null;
        boolean isCopySuccess;
        try {
            byte[] buffer = new byte[1024];
            File fi = new File(tmpFile);
            in = context.getAssets().open(srcAssetName, AssetManager.ACCESS_STREAMING);

            boolean isAppend = false;
            if (fi.exists()) { // 续传，老机器，T35性能差，速度慢，会重复复制
                Log.e("CopyAssets", "file exists!!!" + fi.length());
                in.skip(fi.length());
                isAppend = true;
            }
            out = new FileOutputStream(fi, isAppend);

            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            isCopySuccess = true;

        } catch (Exception e) {
            e.printStackTrace();
            isCopySuccess = false;
        } finally {
            close(in);
            close(out);
        }
        return isCopySuccess;
    }


    public static void close(Closeable closer) {
        if (closer != null) {
            try {
                closer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            closer = null;
        }
    }

}
