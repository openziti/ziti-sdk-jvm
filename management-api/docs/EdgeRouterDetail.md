

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
|**capabilities** | **List&lt;String&gt;** | Capabilities this edge router advertises, used by SDK clients to select a router that supports a given feature before connecting. Open-ended: clients ignore values they do not recognize. Absent when the controller predates this field.  |  [optional] |
|**cost** | **Integer** |  |  |
|**disabled** | **Boolean** |  |  |
|**hostname** | **String** |  |  |
|**isOnline** | **Boolean** |  |  |
|**name** | **String** |  |  |
|**noTraversal** | **Boolean** |  |  |
|**supportedProtocols** | **Map&lt;String, String&gt;** |  |  |
|**syncStatus** | **String** |  |  |
|**version** | **String** | The version of the edge router binary, as reported to the controller when the router connects. Empty when the controller has not yet received a version from the router or predates this field.  |  [optional] |
|**capabilitiesMask** | **Long** | The raw router capabilities bitmask as reported to the controller, exposed for diagnostics. The capabilities array is the decoded, human-readable form of the bits this controller recognizes.  |  [optional] |
|**certPem** | **String** |  |  [optional] |
|**configs** | **List&lt;String&gt;** | Config IDs associated with this edge router |  [optional] |
|**ctrlChanListeners** | **Map&lt;String, List&lt;String&gt;&gt;** |  |  [optional] |
|**enrollmentCreatedAt** | **OffsetDateTime** |  |  [optional] |
|**enrollmentExpiresAt** | **OffsetDateTime** |  |  [optional] |
|**enrollmentJwt** | **String** |  |  [optional] |
|**enrollmentToken** | **String** |  |  [optional] |
|**fingerprint** | **String** |  |  [optional] |
|**interfaces** | [**List&lt;ModelInterface&gt;**](ModelInterface.md) |  |  [optional] |
|**isTunnelerEnabled** | **Boolean** |  |  |
|**isVerified** | **Boolean** |  |  |
|**roleAttributes** | **List&lt;String&gt;** | A set of strings used to loosely couple this resource to policies |  |
|**unverifiedCertPem** | **String** |  |  [optional] |
|**unverifiedFingerprint** | **String** |  |  [optional] |
|**versionInfo** | [**VersionInfo**](VersionInfo.md) |  |  [optional] |



