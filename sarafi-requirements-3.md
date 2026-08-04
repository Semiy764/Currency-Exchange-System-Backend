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
| CUSTOMER (مشتری) | مشاهدهٔ نرخ‌ها، ثبت درخواست تراکنش (وضعیت اولیه `PENDING`)، مشاهده/لغو درخواست‌های خودش |

### 2.1. جدول خلاصهٔ دسترسی‌ها

| قابلیت | CUSTOMER | TELLER | ADMIN |
|---|:---:|:---:|:---:|
| مشاهدهٔ نرخ ارز | ✅ | ✅ | ✅ |
| ثبت درخواست تراکنش (`PENDING`) | ✅ | ➖ | ➖ |
| ثبت مستقیم تراکنش (بدون درخواست) | ❌ | ✅ | ✅ |
| مشاهدهٔ لیست درخواست‌های در انتظار | ❌ | ✅ | ✅ |
| تأیید/رد درخواست مشتری | ❌ | ✅ | ✅ |
| لغو درخواست خودش (قبل از تأیید) | ✅ | ❌ | ❌ |
| ثبت/جستجوی مشتری | ❌ | ✅ | ✅ |
| ثبت/ویرایش نرخ ارز | ❌ | ✅ | ✅ |
| تعریف ارز جدید | ❌ | ❌ | ✅ |
| مدیریت کاربران | ❌ | ❌ | ✅ |
| واریز/برداشت صندوق | ❌ | ❌ | ✅ |
| لغو تراکنش تأییدشده | ❌ | ❌ | ✅ |
| گزارش سود و زیان کامل | ❌ | ❌ | ✅ |

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
- ثبت تراکنش خرید ارز از مشتری (صرافی ارز می‌خرد) توسط TELLER/ADMIN
- ثبت تراکنش فروش ارز به مشتری (صرافی ارز می‌فروشد) توسط TELLER/ADMIN
- محاسبهٔ خودکار مبلغ معادل بر اساس نرخ لحظه‌ای
- صدور رسید/فاکتور برای هر تراکنش
- امکان لغو یا اصلاح تراکنش (با ثبت لاگ تغییرات - Audit Trail)

### 3.5.1. درخواست تراکنش توسط مشتری (Customer-Initiated Request)
- مشتری می‌تواند خودش درخواست خرید یا فروش ارز ثبت کند؛ وضعیت اولیهٔ این درخواست `PENDING` است
- درخواست تا زمانی که توسط ADMIN یا TELLER تأیید نشود، اثری روی موجودی صندوق یا حساب مشتری ندارد
- **گردش کار (Workflow):**
  1. مشتری درخواست را با مبلغ و نوع (خرید/فروش) ثبت می‌کند → نرخ لحظهٔ درخواست (`requestedRate`) ذخیره می‌شود، وضعیت = `PENDING`
  2. ADMIN/TELLER لیست درخواست‌های در انتظار را می‌بیند، نرخ درخواست را با نرخ فعلی مقایسه می‌کند
  3. **تأیید:** نرخ لحظهٔ تأیید (`rateUsed`) ثبت می‌شود، وضعیت → `COMPLETED`، موجودی صندوق به‌روزرسانی می‌شود
  4. **رد:** وضعیت → `REJECTED` (مثلاً به دلیل نبود موجودی کافی یا تغییر زیاد نرخ)
- مشتری تا زمانی که درخواستش `PENDING` است می‌تواند خودش آن را لغو کند → وضعیت `CANCELLED`
- **نکتهٔ مهم دربارهٔ نرخ:** نرخ نهایی معامله همیشه نرخ لحظهٔ تأیید (`rateUsed`) است، نه نرخ لحظهٔ درخواست؛ چون پول واقعاً در لحظهٔ تأیید جابه‌جا می‌شود. نرخ لحظهٔ درخواست فقط جهت مرجع و مقایسه برای کارمند نگهداری می‌شود

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
  amountCurrency, amountToman,
  requestedRate,            // نرخ لحظهٔ درخواست مشتری (مرجع)
  rateUsed,                 // نرخ نهایی لحظهٔ تأیید (ملاک محاسبه)
  requestedByCustomer,      // آیا درخواست از طرف خود مشتری بوده؟
  performedByUserId,        // کارمند/ادمینی که تراکنش را ثبت/تأیید کرده
  approvedByUserId,         // در حالت درخواست مشتری: تأییدکننده
  createdAt, approvedAt,
  status[PENDING/COMPLETED/REJECTED/CANCELLED]
)

VaultBalance (id, currencyId, balance, lastUpdated)

