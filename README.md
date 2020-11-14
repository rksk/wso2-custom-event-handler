This event handler was developed to track the time when user logged in previously. This is useful for client applications to perform certain tasks when a user logs in after a long time.

This is tested with WSO2 IS 5.7.0.

**Steps to deploy**

Build the component using `mvn clean install`

Copy the `org.wso2.custom.handler.previouslogintime-1.0.0.jar` file into `<IS_HOME>/repository/components/dropins`

Add following two lines into `<IS_HOME>/repository/conf/identity/identity-event.properties` file

    module.name.13=previousLoginTimeHandler
    previousLoginTimeHandler.subscription.1=POST_AUTHENTICATION

    Note: the number 13 has to be changed to `the highest existing number with module.name + 1`

Create two local claims

    http://wso2.org/claims/identity/currentLoginTime
    http://wso2.org/claims/identity/previousLoginTime
    
    Since these claims are identity claims (starting with http://wso2.org/claims/identity), they will be stored on the Identity DB and not on the external userstore.
    If you need to change the claim URIs, it can be done from the PreviousLoginTimeHandler class.
    
Restart WSO2 IS


**Troubleshooting**

Add following line into `log4j.properties` file and restart WSO2 IS.
`
    log4j.logger.org.wso2.custom.handler.previouslogintime=DEBUG
  

Following log will be written at server startup.  
```
DEBUG {org.wso2.custom.handler.previouslogintime.internal.ServiceComponent} -  PreviousLoginTimeHandler is activated.
```

 Following lines will be written when a user logs in sucessfully.
```
DEBUG {org.wso2.custom.handler.previouslogintime.PreviousLoginTimeHandler} -  Start handling post authentication event.
DEBUG {org.wso2.custom.handler.previouslogintime.PreviousLoginTimeHandler} -  Retrieved http://wso2.org/claims/identity/currentLoginTime claim value: 1605370394597
DEBUG {org.wso2.custom.handler.previouslogintime.PreviousLoginTimeHandler} -  Successfully updated claim values, http://wso2.org/claims/identity/currentLoginTime : 1605370475020, http://wso2.org/claims/identity/previousLoginTime : 1605370394597
```