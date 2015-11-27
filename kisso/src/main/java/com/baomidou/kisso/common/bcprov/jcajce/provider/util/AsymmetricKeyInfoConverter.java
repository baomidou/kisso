package com.baomidou.kisso.common.bcprov.jcajce.provider.util;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.baomidou.kisso.common.bcprov.asn1.pkcs.PrivateKeyInfo;
import com.baomidou.kisso.common.bcprov.asn1.x509.SubjectPublicKeyInfo;

public interface AsymmetricKeyInfoConverter {

	PrivateKey generatePrivate( PrivateKeyInfo keyInfo ) throws IOException;

	PublicKey generatePublic( SubjectPublicKeyInfo keyInfo ) throws IOException;
}
