package com.missionse.nfc;

import java.util.ArrayList;
import java.util.List;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Parcelable;

import com.missionse.nfc.TextRecord;

public class NfcUtilities {

	public static String dumpTagData(final Parcelable p) {
		StringBuilder sb = new StringBuilder();
		Tag tag = (Tag) p;
		byte[] id = tag.getId();
		sb.append("Tag ID (hex): ").append(getHex(id)).append("\n");
		sb.append("Tag ID (dec): ").append(getDec(id)).append("\n");
		sb.append("ID (reversed): ").append(getReversed(id)).append("\n");

		String prefix = "android.nfc.tech.";
		sb.append("Technologies: ");
		for (String tech : tag.getTechList()) {
			sb.append(tech.substring(prefix.length()));
			sb.append(", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		for (String tech : tag.getTechList()) {
			if (tech.equals(MifareClassic.class.getName())) {
				sb.append('\n');
				MifareClassic mifareTag = MifareClassic.get(tag);
				String type = "Unknown";
				switch (mifareTag.getType()) {
					case MifareClassic.TYPE_CLASSIC:
						type = "Classic";
						break;
					case MifareClassic.TYPE_PLUS:
						type = "Plus";
						break;
					case MifareClassic.TYPE_PRO:
						type = "Pro";
						break;
				}
				sb.append("Mifare Classic type: ");
				sb.append(type);
				sb.append('\n');

				sb.append("Mifare size: ");
				sb.append(mifareTag.getSize() + " bytes");
				sb.append('\n');

				sb.append("Mifare sectors: ");
				sb.append(mifareTag.getSectorCount());
				sb.append('\n');

				sb.append("Mifare blocks: ");
				sb.append(mifareTag.getBlockCount());
			}

			if (tech.equals(MifareUltralight.class.getName())) {
				sb.append('\n');
				MifareUltralight mifareUlTag = MifareUltralight.get(tag);
				String type = "Unknown";
				switch (mifareUlTag.getType()) {
					case MifareUltralight.TYPE_ULTRALIGHT:
						type = "Ultralight";
						break;
					case MifareUltralight.TYPE_ULTRALIGHT_C:
						type = "Ultralight C";
						break;
				}
				sb.append("Mifare Ultralight type: ");
				sb.append(type);
			}
		}

		return sb.toString();
	}

	private static String getHex(final byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = bytes.length - 1; i >= 0; --i) {
			int b = bytes[i] & 0xff;
			if (b < 0x10) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(b));
			if (i > 0) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	private static long getDec(final byte[] bytes) {
		long result = 0;
		long factor = 1;
		for (byte b : bytes) {
			long value = b & 0xffl;
			result += value * factor;
			factor *= 256l;
		}
		return result;
	}

	private static long getReversed(final byte[] bytes) {
		long result = 0;
		long factor = 1;
		for (int i = bytes.length - 1; i >= 0; --i) {
			long value = bytes[i] & 0xffl;
			result += value * factor;
			factor *= 256l;
		}
		return result;
	}

	public static List<TextRecord> parse(final NdefMessage message) {
		return getTextRecords(message.getRecords());
	}

	private static List<TextRecord> getTextRecords(final NdefRecord[] records) {
		List<TextRecord> elements = new ArrayList<TextRecord>();
		for (final NdefRecord record : records) {
			if (TextRecord.isTextRecord(record)) {
				elements.add(TextRecord.parse(record));
			}
		}
		return elements;
	}
}
