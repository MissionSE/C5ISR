package com.missionse.nfc;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

import android.nfc.NdefRecord;

/**
 * Acts as a NdefRecord that contains text as a content.
 */
public class TextRecord {

	private static final int LANG_CODE_LENGTH = 0077;
	private static final int TEXT_ENCODE = 0200;
	private static final int UTF_BIT = 7;

	private final String mText;
	private final Locale mLocale;
	private final Charset mCharacterSet;

	/**
	 * Creates a new TextRecord given various parameters.
	 * @param content the text content of the TextRecord
	 * @param locale the locale to encode the content in
	 * @param set the character set to use when encoding the content
	 */
	public TextRecord(final String content, final Locale locale, final Charset set) {
		mText = content;
		this.mLocale = locale;
		mCharacterSet = set;
	}

	/**
	 * Retrieves the text content of the TextRecord.
	 * @return the text content
	 */
	public String getText() {
		return mText;
	}

	/**
	 * Retrieves the locale of the TextRecord.
	 * @return the locale
	 */
	public Locale getLocale() {
		return mLocale;
	}

	/**
	 * Retrieves the character set of the TextRecord.
	 * @return the character set
	 */
	public Charset getCharacterSet() {
		return mCharacterSet;
	}

	/**
	 * Determines whether a given NdefRecord is a TextRecord.
	 * @param record the record on which to determine TextRecord-ness
	 * @return whether or not the record was a TextRecord
	 */
	public static boolean isTextRecord(final NdefRecord record) {
		return (record.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(record.getType(), NdefRecord.RTD_TEXT));
	}

	/**
	 * Acts as an exception to be thrown if TextRecord operations are done on a non-TextRecord.
	 */
	public static class NotATextRecordException extends Exception {
		private static final long serialVersionUID = 2583701390031556826L;

		/**
		 * Creates a generic NotATextRecordException.
		 * @param message the message to display when this exception is thrown
		 */
		public NotATextRecordException(final String message) {
			super(message);
		}
	}

	/**
	 * Parses a TextRecord from an NdefRecord.
	 * @param record the raw NdefRecord to parse
	 * @return the TextRecord result of the parsing
	 * @throws UnsupportedEncodingException thrown when the specified encoding is not supported on this device
	 * @throws NotATextRecordException thrown when this method is envoke on a non-TextRecord
	 */
	public static TextRecord parseTextRecord(final NdefRecord record) throws UnsupportedEncodingException,
			NotATextRecordException {
		if (!TextRecord.isTextRecord(record)) {
			throw new NotATextRecordException("Did you forget to check if this was a valid TextRecord first?");
		}

		/*
		 * payload[0] contains the "Status Byte Encodings" field, per the NFC Forum "Text Record Type Definition"
		 * section 3.2.1.
		 * 
		 * bit7 is the Text Encoding Field.
		 * if (Bit_7 == 0): The mText is encoded in UTF-8
		 * if (Bit_7 == 1): The mText is encoded in UTF16
		 * Bit_6 is reserved for future use and must be set to zero.
		 * 
		 * Bits 5 to 0 are the length of the IANA language code.
		 */
		byte[] payload = record.getPayload();

		int languageCodeLength = payload[0] & LANG_CODE_LENGTH;
		String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

		String textEncoding;
		if ((payload[0] & TEXT_ENCODE) == 0) {
			textEncoding = "UTF-8";
		} else {
			textEncoding = "UTF-16";
		}
		String text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);

		return new TextRecord(text, new Locale(languageCode), Charset.forName(textEncoding));
	}

	/**
	 * Converts a TextRecord to an NdefRecord.
	 * @param record the TextRecord to convert
	 * @return a new NdefRecord
	 */
	public static NdefRecord toNdefRecord(final TextRecord record) {
		byte[] langBytes = record.getLocale().getLanguage().getBytes(Charset.forName("US-ASCII"));
		Charset utfEncoding = record.getCharacterSet();
		byte[] textBytes = record.getText().getBytes(utfEncoding);

		int utfBit = 0;
		if (!record.getCharacterSet().displayName().equals("UTF-8")) {
			utfBit = (1 << UTF_BIT);
		}
		char status = (char) (utfBit + langBytes.length);

		byte[] data = new byte[1 + langBytes.length + textBytes.length];
		data[0] = (byte) status;
		System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
	}
}
