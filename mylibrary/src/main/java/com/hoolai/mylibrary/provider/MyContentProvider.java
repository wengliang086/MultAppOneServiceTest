package com.hoolai.mylibrary.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by phoenix on 2017/4/2.
 */

public class MyContentProvider extends ContentProvider {
    //访问表的所有列
    public static final int INCOMING_USER_COLLECTION = 1;
    //访问单独的列
    public static final int INCOMING_USER_SINGLE = 2;
    //操作URI的类
    public static final UriMatcher uriMatcher;

    //为UriMatcher添加自定义的URI
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MyContentProviderMetaData.AUTHORITIES, "/user", INCOMING_USER_COLLECTION);
        uriMatcher.addURI(MyContentProviderMetaData.AUTHORITIES, "/user/#", INCOMING_USER_SINGLE);
    }

    private DatabaseHelper dh;
    //为数据库表字段起别名
    public static HashMap<String, String> userProjectionMap;

    static {
        userProjectionMap = new HashMap<>();
        userProjectionMap.put(MyContentProviderMetaData.UserTableMetaData._ID, MyContentProviderMetaData.UserTableMetaData._ID);
        userProjectionMap.put(MyContentProviderMetaData.UserTableMetaData.USER_NAME, MyContentProviderMetaData.UserTableMetaData.USER_NAME);
    }

    @Override
    public boolean onCreate() {
        //得到数据库帮助类
        dh = new DatabaseHelper(getContext(), MyContentProviderMetaData.DATABASE_NAME);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //创建一个执行查询的Sqlite
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        //判断用户请求，查询所有还是单个
        switch (uriMatcher.match(uri)) {
            case INCOMING_USER_COLLECTION:
                //设置要查询的表名
                qb.setTables(MyContentProviderMetaData.UserTableMetaData.TABLE_NAME);
                qb.setProjectionMap(userProjectionMap);//设置表字段的别名
                break;
            case INCOMING_USER_SINGLE:
                qb.setTables(MyContentProviderMetaData.UserTableMetaData.TABLE_NAME);
                qb.setProjectionMap(userProjectionMap);
                // 追加条件,getPathSegments()得到用户请求的Uri地址截取的数组，
                // get(1)得到去掉地址中/以后的第二个元素
                qb.appendWhere(MyContentProviderMetaData.UserTableMetaData._ID + "=" + uri.getPathSegments().get(1));
                break;
        }
        //设置排序
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = MyContentProviderMetaData.UserTableMetaData.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }
        //得到一个可读的数据库
        SQLiteDatabase db = dh.getReadableDatabase();
        //执行查询，把输入传入
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
        //设置监听
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        //根据用户请求，得到数据类型
        switch (uriMatcher.match(uri)) {
            case INCOMING_USER_COLLECTION:
                return MyContentProviderMetaData.UserTableMetaData.CONTENT_TYPE;
            case INCOMING_USER_SINGLE:
                return MyContentProviderMetaData.UserTableMetaData.CONTENT_TYPE_ITEM;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        //得到一个可写的数据库
        SQLiteDatabase db = dh.getWritableDatabase();
        //向指定的表插入数据，得到返回的Id
        long rowId = db.insert(MyContentProviderMetaData.UserTableMetaData.TABLE_NAME, null, values);
        if (rowId > 0) { // 判断插入是否执行成功
            //如果添加成功，利用新添加的Id和
            Uri insertedUserUri = ContentUris.withAppendedId(MyContentProviderMetaData.UserTableMetaData.CONTENT_URI, rowId);
            //通知监听器，数据已经改变
            getContext().getContentResolver().notifyChange(insertedUserUri, null);
            return insertedUserUri;
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        //得到一个可写的数据库
        SQLiteDatabase db = dh.getWritableDatabase();
        //执行删除，得到删除的行数
        int count = db.delete(MyContentProviderMetaData.UserTableMetaData.TABLE_NAME, selection, selectionArgs);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        //得到一个可写的数据库
        SQLiteDatabase db = dh.getWritableDatabase();
        //执行更新语句，得到更新的条数
        int count = db.update(MyContentProviderMetaData.UserTableMetaData.TABLE_NAME, values, selection, selectionArgs);
        return count;
    }
}
