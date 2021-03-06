<!--

    Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Public License v. 2.0, which is available at
    http://www.eclipse.org/legal/epl-2.0.

    This Source Code may also be made available under the following Secondary
    Licenses when the conditions for such availability set forth in the
    Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
    version 2 with the GNU Classpath Exception, which is available at
    https://www.gnu.org/software/classpath/license.html.

    SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0

-->

<!-- security/msgSecurity/msgSecurityTabs.inc -->

<sun:tabSet id="msgSecurityTabs" immediate="true" selected="#{sessionScope.msgSecurityTabs}">
    <sun:tab id="msgSecurityEdit" immediate="true" text="$resource{i18nc.msgSecurity.msgSecurityTab}" toolTip="$resource{i18nc.msgSecurity.msgSecurityTab.tooltip}" >
        <!command
            setSessionAttribute(key="msgSecurityTabs" value="msgSecurityEdit");
            gf.redirect(page="msgSecurityEdit.jsf?configName=#{encodedConfigName}&name=#{pageSession.encodedName}");
        />       
    </sun:tab>
    <sun:tab id="providers" immediate="true" text="$resource{i18nc.msgSecurity.providersTab}" toolTip="$resource{i18nc.msgSecurity.providersTab.tooltip}">
        <!command
            setSessionAttribute(key="msgSecurityTabs" value="providers");
            gf.redirect(page="providers.jsf?configName=#{encodedConfigName}&name=#{pageSession.encodedName}");
	/>    
    </sun:tab>    
</sun:tabSet>
