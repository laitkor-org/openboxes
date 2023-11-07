/**
 * Copyright (c) 2012 Partners In Health.  All rights reserved.
 * The use and distribution terms for this software are covered by the
 * Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 * which can be found in the file epl-v10.html at the root of this distribution.
 * By using this software in any fashion, you are agreeing to be bound by
 * the terms of this license.
 * You must not remove this notice, or any other, from this software.
 **/
package org.pih.warehouse.importer


// import org.codehaus.groovy.grails.commons.ApplicationHolder
import grails.util.Holders
import org.grails.plugins.excelimport.AbstractExcelImporter
import org.grails.plugins.excelimport.ExcelImportService
import org.pih.warehouse.data.ProductSupplierDataService

class ProductSupplierPreferenceImporter extends AbstractExcelImporter {

    ProductSupplierDataService productSupplierDataService

    static Map columnMap = [
            sheet    : 'Sheet1',
            startRow : 1,
            columnMap: [
                    'A': 'supplierCode',
                    'B': 'organizationCode',
                    'C': 'organizationName',
                    'D': 'preferenceTypeName',
                    'E': 'validityStartDate',
                    'F': 'validityEndDate',
                    'G': 'preferenceComments',
            ]
    ]

    static Map propertyMap = [
            supplierCode         : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            organizationCode     : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            organizationName     : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            preferenceTypeName   : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            validityStartDate    : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            validityEndDate      : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            preferenceComments   : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
    ]


    ProductSupplierPreferenceImporter(String fileName) {
        super(fileName)
    }

    def getDataService() {
        return Holders.grailsApplication.getMainContext().getBean("productSupplierPreferenceDataService")
    }


    List<Map> getData() {
        return ExcelImportService.convertColumnMapConfigManyRows(workbook, columnMap, null, propertyMap)
    }

    void validateData(ImportDataCommand command) {
        dataService.validate(command)
    }

    void importData(ImportDataCommand command) {
        dataService.process(command)
    }

}
