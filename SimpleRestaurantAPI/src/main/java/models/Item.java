package models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Item {
    private final UUID itemId;
    private final String itemName;
    private final String itemCookingTime;
    private final int tableNo;
    private final LocalDateTime createdAt;
    private final LocalDateTime removedAt;
    private final boolean isRemoved;

    public Item(
            UUID itemId,
            String itemName,
            String itemCookingTime,
            int tableNo,
            LocalDateTime createdAt,
            LocalDateTime removedAt,
            boolean isRemoved) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemCookingTime = itemCookingTime;
        this.tableNo = tableNo;
        this.createdAt = createdAt;
        this.removedAt = removedAt;
        this.isRemoved = isRemoved;
    }

    public String getItemCookingTime() {
        return itemCookingTime;
    }

    public String getItemName() {
        return itemName;
    }

    public int getTableNo() {
        return tableNo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getRemovedAt() {
        return removedAt;
    }

    public UUID getItemId() {
        return itemId;
    }

    public boolean getIsRemoved() {
        return isRemoved;
    }
}
