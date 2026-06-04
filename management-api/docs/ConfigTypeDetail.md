

# ConfigTypeDetail

A config-type resource

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**Map&lt;String, Link&gt;**](Link.md) | A map of named links |  |
|**createdAt** | **OffsetDateTime** |  |  |
|**id** | **String** |  |  |
|**tags** | [**Tags**](Tags.md) |  |  [optional] |
|**updatedAt** | **OffsetDateTime** |  |  |
|**name** | **String** |  |  |
|**schema** | **Map&lt;String, Object&gt;** | A JSON schema to enforce configuration against |  |
|**target** | [**TargetEnum**](#TargetEnum) | Indicates the target of this config type, e.g. \&quot;service\&quot;, \&quot;router\&quot; or \&quot;other\&quot; |  [optional] |



## Enum: TargetEnum

| Name | Value |
|---- | -----|
| SERVICE | &quot;service&quot; |
| ROUTER | &quot;router&quot; |
| OTHER | &quot;other&quot; |



