package org.openziti.jdbc;

import java.sql.Driver;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.openziti.jdbc.ZitiDriver.ZitiFeature;

public class BaseZitiDriverShim {
  private static final Logger log = Logger.getLogger(BaseZitiDriverShim.class.getName());
  
  protected final Driver delegate;
  protected final Pattern urlPattern;
  protected final String driverClass;
  protected final EnumSet<ZitiFeature> zitiFeatures;

  public BaseZitiDriverShim(String urlPattern, String driverClassName, EnumSet<ZitiFeature> zitiFeatures) throws ReflectiveOperationException {
    this.urlPattern = Pattern.compile(urlPattern);
    this.driverClass = driverClassName;
    this.zitiFeatures = zitiFeatures;
    delegate = (Driver) Class.forName(driverClassName, false, getClass().getClassLoader()).getDeclaredConstructor().newInstance();
  }

  /** return <code>true</code> if this shim can process a jdbc connection url */
  public boolean acceptsURL(String url) {
    boolean result = urlPattern.matcher(url).matches();
    log.finer(() -> String.format("Shim %s %s url %s", getClass().getName(), (result?"accpts":"does not accept"), url));
    return result;
  }

  /** Return an instance of the vendor JDBC driver for this shim */
  public Driver getDelegate() {
    return delegate;
  }

  /** The set of the features required of the Ziti network for the vendor JDBC driver to connect */
  public EnumSet<ZitiFeature> getZitiFeatures() {
    return zitiFeatures;
  }

  /**
   * Optionally configure additional properties required to allow the vendor JDBC driver to connect
   * via a Ziti network
   */
  public void configureDriverProperties(Properties props) { /* NOOP */ }

  @Override
  public int hashCode() {
    return Objects.hash(urlPattern.pattern());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    BaseZitiDriverShim other = (BaseZitiDriverShim) obj;
    return Objects.equals(urlPattern.pattern(), other.urlPattern.pattern());
  }
}
