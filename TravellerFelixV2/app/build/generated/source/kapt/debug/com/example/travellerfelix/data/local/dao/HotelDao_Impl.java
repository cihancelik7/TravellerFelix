package com.example.travellerfelix.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
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
import com.example.travellerfelix.data.local.model.Hotel;
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
public final class HotelDao_Impl implements HotelDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Hotel> __insertionAdapterOfHotel;

  private final EntityDeletionOrUpdateAdapter<Hotel> __deletionAdapterOfHotel;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public HotelDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHotel = new EntityInsertionAdapter<Hotel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `hotel` (`id`,`dayPlanId`,`name`,`checkInDate`,`checkOutDate`,`checkInTime`,`checkOutTime`,`city`,`country`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Hotel entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDayPlanId());
        if (entity.getName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getName());
        }
        if (entity.getCheckInDate() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCheckInDate());
        }
        if (entity.getCheckOutDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCheckOutDate());
        }
        if (entity.getCheckInTime() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCheckInTime());
        }
        if (entity.getCheckOutTime() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCheckOutTime());
        }
        if (entity.getCity() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getCity());
        }
        if (entity.getCountry() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getCountry());
        }
      }
    };
    this.__deletionAdapterOfHotel = new EntityDeletionOrUpdateAdapter<Hotel>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `hotel` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Hotel entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM hotel";
        return _query;
      }
    };
  }

  @Override
  public Object insertHotel(final Hotel hotel, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfHotel.insertAndReturnId(hotel);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteHotel(final Hotel hotel, final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfHotel.handle(hotel);
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
  public Flow<List<Hotel>> getHotelsByDayPlan(final int dayPlanId) {
    final String _sql = "SELECT * FROM hotel WHERE dayPlanId = ? ORDER BY checkInDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, dayPlanId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"hotel"}, new Callable<List<Hotel>>() {
      @Override
      @NonNull
      public List<Hotel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDayPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayPlanId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCheckInDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkInDate");
          final int _cursorIndexOfCheckOutDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkOutDate");
          final int _cursorIndexOfCheckInTime = CursorUtil.getColumnIndexOrThrow(_cursor, "checkInTime");
          final int _cursorIndexOfCheckOutTime = CursorUtil.getColumnIndexOrThrow(_cursor, "checkOutTime");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfCountry = CursorUtil.getColumnIndexOrThrow(_cursor, "country");
          final List<Hotel> _result = new ArrayList<Hotel>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Hotel _item;
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
            final String _tmpCheckInDate;
            if (_cursor.isNull(_cursorIndexOfCheckInDate)) {
              _tmpCheckInDate = null;
            } else {
              _tmpCheckInDate = _cursor.getString(_cursorIndexOfCheckInDate);
            }
            final String _tmpCheckOutDate;
            if (_cursor.isNull(_cursorIndexOfCheckOutDate)) {
              _tmpCheckOutDate = null;
            } else {
              _tmpCheckOutDate = _cursor.getString(_cursorIndexOfCheckOutDate);
            }
            final String _tmpCheckInTime;
            if (_cursor.isNull(_cursorIndexOfCheckInTime)) {
              _tmpCheckInTime = null;
            } else {
              _tmpCheckInTime = _cursor.getString(_cursorIndexOfCheckInTime);
            }
            final String _tmpCheckOutTime;
            if (_cursor.isNull(_cursorIndexOfCheckOutTime)) {
              _tmpCheckOutTime = null;
            } else {
              _tmpCheckOutTime = _cursor.getString(_cursorIndexOfCheckOutTime);
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
            _item = new Hotel(_tmpId,_tmpDayPlanId,_tmpName,_tmpCheckInDate,_tmpCheckOutDate,_tmpCheckInTime,_tmpCheckOutTime,_tmpCity,_tmpCountry);
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
  public Flow<List<Hotel>> getHotelsByDate(final String date) {
    final String _sql = "\n"
            + "        SELECT * FROM hotel \n"
            + "        INNER JOIN day_plan ON hotel.dayPlanId = day_plan.id \n"
            + "        WHERE ? BETWEEN hotel.checkInDate AND hotel.checkOutDate\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, date);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"hotel",
        "day_plan"}, new Callable<List<Hotel>>() {
      @Override
      @NonNull
      public List<Hotel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDayPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayPlanId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCheckInDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkInDate");
          final int _cursorIndexOfCheckOutDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkOutDate");
          final int _cursorIndexOfCheckInTime = CursorUtil.getColumnIndexOrThrow(_cursor, "checkInTime");
          final int _cursorIndexOfCheckOutTime = CursorUtil.getColumnIndexOrThrow(_cursor, "checkOutTime");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfCountry = CursorUtil.getColumnIndexOrThrow(_cursor, "country");
          final List<Hotel> _result = new ArrayList<Hotel>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Hotel _item;
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
            final String _tmpCheckInDate;
            if (_cursor.isNull(_cursorIndexOfCheckInDate)) {
              _tmpCheckInDate = null;
            } else {
              _tmpCheckInDate = _cursor.getString(_cursorIndexOfCheckInDate);
            }
            final String _tmpCheckOutDate;
            if (_cursor.isNull(_cursorIndexOfCheckOutDate)) {
              _tmpCheckOutDate = null;
            } else {
              _tmpCheckOutDate = _cursor.getString(_cursorIndexOfCheckOutDate);
            }
            final String _tmpCheckInTime;
            if (_cursor.isNull(_cursorIndexOfCheckInTime)) {
              _tmpCheckInTime = null;
            } else {
              _tmpCheckInTime = _cursor.getString(_cursorIndexOfCheckInTime);
            }
            final String _tmpCheckOutTime;
            if (_cursor.isNull(_cursorIndexOfCheckOutTime)) {
              _tmpCheckOutTime = null;
            } else {
              _tmpCheckOutTime = _cursor.getString(_cursorIndexOfCheckOutTime);
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
            _item = new Hotel(_tmpId,_tmpDayPlanId,_tmpName,_tmpCheckInDate,_tmpCheckOutDate,_tmpCheckInTime,_tmpCheckOutTime,_tmpCity,_tmpCountry);
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
  public Object getAllHotels(final Continuation<? super List<Hotel>> $completion) {
    final String _sql = "SELECT * FROM hotel";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Hotel>>() {
      @Override
      @NonNull
      public List<Hotel> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDayPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayPlanId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCheckInDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkInDate");
          final int _cursorIndexOfCheckOutDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkOutDate");
          final int _cursorIndexOfCheckInTime = CursorUtil.getColumnIndexOrThrow(_cursor, "checkInTime");
          final int _cursorIndexOfCheckOutTime = CursorUtil.getColumnIndexOrThrow(_cursor, "checkOutTime");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfCountry = CursorUtil.getColumnIndexOrThrow(_cursor, "country");
          final List<Hotel> _result = new ArrayList<Hotel>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Hotel _item;
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
            final String _tmpCheckInDate;
            if (_cursor.isNull(_cursorIndexOfCheckInDate)) {
              _tmpCheckInDate = null;
            } else {
              _tmpCheckInDate = _cursor.getString(_cursorIndexOfCheckInDate);
            }
            final String _tmpCheckOutDate;
            if (_cursor.isNull(_cursorIndexOfCheckOutDate)) {
              _tmpCheckOutDate = null;
            } else {
              _tmpCheckOutDate = _cursor.getString(_cursorIndexOfCheckOutDate);
            }
            final String _tmpCheckInTime;
            if (_cursor.isNull(_cursorIndexOfCheckInTime)) {
              _tmpCheckInTime = null;
            } else {
              _tmpCheckInTime = _cursor.getString(_cursorIndexOfCheckInTime);
            }
            final String _tmpCheckOutTime;
            if (_cursor.isNull(_cursorIndexOfCheckOutTime)) {
              _tmpCheckOutTime = null;
            } else {
              _tmpCheckOutTime = _cursor.getString(_cursorIndexOfCheckOutTime);
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
            _item = new Hotel(_tmpId,_tmpDayPlanId,_tmpName,_tmpCheckInDate,_tmpCheckOutDate,_tmpCheckInTime,_tmpCheckOutTime,_tmpCity,_tmpCountry);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
