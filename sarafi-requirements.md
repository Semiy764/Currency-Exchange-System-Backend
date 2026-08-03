# مستند نیازمندی‌های سیستم صرافی (Currency Exchange Backend)

## 1. معرفی و هدف پروژه
هدف، ساخت یک بک‌اند برای سیستم صرافی است که امکان مدیریت خرید و فروش ارز، مدیریت مشتریان، مدیریت نرخ ارز، و ثبت تراکنش‌ها را فراهم کند. این سیستم روی کامپیوتر لوکال اجرا و توسعه داده می‌شود.

**استک پیشنهادی (قابل تغییر):**
- Java 17+
- Spring Boot (Web, Security, Data JPA)
- JWT برای احراز هویت
- SQLite برای دیتابیس (سبک و مناسب اجرای لوکال)
- Maven یا Gradle برای مدیریت پروژه
- Swagger/OpenAPI برای مستندسازی API (اختیاری ولی توصیه‌شده)

---

## 2. نقش‌های کاربری (Roles)

| نقش | توضیح |
|---|---|
| ADMIN | مدیریت کامل سیستم: کاربران، نرخ ارزها، تنظیمات |
| TELLER (صراف/کارمند باجه) | ثبت تراکنش‌های خرید/فروش ارز برای مشتریان |
| CUSTOMER (مشتری) | مشاهده نرخ‌ها، تاریخچه تراکنش‌های خودش (اختیاری - اگر پنل مشتری هم می‌خواهی) |

---

## 3. نیازمندی‌های عملکردی (Functional Requirements)

### 3.1. احراز هویت و مدیریت کاربران
- ثبت‌نام و ورود کاربران (ADMIN / TELLER) با JWT
- مدیریت نقش‌ها و سطح دسترسی (Role-based access control)
- تغییر رمز عبور، غیرفعال‌سازی کاربر توسط ادمین

### 3.2. مدیریت ارزها (Currency Management)
- افزودن/ویرایش/حذف ارزهای پشتیبانی‌شده (مثلاً USD, EUR, TRY, AED, ...)
- هر ارز شامل: کد ارز (ISO)، نام، نماد

### 3.3. مدیریت نرخ ارز (Exchange Rate Management)
- ثبت نرخ خرید و فروش برای هر ارز (نسبت به ریال/تومان)
- به‌روزرسانی نرخ‌ها توسط ادمین یا صراف مجاز
- نگهداری تاریخچهٔ نرخ‌ها (برای گزارش‌گیری و بررسی نوسانات)

### 3.4. مدیریت مشتریان (Customer Management)
- ثبت اطلاعات مشتری (نام، کد ملی/شماره تماس، و در صورت نیاز مدارک هویتی برای الزامات قانونی AML/KYC)
- جستجوی مشتری بر اساس نام یا کد ملی

### 3.5. عملیات خرید و فروش ارز (Core Transaction)
- ثبت تراکنش خرید ارز از مشتری (صرافی ارز می‌خرد)
- ثبت تراکنش فروش ارز به مشتری (صرافی ارز می‌فروشد)
- محاسبهٔ خودکار مبلغ معادل بر اساس نرخ لحظه‌ای
- صدور رسید/فاکتور برای هر تراکنش
- امکان لغو یا اصلاح تراکنش (با ثبت لاگ تغییرات - Audit Trail)

### 3.6. مدیریت صندوق و موجودی (Cash/Vault Management)
- پیگیری موجودی هر ارز در صندوق صرافی
- ثبت واریز/برداشت به صندوق (تزریق نقدینگی)
- هشدار در صورت کمبود موجودی یک ارز خاص

### 3.7. گزارش‌گیری (Reporting)
- گزارش تراکنش‌های روزانه/ماهانه
- گزارش سود و زیان بر اساس اختلاف نرخ خرید و فروش
- گزارش موجودی لحظه‌ای صندوق به تفکیک ارز
- خروجی گزارش‌ها (مثلاً CSV/Excel)

