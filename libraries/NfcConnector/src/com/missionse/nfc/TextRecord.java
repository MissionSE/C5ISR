package com.missionse.nfc;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

import android.nfc.NdefRecord;

public class TextRecord {

	private final String text;
	private final Locale locale;
	private final Charset characterSet;

	public TextRecord(final String content, final Locale locale, final Charset set) {
		text = content;
		this.locale = locale;
		characterSet = set;
	}

	public String getText() {
		return text;
	}

	public Locale getLocale() {
		return locale;
	}

	public Charset getCharacterSet() {
		return characterSet;
	}

	public static boolean isTextRecord(final NdefRecord record) {
		return (record.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(record.getType(), NdefRecord.RTD_TEXT));
	}

	public static class NotATextRecordException extends Exception {
		private static final long serialVersionUID = 2583701390031556826L;

		public NotATextRecordException(final String message) {
			super(message);
		}
	}

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
		 * if (Bit_7 == 0): The text is encoded in UTF-8
		 * if (Bit_7 == 1): The text is encoded in UTF16
		 * Bit_6 is reserved for future use and must be set to zero.
		 * 
		 * Bits 5 to 0 are the length of the IANA language code.
		 */
		byte[] payload = record.getPayload();

		int languageCodeLength = payload[0] & 0077;
		String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

		String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
		String text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);

		return new TextRecord(text, new Locale(languageCode), Charset.forName(textEncoding));
	}

	public static NdefRecord toNdefRecord(final TextRecord record) {
		byte[] langBytes = record.getLocale().getLanguage().getBytes(Charset.forName("US-ASCII"));
		Charset utfEncoding = record.getCharacterSet();
		byte[] textBytes = record.getText().getBytes(utfEncoding);

		int utfBit = record.getCharacterSet().displayName().equals("UTF-8") ? 0 : (1 << 7);
		char status = (char) (utfBit + langBytes.length);

		byte[] data = new byte[1 + langBytes.length + textBytes.length];
		data[0] = (byte) status;
		System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
	}
}