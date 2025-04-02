package com.uplus.data.type;

import com.pega.sdk.data.type.annotation.Field;
import java.util.List;
import java.util.Objects;

/**
 * Purpose of this class is to demonstrate how to use the
 * {@link com.pega.sdk.data.type.annotation.Field} annotation to capture
 * relevant information in a POJO.
 */
public class PurchaseOrder {

    public PurchaseOrder(){}
    public PurchaseOrder(String id, List<PurchaseOrderItem> orders){
        this.id = id;
        this.orders = orders;
    }

    @Field(ID = "ID", namespace = "PegaPlatform")
    String id;

    @Field(ID = "Orders", namespace = "UPlus")
    List<PurchaseOrderItem> orders;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseOrder that = (PurchaseOrder) o;
        return Objects.equals(id, that.id) && Objects.equals(orders, that.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orders);
    }
}
