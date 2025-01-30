

# EnrollmentDetail

An enrollment object. Enrollments are tied to identities and potentially a CA. Depending on the method, different fields are utilized. For example ottca enrollments use the `ca` field and updb enrollments use the username field, but not vice versa. 

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**links** | [**Map&lt;String, Link&gt;**](Link.md) | A map of named links |  |
|**createdAt** | **OffsetDateTime** |  |  |
|**id** | **String** |  |  |
|**tags** | [**Tags**](Tags.md) |  |  [optional] |
|**updatedAt** | **OffsetDateTime** |  |  |
|**caId** | **String** |  |  [optional] |
|**edgeRouter** | [**EntityRef**](EntityRef.md) |  |  [optional] |
|**edgeRouterId** | **String** |  |  [optional] |
|**expiresAt** | **OffsetDateTime** |  |  |
|**identity** | [**EntityRef**](EntityRef.md) |  |  [optional] |
|**identityId** | **String** |  |  [optional] |
|**jwt** | **String** |  |  [optional] |
|**method** | **String** |  |  |
|**token** | **String** |  |  |
|**transitRouter** | [**EntityRef**](EntityRef.md) |  |  [optional] |
|**transitRouterId** | **String** |  |  [optional] |
|**username** | **String** |  |  [optional] |



