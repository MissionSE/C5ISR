package com.missionse.uiextensions.imageview;

import java.io.InputStream;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * Decodes an InputStream (assumed to be a ".gif" file), and extracts single mFrames as images, to display
 * mInputStreamSource an animation sequence.
 */
public class GifDecoder {

	/**
	 * Code representing dispose code for a given frame.
	 */
	private enum DisposeCode {
		DISPOSE_NO_ACTION, DISPOSE_LEAVE_STREAM, DISPOSE_RESTORE_BACKGROUND, DISPOSE_RESTORE_PREVIOUS
	}

	/**
	 * Status code for success of resource stream reading.
	 */
	private enum ReadStatus {
		STATUS_OK, STATUS_FORMAT_ERROR, STATUS_OPEN_ERROR
	}

	private static final int MAX_DECODER_PIXEL_STACK_SIZE = 4096;
	private static final int DATA_BLOCK_SIZE = 256;
	private static final int BYTE_SHIFT_LENGTH = 8;
	private static final int BYTE_LENGTH = 8;
	private static final int TWO_BYTES_SHIFT_LENGTH = 16;
	private static final int GIF_HEADER_LENGTH = 6;
	private static final int MILLI_CONVERSION_FACTOR = 10;
	private static final int ONES_COMPLEMENT_OF_ZERO = 0xff;
	private static final int BYTES_PER_COLOR = 3;

	private static final int COLOR_TABLE_FLAG_OFFSET = 0x80;
	private static final int COLOR_TABLE_SIZE_OFFSET = 0x07;
	private static final int INTERLACE_OFFSET = 0x40;
	private static final int DISPOSE_CODE_OFFSET = 0x1c;
	private static final int COLOR_OFFSET = 0xff000000;

	private static final int FRAME_SEPARATOR = 0x2C;
	private static final int CONTENT_EXTENSION = 0x21;
	private static final int GRAPHICS_CONTROL_EXTENSION = 0xf9;
	private static final int APPLICATION_EXTENSION = 0xff;
	private static final int APPLICATION_EXTENSION_LENGTH = 11;
	private static final int COMMENT_EXTENSION = 0xfe;
	private static final int PLAIN_TEXT_EXTENSION = 0x01;
	private static final int TERMINATOR = 0x3b;
	private static final int BAD_BYTE = 0x00;

	private static final int SOURCE_INCREMENT_DEFAULT = 8;
	private static final int SOURCE_INCREMENT_HALF = 4;
	private static final int SOURCE_TWO = 2;
	private static final int SOURCE_THREE = 3;
	private static final int SOURCE_FOUR = 4;

	private static final int NULL_CODE = -1;

	private ReadStatus mStatus;

	private InputStream mInputStreamSource;
	private int mFullImageWidth;
	private int mFullImageHeight;

	private int mLoopCount = 1; // iterations; 0 = repeat forever

	private boolean mUsingGlobalColorTable;
	private int mGlobalColorTableSize;
	private int[] mGlobalColorTable;

	private boolean mUsingLocalColorTable;
	private int mLocalColorTableSize;
	private int[] mLocalColorTable;

	private int[] mActiveColorTable;

	private int mBackgroundColorIndex;
	private int mBackgroundColor;
	private int mPreviousBackgroundColor;

	private boolean mIsInterlaced;

	private Bitmap mCurrentFrame;
	private int mCurrentFrameX, mCurrentFrameY, mCurrentFrameWidth, mCurrentFrameHeight;

	private Bitmap mPreviousFrame;
	private int mPreviousFrameX, mPreviousFrameY, mPreviousFrameWidth, mPreviousFrameHeight;

	private int mBlockSize = 0;
	private byte[] mCurrentDataBlock = new byte[DATA_BLOCK_SIZE];

	private DisposeCode mDisposeStatus = DisposeCode.DISPOSE_NO_ACTION;
	private DisposeCode mLastDisposeStatus = DisposeCode.DISPOSE_NO_ACTION;

	private boolean mUseTransparency = false;

	private int mStreamDelay = 0;
	private int mTransparentColorIndex;

	// LZW decoder working arrays
	private final short[] mPrefix = new short[MAX_DECODER_PIXEL_STACK_SIZE];
	private final byte[] mSuffix = new byte[MAX_DECODER_PIXEL_STACK_SIZE];
	private byte[] mPixelStack = new byte[MAX_DECODER_PIXEL_STACK_SIZE + 1];
	private byte[] mPixels;
	private Vector<GifFrame> mFrames;
	private int mFrameCount;

