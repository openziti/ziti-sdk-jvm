package org.openziti.jdbc.shim;

import java.util.EnumSet;
import org.openziti.jdbc.BaseZitiDriverShim;
import org.openziti.jdbc.ZitiDriver.ZitiFeature;

public class Mysql extends BaseZitiDriverShim {

  public Mysql() throws ReflectiveOperationException {
    super("^(zdbc|jdbc:ziti):mysql.*", "com.mysql.cj.jdbc.Driver", EnumSet.of(ZitiFeature.seamless));
  }
}
