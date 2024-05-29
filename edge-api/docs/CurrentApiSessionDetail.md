

# CurrentApiSessionDetail

An API Session object for the current API session

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**Map&lt;String, Link&gt;**](Link.md) | A map of named links |  |
|**createdAt** | **OffsetDateTime** |  |  |
|**id** | **String** |  |  |
|**tags** | [**Tags**](Tags.md) |  |  [optional] |
|**updatedAt** | **OffsetDateTime** |  |  |
|**authQueries** | [**List&lt;AuthQueryDetail&gt;**](AuthQueryDetail.md) |  |  |
|**authenticatorId** | **String** |  |  |
|**cachedLastActivityAt** | **OffsetDateTime** |  |  [optional] |
|**configTypes** | **List&lt;String&gt;** |  |  |
|**identity** | [**EntityRef**](EntityRef.md) |  |  |
|**identityId** | **String** |  |  |
|**ipAddress** | **String** |  |  |
|**isMfaComplete** | **Boolean** |  |  |
|**isMfaRequired** | **Boolean** |  |  |
|**lastActivityAt** | **OffsetDateTime** |  |  [optional] |
|**token** | **String** |  |  |
|**expirationSeconds** | **Integer** |  |  |
|**expiresAt** | **OffsetDateTime** |  |  |