	/**
	 * Basic structure representing a single frame of a .gif mImage.
	 */
	private static class GifFrame {
		private Bitmap mImage;
		private int mDelay;

		public GifFrame(final Bitmap image, final int delay) {
			mImage = image;
			mDelay = delay;
		}
	}

	/**
	 * Gets the display duration for specified frame.
	 * 
	 * @param frameIndex index of frame
	 * @return the stream delay in milliseconds
	 */
	public int getDelay(final int frameIndex) {
		mStreamDelay = -1;
		if ((frameIndex >= 0) && (frameIndex < mFrameCount)) {
			mStreamDelay = mFrames.elementAt(frameIndex).mDelay;
		}
		return mStreamDelay;
	}

	/**
	 * Gets the number of frames read from the file.
	 * 
	 * @return the current frame count
	 */
	public int getFrameCount() {
		return mFrameCount;
	}

	/**
	 * Gets the first (or only) mCurrentFrame read.
	 * 
	 * @return BufferedBitmap containing first frame, or null if none.
	 */
	public Bitmap getBitmap() {
		return getFrame(0);
	}

	/**
	 * Gets the "Netscape" iteration count, if any. A count of 0 means repeat indefinitely.
	 * 
	 * @return iteration count if one was specified, else 1
	 */
	public int getLoopCount() {
		return mLoopCount;
	}

	/**
	 * Creates new frame from current data (and previous frames as specified by their disposition codes).
	 */
	private void setPixels() {
		// Expose destination frame's pixels as an int array.
		int[] dest = new int[mFullImageWidth * mFullImageHeight];
		// Fill in starting frame's contents based on last frame's dispose code.
		if (mLastDisposeStatus != DisposeCode.DISPOSE_NO_ACTION) {
			if (mLastDisposeStatus == DisposeCode.DISPOSE_RESTORE_PREVIOUS) {
				int n = mFrameCount - 2;
				if (n > 0) {
					mPreviousFrame = getFrame(n - 1);
				} else {
					mPreviousFrame = null;
				}
			}
			if (mPreviousFrame != null) {
				mPreviousFrame.getPixels(dest, 0, mFullImageWidth, 0, 0, mFullImageWidth, mFullImageHeight);
				if (mLastDisposeStatus == DisposeCode.DISPOSE_RESTORE_BACKGROUND) {
					int c = 0;
					if (!mUseTransparency) {
						c = mPreviousBackgroundColor;
					}
					for (int i = 0; i < mPreviousFrameHeight; i++) {
						int n1 = (mPreviousFrameY + i) * mFullImageWidth + mPreviousFrameX;
						int n2 = n1 + mPreviousFrameWidth;
						for (int k = n1; k < n2; k++) {
							dest[k] = c;
						}
					}
				}
			}
		}
		// Copy each source line to the appropriate place in the destination.
		int pass = 1;
		int inc = SOURCE_INCREMENT_DEFAULT;
		int iline = 0;
		for (int i = 0; i < mCurrentFrameHeight; i++) {
			int line = i;
			if (mIsInterlaced) {
				if (iline >= mCurrentFrameHeight) {
					pass++;
					switch (pass) {
						case SOURCE_TWO:
							iline = SOURCE_INCREMENT_HALF;
							break;
						case SOURCE_THREE:
							iline = 2;
							inc = SOURCE_INCREMENT_HALF;
							break;
						case SOURCE_FOUR:
							iline = 1;
							inc = 2;
							break;
						default:
							break;
					}
				}
				line = iline;
				iline += inc;
			}
			line += mCurrentFrameY;
			if (line < mFullImageHeight) {
				int k = line * mFullImageWidth;
				int dx = k + mCurrentFrameX; // start of line in dest
				int dlim = dx + mCurrentFrameWidth; // end of dest line
				if ((k + mFullImageWidth) < dlim) {
					dlim = k + mFullImageWidth; // past dest edge
				}
				int sx = i * mCurrentFrameWidth; // start of line in source
				while (dx < dlim) {
					// map color and insert in destination
					int index = (mPixels[sx++]) & ONES_COMPLEMENT_OF_ZERO;
					int c = mActiveColorTable[index];
					if (c != 0) {
						dest[dx] = c;
					}
					dx++;
				}
			}
		}
		mCurrentFrame = Bitmap.createBitmap(dest, mFullImageWidth, mFullImageHeight, Config.ARGB_4444);
	}

