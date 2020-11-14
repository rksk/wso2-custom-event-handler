package org.wso2.custom.handler.previouslogintime.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.identity.event.handler.AbstractEventHandler;
import org.wso2.custom.handler.previouslogintime.PreviousLoginTimeHandler;

/**
 * @scr.component name="org.wso2.custom.handler.previouslogintime" immediate="true"
 */
public class ServiceComponent {

    private static Log log = LogFactory.getLog(ServiceComponent.class);

    protected void activate(ComponentContext context) {
        try {
            BundleContext bundleContext = context.getBundleContext();
            bundleContext.registerService(AbstractEventHandler.class.getName(), new PreviousLoginTimeHandler(),
                    null);
            if (log.isDebugEnabled()) {
                log.debug("PreviousLoginTimeHandler is activated.");
            }
        } catch (Exception e) {
            log.error("Error while activating PreviousLoginTimeHandler component.", e);
        }
    }

    protected void deactivate(ComponentContext context) {
        if (log.isDebugEnabled()) {
            log.debug("PreviousLoginTimeHandler is de-activated.");
        }
    }

}

