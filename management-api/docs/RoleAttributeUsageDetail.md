

# RoleAttributeUsageDetail


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**roleAttribute** | **String** | The role attribute value (without any leading &#39;#&#39;). |  |
|**usage** | [**Map&lt;String, RoleAttributeSourceUsage&gt;**](RoleAttributeSourceUsage.md) | Per-source usage breakdown keyed by source name. Source names include the defining-entity collection (\&quot;identities\&quot;, \&quot;edgeRouters\&quot;, \&quot;services\&quot;, \&quot;postureChecks\&quot;) and the policy collections that may reference the attribute (\&quot;servicePolicies\&quot;, \&quot;edgeRouterPolicies\&quot;, \&quot;serviceEdgeRouterPolicies\&quot;). Sources that don&#39;t apply to the endpoint are omitted; sources that apply but have no references are still emitted with a zero count for schema symmetry.  |  |



