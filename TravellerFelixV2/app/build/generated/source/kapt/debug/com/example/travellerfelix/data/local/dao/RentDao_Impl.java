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
import com.example.travellerfelix.data.local.model.Rent;
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
public final class RentDao_Impl implements RentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Rent> __insertionAdapterOfRent;

  private final EntityDeletionOrUpdateAdapter<Rent> __deletionAdapterOfRent;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public RentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRent = new EntityInsertionAdapter<Rent>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `rent` (`id`,`dayPlanId`,`rentalCompany`,`pickupDate`,`dropOffDate`,`pickUpTime`,`dropOffTime`,`pickupLocation`,`dropOffLocation`,`carModel`,`city`,`country`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Rent entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDayPlanId());
        if (entity.getRentalCompany() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getRentalCompany());
        }
        if (entity.getPickupDate() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPickupDate());
        }
        if (entity.getDropOffDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDropOffDate());
        }
        if (entity.getPickUpTime() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPickUpTime());
        }
        if (entity.getDropOffTime() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDropOffTime());
        }
        if (entity.getPickupLocation() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getPickupLocation());
        }
        if (entity.getDropOffLocation() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDropOffLocation());
        }
        if (entity.getCarModel() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getCarModel());
        }
        if (entity.getCity() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getCity());
        }
        if (entity.getCountry() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getCountry());
        }
      }
    };
    this.__deletionAdapterOfRent = new EntityDeletionOrUpdateAdapter<Rent>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `rent` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Rent entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM rent";
        return _query;
      }
    };
  }

  @Override
  public Object insertRent(final Rent rent, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRent.insertAndReturnId(rent);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteRent(final Rent rent, final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __deletionAdapterOfRent.handle(rent);
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
  public Flow<List<Rent>> getRentsByDayPlan(final int dayPlanId) {
    final String _sql = "SELECT * FROM rent WHERE dayPlanId = ? ORDER BY pickupDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, dayPlanId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rent"}, new Callable<List<Rent>>() {
      @Override
      @NonNull
      public List<Rent> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDayPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayPlanId");
          final int _cursorIndexOfRentalCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "rentalCompany");
          final int _cursorIndexOfPickupDate = CursorUtil.getColumnIndexOrThrow(_cursor, "pickupDate");
          final int _cursorIndexOfDropOffDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dropOffDate");
          final int _cursorIndexOfPickUpTime = CursorUtil.getColumnIndexOrThrow(_cursor, "pickUpTime");
          final int _cursorIndexOfDropOffTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dropOffTime");
          final int _cursorIndexOfPickupLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "pickupLocation");
          final int _cursorIndexOfDropOffLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "dropOffLocation");
          final int _cursorIndexOfCarModel = CursorUtil.getColumnIndexOrThrow(_cursor, "carModel");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfCountry = CursorUtil.getColumnIndexOrThrow(_cursor, "country");
          final List<Rent> _result = new ArrayList<Rent>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Rent _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpDayPlanId;
            _tmpDayPlanId = _cursor.getInt(_cursorIndexOfDayPlanId);
            final String _tmpRentalCompany;
            if (_cursor.isNull(_cursorIndexOfRentalCompany)) {
              _tmpRentalCompany = null;
            } else {
              _tmpRentalCompany = _cursor.getString(_cursorIndexOfRentalCompany);
            }
            final String _tmpPickupDate;
            if (_cursor.isNull(_cursorIndexOfPickupDate)) {
              _tmpPickupDate = null;
            } else {
              _tmpPickupDate = _cursor.getString(_cursorIndexOfPickupDate);
            }
            final String _tmpDropOffDate;
            if (_cursor.isNull(_cursorIndexOfDropOffDate)) {
              _tmpDropOffDate = null;
            } else {
              _tmpDropOffDate = _cursor.getString(_cursorIndexOfDropOffDate);
            }
            final String _tmpPickUpTime;
            if (_cursor.isNull(_cursorIndexOfPickUpTime)) {
              _tmpPickUpTime = null;
            } else {
              _tmpPickUpTime = _cursor.getString(_cursorIndexOfPickUpTime);
            }
            final String _tmpDropOffTime;
            if (_cursor.isNull(_cursorIndexOfDropOffTime)) {
              _tmpDropOffTime = null;
            } else {
              _tmpDropOffTime = _cursor.getString(_cursorIndexOfDropOffTime);
            }
            final String _tmpPickupLocation;
            if (_cursor.isNull(_cursorIndexOfPickupLocation)) {
              _tmpPickupLocation = null;
            } else {
              _tmpPickupLocation = _cursor.getString(_cursorIndexOfPickupLocation);
            }
            final String _tmpDropOffLocation;
            if (_cursor.isNull(_cursorIndexOfDropOffLocation)) {
              _tmpDropOffLocation = null;
            } else {
              _tmpDropOffLocation = _cursor.getString(_cursorIndexOfDropOffLocation);
            }
            final String _tmpCarModel;
            if (_cursor.isNull(_cursorIndexOfCarModel)) {
              _tmpCarModel = null;
            } else {
              _tmpCarModel = _cursor.getString(_cursorIndexOfCarModel);
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
            _item = new Rent(_tmpId,_tmpDayPlanId,_tmpRentalCompany,_tmpPickupDate,_tmpDropOffDate,_tmpPickUpTime,_tmpDropOffTime,_tmpPickupLocation,_tmpDropOffLocation,_tmpCarModel,_tmpCity,_tmpCountry);
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
  public Flow<List<Rent>> getRentsByDate(final String date) {
    final String _sql = "\n"
            + "    SELECT * FROM rent \n"
            + "    INNER JOIN day_plan ON rent.dayPlanId = day_plan.id \n"
            + "    WHERE ? BETWEEN rent.pickupDate AND rent.dropOffDate\n";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, date);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rent",
        "day_plan"}, new Callable<List<Rent>>() {
      @Override
      @NonNull
      public List<Rent> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDayPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayPlanId");
          final int _cursorIndexOfRentalCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "rentalCompany");
          final int _cursorIndexOfPickupDate = CursorUtil.getColumnIndexOrThrow(_cursor, "pickupDate");
          final int _cursorIndexOfDropOffDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dropOffDate");
          final int _cursorIndexOfPickUpTime = CursorUtil.getColumnIndexOrThrow(_cursor, "pickUpTime");
          final int _cursorIndexOfDropOffTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dropOffTime");
          final int _cursorIndexOfPickupLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "pickupLocation");
          final int _cursorIndexOfDropOffLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "dropOffLocation");
          final int _cursorIndexOfCarModel = CursorUtil.getColumnIndexOrThrow(_cursor, "carModel");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfCountry = CursorUtil.getColumnIndexOrThrow(_cursor, "country");
          final List<Rent> _result = new ArrayList<Rent>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Rent _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpDayPlanId;
            _tmpDayPlanId = _cursor.getInt(_cursorIndexOfDayPlanId);
            final String _tmpRentalCompany;
            if (_cursor.isNull(_cursorIndexOfRentalCompany)) {
              _tmpRentalCompany = null;
            } else {
              _tmpRentalCompany = _cursor.getString(_cursorIndexOfRentalCompany);
            }
            final String _tmpPickupDate;
            if (_cursor.isNull(_cursorIndexOfPickupDate)) {
              _tmpPickupDate = null;
            } else {
              _tmpPickupDate = _cursor.getString(_cursorIndexOfPickupDate);
            }
            final String _tmpDropOffDate;
            if (_cursor.isNull(_cursorIndexOfDropOffDate)) {
              _tmpDropOffDate = null;
            } else {
              _tmpDropOffDate = _cursor.getString(_cursorIndexOfDropOffDate);
            }
            final String _tmpPickUpTime;
            if (_cursor.isNull(_cursorIndexOfPickUpTime)) {
              _tmpPickUpTime = null;
            } else {
              _tmpPickUpTime = _cursor.getString(_cursorIndexOfPickUpTime);
            }
            final String _tmpDropOffTime;
            if (_cursor.isNull(_cursorIndexOfDropOffTime)) {
              _tmpDropOffTime = null;
            } else {
              _tmpDropOffTime = _cursor.getString(_cursorIndexOfDropOffTime);
            }
            final String _tmpPickupLocation;
            if (_cursor.isNull(_cursorIndexOfPickupLocation)) {
              _tmpPickupLocation = null;
            } else {
              _tmpPickupLocation = _cursor.getString(_cursorIndexOfPickupLocation);
            }
            final String _tmpDropOffLocation;
            if (_cursor.isNull(_cursorIndexOfDropOffLocation)) {
              _tmpDropOffLocation = null;
            } else {
              _tmpDropOffLocation = _cursor.getString(_cursorIndexOfDropOffLocation);
            }
            final String _tmpCarModel;
            if (_cursor.isNull(_cursorIndexOfCarModel)) {
              _tmpCarModel = null;
            } else {
              _tmpCarModel = _cursor.getString(_cursorIndexOfCarModel);
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
            _item = new Rent(_tmpId,_tmpDayPlanId,_tmpRentalCompany,_tmpPickupDate,_tmpDropOffDate,_tmpPickUpTime,_tmpDropOffTime,_tmpPickupLocation,_tmpDropOffLocation,_tmpCarModel,_tmpCity,_tmpCountry);
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
  public Object getAllRents(final Continuation<? super List<Rent>> $completion) {
    final String _sql = "SELECT * FROM rent";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Rent>>() {
      @Override
      @NonNull
      public List<Rent> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDayPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "dayPlanId");
          final int _cursorIndexOfRentalCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "rentalCompany");
          final int _cursorIndexOfPickupDate = CursorUtil.getColumnIndexOrThrow(_cursor, "pickupDate");
          final int _cursorIndexOfDropOffDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dropOffDate");
          final int _cursorIndexOfPickUpTime = CursorUtil.getColumnIndexOrThrow(_cursor, "pickUpTime");
          final int _cursorIndexOfDropOffTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dropOffTime");
          final int _cursorIndexOfPickupLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "pickupLocation");
          final int _cursorIndexOfDropOffLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "dropOffLocation");
          final int _cursorIndexOfCarModel = CursorUtil.getColumnIndexOrThrow(_cursor, "carModel");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfCountry = CursorUtil.getColumnIndexOrThrow(_cursor, "country");
          final List<Rent> _result = new ArrayList<Rent>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Rent _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpDayPlanId;
            _tmpDayPlanId = _cursor.getInt(_cursorIndexOfDayPlanId);
            final String _tmpRentalCompany;
            if (_cursor.isNull(_cursorIndexOfRentalCompany)) {
              _tmpRentalCompany = null;
            } else {
              _tmpRentalCompany = _cursor.getString(_cursorIndexOfRentalCompany);
            }
            final String _tmpPickupDate;
            if (_cursor.isNull(_cursorIndexOfPickupDate)) {
              _tmpPickupDate = null;
            } else {
              _tmpPickupDate = _cursor.getString(_cursorIndexOfPickupDate);
            }
            final String _tmpDropOffDate;
            if (_cursor.isNull(_cursorIndexOfDropOffDate)) {
              _tmpDropOffDate = null;
            } else {
              _tmpDropOffDate = _cursor.getString(_cursorIndexOfDropOffDate);
            }
            final String _tmpPickUpTime;
            if (_cursor.isNull(_cursorIndexOfPickUpTime)) {
              _tmpPickUpTime = null;
            } else {
              _tmpPickUpTime = _cursor.getString(_cursorIndexOfPickUpTime);
            }
            final String _tmpDropOffTime;
            if (_cursor.isNull(_cursorIndexOfDropOffTime)) {
              _tmpDropOffTime = null;
            } else {
              _tmpDropOffTime = _cursor.getString(_cursorIndexOfDropOffTime);
            }
            final String _tmpPickupLocation;
            if (_cursor.isNull(_cursorIndexOfPickupLocation)) {
              _tmpPickupLocation = null;
            } else {
              _tmpPickupLocation = _cursor.getString(_cursorIndexOfPickupLocation);
            }
            final String _tmpDropOffLocation;
            if (_cursor.isNull(_cursorIndexOfDropOffLocation)) {
              _tmpDropOffLocation = null;
            } else {
              _tmpDropOffLocation = _cursor.getString(_cursorIndexOfDropOffLocation);
            }
            final String _tmpCarModel;
            if (_cursor.isNull(_cursorIndexOfCarModel)) {
              _tmpCarModel = null;
            } else {
              _tmpCarModel = _cursor.getString(_cursorIndexOfCarModel);
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
            _item = new Rent(_tmpId,_tmpDayPlanId,_tmpRentalCompany,_tmpPickupDate,_tmpDropOffDate,_tmpPickUpTime,_tmpDropOffTime,_tmpPickupLocation,_tmpDropOffLocation,_tmpCarModel,_tmpCity,_tmpCountry);
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
