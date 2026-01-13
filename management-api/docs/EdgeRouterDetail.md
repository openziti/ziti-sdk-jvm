

# EdgeRouterDetail

A detail edge router resource

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**Map&lt;String, Link&gt;**](Link.md) | A map of named links |  |
|**createdAt** | **OffsetDateTime** |  |  |
|**id** | **String** |  |  |
|**tags** | [**Tags**](Tags.md) |  |  [optional] |
|**updatedAt** | **OffsetDateTime** |  |  |
|**appData** | [**Tags**](Tags.md) |  |  [optional] |
|**cost** | **Integer** |  |  |
|**disabled** | **Boolean** |  |  |
|**hostname** | **String** |  |  |
|**isOnline** | **Boolean** |  |  |
|**name** | **String** |  |  |
|**noTraversal** | **Boolean** |  |  |
|**supportedProtocols** | **Map&lt;String, String&gt;** |  |  |
|**syncStatus** | **String** |  |  |
|**certPem** | **String** |  |  [optional] |
|**enrollmentCreatedAt** | **OffsetDateTime** |  |  [optional] |
|**enrollmentExpiresAt** | **OffsetDateTime** |  |  [optional] |
|**enrollmentJwt** | **String** |  |  [optional] |
|**enrollmentToken** | **String** |  |  [optional] |
|**fingerprint** | **String** |  |  [optional] |
|**interfaces** | [**List&lt;ModelInterface&gt;**](ModelInterface.md) |  |  [optional] |
|**isTunnelerEnabled** | **Boolean** |  |  |
|**isVerified** | **Boolean** |  |  |
|**roleAttributes** | **List&lt;String&gt;** | A set of strings used to loosly couple this resource to policies |  |
|**unverifiedCertPem** | **String** |  |  [optional] |
|**unverifiedFingerprint** | **String** |  |  [optional] |
|**versionInfo** | [**VersionInfo**](VersionInfo.md) |  |  [optional] |



