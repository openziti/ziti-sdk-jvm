
package org.openziti.jdbc;

import static org.openziti.jdbc.ZitiDriver.ZitiFeature.nioProvider;
import static org.openziti.jdbc.ZitiDriver.ZitiFeature.seamless;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Base64;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import org.openziti.Ziti;
import org.openziti.ZitiContext;
import org.openziti.api.Service;

public class ZitiDriver implements java.sql.Driver {
  private static final Logger log = Logger.getLogger(ZitiDriver.class.getName());

  private static ZitiDriver registeredDriver;
  private static Set<String> zitiConfigs = new HashSet<>();
  private static Set<BaseZitiDriverShim> configuredShims = new HashSet<>();

  public static final String ZITI_JSON = "zitiIdentityFile";
  public static final String ZITI_IDENTITY = "zitiIdentity";
  public static final String ZITI_KEYSTORE = "zitiKeystore";
  public static final String ZITI_KEYSTORE_PASSWORD = "zitiKeystorePassword";
  public static final String ZITI_DRIVER_URL_PATTERN = "zitiDriverUrlPattern";
  public static final String ZITI_DRIVER_CLASSNAME = "zitiDriverClassname";
  public static final String ZITI_DRIVER_FEATURES = "zitiDriverFeatures";
  public static final String ZITI_WAIT_FOR_SERVICE_NAME = "zitiWaitForService";
  public static final String ZITI_WAIT_FOR_SERVICE_TIMEOUT = "zitiWaitForServiceTimeout";

  public static enum ZitiFeature {
    seamless, nioProvider
  }

