/*
 * ****************************************************************************
 *     Cloud Foundry
 *     Copyright (c) [2009-2016] Pivotal Software, Inc. All Rights Reserved.
 *
 *     This product is licensed to you under the Apache License, Version 2.0 (the "License").
 *     You may not use this product except in compliance with the License.
 *
 *     This product includes a number of subcomponents with
 *     separate copyright notices and license terms. Your use of these
 *     subcomponents is subject to the terms and conditions of the
 *     subcomponent's license, as noted in the LICENSE file.
 * ****************************************************************************
 */

package org.cloudfoundry.identity.uaa.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.cloudfoundry.identity.uaa.util.UaaUrlUtils;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UaaSavedRequestAwareAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public static final String SAVED_REQUEST_SESSION_ATTRIBUTE = "SPRING_SECURITY_SAVED_REQUEST";

    public static final String URI_OVERRIDE_ATTRIBUTE = "override.redirect_uri";

    public static final String FORM_REDIRECT_PARAMETER = "form_redirect_uri";

    private static Logger logger = LoggerFactory.getLogger(UaaSavedRequestAwareAuthenticationSuccessHandler.class);

    @Override
    public String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("******** karun: determineTargetUrl: ");
        Object redirectAttribute = request.getAttribute(URI_OVERRIDE_ATTRIBUTE);
        logger.debug("redirectAttribute: {}", redirectAttribute);
        String redirectFormParam = request.getParameter(FORM_REDIRECT_PARAMETER);
        logger.debug("redirectFormParam: {}", redirectFormParam);
        if (redirectAttribute !=null) {
            logger.debug("Returning redirectAttribute saved URI:"+redirectAttribute);
            return (String) redirectAttribute;
        } else if (UaaUrlUtils.uriHasMatchingHost(redirectFormParam, request.getServerName())) {
            logger.debug("Returning Form Param");
            return redirectFormParam;
        } else {
            logger.debug("Falling through to superclass");
            return super.determineTargetUrl(request, response);
        }
    }
}