### 3.8. لاگ و امنیت
- ثبت لاگ عملیات حساس (ورود، تغییر نرخ، تراکنش‌های بزرگ)
- محدودسازی دسترسی بر اساس نقش

---

## 4. نیازمندی‌های غیرعملکردی (Non-Functional Requirements)

- **امنیت:** رمزنگاری پسورد (BCrypt)، احراز هویت با JWT، اعتبارسنجی ورودی‌ها
- **کارایی:** پاسخ‌گویی سریع برای عملیات ثبت تراکنش (زیر ۱ ثانیه در محیط لوکال)
- **قابلیت نگهداری:** کد تمیز، لایه‌بندی‌شده (Controller / Service / Repository / DTO)
- **قابلیت توسعه:** امکان افزودن ارز جدید یا نقش جدید بدون تغییر ساختار اصلی
- **صحت داده مالی:** استفاده از `BigDecimal` برای همهٔ محاسبات مالی (نه double/float) تا خطای گرد کردن اعداد رخ ندهد
- **Backup:** امکان پشتیبان‌گیری ساده از فایل دیتابیس SQLite

---

## 5. مدل داده پیشنهادی (Entities)

```
User (id, username, passwordHash, role, isActive)

Currency (id, code[USD/EUR/...], name, symbol)

ExchangeRate (id, currencyId, buyRate, sellRate, effectiveDate, createdBy)

Customer (id, fullName, nationalId, phoneNumber)

Transaction (
  id, type[BUY/SELL], currencyId, customerId,
  amountCurrency, amountToman, rateUsed,
  performedByUserId, createdAt, status[COMPLETED/CANCELLED]
)

VaultBalance (id, currencyId, balance, lastUpdated)

VaultLedger (id, currencyId, changeAmount, reason, createdAt, performedByUserId)
```

---

## 6. پیشنهاد API Endpoints (نسخهٔ اولیه)

```
POST   /api/auth/login
POST   /api/auth/register        (فقط ADMIN)

GET    /api/currencies
POST   /api/currencies           (ADMIN)
PUT    /api/currencies/{id}      (ADMIN)

GET    /api/rates
POST   /api/rates                (ADMIN/TELLER)
GET    /api/rates/history/{currencyId}

GET    /api/customers
POST   /api/customers
GET    /api/customers/{id}

POST   /api/transactions/buy
POST   /api/transactions/sell
GET    /api/transactions
GET    /api/transactions/{id}
POST   /api/transactions/{id}/cancel

GET    /api/vault/balances
POST   /api/vault/deposit
POST   /api/vault/withdraw

GET    /api/reports/daily
GET    /api/reports/profit-loss
```

---

## 7. مراحل پیشنهادی پیاده‌سازی (Roadmap)

1. **راه‌اندازی اولیه پروژه:** Spring Boot + SQLite + ساختار لایه‌بندی‌شده
2. **پیاده‌سازی احراز هویت:** User entity + JWT + Login/Register
3. **مدیریت ارز و نرخ:** Currency + ExchangeRate CRUD
4. **مدیریت مشتری:** Customer CRUD
5. **هستهٔ اصلی - تراکنش‌ها:** Transaction (خرید/فروش) + محاسبهٔ خودکار + به‌روزرسانی موجودی صندوق
6. **مدیریت صندوق:** VaultBalance + VaultLedger
7. **گزارش‌گیری:** Endpointهای گزارش
8. **تست:** یونیت‌تست برای منطق محاسباتی (خصوصاً تراکنش‌ها و نرخ‌ها)
9. **مستندسازی API:** Swagger

---

## 8. نکات مهم فنی
- برای همهٔ مبالغ حتماً از `BigDecimal` استفاده کن، نه `double`.
- هر تراکنش باید به‌صورت Transactional (`@Transactional`) باشد تا اگر خطایی رخ داد، موجودی صندوق ناهماهنگ نشود.
- نرخ استفاده‌شده در هر تراکنش باید در خود رکورد تراکنش ذخیره شود (نه فقط ارجاع به نرخ فعلی)، چون نرخ‌ها تغییر می‌کنند و باید تاریخچهٔ دقیق تراکنش حفظ شود.
