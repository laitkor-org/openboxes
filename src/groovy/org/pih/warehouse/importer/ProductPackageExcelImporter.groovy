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

class ProductPackageExcelImporter extends AbstractExcelImporter {

    static Map columnMap = [
            sheet    : 'Sheet1',
            startRow : 1,
            columnMap: [
                    'A': 'id',
                    'B': 'productCode',
                    'C': 'productSupplierCode',
                    'D': 'name',
                    'E': 'description',
                    'F': 'gtin',
                    'G': 'uomCode',
                    'H': 'quantity',
                    'I': 'price'
            ]
    ]

    static Map propertyMap = [
            id                 : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            productCode        : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            productSupplierCode: ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            name               : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            description        : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            gtin               : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            uomCode            : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            quantity           : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            price              : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null])
    ]

    ProductPackageExcelImporter(String fileName) {
        super(fileName)
    }

    def getDataService() {
        return Holders.grailsApplication.getMainContext().getBean("productPackageDataService")
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