	/**
	 * Gets the frame contents of a given frame.
	 * 
	 * @param frameNumber the number of the frame
	 * @return the frame, or null if the frameNumber is invalid or there are no frames to return
	 */
	public Bitmap getFrame(int frameNumber) {
		if (mFrameCount <= 0) {
			return null;
		}
		frameNumber = frameNumber % mFrameCount;
		return mFrames.elementAt(frameNumber).mImage;
	}

	/**
	 * Reads .gif frame from stream.
	 * 
	 * @param inputStream the .gif file as a stream
	 * @return read status code
	 */
	public ReadStatus read(final InputStream inputStream) {
		init();
		if (inputStream != null) {
			mInputStreamSource = inputStream;
			readHeader();
			if (!isInErrorState()) {
				readContents();
				if (mFrameCount < 0) {
					mStatus = ReadStatus.STATUS_FORMAT_ERROR;
				}
			}
		} else {
			mStatus = ReadStatus.STATUS_OPEN_ERROR;
		}
		try {
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mStatus;
	}

	/**
	 * Decodes LZW mCurrentFrame data into pixel array. Adapted from John Cristy's BitmapMagick.
	 */
	private void decodeBitmapData() {
		int code, i, inCode, oldCode = NULL_CODE, bits = 0, count = 0, datum = 0, first = 0, top = 0, bi = 0, pi = 0;
		if ((mPixels == null) || (mPixels.length < mCurrentFrameWidth * mCurrentFrameHeight)) {
			mPixels = new byte[mCurrentFrameWidth * mCurrentFrameHeight];
		}
		int dataSize = read();
		int clear = 1 << dataSize;
		int available = clear + 2;
		int codeSize = dataSize + 1;
		int codeMask = (1 << codeSize) - 1;
		for (code = 0; code < clear; code++) {
			mPrefix[code] = 0;
			mSuffix[code] = (byte) code;
		}
		for (i = 0; i < mCurrentFrameWidth * mCurrentFrameHeight;) {
			if (top == 0) {
				if (bits < codeSize) {
					if (count == 0) {
						count = readBlock();
						if (count <= 0) {
							break;
						}
						bi = 0;
					}
					datum += ((mCurrentDataBlock[bi]) & ONES_COMPLEMENT_OF_ZERO) << bits;
					bits += BYTE_LENGTH;
					bi++;
					count--;
					continue;
				}
				code = datum & codeMask;
				datum >>= codeSize;
				bits -= codeSize;
				if ((code > available) || (code == clear + 1)) {
					break;
				}
				if (code == clear) {
					codeSize = dataSize + 1;
					codeMask = (1 << codeSize) - 1;
					available = clear + 2;
					oldCode = NULL_CODE;
					continue;
				}
				if (oldCode == NULL_CODE) {
					mPixelStack[top++] = mSuffix[code];
					oldCode = code;
					first = code;
					continue;
				}
				inCode = code;
				if (code == available) {
					mPixelStack[top++] = (byte) first;
					code = oldCode;
				}
				while (code > clear) {
					mPixelStack[top++] = mSuffix[code];
					code = mPrefix[code];
				}
				first = (mSuffix[code]) & ONES_COMPLEMENT_OF_ZERO;
				if (available >= MAX_DECODER_PIXEL_STACK_SIZE) {
					break;
				}
				mPixelStack[top++] = (byte) first;
				mPrefix[available] = (short) oldCode;
				mSuffix[available] = (byte) first;
				available++;
				if (((available & codeMask) == 0) && (available < MAX_DECODER_PIXEL_STACK_SIZE)) {
					codeSize++;
					codeMask += available;
				}
				oldCode = inCode;
			}
			top--; // Pop a pixel off the pixel stack.
			mPixels[pi++] = mPixelStack[top];
			i++;
		}
		for (i = pi; i < mCurrentFrameWidth * mCurrentFrameHeight; i++) {
			mPixels[i] = 0; // clear missing mPixels
		}
	}

	/**
	 * Returns true if an error was encountered during reading/decoding.
	 */
	protected boolean isInErrorState() {
		return mStatus != ReadStatus.STATUS_OK;
	}

	/**
	 * Initializes or re-initializes reader.
	 */
	protected void init() {
		mStatus = ReadStatus.STATUS_OK;
		mFrameCount = 0;
		mFrames = new Vector<GifFrame>();
		mGlobalColorTable = null;
		mLocalColorTable = null;
	}

	/**
	 * Reads a single byte from the input stream.
	 */
	protected int read() {
		int curByte = 0;
		try {
			curByte = mInputStreamSource.read();
		} catch (Exception e) {
			mStatus = ReadStatus.STATUS_FORMAT_ERROR;
		}
		return curByte;
	}

	/**
	 * Reads next variable length mCurrentDataBlock from input.
	 * 
	 * @return number of bytes stored mInputStreamSource "buffer"
	 */
	protected int readBlock() {
		mBlockSize = read();
		int n = 0;
		if (mBlockSize > 0) {
			try {
				int count = 0;
				while (n < mBlockSize) {
					count = mInputStreamSource.read(mCurrentDataBlock, n, mBlockSize - n);
					if (count == -1) {
						break;
					}
					n += count;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (n < mBlockSize) {
				mStatus = ReadStatus.STATUS_FORMAT_ERROR;
			}
		}
		return n;
	}

	/**
	 * Reads color table as 256 RGB integer values.
	 * 
	 * @param numberOfColors number of colors to read
	 * @return array containing 256 colors (packed ARGB with full alpha)
	 */
	protected int[] readColorTable(final int numberOfColors) {
		int numberOfBytes = BYTES_PER_COLOR * numberOfColors;
		int[] tab = null;
		byte[] c = new byte[numberOfBytes];
		int n = 0;
		try {
			n = mInputStreamSource.read(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (n < numberOfBytes) {
			mStatus = ReadStatus.STATUS_FORMAT_ERROR;
		} else {
			tab = new int[DATA_BLOCK_SIZE];
			int i = 0;
			int j = 0;
			while (i < numberOfColors) {
				int r = (c[j++]) & ONES_COMPLEMENT_OF_ZERO;
				int g = (c[j++]) & ONES_COMPLEMENT_OF_ZERO;
				int b = (c[j++]) & ONES_COMPLEMENT_OF_ZERO;
				tab[i++] = COLOR_OFFSET | (r << TWO_BYTES_SHIFT_LENGTH) | (g << BYTE_SHIFT_LENGTH) | b;
			}
		}
		return tab;
	}

	/**
	 * Main file parser. Reads GIF content blocks.
	 */
	protected void readContents() {
		boolean done = false;
		while (!(done || isInErrorState())) {
			int code = read();
			switch (code) {
				case FRAME_SEPARATOR:
					readBitmap();
					break;
				case CONTENT_EXTENSION:
					code = read();
					switch (code) {
						case GRAPHICS_CONTROL_EXTENSION:
							readGraphicControlExt();
							break;
						case APPLICATION_EXTENSION:
							readBlock();
							String app = "";
							for (int i = 0; i < APPLICATION_EXTENSION_LENGTH; i++) {
								app += (char) mCurrentDataBlock[i];
							}
							if (app.equals("NETSCAPE2.0")) {
								readNetscapeExt();
							} else {
								skip();
							}
							break;
						case COMMENT_EXTENSION:
							skip();
							break;
						case PLAIN_TEXT_EXTENSION:
							skip();
							break;
						default:
							skip();
					}
					break;
				case TERMINATOR:
					done = true;
					break;
				case BAD_BYTE:
				default:
					mStatus = ReadStatus.STATUS_FORMAT_ERROR;
			}
		}
	}

	/**
	 * Reads Graphics Control Extension values.
	 */
	protected void readGraphicControlExt() {
		read();
		int packed = read();
		mDisposeStatus = DisposeCode.values()[(packed & DISPOSE_CODE_OFFSET) >> 2]; // disposal method
		if (mDisposeStatus == DisposeCode.DISPOSE_NO_ACTION) {
			mDisposeStatus = DisposeCode.DISPOSE_LEAVE_STREAM; // elect to keep old frame if discretionary
		}
		mUseTransparency = (packed & 1) != 0;
		mStreamDelay = readShort() * MILLI_CONVERSION_FACTOR; // in milliseconds
		mTransparentColorIndex = read();
		read();
	}

	/**
	 * Reads .gif file header information.
	 */
	protected void readHeader() {
		String id = "";
		for (int i = 0; i < GIF_HEADER_LENGTH; i++) {
			id += (char) read();
		}
		if (!id.startsWith("GIF")) {
			mStatus = ReadStatus.STATUS_FORMAT_ERROR;
			return;
		}
		readLSD();
		if (mUsingGlobalColorTable && !isInErrorState()) {
			mGlobalColorTable = readColorTable(mGlobalColorTableSize);
			mBackgroundColor = mGlobalColorTable[mBackgroundColorIndex];
		}
	}

	/**
	 * Reads next frame.
	 */
	protected void readBitmap() {
		mCurrentFrameX = readShort(); // (sub)frame position & size
		mCurrentFrameY = readShort();
		mCurrentFrameWidth = readShort();
		mCurrentFrameHeight = readShort();
		int packed = read();
		mUsingLocalColorTable = (packed & COLOR_TABLE_FLAG_OFFSET) != 0;
		mLocalColorTableSize = (int) Math.pow(2, (packed & COLOR_TABLE_SIZE_OFFSET) + 1);
		// 1 - local color table flag interlace
		// 3 - sort flag
		// 4-5 - reserved local color table = 2 << (packed & 7);
		// 6-8 - local color table size
		mIsInterlaced = (packed & INTERLACE_OFFSET) != 0;
		if (mUsingLocalColorTable) {
			mLocalColorTable = readColorTable(mLocalColorTableSize);
			mActiveColorTable = mLocalColorTable;
		} else {
			mActiveColorTable = mGlobalColorTable;
			if (mBackgroundColorIndex == mTransparentColorIndex) {
				mBackgroundColor = 0;
			}
		}
		int save = 0;
		if (mUseTransparency) {
			save = mActiveColorTable[mTransparentColorIndex];
			mActiveColorTable[mTransparentColorIndex] = 0;
		}
		if (mActiveColorTable == null) {
			mStatus = ReadStatus.STATUS_FORMAT_ERROR;
		}
		if (isInErrorState()) {
			return;
		}
		decodeBitmapData();
		skip();
		if (isInErrorState()) {
			return;
		}
		mFrameCount++;
		mCurrentFrame = Bitmap.createBitmap(mFullImageWidth, mFullImageHeight, Config.ARGB_4444);
		setPixels();
		mFrames.addElement(new GifFrame(mCurrentFrame, mStreamDelay));
		if (mUseTransparency) {
			mActiveColorTable[mTransparentColorIndex] = save;
		}
		resetFrame();
	}

	/**
	 * Reads Logical Screen Descriptor.
	 */
	protected void readLSD() {
		mFullImageWidth = readShort();
		mFullImageHeight = readShort();
		int packed = read();
		// 1 : global color table flag
		// 2-4 : color resolution
		// 5 : global color table sort flag
		// 6-8 : global color table size
		mUsingGlobalColorTable = (packed & COLOR_TABLE_FLAG_OFFSET) != 0;
		mGlobalColorTableSize = 2 << (packed & COLOR_TABLE_SIZE_OFFSET);
		mBackgroundColorIndex = read(); // background color index
	}

	/**
	 * Reads "Netscape" extension to obtain iteration count.
	 */
	protected void readNetscapeExt() {
		do {
			readBlock();
			if (mCurrentDataBlock[0] == 1) {
				int firstByte = (mCurrentDataBlock[1]) & ONES_COMPLEMENT_OF_ZERO;
				int secondByte = (mCurrentDataBlock[2]) & ONES_COMPLEMENT_OF_ZERO;
				mLoopCount = (secondByte << BYTE_SHIFT_LENGTH) | firstByte;
			}
		} while ((mBlockSize > 0) && !isInErrorState());
	}

	/**
	 * Reads next 16-bit value, LSB first.
	 */
	protected int readShort() {
		return read() | (read() << BYTE_SHIFT_LENGTH);
	}

	/**
	 * Resets frame state for reading next frame.
	 */
	protected void resetFrame() {
		mLastDisposeStatus = mDisposeStatus;
		mPreviousFrameX = mCurrentFrameX;
		mPreviousFrameY = mCurrentFrameY;
		mPreviousFrameWidth = mCurrentFrameWidth;
		mPreviousFrameHeight = mCurrentFrameHeight;
		mPreviousFrame = mCurrentFrame;
		mPreviousBackgroundColor = mBackgroundColor;
		mDisposeStatus = DisposeCode.DISPOSE_NO_ACTION;
		mUseTransparency = false;
		mStreamDelay = 0;
		mLocalColorTable = null;
	}

	/**
	 * Skips variable length blocks up to and including next zero length data block.
	 */
	protected void skip() {
		do {
			readBlock();
		} while ((mBlockSize > 0) && !isInErrorState());
	}
}
