package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.SitaraMartDao
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Dealer::class,
        Product::class,
        DealerProductRate::class,
        PurchaseEntry::class,
        BannerConfig::class,
        InventoryAlert::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SitaraMartDatabase : RoomDatabase() {
    abstract fun dao(): SitaraMartDao

    companion object {
        @Volatile
        private var INSTANCE: SitaraMartDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): SitaraMartDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SitaraMartDatabase::class.java,
                    "sitara_mart_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.dao())
                    }
                }
            }
        }

        suspend fun populateInitialData(dao: SitaraMartDao) {
            // Seed Dealers
            val d1 = dao.insertDealer(
                Dealer(
                    name = "Ali Suppliers",
                    urduName = "علی سپلائرز",
                    contactPerson = "حاجی علی احمد",
                    phone = "03001234567",
                    whatsapp = "923001234567",
                    address = "دکان نمبر 14، غلہ منڈی، لاہور",
                    categories = "چینی، چاول، گھی",
                    balanceDue = 45000.0,
                    rating = 4.9f
                )
            )
            val d2 = dao.insertDealer(
                Dealer(
                    name = "Bilal Suppliers",
                    urduName = "بلال سپلائرز",
                    contactPerson = "ملک بلال حسین",
                    phone = "03219876543",
                    whatsapp = "923219876543",
                    address = "پلاٹ 22، اکبری منڈی، لاہور",
                    categories = "چاول، دالیں، چنے",
                    balanceDue = 12500.0,
                    rating = 4.8f
                )
            )
            val d3 = dao.insertDealer(
                Dealer(
                    name = "Ahmed Traders",
                    urduName = "احمد ٹریڈرز",
                    contactPerson = "شیخ احمد نواز",
                    phone = "03335551234",
                    whatsapp = "923335551234",
                    address = "مارکیٹ گیٹ، غلہ منڈی، گوجرانوالہ",
                    categories = "دالیں، مصالحہ جات",
                    balanceDue = 0.0,
                    rating = 4.7f
                )
            )
            val d4 = dao.insertDealer(
                Dealer(
                    name = "Tariq Oil Mills",
                    urduName = "طارق آئل ملز",
                    contactPerson = "طارق محمود",
                    phone = "03124448888",
                    whatsapp = "923124448888",
                    address = "انڈسٹریل اسٹیٹ، فیصل آباد",
                    categories = "کوکنگ آئل، بناسپتی گھی",
                    balanceDue = 80000.0,
                    rating = 4.9f
                )
            )
            val d5 = dao.insertDealer(
                Dealer(
                    name = "Punjab Flour Mills",
                    urduName = "پنجاب فلور ملز",
                    contactPerson = "چوہدری اکرم",
                    phone = "03017779999",
                    whatsapp = "923017779999",
                    address = "ملتان روڈ، لاہور",
                    categories = "آٹا، میدہ، سوجی",
                    balanceDue = 30000.0,
                    rating = 4.6f
                )
            )

            // Seed Products
            val p1 = dao.insertProduct(
                Product(
                    name = "Sugar",
                    urduName = "چینی",
                    category = "کریانہ",
                    unit = "فی کلو",
                    currentStock = 85.0, // Low stock alert! (Threshold 100)
                    minStockAlert = 100.0,
                    imageResName = "thumb_sugar",
                    description = "ریفائنڈ وائٹ شوگر گریڈ اے 50 کلو بوری"
                )
            )
            val p2 = dao.insertProduct(
                Product(
                    name = "Rice Super Basmati",
                    urduName = "چاول",
                    category = "کریانہ",
                    unit = "فی کلو",
                    currentStock = 240.0,
                    minStockAlert = 120.0,
                    imageResName = "thumb_rice",
                    description = "سپر باسمتی کرنل چاول، پرانا اسٹاک"
                )
            )
            val p3 = dao.insertProduct(
                Product(
                    name = "Banaspati Ghee",
                    urduName = "گھی",
                    category = "آئل و گھی",
                    unit = "فی کلو",
                    currentStock = 45.0, // Low stock
                    minStockAlert = 60.0,
                    imageResName = "",
                    description = "درجہ اول بناسپتی گھی 16 کلو ٹن پیکنگ"
                )
            )
            val p4 = dao.insertProduct(
                Product(
                    name = "Wheat Flour Atta",
                    urduName = "آٹا",
                    category = "فلور ملز",
                    unit = "فی کلو",
                    currentStock = 180.0,
                    minStockAlert = 100.0,
                    imageResName = "",
                    description = "چکی کا خالص فائن گندم آٹا 20 کلو تھیلا"
                )
            )
            val p5 = dao.insertProduct(
                Product(
                    name = "Daal Chana",
                    urduName = "دال چنا",
                    category = "دالیں",
                    unit = "فی کلو",
                    currentStock = 90.0,
                    minStockAlert = 50.0,
                    imageResName = "",
                    description = "موٹی اسپیشل دال چنا صاف شدہ"
                )
            )
            val p6 = dao.insertProduct(
                Product(
                    name = "Cooking Oil",
                    urduName = "کوکنگ آئل",
                    category = "آئل و گھی",
                    unit = "فی لیٹر",
                    currentStock = 35.0, // Low stock
                    minStockAlert = 50.0,
                    imageResName = "",
                    description = "پریمیم کینولا و سویا بین آئل پاؤچ"
                )
            )

            // Seed Rates (Matching Reference UI: Sugar 155 prev 150 +5, Rice 142 prev 145 -3)
            dao.insertRate(
                DealerProductRate(
                    dealerId = d1,
                    productId = p1,
                    currentRate = 155.0,
                    previousRate = 150.0,
                    notes = "تازہ نرخ نامہ"
                )
            )
            dao.insertRate(
                DealerProductRate(
                    dealerId = d2,
                    productId = p1,
                    currentRate = 158.0,
                    previousRate = 154.0,
                    notes = "نقد ادائیگی"
                )
            )
            dao.insertRate(
                DealerProductRate(
                    dealerId = d2,
                    productId = p2,
                    currentRate = 142.0,
                    previousRate = 145.0,
                    notes = "کوالٹی گارنٹی"
                )
            )
            dao.insertRate(
                DealerProductRate(
                    dealerId = d1,
                    productId = p2,
                    currentRate = 146.0,
                    previousRate = 148.0,
                    notes = "50 کلو بوری"
                )
            )
            dao.insertRate(
                DealerProductRate(
                    dealerId = d4,
                    productId = p3,
                    currentRate = 480.0,
                    previousRate = 470.0,
                    notes = "16 کلو ٹن"
                )
            )
            dao.insertRate(
                DealerProductRate(
                    dealerId = d5,
                    productId = p4,
                    currentRate = 120.0,
                    previousRate = 120.0,
                    notes = "سرکاری ریٹ"
                )
            )
            dao.insertRate(
                DealerProductRate(
                    dealerId = d3,
                    productId = p5,
                    currentRate = 260.0,
                    previousRate = 270.0,
                    notes = "ڈسکاؤنٹ آفر"
                )
            )
            dao.insertRate(
                DealerProductRate(
                    dealerId = d4,
                    productId = p6,
                    currentRate = 510.0,
                    previousRate = 500.0,
                    notes = "پیکنگ 5 لیٹر"
                )
            )

            // Seed Recent Purchases
            dao.insertPurchase(
                PurchaseEntry(
                    invoiceNo = "SM-9021",
                    dealerId = d1,
                    productId = p1,
                    quantity = 500.0,
                    rate = 155.0,
                    totalAmount = 77500.0,
                    status = "Delivered",
                    notes = "10 بوریاں وصول ہوئیں"
                )
            )
            dao.insertPurchase(
                PurchaseEntry(
                    invoiceNo = "SM-9020",
                    dealerId = d2,
                    productId = p2,
                    quantity = 400.0,
                    rate = 142.0,
                    totalAmount = 56800.0,
                    status = "Delivered",
                    notes = "سپر کرنل چاول"
                )
            )
            dao.insertPurchase(
                PurchaseEntry(
                    invoiceNo = "SM-9022",
                    dealerId = d4,
                    productId = p3,
                    quantity = 160.0,
                    rate = 480.0,
                    totalAmount = 76800.0,
                    status = "Pending",
                    notes = "آرڈر بھیج دیا گیا ہے، ڈیلیوری متوقع"
                )
            )

            // Seed Banner Config
            dao.insertOrUpdateBanner(
                BannerConfig(
                    id = 1,
                    isVisible = true,
                    titleUrdu = "آج کی خریداری\nآسان بنائیں",
                    subtitleUrdu = "ریٹ دیکھیں • ڈیلر تلاش کریں",
                    imageUrl = "grocery_basket",
                    highlightTag = "سپلائر ریٹس"
                )
            )

            // Seed Inventory Alerts
            dao.insertAlert(
                InventoryAlert(
                    title = "اسٹاک کی کمی کا الرٹ!",
                    message = "چینی کا اسٹاک 85 کلو رہ گیا ہے۔ فوری سپلائر علی سے نیا اسٹاک منگوائیں۔",
                    alertType = "LOW_STOCK"
                )
            )
            dao.insertAlert(
                InventoryAlert(
                    title = "ریٹ میں اضافہ: چینی +PKR 5",
                    message = "علی سپلائرز نے چینی کا ریٹ 150 سے بڑھا کر 155 کر دیا ہے۔",
                    alertType = "RATE_HIKE"
                )
            )
            dao.insertAlert(
                InventoryAlert(
                    title = "ریٹ میں کمی: چاول -PKR 3",
                    message = "بلال سپلائرز نے چاول کا ریٹ 145 سے کم کر کے 142 کر دیا ہے۔ خریداری کے لیے موزوں وقت!",
                    alertType = "RATE_DROP"
                )
            )
        }
    }
}
