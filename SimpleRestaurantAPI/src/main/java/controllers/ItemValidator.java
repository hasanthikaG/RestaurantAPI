package controllers;

import models.Item;

import java.util.function.Function;

import static controllers.ItemValidator.ValidationResult;
import static controllers.ItemValidator.ValidationResult.*;


public interface ItemValidator extends Function<Item, ValidationResult> {

    static ItemValidator isItemNameValid() {
        return item -> item.getItemName().matches("^[a-zA-Z0-9 ]*$") ? SUCCESS : ITEM_NAME_NOT_VALID;
    }

    static ItemValidator isTableNumberExist() {
        return item -> item.getTableNo() < Integer.parseInt(System.getenv("TABLE_COUNT")) ? SUCCESS: TABLE_NUMBER_NOT_EXIST;
    }

    static ItemValidator isTableNumberValid() {
        return item -> item.getTableNo()  > 1 ? SUCCESS : TABLE_NUMBER_NOT_VALID ;
    }

    default ItemValidator and (ItemValidator other) {
        return item -> {
            ValidationResult result = this.apply(item);
            return result.equals(SUCCESS) ? other.apply(item): result;
        };
    }

    enum ValidationResult {
        SUCCESS,
        ITEM_NAME_NOT_VALID,
        TABLE_NUMBER_NOT_EXIST,
        TABLE_NUMBER_NOT_VALID,
    }
}
