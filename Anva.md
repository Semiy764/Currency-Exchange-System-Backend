# فیلدهای `performedByUserId` و `approvedByUserId` در Entity تراکنش

## تفاوت این دو فیلد

| فیلد | یعنی چی؟ |
|---|---|
| `performedByUserId` | چه کسی تراکنش را **ثبت اولیه** کرده |
| `approvedByUserId` | چه کسی درخواست `PENDING` را **تأیید نهایی** کرده |

---

## جدول nullability بر اساس سناریو

| سناریو | status | `performedByUserId` | `approvedByUserId` |
|---|---|---|---|
| ثبت مستقیم توسط کارمند | `COMPLETED` | ✅ پر (همون کارمند) | ❌ NULL |
| درخواست مشتری — هنوز تأیید نشده | `PENDING` | ❌ NULL | ❌ NULL |
| درخواست مشتری — تأیید شده | `COMPLETED` | ❌ NULL یا = همون approver | ✅ پر (کارمند تأییدکننده) |
| درخواست مشتری — رد شده | `REJECTED` | ❌ NULL | ✅ پر (کارمندی که رد کرد) |
| درخواست مشتری — خودش لغو کرده | `CANCELLED` | ❌ NULL | ❌ NULL |

---

## تعریف ستون‌ها (SQLite)

```sql
performed_by_user_id INTEGER,   -- بدون NOT NULL یعنی nullable
approved_by_user_id  INTEGER,   -- همینطور
```

## تعریف فیلدها (JPA Entity)

```java
@Column(name = "performed_by_user_id", nullable = true)
private Long performedByUserId;

@Column(name = "approved_by_user_id", nullable = true)
private Long approvedByUserId;
```

---

## قانون منطقی (Business Rule) لازم در لایهٔ Service

هیچ‌کدام از این دو فیلد در دیتابیس اجباری نیستند، پس باید در سرویس تضمین شود حالت نامعتبر ساخته نشود:

```
status = COMPLETED  &&  performedByUserId = NULL  &&  approvedByUserId = NULL
```
این حالت باید غیرمجاز باشد — یعنی یک تراکنش تکمیل‌شده باید حداقل یک مسئول (ثبت‌کننده یا تأییدکننده) داشته باشد.

```java
public void validateTransactionIntegrity(Transaction t) {
    if (t.getStatus() == TransactionStatus.COMPLETED) {
        boolean hasPerformer = t.getPerformedByUserId() != null;
        boolean hasApprover = t.getApprovedByUserId() != null;
        if (!hasPerformer && !hasApprover) {
            throw new IllegalStateException(
                "تراکنش تکمیل‌شده باید حداقل یک مسئول (ثبت‌کننده یا تأییدکننده) داشته باشد");
        }
    }
}
```

**جمع‌بندی:** همیشه حداقل یکی از این دو فیلد باید پر باشد وقتی تراکنش `COMPLETED` یا `REJECTED` است.
