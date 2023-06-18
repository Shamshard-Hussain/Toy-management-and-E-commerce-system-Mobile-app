package toy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Vector;

public class DBHelper extends SQLiteOpenHelper {


    //database name
    public static final String DATABASE_NAME = "DataBase";
    private static final String TABLE1 = "UserInfo";
    private static final String TABLE2 = "Category";
    private static final String TABLE3 = "Toy";
    private static final String TABLE4 = "Cart";
    private static final String TABLE5 = "OrderTable";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table1 = "CREATE TABLE "+TABLE1+"(Id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT,Email TEXT, PNumber TEXT, Password TEXT)";
        String table2 = "CREATE TABLE "+TABLE2+"(Id INTEGER PRIMARY KEY AUTOINCREMENT, CategoryName VARCHAR)";
        String table3 = "CREATE TABLE "+TABLE3+"(Id INTEGER PRIMARY KEY AUTOINCREMENT, ToyName VARCHAR, CategoryName VARCHAR,image BLOB not null,AgeGroup VARCHAR, Price VARCHAR, Quantity VARCHAR)";
        String table4 = "CREATE TABLE "+TABLE4+"(Id INTEGER PRIMARY KEY AUTOINCREMENT, UserId VARCHAR,ToyName TEXT, CategoryName TEXT,image BLOB not null, Price VARCHAR, Quantity VARCHAR)";
        String table5 = "CREATE TABLE "+TABLE5+"(Id INTEGER PRIMARY KEY AUTOINCREMENT, ToyName VARCHAR,Quantity VARCHAR, UserName VARCHAR,Email VARCHAR, Address VARCHAR, PostalCode VARCHAR,Date VARCHAR,Status VARCHAR)";
        db.execSQL(table1);
        db.execSQL(table2);
        db.execSQL(table3);
        db.execSQL(table4);
        db.execSQL(table5);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop tables if existed
        db.execSQL("DROP TABLE IF EXISTS "+TABLE1);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE2);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE3);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE4);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE5);

        // creating tables again
        onCreate(db);
    }


    public boolean insertUser(String Username, String Email, String TpNumber, String Password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Username", Username);
        contentValues.put("Email", Email);
        contentValues.put("PNumber", TpNumber);
        contentValues.put("Password", Password);

        long result = sqLiteDatabase.insert(TABLE1, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public Boolean checkUsername(String Username) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from UserInfo where username = ?", new String [] {Username});

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkEmail(String Email) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from UserInfo where Email = ?", new String [] {Email});

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Account> ValidLogin(String UserId, String Password) {
        ArrayList<Account> userList = new ArrayList<Account>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("Select * from UserInfo where username ='" + UserId + "' and Password ='" + Password + "'", null);
            if (cursor.moveToFirst()) {
                do {
                    Account user = new Account();
                    user.setUsername(cursor.getString(0));
                    user.setEmail(cursor.getString(1));
                    user.setTpNumber(cursor.getString(2));
                    user.setPassword(cursor.getString(3));
                    userList.add(user);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userList;
    }

    public boolean insertPost(String ToyName, String CategoryName, byte[] image, String AgeGroup, String Price, String Quantity) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ToyName", ToyName);
        contentValues.put("ToyName", ToyName);
        contentValues.put("CategoryName", CategoryName);
        contentValues.put("image", image);
        contentValues.put("AgeGroup", AgeGroup);
        contentValues.put("Price", Price);
        contentValues.put("Quantity", Quantity);


        long result = sqLiteDatabase.insert(TABLE3, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addToCart(String UserId, String ToyName, String CategoryName, byte[] image, String Price, String Quantity) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("UserId", UserId);
        contentValues.put("ToyName", ToyName);
        contentValues.put("CategoryName", CategoryName);
        contentValues.put("image", image);
        contentValues.put("Price", Price);
        contentValues.put("Quantity", Quantity);


        long result = sqLiteDatabase.insert(TABLE4, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkToyName( String ToyName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from Toy where ToyName= ?", new String[] { ToyName});

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkCart(String UserId, String ToyName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from Cart where UserId= ? and ToyName= ?", new String[] {UserId, ToyName});

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Toy> searchToys(String Tcategory) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Toy> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from Toy where CategoryName = ?", new String [] {Tcategory});
        while (cursor.moveToNext()){
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String category = cursor.getString(2);
            byte[] img = cursor.getBlob(3);
            String age = cursor.getString(4);
            String price = cursor.getString(5);
            String qty = cursor.getString(6);

            Toy toy = new Toy(id, name, category,img, age, price,qty );
            arrayList.add(toy);
        }
        return arrayList;
    }

    public ArrayList<Toy> searchAllToys(String keyword) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Toy> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Toy WHERE ToyName LIKE ? OR CategoryName LIKE ? OR AgeGroup LIKE ? OR Price LIKE ?", new String [] {"%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%"});

        while (cursor.moveToNext()){
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String category = cursor.getString(2);
            byte[] img = cursor.getBlob(3);
            String age = cursor.getString(4);
            String price = cursor.getString(5);
            String qty = cursor.getString(6);

            Toy toy = new Toy(id, name, category,img, age, price,qty );
            arrayList.add(toy);
        }
        return arrayList;
    }

    public ArrayList<Toy> getPost() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Toy> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE3, null);
        while (cursor.moveToNext()){
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String category = cursor.getString(2);
            byte[] img = cursor.getBlob(3);
            String age = cursor.getString(4);
            String price = cursor.getString(5);
            String qty = cursor.getString(6);

            Toy toy = new Toy(id, name, category,img, age, price,qty );
            arrayList.add(toy);
        }
        return arrayList;
    }

    public ArrayList<Order> ViewOrder() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Order> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE5, null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String ToyName = cursor.getString(1);
            String Quantity = cursor.getString(2);
            String UserName = cursor.getString(3);
            String Email = cursor.getString(4);
            String Address = cursor.getString(5);
            String PostalCode = cursor.getString(6);
            String Date = cursor.getString(7);
            String Status = cursor.getString(8);

            Order order = new Order(id, ToyName, Quantity,UserName, Email, Address,PostalCode,Date,Status );
            arrayList.add(order);
        }
        return arrayList;
    }

    public ArrayList<Order> searchAllOrders(String keyword) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Order> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM OrderTable WHERE ToyName LIKE ? OR Quantity LIKE ? OR Email LIKE ? OR Address LIKE ? OR Date LIKE ? OR Status LIKE ? OR PostalCode LIKE ?", new String [] {"%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%"});

        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String ToyName = cursor.getString(1);
            String Quantity = cursor.getString(2);
            String UserName = cursor.getString(3);
            String Email = cursor.getString(4);
            String Address = cursor.getString(5);
            String PostalCode = cursor.getString(6);
            String Date = cursor.getString(7);
            String Status = cursor.getString(8);

            Order order = new Order(id, ToyName, Quantity,UserName ,Email, Address, PostalCode,  Date,Status );
            arrayList.add(order);
        }
        return arrayList;
    }

    public ArrayList<Cart> getCartPost(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Cart> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * from Cart where UserId = ?", new String[]{id});
        while (cursor.moveToNext()){
            int Id = cursor.getInt(0);
            String Userid = cursor.getString(1);
            String name = cursor.getString(2);
            String category = cursor.getString(3);
            byte[] img = cursor.getBlob(4);
            String price = cursor.getString(5);
            String qty = cursor.getString(6);


            Cart cat = new Cart(Id,Userid,name,category,img,price,qty);
            arrayList.add(cat);
        }
        return arrayList;
    }

    public ArrayList<Category> getCategoryPost() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Category> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE2, null);
        while (cursor.moveToNext()){
            String id = cursor.getString(0);
            String name = cursor.getString(1);

            Category cat = new Category(id,name);
            arrayList.add(cat);
        }
        return arrayList;
    }

    public boolean Category(String CategoryName ) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CategoryName", CategoryName);
        long result = sqLiteDatabase.insert(TABLE2, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Vector<String> getCategory_Name(){
        Vector<String> vecCategory=new Vector<String>();

        try{
            SQLiteDatabase db=this.getWritableDatabase();
            Cursor cursor=db.rawQuery("Select * from Category",null);
            if (cursor.moveToNext()){
                do {
                    vecCategory.add(cursor.getString(1));
                }while (cursor.moveToNext());
            }
        }

        catch (Exception ex)
        {
            ex.printStackTrace();
        }return vecCategory;
    }

    public Cursor SearchAllCategory(){
        Cursor cursor=null;
        try {
            SQLiteDatabase db=this.getWritableDatabase();
            cursor=db.rawQuery("Select CategoryName from Category",null);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }return cursor;
    }

    public Boolean RemoveCartItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Cart where Id = ?", new String[]{String.valueOf(id)});
        if (cursor.getCount() > 0) {
            long result = db.delete("Cart", "Id=?", new String[]
                    {String.valueOf(id)});
            return result != -1;
        } else {
            return false;
        }
    }

    public Boolean RemoveAllCartItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Cart where UserId = ?", new String[]{id});
        if (cursor.getCount() > 0) {
            long result = db.delete("Cart", "UserId=?", new String[]
                    {String.valueOf(id)});
            return result != -1;
        } else {
            return false;
        }
    }

    public Boolean deletedata(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Category where CategoryName = ?", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = db.delete("Category", "CategoryName=?", new String[]
                    {name});
            return result != -1;
        } else {
            return false;
        }
    }

    public Boolean deleteToy(int id ) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Toy where Id = ?", new String[]{String.valueOf(id)});
        if (cursor.getCount() > 0) {
            long result = db.delete("Toy", "Id=?", new String[]
                    {String.valueOf(id)});
            return result != -1;
        } else {
            return false;
        }
    }

    public Boolean updateToydata(int id , String ToyName, String CategoryName, byte[] image, String AgeGroup, String Price, String Quantity) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("ToyName", ToyName);
        contentValues.put("CategoryName", CategoryName);
        contentValues.put("image", image);
        contentValues.put("AgeGroup", AgeGroup);
        contentValues.put("Price", Price);
        contentValues.put("Quantity", Quantity);
        Cursor cursor=DB.rawQuery("Select*from Toy where Id=?",new String[]{String.valueOf(id)});
        if (cursor.getCount()>0) {
            long result = DB.update("Toy", contentValues, "Id=?", new String[]{String.valueOf(id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else {
            return false;
        }
    }

    public Boolean updateOrderStatus(int id,String Status) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("Status", Status);
        Cursor cursor=DB.rawQuery("Select*from OrderTable where Id=?",new String[]{String.valueOf(id)});
        if (cursor.getCount()>0) {
            long result = DB.update("OrderTable", contentValues, "Id=?", new String[]{String.valueOf(id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else {
            return false;
        }
    }
    public Boolean RemoveItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from OrderTable where Id = ?", new String[]{String.valueOf(id)});
        if (cursor.getCount() > 0) {
            long result = db.delete("OrderTable", "Id=?", new String[]
                    {String.valueOf(id)});
            return result != -1;
        } else {
            return false;
        }
    }
    public boolean insertOrder(String ToyName, String Quantity, String UserName, String Email,String Address, String PostalCode, String Date,String Status) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ToyName", ToyName);
        contentValues.put("Quantity", Quantity);
        contentValues.put("UserName", UserName);
        contentValues.put("Email", Email);
        contentValues.put("Address", Address);
        contentValues.put("PostalCode", PostalCode);
        contentValues.put("Date", Date);
        contentValues.put("Status", Status);

        long result = sqLiteDatabase.insert(TABLE5, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

}
