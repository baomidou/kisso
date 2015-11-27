package com.baomidou.kisso.common.bcprov.asn1;

import java.io.InputStream;

public interface ASN1OctetStringParser extends ASN1Encodable, InMemoryRepresentable {

	public InputStream getOctetStream();
}
