package com.pms;

/*
 * TEMPORARY frontend copy of the Supplier model from the shared contract
 * (IMPLEMENTATION_PLAN.md). Delete this file once P1's backend jar provides
 * com.pms.Supplier, otherwise this copy hides the real one.
 */
public class Supplier {

    public int supplierId;
    public String supplierName;

    public Supplier(int supplierId, String supplierName) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    // ComboBox<Supplier> shows this text
    @Override
    public String toString() {
        return supplierName;
    }
}