VaultLedger (id, currencyId, changeAmount, reason, createdAt, performedByUserId)
```

---

## 6. پیشنهاد API Endpoints (نسخهٔ اولیه)

```
POST   /api/auth/login            (مشترک برای همهٔ نقش‌ها: ADMIN, TELLER, CUSTOMER)
POST   /api/auth/register        (فقط ADMIN — ساخت کاربر ADMIN/TELLER)

GET    /api/currencies
POST   /api/currencies           (ADMIN)
PUT    /api/currencies/{id}      (ADMIN)

GET    /api/rates
POST   /api/rates                (ADMIN/TELLER)
GET    /api/rates/history/{currencyId}

GET    /api/customers
POST   /api/customers            (TELLER/ADMIN — ثبت پروفایل مشتری + تعیین رمز عبور اولیه در همان درخواست؛ از این پس مشتری با همین اطلاعات از /api/auth/login وارد می‌شود)
GET    /api/customers/{id}

POST   /api/transactions/buy              (ADMIN/TELLER — ثبت مستقیم)
POST   /api/transactions/sell             (ADMIN/TELLER — ثبت مستقیم)
GET    /api/transactions
GET    /api/transactions/{id}
POST   /api/transactions/{id}/cancel

POST   /api/transactions/request          (CUSTOMER — ثبت درخواست، وضعیت اولیه PENDING)
GET    /api/transactions/pending          (ADMIN/TELLER — لیست درخواست‌های در انتظار)
POST   /api/transactions/{id}/approve     (ADMIN/TELLER — تأیید با نرخ لحظهٔ تأیید)
POST   /api/transactions/{id}/reject      (ADMIN/TELLER — رد درخواست)
GET    /api/transactions/my               (CUSTOMER — تاریخچه و وضعیت درخواست‌های خودش)

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
- برای درخواست‌های ثبت‌شده توسط مشتری: عملیات `approve` باید Transactional باشد و شامل سه کار در یک تراکنش دیتابیسی: (۱) ثبت `rateUsed` و `approvedAt`/`approvedByUserId`، (۲) تغییر `status` به `COMPLETED`، (۳) به‌روزرسانی موجودی صندوق (`VaultBalance`) — تا در صورت بروز خطا، هیچ‌کدام از این سه بخشی انجام‌نشده باقی نماند.

---

## 9. تایپ فیلدهای Entity ها (Java / JPA)

بر اساس استک پیشنهادی (Java 17 + Spring Data JPA + SQLite) و الزام استفاده از `BigDecimal` برای مبالغ (بخش ۴ و ۸):

### User
| فیلد | تایپ |
|---|---|
| id | `Long` |
| username | `String` |
| passwordHash | `String` |
| role | `enum Role { ADMIN, TELLER, CUSTOMER }` |
| isActive | `boolean` |

### Currency
| فیلد | تایپ |
|---|---|
| id | `Long` |
| code | `String` (مثلاً "USD") |
| name | `String` |
| symbol | `String` |

### ExchangeRate
| فیلد | تایپ |
|---|---|
| id | `Long` |
| currencyId | `Long` (یا relation به `Currency`) |
| buyRate | `BigDecimal` |
| sellRate | `BigDecimal` |
| effectiveDate | `LocalDateTime` |
| createdBy | `Long` (userId) |

### Customer
| فیلد | تایپ |
|---|---|
| id | `Long` |
| fullName | `String` |
| nationalId | `String` (نه عددی، چون ممکن است صفر ابتدایی داشته باشد) |
| phoneNumber | `String` |

### Transaction
| فیلد | تایپ |
|---|---|
| id | `Long` |
| type | `enum TxType { BUY, SELL }` |
| currencyId | `Long` |
| customerId | `Long` |
| amountCurrency | `BigDecimal` |
| amountToman | `BigDecimal` |
| requestedRate | `BigDecimal` |
| rateUsed | `BigDecimal` |
| requestedByCustomer | `boolean` |
| performedByUserId | `Long` |
| approvedByUserId | `Long` (nullable) |
| createdAt | `LocalDateTime` |
| approvedAt | `LocalDateTime` (nullable) |
| status | `enum TxStatus { PENDING, COMPLETED, REJECTED, CANCELLED }` |

### VaultBalance
| فیلد | تایپ |
|---|---|
| id | `Long` |
| currencyId | `Long` |
| balance | `BigDecimal` |
| lastUpdated | `LocalDateTime` |

