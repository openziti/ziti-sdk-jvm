

# AuthenticatorDetail

A singular authenticator resource

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**Map&lt;String, Link&gt;**](Link.md) | A map of named links |  |
|**createdAt** | **OffsetDateTime** |  |  |
|**id** | **String** |  |  |
|**tags** | [**Tags**](Tags.md) |  |  [optional] |
|**updatedAt** | **OffsetDateTime** |  |  |
|**certPem** | **String** |  |  [optional] |
|**extendRequestedAt** | **OffsetDateTime** |  |  [optional] |
|**fingerprint** | **String** |  |  [optional] |
|**identity** | [**EntityRef**](EntityRef.md) |  |  |
|**identityId** | **String** |  |  |
|**isExtendRequested** | **Boolean** |  |  [optional] |
|**isIssuedByNetwork** | **Boolean** |  |  [optional] |
|**isKeyRollRequested** | **Boolean** |  |  [optional] |
|**lastAuthResolvedToRoot** | **Boolean** |  |  [optional] |
|**lastExtendRolledKeys** | **Boolean** |  |  [optional] |
|**method** | **String** |  |  |
|**username** | **String** |  |  [optional] |



