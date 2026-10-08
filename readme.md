# Product Management System

A JavaFX product management project backed by Oracle XE.

## Prerequisites

- JDK 17
- Oracle XE
- Maven (needed to install the backend artifact if it is not already in your local Maven repository)

## Database setup

The workspace contains these scripts under `database/`:

1. Run `schema.sql` to create the CATEGORY, SUPPLIER, and PRODUCT tables.
2. Run `insert.sql` to add the sample categories, suppliers, and products. The script notes that parent rows are inserted before product rows.

The Java connector settings are in `backend/src/main/java/com/pms/DatabaseConnector.java`. Check that they match the Oracle XE service and account configured on your machine; this repository does not include a separate database setup guide.

## Run the JavaFX app

From the `frontend` folder, install the backend module into your local Maven repository once if it is not already available:

```powershell
mvn -f ..\backend\pom.xml install
```

Then launch the app from `frontend`:

```powershell
.\mvnw.cmd clean javafx:run
```

The Maven wrapper is included in this folder. The app loads products from Oracle when it starts and shows a loading indicator while database work is running.

## Manual test checklist

- [ ] Search products by a numeric ID and confirm matching results appear.
- [ ] Search by part or all of a product name and confirm results filter while typing.
- [ ] Select a result and confirm the product details are displayed and Delete becomes enabled.
- [ ] Start deletion and choose **No**; confirm the product remains in the list.
- [ ] Confirm deletion; confirm a success message appears and the product disappears from the refreshed list.
- [ ] Observe the loading indicator during initial database loading and deletion/refresh.
- [ ] Stop Oracle XE or use unavailable database settings; confirm the screen shows a readable load error instead of closing unexpectedly.