# The edge-api jar, which ziti depends on, still contains the generated API client. It is built on
# java.net.http, which Android does not have. The SDK never loads those classes (it uses its own
# OkHttp client with the generated models), so R8 can ignore the missing references.
-dontwarn java.net.http.**

# jjwt loads its implementation classes by name.
-keep class io.jsonwebtoken.** { *; }
-keepnames class io.jsonwebtoken.* { *; }
-keepnames interface io.jsonwebtoken.* { *; }

# The SDK's own classes and the edge models are bound to JSON by reflection (Jackson, Gson, Kotlin
# reflection), which needs their members, annotations, generic signatures and Kotlin metadata.
-keep class org.openziti.** { *; }
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keep class kotlin.Metadata { *; }

# jackson-databind-nullable has optional Bean Validation and Jackson 3 integrations.
-dontwarn javax.validation.**
-dontwarn jakarta.validation.**
-dontwarn tools.jackson.**
