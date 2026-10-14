

# Version


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**apiVersions** | **Map&lt;String, Map&lt;String, ApiVersion&gt;&gt;** |  |  [optional] |
|**buildDate** | **String** |  |  [optional] |
|**buildFlags** | **List&lt;String&gt;** | Flags supplied when this controller binary was built, naming build time choices a client may need to know about. Distinct from capabilities, which describe features the controller offers over the API. Open-ended: the values are defined by whoever produced the build, and clients ignore values they do not recognize. Absent when the controller predates this field or was built without flags.  |  [optional] |
|**capabilities** | **List&lt;String&gt;** |  |  [optional] |
|**revision** | **String** |  |  [optional] |
|**runtimeVersion** | **String** |  |  [optional] |
|**version** | **String** |  |  [optional] |