### VaultLedger
| فیلد | تایپ |
|---|---|
| id | `Long` |
| currencyId | `Long` |
| changeAmount | `BigDecimal` (می‌تواند مثبت یا منفی باشد) |
| reason | `String` یا `enum LedgerReason { DEPOSIT, WITHDRAW, TX_BUY, TX_SELL, ADJUSTMENT }` |
| createdAt | `LocalDateTime` |
| performedByUserId | `Long` |

### نکات مهم
- همه‌ی مبالغ و نرخ‌ها حتماً `BigDecimal` — نه `double`/`float`
- id ها: `Long` با `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- enum ها (`Role`, `TxType`, `TxStatus`) باید با `@Enumerated(EnumType.STRING)` map شوند نه `ORDINAL`، تا تغییر بعدی enum دیتای قبلی را خراب نکند
- تاریخ/زمان: `LocalDateTime` (نه `Date` قدیمی)
- برای رابطه‌های بین entity ها می‌توان به‌جای نگه‌داشتن فقط ID خام، از `@ManyToOne` relation به entity واقعی استفاده کرد (idiomatic‌تر برای JPA و query نویسی راحت‌تر)

---

## 10. [فاز ۲] دریافت خودکار نرخ لحظه‌ای از API خارجی

طبق روال صرافی‌های حرفه‌ای، نرخ ارز می‌تواند به‌جای ثبت کاملاً دستی، از یک منبع خارجی (API نرخ ارز، بازار interbank، یا سایت‌های نرخ بازار آزاد) دریافت و سپس با اعمال اسپرد داخلی صرافی، نهایی شود.

### 10.1. تغییرات مدل داده

**افزودن به `ExchangeRate`:**
| فیلد جدید | تایپ | توضیح |
|---|---|---|
| marketRate | `BigDecimal` (nullable) | نرخ خام دریافتی از منبع خارجی، پیش از اعمال اسپرد |
| rateSource | `enum RateSource { MANUAL, AUTO }` | آیا این رکورد دستی توسط TELLER/ADMIN ثبت شده یا خودکار از API |
| fetchedAt | `LocalDateTime` (nullable) | زمان دریافت `marketRate` از منبع خارجی |

**افزودن entity جدید — تنظیمات اسپرد:**

`SpreadConfig`
| فیلد | تایپ | توضیح |
|---|---|---|
| id | `Long` | |
| currencyId | `Long` | |
| buySpreadPercent | `BigDecimal` | درصد کسر از نرخ خام برای محاسبه‌ی `buyRate` |
| sellSpreadPercent | `BigDecimal` | درصد افزوده به نرخ خام برای محاسبه‌ی `sellRate` |
| updatedByUserId | `Long` | |
| updatedAt | `LocalDateTime` | |

### 10.2. منطق محاسبه
```
buyRate  = marketRate × (1 - buySpreadPercent / 100)
sellRate = marketRate × (1 + sellSpreadPercent / 100)
```

### 10.3. نیازمندی‌های عملکردی
- یک Scheduled Job (مثلاً هر ۵ تا ۱۵ دقیقه) نرخ خام هر ارز فعال را از API خارجی می‌گیرد و با `rateSource = AUTO` رکورد جدید `ExchangeRate` ثبت می‌کند
- ADMIN می‌تواند برای هر ارز تعیین کند که نرخش `AUTO` (خودکار از API) باشد یا `MANUAL` (فقط دستی توسط TELLER/ADMIN) — این کار به‌خصوص برای ارزهایی با بازار آزاد/غیررسمی (مثل نرخ دلار در ایران) لازم است
- در حالت `MANUAL`، Job خودکار آن ارز را نادیده می‌گیرد
- ADMIN تنظیمات اسپرد (`SpreadConfig`) هر ارز را مدیریت می‌کند
- در صورت قطعی یا خطای API خارجی، سیستم باید به آخرین نرخ معتبر (`effectiveDate` قبلی) fallback کند و خطا را لاگ کند، نه اینکه سیستم را متوقف کند
- امکان Override دستی نرخ توسط TELLER/ADMIN حتی در حالت `AUTO` باید باقی بماند (برای موارد استثنایی)

### 10.4. ملاحظات فنی
- منابع پیشنهادی: `exchangerate-api.com`, `Open Exchange Rates`, `Fixer.io`، یا برای نرخ بازار آزاد ایران، منابع محلی مرتبط
- فراخوانی API خارجی باید Timeout و Retry مناسب داشته باشد تا در صورت کندی/خطای شبکه، کل سیستم قفل نشود
- این قابلیت متعلق به فاز ۲ است و پس از تکمیل و تست کامل فاز ۱ (سیستم پایه‌ی ارز↔تومان با ثبت دستی نرخ) پیاده‌سازی می‌شود
