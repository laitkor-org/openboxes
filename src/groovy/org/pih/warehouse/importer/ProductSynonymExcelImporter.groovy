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
import org.grails.plugins.excelimport.ExcelImportService

class ProductSynonymExcelImporter extends AbstractExcelImporter {

    static Map columnMap = [
            sheet    : 'Sheet1',
            startRow : 1,
            columnMap: [
                    'A': 'product.productCode',
                    'B': 'synonymTypeCode',
                    'C': 'locale',
                    'D': 'name',
            ]
    ]

    static Map propertyMap = [
            "product.productCode"       : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            "synonymTypeCode"           : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            "locale"                    : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            "name"                      : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
    ]


    ProductSynonymExcelImporter(String fileName) {
        super(fileName)
    }

    def getDataService() {
        return Holders.grailsApplication.getMainContext().getBean("productSynonymDataService")
    }

    List<Map> getData() {
        return ExcelImportService.convertColumnMapConfigManyRows(workbook, columnMap, null, propertyMap)
    }

    void validateData(ImportDataCommand command) {
        dataService.validateData(command)
    }

    void importData(ImportDataCommand command) {
        dataService.importData(command)
    }

}
