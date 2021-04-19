package sidev.app.course.dicoding.bab3_modul3.appcommon.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sidev.app.course.dicoding.bab3_modul3.appcommon.model.User
import sidev.app.course.dicoding.bab3_modul3.appcommon.model.UserDetail

@Database(entities = [User::class], version = 1)
abstract class UserFavDb: RoomDatabase() {
    abstract fun dao(): UserFavDao

    companion object {
        private var instace: UserFavDb?= null
        const val DB_NAME = "user_db"

        fun getInstance(ctx: Context): UserFavDb {
            if(instace == null){
                synchronized(UserFavDb::class){
                    instace = Room.databaseBuilder(
                        ctx,
                        UserFavDb::class.java,
                        "$DB_NAME.db"
                    ).build()
                }
            }
            return instace!!
        }

        fun clearInstance(){
            instace = null
        }
    }
}