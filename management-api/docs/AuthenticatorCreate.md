

# AuthenticatorCreate

Creates an authenticator for a specific identity which can be used for API authentication

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**certPem** | **String** | The client certificate the identity will login with. Used only for method&#x3D;&#39;cert&#39; |  [optional] |
|**identityId** | **String** | The id of an existing identity that will be assigned this authenticator |  |
|**method** | **String** | The type of authenticator to create; which will dictate which properties on this object are required. |  |
|**password** | **String** | The password the identity will login with. Used only for method&#x3D;&#39;updb&#39; |  [optional] |
|**tags** | [**Tags**](Tags.md) |  |  [optional] |
|**username** | **String** | The username that the identity will login with. Used only for method&#x3D;&#39;updb&#39; |  [optional] |



