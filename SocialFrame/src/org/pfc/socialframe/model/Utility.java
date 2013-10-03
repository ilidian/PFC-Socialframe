package org.pfc.socialframe.model;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.provider.MediaStore;

public class Utility {
	public static AndroidHttpClient httpclient = null;
	//Descargar imagen de la red social a través de una url
	public static Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(new FlushedInputStream(is));
            bis.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
        }
        return bm;
    }
	//Imagen descargada
	static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }
        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break; 
                    } else {
                        bytesSkipped = 1; 
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
	//Imagen escalada para subir a la red social
	public static byte[] scaleImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();
        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);
        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }
        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > Constants.MAX_DIMENSION || rotatedHeight > Constants.MAX_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) Constants.MAX_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) Constants.MAX_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }
        String type = context.getContentResolver().getType(photoUri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (type.equals("image/png")) {
            srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] bMapArray = baos.toByteArray();
        baos.close();
        return bMapArray;
    }
	//Saber la orientación del móvil
    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }
        cursor.moveToFirst();
        return cursor.getInt(0);
    }
    //redimensionar un bitmap para imagenes escaladas
    public static Bitmap resizeBitmap( Bitmap input, int destWidth, int destHeight )
	{
		int srcWidth = input.getWidth();
		int srcHeight = input.getHeight();
		boolean needsResize = false;
		float p;
		if ( srcWidth > destWidth || srcHeight > destHeight ) {
			needsResize = true;
			if ( srcWidth > srcHeight && srcWidth > destWidth ) {
				p = (float)destWidth / (float)srcWidth;
				destHeight = (int)( srcHeight * p );
			} else {
				p = (float)destHeight / (float)srcHeight;
				destWidth = (int)( srcWidth * p );
			}
		} else {
			destWidth = srcWidth;
			destHeight = srcHeight;
		}
		if ( needsResize ) {
			Bitmap output = Bitmap.createScaledBitmap( input, destWidth, destHeight, true );
			return output;
		} else {
			return input;
		}
	}

}
