package org.openziti.jdbc.shim;

import static org.openziti.jdbc.ZitiDriver.ZitiFeature.seamless;
import java.util.EnumSet;
import java.util.Properties;

import org.openziti.jdbc.BaseZitiDriverShim;

public class Oracle extends BaseZitiDriverShim {

	public Oracle() throws ReflectiveOperationException {
		super("^(zdbc|jdbc:ziti):oracle:thin.*", "oracle.jdbc.OracleDriver", EnumSet.of(seamless));
	}

	@Override
	public void configureDriverProperties(Properties props) {
		props.setProperty("oracle.jdbc.javaNetNio", "false");
		props.setProperty("oracle.net.disableOob", "true");
	}
}