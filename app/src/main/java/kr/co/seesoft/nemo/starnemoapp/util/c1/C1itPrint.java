//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package kr.co.seesoft.nemo.starnemoapp.util.c1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class C1itPrint {
    private Bitmap mBitmap;
    private String mBitmapPath;
    private int m_pixelCount;
    private int m_Offset;
    private int[] m_pixels;
    private int[] m_pixelInfo;
    private int[][] m_hash;
    private String m_BitmapHexString;
    private int PRINTWIDTH = 384;
    private Context m_context;
    private int BITMAPHEADERINFO_SIZE = 52;
    ImagePrintEventListenser printEventHandler = null;
    ByteData bytedata;
    int ffCount = 0;
    int mMode;  // 0 - gray, 1 - color

    private static final int BMP_WIDTH_OF_TIMES = 4;

    public C1itPrint(int mode) {
        mMode = mode;
        this.init();
    }

    public byte[] Print(Bitmap _bmp, int _alignment) {
        this.m_Offset = _alignment;
        if (_bmp == null) {
            return null;
        } else {
            this.mBitmap = _bmp;
            byte[] tmp = this.setChangeImage_proc();
            return tmp != null && tmp.length >= 1 ? tmp : null;
        }
    }

    public byte[] Print(String _path, int _alignment) {
        this.m_Offset = _alignment;
        if (_path == null) {
            return null;
        } else {
            this.mBitmapPath = _path;
            byte[] tmp = this.setChangeImage_procB(inputType.FILEPATH);
            return tmp != null && tmp.length >= 1 ? tmp : null;
        }
    }

    private void init() {
        this.m_Offset = 1;
    }

    private String setChangeImage_procS(inputType _type) {
        if (!this.ChangeBitMapTOGray(_type)) {
            return "";
        } else {
            this.ConvertDataToIntArray();
            byte[] tmp = this.ConvertIntArrayToByteArray();
            String sTmp = this.reSizeByteArray(tmp);
            return sTmp;
        }
    }

    private byte[] setChangeImage_proc() {
        byte[] tmp = null;
        if (!this.ChangeBitMapTOGray(inputType.BITMAP)) {
            return null;
        } else {
            this.bytedata = null;
            double length = this.m_pixelInfo.length;
            int size = (int)Math.ceil(length/8);
            this.bytedata = new ByteData(size + 54);
            this.addBitmapHeaderData();

            for(int i = 0; i < this.m_pixelInfo.length; i += 8) {
                try {
                    byte result = (byte) (this.m_pixelInfo[i] * 128 + this.m_pixelInfo[i + 1] * 64 + this.m_pixelInfo[i + 2] * 32 + this.m_pixelInfo[i + 3] * 16 + this.m_pixelInfo[i + 4] * 8 + this.m_pixelInfo[i + 5] * 4 + this.m_pixelInfo[i + 6] * 2 + this.m_pixelInfo[i + 7]);
                    this.bytedata.add(result);
                }catch (ArrayIndexOutOfBoundsException e){
                    byte result = (byte) 0xFF;
                    this.bytedata.add(result);
                }
            }

            String sTmp = this.ConvertbyteArrayToHex(this.bytedata.getBytes_noNullData());
            return this.bytedata.getBytes_noNullData();
        }
    }

    private void addBitmapHeaderData() {
        this.bytedata.add((byte)66);
        this.bytedata.add((byte)77);
        this.bytedata.add(intToByteArray(this.mBitmap.getWidth() * this.mBitmap.getHeight() / 8 + 54));
        this.bytedata.add((byte)0);
        this.bytedata.add((byte)0);
        this.bytedata.add((byte)0);
        this.bytedata.add((byte)0);
        this.bytedata.add(intToByteArray(54));
        this.bytedata.add(intToByteArray(40));
        this.bytedata.add(intToByteArray(this.mBitmap.getWidth()));
        this.bytedata.add(intToByteArray(this.mBitmap.getHeight()));
        this.bytedata.add((byte)1);
        this.bytedata.add((byte)0);
        this.bytedata.add((byte)1);
        this.bytedata.add((byte)0);
        this.bytedata.add(intToByteArray(0));
        this.bytedata.add(intToByteArray(64));
        this.bytedata.add(intToByteArray(0));
        this.bytedata.add(intToByteArray(0));
        this.bytedata.add(intToByteArray(0));
        this.bytedata.add(intToByteArray(0));
    }

    private byte[] setChangeImage_procB(inputType _type) {
        if (!this.ChangeBitMapTOGray(_type)) {
            return null;
        } else {
            this.ConvertDataToIntArray();
            byte[] tmp = this.ConvertIntArrayToByteArray();
            return tmp;
        }
    }

    private boolean ChangeBitMapTOGray(inputType _type) {
        if (_type == inputType.FILEPATH) {
            Options options = new Options();
            this.mBitmap = BitmapFactory.decodeFile(this.mBitmapPath, options);
        }

        this.m_pixels = new int[this.mBitmap.getWidth() * this.mBitmap.getHeight()];
        this.m_pixelInfo = new int[this.m_pixels.length];
        this.mBitmap.getPixels(this.m_pixels, 0, this.mBitmap.getWidth(), 0, 0, this.mBitmap.getWidth(), this.mBitmap.getHeight());

        for(int i = 0; i < this.m_pixels.length; ++i) {
            int _R = Color.red(this.m_pixels[i]);
            int _G = Color.green(this.m_pixels[i]);
            int _B = Color.blue(this.m_pixels[i]);
            if (_R <= 128 && _G <= 128 && _B <= 128) {

                if(mMode == 0)
                    this.m_pixelInfo[i] = 0;
                else
                    this.m_pixelInfo[i] = 1;

            } else {

                if(mMode == 0)
                    this.m_pixelInfo[i] = 1;
                else
                    this.m_pixelInfo[i] = 0;
            }
        }

        return true;
    }

    private String reSizeByteArray(byte[] _bytes) {
        this.m_BitmapHexString = this.ConvertbyteArrayToHex(_bytes);
        Log.d("Hexdata", this.m_BitmapHexString);
        return this.m_BitmapHexString;
    }

    private void setBitMapHexString(String _str) {
        this.m_BitmapHexString = _str;
    }

    private String getBitMapHexString() {
        return this.m_BitmapHexString == null ? "" : this.m_BitmapHexString;
    }

    private int getPrintWidth() {
        return this.PRINTWIDTH;
    }

    private int getPrintHeight() {
        return this.mBitmap != null ? this.mBitmap.getHeight() : 0;
    }

    private void ConvertDataToIntArray() {
        this.m_hash = new int[this.PRINTWIDTH][this.mBitmap.getHeight()];
        int tmp_count = 0;

        for(int h = 0; h < this.mBitmap.getHeight(); ++h) {
            for(int i = 0; i < this.mBitmap.getWidth(); ++i) {
                this.m_hash[i + 2][h] = this.m_pixelInfo[tmp_count];
                ++tmp_count;
            }
        }

    }

    private byte[] ConvertIntArrayToByteArray() {
        this.bytedata = null;
        this.bytedata = new ByteData(this.PRINTWIDTH * this.mBitmap.getHeight() / 8);
        new ArrayList();

        for(int h = 0; h < this.mBitmap.getHeight(); ++h) {
            for(int i = 0; i < this.PRINTWIDTH; i += 8) {
                byte result = (byte)(this.m_hash[i][h] * 128 + this.m_hash[i + 1][h] * 64 + this.m_hash[i + 2][h] * 32 + this.m_hash[i + 3][h] * 16 + this.m_hash[i + 4][h] * 8 + this.m_hash[i + 5][h] * 4 + this.m_hash[i + 6][h] * 2 + this.m_hash[i + 7][h]);
                if (result == 0) {
                    ++this.ffCount;
                    if (this.ffCount == 255) {
                        this.bytedata.add((byte)0);
                        this.bytedata.add((byte)-1);
                        this.ffCount = 0;
                    }
                } else {
                    if (this.ffCount != 0) {
                        this.bytedata.add((byte)0);
                        this.bytedata.add((byte)this.ffCount);
                        this.ffCount = 0;
                    }

                    this.bytedata.add(result);
                }
            }
        }

        return this.bytedata.getBytes_noNullData();
    }

    private String ConvertbyteArrayToHex(byte[] _byte) {
        StringBuilder sb = new StringBuilder();
        byte[] var3 = _byte;
        int var4 = _byte.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            byte b = var3[var5];
            String _str = "0x" + String.format("%02x", b & 255) + ",";
            sb.append(_str);
        }

        return sb.toString();
    }

    private void FFCounter(String _str, StringBuilder _sb) {
        if (_str == "00") {
            ++this.ffCount;
            if (this.ffCount == 255) {
                _sb.append("00");
                _sb.append("FF");
                this.ffCount = 0;
            }
        } else {
            if (this.ffCount != 0) {
                _sb.append("00");
                _sb.append(String.format("%02X", this.ffCount));
                this.ffCount = 0;
            }

            _sb.append(_str);
        }

    }

    private void setReadyIntArray() {
        this.m_hash = new int[this.PRINTWIDTH][this.mBitmap.getHeight()];

        for(int i = 0; i < this.mBitmap.getHeight(); ++i) {
            this.m_hash[0][i] = 0;
            this.m_hash[1][i] = 0;
            this.m_hash[this.PRINTWIDTH - 2][i] = 0;
            this.m_hash[this.PRINTWIDTH - 1][i] = 0;
        }

    }

    public static final byte[] intToByteArray(int value) {
        byte[] tmp = new byte[]{(byte)value, (byte)(value >> 8), (byte)(value >> 16), (byte)(value >> 24)};
        return tmp;
    }

    public byte[] getBmpHeader(Bitmap orgBitmap) {
        if (orgBitmap == null) {
            return null;
        } else {
            int width = orgBitmap.getWidth();
            int height = orgBitmap.getHeight();
            byte[] dummyBytesPerRow = null;
            boolean hasDummy = false;
            int rowWidthInBytes = width / 8;
            int i;
            if (rowWidthInBytes % 4 > 0) {
                hasDummy = true;
                dummyBytesPerRow = new byte[4 - rowWidthInBytes % 4];

                for(i = 0; i < dummyBytesPerRow.length; ++i) {
                    dummyBytesPerRow[i] = -1;
                }
            }

            i = (rowWidthInBytes + (hasDummy ? dummyBytesPerRow.length : 0)) * height;
            int imageDataOffset = 54;
            int fileSize = i + imageDataOffset;
            ByteBuffer buffer = ByteBuffer.allocate(fileSize);
            buffer.put((byte)66);
            buffer.put((byte)77);
            buffer.put(writeInt(fileSize));
            buffer.put(writeShort((short)0));
            buffer.put(writeShort((short)0));
            buffer.put(writeInt(imageDataOffset));
            buffer.put(writeInt(40));
            buffer.put(writeInt(width + (hasDummy ? (dummyBytesPerRow.length == 3 ? 1 : 0) : 0)));
            buffer.put(writeInt(height));
            buffer.put(writeShort((short)1));
            buffer.put(writeShort((short)1));
            buffer.put(writeInt(0));
            buffer.put(writeInt(i));
            buffer.put(writeInt(0));
            buffer.put(writeInt(0));
            buffer.put(writeInt(0));
            buffer.put(writeInt(0));
            return buffer.array();
        }
    }

    private static byte[] writeInt(int value) {
        byte[] b = new byte[]{(byte)(value & 255), (byte)((value & '\uff00') >> 8), (byte)((value & 16711680) >> 16), (byte)((value & -16777216) >> 24)};
        return b;
    }

    private static byte[] writeShort(short value) {
        byte[] b = new byte[]{(byte)(value & 255), (byte)((value & '\uff00') >> 8)};
        return b;
    }

    class ByteData {
        byte[] m_byte;
        int loc;

        public ByteData(int _byteCount) {
            this.m_byte = new byte[_byteCount];
            this.loc = 0;
        }

        public void add(byte _b) {
            this.m_byte[this.loc] = _b;
            ++this.loc;
        }

        public void add(byte[] _b) {
            for(int i = 0; i < _b.length; ++i) {
                this.m_byte[this.loc] = _b[i];
                ++this.loc;
            }

        }

        public void Clear() {
            this.m_byte = null;
            this.loc = 0;
        }

        public void Reset(int _byteCount) {
            this.m_byte = new byte[_byteCount];
            this.loc = 0;
        }

        public byte[] getBytes() {
            return this.m_byte;
        }

        public byte[] getBytes_noNullData() {
            byte[] tmp = new byte[this.loc];
            System.arraycopy(this.m_byte, 0, tmp, 0, this.loc);
            return tmp;
        }
    }

    public interface ImagePrintEventListenser {
        void EventImageToByteString(String var1);

        void EventImageToByte(byte[] var1);

        void PrintCompelete();
    }

    private static enum inputType {
        BITMAP,
        FILEPATH,
        DEBUGTEST;

        private inputType() {
        }
    }
}
