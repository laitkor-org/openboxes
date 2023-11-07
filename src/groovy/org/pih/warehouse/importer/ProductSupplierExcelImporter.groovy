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

class ProductSupplierExcelImporter extends AbstractExcelImporter {

    ProductSupplierDataService productSupplierDataService

    static Map columnMap = [
            sheet    : 'Sheet1',
            startRow : 1,
            columnMap: [
                    'A': 'active',
                    'B': 'id',
                    'C': 'code',
                    'D': 'name',
                    'E': 'productCode',
                    'F': 'productName',
                    'G': 'legacyProductCode',
                    'H': 'supplierName',
                    'I': 'supplierCode',
                    'J': 'manufacturerName',
                    'K': 'manufacturerCode',
                    'L': 'minOrderQuantity',
                    'M': 'contractPricePrice',
                    'N': 'contractPriceValidUntil',
                    'O': 'ratingTypeCode',
                    'P': 'globalPreferenceTypeName',
                    'Q': 'globalPreferenceTypeValidityStartDate',
                    'R': 'globalPreferenceTypeValidityEndDate',
                    'S': 'globalPreferenceTypeComments',
                    'T': 'defaultProductPackageUomCode',
                    'U': 'defaultProductPackageQuantity',
                    'V': 'defaultProductPackagePrice',
            ]
    ]

    static Map propertyMap = [
            active                                : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            id                                    : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            code                                  : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            name                                  : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            productCode                           : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            productName                           : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            legacyProductCode                     : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            supplierName                          : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            supplierCode                          : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            manufacturerName                      : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            manufacturerCode                      : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            minOrderQuantity                      : ([expectedType: ExcelImportService.PROPERTY_TYPE_INT, defaultValue: null]),
            contractPricePrice                    : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            contractPriceValidUntil               : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            ratingType                            : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            globalPreferenceTypeName              : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            globalPreferenceTypeValidityStartDate : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            globalPreferenceTypeValidityEndDate   : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            globalPreferenceTypeComments          : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            defaultProductPackageUomCode          : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null]),
            defaultProductPackageQuantity         : ([expectedType: ExcelImportService.PROPERTY_TYPE_INT, defaultValue: null]),
            defaultProductPackagePrice            : ([expectedType: ExcelImportService.PROPERTY_TYPE_STRING, defaultValue: null])
    ]


    ProductSupplierExcelImporter(String fileName) {
        super(fileName)
    }

    def getDataService() {
        return Holders.grailsApplication.getMainContext().getBean("productSupplierDataService")
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
