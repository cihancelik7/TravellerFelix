package com.example.travellerfelix.data.local.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.travellerfelix.data.local.dao.CityDao;
import com.example.travellerfelix.data.local.dao.CityDao_Impl;
import com.example.travellerfelix.data.local.dao.CountryDao;
import com.example.travellerfelix.data.local.dao.CountryDao_Impl;
import com.example.travellerfelix.data.local.dao.DayPlanDao;
import com.example.travellerfelix.data.local.dao.DayPlanDao_Impl;
import com.example.travellerfelix.data.local.dao.HotelDao;
import com.example.travellerfelix.data.local.dao.HotelDao_Impl;
import com.example.travellerfelix.data.local.dao.MuseumDao;
import com.example.travellerfelix.data.local.dao.MuseumDao_Impl;
import com.example.travellerfelix.data.local.dao.NoteDao;
import com.example.travellerfelix.data.local.dao.NoteDao_Impl;
import com.example.travellerfelix.data.local.dao.PlaceDao;
import com.example.travellerfelix.data.local.dao.PlaceDao_Impl;
import com.example.travellerfelix.data.local.dao.ReminderDao;
import com.example.travellerfelix.data.local.dao.ReminderDao_Impl;
import com.example.travellerfelix.data.local.dao.RentDao;
import com.example.travellerfelix.data.local.dao.RentDao_Impl;
import com.example.travellerfelix.data.local.dao.RestaurantDao;
import com.example.travellerfelix.data.local.dao.RestaurantDao_Impl;
import com.example.travellerfelix.data.local.dao.TransportDao;
import com.example.travellerfelix.data.local.dao.TransportDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TravelDatabase_Impl extends TravelDatabase {
  private volatile CityDao _cityDao;

  private volatile DayPlanDao _dayPlanDao;

  private volatile TransportDao _transportDao;

  private volatile ReminderDao _reminderDao;

  private volatile HotelDao _hotelDao;

  private volatile RentDao _rentDao;

  private volatile MuseumDao _museumDao;

  private volatile RestaurantDao _restaurantDao;

  private volatile CountryDao _countryDao;

  private volatile NoteDao _noteDao;

  private volatile PlaceDao _placeDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `city` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `countryId` INTEGER NOT NULL, `name` TEXT NOT NULL, `date` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `day_plan` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `cityId` INTEGER NOT NULL, `date` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Country` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `date` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `rent` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dayPlanId` INTEGER NOT NULL, `rentalCompany` TEXT NOT NULL, `pickupDate` TEXT NOT NULL, `dropOffDate` TEXT NOT NULL, `pickUpTime` TEXT NOT NULL, `dropOffTime` TEXT NOT NULL, `pickupLocation` TEXT NOT NULL, `dropOffLocation` TEXT NOT NULL, `carModel` TEXT NOT NULL, `city` TEXT NOT NULL, `country` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `museum` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dayPlanId` INTEGER NOT NULL, `name` TEXT NOT NULL, `visitDate` TEXT NOT NULL, `visitTime` TEXT NOT NULL, `openCloseTime` TEXT NOT NULL, `city` TEXT NOT NULL, `country` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `transport` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dayPlanId` INTEGER NOT NULL, `transportType` TEXT NOT NULL, `ticketName` TEXT NOT NULL, `ticketDate` TEXT NOT NULL, `reservationTime` TEXT NOT NULL, `departureTime` TEXT NOT NULL, `otherTransportType` TEXT, `city` TEXT NOT NULL, `country` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reminder` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dayPlanId` INTEGER NOT NULL, `title` TEXT NOT NULL, `date` TEXT NOT NULL, `time` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `restaurant` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dayPlanId` INTEGER NOT NULL, `name` TEXT NOT NULL, `reservationDate` TEXT NOT NULL, `reservationTime` TEXT NOT NULL, `openCloseTime` TEXT NOT NULL, `city` TEXT NOT NULL, `country` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `hotel` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dayPlanId` INTEGER NOT NULL, `name` TEXT NOT NULL, `checkInDate` TEXT NOT NULL, `checkOutDate` TEXT NOT NULL, `checkInTime` TEXT NOT NULL, `checkOutTime` TEXT NOT NULL, `city` TEXT NOT NULL, `country` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `notes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `content` TEXT NOT NULL, `createdAt` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `place` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dayPlanId` INTEGER NOT NULL, `name` TEXT NOT NULL, `type` TEXT NOT NULL, `openTime` TEXT NOT NULL, `closeTime` TEXT NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `city` TEXT NOT NULL, `country` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'af1341c138166c9da9140e20a26a4a40')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `city`");
        db.execSQL("DROP TABLE IF EXISTS `day_plan`");
        db.execSQL("DROP TABLE IF EXISTS `Country`");
        db.execSQL("DROP TABLE IF EXISTS `rent`");
        db.execSQL("DROP TABLE IF EXISTS `museum`");
        db.execSQL("DROP TABLE IF EXISTS `transport`");
        db.execSQL("DROP TABLE IF EXISTS `reminder`");
        db.execSQL("DROP TABLE IF EXISTS `restaurant`");
        db.execSQL("DROP TABLE IF EXISTS `hotel`");
        db.execSQL("DROP TABLE IF EXISTS `notes`");
        db.execSQL("DROP TABLE IF EXISTS `place`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsCity = new HashMap<String, TableInfo.Column>(4);
        _columnsCity.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCity.put("countryId", new TableInfo.Column("countryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCity.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCity.put("date", new TableInfo.Column("date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCity = new TableInfo("city", _columnsCity, _foreignKeysCity, _indicesCity);
        final TableInfo _existingCity = TableInfo.read(db, "city");
        if (!_infoCity.equals(_existingCity)) {
          return new RoomOpenHelper.ValidationResult(false, "city(com.example.travellerfelix.data.local.model.City).\n"
                  + " Expected:\n" + _infoCity + "\n"
                  + " Found:\n" + _existingCity);
        }
        final HashMap<String, TableInfo.Column> _columnsDayPlan = new HashMap<String, TableInfo.Column>(3);
        _columnsDayPlan.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayPlan.put("cityId", new TableInfo.Column("cityId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDayPlan.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDayPlan = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDayPlan = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDayPlan = new TableInfo("day_plan", _columnsDayPlan, _foreignKeysDayPlan, _indicesDayPlan);
        final TableInfo _existingDayPlan = TableInfo.read(db, "day_plan");
        if (!_infoDayPlan.equals(_existingDayPlan)) {
          return new RoomOpenHelper.ValidationResult(false, "day_plan(com.example.travellerfelix.data.local.model.DayPlan).\n"
                  + " Expected:\n" + _infoDayPlan + "\n"
                  + " Found:\n" + _existingDayPlan);
        }
        final HashMap<String, TableInfo.Column> _columnsCountry = new HashMap<String, TableInfo.Column>(3);
        _columnsCountry.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountry.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCountry.put("date", new TableInfo.Column("date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCountry = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCountry = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCountry = new TableInfo("Country", _columnsCountry, _foreignKeysCountry, _indicesCountry);
        final TableInfo _existingCountry = TableInfo.read(db, "Country");
        if (!_infoCountry.equals(_existingCountry)) {
          return new RoomOpenHelper.ValidationResult(false, "Country(com.example.travellerfelix.data.local.model.Country).\n"
                  + " Expected:\n" + _infoCountry + "\n"
                  + " Found:\n" + _existingCountry);
        }
        final HashMap<String, TableInfo.Column> _columnsRent = new HashMap<String, TableInfo.Column>(12);
        _columnsRent.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRent.put("dayPlanId", new TableInfo.Column("dayPlanId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRent.put("rentalCompany", new TableInfo.Column("rentalCompany", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRent.put("pickupDate", new TableInfo.Column("pickupDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRent.put("dropOffDate", new TableInfo.Column("dropOffDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRent.put("pickUpTime", new TableInfo.Column("pickUpTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRent.put("dropOffTime", new TableInfo.Column("dropOffTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRent.put("pickupLocation", new TableInfo.Column("pickupLocation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRent.put("dropOffLocation", new TableInfo.Column("dropOffLocation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRent.put("carModel", new TableInfo.Column("carModel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRent.put("city", new TableInfo.Column("city", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRent.put("country", new TableInfo.Column("country", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRent = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRent = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRent = new TableInfo("rent", _columnsRent, _foreignKeysRent, _indicesRent);
        final TableInfo _existingRent = TableInfo.read(db, "rent");
        if (!_infoRent.equals(_existingRent)) {
          return new RoomOpenHelper.ValidationResult(false, "rent(com.example.travellerfelix.data.local.model.Rent).\n"
                  + " Expected:\n" + _infoRent + "\n"
                  + " Found:\n" + _existingRent);
        }
        final HashMap<String, TableInfo.Column> _columnsMuseum = new HashMap<String, TableInfo.Column>(8);
        _columnsMuseum.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuseum.put("dayPlanId", new TableInfo.Column("dayPlanId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuseum.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuseum.put("visitDate", new TableInfo.Column("visitDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuseum.put("visitTime", new TableInfo.Column("visitTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuseum.put("openCloseTime", new TableInfo.Column("openCloseTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuseum.put("city", new TableInfo.Column("city", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMuseum.put("country", new TableInfo.Column("country", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMuseum = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMuseum = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMuseum = new TableInfo("museum", _columnsMuseum, _foreignKeysMuseum, _indicesMuseum);
        final TableInfo _existingMuseum = TableInfo.read(db, "museum");
        if (!_infoMuseum.equals(_existingMuseum)) {
          return new RoomOpenHelper.ValidationResult(false, "museum(com.example.travellerfelix.data.local.model.Museum).\n"
                  + " Expected:\n" + _infoMuseum + "\n"
                  + " Found:\n" + _existingMuseum);
        }
        final HashMap<String, TableInfo.Column> _columnsTransport = new HashMap<String, TableInfo.Column>(10);
        _columnsTransport.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransport.put("dayPlanId", new TableInfo.Column("dayPlanId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransport.put("transportType", new TableInfo.Column("transportType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransport.put("ticketName", new TableInfo.Column("ticketName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransport.put("ticketDate", new TableInfo.Column("ticketDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransport.put("reservationTime", new TableInfo.Column("reservationTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransport.put("departureTime", new TableInfo.Column("departureTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransport.put("otherTransportType", new TableInfo.Column("otherTransportType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransport.put("city", new TableInfo.Column("city", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransport.put("country", new TableInfo.Column("country", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTransport = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTransport = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTransport = new TableInfo("transport", _columnsTransport, _foreignKeysTransport, _indicesTransport);
        final TableInfo _existingTransport = TableInfo.read(db, "transport");
        if (!_infoTransport.equals(_existingTransport)) {
          return new RoomOpenHelper.ValidationResult(false, "transport(com.example.travellerfelix.data.local.model.Transport).\n"
                  + " Expected:\n" + _infoTransport + "\n"
                  + " Found:\n" + _existingTransport);
        }
        final HashMap<String, TableInfo.Column> _columnsReminder = new HashMap<String, TableInfo.Column>(5);
        _columnsReminder.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminder.put("dayPlanId", new TableInfo.Column("dayPlanId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminder.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminder.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminder.put("time", new TableInfo.Column("time", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReminder = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReminder = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReminder = new TableInfo("reminder", _columnsReminder, _foreignKeysReminder, _indicesReminder);
        final TableInfo _existingReminder = TableInfo.read(db, "reminder");
        if (!_infoReminder.equals(_existingReminder)) {
          return new RoomOpenHelper.ValidationResult(false, "reminder(com.example.travellerfelix.data.local.model.Reminder).\n"
                  + " Expected:\n" + _infoReminder + "\n"
                  + " Found:\n" + _existingReminder);
        }
        final HashMap<String, TableInfo.Column> _columnsRestaurant = new HashMap<String, TableInfo.Column>(8);
        _columnsRestaurant.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurant.put("dayPlanId", new TableInfo.Column("dayPlanId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurant.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurant.put("reservationDate", new TableInfo.Column("reservationDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurant.put("reservationTime", new TableInfo.Column("reservationTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurant.put("openCloseTime", new TableInfo.Column("openCloseTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurant.put("city", new TableInfo.Column("city", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurant.put("country", new TableInfo.Column("country", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRestaurant = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRestaurant = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRestaurant = new TableInfo("restaurant", _columnsRestaurant, _foreignKeysRestaurant, _indicesRestaurant);
        final TableInfo _existingRestaurant = TableInfo.read(db, "restaurant");
        if (!_infoRestaurant.equals(_existingRestaurant)) {
          return new RoomOpenHelper.ValidationResult(false, "restaurant(com.example.travellerfelix.data.local.model.Restaurant).\n"
                  + " Expected:\n" + _infoRestaurant + "\n"
                  + " Found:\n" + _existingRestaurant);
        }
        final HashMap<String, TableInfo.Column> _columnsHotel = new HashMap<String, TableInfo.Column>(9);
        _columnsHotel.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotel.put("dayPlanId", new TableInfo.Column("dayPlanId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotel.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotel.put("checkInDate", new TableInfo.Column("checkInDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotel.put("checkOutDate", new TableInfo.Column("checkOutDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotel.put("checkInTime", new TableInfo.Column("checkInTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotel.put("checkOutTime", new TableInfo.Column("checkOutTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotel.put("city", new TableInfo.Column("city", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHotel.put("country", new TableInfo.Column("country", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHotel = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHotel = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHotel = new TableInfo("hotel", _columnsHotel, _foreignKeysHotel, _indicesHotel);
        final TableInfo _existingHotel = TableInfo.read(db, "hotel");
        if (!_infoHotel.equals(_existingHotel)) {
          return new RoomOpenHelper.ValidationResult(false, "hotel(com.example.travellerfelix.data.local.model.Hotel).\n"
                  + " Expected:\n" + _infoHotel + "\n"
                  + " Found:\n" + _existingHotel);
        }
        final HashMap<String, TableInfo.Column> _columnsNotes = new HashMap<String, TableInfo.Column>(3);
        _columnsNotes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNotes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNotes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNotes = new TableInfo("notes", _columnsNotes, _foreignKeysNotes, _indicesNotes);
        final TableInfo _existingNotes = TableInfo.read(db, "notes");
        if (!_infoNotes.equals(_existingNotes)) {
          return new RoomOpenHelper.ValidationResult(false, "notes(com.example.travellerfelix.data.local.model.Note).\n"
                  + " Expected:\n" + _infoNotes + "\n"
                  + " Found:\n" + _existingNotes);
        }
        final HashMap<String, TableInfo.Column> _columnsPlace = new HashMap<String, TableInfo.Column>(10);
        _columnsPlace.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlace.put("dayPlanId", new TableInfo.Column("dayPlanId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlace.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlace.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlace.put("openTime", new TableInfo.Column("openTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlace.put("closeTime", new TableInfo.Column("closeTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlace.put("latitude", new TableInfo.Column("latitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlace.put("longitude", new TableInfo.Column("longitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlace.put("city", new TableInfo.Column("city", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlace.put("country", new TableInfo.Column("country", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlace = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlace = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlace = new TableInfo("place", _columnsPlace, _foreignKeysPlace, _indicesPlace);
        final TableInfo _existingPlace = TableInfo.read(db, "place");
        if (!_infoPlace.equals(_existingPlace)) {
          return new RoomOpenHelper.ValidationResult(false, "place(com.example.travellerfelix.data.local.model.Place).\n"
                  + " Expected:\n" + _infoPlace + "\n"
                  + " Found:\n" + _existingPlace);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "af1341c138166c9da9140e20a26a4a40", "b331098970ae65d755bdb55795dda3e7");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "city","day_plan","Country","rent","museum","transport","reminder","restaurant","hotel","notes","place");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `city`");
      _db.execSQL("DELETE FROM `day_plan`");
      _db.execSQL("DELETE FROM `Country`");
      _db.execSQL("DELETE FROM `rent`");
      _db.execSQL("DELETE FROM `museum`");
      _db.execSQL("DELETE FROM `transport`");
      _db.execSQL("DELETE FROM `reminder`");
      _db.execSQL("DELETE FROM `restaurant`");
      _db.execSQL("DELETE FROM `hotel`");
      _db.execSQL("DELETE FROM `notes`");
      _db.execSQL("DELETE FROM `place`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(CityDao.class, CityDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DayPlanDao.class, DayPlanDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TransportDao.class, TransportDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReminderDao.class, ReminderDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(HotelDao.class, HotelDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RentDao.class, RentDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MuseumDao.class, MuseumDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RestaurantDao.class, RestaurantDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CountryDao.class, CountryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(NoteDao.class, NoteDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PlaceDao.class, PlaceDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public CityDao cityDao() {
    if (_cityDao != null) {
      return _cityDao;
    } else {
      synchronized(this) {
        if(_cityDao == null) {
          _cityDao = new CityDao_Impl(this);
        }
        return _cityDao;
      }
    }
  }

  @Override
  public DayPlanDao dayPlanDao() {
    if (_dayPlanDao != null) {
      return _dayPlanDao;
    } else {
      synchronized(this) {
        if(_dayPlanDao == null) {
          _dayPlanDao = new DayPlanDao_Impl(this);
        }
        return _dayPlanDao;
      }
    }
  }

  @Override
  public TransportDao transportDao() {
    if (_transportDao != null) {
      return _transportDao;
    } else {
      synchronized(this) {
        if(_transportDao == null) {
          _transportDao = new TransportDao_Impl(this);
        }
        return _transportDao;
      }
    }
  }

  @Override
  public ReminderDao reminderDao() {
    if (_reminderDao != null) {
      return _reminderDao;
    } else {
      synchronized(this) {
        if(_reminderDao == null) {
          _reminderDao = new ReminderDao_Impl(this);
        }
        return _reminderDao;
      }
    }
  }

  @Override
  public HotelDao hotelDao() {
    if (_hotelDao != null) {
      return _hotelDao;
    } else {
      synchronized(this) {
        if(_hotelDao == null) {
          _hotelDao = new HotelDao_Impl(this);
        }
        return _hotelDao;
      }
    }
  }

  @Override
  public RentDao rentDao() {
    if (_rentDao != null) {
      return _rentDao;
    } else {
      synchronized(this) {
        if(_rentDao == null) {
          _rentDao = new RentDao_Impl(this);
        }
        return _rentDao;
      }
    }
  }

  @Override
  public MuseumDao museumDao() {
    if (_museumDao != null) {
      return _museumDao;
    } else {
      synchronized(this) {
        if(_museumDao == null) {
          _museumDao = new MuseumDao_Impl(this);
        }
        return _museumDao;
      }
    }
  }

  @Override
  public RestaurantDao restaurantDao() {
    if (_restaurantDao != null) {
      return _restaurantDao;
    } else {
      synchronized(this) {
        if(_restaurantDao == null) {
          _restaurantDao = new RestaurantDao_Impl(this);
        }
        return _restaurantDao;
      }
    }
  }

  @Override
  public CountryDao countryDao() {
    if (_countryDao != null) {
      return _countryDao;
    } else {
      synchronized(this) {
        if(_countryDao == null) {
          _countryDao = new CountryDao_Impl(this);
        }
        return _countryDao;
      }
    }
  }

  @Override
  public NoteDao noteDao() {
    if (_noteDao != null) {
      return _noteDao;
    } else {
      synchronized(this) {
        if(_noteDao == null) {
          _noteDao = new NoteDao_Impl(this);
        }
        return _noteDao;
      }
    }
  }

  @Override
  public PlaceDao placeDao() {
    if (_placeDao != null) {
      return _placeDao;
    } else {
      synchronized(this) {
        if(_placeDao == null) {
          _placeDao = new PlaceDao_Impl(this);
        }
        return _placeDao;
      }
    }
  }
}
