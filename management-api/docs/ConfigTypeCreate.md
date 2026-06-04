

# ConfigTypeCreate

A config-type create object

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**name** | **String** |  |  |
|**schema** | **Map&lt;String, Object&gt;** | A JSON schema to enforce configuration against |  [optional] |
|**tags** | [**Tags**](Tags.md) |  |  [optional] |
|**target** | [**TargetEnum**](#TargetEnum) | Indicates the target of this config type, e.g. \&quot;service\&quot;, \&quot;router\&quot; or \&quot;other\&quot;. If not provided, defaults to \&quot;service\&quot;.  |  [optional] |



## Enum: TargetEnum

| Name | Value |
|---- | -----|
| SERVICE | &quot;service&quot; |
| ROUTER | &quot;router&quot; |
| OTHER | &quot;other&quot; |



