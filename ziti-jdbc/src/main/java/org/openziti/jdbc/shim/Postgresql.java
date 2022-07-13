package org.openziti.jdbc.shim;

import java.util.EnumSet;
import java.util.Properties;
import org.openziti.jdbc.BaseZitiDriverShim;
import org.openziti.jdbc.ZitiDriver.ZitiFeature;

public class Postgresql extends BaseZitiDriverShim {
  public Postgresql() throws ReflectiveOperationException {
    super("^(zdbc|jdbc:ziti):postgresql.*", "org.postgresql.Driver", EnumSet.noneOf(ZitiFeature.class));
  }

  @Override
  public void configureDriverProperties(Properties props) {
    props.setProperty("socketFactory", "org.openziti.net.ZitiSocketFactory");
  }
}
