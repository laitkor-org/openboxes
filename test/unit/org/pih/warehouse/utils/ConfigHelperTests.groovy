package org.pih.warehouse.utils

import grails.test.GrailsUnitTestCase
import org.junit.Before
import org.junit.Test
import org.pih.warehouse.core.RoleType
import util.ConfigHelper

class ConfigHelperTests extends GrailsUnitTestCase {
    @Before
    void setUp() {
        super.setUp()
        String rbacRulesConfig = """
            openboxes { 
                security {
                    rbac { 
                        rules = [
                            [controller: '*', actions: ['*'], accessRules: [ minimumRequiredRole: org.pih.warehouse.core.RoleType.ROLE_BDSUPERADMIN ]],
                            [controller: "order", actions: ['show'], accessRules: [ minimumRequiredRole: org.pih.warehouse.core.RoleType.ROLE_BDSUPERADMIN ]],
                            [controller: 'order', actions: ['*'], accessRules: [ minimumRequiredRole: org.pih.warehouse.core.RoleType.ROLE_BDSUPERADMIN ]],
                            [controller: '*', actions: ['remove'], accessRules: [ minimumRequiredRole: org.pih.warehouse.core.RoleType.ROLE_BDSUPERADMIN ]],
                            [controller : "stockRequest", actions : ["remove"], accessRules: [ minimumRequiredRole: org.pih.warehouse.core.RoleType.ROLE_MANAGER ]],
                            [controller : "stockRequest", actions : ["remove"], accessRules: [ minimumRequiredRole: org.pih.warehouse.core.RoleType.ROLE_BDT_ADMIN ]],
                            [controller: 'stockRequest', actions: ['removeItem'], accessRules: [ minimumRequiredRole: org.pih.warehouse.core.RoleType.ROLE_BROWSER ]],
                        ]
                    }    
                }    
            }
        """
        mockConfig(rbacRulesConfig)
    }
    @Test
    void findAccessRule_shouldReturnCorrectRuleWhenTwoRulesForTheSameControllerAndAction() {
        // More specified role is expected to be returned
        def expectedResult = [controller: 'stockRequest', actions: ['remove'], accessRules: [minimumRequiredRole: RoleType.ROLE_BDT_ADMIN]]
        assertEquals expectedResult, ConfigHelper.findAccessRule("stockRequest", "remove")
    }


    @Test
    void findAccessRule_shouldReturnCorrectRuleWhenTwoActionsContainingTheSameStringFirstScenario() {
        def expectedResult = [controller: 'stockRequest', actions: ['remove'], accessRules: [minimumRequiredRole: RoleType.ROLE_BDT_ADMIN]]
        assertEquals expectedResult, ConfigHelper.findAccessRule("stockRequest", "remove")
    }

    @Test
    void findAccessRule_shouldReturnCorrectRuleWhenTwoActionsContainingTheSameStringSecondScenario() {
        def expectedResult = [controller: 'stockRequest', actions: ['removeItem'], accessRules: [ minimumRequiredRole: RoleType.ROLE_BROWSER ]]
        assertEquals expectedResult, ConfigHelper.findAccessRule("stockRequest", "removeItem")
    }

    @Test
    void findAccessRule_shouldReturnGenericRuleIfNotExistingForControllerButGenericExists() {
        def expectedResult = [controller: '*', actions: ['*'], accessRules: [ minimumRequiredRole: RoleType.ROLE_BDSUPERADMIN ]]
        assertEquals expectedResult, ConfigHelper.findAccessRule("inventoryItem", "create")
    }

    @Test
    void findAccessRule_shouldReturnCorrectRuleIfControllerHasAsteriskAsActions() {
        def expectedResult = [controller: 'order', actions: ['*'], accessRules: [ minimumRequiredRole: RoleType.ROLE_BDSUPERADMIN ]]
        assertEquals expectedResult, ConfigHelper.findAccessRule("order", "remove")
    }

    @Test
    void findAccessRule_shouldReturnMoreSpecifiedRuleIfAdditionalExisitingRuleForControllerButActionIsAsterisk() {
        def expectedResult = [controller: 'order', actions: ['show'], accessRules: [minimumRequiredRole: RoleType.ROLE_BDSUPERADMIN ]]
        assertEquals expectedResult, ConfigHelper.findAccessRule("order", "show")

    }
}
