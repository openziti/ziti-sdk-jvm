

# IdentityDetail

Detail of a specific identity

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**Map&lt;String, Link&gt;**](Link.md) | A map of named links |  |
|**createdAt** | **OffsetDateTime** |  |  |
|**id** | **String** |  |  |
|**tags** | [**Tags**](Tags.md) |  |  [optional] |
|**updatedAt** | **OffsetDateTime** |  |  |
|**appData** | [**Tags**](Tags.md) |  |  [optional] |
|**authPolicy** | [**EntityRef**](EntityRef.md) |  |  |
|**authPolicyId** | **String** |  |  |
|**authenticators** | [**IdentityAuthenticators**](IdentityAuthenticators.md) |  |  |
|**defaultHostingCost** | **Integer** |  |  |
|**defaultHostingPrecedence** | **TerminatorPrecedence** |  |  [optional] |
|**disabled** | **Boolean** |  |  |
|**disabledAt** | **OffsetDateTime** |  |  [optional] |
|**disabledUntil** | **OffsetDateTime** |  |  [optional] |
|**edgeRouterConnectionStatus** | [**EdgeRouterConnectionStatusEnum**](#EdgeRouterConnectionStatusEnum) |  |  |
|**enrollment** | [**IdentityEnrollments**](IdentityEnrollments.md) |  |  |
|**envInfo** | [**EnvInfo**](EnvInfo.md) |  |  |
|**externalId** | **String** |  |  |
|**hasApiSession** | **Boolean** |  |  |
|**hasEdgeRouterConnection** | **Boolean** |  |  |
|**interfaces** | [**List&lt;ModelInterface&gt;**](ModelInterface.md) |  |  [optional] |
|**isAdmin** | **Boolean** |  |  |
|**isDefaultAdmin** | **Boolean** |  |  |
|**isMfaEnabled** | **Boolean** |  |  |
|**name** | **String** |  |  |
|**roleAttributes** | **List&lt;String&gt;** | A set of strings used to loosly couple this resource to policies |  |
|**sdkInfo** | [**SdkInfo**](SdkInfo.md) |  |  |
|**serviceHostingCosts** | **Map&lt;String, Integer&gt;** |  |  |
|**serviceHostingPrecedences** | **Map&lt;String, TerminatorPrecedence&gt;** |  |  |
|**type** | [**EntityRef**](EntityRef.md) |  |  |
|**typeId** | **String** |  |  |



## Enum: EdgeRouterConnectionStatusEnum

| Name | Value |
|---- | -----|
| ONLINE | &quot;online&quot; |
| OFFLINE | &quot;offline&quot; |
| UNKNOWN | &quot;unknown&quot; |



