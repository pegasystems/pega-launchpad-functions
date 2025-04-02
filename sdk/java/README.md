
## SDK in Launchpad
Pega Launchpad supports a number of data types to use with your
application, most of which are compatible with standard Java types.

* DateTime  -> LocalDateTime
* Date      -> LocalDate
* Decimal   -> BigDecimal
* Integer   -> int / java.lang.Integer

However, for data types of type `Single Record` or `Multi Records` we suggest using the
[@Field](src/main/java/com/pega/sdk/data/type/annotation/Field.java) annotation. 
The annotation works in the same way 
Jackson's [@JsonProperty](https://fasterxml.github.io/jackson-annotations/javadoc/2.6/com/fasterxml/jackson/annotation/JsonProperty.html)
works with the caveat that it natively supports Launchpad's data model.
For more information see [@Field](src/main/java/com/pega/sdk/data/type/annotation/Field.java). 

The [TestPurchaseOrder](src/test/java/com/uplus/data/type/TestPurchaseOrder.java)
JUnit tests demonstrates the use
of the [@Field](src/main/java/com/pega/sdk/data/type/annotation/Field.java)
with a [PurchaseOrder](src/test/java/com/uplus/data/type/PurchaseOrder.java).
Outside of Launchpad the annotation behaves the same as
[@JsonProperty](https://fasterxml.github.io/jackson-annotations/javadoc/2.6/com/fasterxml/jackson/annotation/JsonProperty.html). 

```java
public class PurchaseOrder {
    @Field(ID = "Quantity", namespace = "UPlus")
    int quantity;
}
```



