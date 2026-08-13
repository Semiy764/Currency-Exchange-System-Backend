# لیست API های سیستم صرافی (Sarafi)

## 1. احراز هویت (Auth)

```
POST   /api/auth/login                    ورود و دریافت JWT
POST   /api/auth/register-teller          ثبت‌نام کارمند صرافی (فقط ADMIN)
POST   /api/auth/register-customer        ثبت‌نام/خودثبت‌نامی مشتری (بدون نیاز به JWT)
POST   /api/auth/logout                   خروج
POST   /api/auth/refresh                  دریافت توکن جدید (در صورت پیاده‌سازی refresh token)
POST   /api/auth/change-password          تغییر رمز عبور توسط خود کاربر
POST   /api/auth/reset-password           ریست رمز توسط ادمین
```

---

## 2. مدیریت کاربران (Users)

```
GET    /api/users                         لیست همهٔ کاربران (ADMIN)
GET    /api/users/{id}                    جزئیات یک کاربر
PUT    /api/users/{id}                    ویرایش اطلاعات کاربر
POST   /api/users/{id}/deactivate         غیرفعال‌سازی کاربر (ADMIN)
POST   /api/users/{id}/activate           فعال‌سازی مجدد کاربر (ADMIN)
```

---

## 3. مدیریت ارزها (Currencies)

```
GET    /api/currencies                    لیست ارزهای فعال
POST   /api/currencies                    افزودن ارز جدید (ADMIN)
PUT    /api/currencies/{id}               ویرایش ارز (ADMIN)
POST   /api/currencies/{id}/deactivate    غیرفعال‌سازی ارز (ADMIN)
```

---

## 4. نرخ ارز (Exchange Rates)

```
GET    /api/rates                         نرخ فعلی همهٔ ارزها
POST   /api/rates                         ثبت نرخ جدید (ADMIN/TELLER)
GET    /api/rates/history/{currencyId}    تاریخچهٔ نرخ یک ارز
```

---

## 5. مشتریان (Customers)

```
GET    /api/customers                     لیست مشتریان
POST   /api/customers                     ثبت مشتری حضوری جدید (TELLER/ADMIN)
GET    /api/customers/{id}                جزئیات یک مشتری
GET    /api/customers/search?query=       جستجوی مشتری با نام یا کد ملی
```

---

## 6. کارمندان صرافی (Tellers)

```
GET    /api/tellers                       لیست کارمندان (ADMIN)
GET    /api/tellers/{id}                  جزئیات یک کارمند
```

---

## 7. تراکنش‌ها (Transactions)

```
POST   /api/transactions/buy              ثبت مستقیم خرید ارز (ADMIN/TELLER)
POST   /api/transactions/sell             ثبت مستقیم فروش ارز (ADMIN/TELLER)
GET    /api/transactions                  لیست همهٔ تراکنش‌ها (ADMIN)
GET    /api/transactions/{id}             جزئیات یک تراکنش

POST   /api/transactions/request          ثبت درخواست تراکنش توسط مشتری (وضعیت PENDING)
GET    /api/transactions/pending          لیست درخواست‌های در انتظار تأیید (ADMIN/TELLER)
POST   /api/transactions/{id}/approve     تأیید درخواست (ADMIN/TELLER)
POST   /api/transactions/{id}/reject      رد درخواست (ADMIN/TELLER)
POST   /api/transactions/{id}/cancel      لغو درخواست توسط خود مشتری (فقط در وضعیت PENDING)
GET    /api/transactions/my               تاریخچهٔ درخواست‌های خود مشتری
```

---

## 8. صندوق (Vault)

```
GET    /api/vault/balances                موجودی همهٔ ارزها در صندوق
GET    /api/vault/balances/{currencyId}   موجودی یک ارز خاص
GET    /api/vault/balances/low            ارزهایی با موجودی کمتر از حد آستانه
POST   /api/vault/deposit                 واریز دستی به صندوق (ADMIN)
POST   /api/vault/withdraw                برداشت دستی از صندوق (ADMIN)
GET    /api/vault/ledger/{currencyId}     تاریخچهٔ کامل تغییرات یک ارز
GET    /api/vault/reconcile/{currencyId}  بررسی سلامت/هماهنگی موجودی با دفتر ثبت (ADMIN)
```

---

## 9. گزارش‌گیری (Reports)

```
GET    /api/reports/daily                 گزارش تراکنش‌های روزانه
GET    /api/reports/profit-loss           گزارش سود و زیان
GET    /api/reports/vault-summary         خلاصهٔ وضعیت صندوق
```

---

## نکات دسترسی (خلاصه)

| بخش | ADMIN | TELLER | CUSTOMER |
|---|:---:|:---:|:---:|
| Auth (login/register-customer) | ✅ | ✅ | ✅ (بدون نیاز به JWT) |
| Users | ✅ | ❌ | ❌ |
| Currencies (خواندن) | ✅ | ✅ | ✅ |
| Currencies (نوشتن) | ✅ | ❌ | ❌ |
| Rates (خواندن) | ✅ | ✅ | ✅ |
| Rates (نوشتن) | ✅ | ✅ | ❌ |
| Customers | ✅ | ✅ | ❌ |
| Tellers | ✅ | ❌ | ❌ |
| Transactions (ثبت مستقیم) | ✅ | ✅ | ❌ |
| Transactions (درخواست) | ➖ | ➖ | ✅ |
| Transactions (تأیید/رد) | ✅ | ✅ | ❌ |
| Vault (خواندن) | ✅ | ✅ | ❌ |
| Vault (واریز/برداشت) | ✅ | ❌ | ❌ |
| Reports | ✅ | ❌ | ❌ |
