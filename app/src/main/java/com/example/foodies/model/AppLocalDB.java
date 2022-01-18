package com.example.foodies.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.foodies.MyApplication;

@Database(entities = {Post.class}, version = 4)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao PostDao();
}

public class AppLocalDB {
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
