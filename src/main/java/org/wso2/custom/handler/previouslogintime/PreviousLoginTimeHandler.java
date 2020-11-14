package org.wso2.custom.handler.previouslogintime;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.base.IdentityRuntimeException;
import org.wso2.carbon.identity.core.handler.InitConfig;
import org.wso2.carbon.identity.event.IdentityEventConstants;
import org.wso2.carbon.identity.event.IdentityEventException;
import org.wso2.carbon.identity.event.event.Event;
import org.wso2.carbon.identity.event.handler.AbstractEventHandler;

import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.identity.core.bean.context.MessageContext;

import java.util.HashMap;
import java.util.Map;

public class PreviousLoginTimeHandler extends AbstractEventHandler {

    // Claim URIs
    public static final String CURRENT_LOGIN_TIME_CLAIM = "http://wso2.org/claims/identity/currentLoginTime";
    public static final String PREVIOUS_LOGIN_TIME_CLAIM = "http://wso2.org/claims/identity/previousLoginTime";

    private static final Log log = LogFactory.getLog(PreviousLoginTimeHandler.class);
    private static final String HANDLER_NAME = "previousLoginTimeHandler";

    public void handleEvent(Event event) throws IdentityEventException {

        Map<String, Object> eventProperties = event.getEventProperties();
        UserStoreManager userStoreManager = (UserStoreManager)
                eventProperties.get(IdentityEventConstants.EventProperty.USER_STORE_MANAGER);

        if (IdentityEventConstants.Event.POST_AUTHENTICATION.equals(event.getEventName())) {
            handlePostAuthenticate(eventProperties, userStoreManager);
        }
    }

    private void handlePostAuthenticate(Map<String, Object> eventProperties, UserStoreManager userStoreManager)
            throws IdentityEventException {

        if (log.isDebugEnabled()) {
            log.debug("Start handling post authentication event.");
        }
        if ((Boolean) eventProperties.get(IdentityEventConstants.EventProperty.OPERATION_STATUS)) {

            String username = (String) eventProperties.get(IdentityEventConstants.EventProperty.USER_NAME);
            String currentLoginTime = Long.toString(System.currentTimeMillis());
            String previousLoginTime = null;
            try {
                Map<String, String> values = userStoreManager.getUserClaimValues(username,
                        new String[]{CURRENT_LOGIN_TIME_CLAIM}, null);
                previousLoginTime = values.get(CURRENT_LOGIN_TIME_CLAIM);
                if (log.isDebugEnabled()) {
                    log.debug("Retrieved " + CURRENT_LOGIN_TIME_CLAIM + " claim value: " + previousLoginTime);
                }
            } catch (UserStoreException e) {
                log.error("Error occurred while retrieving " + CURRENT_LOGIN_TIME_CLAIM + " claim value.", e);
            }

            Map<String, String> userClaims = new HashMap<>();
            userClaims.put(CURRENT_LOGIN_TIME_CLAIM, currentLoginTime);
            userClaims.put(PREVIOUS_LOGIN_TIME_CLAIM, previousLoginTime);
            try {
                userStoreManager.setUserClaimValues(username, userClaims, null);
                if (log.isDebugEnabled()) {
                    log.debug("Successfully updated claim values, " + CURRENT_LOGIN_TIME_CLAIM + " : "
                            + currentLoginTime + ", " + PREVIOUS_LOGIN_TIME_CLAIM + " : " + previousLoginTime);
                }
            } catch (UserStoreException e) {
                log.error("Error occurred while updating user claims.", e);
            }
        }
    }


    @Override public String getName() {
        return HANDLER_NAME;
    }

    @Override
    public void init(InitConfig configuration) throws IdentityRuntimeException {
        super.init(configuration);
    }

    @Override
    public int getPriority(MessageContext messageContext) {
        return 250;
    }

}
