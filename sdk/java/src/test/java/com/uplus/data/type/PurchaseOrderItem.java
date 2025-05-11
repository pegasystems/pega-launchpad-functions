package com.uplus.data.type;

import com.pega.sdk.data.type.annotation.Field;

import java.util.Objects;

public class PurchaseOrderItem {
    @Field(ID = "Quantity", namespace = "UPlus")
    int quantity;
    @Field(ID = "Price", namespace = "UPlus")
    double price;
    @Field(ID = "ItemID", namespace = "UPlus")
    String itemID;

    @SuppressWarnings("unused") public PurchaseOrderItem() {

    }

    public PurchaseOrderItem(String itemID, double price, int quantity) {
        this.itemID = itemID;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseOrderItem that = (PurchaseOrderItem) o;
        return quantity == that.quantity && Double.compare(price, that.price) == 0 && Objects.equals(itemID, that.itemID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, price, itemID);
    }
}
