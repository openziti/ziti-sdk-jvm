

# CurrentIdentityEdgeRouterDetail

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



