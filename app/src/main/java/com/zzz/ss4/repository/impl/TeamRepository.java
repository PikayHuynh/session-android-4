package com.zzz.ss4.repository.impl;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zzz.ss4.constant.Constant;

public class TeamRepository {
    private static SQLiteDatabase db;

    public static void init(Context context) {
        if (db == null) {
            db = context.openOrCreateDatabase(Constant.DATABASE_NAME, Context.MODE_PRIVATE, null);
        }
    }

    public static SQLiteDatabase getDb() {
        return db;
    }

    public static Cursor findAll() {
        return db.rawQuery("SELECT * FROM " + Constant.FC_TEAM_TABLE, null);
    }

    public static Cursor findById(int id) {
        return db.rawQuery(
                "SELECT * FROM " + Constant.FC_TEAM_TABLE + " WHERE " + Constant.ID + " = ?",
                new String[] {String.valueOf(id)});
    }

    public static Long save(ContentValues values) {
        return db.insert(Constant.FC_TEAM_TABLE, null, values);
    }

    public static Integer save(ContentValues values, int id) {
        return db.update(
                Constant.FC_TEAM_TABLE,
                values,
                Constant.ID + " = ?",
                new String[] {String.valueOf(id)});
    }

    public static Integer delete(int id) {
        return db.delete(Constant.FC_TEAM_TABLE, Constant.ID + " = ?", new String[] {String.valueOf(id)});
    }
}
