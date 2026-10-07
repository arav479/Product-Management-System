package com.pms;

/*
 * TEMPORARY frontend copy of the Category model from the shared contract
 * (IMPLEMENTATION_PLAN.md). Delete this file once P1's backend jar provides
 * com.pms.Category, otherwise this copy hides the real one.
 */
public class Category {

    public int categoryId;
    public String categoryName;

    public Category(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    // ComboBox<Category> shows this text
    @Override
    public String toString() {
        return categoryName;
    }
}
