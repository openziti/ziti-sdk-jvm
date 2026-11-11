# The edge-api jar, which ziti depends on, still contains the generated API client. It is built on
# java.net.http, which Android does not have. The SDK never loads those classes (it uses its own
# OkHttp client with the generated models), so R8 can ignore the missing references.
-dontwarn java.net.http.**
