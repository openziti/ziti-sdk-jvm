package org.openziti.jdbc;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import org.openziti.jdbc.ZitiDriver.ZitiFeature;
import org.openziti.jdbc.shim.Mysql;
import org.openziti.jdbc.shim.Oracle;
import org.openziti.jdbc.shim.Postgresql;

public class ZitiShimManager {
  private static final Logger log = Logger.getLogger(ZitiShimManager.class.getName());
  
  private static Set<BaseZitiDriverShim> shims = new HashSet<>();

  // Well-known and well-supported JDBC shims.  
  // Shims that cannot find vendor JDBC driver classes will be ignored.
  static {
    try {
      shims.add(new Postgresql());
    } catch (ReflectiveOperationException e) {
      log.fine("Postgres driver not detected, skipping Ziti driver shim");
    }

    try {
      shims.add(new Oracle());
    } catch (ReflectiveOperationException e) {
      log.fine("Oracle driver not detected, skipping Ziti driver shim");
    }

    try {
      shims.add(new Mysql());
    } catch (ReflectiveOperationException e) {
      log.fine("Mysql driver not detected, skipping Ziti driver shim");
    }
  }

  /**
   * Add a custom Ziti JDBC driver shim. This method will replace an existing shim registered with the same urlPattern.
   * 
   * @throws ReflectiveOperationException if driverClassName does not exist or does not implement the java.jdbc.Driver interface
   */  
  public static BaseZitiDriverShim registerShim(String urlPattern, String driverClassName, EnumSet<ZitiFeature> zitiFeatures) throws ReflectiveOperationException {
    log.fine(() -> String.format("Registring shim, URL: %s, driver: %s, features: %s", urlPattern, driverClassName, zitiFeatures));
    BaseZitiDriverShim shim = new BaseZitiDriverShim(urlPattern, driverClassName, zitiFeatures);
    return registerShim(shim);
  }

  /** 
   * Add a custom Ziti JDBC driver shim. This method will replace an existing shim registered with the same urlPattern.
   */ 
  public static BaseZitiDriverShim registerShim(BaseZitiDriverShim shim) throws ReflectiveOperationException {
    shims.add(shim);
    return shim;
  }

  protected static Optional<BaseZitiDriverShim> getShim(String url) {
    log.fine(() -> String.format("Looking for shim for url %s", url));
    Optional<BaseZitiDriverShim> result = shims.stream().filter(s -> s.acceptsURL(url) || s.acceptsURL(url)).findFirst();
    log.fine(() -> String.format("%s shim for url %s", (result.isPresent()?"Found":"Did not find"), url));
    return result;
  }
}
