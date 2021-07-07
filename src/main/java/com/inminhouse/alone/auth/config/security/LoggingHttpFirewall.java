package com.inminhouse.alone.auth.config.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingHttpFirewall extends StrictHttpFirewall {
	
	@Override
    public FirewalledRequest getFirewalledRequest(final HttpServletRequest request) throws RequestRejectedException
    {
        try {
            return super.getFirewalledRequest(request);
        } catch (RequestRejectedException ex) {
            
        	if(log.isWarnEnabled()) {
            	log.warn("Intercepted RequestBlockedException: Remote Host: " + request.getRemoteHost() + " User Agent: " + request.getHeader("User-Agent") + " Request URL: " + request.getRequestURL().toString());
            }

            // Wrap in a new RequestRejectedException with request metadata and a shallower stack trace.
            throw new RequestRejectedException(
        		ex.getMessage() + 
        		".\n Remote Host: " + request.getRemoteHost() + 
				"\n User Agent: " + request.getHeader("User-Agent") + 
				"\n Request URL: " + request.getRequestURL().toString()
    		) {
                private static final long serialVersionUID = 1L;

                @Override
                public synchronized Throwable fillInStackTrace() {
                    return this; // suppress the stack trace.
                }
            };
        }
    }
	
}