  static {
    try {
      register();
    } catch (SQLException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  @Override
  public Connection connect(String url, Properties info) throws SQLException {
    if (!acceptsURL(url)) {
      return null;
    }

    log.fine("Ziti driver is attempting to connect to " + url);

    // Throws a SQLException if a shim could not be found or configured
    BaseZitiDriverShim shim = ZitiShimManager.getShim(url).orElseGet(() -> registerShim(info));

    parseUrl(url, info);
    shim.configureDriverProperties(info);

    setupZiti(shim, info);

    // Replace zdbc with jdbc so shim drivers know how to connect
    String dbUrl = toProviderUrl(url);

    return shim.getDelegate().connect(dbUrl, info);
  }

  @Override
  public boolean acceptsURL(String url) throws SQLException {
    return null != url && (url.startsWith("zdbc") || url.startsWith("jdbc:ziti:"));
  }

  private String toProviderUrl(String zdbcUrl) {
    final String result;
    if (zdbcUrl.startsWith("zdbc")) {
      result = zdbcUrl.replaceFirst("zdbc","jdbc");
    } else if (zdbcUrl.startsWith("jdbc:ziti")) {
      result = zdbcUrl.replaceFirst("jdbc:ziti","jdbc");
    } else {
      result = zdbcUrl;
    }

    return result;
  }
  @Override
  public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {

    DriverPropertyInfo identity = new DriverPropertyInfo(ZITI_IDENTITY, "Ziti Identity");
    identity.description = "Ziti Identity json.  Zipped and base64 encoded";
    identity.required = false;

    DriverPropertyInfo identityFile = new DriverPropertyInfo(ZITI_JSON, "/path/to/identity.json");
    identityFile.description = "Ziti identify file to use for the connection";
    identityFile.required = false;

    DriverPropertyInfo keystoreFile = new DriverPropertyInfo(ZITI_KEYSTORE, "/path/to/keystore");
    keystoreFile.description = "Keystore containing one or more Ziti identities";
    keystoreFile.required = false;

    DriverPropertyInfo keystorePassword =
        new DriverPropertyInfo(ZITI_KEYSTORE_PASSWORD, "secretPassword");
    keystorePassword.description = "Password for the Ziti keystore";
    keystorePassword.required = false;

    try {
      // Throws a SQL Exception if a shim could not be found or created
      BaseZitiDriverShim shim = ZitiShimManager.getShim(url).orElseGet(() -> registerShim(info));
      String dbUrl = toProviderUrl(url);

      DriverPropertyInfo[] props = shim.getDelegate().getPropertyInfo(dbUrl, info);

      DriverPropertyInfo[] result = new DriverPropertyInfo[props.length + 4];
      result[0] = identity;
      result[1] = identityFile;
      result[2] = keystoreFile;
      result[3] = keystorePassword;
      System.arraycopy(props, 0, result, 4, props.length);

      return result;
    } catch (ShimException e) {
      // No shim could be found, and no shim could be registered. Return the basic properties to define a shim

      log.fine("Ziti driver could not find a shim for url: " + url + ".  Returning basic properties.");

      DriverPropertyInfo driverUrlPattern = new DriverPropertyInfo(ZITI_DRIVER_URL_PATTERN, "^jdbc.ziti:<database>.*");
      identityFile.description = "Regular expression used to identify the jdbc URL for this driver";
      identityFile.required = true;

      DriverPropertyInfo driverClassName = new DriverPropertyInfo(ZITI_DRIVER_CLASSNAME, "org.jdbc.Driver");
      keystoreFile.description = "The JDBC driver to delegate to";
      keystoreFile.required = true;

      DriverPropertyInfo zitiFeatures = new DriverPropertyInfo(ZITI_DRIVER_FEATURES, "seamless,nioProvider");
      keystorePassword.description = "Ziti driver features required for the JDBC driver";
      keystorePassword.required = true;

      DriverPropertyInfo[] result = new DriverPropertyInfo[6];
      result[0] = identityFile;
      result[1] = keystoreFile;
      result[2] = keystorePassword;
      result[3] = driverUrlPattern;
      result[4] = driverClassName;
      result[5] = zitiFeatures;

      return result;
    }
  }

  @Override
  public int getMajorVersion() {
    return 1;
  }

  @Override
  public int getMinorVersion() {
    return 0;
  }

  @Override
  public boolean jdbcCompliant() {
    // The JDBC driver provided by the shim may be JDBC compliant, but this driver cannot know that
    // for sure.
    // It is forced to return false to be compliant with the spec.
    return false;
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return null;
  }

  public static void register() throws SQLException {
    log.fine("Registering Ziti JDBC Driver");

    if (isRegistered()) {
      throw new IllegalStateException(
          "Driver is already registered. It can only be registered once.");
    }
    ZitiDriver registeredDriver = new ZitiDriver();
    DriverManager.registerDriver(registeredDriver);
    ZitiDriver.registeredDriver = registeredDriver;
  }


  /**
   * @return {@code true} if the driver is registered against {@link DriverManager}
   */
  public static boolean isRegistered() {
    return registeredDriver != null;
  }

  private BaseZitiDriverShim registerShim(Properties info) throws ShimException {
    if (!info.containsKey(ZITI_DRIVER_URL_PATTERN) || !info.containsKey(ZITI_DRIVER_CLASSNAME)) {
      throw new ShimException("No Ziti driver shim available");
    }

    final EnumSet<ZitiFeature> features;
    if (info.containsKey(ZITI_DRIVER_FEATURES)) {
      features = Arrays
          .stream(info.getProperty(ZITI_DRIVER_FEATURES).split(","))
          .map(s -> ZitiFeature.valueOf(s))
          .collect(Collectors.toCollection(() -> EnumSet.noneOf(ZitiFeature.class)));
    } else {
      features = EnumSet.noneOf(ZitiFeature.class);
    }

    try {
      return ZitiShimManager.registerShim(
          info.getProperty(ZITI_DRIVER_URL_PATTERN),
          info.getProperty(ZITI_DRIVER_CLASSNAME),
          features);
    } catch (ReflectiveOperationException e) {
      throw new ShimException("Could not create custom Ziti jdbc driver shim", e);
    }
  }

  private synchronized void setupZiti(BaseZitiDriverShim shim, Properties info) {

    if (zitiConfigs.contains(info.getProperty(ZITI_JSON))
        || zitiConfigs.contains(info.getProperty(ZITI_KEYSTORE))
        || zitiConfigs.contains(info.getProperty(ZITI_IDENTITY))) {
      log.finest("Ziti has already been configured, skipping setup");
      return;
    }

    if (info.containsKey(ZITI_JSON) || info.containsKey(ZITI_KEYSTORE)
        || info.containsKey(ZITI_IDENTITY)) {
      log.info(
          "Ziti JDBC wrapper is configuring Ziti identities. Production applications should manage Ziti identities directly");
    }


    // Check to see if NIO feature is required
    if (!configuredShims.contains(shim) && requiresFeature(shim, nioProvider)) {
      log.info(
          "Ziti JDBC wrapper is setting the system property 'java.nio.channels.spi.SelectorProvider' to 'org.openziti.net.nio.ZitiSelectorProvider'");
      System.setProperty("java.nio.channels.spi.SelectorProvider",
          "org.openziti.net.nio.ZitiSelectorProvider");
    }

    if (info.containsKey(ZITI_JSON)) {
      log.finer(() -> String.format("Found identity file %s in connection properties.",
          info.getProperty(ZITI_JSON)));

      Ziti.init(info.getProperty(ZITI_JSON), "".toCharArray(), requiresFeature(shim, seamless));

      zitiConfigs.add(info.getProperty(ZITI_JSON));
    } else if (info.containsKey(ZITI_KEYSTORE)) {

      log.finer(() -> String.format("Found keystore file %s in connection properties.",
          info.getProperty(ZITI_KEYSTORE)));
      Ziti.init(info.getProperty(ZITI_KEYSTORE),
          info.getProperty(ZITI_KEYSTORE_PASSWORD).toCharArray(), requiresFeature(shim, seamless));
      zitiConfigs.add(info.getProperty(ZITI_KEYSTORE));
    } else if (info.containsKey(ZITI_IDENTITY)) {
      log.finer(() -> String.format("Found ziti identity connection properties."));

      byte[] decoded = Base64.getDecoder().decode(info.getProperty(ZITI_IDENTITY).getBytes());

      try (
          ByteArrayInputStream is = new ByteArrayInputStream(decoded);
          GZIPInputStream gz = new GZIPInputStream(is);) {

        Ziti.init(toByteArray(gz), requiresFeature(shim, seamless));

        zitiConfigs.add(info.getProperty(ZITI_KEYSTORE));

      } catch (IOException ioe) {
        throw new IllegalArgumentException(
            ZITI_IDENTITY + " is expected to be a base64 encoded, gzipped value.");
      }
    }
    
    if (!configuredShims.contains(shim) && requiresFeature(shim, seamless) && !Ziti.isSeamless()) {
      throw new ShimException(String.format(
          "Ziti JDBC configuration error. JDBC driver %s requires ziti seamless mode, but it is not enabled",
          shim.getDelegate().getClass().getName()));
    }

    if (info.containsKey(ZITI_WAIT_FOR_SERVICE_NAME)) {
      boolean found = checkForService(info.getProperty(ZITI_WAIT_FOR_SERVICE_NAME));

      if (!found) {
        if (info.containsKey(ZITI_WAIT_FOR_SERVICE_TIMEOUT)) {
          Duration timeout;

          try {
            timeout = Duration.parse(info.getProperty(ZITI_WAIT_FOR_SERVICE_TIMEOUT));
          } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("The property " + ZITI_WAIT_FOR_SERVICE_TIMEOUT + " must be parsable as a java Duration", e);
          }

          long startTime = System.currentTimeMillis();
          while (!found && (System.currentTimeMillis() < startTime+timeout.toMillis())) {
            try {
              Thread.sleep(500);
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              break;
            }
            found = checkForService(info.getProperty(ZITI_WAIT_FOR_SERVICE_NAME));
            log.fine(() -> String.format("No Ziti contexts provide %s yet. Remaining timeout: %sms",
                    info.getProperty(ZITI_WAIT_FOR_SERVICE_NAME), ((startTime + timeout.toMillis()) - System.currentTimeMillis())));
          }
        }
      }

      if (!found) {
        throw new IllegalArgumentException("The Ziti service " + info.getProperty(ZITI_WAIT_FOR_SERVICE_NAME + " is not available in any Ziti context"));
      }
      
      log.fine(() -> String.format("Found a Ziti context that provides %s, continuing with connection", info.getProperty(ZITI_WAIT_FOR_SERVICE_TIMEOUT)));
    }

    configuredShims.add(shim);
  }

  private boolean checkForService(String serviceName) {
    boolean result = false;
    log.fine("Checking Ziti context for service " + serviceName);
    for (ZitiContext c:Ziti.getContexts()) {
      if (null != c.getService(serviceName)) {
        log.fine("Found service with name " + serviceName );
        result = true;
        break;
      }
    }
    
    return result;
  }

  protected static byte[] toByteArray(InputStream is) throws IOException {
    // Copy the unzipped identity to a byte array
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    int nRead;
    byte[] data = new byte[1024];

    while ((nRead = is.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, nRead);
    }

    buffer.flush();
    byte result[] = buffer.toByteArray();
    return result;
  }
  
  protected static boolean requiresFeature(BaseZitiDriverShim shim, ZitiFeature feature) {
    return null != shim.getZitiFeatures() && shim.getZitiFeatures().contains(feature);
  }

  protected void parseUrl(String url, Properties info) throws SQLException {
    int qPos = url.indexOf('?');
    if (qPos == -1) {
      // No arguments on the URL
      return;
    }

    String urlArgs = url.substring(qPos + 1);

    // parse the args part of the url
    String[] args = urlArgs.split("&");
    for (String token : args) {
      if (token.isEmpty()) {
        continue;
      }
      int pos = token.indexOf('=');
      if (pos == -1) {
        info.setProperty(token, "");
      } else {
        try {
          info.setProperty(token.substring(0, pos),
              URLDecoder.decode(token.substring(pos + 1), Charset.defaultCharset().name()));
        } catch (UnsupportedEncodingException e) {
          throw new SQLException(e);
        }
      }
    }
  }

  /** Little wrapper that lets us wrap exceptions found during shim init in lambdas */
  private static class ShimException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    ShimException(String message) {
      super(message);
    }

    ShimException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
