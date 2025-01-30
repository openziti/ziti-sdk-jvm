

# ServiceDetail


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**Map&lt;String, Link&gt;**](Link.md) | A map of named links |  |
|**createdAt** | **OffsetDateTime** |  |  |
|**id** | **String** |  |  |
|**tags** | [**Tags**](Tags.md) |  |  [optional] |
|**updatedAt** | **OffsetDateTime** |  |  |
|**config** | **Map&lt;String, Map&lt;String, Object&gt;&gt;** | map of config data for this service keyed by the config type name. Only configs of the types requested will be returned. |  |
|**configs** | **List&lt;String&gt;** |  |  |
|**encryptionRequired** | **Boolean** | Describes whether connections must support end-to-end encryption on both sides of the connection. Read-only property, set at create. |  |
|**maxIdleTimeMillis** | **Integer** |  |  |
|**name** | **String** |  |  |
|**permissions** | **List&lt;DialBind&gt;** |  |  |
|**postureQueries** | [**List&lt;PostureQueries&gt;**](PostureQueries.md) |  |  |
|**roleAttributes** | **List&lt;String&gt;** | A set of strings used to loosly couple this resource to policies |  |
|**terminatorStrategy** | **String** |  |  |



