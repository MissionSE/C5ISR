package com.missionse.nfc;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

import android.nfc.NdefRecord;

public class TextRecord {
	private final String languageCode;
	private final String text;

	public TextRecord(final String code, final String content) {
		languageCode = code;
		text = content;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public String getText() {
		return text;
	}

	public static TextRecord parse(final NdefRecord record) {
		if (record.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(record.getType(), NdefRecord.RTD_TEXT)) {

			try {
				/*
				 * payload[0] contains the "Status Byte Encodings" field, per the NFC Forum "Text Record Type Definition"
				 * section 3.2.1.
				 * 
				 * bit7 is the Text Encoding Field.
				 * if (Bit_7 == 0): The text is encoded in UTF-8 if (Bit_7 == 1): The text is encoded in UTF16 Bit_6 is
				 * reserved for future use and must be set to zero.
				 * 
				 * Bits 5 to 0 are the length of the IANA language code.
				 */
				byte[] payload = record.getPayload();

				String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";

				int languageCodeLength = payload[0] & 0077;
				String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

				String text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1,
						textEncoding);

				return new TextRecord(languageCode, text);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();;
			}
		}
		return null;
	}

	public static boolean isTextRecord(final NdefRecord record) {
		TextRecord potentialTextRecord = parse(record);
		if (potentialTextRecord == null) {
			return false;
		}
		return true;
	}

	public static NdefRecord newTextRecord(final String text, final Locale locale, final boolean encodeInUtf8) {
		byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

		Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
		byte[] textBytes = text.getBytes(utfEncoding);

		int utfBit = encodeInUtf8 ? 0 : (1 << 7);
		char status = (char) (utfBit + langBytes.length);

		byte[] data = new byte[1 + langBytes.length + textBytes.length];
		data[0] = (byte) status;
		System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
	}
}
