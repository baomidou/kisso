/*
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.kisso.common.signature;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baomidou.kisso.common.SSOConstants;
import com.baomidou.kisso.common.encrypt.MD5;
import com.baomidou.kisso.common.util.JoinUtil;
import com.baomidou.kisso.exception.AuthenticationException;
import com.baomidou.kisso.exception.MissingAlgorithmException;
import com.baomidou.kisso.exception.MissingKeyIdException;
import com.baomidou.kisso.exception.MissingRequiredHeaderException;
import com.baomidou.kisso.exception.MissingSignatureException;
import com.baomidou.kisso.exception.UnparsableSignatureException;

/**
 * <p>
 * 签名
 * </p>
 *
 * @author hubin
 * @since 2019-04-05
 */
public class Signature {
    private final String keyId;
    private final ShaAlgorithm shaAlgorithm;
    private final String signature;
    private final List<String> headers;
    private static final Pattern RFC_2617_PARAM = Pattern.compile("(\\w+)=\"([^\"]*)\"");

    public Signature(final String keyId, final String shaAlgorithm, final String signature, final String... headers) {
        this(keyId, getShaAlgorithm(shaAlgorithm), signature, headers);
    }

    private static ShaAlgorithm getShaAlgorithm(String shaAlgorithm) {
        if (shaAlgorithm == null) {
            throw new IllegalArgumentException("ShaAlgorithm cannot be null");
        }
        return ShaAlgorithm.get(shaAlgorithm);
    }

    public Signature(final String keyId, final ShaAlgorithm algorithm, final String signature, final String... headers) {
        this(keyId, algorithm, signature, Arrays.asList(headers));
    }

    public Signature(final String keyId, final String shaAlgorithm, final String signature, final List<String> headers) {
        this(keyId, getShaAlgorithm(shaAlgorithm), signature, headers);
    }

    public Signature(final String keyId, ShaAlgorithm shaAlgorithm, final String signature, final List<String> headers) {
        if (keyId == null || keyId.trim().isEmpty()) {
            throw new IllegalArgumentException("keyId is required.");
        }
        if (shaAlgorithm == null) {
            throw new IllegalArgumentException("shaAlgorithm is required.");
        }
        this.keyId = keyId;
        this.shaAlgorithm = shaAlgorithm;
        // this is the only one that can be null cause the object
        // can be used as a template/specification
        this.signature = signature;
        if (headers.size() == 0) {
            final List<String> list = Arrays.asList("date");
            this.headers = Collections.unmodifiableList(list);
        } else {
            this.headers = Collections.unmodifiableList(lowercase(headers));
        }
    }

    private List<String> lowercase(List<String> headers) {
        final List<String> list = new ArrayList<>(headers.size());
        for (String header : headers) {
            list.add(header.toLowerCase());
        }
        return list;
    }

    public String getKeyId() {
        return keyId;
    }

    public ShaAlgorithm getShaAlgorithm() {
        return shaAlgorithm;
    }

    public String getSignature() {
        return signature;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public static Signature fromString(String authorization) {
        try {
            authorization = normalize(authorization);
            final Map<String, String> map = new HashMap<>(16);
            final Matcher matcher = RFC_2617_PARAM.matcher(authorization);
            while (matcher.find()) {
                final String key = matcher.group(1).toLowerCase();
                final String value = matcher.group(2);
                map.put(key, value);
            }
            final List<String> headers = new ArrayList<String>();
            final String headerString = map.get("headers");
            if (headerString != null) {
                Collections.addAll(headers, headerString.toLowerCase().split(" +"));
            }
            final String keyid = map.get("keyid");
            if (keyid == null) {
                throw new MissingKeyIdException();
            }
            final String algorithm = map.get("shaalgorithm");
            if (algorithm == null) {
                throw new MissingAlgorithmException();
            }
            final String signature = map.get("signature");
            if (signature == null) {
                throw new MissingSignatureException();
            }
            final ShaAlgorithm parsedAlgorithm = ShaAlgorithm.get(algorithm);
            return new Signature(keyid, parsedAlgorithm, signature, headers);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Throwable e) {
            throw new UnparsableSignatureException(authorization, e);
        }
    }

    private static String normalize(String authorization) {
        final String start = "signature ";
        final String prefix = authorization.substring(0, start.length()).toLowerCase();
        if (prefix.equals(start)) {
            authorization = authorization.substring(start.length());
        }
        return authorization.trim();
    }

    public static String createSigningString(final List<String> required, String method, final String uri, Map<String, String> headers) {
        headers = lowercase(headers);
        final List<String> list = new ArrayList<>(required.size());
        for (final String key : required) {
            if ("(request-target)".equals(key)) {
                method = lowercase(method);
                list.add(JoinUtil.join(" ", "(request-target):", method, uri));
            } else {
                final String value = headers.get(key);
                if (value == null) {
                    throw new MissingRequiredHeaderException(key);
                }
                list.add(key + ": " + value);
            }
        }
        return JoinUtil.join("\n", list);
    }

    private static Map<String, String> lowercase(final Map<String, String> headers) {
        final Map<String, String> map = new HashMap<>(16);
        for (final Map.Entry<String, String> entry : headers.entrySet()) {
            map.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        return map;
    }

    private static String lowercase(final String spec) {
        return spec.toLowerCase();
    }

    /**
     * <p>
     * MD5 参数签名
     * </p>
     *
     * @param accessSecret 访问密钥
     * @param params       参数
     * @return
     */
    public static String md5Signing(String accessSecret, Map<String, Object> params) {
        requireNonNull(accessSecret, "AccessSecret cannot be null");
        Object[] keys = params.keySet().toArray();
        Arrays.sort(keys);
        StringBuilder temp = new StringBuilder();
        boolean first = true;
        for (Object key : keys) {
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueString = "";
            if (null != value) {
                valueString = String.valueOf(value);
            }
            temp.append(valueString);
        }
        temp.append("&").append(SSOConstants.ACCESS_SECRET).append("=").append(accessSecret);
        return MD5.toMD5(temp.toString());
    }

    @Override
    public String toString() {
        return "Signature keyId=\"" + keyId + '\"' +
                ",shaAlgorithm=\"" + shaAlgorithm + '\"' +
                ",headers=\"" + JoinUtil.join(" ", headers) + '\"' +
                ",signature=\"" + signature + '\"';
    }
}
