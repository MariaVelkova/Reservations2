package bg.mentormate.academy.reservations.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Student16 on 2/5/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, DBConstants.DB_NAME, null, DBConstants.DB_VERSION);
        DBConstants.init();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (int i = 0; i < DBConstants.DB_TABLES.length; i++) {
            //Log.d("table", DBConstants.DB_TABLES[i]);
            //Log.d("query",DBConstants.create_tables.get(DBConstants.DB_TABLES[i]));
            db.execSQL(DBConstants.create_tables.get(DBConstants.DB_TABLES[i]));
        }

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        long time = cal.getTimeInMillis();
//        db.execSQL("INSERT INTO cities(`name`) VALUES('Sofia'),('Plovdiv'),('Varna'),('Burgas'),('Rousse'),('Stara Zagora')," +
//                "('Pleven'),('Sliven'),('Dobrich'),('Pernik'),('Shumen'),('Haskovo'),('Yambol'),('Pazardzhik'),('Blagoevgrad')," +
//                "('Veliko Tarnovo'),('Vratsa'),('Gabrovo'),('Asenovgrad'),('Vidin'),('Kazanlak')");
//        db.execSQL("INSERT INTO users(`type`,`email`,`password`,`first_name`,`last_name`,`phone`,`city`) VALUES" +
//                "(2,'welkowa@gmail.com','1234','Maria','Velkova','+359 887 767 705',1)");
//        db.execSQL("INSERT INTO VENUES(`name`,`type`,`city`,`address`,`lat`,`lon`,`phone`,`worktime`,`capacity`,`owner_id`,`created`,`last_updated`) VALUES" +
//                "('Tabiet',1,1,'Lozenetz, 76 James Boucher',42.6726231853202,23.3204984664917,'+359 2 969 696','12:00 - 24:00',150,1," + time + ", " + time + ")," +
//                "('The Med',1,1,'Motopista, Bulgaria boulevard 49',42.66724316336978,23.29055428504944,'+359 877 721 271','12:00 - 24:00',80,1," + time + ", " + time + ")," +
//                "('Genacvale',1,1,'Center, 62 Maria Luiza blvd.',42.705666180070644,23.324344754219055,'+359 885 968 888','11:00 - 23:00',80,1," + time + ", " + time + ")," +
//                "('Bavaria',1,1,'Gevgelijski, 11 Popchevo str.',42.709281,23.281633,'+359 893 332 404','10:00 - 24:00',130,1," + time + ", " + time + ")," +
//                "('Il Viaggio Ristorante',1,1,'Boiana, 116 Aleksander Pushkin Blvd.',42.650042409343854,23.26571099460125,'+359 876 767 647','11:30 - 24:00 / Monday Off /',0,1," + time + ", " + time + ")," +
//                "('Ascua',1,1,'Center, 1 Racho Dimchev Str',42.69072014818213,23.323341608047485,'+359 882 880 901','11:30 - 23:30',45,1," + time + ", " + time + ")," +
//                "('Petleto',1,1,'Center, ul. Neofit Rilski 44',42.68988028813314,23.32144260406494,'+359 876 300 700','11:00 - 23:00',80,1," + time + ", " + time + ")," +
//                "('Intrigue',1,1,'Center, blv.Vitosha 102',42.6861421817153,23.3173656463623,'+359 892 222 244','10:00 - 01:00',60,1," + time + ", " + time + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ". Old data will be destroyed");
        for (int i = 0; i < DBConstants.DB_TABLES.length; i++) {
            db.execSQL("DROP TABLE IF EXISTS " +  DBConstants.DB_TABLES[i]);
        }
        onCreate(db);
    }
}