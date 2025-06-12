package com.example.travellerfelix.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.travellerfelix.data.local.model.Restaurant;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RestaurantDao_Impl implements RestaurantDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Restaurant> __insertionAdapterOfRestaurant;

  private final EntityDeletionOrUpdateAdapter<Restaurant> __deletionAdapterOfRestaurant;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public RestaurantDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRestaurant = new EntityInsertionAdapter<Restaurant>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `restaurant` (`id`,`dayPlanId`,`name`,`reservationDate`,`reservationTime`,`openCloseTime`,`city`,`country`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Restaurant entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDayPlanId());
        if (entity.getName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getName());
        }
        if (entity.getReservationDate() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getReservationDate());
        }
        if (entity.getReservationTime() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getReservationTime());
        }
        if (entity.getOpenCloseTime() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getOpenCloseTime());
        }
        if (entity.getCity() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCity());
        }
        if (entity.getCountry() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getCountry());
        }
      }
    };
    this.__deletionAdapterOfRestaurant = new EntityDeletionOrUpdateAdapter<Restaurant>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `restaurant` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Restaurant entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM restaurant";
        return _query;
      }
    };
  }

  @Override
  public Object insertRestaurant(final Restaurant restaurant,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRestaurant.insertAndReturnId(restaurant);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteRestaurant(final Restaurant restaurant,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfRestaurant.handle(restaurant);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Restaurant>> getRestaurantsByDayPlan(final int dayPlanId) {
    final String _sql = "SELECT * FROM restaurant WHERE dayPlanId = ? ORDER BY reservationTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, dayPlanId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"restaurant"}, new Callable<List<Restaurant>>() {
      @Override
      @NonNull
      public List<Restaurant> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDayPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayPlanId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfReservationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "reservationDate");
          final int _cursorIndexOfReservationTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reservationTime");
          final int _cursorIndexOfOpenCloseTime = CursorUtil.getColumnIndexOrThrow(_cursor, "openCloseTime");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfCountry = CursorUtil.getColumnIndexOrThrow(_cursor, "country");
          final List<Restaurant> _result = new ArrayList<Restaurant>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Restaurant _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpDayPlanId;
            _tmpDayPlanId = _cursor.getInt(_cursorIndexOfDayPlanId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpReservationDate;
            if (_cursor.isNull(_cursorIndexOfReservationDate)) {
              _tmpReservationDate = null;
            } else {
              _tmpReservationDate = _cursor.getString(_cursorIndexOfReservationDate);
            }
            final String _tmpReservationTime;
            if (_cursor.isNull(_cursorIndexOfReservationTime)) {
              _tmpReservationTime = null;
            } else {
              _tmpReservationTime = _cursor.getString(_cursorIndexOfReservationTime);
            }
            final String _tmpOpenCloseTime;
            if (_cursor.isNull(_cursorIndexOfOpenCloseTime)) {
              _tmpOpenCloseTime = null;
            } else {
              _tmpOpenCloseTime = _cursor.getString(_cursorIndexOfOpenCloseTime);
            }
            final String _tmpCity;
            if (_cursor.isNull(_cursorIndexOfCity)) {
              _tmpCity = null;
            } else {
              _tmpCity = _cursor.getString(_cursorIndexOfCity);
            }
            final String _tmpCountry;
            if (_cursor.isNull(_cursorIndexOfCountry)) {
              _tmpCountry = null;
            } else {
              _tmpCountry = _cursor.getString(_cursorIndexOfCountry);
            }
            _item = new Restaurant(_tmpId,_tmpDayPlanId,_tmpName,_tmpReservationDate,_tmpReservationTime,_tmpOpenCloseTime,_tmpCity,_tmpCountry);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Restaurant>> getRestaurantsByDate(final String date) {
    final String _sql = "SELECT * FROM restaurant WHERE reservationDate = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, date);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"restaurant"}, new Callable<List<Restaurant>>() {
      @Override
      @NonNull
      public List<Restaurant> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDayPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayPlanId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfReservationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "reservationDate");
          final int _cursorIndexOfReservationTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reservationTime");
          final int _cursorIndexOfOpenCloseTime = CursorUtil.getColumnIndexOrThrow(_cursor, "openCloseTime");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfCountry = CursorUtil.getColumnIndexOrThrow(_cursor, "country");
          final List<Restaurant> _result = new ArrayList<Restaurant>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Restaurant _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpDayPlanId;
            _tmpDayPlanId = _cursor.getInt(_cursorIndexOfDayPlanId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpReservationDate;
            if (_cursor.isNull(_cursorIndexOfReservationDate)) {
              _tmpReservationDate = null;
            } else {
              _tmpReservationDate = _cursor.getString(_cursorIndexOfReservationDate);
            }
            final String _tmpReservationTime;
            if (_cursor.isNull(_cursorIndexOfReservationTime)) {
              _tmpReservationTime = null;
            } else {
              _tmpReservationTime = _cursor.getString(_cursorIndexOfReservationTime);
            }
            final String _tmpOpenCloseTime;
            if (_cursor.isNull(_cursorIndexOfOpenCloseTime)) {
              _tmpOpenCloseTime = null;
            } else {
              _tmpOpenCloseTime = _cursor.getString(_cursorIndexOfOpenCloseTime);
            }
            final String _tmpCity;
            if (_cursor.isNull(_cursorIndexOfCity)) {
              _tmpCity = null;
            } else {
              _tmpCity = _cursor.getString(_cursorIndexOfCity);
            }
            final String _tmpCountry;
            if (_cursor.isNull(_cursorIndexOfCountry)) {
              _tmpCountry = null;
            } else {
              _tmpCountry = _cursor.getString(_cursorIndexOfCountry);
            }
            _item = new Restaurant(_tmpId,_tmpDayPlanId,_tmpName,_tmpReservationDate,_tmpReservationTime,_tmpOpenCloseTime,_tmpCity,_tmpCountry);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
