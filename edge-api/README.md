# edge-api

Ziti Edge Client

- API version: 0.26.20

- Build date: 2024-06-07T09:12:49.062759-04:00[America/New_York]

- Generator version: 7.6.0

OpenZiti Edge Client API

  For more information, please visit [https://openziti.discourse.group](https://openziti.discourse.group)

*Automatically generated by the [OpenAPI Generator](https://openapi-generator.tech)*

## Requirements

Building the API client library requires:

1. Java 11+
2. Maven/Gradle

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn clean install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn clean deploy
```

Refer to the [OSSRH Guide](http://central.sonatype.org/pages/ossrh-guide.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
  <groupId>org.openziti</groupId>
  <artifactId>edge-api</artifactId>
  <version>0.26.20</version>
  <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "org.openziti:edge-api:0.26.20"
```

### Others

At first generate the JAR by executing:

```shell
mvn clean package
```

Then manually install the following JARs:

- `target/edge-api-0.26.20.jar`
- `target/lib/*.jar`

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import org.openziti.edge.*;
import org.openziti.edge.model.*;
import org.openziti.edge.api.AuthenticationApi;
import java.util.concurrent.CompletableFuture;

public class AuthenticationApiExample {

    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        // Configure clients using the `defaultClient` object, such as
        // overriding the host and port, timeout, etc.
        AuthenticationApi apiInstance = new AuthenticationApi(defaultClient);
        String method = "password"; // String | 
        Authenticate auth = new Authenticate(); // Authenticate | 
        try {
            CompletableFuture<CurrentApiSessionDetailEnvelope> result = apiInstance.authenticate(method, auth);
            System.out.println(result.get());
        } catch (ApiException e) {
            System.err.println("Exception when calling AuthenticationApi#authenticate");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *https://demo.ziti.dev/edge/client/v1*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*AuthenticationApi* | [**authenticate**](docs/AuthenticationApi.md#authenticate) | **POST** /authenticate | Authenticate via a method supplied via a query string parameter
*AuthenticationApi* | [**authenticateWithHttpInfo**](docs/AuthenticationApi.md#authenticateWithHttpInfo) | **POST** /authenticate | Authenticate via a method supplied via a query string parameter
*AuthenticationApi* | [**authenticateMfa**](docs/AuthenticationApi.md#authenticateMfa) | **POST** /authenticate/mfa | Complete MFA authentication
*AuthenticationApi* | [**authenticateMfaWithHttpInfo**](docs/AuthenticationApi.md#authenticateMfaWithHttpInfo) | **POST** /authenticate/mfa | Complete MFA authentication
*ControllersApi* | [**listControllers**](docs/ControllersApi.md#listControllers) | **GET** /controllers | List controllers
*ControllersApi* | [**listControllersWithHttpInfo**](docs/ControllersApi.md#listControllersWithHttpInfo) | **GET** /controllers | List controllers
*CurrentApiSessionApi* | [**createCurrentApiSessionCertificate**](docs/CurrentApiSessionApi.md#createCurrentApiSessionCertificate) | **POST** /current-api-session/certificates | Creates an ephemeral certificate for the current API Session
*CurrentApiSessionApi* | [**createCurrentApiSessionCertificateWithHttpInfo**](docs/CurrentApiSessionApi.md#createCurrentApiSessionCertificateWithHttpInfo) | **POST** /current-api-session/certificates | Creates an ephemeral certificate for the current API Session
*CurrentApiSessionApi* | [**currentApiSessionDelete**](docs/CurrentApiSessionApi.md#currentApiSessionDelete) | **DELETE** /current-api-session | Logout
*CurrentApiSessionApi* | [**currentApiSessionDeleteWithHttpInfo**](docs/CurrentApiSessionApi.md#currentApiSessionDeleteWithHttpInfo) | **DELETE** /current-api-session | Logout
*CurrentApiSessionApi* | [**deleteCurrentApiSessionCertificate**](docs/CurrentApiSessionApi.md#deleteCurrentApiSessionCertificate) | **DELETE** /current-api-session/certificates/{id} | Delete an ephemeral certificate
*CurrentApiSessionApi* | [**deleteCurrentApiSessionCertificateWithHttpInfo**](docs/CurrentApiSessionApi.md#deleteCurrentApiSessionCertificateWithHttpInfo) | **DELETE** /current-api-session/certificates/{id} | Delete an ephemeral certificate
*CurrentApiSessionApi* | [**detailCurrentApiSessionCertificate**](docs/CurrentApiSessionApi.md#detailCurrentApiSessionCertificate) | **GET** /current-api-session/certificates/{id} | Retrieves an ephemeral certificate
*CurrentApiSessionApi* | [**detailCurrentApiSessionCertificateWithHttpInfo**](docs/CurrentApiSessionApi.md#detailCurrentApiSessionCertificateWithHttpInfo) | **GET** /current-api-session/certificates/{id} | Retrieves an ephemeral certificate
*CurrentApiSessionApi* | [**detailCurrentIdentityAuthenticator**](docs/CurrentApiSessionApi.md#detailCurrentIdentityAuthenticator) | **GET** /current-identity/authenticators/{id} | Retrieve an authenticator for the current identity
*CurrentApiSessionApi* | [**detailCurrentIdentityAuthenticatorWithHttpInfo**](docs/CurrentApiSessionApi.md#detailCurrentIdentityAuthenticatorWithHttpInfo) | **GET** /current-identity/authenticators/{id} | Retrieve an authenticator for the current identity
*CurrentApiSessionApi* | [**extendCurrentIdentityAuthenticator**](docs/CurrentApiSessionApi.md#extendCurrentIdentityAuthenticator) | **POST** /current-identity/authenticators/{id}/extend | Allows the current identity to recieve a new certificate associated with a certificate based authenticator
*CurrentApiSessionApi* | [**extendCurrentIdentityAuthenticatorWithHttpInfo**](docs/CurrentApiSessionApi.md#extendCurrentIdentityAuthenticatorWithHttpInfo) | **POST** /current-identity/authenticators/{id}/extend | Allows the current identity to recieve a new certificate associated with a certificate based authenticator
*CurrentApiSessionApi* | [**extendVerifyCurrentIdentityAuthenticator**](docs/CurrentApiSessionApi.md#extendVerifyCurrentIdentityAuthenticator) | **POST** /current-identity/authenticators/{id}/extend-verify | Allows the current identity to validate reciept of a new client certificate
*CurrentApiSessionApi* | [**extendVerifyCurrentIdentityAuthenticatorWithHttpInfo**](docs/CurrentApiSessionApi.md#extendVerifyCurrentIdentityAuthenticatorWithHttpInfo) | **POST** /current-identity/authenticators/{id}/extend-verify | Allows the current identity to validate reciept of a new client certificate
*CurrentApiSessionApi* | [**getCurrentAPISession**](docs/CurrentApiSessionApi.md#getCurrentAPISession) | **GET** /current-api-session | Return the current API session
*CurrentApiSessionApi* | [**getCurrentAPISessionWithHttpInfo**](docs/CurrentApiSessionApi.md#getCurrentAPISessionWithHttpInfo) | **GET** /current-api-session | Return the current API session
*CurrentApiSessionApi* | [**listCurrentApiSessionCertificates**](docs/CurrentApiSessionApi.md#listCurrentApiSessionCertificates) | **GET** /current-api-session/certificates | List the ephemeral certificates available for the current API Session
*CurrentApiSessionApi* | [**listCurrentApiSessionCertificatesWithHttpInfo**](docs/CurrentApiSessionApi.md#listCurrentApiSessionCertificatesWithHttpInfo) | **GET** /current-api-session/certificates | List the ephemeral certificates available for the current API Session
*CurrentApiSessionApi* | [**listCurrentIdentityAuthenticators**](docs/CurrentApiSessionApi.md#listCurrentIdentityAuthenticators) | **GET** /current-identity/authenticators | List authenticators for the current identity
*CurrentApiSessionApi* | [**listCurrentIdentityAuthenticatorsWithHttpInfo**](docs/CurrentApiSessionApi.md#listCurrentIdentityAuthenticatorsWithHttpInfo) | **GET** /current-identity/authenticators | List authenticators for the current identity
*CurrentApiSessionApi* | [**listServiceUpdates**](docs/CurrentApiSessionApi.md#listServiceUpdates) | **GET** /current-api-session/service-updates | Returns data indicating whether a client should updates it service list
*CurrentApiSessionApi* | [**listServiceUpdatesWithHttpInfo**](docs/CurrentApiSessionApi.md#listServiceUpdatesWithHttpInfo) | **GET** /current-api-session/service-updates | Returns data indicating whether a client should updates it service list
*CurrentApiSessionApi* | [**patchCurrentIdentityAuthenticator**](docs/CurrentApiSessionApi.md#patchCurrentIdentityAuthenticator) | **PATCH** /current-identity/authenticators/{id} | Update the supplied fields on an authenticator of this identity
*CurrentApiSessionApi* | [**patchCurrentIdentityAuthenticatorWithHttpInfo**](docs/CurrentApiSessionApi.md#patchCurrentIdentityAuthenticatorWithHttpInfo) | **PATCH** /current-identity/authenticators/{id} | Update the supplied fields on an authenticator of this identity
*CurrentApiSessionApi* | [**updateCurrentIdentityAuthenticator**](docs/CurrentApiSessionApi.md#updateCurrentIdentityAuthenticator) | **PUT** /current-identity/authenticators/{id} | Update all fields on an authenticator of this identity
*CurrentApiSessionApi* | [**updateCurrentIdentityAuthenticatorWithHttpInfo**](docs/CurrentApiSessionApi.md#updateCurrentIdentityAuthenticatorWithHttpInfo) | **PUT** /current-identity/authenticators/{id} | Update all fields on an authenticator of this identity
*CurrentIdentityApi* | [**createMfaRecoveryCodes**](docs/CurrentIdentityApi.md#createMfaRecoveryCodes) | **POST** /current-identity/mfa/recovery-codes | For a completed MFA enrollment regenerate the recovery codes
*CurrentIdentityApi* | [**createMfaRecoveryCodesWithHttpInfo**](docs/CurrentIdentityApi.md#createMfaRecoveryCodesWithHttpInfo) | **POST** /current-identity/mfa/recovery-codes | For a completed MFA enrollment regenerate the recovery codes
*CurrentIdentityApi* | [**deleteMfa**](docs/CurrentIdentityApi.md#deleteMfa) | **DELETE** /current-identity/mfa | Disable MFA for the current identity
*CurrentIdentityApi* | [**deleteMfaWithHttpInfo**](docs/CurrentIdentityApi.md#deleteMfaWithHttpInfo) | **DELETE** /current-identity/mfa | Disable MFA for the current identity
*CurrentIdentityApi* | [**detailMfa**](docs/CurrentIdentityApi.md#detailMfa) | **GET** /current-identity/mfa | Returns the current status of MFA enrollment
*CurrentIdentityApi* | [**detailMfaWithHttpInfo**](docs/CurrentIdentityApi.md#detailMfaWithHttpInfo) | **GET** /current-identity/mfa | Returns the current status of MFA enrollment
*CurrentIdentityApi* | [**detailMfaQrCode**](docs/CurrentIdentityApi.md#detailMfaQrCode) | **GET** /current-identity/mfa/qr-code | Show a QR code for unverified MFA enrollments
*CurrentIdentityApi* | [**detailMfaQrCodeWithHttpInfo**](docs/CurrentIdentityApi.md#detailMfaQrCodeWithHttpInfo) | **GET** /current-identity/mfa/qr-code | Show a QR code for unverified MFA enrollments
*CurrentIdentityApi* | [**detailMfaRecoveryCodes**](docs/CurrentIdentityApi.md#detailMfaRecoveryCodes) | **GET** /current-identity/mfa/recovery-codes | For a completed MFA enrollment view the current recovery codes
*CurrentIdentityApi* | [**detailMfaRecoveryCodesWithHttpInfo**](docs/CurrentIdentityApi.md#detailMfaRecoveryCodesWithHttpInfo) | **GET** /current-identity/mfa/recovery-codes | For a completed MFA enrollment view the current recovery codes
*CurrentIdentityApi* | [**enrollMfa**](docs/CurrentIdentityApi.md#enrollMfa) | **POST** /current-identity/mfa | Initiate MFA enrollment
*CurrentIdentityApi* | [**enrollMfaWithHttpInfo**](docs/CurrentIdentityApi.md#enrollMfaWithHttpInfo) | **POST** /current-identity/mfa | Initiate MFA enrollment
*CurrentIdentityApi* | [**getCurrentIdentity**](docs/CurrentIdentityApi.md#getCurrentIdentity) | **GET** /current-identity | Return the current identity
*CurrentIdentityApi* | [**getCurrentIdentityWithHttpInfo**](docs/CurrentIdentityApi.md#getCurrentIdentityWithHttpInfo) | **GET** /current-identity | Return the current identity
*CurrentIdentityApi* | [**getCurrentIdentityEdgeRouters**](docs/CurrentIdentityApi.md#getCurrentIdentityEdgeRouters) | **GET** /current-identity/edge-routers | Return this list of Edge Routers the identity has access to
*CurrentIdentityApi* | [**getCurrentIdentityEdgeRoutersWithHttpInfo**](docs/CurrentIdentityApi.md#getCurrentIdentityEdgeRoutersWithHttpInfo) | **GET** /current-identity/edge-routers | Return this list of Edge Routers the identity has access to
*CurrentIdentityApi* | [**verifyMfa**](docs/CurrentIdentityApi.md#verifyMfa) | **POST** /current-identity/mfa/verify | Complete MFA enrollment by verifying a time based one time token
*CurrentIdentityApi* | [**verifyMfaWithHttpInfo**](docs/CurrentIdentityApi.md#verifyMfaWithHttpInfo) | **POST** /current-identity/mfa/verify | Complete MFA enrollment by verifying a time based one time token
*EdgeRouterApi* | [**getCurrentIdentityEdgeRouters**](docs/EdgeRouterApi.md#getCurrentIdentityEdgeRouters) | **GET** /current-identity/edge-routers | Return this list of Edge Routers the identity has access to
*EdgeRouterApi* | [**getCurrentIdentityEdgeRoutersWithHttpInfo**](docs/EdgeRouterApi.md#getCurrentIdentityEdgeRoutersWithHttpInfo) | **GET** /current-identity/edge-routers | Return this list of Edge Routers the identity has access to
*EnrollApi* | [**enroll**](docs/EnrollApi.md#enroll) | **POST** /enroll | Enroll an identity via one-time-token
*EnrollApi* | [**enrollWithHttpInfo**](docs/EnrollApi.md#enrollWithHttpInfo) | **POST** /enroll | Enroll an identity via one-time-token
*EnrollApi* | [**enrollCa**](docs/EnrollApi.md#enrollCa) | **POST** /enroll/ca | Enroll an identity with a pre-exchanged certificate
*EnrollApi* | [**enrollCaWithHttpInfo**](docs/EnrollApi.md#enrollCaWithHttpInfo) | **POST** /enroll/ca | Enroll an identity with a pre-exchanged certificate
*EnrollApi* | [**enrollErOtt**](docs/EnrollApi.md#enrollErOtt) | **POST** /enroll/erott | Enroll an edge-router
*EnrollApi* | [**enrollErOttWithHttpInfo**](docs/EnrollApi.md#enrollErOttWithHttpInfo) | **POST** /enroll/erott | Enroll an edge-router
*EnrollApi* | [**enrollOtt**](docs/EnrollApi.md#enrollOtt) | **POST** /enroll/ott | Enroll an identity via one-time-token
*EnrollApi* | [**enrollOttWithHttpInfo**](docs/EnrollApi.md#enrollOttWithHttpInfo) | **POST** /enroll/ott | Enroll an identity via one-time-token
*EnrollApi* | [**enrollOttCa**](docs/EnrollApi.md#enrollOttCa) | **POST** /enroll/ottca | Enroll an identity via one-time-token with a pre-exchanged client certificate
*EnrollApi* | [**enrollOttCaWithHttpInfo**](docs/EnrollApi.md#enrollOttCaWithHttpInfo) | **POST** /enroll/ottca | Enroll an identity via one-time-token with a pre-exchanged client certificate
*EnrollApi* | [**enrollUpdb**](docs/EnrollApi.md#enrollUpdb) | **POST** /enroll/updb | Enroll an identity via one-time-token
*EnrollApi* | [**enrollUpdbWithHttpInfo**](docs/EnrollApi.md#enrollUpdbWithHttpInfo) | **POST** /enroll/updb | Enroll an identity via one-time-token
*EnrollApi* | [**enrollmentChallenge**](docs/EnrollApi.md#enrollmentChallenge) | **POST** /enroll/challenge | Allows verification of a controller or cluster of controllers as being the valid target for enrollment.
*EnrollApi* | [**enrollmentChallengeWithHttpInfo**](docs/EnrollApi.md#enrollmentChallengeWithHttpInfo) | **POST** /enroll/challenge | Allows verification of a controller or cluster of controllers as being the valid target for enrollment.
*EnrollApi* | [**extendCurrentIdentityAuthenticator**](docs/EnrollApi.md#extendCurrentIdentityAuthenticator) | **POST** /current-identity/authenticators/{id}/extend | Allows the current identity to recieve a new certificate associated with a certificate based authenticator
*EnrollApi* | [**extendCurrentIdentityAuthenticatorWithHttpInfo**](docs/EnrollApi.md#extendCurrentIdentityAuthenticatorWithHttpInfo) | **POST** /current-identity/authenticators/{id}/extend | Allows the current identity to recieve a new certificate associated with a certificate based authenticator
*EnrollApi* | [**extendRouterEnrollment**](docs/EnrollApi.md#extendRouterEnrollment) | **POST** /enroll/extend/router | Extend the life of a currently enrolled router&#39;s certificates
*EnrollApi* | [**extendRouterEnrollmentWithHttpInfo**](docs/EnrollApi.md#extendRouterEnrollmentWithHttpInfo) | **POST** /enroll/extend/router | Extend the life of a currently enrolled router&#39;s certificates
*EnrollApi* | [**extendVerifyCurrentIdentityAuthenticator**](docs/EnrollApi.md#extendVerifyCurrentIdentityAuthenticator) | **POST** /current-identity/authenticators/{id}/extend-verify | Allows the current identity to validate reciept of a new client certificate
*EnrollApi* | [**extendVerifyCurrentIdentityAuthenticatorWithHttpInfo**](docs/EnrollApi.md#extendVerifyCurrentIdentityAuthenticatorWithHttpInfo) | **POST** /current-identity/authenticators/{id}/extend-verify | Allows the current identity to validate reciept of a new client certificate
*EnrollApi* | [**getEnrollmentJwks**](docs/EnrollApi.md#getEnrollmentJwks) | **GET** /enroll/jwks | List JSON Web Keys associated with enrollment
*EnrollApi* | [**getEnrollmentJwksWithHttpInfo**](docs/EnrollApi.md#getEnrollmentJwksWithHttpInfo) | **GET** /enroll/jwks | List JSON Web Keys associated with enrollment
*ExtendEnrollmentApi* | [**extendCurrentIdentityAuthenticator**](docs/ExtendEnrollmentApi.md#extendCurrentIdentityAuthenticator) | **POST** /current-identity/authenticators/{id}/extend | Allows the current identity to recieve a new certificate associated with a certificate based authenticator
*ExtendEnrollmentApi* | [**extendCurrentIdentityAuthenticatorWithHttpInfo**](docs/ExtendEnrollmentApi.md#extendCurrentIdentityAuthenticatorWithHttpInfo) | **POST** /current-identity/authenticators/{id}/extend | Allows the current identity to recieve a new certificate associated with a certificate based authenticator
*ExtendEnrollmentApi* | [**extendRouterEnrollment**](docs/ExtendEnrollmentApi.md#extendRouterEnrollment) | **POST** /enroll/extend/router | Extend the life of a currently enrolled router&#39;s certificates
*ExtendEnrollmentApi* | [**extendRouterEnrollmentWithHttpInfo**](docs/ExtendEnrollmentApi.md#extendRouterEnrollmentWithHttpInfo) | **POST** /enroll/extend/router | Extend the life of a currently enrolled router&#39;s certificates
*ExtendEnrollmentApi* | [**extendVerifyCurrentIdentityAuthenticator**](docs/ExtendEnrollmentApi.md#extendVerifyCurrentIdentityAuthenticator) | **POST** /current-identity/authenticators/{id}/extend-verify | Allows the current identity to validate reciept of a new client certificate
*ExtendEnrollmentApi* | [**extendVerifyCurrentIdentityAuthenticatorWithHttpInfo**](docs/ExtendEnrollmentApi.md#extendVerifyCurrentIdentityAuthenticatorWithHttpInfo) | **POST** /current-identity/authenticators/{id}/extend-verify | Allows the current identity to validate reciept of a new client certificate
*ExternalJwtSignerApi* | [**listExternalJwtSigners**](docs/ExternalJwtSignerApi.md#listExternalJwtSigners) | **GET** /external-jwt-signers | List Client Authentication External JWT
*ExternalJwtSignerApi* | [**listExternalJwtSignersWithHttpInfo**](docs/ExternalJwtSignerApi.md#listExternalJwtSignersWithHttpInfo) | **GET** /external-jwt-signers | List Client Authentication External JWT
*InformationalApi* | [**detailSpec**](docs/InformationalApi.md#detailSpec) | **GET** /specs/{id} | Return a single spec resource
*InformationalApi* | [**detailSpecWithHttpInfo**](docs/InformationalApi.md#detailSpecWithHttpInfo) | **GET** /specs/{id} | Return a single spec resource
*InformationalApi* | [**detailSpecBody**](docs/InformationalApi.md#detailSpecBody) | **GET** /specs/{id}/spec | Returns the spec&#39;s file
*InformationalApi* | [**detailSpecBodyWithHttpInfo**](docs/InformationalApi.md#detailSpecBodyWithHttpInfo) | **GET** /specs/{id}/spec | Returns the spec&#39;s file
*InformationalApi* | [**listEnumeratedCapabilities**](docs/InformationalApi.md#listEnumeratedCapabilities) | **GET** /enumerated-capabilities | Returns all capabilities this version of the controller is aware of, enabled or not.
*InformationalApi* | [**listEnumeratedCapabilitiesWithHttpInfo**](docs/InformationalApi.md#listEnumeratedCapabilitiesWithHttpInfo) | **GET** /enumerated-capabilities | Returns all capabilities this version of the controller is aware of, enabled or not.
*InformationalApi* | [**listProtocols**](docs/InformationalApi.md#listProtocols) | **GET** /protocols | Return a list of the listening Edge protocols
*InformationalApi* | [**listProtocolsWithHttpInfo**](docs/InformationalApi.md#listProtocolsWithHttpInfo) | **GET** /protocols | Return a list of the listening Edge protocols
*InformationalApi* | [**listRoot**](docs/InformationalApi.md#listRoot) | **GET** / | Returns version information
*InformationalApi* | [**listRootWithHttpInfo**](docs/InformationalApi.md#listRootWithHttpInfo) | **GET** / | Returns version information
*InformationalApi* | [**listSpecs**](docs/InformationalApi.md#listSpecs) | **GET** /specs | Returns a list of API specs
*InformationalApi* | [**listSpecsWithHttpInfo**](docs/InformationalApi.md#listSpecsWithHttpInfo) | **GET** /specs | Returns a list of API specs
*InformationalApi* | [**listVersion**](docs/InformationalApi.md#listVersion) | **GET** /version | Returns version information
*InformationalApi* | [**listVersionWithHttpInfo**](docs/InformationalApi.md#listVersionWithHttpInfo) | **GET** /version | Returns version information
*MfaApi* | [**authenticateMfa**](docs/MfaApi.md#authenticateMfa) | **POST** /authenticate/mfa | Complete MFA authentication
*MfaApi* | [**authenticateMfaWithHttpInfo**](docs/MfaApi.md#authenticateMfaWithHttpInfo) | **POST** /authenticate/mfa | Complete MFA authentication
*MfaApi* | [**createMfaRecoveryCodes**](docs/MfaApi.md#createMfaRecoveryCodes) | **POST** /current-identity/mfa/recovery-codes | For a completed MFA enrollment regenerate the recovery codes
*MfaApi* | [**createMfaRecoveryCodesWithHttpInfo**](docs/MfaApi.md#createMfaRecoveryCodesWithHttpInfo) | **POST** /current-identity/mfa/recovery-codes | For a completed MFA enrollment regenerate the recovery codes
*MfaApi* | [**deleteMfa**](docs/MfaApi.md#deleteMfa) | **DELETE** /current-identity/mfa | Disable MFA for the current identity
*MfaApi* | [**deleteMfaWithHttpInfo**](docs/MfaApi.md#deleteMfaWithHttpInfo) | **DELETE** /current-identity/mfa | Disable MFA for the current identity
*MfaApi* | [**detailMfa**](docs/MfaApi.md#detailMfa) | **GET** /current-identity/mfa | Returns the current status of MFA enrollment
*MfaApi* | [**detailMfaWithHttpInfo**](docs/MfaApi.md#detailMfaWithHttpInfo) | **GET** /current-identity/mfa | Returns the current status of MFA enrollment
*MfaApi* | [**detailMfaQrCode**](docs/MfaApi.md#detailMfaQrCode) | **GET** /current-identity/mfa/qr-code | Show a QR code for unverified MFA enrollments
*MfaApi* | [**detailMfaQrCodeWithHttpInfo**](docs/MfaApi.md#detailMfaQrCodeWithHttpInfo) | **GET** /current-identity/mfa/qr-code | Show a QR code for unverified MFA enrollments
*MfaApi* | [**detailMfaRecoveryCodes**](docs/MfaApi.md#detailMfaRecoveryCodes) | **GET** /current-identity/mfa/recovery-codes | For a completed MFA enrollment view the current recovery codes
*MfaApi* | [**detailMfaRecoveryCodesWithHttpInfo**](docs/MfaApi.md#detailMfaRecoveryCodesWithHttpInfo) | **GET** /current-identity/mfa/recovery-codes | For a completed MFA enrollment view the current recovery codes
*MfaApi* | [**enrollMfa**](docs/MfaApi.md#enrollMfa) | **POST** /current-identity/mfa | Initiate MFA enrollment
*MfaApi* | [**enrollMfaWithHttpInfo**](docs/MfaApi.md#enrollMfaWithHttpInfo) | **POST** /current-identity/mfa | Initiate MFA enrollment
*MfaApi* | [**verifyMfa**](docs/MfaApi.md#verifyMfa) | **POST** /current-identity/mfa/verify | Complete MFA enrollment by verifying a time based one time token
*MfaApi* | [**verifyMfaWithHttpInfo**](docs/MfaApi.md#verifyMfaWithHttpInfo) | **POST** /current-identity/mfa/verify | Complete MFA enrollment by verifying a time based one time token
*PostureChecksApi* | [**createPostureResponse**](docs/PostureChecksApi.md#createPostureResponse) | **POST** /posture-response | Submit a posture response to a posture query
*PostureChecksApi* | [**createPostureResponseWithHttpInfo**](docs/PostureChecksApi.md#createPostureResponseWithHttpInfo) | **POST** /posture-response | Submit a posture response to a posture query
*PostureChecksApi* | [**createPostureResponseBulk**](docs/PostureChecksApi.md#createPostureResponseBulk) | **POST** /posture-response-bulk | Submit multiple posture responses
*PostureChecksApi* | [**createPostureResponseBulkWithHttpInfo**](docs/PostureChecksApi.md#createPostureResponseBulkWithHttpInfo) | **POST** /posture-response-bulk | Submit multiple posture responses
*ServiceApi* | [**deleteService**](docs/ServiceApi.md#deleteService) | **DELETE** /services/{id} | Delete a service
*ServiceApi* | [**deleteServiceWithHttpInfo**](docs/ServiceApi.md#deleteServiceWithHttpInfo) | **DELETE** /services/{id} | Delete a service
*ServiceApi* | [**detailService**](docs/ServiceApi.md#detailService) | **GET** /services/{id} | Retrieves a single service
*ServiceApi* | [**detailServiceWithHttpInfo**](docs/ServiceApi.md#detailServiceWithHttpInfo) | **GET** /services/{id} | Retrieves a single service
*ServiceApi* | [**listServiceEdgeRouters**](docs/ServiceApi.md#listServiceEdgeRouters) | **GET** /services/{id}/edge-routers | List of edge routers permitted to handle traffic for the specified service
*ServiceApi* | [**listServiceEdgeRoutersWithHttpInfo**](docs/ServiceApi.md#listServiceEdgeRoutersWithHttpInfo) | **GET** /services/{id}/edge-routers | List of edge routers permitted to handle traffic for the specified service
*ServiceApi* | [**listServiceTerminators**](docs/ServiceApi.md#listServiceTerminators) | **GET** /services/{id}/terminators | List of terminators assigned to a service
*ServiceApi* | [**listServiceTerminatorsWithHttpInfo**](docs/ServiceApi.md#listServiceTerminatorsWithHttpInfo) | **GET** /services/{id}/terminators | List of terminators assigned to a service
*ServiceApi* | [**listServices**](docs/ServiceApi.md#listServices) | **GET** /services | List services
*ServiceApi* | [**listServicesWithHttpInfo**](docs/ServiceApi.md#listServicesWithHttpInfo) | **GET** /services | List services
*ServiceApi* | [**patchService**](docs/ServiceApi.md#patchService) | **PATCH** /services/{id} | Update the supplied fields on a service
*ServiceApi* | [**patchServiceWithHttpInfo**](docs/ServiceApi.md#patchServiceWithHttpInfo) | **PATCH** /services/{id} | Update the supplied fields on a service
*ServiceApi* | [**updateService**](docs/ServiceApi.md#updateService) | **PUT** /services/{id} | Update all fields on a service
*ServiceApi* | [**updateServiceWithHttpInfo**](docs/ServiceApi.md#updateServiceWithHttpInfo) | **PUT** /services/{id} | Update all fields on a service
*ServicesApi* | [**listServiceUpdates**](docs/ServicesApi.md#listServiceUpdates) | **GET** /current-api-session/service-updates | Returns data indicating whether a client should updates it service list
*ServicesApi* | [**listServiceUpdatesWithHttpInfo**](docs/ServicesApi.md#listServiceUpdatesWithHttpInfo) | **GET** /current-api-session/service-updates | Returns data indicating whether a client should updates it service list
*SessionApi* | [**createSession**](docs/SessionApi.md#createSession) | **POST** /sessions | Create a session resource
*SessionApi* | [**createSessionWithHttpInfo**](docs/SessionApi.md#createSessionWithHttpInfo) | **POST** /sessions | Create a session resource
*SessionApi* | [**deleteSession**](docs/SessionApi.md#deleteSession) | **DELETE** /sessions/{id} | Delete a session
*SessionApi* | [**deleteSessionWithHttpInfo**](docs/SessionApi.md#deleteSessionWithHttpInfo) | **DELETE** /sessions/{id} | Delete a session
*SessionApi* | [**detailSession**](docs/SessionApi.md#detailSession) | **GET** /sessions/{id} | Retrieves a single session
*SessionApi* | [**detailSessionWithHttpInfo**](docs/SessionApi.md#detailSessionWithHttpInfo) | **GET** /sessions/{id} | Retrieves a single session
*SessionApi* | [**listSessions**](docs/SessionApi.md#listSessions) | **GET** /sessions | List sessions
*SessionApi* | [**listSessionsWithHttpInfo**](docs/SessionApi.md#listSessionsWithHttpInfo) | **GET** /sessions | List sessions
*WellKnownApi* | [**listWellKnownCas**](docs/WellKnownApi.md#listWellKnownCas) | **GET** /.well-known/est/cacerts | Get CA Cert Store
*WellKnownApi* | [**listWellKnownCasWithHttpInfo**](docs/WellKnownApi.md#listWellKnownCasWithHttpInfo) | **GET** /.well-known/est/cacerts | Get CA Cert Store


## Documentation for Models

 - [ApiAddress](docs/ApiAddress.md)
 - [ApiError](docs/ApiError.md)
 - [ApiErrorArgs](docs/ApiErrorArgs.md)
 - [ApiErrorCause](docs/ApiErrorCause.md)
 - [ApiErrorEnvelope](docs/ApiErrorEnvelope.md)
 - [ApiFieldError](docs/ApiFieldError.md)
 - [ApiSessionDetail](docs/ApiSessionDetail.md)
 - [ApiVersion](docs/ApiVersion.md)
 - [AuthQueryDetail](docs/AuthQueryDetail.md)
 - [Authenticate](docs/Authenticate.md)
 - [AuthenticatorDetail](docs/AuthenticatorDetail.md)
 - [AuthenticatorPatch](docs/AuthenticatorPatch.md)
 - [AuthenticatorPatchWithCurrent](docs/AuthenticatorPatchWithCurrent.md)
 - [AuthenticatorUpdate](docs/AuthenticatorUpdate.md)
 - [AuthenticatorUpdateWithCurrent](docs/AuthenticatorUpdateWithCurrent.md)
 - [BaseEntity](docs/BaseEntity.md)
 - [Capabilities](docs/Capabilities.md)
 - [ClientExternalJwtSignerDetail](docs/ClientExternalJwtSignerDetail.md)
 - [CommonEdgeRouterProperties](docs/CommonEdgeRouterProperties.md)
 - [ControllerDetail](docs/ControllerDetail.md)
 - [CreateCurrentApiSessionCertificateEnvelope](docs/CreateCurrentApiSessionCertificateEnvelope.md)
 - [CreateEnvelope](docs/CreateEnvelope.md)
 - [CreateLocation](docs/CreateLocation.md)
 - [CurrentApiSessionCertificateCreate](docs/CurrentApiSessionCertificateCreate.md)
 - [CurrentApiSessionCertificateCreateResponse](docs/CurrentApiSessionCertificateCreateResponse.md)
 - [CurrentApiSessionCertificateDetail](docs/CurrentApiSessionCertificateDetail.md)
 - [CurrentApiSessionDetail](docs/CurrentApiSessionDetail.md)
 - [CurrentApiSessionDetailEnvelope](docs/CurrentApiSessionDetailEnvelope.md)
 - [CurrentApiSessionServiceUpdateList](docs/CurrentApiSessionServiceUpdateList.md)
 - [CurrentIdentityDetailEnvelope](docs/CurrentIdentityDetailEnvelope.md)
 - [CurrentIdentityEdgeRouterDetail](docs/CurrentIdentityEdgeRouterDetail.md)
 - [DetailAuthenticatorEnvelope](docs/DetailAuthenticatorEnvelope.md)
 - [DetailCurrentApiSessionCertificateEnvelope](docs/DetailCurrentApiSessionCertificateEnvelope.md)
 - [DetailMfa](docs/DetailMfa.md)
 - [DetailMfaEnvelope](docs/DetailMfaEnvelope.md)
 - [DetailMfaRecoveryCodes](docs/DetailMfaRecoveryCodes.md)
 - [DetailMfaRecoveryCodesEnvelope](docs/DetailMfaRecoveryCodesEnvelope.md)
 - [DetailServiceEnvelope](docs/DetailServiceEnvelope.md)
 - [DetailSessionEnvelope](docs/DetailSessionEnvelope.md)
 - [DetailSpecBodyEnvelope](docs/DetailSpecBodyEnvelope.md)
 - [DetailSpecEnvelope](docs/DetailSpecEnvelope.md)
 - [DialBind](docs/DialBind.md)
 - [Empty](docs/Empty.md)
 - [EnrollUpdbRequest](docs/EnrollUpdbRequest.md)
 - [EnrollmentCerts](docs/EnrollmentCerts.md)
 - [EnrollmentCertsEnvelope](docs/EnrollmentCertsEnvelope.md)
 - [EntityRef](docs/EntityRef.md)
 - [EnvInfo](docs/EnvInfo.md)
 - [ErOttEnrollmentRequest](docs/ErOttEnrollmentRequest.md)
 - [IdentityAuthenticators](docs/IdentityAuthenticators.md)
 - [IdentityAuthenticatorsCert](docs/IdentityAuthenticatorsCert.md)
 - [IdentityAuthenticatorsUpdb](docs/IdentityAuthenticatorsUpdb.md)
 - [IdentityDetail](docs/IdentityDetail.md)
 - [IdentityEnrollments](docs/IdentityEnrollments.md)
 - [IdentityEnrollmentsOtt](docs/IdentityEnrollmentsOtt.md)
 - [IdentityEnrollmentsOttca](docs/IdentityEnrollmentsOttca.md)
 - [IdentityExtendCerts](docs/IdentityExtendCerts.md)
 - [IdentityExtendEnrollmentEnvelope](docs/IdentityExtendEnrollmentEnvelope.md)
 - [IdentityExtendEnrollmentRequest](docs/IdentityExtendEnrollmentRequest.md)
 - [IdentityExtendValidateEnrollmentRequest](docs/IdentityExtendValidateEnrollmentRequest.md)
 - [Jwk](docs/Jwk.md)
 - [Jwks](docs/Jwks.md)
 - [Link](docs/Link.md)
 - [ListAuthenticatorsEnvelope](docs/ListAuthenticatorsEnvelope.md)
 - [ListClientExternalJwtSignersEnvelope](docs/ListClientExternalJwtSignersEnvelope.md)
 - [ListClientTerminatorsEnvelope](docs/ListClientTerminatorsEnvelope.md)
 - [ListControllersEnvelope](docs/ListControllersEnvelope.md)
 - [ListCurrentApiSessionCertificatesEnvelope](docs/ListCurrentApiSessionCertificatesEnvelope.md)
 - [ListCurrentApiSessionServiceUpdatesEnvelope](docs/ListCurrentApiSessionServiceUpdatesEnvelope.md)
 - [ListCurrentIdentityEdgeRoutersEnvelope](docs/ListCurrentIdentityEdgeRoutersEnvelope.md)
 - [ListEnumeratedCapabilitiesEnvelope](docs/ListEnumeratedCapabilitiesEnvelope.md)
 - [ListProtocolsEnvelope](docs/ListProtocolsEnvelope.md)
 - [ListServiceEdgeRoutersEnvelope](docs/ListServiceEdgeRoutersEnvelope.md)
 - [ListServicesEnvelope](docs/ListServicesEnvelope.md)
 - [ListSessionsEnvelope](docs/ListSessionsEnvelope.md)
 - [ListSpecsEnvelope](docs/ListSpecsEnvelope.md)
 - [ListVersionEnvelope](docs/ListVersionEnvelope.md)
 - [Meta](docs/Meta.md)
 - [MfaCode](docs/MfaCode.md)
 - [MfaFormats](docs/MfaFormats.md)
 - [MfaProviders](docs/MfaProviders.md)
 - [NonceChallenge](docs/NonceChallenge.md)
 - [NonceSignature](docs/NonceSignature.md)
 - [OsType](docs/OsType.md)
 - [OtherPrime](docs/OtherPrime.md)
 - [OttEnrollmentRequest](docs/OttEnrollmentRequest.md)
 - [Pagination](docs/Pagination.md)
 - [PostureCheckType](docs/PostureCheckType.md)
 - [PostureQueries](docs/PostureQueries.md)
 - [PostureQuery](docs/PostureQuery.md)
 - [PostureQueryProcess](docs/PostureQueryProcess.md)
 - [PostureResponse](docs/PostureResponse.md)
 - [PostureResponseCreate](docs/PostureResponseCreate.md)
 - [PostureResponseDomainCreate](docs/PostureResponseDomainCreate.md)
 - [PostureResponseEndpointStateCreate](docs/PostureResponseEndpointStateCreate.md)
 - [PostureResponseEnvelope](docs/PostureResponseEnvelope.md)
 - [PostureResponseMacAddressCreate](docs/PostureResponseMacAddressCreate.md)
 - [PostureResponseOperatingSystemCreate](docs/PostureResponseOperatingSystemCreate.md)
 - [PostureResponseProcessCreate](docs/PostureResponseProcessCreate.md)
 - [PostureResponseService](docs/PostureResponseService.md)
 - [Protocol](docs/Protocol.md)
 - [RouterExtendEnrollmentRequest](docs/RouterExtendEnrollmentRequest.md)
 - [SdkInfo](docs/SdkInfo.md)
 - [ServiceDetail](docs/ServiceDetail.md)
 - [ServiceEdgeRouters](docs/ServiceEdgeRouters.md)
 - [ServicePatch](docs/ServicePatch.md)
 - [ServiceUpdate](docs/ServiceUpdate.md)
 - [SessionCreate](docs/SessionCreate.md)
 - [SessionCreateEnvelope](docs/SessionCreateEnvelope.md)
 - [SessionDetail](docs/SessionDetail.md)
 - [SessionEdgeRouter](docs/SessionEdgeRouter.md)
 - [SpecDetail](docs/SpecDetail.md)
 - [Tags](docs/Tags.md)
 - [TerminatorClientDetail](docs/TerminatorClientDetail.md)
 - [TerminatorPrecedence](docs/TerminatorPrecedence.md)
 - [Version](docs/Version.md)


<a id="documentation-for-authorization"></a>
## Documentation for Authorization


Authentication schemes defined for the API:
<a id="oauth2"></a>
### oauth2


- **Type**: OAuth
- **Flow**: accessCode
- **Authorization URL**: /oidc/authorize
- **Scopes**: 
  - openid: openid

<a id="ztSession"></a>
### ztSession


- **Type**: API key
- **API key parameter name**: zt-session
- **Location**: HTTP header


## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.
However, the instances of the api clients created from the `ApiClient` are thread-safe and can be re-used.

## Author

help@openziti.org
