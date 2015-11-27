package com.baomidou.kisso.common.bcprov.asn1;

import java.io.IOException;

public interface ASN1ApplicationSpecificParser extends ASN1Encodable, InMemoryRepresentable {

	ASN1Encodable readObject() throws IOException;
}
