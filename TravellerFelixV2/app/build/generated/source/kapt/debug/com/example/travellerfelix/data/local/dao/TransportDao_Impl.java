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
import com.example.travellerfelix.data.local.model.Transport;
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
public final class TransportDao_Impl implements TransportDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Transport> __insertionAdapterOfTransport;

  private final EntityDeletionOrUpdateAdapter<Transport> __deletionAdapterOfTransport;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public TransportDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTransport = new EntityInsertionAdapter<Transport>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `transport` (`id`,`dayPlanId`,`transportType`,`ticketName`,`ticketDate`,`reservationTime`,`departureTime`,`otherTransportType`,`city`,`country`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Transport entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDayPlanId());
        if (entity.getTransportType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTransportType());
        }
        if (entity.getTicketName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTicketName());
        }
        if (entity.getTicketDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTicketDate());
        }
        if (entity.getReservationTime() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getReservationTime());
        }
        if (entity.getDepartureTime() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDepartureTime());
        }
        if (entity.getOtherTransportType() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getOtherTransportType());
        }
        if (entity.getCity() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getCity());
        }
        if (entity.getCountry() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getCountry());
        }
      }
    };
    this.__deletionAdapterOfTransport = new EntityDeletionOrUpdateAdapter<Transport>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `transport` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Transport entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM transport";
        return _query;
      }
    };
  }

  @Override
  public Object insertTransport(final Transport transport,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTransport.insertAndReturnId(transport);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTransport(final Transport transport,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfTransport.handle(transport);
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
  public Flow<List<Transport>> getTransportByDayPlan(final int dayPlanId) {
    final String _sql = "SELECT * FROM transport WHERE dayPlanId = ? ORDER BY ticketDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, dayPlanId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transport"}, new Callable<List<Transport>>() {
      @Override
      @NonNull
      public List<Transport> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDayPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayPlanId");
          final int _cursorIndexOfTransportType = CursorUtil.getColumnIndexOrThrow(_cursor, "transportType");
          final int _cursorIndexOfTicketName = CursorUtil.getColumnIndexOrThrow(_cursor, "ticketName");
          final int _cursorIndexOfTicketDate = CursorUtil.getColumnIndexOrThrow(_cursor, "ticketDate");
          final int _cursorIndexOfReservationTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reservationTime");
          final int _cursorIndexOfDepartureTime = CursorUtil.getColumnIndexOrThrow(_cursor, "departureTime");
          final int _cursorIndexOfOtherTransportType = CursorUtil.getColumnIndexOrThrow(_cursor, "otherTransportType");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfCountry = CursorUtil.getColumnIndexOrThrow(_cursor, "country");
          final List<Transport> _result = new ArrayList<Transport>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Transport _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpDayPlanId;
            _tmpDayPlanId = _cursor.getInt(_cursorIndexOfDayPlanId);
            final String _tmpTransportType;
            if (_cursor.isNull(_cursorIndexOfTransportType)) {
              _tmpTransportType = null;
            } else {
              _tmpTransportType = _cursor.getString(_cursorIndexOfTransportType);
            }
            final String _tmpTicketName;
            if (_cursor.isNull(_cursorIndexOfTicketName)) {
              _tmpTicketName = null;
            } else {
              _tmpTicketName = _cursor.getString(_cursorIndexOfTicketName);
            }
            final String _tmpTicketDate;
            if (_cursor.isNull(_cursorIndexOfTicketDate)) {
              _tmpTicketDate = null;
            } else {
              _tmpTicketDate = _cursor.getString(_cursorIndexOfTicketDate);
            }
            final String _tmpReservationTime;
            if (_cursor.isNull(_cursorIndexOfReservationTime)) {
              _tmpReservationTime = null;
            } else {
              _tmpReservationTime = _cursor.getString(_cursorIndexOfReservationTime);
            }
            final String _tmpDepartureTime;
            if (_cursor.isNull(_cursorIndexOfDepartureTime)) {
              _tmpDepartureTime = null;
            } else {
              _tmpDepartureTime = _cursor.getString(_cursorIndexOfDepartureTime);
            }
            final String _tmpOtherTransportType;
            if (_cursor.isNull(_cursorIndexOfOtherTransportType)) {
              _tmpOtherTransportType = null;
            } else {
              _tmpOtherTransportType = _cursor.getString(_cursorIndexOfOtherTransportType);
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
            _item = new Transport(_tmpId,_tmpDayPlanId,_tmpTransportType,_tmpTicketName,_tmpTicketDate,_tmpReservationTime,_tmpDepartureTime,_tmpOtherTransportType,_tmpCity,_tmpCountry);
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
  public Flow<List<Transport>> getTransportsByDate(final String date) {
    final String _sql = "SELECT * FROM transport WHERE ticketDate = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, date);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transport"}, new Callable<List<Transport>>() {
      @Override
      @NonNull
      public List<Transport> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDayPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayPlanId");
          final int _cursorIndexOfTransportType = CursorUtil.getColumnIndexOrThrow(_cursor, "transportType");
          final int _cursorIndexOfTicketName = CursorUtil.getColumnIndexOrThrow(_cursor, "ticketName");
          final int _cursorIndexOfTicketDate = CursorUtil.getColumnIndexOrThrow(_cursor, "ticketDate");
          final int _cursorIndexOfReservationTime = CursorUtil.getColumnIndexOrThrow(_cursor, "reservationTime");
          final int _cursorIndexOfDepartureTime = CursorUtil.getColumnIndexOrThrow(_cursor, "departureTime");
          final int _cursorIndexOfOtherTransportType = CursorUtil.getColumnIndexOrThrow(_cursor, "otherTransportType");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfCountry = CursorUtil.getColumnIndexOrThrow(_cursor, "country");
          final List<Transport> _result = new ArrayList<Transport>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Transport _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpDayPlanId;
            _tmpDayPlanId = _cursor.getInt(_cursorIndexOfDayPlanId);
            final String _tmpTransportType;
            if (_cursor.isNull(_cursorIndexOfTransportType)) {
              _tmpTransportType = null;
            } else {
              _tmpTransportType = _cursor.getString(_cursorIndexOfTransportType);
            }
            final String _tmpTicketName;
            if (_cursor.isNull(_cursorIndexOfTicketName)) {
              _tmpTicketName = null;
            } else {
              _tmpTicketName = _cursor.getString(_cursorIndexOfTicketName);
            }
            final String _tmpTicketDate;
            if (_cursor.isNull(_cursorIndexOfTicketDate)) {
              _tmpTicketDate = null;
            } else {
              _tmpTicketDate = _cursor.getString(_cursorIndexOfTicketDate);
            }
            final String _tmpReservationTime;
            if (_cursor.isNull(_cursorIndexOfReservationTime)) {
              _tmpReservationTime = null;
            } else {
              _tmpReservationTime = _cursor.getString(_cursorIndexOfReservationTime);
            }
            final String _tmpDepartureTime;
            if (_cursor.isNull(_cursorIndexOfDepartureTime)) {
              _tmpDepartureTime = null;
            } else {
              _tmpDepartureTime = _cursor.getString(_cursorIndexOfDepartureTime);
            }
            final String _tmpOtherTransportType;
            if (_cursor.isNull(_cursorIndexOfOtherTransportType)) {
              _tmpOtherTransportType = null;
            } else {
              _tmpOtherTransportType = _cursor.getString(_cursorIndexOfOtherTransportType);
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
            _item = new Transport(_tmpId,_tmpDayPlanId,_tmpTransportType,_tmpTicketName,_tmpTicketDate,_tmpReservationTime,_tmpDepartureTime,_tmpOtherTransportType,_tmpCity,_tmpCountry);
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
