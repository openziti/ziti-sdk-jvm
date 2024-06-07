

# Jwk


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**alg** | **String** | Algorithm intended for use with the key. |  [optional] |
|**crv** | **String** | Curve for ECC Public Keys. |  [optional] |
|**d** | **String** | ECC Private Key or RSA Private Exponent. |  [optional] |
|**dp** | **String** | First Factor CRT Exponent for RSA. |  [optional] |
|**dq** | **String** | Second Factor CRT Exponent for RSA. |  [optional] |
|**e** | **String** | Exponent for RSA Public Key. |  [optional] |
|**keyOps** | **List&lt;String&gt;** | Intended key operations, e.g., sign, verify. |  [optional] |
|**kid** | **String** | Key ID. |  [optional] |
|**kty** | **String** | Key Type. |  |
|**n** | **String** | Modulus for RSA Public Key. |  [optional] |
|**oth** | [**List&lt;OtherPrime&gt;**](OtherPrime.md) | Other Primes Info not represented by the first two primes. |  [optional] |
|**p** | **String** | First Prime Factor for RSA. |  [optional] |
|**q** | **String** | Second Prime Factor for RSA. |  [optional] |
|**qi** | **String** | First CRT Coefficient for RSA. |  [optional] |
|**use** | **String** | Public key use, e.g., sig (signature) or enc (encryption). |  [optional] |
|**x** | **String** | X Coordinate for ECC Public Keys. |  [optional] |
|**x5c** | **List&lt;String&gt;** | X.509 Certificate Chain. |  [optional] |
|**x5t** | **String** | X.509 Certificate SHA-1 Thumbprint. |  [optional] |
|**x5tHashS256** | **String** | X.509 Certificate SHA-256 Thumbprint. |  [optional] |
|**x5u** | **String** | X.509 URL. |  [optional] |
|**y** | **String** | Y Coordinate for ECC Public Keys. |  [optional] |



