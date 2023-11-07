package org.pih.warehouse.jobs

//import org.pih.warehouse.product.Product
import org.quartz.DisallowConcurrentExecution

@DisallowConcurrentExecution
class AssignIdentifierJob {

    def identifierService

    //def sessionRequired = false

    //static concurrent = false
    def concurrent = false

    static triggers = {
        cron name: JobUtils.getCronName(AssignIdentifierJob),
            cronExpression: JobUtils.getCronExpression(AssignIdentifierJob)
    }

    //void execute() {
    def execute() {
        if (!JobUtils.shouldExecute(AssignIdentifierJob)) {
            return
        }

        //Product.withNewSession {
            identifierService.assignProductIdentifiers()
            identifierService.assignShipmentIdentifiers()
            identifierService.assignReceiptIdentifiers()
            identifierService.assignOrderIdentifiers()
            identifierService.assignRequisitionIdentifiers()
            identifierService.assignTransactionIdentifiers()
        //}
    }
}
