package com.example.eatitapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import androidx.annotation.Nullable;

import com.example.eatitapp.Model.OrderDetail;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "EatItDB.db";
    private static final int DB_VER = 1;


    public Database(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VER);
    }


    public ArrayList<OrderDetail> getCarts() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"FoodID", "Quantity", "UnitSellingPrice", "Discount"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);
        final ArrayList<OrderDetail> result = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                result.add(new OrderDetail(c.getString(c.getColumnIndex("FoodID")),
                        c.getInt(c.getColumnIndex("Quantity")),
                        c.getFloat(c.getColumnIndex("UnitSellingPrice")),
                        c.getFloat(c.getColumnIndex("Discount"))));
            } while (c.moveToNext());
        }
        return result;
    }

    public OrderDetail getCartItem(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("Select * from OrderDetail where FoodID = %s", id);
        Cursor  c = db.rawQuery(query, null);
        c.moveToFirst();
        OrderDetail item = new OrderDetail(c.getString(c.getColumnIndex("FoodID")),
                c.getInt(c.getColumnIndex("Quantity")),
                c.getFloat(c.getColumnIndex("UnitSellingPrice")),
                c.getFloat(c.getColumnIndex("Discount")));
        return  item;

    }

    public void addToCart(OrderDetail orderDetail) {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail (FoodID, Quantity, UnitSellingPrice, Discount) VALUES('%s', %d, %f, %f);",
                orderDetail.getFoodID(),
                orderDetail.getQuantity(),
                orderDetail.getUnitSellingPrice(),
                orderDetail.getDiscount());
        db.execSQL(query);
    }
    public void updateCartQuantity(String id, int quantity) {

        SQLiteDatabase db = getReadableDatabase();
        String query;
        query = String.format("Update OrderDetail set Quantity = Quantity + %d where FoodID = '%s'", quantity, id);
        db.execSQL(query);
    }
    public void deleteCartItem(String foodID) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("Delete from OrderDetail where FoodID = '%s'", foodID);
        db.execSQL(query);
    }
    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }
}
