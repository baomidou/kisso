package com.baomidou.kisso.common.bcprov.jcajce.provider.config;

import javax.crypto.spec.DHParameterSpec;

import com.baomidou.kisso.common.bcprov.jce.spec.ECParameterSpec;

public interface ProviderConfiguration
{
    ECParameterSpec getEcImplicitlyCa();

    DHParameterSpec getDHDefaultParameters(int keySize);
}
