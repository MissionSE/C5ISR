package com.missionse.nfc;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Parcelable;

public class NfcUtilities {

	public static NdefMessage[] parseIntent(final Intent intent) {
		NdefMessage[] messages = null;

		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

			if (rawMessages != null) {
				messages = new NdefMessage[rawMessages.length];
				for (int i = 0; i < rawMessages.length; i++) {
					messages[i] = (NdefMessage) rawMessages[i];
				}
			} else {
				// Unknown tag type
				byte[] empty = new byte[0];
				byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
				Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				byte[] payload = NfcUtilities.dumpTagData(tag).getBytes();
				NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
				NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
				messages = new NdefMessage[] { msg };
			}
		}
		return messages;
	}

	public static String dumpTagData(final Parcelable p) {
		StringBuilder stringBuilder = new StringBuilder();
		Tag tag = (Tag) p;
		byte[] id = tag.getId();
		stringBuilder.append("Tag ID (hex): ").append(getHex(id)).append("\n");
		stringBuilder.append("Tag ID (dec): ").append(getDec(id)).append("\n");
		stringBuilder.append("ID (reversed): ").append(getReversed(id)).append("\n");

		String prefix = "android.nfc.tech.";
		stringBuilder.append("Technologies: ");
		for (String tech : tag.getTechList()) {
			stringBuilder.append(tech.substring(prefix.length()));
			stringBuilder.append(", ");
		}
		stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
		for (String tech : tag.getTechList()) {
			if (tech.equals(MifareClassic.class.getName())) {
				stringBuilder.append('\n');
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
				stringBuilder.append("Mifare Classic type: ");
				stringBuilder.append(type);
				stringBuilder.append('\n');

				stringBuilder.append("Mifare size: ");
				stringBuilder.append(mifareTag.getSize() + " bytes");
				stringBuilder.append('\n');

				stringBuilder.append("Mifare sectors: ");
				stringBuilder.append(mifareTag.getSectorCount());
				stringBuilder.append('\n');

				stringBuilder.append("Mifare blocks: ");
				stringBuilder.append(mifareTag.getBlockCount());
			}

			if (tech.equals(MifareUltralight.class.getName())) {
				stringBuilder.append('\n');
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
				stringBuilder.append("Mifare Ultralight type: ");
				stringBuilder.append(type);
			}
		}

		return stringBuilder.toString();
	}

	private static String getHex(final byte[] bytes) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = bytes.length - 1; i >= 0; --i) {
			int b = bytes[i] & 0xff;
			if (b < 0x10) {
				stringBuilder.append('0');
			}
			stringBuilder.append(Integer.toHexString(b));
			if (i > 0) {
				stringBuilder.append(" ");
			}
		}
		return stringBuilder.toString();
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
}
