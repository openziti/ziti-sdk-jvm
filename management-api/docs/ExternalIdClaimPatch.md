

# ExternalIdClaimPatch


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**index** | **Integer** |  |  [optional] |
|**location** | [**LocationEnum**](#LocationEnum) |  |  [optional] |
|**matcher** | [**MatcherEnum**](#MatcherEnum) |  |  [optional] |
|**matcherCriteria** | **String** |  |  [optional] |
|**parser** | [**ParserEnum**](#ParserEnum) |  |  [optional] |
|**parserCriteria** | **String** |  |  [optional] |



## Enum: LocationEnum

| Name | Value |
|---- | -----|
| COMMON_NAME | &quot;COMMON_NAME&quot; |
| SAN_URI | &quot;SAN_URI&quot; |
| SAN_EMAIL | &quot;SAN_EMAIL&quot; |



## Enum: MatcherEnum

| Name | Value |
|---- | -----|
| ALL | &quot;ALL&quot; |
| PREFIX | &quot;PREFIX&quot; |
| SUFFIX | &quot;SUFFIX&quot; |
| SCHEME | &quot;SCHEME&quot; |



## Enum: ParserEnum

| Name | Value |
|---- | -----|
| NONE | &quot;NONE&quot; |
| SPLIT | &quot;SPLIT&quot; |



