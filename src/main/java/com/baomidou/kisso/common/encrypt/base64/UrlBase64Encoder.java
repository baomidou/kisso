package com.baomidou.kisso.common.encrypt.base64;

public class UrlBase64Encoder extends Base64Encoder {

	public UrlBase64Encoder() {
		encodingTable[encodingTable.length - 2] = (byte) '-';
		encodingTable[encodingTable.length - 1] = (byte) '_';
		padding = (byte) '.';
		// we must re-create the decoding table with the new encoded values.
		initialiseDecodingTable();
	}

}
